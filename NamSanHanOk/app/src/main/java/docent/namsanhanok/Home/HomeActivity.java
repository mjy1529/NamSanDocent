package docent.namsanhanok.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.angads25.toggle.LabeledSwitch;
import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;
import com.squareup.picasso.Picasso;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import docent.namsanhanok.Application;
import docent.namsanhanok.BackPressCloseHandler;
import docent.namsanhanok.Category.CategoryActivity;
import docent.namsanhanok.Category.CategoryData;
import docent.namsanhanok.Category.CategoryListActivity;
import docent.namsanhanok.Category.CategoryResult;
import docent.namsanhanok.Docent.DocentActivity;
import docent.namsanhanok.Docent.DocentBeaconResult;
import docent.namsanhanok.Docent.DocentData;
import docent.namsanhanok.Docent.DocentResult;
import docent.namsanhanok.Event.EventActivity;
import docent.namsanhanok.Info.InfoActivity;
//import docent.namsanhanok.IntentService;
import docent.namsanhanok.Manager.DocentMemList;
import docent.namsanhanok.Manager.IDInfoData;
import docent.namsanhanok.NetworkService;
import docent.namsanhanok.Notice.NoticeActivity;
import docent.namsanhanok.Question.QuestionWriteActivity;
import docent.namsanhanok.R;
import docent.namsanhanok.Setting.SettingActivity;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HEAD;

import static com.minew.beacon.BluetoothState.BluetoothStatePowerOn;
import static java.lang.Thread.sleep;

public class HomeActivity extends AppCompatActivity {

    ImageButton settingBtn;
    ImageView menuBtn1, menuBtn2, menuBtn3, menuBtn4, menuBtn5;
    LabeledSwitch toggleBtn;
    PrettyDialog newItemDialog;

    private BackPressCloseHandler backPressCloseHandler;
    private MinewBeaconManager mMinewBeaconManager;
    private Vibrator vibrator;

    UserRssi comp = new UserRssi();
    List<MinewBeacon> appearBeaconList = new ArrayList<>(); //인식된 비콘 리스트
    private Handler handler = null;
    String prev_beacon = "";

    private Application applicationclass;
    NetworkService service;

