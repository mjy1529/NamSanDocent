package docent.namsanhanok.Category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;

public class CategoryListActivity extends Activity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CategoryListAdapter categoryListAdapter;
    private ArrayList<CategoryListActivityItem> categoryListActivityItem;

    ImageButton homeBtn;
    TextView category_list_toolbar_title;
    String category_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Intent secondIntent = getIntent();
        category_title = secondIntent.getExtras().getString("cate_title");

        init();


    }


    public void init() {
            initDataset();

            category_list_toolbar_title = (TextView) findViewById(R.id.docentTitle);
            Toolbar categoryListToolbar = (Toolbar)findViewById(R.id.category_list_Toolbar);
            categoryListToolbar.bringToFront();

            homeBtn = (ImageButton) findViewById(R.id.homeBtn);
            category_list_toolbar_title.setText(category_title);

            recyclerView = (RecyclerView) findViewById(R.id.category_list_recyclerView);
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            categoryListAdapter = new CategoryListAdapter(this, categoryListActivityItem);

            recyclerView.setAdapter(categoryListAdapter);
    }


    private void initDataset() {
        categoryListActivityItem = new ArrayList<>();
        categoryListActivityItem.add(new CategoryListActivityItem("삼각동 도편수(都片手) 이승업(李承業) 가옥", R.drawable.namsan1));
        categoryListActivityItem.add(new CategoryListActivityItem("삼각동 도편수(都片手) 이승업(李承業) 가옥2", R.drawable.namsan2));
        categoryListActivityItem.add(new CategoryListActivityItem("삼각동 도편수(都片手) 이승업(李承業) 가옥", R.drawable.namsan1));
        categoryListActivityItem.add(new CategoryListActivityItem("삼각동 도편수(都片手) 이승업(李承業) 가옥2", R.drawable.namsan2));
        categoryListActivityItem.add(new CategoryListActivityItem("삼각동 도편수(都片手) 이승업(李承業) 가옥", R.drawable.namsan1));
        categoryListActivityItem.add(new CategoryListActivityItem("삼각동 도편수(都片手) 이승업(李承業) 가옥2", R.drawable.namsan2));
        categoryListActivityItem.add(new CategoryListActivityItem("삼각동 도편수(都片手) 이승업(李承業) 가옥", R.drawable.namsan1));
        categoryListActivityItem.add(new CategoryListActivityItem("삼각동 도편수(都片手) 이승업(李承業) 가옥2", R.drawable.namsan2));

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.homeBtn :
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                break;

        }
    }
}
