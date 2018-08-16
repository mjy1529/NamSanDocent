package docent.namsanhanok.Location;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import docent.namsanhanok.Application;
import docent.namsanhanok.Docent.DocentData;
import docent.namsanhanok.NetworkService;
import uk.co.senab.photoview.PhotoViewAttacher;

import docent.namsanhanok.R;

public class LocationActivity extends AppCompatActivity {

    ImageButton cancelBtn;
    TextView toolbar_title;
    String locationTitle;

    ImageView map;
    PhotoViewAttacher mAttacher;

    NetworkService service;
    ArrayList<DocentData> docentDataList;

    DocentData docentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Intent secondIntent = getIntent();

        docentData = (DocentData) secondIntent.getSerializableExtra("docentData");
        service = Application.getInstance().getNetworkService();

        init();
        setLocationContent();

        }

    public void setLocationContent(){
        Glide.with(getApplicationContext())
                .load(Environment.getExternalStorageDirectory() + docentData.docent_location)
                .apply(new RequestOptions().fitCenter())
                .into(map);


        mAttacher = new PhotoViewAttacher(map);
    }



    public void init() {
        cancelBtn = (ImageButton) findViewById(R.id.location_cancelBtn);
        Toolbar questionRegisterToolbar = (Toolbar)findViewById(R.id.location_toolbar);
        questionRegisterToolbar.bringToFront();

        toolbar_title = (TextView) findViewById(R.id.location_toolbar_title);
        toolbar_title.setText(locationTitle);

        map = (ImageView) findViewById(R.id.location_imageView);

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
