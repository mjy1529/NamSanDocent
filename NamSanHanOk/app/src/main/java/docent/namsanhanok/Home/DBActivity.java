package docent.namsanhanok.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

import docent.namsanhanok.R;

public class DBActivity extends AppCompatActivity {

    DB_Async dbAsync;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        //db버전체크, splash에서 해야함. 튜토리얼은 실행안될 때도 있어서

        mContext = this;
        dbAsync = new DB_Async();
        dbAsync.execute();

    }

public void ActivityFinish() {
    Intent intent = new Intent(DBActivity.this , HomeActivity.class);
    startActivity(intent);
    finish();
    }

}
