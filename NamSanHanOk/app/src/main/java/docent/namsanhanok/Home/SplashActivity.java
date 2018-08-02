package docent.namsanhanok.Home;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import docent.namsanhanok.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }
}
