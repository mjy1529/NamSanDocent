package docent.namsanhanok.Home;

import android.Manifest;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
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
import com.tsengvn.typekit.Typekit;
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

import docent.namsanhanok.AppUtility.BeaconDialog;
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
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static com.minew.beacon.BluetoothState.BluetoothStatePowerOn;

public class HomeActivity extends AppCompatActivity {

    ImageButton settingBtn;
    ImageView menuBtn1, menuBtn2, menuBtn3, menuBtn4, menuBtn5;
    LabeledSwitch toggleBtn;
    BeaconDialog newItemDialog = null;

    private BackPressCloseHandler backPressCloseHandler;
    private MinewBeaconManager mMinewBeaconManager;
    private Vibrator vibrator;

    UserRssi comp = new UserRssi();
    private Handler handler = null;
    String prev_beacon = "";

    NetworkService service;

    HomeData homeData;
    DocentMemList docentMemList;
    static List<MinewBeacon> beaconArrayList = new ArrayList<>();
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 456;

    static final String SHOWCASE_ID = "tutorial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d("check2", "\n" + "onCreate_prev_beacon : " + prev_beacon);
        init();
        homeNetworking();
        categoryListNetworking();
        initBeaconManager();
        initBeaconListenerManager();

        presentShowcaseSequence();
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

