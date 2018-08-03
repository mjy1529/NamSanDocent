package docent.namsanhanok.Docent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import docent.namsanhanok.Application;
import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.Home.UserRssi;
import docent.namsanhanok.Location.LocationActivity;
import docent.namsanhanok.R;

import static com.minew.beacon.BluetoothState.BluetoothStatePowerOn;

public class DocentActivity extends AppCompatActivity {

    ImageButton homeBtn;
    TextView docentName;
    ImageView docentImage;
    TextView docentExplanation;
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
    ImageView exo_artwork;

    Dialog fullscreenDialog;
    boolean isPlayerFullscreen = false;
    ImageView fullscreenIcon;
    FrameLayout fullscreenButton;

    //audioPlayer
    MediaPlayer audioPlayer;
    ImageButton playAudioBtn;
    SeekBar seekbar;
    TextView audioTotalTime;
    TextView audioCurrentTime;

    //상세보기
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DocentAdapter docentAdapter;
    private ArrayList<DocentActivityItem> docentActivityItem;

    String docent_title;

    LinearLayout go_new_docent_layout;
    TextView confirm_go_new_docent;
    private Vibrator vibrator;
    private MinewBeaconManager mMinewBeaconManager;
    UserRssi comp = new UserRssi();
    private int state;
    Application applicationclass;

    String closeBeacon;

    //지울 것
    TextView go_new_docent_content;

//    ArrayList<String> beaconNumbers = new ArrayList<>(); //임의의 저장된 비콘넘버

    List<MinewBeacon> appearBeaconList = new ArrayList<>(); //인식된 비콘 리스트
    private Handler handler1;
    private Handler handler2;
    String prev_beacon = "";
    ArrayList<String> existBeacon = new ArrayList<>();
    long time;


