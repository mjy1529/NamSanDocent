package docent.namsanhanok.Home;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import docent.namsanhanok.Application;
import docent.namsanhanok.NetworkService;
import docent.namsanhanok.R;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;

public class SplashActivity extends AppCompatActivity {
    static SharedPreferences prefs; //메모리에 값을 저장해두기 위해 사용하는 클래스
    Handler handler = new Handler();
    ArrayList<PackageData> packageList = new ArrayList<>();

    public static ProgressBar downloadBar;
    public static TextView curPercent;
    public static TextView downloadStatus;
    static String dbVersion;
    boolean isFirstRun;

    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(checkInternet()){
                    networking();
                }
                else{
                    showAlertDataDialog();
                }

            }
        }, 3000);
    }

    public void init() {
        mContext = this;

        prefs = getSharedPreferences("Pref", MODE_PRIVATE);

        downloadBar = (ProgressBar) findViewById(R.id.downloadBar);
        curPercent = (TextView) findViewById(R.id.curPercent);
        curPercent.bringToFront();
        downloadStatus = (TextView) findViewById(R.id.downloadStatus);
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
                checkVersion();
            }
        }.execute();
    }

    public void checkVersion() {
        dbVersion = packageList.get(0).package_version; // ko 버전은 0
        String curVersion = prefs.getString("curVersion", "0.0.0");
        isFirstRun = prefs.getBoolean("isFirstRun", true);

        if (!curVersion.equals(dbVersion)) {
            startDownloading();
        } else {
            activityFinish();
        }
    }

    public void startDownloading() {
        downloadStatus.setText(R.string.downloadWaiting);
        downloadStatus.setVisibility(View.VISIBLE);
        downloadBar.setVisibility(View.VISIBLE);
        curPercent.setVisibility(View.VISIBLE);
        showAlertDialog();
    }

    public void showAlertDialog() {
        final PrettyDialog downloadDialog = new PrettyDialog(SplashActivity.this);
        downloadDialog
                .setMessage(getResources().getString(R.string.downloadAlertMessage))
                .setIcon(R.drawable.pdlg_icon_info)
                .setIconTint(R.color.pdlg_color_blue)
                .addButton("확인",
                        R.color.pdlg_color_white,
                        R.color.pdlg_color_blue,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                downloadDialog.dismiss();
                                new DB_Async(packageList.get(0)).execute();
                            }
                        }
                )
                .addButton("취소",
                        R.color.pdlg_color_white,
                        R.color.pdlg_color_gray,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                downloadDialog.dismiss();
                                onBackPressed();
                            }
                        }
                )
                .show();
    }

    public void showAlertDataDialog() {
        final PrettyDialog requestDialog = new PrettyDialog(SplashActivity.this);
        requestDialog
                .setMessage(getResources().getString(R.string.request))
                .setIcon(R.drawable.pdlg_icon_info)
                .setIconTint(R.color.pdlg_color_blue)
                .addButton("확인",
                        R.color.pdlg_color_white,
                        R.color.pdlg_color_blue,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                if(checkInternet()){
                                    requestDialog.dismiss();
                                    networking();
                                }
                                else {
                                    requestDialog.dismiss();
                                    onBackPressed();
                                }
                            }
                        }
                )
                .show();
    }

    public void activityFinish() {
        Intent intent;
        if (isFirstRun) {
            intent = new Intent(SplashActivity.this, TutorialActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, HomeActivity.class);
        }
        startActivity(intent);
        finish();
    }


    public boolean checkInternet() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobile.isConnected() || wifi.isConnected()) { return true;}
        else { return false; }
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
