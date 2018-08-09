package docent.namsanhanok.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.File;

import docent.namsanhanok.R;

public class DBActivity extends AppCompatActivity {

    public static Context mContext;

    ProgressBar downloadBar;
    TextView curPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        mContext = this;

        init();
        new DB_Async().execute();
    }

    public void init() {
        downloadBar = (ProgressBar) findViewById(R.id.downloadBar);
        curPercent = (TextView) findViewById(R.id.curPercent);
        curPercent.bringToFront();
    }

    public void activityFinish() {
        Intent intent = new Intent(DBActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
