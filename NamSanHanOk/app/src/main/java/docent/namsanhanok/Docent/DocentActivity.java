package docent.namsanhanok.Docent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import docent.namsanhanok.Application;
import docent.namsanhanok.Category.CategoryData;
import docent.namsanhanok.Category.CategoryListActivity;
import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.Home.UserRssi;
import docent.namsanhanok.Location.LocationActivity;
import docent.namsanhanok.Manager.DocentMemList;
import docent.namsanhanok.Manager.IDInfoData;
import docent.namsanhanok.NetworkService;
import docent.namsanhanok.R;
import docent.namsanhanok.ShowWiFiMonitor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HEAD;

import static com.google.android.exoplayer2.upstream.HttpDataSource.*;
import static com.minew.beacon.BluetoothState.BluetoothStatePowerOn;
import static docent.namsanhanok.AppUtility.AppUtility.deepClone;

public class DocentActivity extends AppCompatActivity {

    ImageButton homeBtn;
    TextView docentName;
    ImageView docentImage;
    ImageButton audioBtn;
    ImageButton locationBtn;
    LinearLayout bottom_audio_layout;
    TextView audioTxt;
    TextView locaTxt;
    TextView docentTitle;
    TextView docentDetails;

    //videoPlayer
    SimpleExoPlayer videoPlayer;
    SimpleExoPlayerView playerView;
    ImageButton exo_play;
    ImageView exo_thumbnail;

    Dialog fullscreenDialog;
    boolean isPlayerFullscreen = false;
    ImageView fullscreenIcon;
    FrameLayout fullscreenButton;
    LinearLayout docentVideo_Layout;

    //audioPlayer
    MediaPlayer audioPlayer;
    ImageButton playAudioBtn;
    SeekBar seekbar;
    TextView audioTotalTime;
    TextView audioCurrentTime;

    //상세보기
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DocentAdapter docentAdpater;
    LinearLayout docentDetails_Layout;

    LinearLayout go_new_docent_layout;
    TextView confirm_go_new_docent;
    private Vibrator vibrator;
    private MinewBeaconManager mMinewBeaconManager;
    UserRssi comp = new UserRssi();
    static List<MinewBeacon> beaconArrayList = new ArrayList<>();

    DocentMemList docentMemList;

    View docentSpace;
    View audioSpace;

//    //지울 것
//    TextView go_new_docent_content;

    private Handler handler1;
    private Handler handler2;
    String prev_beacon = "";

    //서버 네트워크
    NetworkService service;
    private ArrayList<DocentDetailData> docentDetailDataList;
    private TextView docentExplanation;
    String docent_id;
    String audio_url;
    String video_url;

    static boolean newDocent;

    DocentData docentObject;
    IDInfoData lastIDinfoData;

    //Animation
    Animation bottomUpAnimation;
    Animation topDownAnimation;
    CategoryListActivity categoryListActivity;

    //서버연결확인
//    Socket socket;
//    BufferedWriter networkWriter;
//    BufferedReader networkReader;
//    boolean isOnServer;

    ShowWiFiMonitor receiver;
    public DocentActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docent);

        init();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new ShowWiFiMonitor(this);
        registerReceiver(receiver, filter);

        if (newDocent == true) {
            Intent intent = getIntent();
            final String beaconNumber = intent.getStringExtra("beaconNumber");
            CategoryData categoryData = new CategoryData();
            docentObject = new DocentData();
            IDInfoData idInfoData = new IDInfoData();
            if (docentMemList.check_beacon_number(beaconNumber, idInfoData) == false) {
                Toast.makeText(getApplicationContext(), "알수없는 비콘 정보입니다.", Toast.LENGTH_SHORT).show();
            } else {
                categoryData = (CategoryData) deepClone(docentMemList.getCategorylist().get(idInfoData.category_id));
                //categoryData = SerializationUtils.clone(categorylist_.get(idInfoData.category_id));
                //categoryData = categorylist_.get(idInfoData.category_id);
                if (idInfoData.docent_id.length() > 0) {
                    //docentData = categorylist_.get(idInfoData.category_id).docentlist.get(idInfoData.docent_id);
                    //docentData = SerializationUtils.clone(categorylist_.get(idInfoData.category_id).docentlist.get(idInfoData.docent_id));
                    docentObject = (DocentData) deepClone(docentMemList.getCategorylist().get(idInfoData.category_id).docentlist.get(idInfoData.docent_id));
                }

//            if (docentMemList.get_docent_info(beaconNumber, categoryData, docentObject) == false) {
//                Toast.makeText(getApplicationContext(), "알수없는 비콘 정보입니다.", Toast.LENGTH_SHORT).show();
//            }
            }
        } else {
            Intent docentObjectIntent = getIntent();
            docentObject = (DocentData) docentObjectIntent.getSerializableExtra("docentObject");
        }
        setDocentObject(docentObject);
        networking4();

        initBeaconManager();
        initBeaconListenerManager();
