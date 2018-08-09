package docent.namsanhanok.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import docent.namsanhanok.Application;
import docent.namsanhanok.NetworkService;
import docent.namsanhanok.R;
import retrofit2.Call;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences prefs; //메모리에 값을 저장해두기 위해 사용하는 클래스
    Handler handler = new Handler();
    ArrayList<PackageData> packageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = getSharedPreferences("Pref", MODE_PRIVATE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                networking();
            }
        }, 3000);
    }

    public void networking() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                NetworkService service = Application.getInstance().getNetworkService();
                Call<PackageResult> request = service.getPackageResult(jsonToString());
                try {
                    PackageResult packageResult = request.execute().body();
                    packageList = packageResult.package_info;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                checkFirstRun();
                finish();
            }
        }.execute();
    }

    public void checkFirstRun() {
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true); // isFirstRun이 null값이면 true를 가져옴.
        String curVersion = packageList.get(0).package_version; //현재 버전
        String preVersion = prefs.getString("preVersion", "0.0.0");

//        boolean isFirstRun = prefs.getBoolean("isFirstRun", true); // isFirstRun이 null값이면 true를 가져옴.
//        Intent intent;
//        if (isFirstRun) {
//            intent = new Intent(getApplicationContext(), TutorialActivity.class);
//            startActivity(intent);
//            prefs.edit().putBoolean("isFirstRun", false).apply();
//
//        }
//        else {
//            intent = new Intent(getApplicationContext(), HomeActivity.class);
//            startActivity(intent);
//        }

        Intent intent;
        if (isFirstRun) {
            intent = new Intent(SplashActivity.this, TutorialActivity.class);
            prefs.edit().putBoolean("isFirstRun", false).apply();
            prefs.edit().putString("preVersion", curVersion).apply();
        } else {
            if (curVersion.equals(preVersion)) {
                intent = new Intent(SplashActivity.this, HomeActivity.class);
            } else {
                prefs.edit().putString("preVersion", curVersion).apply();
                intent = new Intent(SplashActivity.this, DBActivity.class);
            }
        }
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public String jsonToString() {
        String jsonStr = "";
        try {
            JSONObject data = new JSONObject();
            data.put("cmd", "package_list");

            JSONObject root = new JSONObject();
            root.put("info", data);
            jsonStr = root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }
}
