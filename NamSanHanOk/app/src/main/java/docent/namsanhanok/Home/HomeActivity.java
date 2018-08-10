package docent.namsanhanok.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import docent.namsanhanok.Application;
import docent.namsanhanok.BackPressCloseHandler;
import docent.namsanhanok.Category.CategoryActivity;
import docent.namsanhanok.Category.CategoryResult;
import docent.namsanhanok.Docent.DocentActivity;
import docent.namsanhanok.Event.EventActivity;
import docent.namsanhanok.Info.InfoActivity;
//import docent.namsanhanok.IntentService;
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

import static com.minew.beacon.BluetoothState.BluetoothStatePowerOn;

public class HomeActivity extends AppCompatActivity {

    ImageButton settingBtn;
    ImageView menuBtn1, menuBtn2, menuBtn3, menuBtn4, menuBtn5;
    LabeledSwitch toggleBtn;
    PrettyDialog newItemDialog;

    private BackPressCloseHandler backPressCloseHandler;
    private MinewBeaconManager mMinewBeaconManager;
    private Vibrator vibrator;

    UserRssi comp = new UserRssi();
    ArrayList<String> beaconNumbers = new ArrayList<>(); //임의의 저장된 비콘넘버
    List<MinewBeacon> appearBeaconList = new ArrayList<>(); //인식된 비콘 리스트
    private Handler handler;
    String prev_beacon = "";

    private Application applicationclass;

    HomeData homeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        backPressCloseHandler = new BackPressCloseHandler(this);
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        beaconNumbers.add("15290");
        beaconNumbers.add("15282");

        applicationclass = (Application) getApplicationContext();

        init();
        networking();
        initBeaconManager();
        initBeaconListenerManager();

        showBeaconAlarm();
    }

    public void networking() {
        NetworkService service = Application.getInstance().getNetworkService();
        Call<HomeResult> request = service.getHomeResult(jsonToString());
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

    public void initBeaconManager() {
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
    }

    public void initBeaconListenerManager() {

        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            @Override
            public void onAppearBeacons(List<MinewBeacon> minewBeacons) {

            }

            @Override
            public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {
                for (int i = 0; i < minewBeacons.size(); i++) {
                    String disappearBeacon_minor = minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                    appearBeaconList.remove(minewBeacons.get(i));
                    if (disappearBeacon_minor.equals(prev_beacon))
                        prev_beacon = "";
                }
            }

            @Override
            public void onRangeBeacons(final List<MinewBeacon> minewBeacons) {
                addAppearBeacon(minewBeacons);
            }

            @Override
            public void onUpdateState(BluetoothState bluetoothState) {
                if (!isOnBluetooth() && toggleBtn.isOn()) {
                    toggleBtn.setOn(false);
                }
            }
        });
    }

    private void showBeaconAlarm() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (!appearBeaconList.isEmpty()) {
                    Collections.sort(appearBeaconList, comp);
                    for (int i = 0; i < appearBeaconList.size(); i++) {
                        int beacon_rssi = appearBeaconList.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getIntValue();
                        String beacon_minor = appearBeaconList.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();

                        Log.d("list", "핸들러 작동중...");

                        if (-70 < beacon_rssi && beacon_rssi < -30) {
                            if (!beacon_minor.equals(prev_beacon)) {
                                if (newItemDialog != null && newItemDialog.isShowing()) {
                                    newItemDialog.dismiss();
                                }
                                vibrator.vibrate(500);
                                showNewItemDialog();
                                prev_beacon = beacon_minor;
                                break;
                            }
                        } else {
                            appearBeaconList.remove(appearBeaconList.get(i));
                        }
                    }
                }
                this.sendEmptyMessageDelayed(0, 3000);
            }
        };
    }

    private void addAppearBeacon(List<MinewBeacon> minewBeacons) {
        if (!minewBeacons.isEmpty()) {
            Collections.sort(minewBeacons, comp);

            for (int i = 0; i < minewBeacons.size(); i++) {
                String beacon_minor = minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                int beacon_rssi = minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getIntValue();

                for (String beacon_number : beaconNumbers) {
                    if (beacon_minor.equals(beacon_number)) {
                        if (!appearBeaconList.contains(minewBeacons.get(i))) { // 중복 제거
                            appearBeaconList.add(minewBeacons.get(i));
                        }
                        Log.d("list", beacon_minor + ", " + beacon_rssi);

                    }

                }
            }
        }
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
                    handler.sendEmptyMessage(0);

                } else if (isOnBluetooth() && toggleBtn.isOn()) { // bluetooth==true, toggle버튼 off
                    applicationclass.setToggleState(false);
                    applicationclass.setScanning(false);
                    if (mMinewBeaconManager != null) {
                        mMinewBeaconManager.stopScan();
                    }
                    handler.removeMessages(0);
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

    public void showNewItemDialog() {
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
                                Intent intent = new Intent(HomeActivity.this, DocentActivity.class);
                                intent.putExtra("docent_title", "temp");
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
        if(toggleBtn.isOn()) {
            mMinewBeaconManager.startScan();
            applicationclass.setScanning(true);
            handler.sendEmptyMessage(0);
        }
//        if(newItemDialog.isShowing()) newItemDialog.dismiss();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (applicationclass.getScanning()) {
            mMinewBeaconManager.stopScan();
            applicationclass.setScanning(false);
        }
        handler.removeMessages(0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        appearBeaconList.clear();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public String jsonToString() {
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
}
