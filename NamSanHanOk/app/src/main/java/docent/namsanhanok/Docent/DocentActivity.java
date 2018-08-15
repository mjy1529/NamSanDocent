package docent.namsanhanok.Docent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codesgood.views.JustifiedTextView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;
import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import docent.namsanhanok.Application;
import docent.namsanhanok.Category.CategoryData;
import docent.namsanhanok.Category.CategoryListActivity;
import docent.namsanhanok.Category.CategoryResult;
import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.Home.UserRssi;
import docent.namsanhanok.Location.LocationActivity;
import docent.namsanhanok.Manager.DocentMemList;
import docent.namsanhanok.NetworkService;
import docent.namsanhanok.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HEAD;

import static com.minew.beacon.BluetoothState.BluetoothStatePowerOn;

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

    //videoPlayer
    SimpleExoPlayer videoPlayer;
    SimpleExoPlayerView playerView;
    //PlayerView playerView;
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
    Application applicationclass;

    String closeBeacon;

    //지울 것
    TextView go_new_docent_content;

    List<MinewBeacon> appearBeaconList = new ArrayList<>(); //인식된 비콘 리스트
    private Handler handler1;
    private Handler handler2;
    String prev_beacon = "";
    ArrayList<String> existBeacon = new ArrayList<>();

    //서버 네트워크
    NetworkService service;
    int category_id;
    private ArrayList<DocentDetailData> docentDetailDataList;
    private TextView docentExplanation;
    int position;
    int docent_id;
    String audio_url;
    String video_url;

    static boolean newDocent;
    int beaconNum;

    DocentData docentObject;

    DocentMemList docentMemList;

    public DocentActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docent);

        init();

        if (newDocent == true) {
            onResume();
//            networking3();
        } else {
            Intent docentObjectIntent = getIntent();
            docentObject = (DocentData) docentObjectIntent.getSerializableExtra("docentObject");
        }

//        networking();
        if(newDocent == false){
            //networking2();
        }
        else if(newDocent == true){
            //networking3();
        }

        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
        applicationclass = (Application) getApplicationContext();
        service = Application.getInstance().getNetworkService();

        existBeacon.add("15290");
        existBeacon.add("15282");
        beaconNum = 15282;

        showBeaconAlarm();


        initBeaconManager();
        initBeaconListenerManager();

        setDocentObject(docentObject);
        networking4();
        setRecyclerView();

        docentImage.setFocusableInTouchMode(true);
        docentImage.requestFocus();
        newDocent = false;
    }

    public void setDocentObject(DocentData docentObject) {
        String category_title = "";
        switch (docentObject.category_id) {
            case "1" : category_title = "한옥"; break;
            case "2" : category_title = "정원"; break;
            case "3" : category_title = "타임캡슐광장"; break;
        }
        docentTitle.setText(category_title);

        Glide.with(getApplicationContext())
                .load(Environment.getExternalStorageDirectory() + docentObject.docent_image_url)
                .into(docentImage);

        docentName.setText(docentObject.docent_title);
        docentExplanation.setText(docentObject.docent_content_info);

        audio_url = docentObject.docent_audio_url;
        video_url = docentObject.docent_vod_url;

        if (audio_url.equals("")) {
            audioBtn.setBackgroundResource(R.drawable.no_headphones);
        } else {
            setAudioPlayer();
        }

        if (video_url.equals("")) {
            docentVideo_Layout.setVisibility(View.GONE);
        } else {
            setVideoPlayer();
        }

        docent_id = Integer.parseInt(docentObject.docent_id);
    }

    //toolbar title
//    public void networking() {
//        Call<CategoryResult> categoryResultCall = service.getCategoryResult(getCategoryInfo("category_list"));
//        categoryResultCall.enqueue(new Callback<CategoryResult>() {
//            @Override
//            public void onResponse(Call<CategoryResult> call, Response<CategoryResult> response) {
//                if (response.isSuccessful()) {
//                    Log.d("check1", "Category network ok");
//                    categoryDataList = response.body().category_info;
//
//                    docentTitle.setText(categoryDataList.get(category_id - 1).category_title);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CategoryResult> call, Throwable t) {
//                Log.d("check1", "실패 : " + t.getMessage());
//            }
//        });
//    }

    //docent content