    HomeData homeData;
    DocentMemList docentMemList;
    static List<MinewBeacon> minewBeacons1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        homeNetworking();
        categoryListNetworking();
        initBeaconManager();
        initBeaconListenerManager();
//        showBeaconAlarm();
    }

    public void homeNetworking() {
        Call<HomeResult> request = service.getHomeResult(homeJsonToString());
        request.enqueue(new Callback<HomeResult>() {
            @Override
            public void onResponse(Call<HomeResult> call, Response<HomeResult> response) {
                Log.d("check", "home 성공 : " + response.code());
                if (response.isSuccessful()) {
                    HomeResult homeResult = response.body();
                    homeData = homeResult.home_info;

                    setting();
                }
            }

            @Override
            public void onFailure(Call<HomeResult> call, Throwable t) {
                Log.d("check", "home 실패");
            }
        });
    }

    public void setting() {
        TextView docentTitle = (TextView) findViewById(R.id.docentTitle);
        final RelativeLayout homeLayout = (RelativeLayout) findViewById(R.id.homeLayout);
        docentTitle.setText(homeData.getHome_title());

        Glide.with(this)
                .load(Environment.getExternalStorageDirectory() + homeData.getHome_image_url())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            homeLayout.setBackground(resource);
                        }
                    }
                });
    }

    public void categoryListNetworking() {
        Call<CategoryResult> request = service.getCategoryResult(categoryJsonToString());
        request.enqueue(new Callback<CategoryResult>() {
            @Override
            public void onResponse(Call<CategoryResult> call, Response<CategoryResult> response) {
                if (response.isSuccessful()) {
                    CategoryResult categoryResult = response.body();
                    ArrayList<CategoryData> categoryList = categoryResult.category_info;
                    for (int i = 0; i < categoryList.size(); i++) {
                        docentMemList.put_category_info(categoryList.get(i));
                    }

                    for (int category_id = 1; category_id <= docentMemList.getCategorylist().size(); category_id++) {
                        docentListNetworking(String.valueOf(category_id));
                    }
                }
                Log.d("check1", "categoryListNetworking : " + docentMemList.getCategorylist().toString());

            }

            @Override
            public void onFailure(Call<CategoryResult> call, Throwable t) {
                Log.d("check", "categoryListNetworking : 실패");
            }
        });
    }

    public void docentListNetworking(String category_id) {
        Call<DocentResult> request = service.getDocentResult(docentJsonToString(category_id));
        request.enqueue(new Callback<DocentResult>() {
            @Override
            public void onResponse(Call<DocentResult> call, Response<DocentResult> response) {
                if (response.isSuccessful()) {
                    DocentResult docentResult = response.body();
                    ArrayList<DocentData> docentList = docentResult.docent_info;

                    for (int i = 0; i < docentList.size(); i++) {
                        docentMemList.put_docent_info(docentList.get(i));
                    }
                }
            }

            @Override
            public void onFailure(Call<DocentResult> call, Throwable t) {
                Log.d("check", "docentListNetworking : 실패");
            }
        });
    }

    public void initBeaconManager() {
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
    }

    public void initBeaconListenerManager() {

        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            @Override
            public void onAppearBeacons(List<MinewBeacon> minewBeacons) {
                for(int i = 0 ; i < minewBeacons.size() ; i++){
                    Log.d("check2", "minewBeacons : " + minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue());
                }

                for(int i = 0; i < minewBeacons.size() ; i++){
                    String beacon_minor = minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();

                    IDInfoData idInfoData = new IDInfoData();
                    if (docentMemList.check_beacon_number(beacon_minor, idInfoData)) {
                        synchronized (this){
                            minewBeacons1.add(minewBeacons.get(i));
                        }
                    }
                }

            }

            @Override
            public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {
                Log.d("check2", "disappear");
                for (int i = 0; i < minewBeacons1.size(); i++) {
                    String disappearBeacon_minor = minewBeacons1.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                    appearBeaconList.remove(minewBeacons1.get(i));
                    if (disappearBeacon_minor.equals(prev_beacon))
                        prev_beacon = "";
                }

            }

            @Override
            public void onRangeBeacons(final List<MinewBeacon> minewBeacons) {
                if (!minewBeacons1.isEmpty()) {
                    Collections.sort(minewBeacons1, comp);
                    for (int i = 0; i < minewBeacons1.size(); i++) {
                        Log.d("check2", "minewBeacons1거리순 : " + minewBeacons1.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue());

                    }
                    Log.d("check2", "minewBeacons1거리 첫번째 : " + minewBeacons1.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue());

                    String beacon_minor;
                    int beacon_rssi;
                    synchronized (this) {
                        beacon_minor = minewBeacons1.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                        beacon_rssi = minewBeacons1.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getIntValue();
                    }


                    if (beacon_rssi > -70 && beacon_rssi < -30) {
                        IDInfoData idInfoData = new IDInfoData();
                        if (!beacon_minor.equals(prev_beacon)) {
                            if (newItemDialog != null && newItemDialog.isShowing()) {
                                newItemDialog.dismiss();

                            }

                            Log.d("check2", "prev_beacon2 : " + prev_beacon);
                            if (docentMemList.check_beacon_number(beacon_minor, idInfoData)) {
                                showBeaconAlarm(idInfoData);
                                prev_beacon = beacon_minor;
                            }


                        }

                    }

//                if (processing == false) {
//                    processing = true;
//                    criticalsection start;
//                    synchronized (this) {
//
//                    }
//                    ArrayList<MinewBeacon> minewBeaconsTemp = new ArrayList<>();
//                    minewBeaconsTemp.addAll(minewBeacons);
//                    addAppearBeacon(minewBeacons);
//                    Log.d("beacon", minewBeacons.toString());
//                    criticalsection end;
//                    processing= false;
//                }

                }
            }

            @Override
            public void onUpdateState(BluetoothState bluetoothState) {
                if (!isOnBluetooth() && toggleBtn.isOn()) {
                    toggleBtn.setOn(false);
                }
            }
        });
    }


