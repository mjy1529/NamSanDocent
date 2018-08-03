package docent.namsanhanok.Location;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import uk.co.senab.photoview.PhotoViewAttacher;

import docent.namsanhanok.R;

public class LocationActivity extends AppCompatActivity {

    ImageButton cancelBtn;
    TextView toolbar_title;
    String locationTitle;
    ImageView map;
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Intent secondIntent = getIntent();
        locationTitle = secondIntent.getExtras().getString("title");

        init();
        setLocationImage();
    }

    public void setLocationImage(){
        if(locationTitle.equals("삼각동 도편수(都片手) 이승업(李承業) 가옥")){
//            map.setImageResource(R.drawable.map_whole_namsanhanok_2);
            map.setImageResource(R.drawable.map_whole_namsanhanok_2);
            mAttacher = new PhotoViewAttacher(map);

        }

    }

    public void init() {
        cancelBtn = (ImageButton) findViewById(R.id.location_cancelBtn);
        Toolbar questionRegisterToolbar = (Toolbar)findViewById(R.id.location_toolbar);
        questionRegisterToolbar.bringToFront();

        toolbar_title = (TextView) findViewById(R.id.location_toolbar_title);
        toolbar_title.setText(locationTitle);

        map = (ImageView) findViewById(R.id.location_imageView);
//        map = (PhotoView) findViewById(R.id.location_imageView);

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.location_cancelBtn :
                finish();
                break;

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
