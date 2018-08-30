package docent.namsanhanok.Category;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import docent.namsanhanok.AppUtility.BeaconDialog;
import docent.namsanhanok.Application;
import docent.namsanhanok.Docent.DocentActivity;
import docent.namsanhanok.Docent.DocentData;
import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.Home.UserRssi;
import docent.namsanhanok.Manager.DocentMemList;
import docent.namsanhanok.Manager.IDInfoData;
import docent.namsanhanok.R;
import libs.mjn.prettydialog.PrettyDialog;
import retrofit2.http.HEAD;

import static com.minew.beacon.BluetoothState.BluetoothStatePowerOn;

public class CategoryListActivity extends AppCompatActivity {
    public static CategoryListActivity categoryListActivity;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CategoryListAdapter categoryListAdapter;

    ImageButton homeBtn;
    TextView category_list_toolbar_title;
    ImageView simple_image;
    TextView category_list_title;
    TextView countText;
    TextView category_text_info;

    private ArrayList<DocentData> docentDataList;

    DocentMemList docentMemList;
    CategoryData categoryData;
    DocentData docentData;

    Vibrator vibrator;
    BeaconDialog newItemDialog = null;
    MinewBeaconManager mMinewBeaconManager = null;
    static List<MinewBeacon> minewBeacons1 = new ArrayList<>();
    String prev_beacon = "";
    UserRssi comp = new UserRssi();
    Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Intent secondIntent = getIntent();
        categoryData = (CategoryData) secondIntent.getSerializableExtra("category");

        categoryListActivity = CategoryListActivity.this;
        init();
        initBeaconManager();

