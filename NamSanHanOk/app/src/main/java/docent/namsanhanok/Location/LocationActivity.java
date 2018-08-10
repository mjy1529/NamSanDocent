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
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import docent.namsanhanok.Application;
import docent.namsanhanok.Docent.DocentData;
import docent.namsanhanok.Docent.DocentResult;
import docent.namsanhanok.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.senab.photoview.PhotoViewAttacher;

import docent.namsanhanok.R;

public class LocationActivity extends AppCompatActivity {

    ImageButton cancelBtn;
    TextView toolbar_title;
    String locationTitle;

    ImageView map;
    PhotoViewAttacher mAttacher;

    NetworkService service;
    int category_id;
    int position;
    ArrayList<DocentData> docentDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Intent secondIntent = getIntent();
        locationTitle = secondIntent.getExtras().getString("title");
        category_id = secondIntent.getExtras().getInt("category_id");
        position = secondIntent.getExtras().getInt("position");
        service = Application.getInstance().getNetworkService();

        init();
        networking();

        }

    public void networking() {
        Call<DocentResult> request = service.getDocentResult(getDocentInfo("docent_list", category_id));
            request.enqueue(new Callback<DocentResult>() {
                @Override
                public void onResponse(Call<DocentResult> call, Response<DocentResult> response) {
                    if(response.body() != null) {


                        docentDataList= response.body().docent_info;

                        Glide.with(getApplicationContext())
                                .load(Environment.getExternalStorageDirectory() + docentDataList.get(position).docent_location)
                                .into(map);


                        mAttacher = new PhotoViewAttacher(map);
                    }
                }

                @Override
                public void onFailure(Call<DocentResult> call, Throwable t) {
                    Log.d("check", "fail");
                }
            });
    }

//    public void setLocationImage(){
//        if(locationTitle.equals("삼각동 도편수(都片手) 이승업(李承業) 가옥")){
////            map.setImageResource(R.drawable.map_whole_namsanhanok_2);
//            map.setImageResource(R.drawable.map_whole_namsanhanok_2);
//            mAttacher = new PhotoViewAttacher(map);
//
//        }
//
//    }

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

    public String getDocentInfo(String cmd, int cate_id) {
        String jsonStr = "";
        try {
            JSONObject data = new JSONObject();
            data.put("cmd", cmd);
            data.put("category_id", cate_id);

            JSONObject root = new JSONObject();
            root.put("info", data);
            jsonStr = root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("check1", "docent content 정보요청 : " + jsonStr);

        return jsonStr;
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
