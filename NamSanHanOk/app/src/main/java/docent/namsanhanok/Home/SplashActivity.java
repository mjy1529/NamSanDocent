package docent.namsanhanok.Home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import docent.namsanhanok.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView splash_title = (TextView) findViewById(R.id.splash_title);
        TextView splash_title2 = (TextView) findViewById(R.id.splash_title2);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "NanumMyeongjo.otf");
        splash_title.setTypeface(typeface);
        splash_title2.setTypeface(typeface);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

}
