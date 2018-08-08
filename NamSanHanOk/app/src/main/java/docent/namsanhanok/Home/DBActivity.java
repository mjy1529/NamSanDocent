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

        mContext = this;
        dbAsync = new DB_Async();
        dbAsync.execute();

    }

    public void activityFinish() {
        Intent intent = new Intent(DBActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
