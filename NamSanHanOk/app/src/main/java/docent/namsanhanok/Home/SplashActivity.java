package docent.namsanhanok.Home;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import docent.namsanhanok.R;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences prefs; //메모리에 값을 저장해두기 위해 사용하는 클래스 // 첫 실행 여부 확인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = getSharedPreferences("Pref", MODE_PRIVATE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkFirstRun();
            }
        }, 3000);
    }

    public void checkFirstRun() {
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true); // isFirstRun이 null값이면 true를 가져옴.
        Intent intent;
        if (isFirstRun) {
            intent = new Intent(getApplicationContext(), TutorialActivity.class);
            startActivity(intent);

            prefs.edit().putBoolean("isFirstRun", false).apply();
            finish();
        }
        else {
            intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
