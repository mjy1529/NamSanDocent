package docent.namsanhanok.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.tsengvn.typekit.TypekitContextWrapper;

import docent.namsanhanok.R;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        init();
    }

    public void init(){
        ImageButton closeBtn = (ImageButton) findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SplashActivity.prefs.edit().putBoolean("isFirstRun", false).apply();
                Intent intent = new Intent(TutorialActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
