package docent.namsanhanok.Category;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import docent.namsanhanok.Application;
import docent.namsanhanok.Docent.DocentActivity;
import docent.namsanhanok.Docent.DocentData;
import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.Manager.DocentMemList;
import docent.namsanhanok.NetworkService;
import docent.namsanhanok.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {
    ImageButton homeBtn;
    TextView category_toolbar_title;

    private NetworkService service;
    public ArrayList<CategoryData> categoryDataList;
    static int categorySize;

    DocentMemList docentMemList;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        service = Application.getInstance().getNetworkService();

        init();
        setContent();
    }

    public void init() {
        docentMemList = DocentMemList.getInstance();

        Toolbar categoryToolbar = (Toolbar) findViewById(R.id.categoryToolbar);
        setSupportActionBar(categoryToolbar);

        //recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.category_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        categoryAdapter = new CategoryAdapter(getApplicationContext(), categoryDataList);
        recyclerView.setAdapter(categoryAdapter);

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

        categoryAdapter.setAdapter(categoryDataList);
        categorySize = categoryDataList.size();

    }

    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
        switch (v.getId()) {
            case R.id.homeBtn:
                onPause();
                Intent intent2 = new Intent(CategoryActivity.this, HomeActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
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

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

            }

        }, 100);
    }

}
