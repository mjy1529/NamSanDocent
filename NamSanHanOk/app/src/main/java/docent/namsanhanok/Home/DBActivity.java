package docent.namsanhanok.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.File;

import docent.namsanhanok.R;

public class DBActivity extends AppCompatActivity {

    DB_Async dbAsync;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        mContext = this;

        init();

        dbAsync = new DB_Async();
        dbAsync.execute();
    }

    public void init() {
        TextView curPercent = (TextView) findViewById(R.id.curPercent);
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

    @Override
    protected void onStop() {
        super.onStop();
        // 다운로드를 받고 있는 중에 종료시 처리
    }
}
