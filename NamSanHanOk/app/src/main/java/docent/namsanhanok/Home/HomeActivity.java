package docent.namsanhanok.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.angads25.toggle.LabeledSwitch;
import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import docent.namsanhanok.BackPressCloseHandler;
import docent.namsanhanok.Category.CategoryActivity;
import docent.namsanhanok.Event.EventActivity;
import docent.namsanhanok.Info.InfoActivity;
import docent.namsanhanok.Notice.NoticeActivity;
import docent.namsanhanok.Question.QuestionWriteActivity;
import docent.namsanhanok.R;
import docent.namsanhanok.Setting.SettingActivity;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static com.minew.beacon.BluetoothState.BluetoothStatePowerOn;

public class HomeActivity extends AppCompatActivity {

    ImageButton settingBtn;
    ImageView menuBtn1, menuBtn2, menuBtn3, menuBtn4, menuBtn5;
    LabeledSwitch toggleBtn;

    private BackPressCloseHandler backPressCloseHandler;
    private MinewBeaconManager mMinewBeaconManager;
    private boolean isScanning;

    UserRssi comp = new UserRssi();
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        backPressCloseHandler = new BackPressCloseHandler(this);
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);

        init();
        initBeaconManager();
        initBeaconListenerManager();
    }

    public void initBeaconManager() {
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
    }

    public void initBeaconListenerManager() {

//        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (mMinewBeaconManager != null) {
//                    BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
//                    switch (bluetoothState) {
//                        case BluetoothStateNotSupported:
//                            Toast.makeText(getApplicationContext(), "Not Support BLE", Toast.LENGTH_SHORT).show();
//                            finish();
//                            break;
//                        case BluetoothStatePowerOff:
//                            toggleBtn.setOn(true);
//                            Toast.makeText(getApplicationContext(), "블루투스가 꺼져있습니다", Toast.LENGTH_SHORT).show();
//                            showAlertDialog();
//                            return;
//                        case BluetoothStatePowerOn:
//                            Log.d("check1", "bluetoothStatePowerOn");
//                            break;
//                    }
//
//                    if (isScanning) {
//                        Log.d("check1", "isScanning==true");
//
//                        isScanning = false;
//                        toggleBtn.setOn(true);
//                        if (mMinewBeaconManager != null) {
//                            Log.d("check1", "stopScan()");
//
//                            mMinewBeaconManager.stopScan();
//                        }
//                    } else {
//                        Log.d("check1", "isScanning==false");
//                        isScanning = true;
//                        toggleBtn.setOn(false);
//                        try {
//                            Log.d("check1", "startScan()");
//                            mMinewBeaconManager.startScan();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }

                mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
                @Override
                public void onAppearBeacons (List < MinewBeacon > minewBeacons) {
                    Log.d("check1", "onAppearBeacons() : " + minewBeacons.get(0));

                }

                @Override
                public void onDisappearBeacons (List < MinewBeacon > minewBeacons) {

                }

                @Override
                public void onRangeBeacons ( final List<MinewBeacon> minewBeacons){
                    Log.d("check1", "onRangeBeaons : " + minewBeacons.size());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Collections.sort(minewBeacons, comp);
                            Log.e("tag", state + "");
                            if (state == 1 || state == 2) {
                            } else {
                                Log.d("check1", "onRangeBeacons : " + minewBeacons.size());
                                if (minewBeacons.size() > 0) {
                                    ArrayList<String> beaconList = new ArrayList<>();

                                    for (int i = 0; i < minewBeacons.size(); i++) {
                                        if (minewBeacons.get(i) != null) {
                                            beaconList.add(minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue());

                                        }
                                    }

                                    Log.d("check1", "onRangeBeacons : " + "\n" +
                                            beaconList.toString());
                                }
                            }
                        }
                    });
                }


            @Override
            public void onUpdateState(BluetoothState bluetoothState) {
                if (!isOnBluetooth() && toggleBtn.isOn()) {
                    toggleBtn.setOn(false);
                }
            }
        });

    }

    private boolean isOnBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        if (bluetoothState == BluetoothStatePowerOn) {
            return true;
        } else {
            return false;
        }
    }

    public void showAlertDialog() {
        final PrettyDialog alertDialog = new PrettyDialog(HomeActivity.this);
        alertDialog
                .setMessage("자동전시안내를 이용하시려면 Bluetooth를 켜 주세요.")
                .setIcon(R.drawable.pdlg_icon_info)
                .setIconTint(R.color.pdlg_color_blue)
                .addButton("확인", // button text
                        R.color.pdlg_color_white,  // button text color
                        R.color.pdlg_color_blue,  // button background color
                        new PrettyDialogCallback() {  // button OnClick listener
                            @Override
                            public void onClick() {
                                alertDialog.dismiss();
                            }
                        }
                );
        alertDialog.show();
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
                startActivity(intent);
                break;

            case R.id.menuBtn2: //세시/행사
                intent = new Intent(getApplicationContext(), EventActivity.class);
                startActivity(intent);
                break;

            case R.id.menuBtn3: //알리는 말씀
                intent = new Intent(HomeActivity.this, NoticeActivity.class);
                startActivity(intent);
                break;

            case R.id.menuBtn4: //문의하기
                intent = new Intent(HomeActivity.this, QuestionWriteActivity.class);
                startActivity(intent);
                break;

            case R.id.menuBtn5: //이용안내
                intent = new Intent(HomeActivity.this, InfoActivity.class);
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


        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnBluetooth() && !toggleBtn.isOn()) {
                    toggleBtn.setOn(true);
                    showAlertDialog();
                } else if (isOnBluetooth() && !toggleBtn.isOn()) {
                    isScanning = true;
                    mMinewBeaconManager.startScan();
                } else if (isOnBluetooth() && toggleBtn.isOn()) {
                    isScanning = false;
                    if (mMinewBeaconManager != null) {
                        mMinewBeaconManager.stopScan();
                        Toast.makeText(HomeActivity.this, "scanning stop!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isScanning) {
            mMinewBeaconManager.stopScan();
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQUEST_ENABLE_BT:
//                break;
//        }
//    }

}