        setCategoryContent(categoryData);
        setDocentList(categoryData);
    }

    public void initBeaconManager() {
        mMinewBeaconManager = new MinewBeaconManager();

        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            @Override
            public void onAppearBeacons(List<MinewBeacon> minewBeacons) {

            }

            @Override
            public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {
                for (MinewBeacon minewBeacon : minewBeacons) {
                    String deviceName = minewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                    Log.d("check2", "사라진다 : " + deviceName);

                    if (minewBeacons1.contains(minewBeacon))
                        minewBeacons1.remove(minewBeacon);
                }
            }

            @Override
            public void onRangeBeacons(List<MinewBeacon> minewBeacons) {
                for (int i = 0; i < minewBeacons.size(); i++) {
                    String beacon_minor = minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                    Log.d("check", "CategoryList_beacon_minor : " + minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue());

                    IDInfoData idInfoData = new IDInfoData();
                    if (docentMemList.check_beacon_number(beacon_minor, idInfoData)) {
                        synchronized (this) {
                            if (!minewBeacons1.contains(minewBeacons.get(i))) {
                                minewBeacons1.add(minewBeacons.get(i));
                                Log.d("check", "categoryList_add : " + minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue());
                            }
                            for (int j = 0; j < minewBeacons1.size(); j++) {
                                Log.d("minewBeaconList", "\n" + "category_List_minewBeacons1 " + (j + 1) + "번째 : " + minewBeacons1.get(j).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue());
                            }
                        }
                    }
                }

                if (!minewBeacons1.isEmpty()) {
                    Collections.sort(minewBeacons1, comp);

                    String beacon_minor;
                    int beacon_rssi;
                    synchronized (this) {
                        beacon_minor = minewBeacons1.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                        beacon_rssi = minewBeacons1.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getIntValue();
                    }

                    Log.d("check", "categoryList_beacon_minor" + beacon_minor);

                    if (beacon_rssi > -70 && beacon_rssi < -30) {
                        IDInfoData idInfoData = new IDInfoData();
                        if (!beacon_minor.equals(prev_beacon) && !beacon_minor.equals(categoryData.beacon_number)) {
                            if (newItemDialog != null && newItemDialog.isShowing()) {
                                newItemDialog.dismiss();
                            }

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
                if (!isOnBluetooth()) {
                    Application.getInstance().setScanning(false);
                    if (mMinewBeaconManager != null) {
                        mMinewBeaconManager.stopScan();
                        handler.removeMessages(0);
                    }
                } else if (isOnBluetooth()) {
                    if (!Application.getInstance().getScanning()) {
                        Application.getInstance().isScanning = true;
                        handler.sendEmptyMessage(0);
                        try {
                            mMinewBeaconManager.startScan();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
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

            }
        }, 2500);
    }

    public void setCategoryContent(CategoryData categoryData) {
        //카테고리 대표 이미지 설정
        Glide.with(CategoryListActivity.this)
                .load(Environment.getExternalStorageDirectory() + categoryData.category_image_url)
                .into(simple_image);

        category_list_toolbar_title.setText(categoryData.category_title); //툴바 타이틀
        category_list_title.setText(categoryData.category_title + " 소개"); //소개 타이틀
        category_text_info.setText(categoryData.category_detail_info); //카테고리 설명

        this.categoryData = categoryData;
    }

    public void setDocentList(CategoryData categoryData) {
        HashMap<String, DocentData> map = new HashMap<String, DocentData>();
        docentMemList.get_docent_info(categoryData.category_id, map);
        docentDataList = new ArrayList<>();

        Iterator<String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            docentData = map.get(key);
            docentDataList.add(docentData);
        }

        categoryListAdapter.setAdapter(docentDataList);
        String count = "전시품 총 " + String.valueOf(docentDataList.size()) + "개";
        countText.setText(count);
    }

    public void showNewItemDialog(final IDInfoData idInfoData) {
        if (newItemDialog != null && newItemDialog.isShowing()) {
            newItemDialog.dismiss();
        }

        if (!idInfoData.category_id.equals("")) {
            if (idInfoData.docent_id.equals("")) {
                CategoryData categoryData = new CategoryData();
                docentMemList.get_category_info(idInfoData.category_id, categoryData);
                newItemDialog = new BeaconDialog(CategoryListActivity.this, categoryData);

            } else {
                HashMap<String, DocentData> map = new HashMap<>();
                docentMemList.get_docent_info(idInfoData.category_id, map);
                DocentData docentData = map.get(idInfoData.docent_id);
                newItemDialog = new BeaconDialog(CategoryListActivity.this, docentData);
            }

            newItemDialog.setCancelable(false);
            newItemDialog.show();
        }
    }

    public void stopScan() {
        mMinewBeaconManager.stopScan();
        Application.getInstance().setScanning(false);
    }

    public void moveToDocentActivity(DocentData docentData) {
        Intent intent = new Intent(CategoryListActivity.this, DocentActivity.class);
        intent.putExtra("docentObject", docentData);
        startActivity(intent);
        finish();
    }

    public void init() {
        docentMemList = DocentMemList.getInstance();
        handler = new Handler();

        Toolbar categoryListToolbar = (Toolbar) findViewById(R.id.category_list_Toolbar);
        setSupportActionBar(categoryListToolbar);

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        simple_image = (ImageView) findViewById(R.id.simple_image);
        category_list_title = (TextView) findViewById(R.id.category_list_title);
        countText = (TextView) findViewById(R.id.countText);
        category_text_info = (TextView) findViewById(R.id.category_text_info);
        category_list_toolbar_title = (TextView) findViewById(R.id.docentTitle);

        recyclerView = (RecyclerView) findViewById(R.id.category_list_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        categoryListAdapter = new CategoryListAdapter(getApplicationContext(), docentDataList);
        recyclerView.setAdapter(categoryListAdapter);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeBtn:
                Intent intent = new Intent(CategoryListActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("check", "categoryList_onResume");

        if (isOnBluetooth()) {
            if (Application.getInstance().getToggleState()) {
                mMinewBeaconManager.startScan();
                if (handler != null) {
                    handler.sendEmptyMessage(0);
                }

            } else if (!Application.getInstance().getToggleState()) {
                Application.getInstance().setScanning(false);
                try {
                    mMinewBeaconManager.stopScan();
                    if (handler != null) handler.removeMessages(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (!isOnBluetooth()) {
            Application.getInstance().setScanning(false);
            if (mMinewBeaconManager != null) {
                mMinewBeaconManager.stopScan();
                if (handler != null) handler.removeMessages(0);
            }
        }
    }

    public void onPause() {
        Log.d("check", "categoryList_onPause");
        super.onPause();

        prev_beacon = "";
        if (Application.getInstance().getToggleState()) {
            mMinewBeaconManager.stopScan();
            Application.getInstance().setScanning(false);
        }
        if (handler != null) {
            handler.removeMessages(0);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

            }

        }, 100);
    }

    @Override
    protected void onStop() {
        Log.d("check", "categoryList_onStop");

        super.onStop();

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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
