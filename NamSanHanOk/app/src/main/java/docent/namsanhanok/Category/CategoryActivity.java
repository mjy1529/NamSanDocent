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
    LinearLayout hanok_img;
    LinearLayout lake_img;
    LinearLayout capsule_img;

    ImageButton homeBtn;
    TextView category_toolbar_title;
    TextView category_title1;
    TextView category_title2;
    TextView category_title3;

    private NetworkService service;
    public ArrayList<CategoryData> categoryDataList;

    public CategoryData categoryData;
    DocentMemList docentMemList;
    DocentData docentData;

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

        hanok_img = (LinearLayout) findViewById(R.id.category_layout1);
        lake_img = (LinearLayout) findViewById(R.id.category_layout2);
        capsule_img = (LinearLayout) findViewById(R.id.category_layout3);

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        category_toolbar_title = (TextView) findViewById(R.id.docentTitle);

        category_title1 = (TextView) findViewById(R.id.category_title1);
        category_title2 = (TextView) findViewById(R.id.category_title2);
        category_title3 = (TextView) findViewById(R.id.category_title3);

        Intent intent = getIntent();
        category_toolbar_title.setText(intent.getStringExtra("category_title"));
    }

    public void setContent() {
        categoryData = new CategoryData();

        int i = 0;
        Iterator<String> keys = docentMemList.categorylist_.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            docentMemList.get_category_info(docentMemList.categorylist_.get(key).category_id, categoryData);
            Log.d("check1", "setContent : " + docentMemList.categorylist_.toString());
            setImg(i); //djkim
            i++;
            Log.d("check1", "setContent : " + toString());
        }

//        for (int i = 1; i <= docentMemList.categorylist_.size(); i++) {
//            String id = String.valueOf(i);
//            docentMemList.get_category_info(id, categoryData);
//            setImg(i);
//        }

    }

    public void setImg(int id) {
        final int blackFilter = getApplication().getResources().getColor(R.color.black_color_filter);
        final PorterDuffColorFilter blakcColorFilter = new PorterDuffColorFilter(blackFilter, PorterDuff.Mode.SRC_ATOP);

        if (id == 1) {
            category_title1.setText(categoryData.category_title);

            Glide.with(this).load(Environment.getExternalStorageDirectory() + categoryData.category_image_url).apply(new RequestOptions()
                    .centerCrop()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        hanok_img.setBackground(resource);
                        resource.setColorFilter(blakcColorFilter);
                    }

                }

            });
        } else if (id == 2) {
            Log.d("check1", "img2 : " + Environment.getExternalStorageDirectory() + categoryData.category_image_url);

            category_title2.setText(categoryData.category_title);

            Glide.with(this).load(Environment.getExternalStorageDirectory() + categoryData.category_image_url).apply(new RequestOptions()
                    .centerCrop()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        lake_img.setBackground(resource);
                        resource.setColorFilter(blakcColorFilter);

                    }
                }
            });
        } else if (id == 3) {
            Log.d("check1", "img3 : " + Environment.getExternalStorageDirectory() + categoryData.category_image_url);

            category_title3.setText(categoryData.category_title);

            Glide.with(this).load(Environment.getExternalStorageDirectory() + categoryData.category_image_url).apply(new RequestOptions()
                    .centerCrop()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        capsule_img.setBackground(resource);
                        resource.setColorFilter(blakcColorFilter);

                    }
                }
            });
        }
    }


    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
        categoryData = new CategoryData();
        switch (v.getId()) {
            case R.id.category_layout1:
            case R.id.category_title1:
                docentMemList.get_category_info("1", categoryData);
                intent.putExtra("category", categoryData);
                onPause();
                startActivity(intent);
                break;

            case R.id.category_layout2:
            case R.id.category_title2:
                docentMemList.get_category_info("2", categoryData);
                onPause();
                intent.putExtra("category", categoryData);
                startActivity(intent);
                break;

            case R.id.category_layout3:
            case R.id.category_title3:
                onPause();
                Intent docentIntent = new Intent(CategoryActivity.this, DocentActivity.class);

                HashMap<String, DocentData> map = new HashMap<String, DocentData>();
                docentMemList.get_docent_info("3", map);
                Iterator<String> keys = map.keySet().iterator();
                String key = keys.next();
                Log.d("check1", "key값 " + key);
                Log.d("check1", "map.get(key)" + map.get(key));

                docentData = map.get(key);

                docentIntent.putExtra("docentObject", docentData);
                startActivity(docentIntent);
                break;

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
