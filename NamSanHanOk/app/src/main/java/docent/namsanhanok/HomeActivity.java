package docent.namsanhanok;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;

public class HomeActivity extends AppCompatActivity {

    SimpleExoPlayer videoPlayer;
    PlayerView playerView;
    ImageButton homeBtn;
    TextView docentTitle;
    ImageView docentImage;
    TextView docentExplanation;
    ImageButton audioBtn;
    ImageButton locationBtn;
    MediaPlayer audioPlayer;
    LinearLayout bottom_audio_layout;
    ImageButton playBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        setVideoPlayer();
        setAudioPlayer();
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
        String videoUrl = "http://192.168.0.6:8070/hot.mp4";
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

        MediaSource videoSource = new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(Util.getUserAgent(getApplicationContext(), "DOCENT"), defaultBandwidthMeter)
        ).createMediaSource(Uri.parse(videoUrl));

        videoPlayer.prepare(videoSource);
    }

    public void setAudioPlayer() {
        audioPlayer = MediaPlayer.create(this, R.raw.konan); //임시로
        //audioPlayer = MediaPlayer.create(this, Uri.parse("http://192.168.0.6:8070/kkk.mp3")); //서버에서 가져올 경우

        audioPlayer.setLooping(true); //무한 반복
    }

    public void onClick(View v) {
        if (audioPlayer.isPlaying()) {
            audioPlayer.pause();
            playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_48dp);
        } else {
            audioPlayer.start();
            playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        }
    }

    public void init() {
        playerView = (PlayerView) findViewById(R.id.playerView);
        homeBtn = (ImageButton)findViewById(R.id.homeBtn);
        docentTitle = (TextView)findViewById(R.id.docentTitle);
        docentImage = (ImageView)findViewById(R.id.docentImage);
        docentExplanation = (TextView)findViewById(R.id.docentExplanation);
        audioBtn = (ImageButton)findViewById(R.id.audioBtn);
        locationBtn = (ImageButton)findViewById(R.id.locationBtn);
        bottom_audio_layout = (LinearLayout) findViewById(R.id.bottom_audio_layout);
        playBtn = (ImageButton) findViewById(R.id.playBtn);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        audioBtn.setOnClickListener(new View.OnClickListener() { //오디오버튼 클릭했을 때
            @Override
            public void onClick(View view) {
                if (bottom_audio_layout.getVisibility() == View.GONE) {
                    bottom_audio_layout.setVisibility(View.VISIBLE);
                } else {
                    bottom_audio_layout.setVisibility(View.GONE);
                }
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() { //위치버튼 클릭했을 때
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "location", Toast.LENGTH_SHORT).show();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() { //home버튼 클릭했을 때
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "home", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoPlayer.stop();
        audioPlayer.stop();
    }
}