//
//    private void addAppearBeacon(List<MinewBeacon> minewBeacons) {
//
//        if (!minewBeacons.isEmpty()) {
//            Collections.sort(minewBeacons, comp);
//            boolean exist = true;
//            int i = 0;
//
//            while(exist && i < minewBeacons.size()){
//                Log.d("check2", "i는 " + i);
//
//                String beacon_minor = minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
//                int beacon_rssi = minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getIntValue();
//                Log.d("check2", "beacon_minor : " + beacon_minor);
//                Log.d("check2", "beacon_rssi : " + beacon_rssi);
//
//                i++;
//                //IDInfoData
//                IDInfoData idInfoData = new IDInfoData();
//                if (docentMemList.check_beacon_number(beacon_minor, idInfoData)) {
//                    Log.d("check2", "beacon_minor is exist : " + beacon_minor);
//                    Log.d("check2", "prev_beacon1 : " + prev_beacon);
//
////                   appearBeaconList.add(minewBeacons.get(i));
////                   Log.d("beaconList", minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue());
//
//                    if (beacon_rssi > -70 && beacon_rssi < -30 ) {
//                        if (!beacon_minor.equals(prev_beacon)) { // 이전 비콘넘버와 다를때
//                            if (newItemDialog != null && newItemDialog.isShowing()) { //newItemDialog가 보여진다면
//                                newItemDialog.dismiss();
//                                showBeaconAlarm(idInfoData);
//                                prev_beacon = beacon_minor;
//                            }
//                            Log.d("check2", "prev_beacon2 : " + prev_beacon);
//
//
//                        }
//                        exist = false;
//                        Log.d("check2", "exist : " + exist);
//                    }
//                }
//
//            }
//        }
//    }

    public void showBeaconAlarm(final IDInfoData idInfoData) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                vibrator.vibrate(500);
                synchronized (this){
                    showNewItemDialog(idInfoData);
                }
                Log.d("check1", "handler 작동중...");
            }
        },2500);

    }


    private boolean isOnBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        if (bluetoothState == BluetoothStatePowerOn) {
            return true;
        } else {
            return false;
        }
    }

    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.settingBtn: //설정 버튼
                intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.menuBtn1: //마을 둘러보기
                intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category_title", homeData.getCategory_title());
                startActivity(intent);
                break;

            case R.id.menuBtn2: //세시/행사
                intent = new Intent(getApplicationContext(), EventActivity.class);
                intent.putExtra("event_title", homeData.getEvent_title());
                startActivity(intent);
                break;

            case R.id.menuBtn3: //알리는 말씀
                intent = new Intent(HomeActivity.this, NoticeActivity.class);
                intent.putExtra("notice_title", homeData.getNotice_title());
                startActivity(intent);
                break;

            case R.id.menuBtn4: //문의하기
                intent = new Intent(HomeActivity.this, QuestionWriteActivity.class);
                intent.putExtra("question_title", homeData.getQuestion_title());
                startActivity(intent);
                break;

            case R.id.menuBtn5: //이용안내
                intent = new Intent(HomeActivity.this, InfoActivity.class);
                intent.putExtra("operationguide_title", homeData.getOperationguide_title());
                startActivity(intent);
                break;
        }
    }

    public void init() {
        handler = new Handler();
        applicationclass = (Application) getApplicationContext();
        docentMemList = DocentMemList.getInstance();
        docentMemList.initialize();

        service = Application.getInstance().getNetworkService();
        backPressCloseHandler = new BackPressCloseHandler(this);
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Toolbar homeToolbar = (Toolbar) findViewById(R.id.homeToolbar);
        homeToolbar.bringToFront();

        settingBtn = (ImageButton) findViewById(R.id.settingBtn);
        menuBtn1 = (ImageView) findViewById(R.id.menuBtn1);
        menuBtn2 = (ImageView) findViewById(R.id.menuBtn2);
        menuBtn3 = (ImageView) findViewById(R.id.menuBtn3);
        menuBtn4 = (ImageView) findViewById(R.id.menuBtn4);
        menuBtn5 = (ImageView) findViewById(R.id.menuBtn5);
        toggleBtn = (LabeledSwitch) findViewById(R.id.toggleBtn);

        toggleBtn.setOn(applicationclass.getToggleState());

        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnBluetooth() && !toggleBtn.isOn()) { // bluethooth == false, toggle버튼 on
                    toggleBtn.setOn(true);
                    applicationclass.setToggleState(false);
                    showBluetoothDialog();

                } else if (isOnBluetooth() && !toggleBtn.isOn()) { // bluetooth==true, toggle버튼 on
                    applicationclass.setScanning(true);
                    applicationclass.setToggleState(true);
                    mMinewBeaconManager.startScan();

                    if(handler != null) {
                        handler.sendEmptyMessage(0);
                    }

                } else if (isOnBluetooth() && toggleBtn.isOn()) { // bluetooth==true, toggle버튼 off
                    applicationclass.setToggleState(false);
                    applicationclass.setScanning(false);
                    if (mMinewBeaconManager != null) {
                        mMinewBeaconManager.stopScan();
                    }

                    if(handler != null) {
                        handler.sendEmptyMessage(0);
                    }

                    appearBeaconList.clear();
                    prev_beacon = "";
                }
            }
        });
    }

    public void showBluetoothDialog() {
        final PrettyDialog bluetoothDialog = new PrettyDialog(HomeActivity.this);
        bluetoothDialog.setMessage(getResources().getString(R.string.bluetoothAlertMessage))
                .setIcon(R.drawable.pdlg_icon_info)
                .setIconTint(R.color.pdlg_color_blue)
                .addButton("확인", // button text
                        R.color.pdlg_color_white,  // button text color
                        R.color.pdlg_color_blue,  // button background color
                        new PrettyDialogCallback() {  // button OnClick listener
                            @Override
                            public void onClick() {
                                bluetoothDialog.dismiss();
                            }
                        }
                ).show();
    }

    public void showNewItemDialog(final IDInfoData idInfoData) {
        Log.d("check1", "알람뜸");
        newItemDialog = new PrettyDialog(HomeActivity.this);
        newItemDialog.setMessage(getResources().getString(R.string.newItemAlertMessage))
                .setIcon(R.drawable.pdlg_icon_info)
                .setIconTint(R.color.pdlg_color_blue)
                .addButton("확인", // button text
                        R.color.pdlg_color_white,  // button text color
                        R.color.pdlg_color_blue,  // button background color
                        new PrettyDialogCallback() {  // button OnClick listener
                            @Override
                            public void onClick() {
                                //getDocentByBeacon(beacon_number);
                                Intent intent = null;
                                if(idInfoData.docent_id.equals("")) { //카테고리리스트액티비티
                                   intent = new Intent(HomeActivity.this, CategoryListActivity.class);
                                   CategoryData categoryData = new CategoryData();
                                   docentMemList.get_category_info(idInfoData.category_id, categoryData);
                                   intent.putExtra("category", categoryData);

                                } else if (!idInfoData.docent_id.equals("") && !idInfoData.category_id.equals("")) {
                                    intent = new Intent(HomeActivity.this, DocentActivity.class);
                                    HashMap<String, DocentData> map = new HashMap<>();
                                    docentMemList.get_docent_info(idInfoData.category_id, map);
                                    DocentData docentData = new DocentData();
                                    docentData = map.get(idInfoData.docent_id);
                                    intent.putExtra("docentObject", docentData);
                                }
                                startActivity(intent);
                                newItemDialog.dismiss();
                            }
                        }
                )
                .addButton("취소", // button text
                        R.color.pdlg_color_white,  // button text color
                        R.color.dialog_cancel,  // button background color
                        new PrettyDialogCallback() {  // button OnClick listener
                            @Override
                            public void onClick() {
                                newItemDialog.dismiss();
                            }
                        }
                );
        newItemDialog.show();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("check1", "home_onResume");

        if (applicationclass.getToggleState()) {
            Log.d("check1", "home_onResume_start_scan");
            if (handler != null) {
                handler.sendEmptyMessageDelayed(0, 2500);
            }
            mMinewBeaconManager.startScan();
            applicationclass.setScanning(true);

        }

        if (newItemDialog != null && newItemDialog.isShowing()) newItemDialog.dismiss();
    }

    @Override
    protected void onStop() {
        Log.d("check1", "home_onStop");

        super.onStop();
        if (applicationclass.getToggleState()) {
            mMinewBeaconManager.stopScan();
            applicationclass.setScanning(false);
        }
        if (handler != null) {
            handler.removeMessages(0);
        }
    }


    @Override
    protected void onDestroy() {
        Log.d("check1", "home_onDestroy");

        super.onDestroy();
        appearBeaconList.clear();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public String homeJsonToString() {
        String jsonStr = "";
        try {
            JSONObject data = new JSONObject();
            data.put("cmd", "home_info");

            JSONObject root = new JSONObject();
            root.put("info", data);
            jsonStr = root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    public String categoryJsonToString() {
        String jsonStr = "";
        try {
            JSONObject data = new JSONObject();
            data.put("cmd", "category_list");

            JSONObject root = new JSONObject();
            root.put("info", data);
            jsonStr = root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    public String docentJsonToString(String category_id) {
        String jsonStr = "";
        try {
            JSONObject data = new JSONObject();
            data.put("cmd", "docent_list");
            data.put("category_id", category_id);

            JSONObject root = new JSONObject();
            root.put("info", data);
            jsonStr = root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    public String beaconJsonToString(String beacon_number) {
        String jsonStr = "";
        try {
            JSONObject data = new JSONObject();
            data.put("cmd", "docent_list");
            data.put("beacon_number", beacon_number);

            JSONObject root = new JSONObject();
            root.put("info", data);
            jsonStr = root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }
}
