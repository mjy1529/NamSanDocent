package docent.namsanhanok.Home;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

import docent.namsanhanok.R;

public class DBActivity extends AppCompatActivity {
//    File storeDir;
//    File temp;

    DB_Async dbAsync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        //db버전체크, splash에서 해야함. 튜토리얼은 실행안될 때도 있어서


//        downloadZipfile();


        dbAsync = new DB_Async();
        dbAsync.execute();
    }



    public boolean checkVesrion(){
        return true;
    }
}
