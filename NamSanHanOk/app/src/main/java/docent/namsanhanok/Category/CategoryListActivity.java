package docent.namsanhanok.Category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.codesgood.views.JustifiedTextView;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import docent.namsanhanok.Application;
import docent.namsanhanok.Docent.DocentData;
import docent.namsanhanok.Docent.DocentResult;
import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.Manager.DocentMemList;
import docent.namsanhanok.NetworkService;
import docent.namsanhanok.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListActivity extends Activity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CategoryListAdapter categoryListAdapter;

    ImageButton homeBtn;
    TextView category_list_toolbar_title;
    //    String category_title;
    ImageView simple_image;
    TextView category_list_title;
    TextView countText;
    TextView category_text_info;


    private NetworkService service;
    private ArrayList<DocentData> docentDataList;
    private ArrayList<CategoryData> categoryDataList;

    private Application applicationclass;
    DocentMemList docentMemList;
    CategoryData categoryData;
    DocentData docentData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Intent secondIntent = getIntent();
        categoryData = (CategoryData) secondIntent.getSerializableExtra("category");
        service = Application.getInstance().getNetworkService();

        init();

        setCategoryContent();
        setDocentList();

    }

    public void setCategoryContent() {
        //카테고리 대표 이미지 설정
        Glide.with(CategoryListActivity.this)
                .load(Environment.getExternalStorageDirectory() + categoryData.category_image_url)
                .into(simple_image);

        //카테고리 설명
        String content = categoryData.category_detail_info;
        category_text_info.setText(content);

    }

    public void setDocentList() {
        HashMap<String, DocentData> map = new HashMap<String, DocentData>();
        docentMemList.get_docent_info(categoryData.category_id, map);
        docentDataList = new ArrayList<>();

        Iterator<String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            Log.d("check1", "key값 " + key);
            Log.d("check1", "map.get(key) " + map.get(key));
            docentData = map.get(key);
            docentDataList.add(docentData);
        }

        categoryListAdapter.setAdapter(docentDataList);
        String count = "전시품 총 " + String.valueOf(docentDataList.size()) + "개";
        countText.setText(count);
    }


    public void init() {
        applicationclass = (Application) getApplicationContext();
        docentMemList = DocentMemList.getInstance();

        category_list_toolbar_title = (TextView) findViewById(R.id.docentTitle);
        Toolbar categoryListToolbar = (Toolbar) findViewById(R.id.category_list_Toolbar);
        categoryListToolbar.bringToFront();

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        category_list_toolbar_title.setText(categoryData.category_title);

        recyclerView = (RecyclerView) findViewById(R.id.category_list_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        categoryListAdapter = new CategoryListAdapter(getApplicationContext(), docentDataList);
        recyclerView.setAdapter(categoryListAdapter);

        simple_image = (ImageView) findViewById(R.id.simple_image);

        category_list_title = (TextView) findViewById(R.id.category_list_title);
        category_list_title.setText(categoryData.category_title + " 소개");
        countText = (TextView) findViewById(R.id.countText);

        category_text_info = (TextView) findViewById(R.id.category_text_info);


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
