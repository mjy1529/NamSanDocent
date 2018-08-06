package docent.namsanhanok.Category;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import docent.namsanhanok.Application;
import docent.namsanhanok.Docent.DocentActivity;
import docent.namsanhanok.Home.HomeActivity;
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
    int category_id;
    private ArrayList<CategoryData> categoryDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        service = Application.getInstance().getNetworkService();
        category_id = 1;





        init();
        setImg();
        Networking();
//        Networking2();
//        Networking3();
    }

    public void setImg() {
        final int blackFilter = getApplication().getResources().getColor(R.color.black_color_filter);
        final PorterDuffColorFilter blakcColorFilter = new PorterDuffColorFilter(blackFilter, PorterDuff.Mode.SRC_ATOP);

        Glide.with(this).load(R.drawable.namsan1).apply(new RequestOptions()
                .centerCrop()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    hanok_img.setBackground(resource);
                    resource.setColorFilter(blakcColorFilter);

                }
            }
        });

        Glide.with(this).load(R.drawable.namsan_lake).apply(new RequestOptions()
                .centerCrop()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    lake_img.setBackground(resource);
                    resource.setColorFilter(blakcColorFilter);
                }
            }
        });

        Glide.with(this).load(R.drawable.timecapsule).apply(new RequestOptions()
                .centerCrop()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource3, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    capsule_img.setBackground(resource3);
                    resource3.setColorFilter(blakcColorFilter);

                }
            }
        });

    }

    public void init() {
        Toolbar categoryToolbar = (Toolbar)findViewById(R.id.categoryToolbar);
        categoryToolbar.bringToFront();

        hanok_img = (LinearLayout) findViewById(R.id.category_layout1);
        lake_img = (LinearLayout) findViewById(R.id.category_layout2);
        capsule_img = (LinearLayout) findViewById(R.id.category_layout3);

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        category_toolbar_title = (TextView) findViewById(R.id.docentTitle);

        category_title1 = (TextView) findViewById(R.id.category_title1);
        category_title2 = (TextView) findViewById(R.id.category_title2);
        category_title3 = (TextView)findViewById(R.id.category_title3);



        //이거 서버에서 갖고오는걸루해야함
        category_toolbar_title.setText("마을 둘러보기");



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
        Log.d("Log", "getCategoryInfo : " + json);
        return json;


    }

    public void Networking() {
        Call<CategoryResult> categoryResultCall = service.getCategoryResult(getCategoryInfo("category_list"));
        categoryResultCall.enqueue(new Callback<CategoryResult>() {
            @Override
            public void onResponse(Call<CategoryResult> call, Response<CategoryResult> response) {
                if (response.isSuccessful()) {
                        Log.d("Log", "Category network ok");
                        categoryDataList = response.body().category_info;
                        category_title1.setText(categoryDataList.get(0).category_title);
                        category_title2.setText(categoryDataList.get(1).category_title);
                        category_title3.setText(categoryDataList.get(0).category_title);


                    Log.d("Log", response.body().category_info.get(0).category_title);
                }
            }

            @Override
            public void onFailure(Call<CategoryResult> call, Throwable t) {
                Log.d("Log", "실패 : " + t.getMessage());            }
        });
    }


    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
        switch (v.getId()) {
            case R.id.category_layout1 :
            case R.id.category_title1 :
                intent.putExtra("cate_title", "한옥");
                startActivity(intent);
                break;

            case R.id.category_layout2 :
            case R.id.category_title2 :
                intent.putExtra("cate_title", "정원");
                startActivity(intent);
                break;

            case R.id.category_layout3 :
            case R.id.category_title3 :
                Intent docentIntent = new Intent(CategoryActivity.this, DocentActivity.class);
                docentIntent.putExtra("docent_title", "타임 캡슐");
                startActivity(docentIntent);
                break;

            case  R.id.homeBtn :
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

        Networking();

    }
}
