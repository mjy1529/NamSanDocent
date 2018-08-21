package docent.namsanhanok.Category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import docent.namsanhanok.Application;
import docent.namsanhanok.Docent.DocentActivity;
import docent.namsanhanok.Docent.DocentData;
import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.Manager.DocentMemList;
import docent.namsanhanok.Manager.IDInfoData;
import docent.namsanhanok.NetworkService;
import docent.namsanhanok.R;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class CategoryListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CategoryListAdapter categoryListAdapter;

    ImageButton homeBtn;
    TextView category_list_toolbar_title;
    ImageView simple_image;
    TextView category_list_title;
    TextView countText;
    TextView category_text_info;

    private NetworkService service;
    private ArrayList<DocentData> docentDataList;

    DocentMemList docentMemList;
    CategoryData categoryData;
    DocentData docentData;

    PrettyDialog newItemDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Intent secondIntent = getIntent();
        categoryData = (CategoryData) secondIntent.getSerializableExtra("category");
        service = Application.getInstance().getNetworkService();

        init();

        setCategoryContent(categoryData);
        setDocentList(categoryData);

        // *** 테스트용 코드 : 비콘알림코드 추가 후 삭제 *** //
        IDInfoData idInfoData = new IDInfoData();
        idInfoData.category_id = "2";
        idInfoData.docent_id = "4";
        // ******************************** //

        showNewItemDialog(idInfoData);
    }

    public void setCategoryContent(CategoryData categoryData) {
        //카테고리 대표 이미지 설정
        Glide.with(CategoryListActivity.this)
                .load(Environment.getExternalStorageDirectory() + categoryData.category_image_url)
                .into(simple_image);

        category_list_toolbar_title.setText(categoryData.category_title); //툴바 타이틀
        category_list_title.setText(categoryData.category_title + " 소개"); //소개 타이틀
        category_text_info.setText(categoryData.category_detail_info); //카테고리 설명
    }

    public void setDocentList(CategoryData categoryData) {
        HashMap<String, DocentData> map = new HashMap<String, DocentData>();
        docentMemList.get_docent_info(categoryData.category_id, map);
        docentDataList = new ArrayList<>();

        Iterator<String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            docentData = map.get(key);
            docentDataList.add(docentData);
        }

        categoryListAdapter.setAdapter(docentDataList);
        String count = "전시품 총 " + String.valueOf(docentDataList.size()) + "개";
        countText.setText(count);
    }

    public void showNewItemDialog(final IDInfoData idInfoData) {
        newItemDialog = new PrettyDialog(CategoryListActivity.this);
        newItemDialog.setMessage(getResources().getString(R.string.newItemAlertMessage))
                .setIcon(R.drawable.pdlg_icon_info)
                .setIconTint(R.color.pdlg_color_blue)
                .addButton("확인", // button text
                        R.color.pdlg_color_white,  // button text color
                        R.color.pdlg_color_blue,  // button background color
                        new PrettyDialogCallback() {  // button OnClick listener
                            @Override
                            public void onClick() {
                                if (!idInfoData.category_id.equals("")) {
                                    if (idInfoData.docent_id.equals("")) {
                                        CategoryData categoryData = new CategoryData();
                                        docentMemList.get_category_info(idInfoData.category_id, categoryData);

                                        setCategoryContent(categoryData);
                                        setDocentList(categoryData);

                                    } else if (!idInfoData.docent_id.equals("")) {
                                        Intent intent = new Intent(CategoryListActivity.this, DocentActivity.class);

                                        HashMap<String, DocentData> map = new HashMap<>();
                                        docentMemList.get_docent_info(idInfoData.category_id, map);
                                        DocentData docentData = map.get(idInfoData.docent_id);

                                        intent.putExtra("docentObject", docentData);
                                        startActivity(intent);
                                    }
                                    newItemDialog.dismiss();
                                }
                            }
                        }
                )
                .addButton("취소", // button text
                        R.color.pdlg_color_white,  // button text color
                        R.color.dialog_cancel,  // button background color
                        new PrettyDialogCallback() {  // button OnClick listener
                            @Override
                            public void onClick() {
                                newItemDialog.dismiss();
                            }
                        }
                );
        newItemDialog.show();
    }

    public void init() {
        docentMemList = DocentMemList.getInstance();

        Toolbar categoryListToolbar = (Toolbar) findViewById(R.id.category_list_Toolbar);
        setSupportActionBar(categoryListToolbar);

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        simple_image = (ImageView) findViewById(R.id.simple_image);
        category_list_title = (TextView) findViewById(R.id.category_list_title);
        countText = (TextView) findViewById(R.id.countText);
        category_text_info = (TextView) findViewById(R.id.category_text_info);
        category_list_toolbar_title = (TextView) findViewById(R.id.docentTitle);

        recyclerView = (RecyclerView) findViewById(R.id.category_list_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        categoryListAdapter = new CategoryListAdapter(getApplicationContext(), docentDataList);
        recyclerView.setAdapter(categoryListAdapter);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeBtn:
                Intent intent = new Intent(CategoryListActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onPause() {

        super.onPause();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

            }

        }, 100);
    }
}