//        setRecyclerView();

        docentImage.setFocusableInTouchMode(true);
        docentImage.requestFocus();

        categoryListActivity = (CategoryListActivity) CategoryListActivity.categoryListActivity;

        newDocent = false;



    }

    public void setDocentObject(DocentData docentObject) {

        docentTitle.setText(docentObject.docent_title);

        Glide.with(getApplicationContext())
                .load(Environment.getExternalStorageDirectory() + docentObject.docent_image_url)
                .into(docentImage);

        docentName.setText(docentObject.docent_title);
        docentExplanation.setText(docentObject.docent_content_info);

        audio_url = docentObject.docent_audio_url;
        video_url = docentObject.docent_vod_url;

//        ServerConneted();

        Log.d("check1", "isOnServer : " + Application.getInstance().getOnServer());

        if (audio_url.equals("") || !Application.getInstance().getOnServer()) {
            audioBtn.setBackgroundResource(R.drawable.no_headphones);
            audioPlayer = null;
        } else {
            setAudioPlayer();
        }

        if (video_url.equals("") || !Application.getInstance().getOnServer()) {
            docentVideo_Layout.setVisibility(View.GONE);
        } else {
            setVideoPlayer();
        }

        docent_id = docentObject.docent_id;

        Glide.with(getApplicationContext())
                .load(Environment.getExternalStorageDirectory() + docentObject.docent_image_url)
                .apply(new RequestOptions().centerCrop())
                .into(exo_thumbnail);

    }

    //docent detail list
    public void networking4() {
        final Call<DocentDetailResult> docentDetailListResult = service.getDocentDetailResult(getDocentDetailInfo("docent_detail_list", docent_id));
        docentDetailListResult.enqueue(new Callback<DocentDetailResult>() {
            @Override
            public void onResponse(Call<DocentDetailResult> call, Response<DocentDetailResult> response) {
                if (response.isSuccessful()) {
                    docentDetailDataList = response.body().docent_detail_info;

                    if (docentDetailDataList.isEmpty()) {
                        docentDetails_Layout.setVisibility(View.GONE);
                    } else {
                        setRecyclerView();
                    }

                    Log.d("check1", "docentDetailDataList : " + docentDetailDataList.toString());
                    Log.d("check1", "docentDetailDataList 크기: " + docentDetailDataList.size());
                }
            }

            @Override
            public void onFailure(Call<DocentDetailResult> call, Throwable t) {
                Log.d("check1", "실패 : " + t.getMessage());
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

    public void initBeaconManager() {
        mMinewBeaconManager = new MinewBeaconManager();
    }

    public void initBeaconListenerManager() {

        if (isOnBluetooth()) {
            if (Application.getInstance().getToggleState()) { // scan중
                Log.d("check1", "docent_isScanning : startScan");

                mMinewBeaconManager.startScan();
                if (handler1 != null) {
                    handler1.sendEmptyMessageDelayed(0, 2200);
                }
            } else if (!Application.getInstance().getToggleState()) { //bluetooth는 on인데 Scanning이 안되고 있다
                Application.getInstance().setScanning(false);
                try {
                    mMinewBeaconManager.stopScan();
                    if (handler1 != null) {
                        handler1.sendEmptyMessage(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (!isOnBluetooth()) { // bluetooth == false
            Application.getInstance().setScanning(false);
            if (mMinewBeaconManager != null) {
                mMinewBeaconManager.stopScan();
                if (handler1 != null) {
                    handler1.removeMessages(0);

                }
            }
        }

        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            @Override
            public void onAppearBeacons(List<MinewBeacon> minewBeacons) {

            }

            @Override
            public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {
                Log.d("minew", "docent_disappear");
                for (MinewBeacon minewBeacon : minewBeacons) {
                    String deviceName = minewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                    Log.d("check2", "docent_사라진다 : " + deviceName);

                    if (beaconArrayList.contains(minewBeacons))
                        beaconArrayList.remove(minewBeacons);
                }

            }

            @Override
            public void onRangeBeacons(final List<MinewBeacon> minewBeacons) {
                //categoryList
                for (int i = 0; i < minewBeacons.size(); i++) {
                    String beacon_minor = minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();

                    IDInfoData idInfoData = new IDInfoData();
                    if (docentMemList.check_beacon_number(beacon_minor, idInfoData)) {
                        synchronized (this) {
                            if (!beaconArrayList.contains(minewBeacons.get(i))) {
                                beaconArrayList.add(minewBeacons.get(i));
                                Log.d("beacon", "add : " + minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue());
                            }
                            for (int j = 0; j < beaconArrayList.size(); j++) {
                                Log.d("check", "\n" + "docent_beaconArrayList " + (j + 1) + "번째 : " + beaconArrayList.get(j).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue());
                            }
                        }
                    }

                }

//                addAppearBeacon(minewBeacons);
                if (!beaconArrayList.isEmpty()) {
                    Collections.sort(beaconArrayList, comp);
                    for (int i = 0; i < beaconArrayList.size(); i++) {
                        Log.d("check2", "\n" + "docent_beaconArrayList " + (i + 1) + "번째 : " + beaconArrayList.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue());
                    }

                    String beacon_minor;
                    int beacon_rssi;
                    synchronized (this) {
                        beacon_minor = beaconArrayList.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                        beacon_rssi = beaconArrayList.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getIntValue();
                    }

                    if (beacon_rssi > -70 && beacon_rssi < -30 && !beacon_minor.equals(docentObject.beacon_number)) {
                        IDInfoData idInfoData = new IDInfoData();
                        if (!beacon_minor.equals(prev_beacon)) {
                            Log.d("check2", "beacon_minor : " + beacon_minor);
                            Log.d("check2", "prev_beacon2 : " + prev_beacon);
                            if (docentMemList.check_beacon_number(beacon_minor, idInfoData)) {
                                Log.d("check2", "idInfoData docent_id: " + idInfoData.docent_id);
                                Log.d("check2", "idInfoData category_id: " + idInfoData.category_id);

                                synchronized (this) {
                                    setIDInfoData(idInfoData);
                                    showBeaconAlarm();
                                    prev_beacon = beacon_minor;
                                }
                            }
                        }
                    }
                }

            }

            @Override
            public void onUpdateState(BluetoothState bluetoothState) {
                if (!isOnBluetooth()) { // bluetooth==false
                    Application.getInstance().setScanning(false);
                    if (mMinewBeaconManager != null) {
                        mMinewBeaconManager.stopScan();
                        if (handler1 != null) {
                            handler1.removeMessages(0);
                        }
                    }
                } else if (isOnBluetooth()) {
                    if (Application.getInstance().getScanning()) { // scan중
                    } else if (!Application.getInstance().getScanning()) { //bluetooth는 on인데 Scanning이 안되고 있다
                        Application.getInstance().isScanning = true;
                        if (handler1 != null) {
                            handler1.sendEmptyMessage(0);
                        }
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

    public void showBeaconAlarm() {
        if (handler2 != null) {
            handler2.removeMessages(0);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                showNewItemDialog();

                Log.d("check1", "handler 작동중...");
            }
        }, 2000);

    }

    private void setIDInfoData(IDInfoData idInfoData) {
        this.lastIDinfoData = idInfoData;
        Log.d("check3", "lastIDinfo_docent_id : " + lastIDinfoData.docent_id);
        Log.d("check3", "lastIDinfo_category_id : " + lastIDinfoData.category_id);
    }

    public void showNewItemDialog() {
        Log.d("check1", "showNewItemDialog 시작");

        handler2 = new Handler();
        handler2.sendEmptyMessage(0);

        vibrator.vibrate(500);

        Log.d("check4", "docent_setVisible");
        go_new_docent_layout.setVisibility(View.VISIBLE);
        docentSpace.setVisibility(View.VISIBLE);

        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("check4", "8초뒤 docent_GONE");
                //지연시키길 원하는 밀리초 뒤에 동작
                go_new_docent_layout.setVisibility(View.GONE);
                docentSpace.setVisibility(View.GONE);
            }
        }, 8000); // delayMills == 지연원하는 밀리초

    }

    public void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.docent_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        Log.d("check1", "setRecyclerView");
        docentAdpater = new DocentAdapter(getApplicationContext(), docentDetailDataList);
        recyclerView.setAdapter(docentAdpater);
    }


    public void setVideoPlayer() {


        //Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        DefaultAllocator allocator = new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE);

        DefaultLoadControl loadControl = new DefaultLoadControl(allocator,
                1000,
                1000,
                1000,
                1000,
                -1,
                true);


        //Create the player
        videoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
//        videoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);


        //Attaching the player to a view
        playerView.setPlayer(videoPlayer);

        //Preparing the player
        String videoUrl = video_url;
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(DocentActivity.this,
                Util.getUserAgent(DocentActivity.this, "NamsanHanokDocent"), defaultBandwidthMeter);
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoUrl));

        videoPlayer.prepare(videoSource);


        exo_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Application.getInstance().checkInternet()) {
                    if (!videoPlayer.getPlayWhenReady() && exo_thumbnail.getVisibility() != View.GONE) {
                        exo_thumbnail.setVisibility(View.GONE);
                    }
                    videoPlayer.setPlayWhenReady(true);
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.wifi_disconnect, Toast.LENGTH_LONG).show();
                }
                if (audioPlayer != null && audioPlayer.isPlaying()) { //비디오 재생시 오디오 일시정지
                    audioPlayer.pause();
                    playAudioBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_48dp);
                }
            }

        });

        initFullscreenDialog();
        initFullscreenButton();