    public DocentActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docent);

        Intent secondIntent = getIntent();
        //docent_title = secondIntent.getExtras().getString("docent_title");
        docent_title = "한옥";
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
        applicationclass = (Application) getApplicationContext();

        existBeacon.add("15290");
        existBeacon.add("15282");

        showBeaconAlarm();
        initBeaconManager();
        initBeaconListenerManager();

        init();
        setRecyclerView();
        setAudioPlayer();
        setVideoPlayer();

        docentImage.setFocusableInTouchMode(true);
        docentImage.requestFocus();
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
        if (isOnBluetooth()) {
            if (applicationclass.getScanning()) { // scan중
                mMinewBeaconManager.startScan();
                handler1.sendEmptyMessageDelayed(0, 2200);
            } else if (!applicationclass.getScanning()) { //bluetooth는 on인데 Scanning이 안되고 있다
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
        if (!minewBeacons.isEmpty()) {
            Collections.sort(minewBeacons, comp);

            for (int i = 0; i < minewBeacons.size(); i++) {
                String beacon_minor = minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                int beacon_rssi = minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getIntValue();

                for (String beacon_number : existBeacon) {
                    if (beacon_minor.equals(beacon_number)) {
                        if (!appearBeaconList.contains(minewBeacons.get(i))) { // 중복 제거
                            appearBeaconList.add(minewBeacons.get(i));
                        }
                        Log.d("check1", "list : " + beacon_minor + ", " + beacon_rssi);
                    }

                }
            }
        }
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

    private void initDataset() {
        docentActivityItem = new ArrayList<>();
        docentActivityItem.add(new DocentActivityItem("베", R.drawable.bae));
        docentActivityItem.add(new DocentActivityItem("짚신", R.drawable.jipshin));
        docentActivityItem.add(new DocentActivityItem("베", R.drawable.bae));
        docentActivityItem.add(new DocentActivityItem("짚신", R.drawable.jipshin));
        docentActivityItem.add(new DocentActivityItem("베", R.drawable.bae));
        docentActivityItem.add(new DocentActivityItem("짚신", R.drawable.jipshin));
        docentActivityItem.add(new DocentActivityItem("베", R.drawable.bae));
        docentActivityItem.add(new DocentActivityItem("짚신", R.drawable.jipshin));
    }

    public void setRecyclerView() {
        initDataset();
        recyclerView = (RecyclerView) findViewById(R.id.docent_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        docentAdapter = new DocentAdapter(this, docentActivityItem);

        recyclerView.setAdapter(docentAdapter);
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
        String videoUrl = "http://175.123.138.125:8070/hot.mp4";
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

        exo_play.setOnClickListener(new View.OnClickListener() { //영상의 재생버튼 클릭했을 때 오디오 일시정지
            @Override
            public void onClick(View view) {
                videoPlayer.setPlayWhenReady(true);
                if (audioPlayer.isPlaying()) {
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
        audioPlayer = MediaPlayer.create(this, Uri.parse("http://175.123.138.125:8070/kkk.mp3"));

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
        switch (v.getId()) {
            case R.id.playAudioBtn: //오디오 play버튼을 클릭했을 때 재생/일시정지
                if (audioPlayer.isPlaying()) {
                    audioPlayer.pause();
                    playAudioBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_48dp);

                } else {
                    audioPlayer.start();
                    playAudioBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);

                    if (videoPlayer.getPlayWhenReady()) { //영상이 play 상태라면
                        videoPlayer.setPlayWhenReady(false); //영상 일시정지
                    }

                    Thread();
                }
                break;

            case R.id.audioBtn: //오디오 이미지버튼을 클릭했을 때 오디오 레이아웃 보이기
            case R.id.audioTxt:
                if (bottom_audio_layout.getVisibility() == View.GONE) {
                    bottom_audio_layout.setVisibility(View.VISIBLE);
                } else {
                    bottom_audio_layout.setVisibility(View.GONE);
                }
                break;

            case R.id.locationBtn:
            case R.id.locationTxt:
                Intent intent2 = new Intent(getApplicationContext(), LocationActivity.class);
                intent2.putExtra("title", docentName.getText().toString());
                startActivity(intent2);
                Toast.makeText(DocentActivity.this, "location", Toast.LENGTH_SHORT).show();
                break;

            case R.id.homeBtn:
                Intent intent = new Intent(DocentActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;

            case R.id.confirm_go_new_docent:
                //새로운 내용으로 내용 업데이트

                //확인버튼을 눌렀으니 사라짐
                go_new_docent_layout.setVisibility(View.GONE);
                bottom_audio_layout.setVisibility(View.GONE);
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
        docentExplanation = (TextView) findViewById(R.id.docentExplanation);
        audioBtn = (ImageButton) findViewById(R.id.audioBtn);
        locationBtn = (ImageButton) findViewById(R.id.locationBtn);
//        playerView = (PlayerView) findViewById(R.id.playerView);
        playerView = (SimpleExoPlayerView) findViewById(R.id.playerView);
        bottom_audio_layout = (LinearLayout) findViewById(R.id.bottom_audio_layout);
        playAudioBtn = (ImageButton) findViewById(R.id.playAudioBtn);
        exo_play = (ImageButton) findViewById(R.id.exo_play);
        exo_artwork = (ImageView) findViewById(R.id.exo_artwork);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        audioTotalTime = (TextView) findViewById(R.id.audioTotalTime);
        audioCurrentTime = (TextView) findViewById(R.id.audioCurrentTime);
        docentTitle = (TextView) findViewById(R.id.docentTitle);
        docentTitle.setText(docent_title);

        audioTxt = (TextView) findViewById(R.id.audioTxt);
        locaTxt = (TextView) findViewById(R.id.locationTxt);

        go_new_docent_layout = (LinearLayout) findViewById(R.id.go_new_docent);
        confirm_go_new_docent = (TextView) findViewById(R.id.confirm_go_new_docent);

        SpannableString content = new SpannableString("확인하기");
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        confirm_go_new_docent.setText(content);

        //지울 것
        go_new_docent_content = (TextView) findViewById(R.id.go_new_docent_content);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoPlayer.stop();
        audioPlayer.stop();

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
    }

    private void closeFullscreenDialog() {
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(playerView);
        isPlayerFullscreen = false;
        fullscreenDialog.dismiss();
        fullscreenIcon.setImageDrawable(ContextCompat.getDrawable(DocentActivity.this, R.drawable.ic_fullscreen_expand));
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
    protected void onStop() {
        super.onStop();
        if (videoPlayer.getPlayWhenReady() || audioPlayer.isPlaying()) {
            videoPlayer.setPlayWhenReady(false);
            audioPlayer.stop();
        }
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
}
