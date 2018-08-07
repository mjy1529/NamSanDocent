package docent.namsanhanok.Category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.Locale;

import docent.namsanhanok.Application;
import docent.namsanhanok.Docent.DocentData;
import docent.namsanhanok.Docent.DocentResult;
import docent.namsanhanok.Home.HomeActivity;
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
    String category_title;
    ImageView simple_image;
    TextView category_list_title;
    TextView countText;
    JustifiedTextView category_text_info;

    private NetworkService service;
    private ArrayList<DocentData> docentDataList;
    private ArrayList<CategoryData> categoryDataList;


    int category_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Intent secondIntent = getIntent();
        category_title = secondIntent.getExtras().getString("cate_title");
        category_id = secondIntent.getExtras().getInt("cate_id");
        service = Application.getInstance().getNetworkService();

        init();

        networking();
        networking2();
    }

    private String getCategoryInfo(String cmd) {
        String json = "";
        try {
            JSONObject data = new JSONObject();
            data.put("cmd",cmd);


            JSONObject root = new JSONObject();
            root.put("info", data);
            json = root.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    private String getDocentListInfo(String cmd, int cate_id) {
        String json = "";
        try {
            JSONObject data = new JSONObject();
            data.put("cmd",cmd);
            data.put("category_id",cate_id);

            JSONObject root = new JSONObject();
            root.put("info", data);
            json = root.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    public void networking(){
        Call<CategoryResult> categoryResultCall = service.getCategoryResult(getCategoryInfo("category_list"));
        categoryResultCall.enqueue(new Callback<CategoryResult>() {
            @Override
            public void onResponse(Call<CategoryResult> call, Response<CategoryResult> response) {
                if (response.isSuccessful()) {
                    Log.d("check1", "Category network ok");
                    categoryDataList = response.body().category_info;

                    Glide.with(CategoryListActivity.this).load(categoryDataList.get(category_id-1).category_image_url).into(simple_image);

//                    String text_info = categoryDataList.get(category_id-1).category_detail_info;
                    CharSequence cs = categoryDataList.get(category_id-1).category_detail_info;
                    category_text_info.setText(cs);

                    Log.d("check1", "cate_detail_info : " + categoryDataList.get(category_id-1).category_detail_info);
                    Log.d("check1", "cate_detail_info자체 : " + category_text_info);

                }
            }

            @Override
            public void onFailure(Call<CategoryResult> call, Throwable t) {
                Log.d("check1", "실패 : " + t.getMessage());            }
        });


    }


    //List
    public void networking2() {
        Call<DocentResult> docent_list_ResultCall = service.getDocentResult(getDocentListInfo("docent_list", category_id));
        docent_list_ResultCall.enqueue(new Callback<DocentResult>() {
            @Override
            public void onResponse(Call<DocentResult> call, Response<DocentResult> response) {
                if (response.isSuccessful()) {
                    Log.d("check1", "Docent List network ok");
                    docentDataList = response.body().docent_info;
                    Log.d("check1", "docentDataList : " + docentDataList);

                    categoryListAdapter.setAdapter(docentDataList);
                    String count = "전시품 총 " + String.valueOf(docentDataList.size()) + "개";
                    countText.setText(count);

                    Log.d("check1", response.body().docent_info.get(0).toString());

                }
            }
            @Override
            public void onFailure(Call<DocentResult> call, Throwable t) {
                Log.d("check1", "실패 : " + t.getMessage());            }
        });
    }

    public void init() {
//        initDataset();

        category_list_toolbar_title = (TextView) findViewById(R.id.docentTitle);
        Toolbar categoryListToolbar = (Toolbar) findViewById(R.id.category_list_Toolbar);
        categoryListToolbar.bringToFront();

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        category_list_toolbar_title.setText(category_title);

        recyclerView = (RecyclerView) findViewById(R.id.category_list_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

//        categoryListAdapter = new CategoryListAdapter(this, categoryListActivityItem);

        categoryListAdapter = new CategoryListAdapter(getApplicationContext(), docentDataList);
        recyclerView.setAdapter(categoryListAdapter);

        simple_image = (ImageView)findViewById(R.id.simple_image);

        category_list_title = (TextView) findViewById(R.id.category_list_title);
        category_list_title.setText(category_title + " 소개");
        countText = (TextView) findViewById(R.id.countText);

        category_text_info = (JustifiedTextView) findViewById(R.id.category_text_info);


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
}
