package docent.namsanhanok.Category;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import docent.namsanhanok.R;

public class CategoryListActivity extends Activity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CategoryListAdapter categoryListAdapter;
    private ArrayList<CategoryListActivityItem> categoryListActivityItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        init();


    }

    public void init() {
            initDataset();
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
        categoryListActivityItem.add(new CategoryListActivityItem("한옥", R.drawable.namsan2));
        categoryListActivityItem.add(new CategoryListActivityItem("정원", R.drawable.namsan_lake));
        categoryListActivityItem.add(new CategoryListActivityItem("타임캡슐", R.drawable.timecapsule));

    }

}