//            videoPlayer.setPlayWhenReady(true);

    }

    public void setAudioPlayer() {
        try {
//            audioPlayer = MediaPlayer.create(this, Uri.parse(audio_url));
            audioPlayer = new MediaPlayer();
            audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            audioPlayer.setDataSource(audio_url);
            audioPlayer.prepare();
//            audioPlayer.prepareAsync();

            audioPlayer.setLooping(true); //무한 반복
            audioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { //총길이 세팅
                @Override
                public void onPrepared(MediaPlayer music) {
                    String minute = String.format("%2d", ((music.getDuration()) / 1000 / 60) % 60);
                    String second = String.format("%2d", ((music.getDuration()) / 1000) % 60);
                    audioTotalTime.setText(minute + ":" + second); //총 재생시간
                    audioCurrentTime.setText("0:00"); //현재 재생시간
                }
            });

            seekbar.setMax(audioPlayer.getDuration()); //seekbar의 총길이를 audioPlayer의 총길이로 설정
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //사용자가 seekbar를 움직여서 값이 변했다면 true, 재생위치를 바꿈 (seekTo)
                    if (fromUser) {
                        audioPlayer.seekTo(progress);
                        String currentTime = String.format("%d:%02d", (audioPlayer.getCurrentPosition() / 1000 / 60) % 60, (audioPlayer.getCurrentPosition() / 1000) % 60);
                        audioCurrentTime.setText(currentTime);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

        } catch (IOException e) {

        }
    }

    public void onClick(View v) {
        Intent intent;
            switch (v.getId()) {
                case R.id.playAudioBtn:
                    if (audioPlayer.isPlaying()) {
                        audioPlayer.pause();
                        playAudioBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_48dp);

                    } else {
                        audioPlayer.start();
                        playAudioBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);

                        if (videoPlayer != null && videoPlayer.getPlayWhenReady()) { //영상이 play 상태라면
                            videoPlayer.setPlayWhenReady(false); //영상 일시정지
                        }

                        Thread();
                    }
                    break;

                case R.id.audioBtn: //오디오 이미지버튼을 클릭했을 때 오디오 레이아웃 보이기
                case R.id.audioTxt:
                        if (audioPlayer != null) {
                            if(Application.getInstance().checkInternet() || bottom_audio_layout.getVisibility()==View.VISIBLE
                                    || audioPlayer.isPlaying()) {
                                if (bottom_audio_layout.getVisibility() == View.GONE) {
                                    Log.d("check4", "audio_visible");
                                    bottom_audio_layout.setVisibility(View.VISIBLE);
                                    bottom_audio_layout.startAnimation(bottomUpAnimation);
                                } else if (bottom_audio_layout.getVisibility() == View.VISIBLE) {
                                    Log.d("check4", "audio_gone");
                                    bottom_audio_layout.startAnimation(topDownAnimation);
                                }
                            }
                            else{
                                Log.d("check4", "인터넷 연결안되있고, 오디오 null아니니?");
                                Toast.makeText(getApplicationContext(), R.string.wifi_disconnect, Toast.LENGTH_LONG).show();
                            }
                        }
                        break;



                case R.id.locationBtn:
                case R.id.locationTxt:
                    intent = new Intent(getApplicationContext(), LocationActivity.class);
                    intent.putExtra("docentData", docentObject);
                    startActivity(intent);
                    break;

                case R.id.homeBtn:
                    if(Application.getInstance().checkInternet()) {
                        intent = new Intent(DocentActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), R.string.wifi_disconnect, Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.confirm_go_new_docent:
                    Log.d("check3", "case_docent_id : " + lastIDinfoData.docent_id);
                    Log.d("check3", "case_category_id : " + lastIDinfoData.category_id);

                    if(Application.getInstance().checkInternet()) {
                        if (lastIDinfoData.docent_id.equals("")) { //카테고리리스트액티비티
                            Log.d("check3", "categoryList로 가자");

                            intent = new Intent(getApplicationContext(), CategoryListActivity.class);
                            CategoryData categoryData = new CategoryData();
                            docentMemList.get_category_info(lastIDinfoData.category_id, categoryData);
                            intent.putExtra("category", categoryData);
                            if (categoryListActivity != null) {
                                categoryListActivity.finish();
                            }
                            finish();
                            startActivity(intent);

                        } else if (!lastIDinfoData.docent_id.equals("") && !lastIDinfoData.category_id.equals("")) {
                            Log.d("check3", "docent_id로 가자");

                            intent = new Intent(getApplicationContext(), DocentActivity.class);
                            HashMap<String, DocentData> map = new HashMap<>();
                            docentMemList.get_docent_info(lastIDinfoData.category_id, map);
                            DocentData docentData = new DocentData();
                            docentData = map.get(lastIDinfoData.docent_id);
                            intent.putExtra("docentObject", docentData);
                            intent.putExtra("beaconNumber", docentData.beacon_number);

                            newDocent = true;
                            finish();
                            startActivity(intent);
                        }

                        go_new_docent_layout.setVisibility(View.GONE);
                        bottom_audio_layout.setVisibility(View.GONE);

                        mMinewBeaconManager.stopScan();
                        Application.getInstance().setScanning(false);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), R.string.wifi_disconnect, Toast.LENGTH_LONG).show();
                    }
                    break;
            }
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String currentTime = String.format("%d:%02d", (audioPlayer.getCurrentPosition() / 1000 / 60) % 60, (audioPlayer.getCurrentPosition() / 1000) % 60);
            audioCurrentTime.setText(currentTime);
        }
    };

    public void Thread() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                while (audioPlayer.isPlaying()) {
                    try {
                        Thread.sleep(1000);
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //현재 음악 재생 위치 가져오기
                    seekbar.setProgress(audioPlayer.getCurrentPosition());
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.docentToolbar);
        setSupportActionBar(toolbar);

        docentMemList = DocentMemList.getInstance();
        service = Application.getInstance().getNetworkService();

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        docentName = (TextView) findViewById(R.id.docentName);
        docentImage = (ImageView) findViewById(R.id.docentImage);
        audioBtn = (ImageButton) findViewById(R.id.audioBtn);
        locationBtn = (ImageButton) findViewById(R.id.locationBtn);
        playerView = (SimpleExoPlayerView) findViewById(R.id.playerView);
        bottom_audio_layout = (LinearLayout) findViewById(R.id.bottom_audio_layout);
        playAudioBtn = (ImageButton) findViewById(R.id.playAudioBtn);
        exo_play = (ImageButton) findViewById(R.id.exo_play);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        audioTotalTime = (TextView) findViewById(R.id.audioTotalTime);
        audioCurrentTime = (TextView) findViewById(R.id.audioCurrentTime);
        docentTitle = (TextView) findViewById(R.id.docentTitle);
        exo_thumbnail = (ImageView) findViewById(R.id.exo_thumbnail);
        docentDetails = (TextView) findViewById(R.id.docentDetails);

        audioTxt = (TextView) findViewById(R.id.audioTxt);
        locaTxt = (TextView) findViewById(R.id.locationTxt);

        go_new_docent_layout = (LinearLayout) findViewById(R.id.bottom_get_new_docent);
        confirm_go_new_docent = (TextView) findViewById(R.id.confirm_go_new_docent);

        SpannableString content = new SpannableString("확인하기");
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        confirm_go_new_docent.setText(content);

        docentDetails_Layout = (LinearLayout) findViewById(R.id.docentDetails_Layout);
        docentVideo_Layout = (LinearLayout) findViewById(R.id.docentVideo_Layout);
        docentExplanation = (TextView) findViewById(R.id.docentExplanation);

        //지울 것
        docentSpace = (View) findViewById(R.id.newDocent_Space);
        audioSpace = (View) findViewById(R.id.Audio_Space);

        bottomUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
        topDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_down);
        //리스너 등록, 끝나는 시점 알 수 있음.
        bottomUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                audioSpace.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        topDownAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("check4", "topdown animation_endl");
                bottom_audio_layout.setVisibility(View.GONE);
                audioSpace.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (videoPlayer != null) videoPlayer.stop();
        if (audioPlayer != null) audioPlayer.stop();

        Log.d("check1", "docent_backpressed");
    }

    private void initFullscreenDialog() {
        fullscreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            @Override
            public void onBackPressed() {
                if (isPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        fullscreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        fullscreenIcon.setImageDrawable(ContextCompat.getDrawable(DocentActivity.this, R.drawable.ic_fullscreen_skrink));
        isPlayerFullscreen = true;
        fullscreenDialog.show();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    private void closeFullscreenDialog() {
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(playerView);
        isPlayerFullscreen = false;
        fullscreenDialog.dismiss();
        fullscreenIcon.setImageDrawable(ContextCompat.getDrawable(DocentActivity.this, R.drawable.ic_fullscreen_expand));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void initFullscreenButton() {
        PlaybackControlView controlView = playerView.findViewById(R.id.exo_controller);
        fullscreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        fullscreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlayerFullscreen) openFullscreenDialog();
                else closeFullscreenDialog();
            }
        });
    }

    @Override
    protected void onRestart() {
        Log.d("check1", "onRestart");
        Log.d("check1", "sendEmptyMessage");

        super.onRestart();
        if (Application.getInstance().getToggleState()) {
            if (handler1 != null) {
                handler1.sendEmptyMessageDelayed(0, 2500);
            }
            mMinewBeaconManager.startScan();
            Application.getInstance().setScanning(true);

        }
    }

    @Override
    protected void onStop() {
        Log.d("check1", "onStop");

        super.onStop();
        if (videoPlayer != null && audioPlayer == null) { //비디오만 있을 경우
            videoPlayer.setPlayWhenReady(false);
        } else if (videoPlayer == null && audioPlayer != null) { //오디오만 있을 경우
            audioPlayer.pause();
            playAudioBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_48dp);
        } else if (videoPlayer != null && audioPlayer != null) { //둘 다 있을 경우
            videoPlayer.setPlayWhenReady(false);
            audioPlayer.pause();
            playAudioBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_48dp);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        prev_beacon = "";
        Log.d("check1", "onPause");
        Log.d("check1", "onPause_getScanning : " + Application.getInstance().getScanning());
        Log.d("check1", "onPause_getToogleState" + Application.getInstance().getToggleState());

        if (Application.getInstance().getToggleState()) {
            mMinewBeaconManager.stopScan();
            Application.getInstance().setScanning(false);
        }
        if (handler1 != null) {
            handler1.removeMessages(0);
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("check1", "onDestroy");

        unregisterReceiver(receiver);
        beaconArrayList.clear();
        super.onDestroy();
        if (handler1 != null) {
            handler1.removeMessages(0);
        }
        if (audioPlayer != null) audioPlayer.release();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private String getDocentDetailInfo(String cmd, String docent_id) {
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




//    @Override
//    public void onPrepared(final MediaPlayer audioPlayer) {
//
//        audioPlayer.setLooping(true);
//        audioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { //총길이 세팅
//            @Override
//            public void onPrepared(MediaPlayer music) {
//                String minute = String.format("%2d", ((music.getDuration()) / 1000 / 60) % 60);
//                String second = String.format("%2d", ((music.getDuration()) / 1000) % 60);
//                audioTotalTime.setText(minute + ":" + second); //총 재생시간
//                audioCurrentTime.setText("0:00"); //현재 재생시간
//            }
//        });
//
//        seekbar.setMax(audioPlayer.getDuration()); //seekbar의 총길이를 audioPlayer의 총길이로 설정
//        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                //사용자가 seekbar를 움직여서 값이 변했다면 true, 재생위치를 바꿈 (seekTo)
//                if (fromUser) {
//                    audioPlayer.seekTo(progress);
//                    String currentTime = String.format("%d:%02d", (audioPlayer.getCurrentPosition() / 1000 / 60) % 60, (audioPlayer.getCurrentPosition() / 1000) % 60);
//                    audioCurrentTime.setText(currentTime);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });
//    }
}
