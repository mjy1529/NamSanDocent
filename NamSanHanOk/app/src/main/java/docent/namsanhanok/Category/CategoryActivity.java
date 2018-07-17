package docent.namsanhanok.Category;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import docent.namsanhanok.R;

public class CategoryActivity extends AppCompatActivity {
    LinearLayout hanok_img;
    LinearLayout lake_img;
    LinearLayout capsule_img;

//    TextView hanok_title;
//    TextView lake_title;
//    TextView capsule_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        init();
        setImg();
    }

    public void setImg() {
        final int blackFilter = getApplication().getResources().getColor(R.color.black_color_filter);
        PorterDuffColorFilter blakcColorFilter = new PorterDuffColorFilter(blackFilter, PorterDuff.Mode.SRC_ATOP);

//            Glide.with(getApplicationContext())
//                    .load(R.drawable.namsan2)
//                    .apply(new RequestOptions()
//                            .centerCrop())
//                    .into(holder.VH_docent_image);

    }

    public void init() {
        hanok_img = (LinearLayout) findViewById(R.id.category_layout1);
        lake_img = (LinearLayout) findViewById(R.id.category_layout2);
        capsule_img = (LinearLayout) findViewById(R.id.category_layout3);

    }
}
