package docent.namsanhanok.Category;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import org.w3c.dom.Text;

import docent.namsanhanok.Docent.DocentActivity;
import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;

public class CategoryActivity extends AppCompatActivity {
    LinearLayout hanok_img;
    LinearLayout lake_img;
    LinearLayout capsule_img;

    ImageButton homeBtn;
    TextView category_toolbar_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        init();
        setImg();
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

        category_toolbar_title.setText("마을 둘러보기");
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
                intent.putExtra("cate_title", "타임 캡슐");
                startActivity(intent);
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
}
