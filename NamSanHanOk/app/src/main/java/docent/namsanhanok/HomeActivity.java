package docent.namsanhanok;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class HomeActivity extends AppCompatActivity {

    PlayerView playerView;
    ImageButton homeBtn;
    TextView docentTitle;
    ImageView docentImage;
    TextView docentExplanation;
    ImageButton audioBtn;
    ImageButton locationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        setPlayer();
    }

    public void setPlayer() {
        //Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        //Create the player
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        //Attaching the player to a view
        playerView.setPlayer(player);

        //Preparing the player
        String videoUrl = "http://192.168.0.6:8070/hot.mp4";
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

        MediaSource videoSource = new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(Util.getUserAgent(getApplicationContext(), "DOCENT"), defaultBandwidthMeter)
        ).createMediaSource(Uri.parse(videoUrl));

        player.prepare(videoSource);
    }

    public void init() {
        playerView = (PlayerView) findViewById(R.id.playerView);
        homeBtn = (ImageButton)findViewById(R.id.homeBtn);
        docentTitle = (TextView)findViewById(R.id.docentTitle);
        docentImage = (ImageView)findViewById(R.id.docentImage);
        docentExplanation = (TextView)findViewById(R.id.docentExplanation);
        audioBtn = (ImageButton)findViewById(R.id.audioBtn);
        locationBtn = (ImageButton)findViewById(R.id.locationBtn);

        audioBtn.setOnClickListener(new View.OnClickListener() { //오디오버튼 클릭했을 때
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "audio", Toast.LENGTH_SHORT).show();
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() { //위치버튼 클릭했을 때
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "location", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
