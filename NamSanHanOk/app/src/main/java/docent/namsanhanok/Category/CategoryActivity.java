package docent.namsanhanok.Category;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Iterator;

import docent.namsanhanok.Home.SplashActivity;
import docent.namsanhanok.ShowWiFiMonitor;
import docent.namsanhanok.Application;
import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.Manager.DocentMemList;
import docent.namsanhanok.NetworkService;
import docent.namsanhanok.R;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class CategoryActivity extends AppCompatActivity {
    ImageButton homeBtn;
    TextView category_toolbar_title;

    private NetworkService service;
    public ArrayList<CategoryData> categoryDataList;

    DocentMemList docentMemList;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        init();
        setContent();
        initRecyclerView();
    }


    public void init() {
        Toolbar categoryToolbar = (Toolbar) findViewById(R.id.categoryToolbar);
        setSupportActionBar(categoryToolbar);

        service = Application.getInstance().getNetworkService();
        docentMemList = DocentMemList.getInstance();
        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        category_toolbar_title = (TextView) findViewById(R.id.docentTitle);
    }
    
    public void setContent() {
        categoryDataList = new ArrayList<>();

        //toolbar title 설정
        Intent intent = getIntent();
        category_toolbar_title.setText(intent.getStringExtra("category_title"));

        Iterator<String> keys = docentMemList.getCategorylist().keySet().iterator();
        while (keys.hasNext()) {
            CategoryData categoryData = new CategoryData();
            String key = keys.next();
            docentMemList.get_category_info(docentMemList.getCategorylist().get(key).category_id, categoryData);

            categoryDataList.add(categoryData);
        }

    }


    public void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.category_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        categoryAdapter = new CategoryAdapter(getApplicationContext(), categoryDataList);
        recyclerView.setAdapter(categoryAdapter);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeBtn:
                onPause();
                Intent intent = new Intent(CategoryActivity.this, HomeActivity.class);
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
    public void onResume() {
        super.onResume();

    }



    @Override
    public void onPause() {

        super.onPause();

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//
//            }
//
//        }, 100);
    }

}