//    public void networking2() {
//        final Call<DocentResult> request = service.getDocentResult(getDocentInfo("docent_list", category_id));
//        request.enqueue(new Callback<DocentResult>() {
//            @Override
//            public void onResponse(Call<DocentResult> call, Response<DocentResult> response) {
//                if (response.body() != null) {
//
//                    //category는 순서대로 나타나 있으니, list를 다시 불러서 position으로 docent를 보냄.
//                    docentDataList = response.body().docent_info;
//
//                    Log.d("check1", "docent image : " + Environment.getExternalStorageDirectory() + docentDataList.get(position).docent_image_url);
//
//
//                    Glide.with(getApplicationContext())
//                            .load(Environment.getExternalStorageDirectory() + docentDataList.get(position).docent_image_url)
//                            .into(docentImage);
//
//                    docentName.setText(docentDataList.get(position).docent_title);
//
//                    String content = docentDataList.get(position).docent_content_info;
//                    docentExplanation.setText(content);
//
//                    audio_url = docentDataList.get(position).docent_audio_url;
//                    video_url = docentDataList.get(position).docent_vod_url;
//
//                    if (audio_url.equals("")) {
//                        audioBtn.setBackgroundResource(R.drawable.no_headphones);
//                    } else {
//                        setAudioPlayer();
//                    }
//
//                    if (video_url.equals("")) {
//                        docentVideo_Layout.setVisibility(View.GONE);
//                    } else {
//                        setVideoPlayer();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DocentResult> call, Throwable t) {
//                Log.d("check", "fail");
//            }
//        });
//    }

    //docent content
    public void networking3() {
        final Call<DocentBeaconResult> request = service.getDocentByBeaconResult(getBeaconDocentInfo("docent_list", beaconNum));
        request.enqueue(new Callback<DocentBeaconResult>() {
            @Override
            public void onResponse(Call<DocentBeaconResult> call, Response<DocentBeaconResult> response) {
                if(response.body() != null) {

                    //category는 순서대로 나타나 있으니, list를 다시 불러서 position으로 docent를 보냄.
                    docentObject = response.body().docent_info;

//                    Log.d("check1", "docent image : " + Environment.getExternalStorageDirectory() + docentObject.docent_image_url);
//
//
//                    Glide.with(getApplicationContext())
//                            .load(Environment.getExternalStorageDirectory() + docentObject.docent_image_url)
//                            .into(docentImage);
//
//                    docentName.setText(docentObject.docent_title);
//
//                    String content = docentObject.docent_content_info;
//                    docentExplanation.setText(content);
//
//                    audio_url = docentObject.docent_audio_url;
//                    video_url = docentObject.docent_vod_url;
//
//                    Log.d("check1", "audio url " + audio_url);
//                    Log.d("check1", "video url : " + video_url);
//
//                    setAudioPlayer();
//                    setVideoPlayer();

                }
            }

            @Override
            public void onFailure(Call<DocentBeaconResult> call, Throwable t) {
                Log.d("check", "fail");
            }
        });
    }

    //docent detail list
    public void networking4() {
        Call<DocentDetailResult> docentDetailListResult = service.getDocentDetailResult(getDocentDetailInfo("docent_detail_list", docent_id));
        docentDetailListResult.enqueue(new Callback<DocentDetailResult>() {
            @Override
            public void onResponse(Call<DocentDetailResult> call, Response<DocentDetailResult> response) {
                if (response.isSuccessful()) {
                    docentDetailDataList = response.body().docent_detail_info;

                    if (docentDetailDataList.size() == 0) {
                        docentDetails_Layout.setVisibility(View.GONE);
                    } else {
                        docentAdpater.setAdapter(docentDetailDataList);
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
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
    }

    public void initBeaconListenerManager() {
        Log.d("check1", "isScanning : " + applicationclass.getToggleState());
//
//        if (isOnBluetooth()) {
//            if (applicationclass.getScanning()) { // scan중
//                Log.d("check1", "isScanning : startScan");
//
//                mMinewBeaconManager.startScan();
//                handler1.sendEmptyMessageDelayed(0, 2200);
//            } else if (!applicationclass.getScanning()) { //bluetooth는 on인데 Scanning이 안되고 있다
//                applicationclass.setScanning(false);
//                try {
//                    mMinewBeaconManager.stopScan();
//                    handler1.removeMessages(0);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        } else if (!isOnBluetooth()) { // bluetooth == false
//            applicationclass.setScanning(false);
//            if (mMinewBeaconManager != null) {
//                mMinewBeaconManager.stopScan();
//                handler1.removeMessages(0);
//            }

        if (isOnBluetooth()) {
            if (applicationclass.getToggleState()) { // scan중
                Log.d("check1", "isScanning : startScan");

                mMinewBeaconManager.startScan();
                handler1.sendEmptyMessageDelayed(0, 2200);
            } else if (!applicationclass.getToggleState()) { //bluetooth는 on인데 Scanning이 안되고 있다
                applicationclass.setScanning(false);
                try {
                    mMinewBeaconManager.stopScan();
                    handler1.removeMessages(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (!isOnBluetooth()) { // bluetooth == false
            applicationclass.setScanning(false);
            if (mMinewBeaconManager != null) {
                mMinewBeaconManager.stopScan();
                handler1.removeMessages(0);
            }
        }

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
                if (!isOnBluetooth()) { // bluetooth==flase
//                    isScanning = false;
                    applicationclass.setScanning(false);
                    if (mMinewBeaconManager != null) {
                        mMinewBeaconManager.stopScan();
                        handler1.removeMessages(0);
                    }
                } else if (isOnBluetooth()) {
                    if (applicationclass.getScanning()) { // scan중
                    } else if (!applicationclass.getScanning()) { //bluetooth는 on인데 Scanning이 안되고 있다
                        applicationclass.isScanning = true;
                        handler1.sendEmptyMessage(0);
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

    private void showBeaconAlarm() {
        handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (!appearBeaconList.isEmpty()) {
                    Collections.sort(appearBeaconList, comp);

                    int beacon_rssi = appearBeaconList.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getIntValue();
                    String beacon_minor = appearBeaconList.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();

                    Log.d("check1", "핸들러 작동중...");


                    if (-70 < beacon_rssi && beacon_rssi < -30) {
                        if (!beacon_minor.equals(prev_beacon)) {
                            if (go_new_docent_layout.getVisibility() == View.VISIBLE) {
                                Log.d("check1", "layout Visible");
                                go_new_docent_layout.setVisibility(View.GONE);
                                handler2.removeMessages(0);
                            }

                            closeBeacon = beacon_minor;
                            showNewItemDialog();
                            prev_beacon = beacon_minor;
                        }
                    } else {
                        for (int i = 0; i < appearBeaconList.size(); i++) {
                            appearBeaconList.remove(appearBeaconList.get(i));
                        }
                    }
                }
                this.sendEmptyMessageDelayed(0, 2200);

            }

        };
    }


    private void addAppearBeacon(List<MinewBeacon> minewBeacons) {
        Log.d("check1", "isScanning : " + applicationclass.isScanning);

        Log.d("check1", "minewBeacons : " + minewBeacons.toString());
        if (!minewBeacons.isEmpty()) {
            Collections.sort(minewBeacons, comp);

            for (int i = 0; i < minewBeacons.size(); i++) {
                String beacon_minor = minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                int beacon_rssi = minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getIntValue();

                //exitBeacon의 내용을 beacon_number에 담아서 비교
                for (String beacon_number : existBeacon) {
                    if (beacon_minor.equals(beacon_number)) {
                        if (!appearBeaconList.contains(minewBeacons.get(i))) { // 중복 제거
                            appearBeaconList.add(minewBeacons.get(i));
                        }
                        Log.d("list", beacon_minor + ", " + beacon_rssi);
                    }

                }
            }
        }
//        if (!minewBeacons.isEmpty()) {
//
//            Collections.sort(minewBeacons, comp);
//
//            for(int i=0; i<minewBeacons.size(); i++) {
//                String beacon_minor = minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
//
//                if(docentMemList.check_beacon_number(beacon_minor)) {
//                    appearBeaconList.add(minewBeacons.get(i));
//                    Log.d("beaconList", minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue());
//                }
//            }
//        }
    }


    public void showNewItemDialog() {
        Log.d("check1", "showNewItemDialog 시작");
//                go_new_docent_layout.setVisibility(View.VISIBLE);

        handler2 = new Handler();
        handler2.sendEmptyMessage(0);

        vibrator.vibrate(500);

        go_new_docent_layout.setVisibility(View.VISIBLE);
        go_new_docent_content.setText(closeBeacon);


        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                //지연시키길 원하는 밀리초 뒤에 동작
                go_new_docent_layout.setVisibility(View.GONE);
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

        //Create the player
        videoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        //Attaching the player to a view
        playerView.setPlayer(videoPlayer);

        //Preparing the player
        //서버에서 가져올 때
//        String videoUrl = "http://175.123.138.125:8070/hot.mp4";
        String videoUrl = video_url;
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        MediaSource videoSource = new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(Util.getUserAgent(getApplicationContext(), "DOCENT"), defaultBandwidthMeter)
        ).createMediaSource(Uri.parse(videoUrl));
        videoPlayer.prepare(videoSource);

        //raw 폴더에서 가져올 때
//        final RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(this);
//        DataSpec dataSpec = new DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.hot));
//        try {
//            rawResourceDataSource.open(dataSpec);
//            DataSource.Factory factory = new DataSource.Factory() {
//                @Override
//                public DataSource createDataSource() {
//                    return rawResourceDataSource;
//                }
//            };
//
//            MediaSource videoSource = new ExtractorMediaSource.Factory(factory).createMediaSource(rawResourceDataSource.getUri());
//            videoPlayer.prepare(videoSource);
//        } catch (RawResourceDataSource.RawResourceDataSourceException e) {
//            e.printStackTrace();
//        }

        exo_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoPlayer.getCurrentPosition() == 0 && !videoPlayer.getPlayWhenReady()) { //썸네일
                    exo_thumbnail.setVisibility(View.GONE);
                }
                videoPlayer.setPlayWhenReady(true);
                if (audioPlayer != null && audioPlayer.isPlaying()) { //비디오 재생시 오디오 일시정지
                    audioPlayer.pause();
                    playAudioBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_48dp);
                }
            }

        });

        initFullscreenDialog();
        initFullscreenButton();
    }

    public void setAudioPlayer() {
        //raw 폴더에서 가져올 때
        //audioPlayer = MediaPlayer.create(this, R.raw.konan);

        //서버에서 가져올 경우
//        audioPlayer = MediaPlayer.create(this, Uri.parse("http://175.123.138.125:8070/kkk.mp3"));
        audioPlayer = MediaPlayer.create(this, Uri.parse(audio_url));

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
                    if (bottom_audio_layout.getVisibility() == View.GONE) {
                        bottom_audio_layout.setVisibility(View.VISIBLE);
                    } else {
                        bottom_audio_layout.setVisibility(View.GONE);
                    }
//                    if (bottom_audio_layout.getVisibility() == View.GONE && audio_url != null) {
//                        bottom_audio_layout.setVisibility(View.VISIBLE);
//                    } else if (bottom_audio_layout.getVisibility() == View.GONE && audio_url.equals("")) {
//                        Toast.makeText(getApplicationContext(), "오디오가 지원되지 않는 전시품입니다.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        bottom_audio_layout.setVisibility(View.GONE);
                }
                break;

            case R.id.locationBtn:
            case R.id.locationTxt:
                intent = new Intent(getApplicationContext(), LocationActivity.class);
                intent.putExtra("title", docentName.getText().toString());
                intent.putExtra("position", position);
                intent.putExtra("category_id", docentObject.category_id);

                startActivity(intent);
                break;

            case R.id.homeBtn:
                intent = new Intent(DocentActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;

            case R.id.confirm_go_new_docent:
                //새로운 내용으로 내용 업데이트
                //Preference
                //매개변수하나 이용해서 값이 0이면 category에서 갖고오고 1이면 docent갱신

                intent = new Intent(DocentActivity.this, DocentActivity.class);
                //확인버튼을 눌렀으니 사라짐
                go_new_docent_layout.setVisibility(View.GONE);
                bottom_audio_layout.setVisibility(View.GONE);

                newDocent = true;

                finish();
                startActivity(intent);

                Log.d("check1", "확인 누름");
                Log.d("check1", "확인버튼_newDocent : " + newDocent);


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


        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        docentName = (TextView) findViewById(R.id.docentName);
        docentImage = (ImageView) findViewById(R.id.docentImage);
        audioBtn = (ImageButton) findViewById(R.id.audioBtn);
        locationBtn = (ImageButton) findViewById(R.id.locationBtn);
//        playerView = (PlayerView) findViewById(R.id.playerView);
        playerView = (SimpleExoPlayerView) findViewById(R.id.playerView);
        bottom_audio_layout = (LinearLayout) findViewById(R.id.bottom_audio_layout);
        playAudioBtn = (ImageButton) findViewById(R.id.playAudioBtn);
        exo_play = (ImageButton) findViewById(R.id.exo_play);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        audioTotalTime = (TextView) findViewById(R.id.audioTotalTime);
        audioCurrentTime = (TextView) findViewById(R.id.audioCurrentTime);
        docentTitle = (TextView) findViewById(R.id.docentTitle);
//        docentTitle.setText(docent_title);

        exo_thumbnail = (ImageView) findViewById(R.id.exo_thumbnail);

        audioTxt = (TextView) findViewById(R.id.audioTxt);
        locaTxt = (TextView) findViewById(R.id.locationTxt);

        go_new_docent_layout = (LinearLayout) findViewById(R.id.go_new_docent);
        confirm_go_new_docent = (TextView) findViewById(R.id.confirm_go_new_docent);

        SpannableString content = new SpannableString("확인하기");
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        confirm_go_new_docent.setText(content);

        docentDetails_Layout = (LinearLayout) findViewById(R.id.docentDetails_Layout);
        docentVideo_Layout = (LinearLayout) findViewById(R.id.docentVideo_Layout);
        docentExplanation = (TextView) findViewById(R.id.docentExplanation);

        //지울 것
        go_new_docent_content = (TextView) findViewById(R.id.go_new_docent_content);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (videoPlayer != null) videoPlayer.stop();
        if (audioPlayer != null) audioPlayer.stop();

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
    protected void onResume() {
        super.onResume();
        Log.d("check1", "newDocent : " + newDocent);

        if(newDocent == true){

            Log.d("check1", "onResume, cate_id : " + category_id);
            Log.d("check1", "onResume, docent_id : " + docent_id);
            //여기서 비콘 넘버 받아서 그 docent의 id와 category_id를 받아오면 됨.
            docentObject.category_id = "2";
            docentObject.docent_id = "3";


            Log.d("check1", "onResume, cate_id : " + category_id);
            Log.d("check1", "onResume, docent_id : " + docent_id);
        }
    }

    @Override
    protected void onStop() {
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
        Log.d("check1", "onPause");
        if (applicationclass.getScanning()) {
            mMinewBeaconManager.stopScan();
            handler1.removeMessages(0);
        }
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

    public String getBeaconDocentInfo(String cmd, int beacon_number) {
        String jsonStr = "";
        try {
            JSONObject data = new JSONObject();
            data.put("cmd", cmd);
            data.put("beacon_number", beacon_number);

            JSONObject root = new JSONObject();
            root.put("info", data);
            jsonStr = root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("check1", "docent content 정보요청 : " + jsonStr);

        return jsonStr;
    }

    private String getCategoryInfo(String cmd) {
        String json = "";
        try {
            JSONObject data = new JSONObject();
            data.put("cmd", cmd);


            JSONObject root = new JSONObject();
            root.put("info", data);
            json = root.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("check1", "Category title info 정보요청 : " + json);

        return json;
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