    public void presentShowcaseSequence() {
        LinearLayout tutorial_layout = (LinearLayout) findViewById(R.id.tutorial_layout);

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "SeoulHangangEB.ttf");

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500);
        config.setDismissTextStyle(typeface);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
        sequence.setConfig(config);
        sequence.addSequenceItem(tutorial_layout, getResources().getString(R.string.tutorialGuide), "확인");
        sequence.start();
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
        mMinewBeaconManager = new MinewBeaconManager();
    }

    public void initBeaconListenerManager() {

        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            @Override
            public void onAppearBeacons(List<MinewBeacon> minewBeacons) {

            }

            @Override
            public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {
                Log.d("check2", "home_disappear");
                for (MinewBeacon minewBeacon : minewBeacons) {
                    String deviceName = minewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                    Log.d("check2", "home_사라진다 : " + deviceName);

                    if (beaconArrayList.contains(minewBeacons))
                        beaconArrayList.remove(minewBeacons);
                }

            }

            @Override
            public void onRangeBeacons(final List<MinewBeacon> minewBeacons) {
                for (int i = 0; i < minewBeacons.size(); i++) {
                    String beacon_minor = minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();

                    IDInfoData idInfoData = new IDInfoData();
                    if (docentMemList.check_beacon_number(beacon_minor, idInfoData)) {
                        synchronized (this) {
                            if (!beaconArrayList.contains(minewBeacons.get(i))) {
                                beaconArrayList.add(minewBeacons.get(i));
                                Log.d("check", "home_Beacon_add : " + minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue());
                            }
                        }
                    }
                }

                if (!beaconArrayList.isEmpty()) {
                    Collections.sort(beaconArrayList, comp);
                    for (int i = 0; i < beaconArrayList.size(); i++) {
                        Log.d("check2", "\n" + "home_beaconArrayList " + (i + 1) + "번째 : " + beaconArrayList.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue());

                    }

                    String beacon_minor;
                    int beacon_rssi;
                    synchronized (this) {
                        beacon_minor = beaconArrayList.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                        beacon_rssi = beaconArrayList.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getIntValue();
                    }

                    if (beacon_rssi > -70 && beacon_rssi < -30) {
                        IDInfoData idInfoData = new IDInfoData();
                        if (!beacon_minor.equals(prev_beacon)) {

                            Log.d("check2", "home_prev_beacon2 : " + prev_beacon);
                            if (docentMemList.check_beacon_number(beacon_minor, idInfoData)) {
                                showBeaconAlarm(idInfoData);
                                prev_beacon = beacon_minor;
                            }
                        }
                    }
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

    public void showBeaconAlarm(final IDInfoData idInfoData) {
        if(handler != null){
            handler.removeMessages(0);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                vibrator.vibrate(500);
                showNewItemDialog(idInfoData);
                Log.d("check1", "handler 작동중...");
            }
        }, 2500);

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
        Log.d("check2", "\n" + "init()실행");
        handler = new Handler();
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

        toggleBtn.setOn(Application.getInstance().getToggleState());

        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnBluetooth() && !toggleBtn.isOn()) { // bluethooth == false, toggle버튼 on
                    toggleBtn.setOn(true);
                    Application.getInstance().setToggleState(false);
                    showBluetoothDialog();

                } else if (isOnBluetooth() && !toggleBtn.isOn()) { // bluetooth==true, toggle버튼 on
                    Application.getInstance().setScanning(true);
                    Application.getInstance().setToggleState(true);
                    mMinewBeaconManager.startScan();

                    if (handler != null) {
                        handler.sendEmptyMessage(0);
                    }

                } else if (isOnBluetooth() && toggleBtn.isOn()) { // bluetooth==true, toggle버튼 off
                    Application.getInstance().setToggleState(false);
                    Application.getInstance().setScanning(false);
                    if (mMinewBeaconManager != null) {
                        mMinewBeaconManager.stopScan();
                    }

                    if (handler != null) {
                        handler.sendEmptyMessage(0);
                    }

                    prev_beacon = "";
                }
            }
        });
    }

    public void showBluetoothDialog() {
        final PrettyDialog bluetoothDialog = new PrettyDialog(HomeActivity.this);
        bluetoothDialog.setMessage(getResources().getString(R.string.bluetoothAlertMessage))
                .setIcon(R.drawable.pdlg_icon_info)
                .setIconTint(R.color.dark_blue)
                .addButton("확인", // button text
                        R.color.pdlg_color_white,  // button text color
                        R.color.dark_blue,  // button background color
                        new PrettyDialogCallback() {  // button OnClick listener
                            @Override
                            public void onClick() {
                                bluetoothDialog.dismiss();
                            }
                        }
                ).show();
    }

    public void showNewItemDialog(final IDInfoData idInfoData) {
        if (newItemDialog != null && newItemDialog.isShowing()) {
            newItemDialog.dismiss();
        }

//        Log.d("check1", "알람뜸");
//        newItemDialog = new PrettyDialog(HomeActivity.this);
//        newItemDialog.setMessage(getResources().getString(R.string.newItemAlertMessage))
//                .setIcon(R.drawable.pdlg_icon_info)
//                .setIconTint(R.color.pdlg_color_blue)
//                .addButton("확인", // button text
//                        R.color.pdlg_color_white,  // button text color
//                        R.color.pdlg_color_blue,  // button background color
//                        new PrettyDialogCallback() {  // button OnClick listener
//                            @Override
//                            public void onClick() {
//                                //getDocentByBeacon(beacon_number);
//                                Intent intent = null;
//                                if (idInfoData.docent_id.equals("")) { //카테고리리스트액티비티
//                                    intent = new Intent(HomeActivity.this, CategoryListActivity.class);
//                                    CategoryData categoryData = new CategoryData();
//                                    docentMemList.get_category_info(idInfoData.category_id, categoryData);
//                                    intent.putExtra("category", categoryData);
//
//                                } else if (!idInfoData.docent_id.equals("") && !idInfoData.category_id.equals("")) {
//                                    intent = new Intent(HomeActivity.this, DocentActivity.class);
//                                    HashMap<String, DocentData> map = new HashMap<>();
//                                    docentMemList.get_docent_info(idInfoData.category_id, map);
//                                    DocentData docentData = new DocentData();
//                                    docentData = map.get(idInfoData.docent_id);
//                                    intent.putExtra("docentObject", docentData);
//                                }
//                                mMinewBeaconManager.stopScan();
//                                applicationclass.setScanning(false);
//                                startActivity(intent);
//                                newItemDialog.dismiss();
//                                Log.d("check2", "Dialog dismiss");
//
//                            }
//                        }
//                )
//                .addButton("취소", // button text
//                        R.color.pdlg_color_white,  // button text color
//                        R.color.dialog_cancel,  // button background color
//                        new PrettyDialogCallback() {  // button OnClick listener
//                            @Override
//                            public void onClick() {
//                                newItemDialog.dismiss();
//
//                                Log.d("check2", "Dialog dismiss");
//
//                            }
//                        }
//                );
//        newItemDialog.show();

        if (!idInfoData.category_id.equals("")) {
            if (idInfoData.docent_id.equals("")) {
                CategoryData categoryData = new CategoryData();
                docentMemList.get_category_info(idInfoData.category_id, categoryData);
                newItemDialog = new BeaconDialog(HomeActivity.this, categoryData);

            } else {
                HashMap<String, DocentData> map = new HashMap<>();
                docentMemList.get_docent_info(idInfoData.category_id, map);
                DocentData docentData = map.get(idInfoData.docent_id);
                newItemDialog = new BeaconDialog(HomeActivity.this, docentData);
            }

            newItemDialog.setCancelable(false);
            newItemDialog.show();
        }
    }

    public void moveToCategoryActivity(CategoryData categoryData) {
        Intent intent = new Intent(HomeActivity.this, CategoryListActivity.class);
        intent.putExtra("category", categoryData);
        startActivity(intent);

        mMinewBeaconManager.stopScan();
        Application.getInstance().setScanning(false);
    }

    public void moveToDocentActivity(DocentData docentData) {
        Intent intent = new Intent(HomeActivity.this, DocentActivity.class);
        intent.putExtra("docentObject", docentData);
        startActivity(intent);

        mMinewBeaconManager.stopScan();
        Application.getInstance().setScanning(false);
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

        if (Application.getInstance().getToggleState()) {
            Log.d("check1", "home_onResume_start_scan");
            if (handler != null) {
                handler.sendEmptyMessageDelayed(0, 2500);
            }
            mMinewBeaconManager.startScan();
            Application.getInstance().setScanning(true);

        }

        if (newItemDialog != null && newItemDialog.isShowing()) {
            newItemDialog.dismiss();
            Log.d("check2", "Dialog dismiss");
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("check1", "home_onPause");
        prev_beacon = "";
        if (Application.getInstance().getToggleState()) {
            mMinewBeaconManager.stopScan();
            Application.getInstance().setScanning(false);
        }
        if (handler != null) {
            handler.removeMessages(0);
        }
    }

    @Override
    protected void onStop() {
        Log.d("check1", "home_onStop");

        super.onStop();
        if (Application.getInstance().getToggleState()) {
            mMinewBeaconManager.stopScan();
            Application.getInstance().setScanning(false);
        }
        if (handler != null) {
            handler.removeMessages(0);
        }


    }


    @Override
    protected void onDestroy() {
        Log.d("check1", "home_onDestroy");

        super.onDestroy();
        beaconArrayList.clear();
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

}
