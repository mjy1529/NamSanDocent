package docent.namsanhanok.Docent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Network;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import docent.namsanhanok.Application;
import docent.namsanhanok.NetworkService;
import docent.namsanhanok.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocentPopUpActivity extends Activity {

    ImageButton closeBtn;
    ImageView imageView;
    TextView textView;
    TextView imageTitle;
//    int docent_detail_id;
    int position;
    int docent_id;

    NetworkService service;
    ArrayList<DocentDetailData> docentDetailDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_docentpupup);

        Intent secondIntent = getIntent();
        position = secondIntent.getExtras().getInt("position");
        docent_id = secondIntent.getExtras().getInt("docent_id");
        service = Application.getInstance().getNetworkService();

        init();


    }

    public void networking() {
        Call<DocentDetailResult> docentDetailListResult = service.getDocentDetailResult(getDocentDetailInfo("docent_detail_list", docent_id));
        docentDetailListResult.enqueue(new Callback<DocentDetailResult>() {
            @Override
            public void onResponse(Call<DocentDetailResult> call, Response<DocentDetailResult> response) {
                if (response.isSuccessful()) {

                    docentDetailDataList = response.body().docent_detail_info;

                    //제목, 이미지, 설명 설정
                    imageTitle.setText(docentDetailDataList.get(position).docent_detail_title);
                    Glide.with(getApplicationContext())
                            .load(Environment.getExternalStorageDirectory() + docentDetailDataList.get(position).docent_detail_image_url)
                            .into(imageView);

                    textView.setText(docentDetailDataList.get(position).docent_detail_info);

                    Log.d("check1", "docentDetailDataList : " + docentDetailDataList.toString());
                    Log.d("check1", "docentDetailDataList 크기: " + docentDetailDataList.size());


                }
            }
            @Override
            public void onFailure(Call<DocentDetailResult> call, Throwable t) {
                Log.d("check1", "실패 : " + t.getMessage());            }
        });
    }

    public void init(){
        closeBtn = (ImageButton) findViewById(R.id.closed);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageView = (ImageView) findViewById(R.id.image);
        textView = (TextView) findViewById(R.id.image_info);
        imageTitle = (TextView) findViewById(R.id.image_title);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private String getDocentDetailInfo(String cmd, int docent_id) {
        String json = "";
        try {
            JSONObject data = new JSONObject();
            data.put("cmd", cmd);
            data.put("docent_id", docent_id);


            JSONObject root = new JSONObject();
            root.put("info", data);
            json = root.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("check1", "docent_detail 정보요청 : " + json);
        return json;
    }

}
