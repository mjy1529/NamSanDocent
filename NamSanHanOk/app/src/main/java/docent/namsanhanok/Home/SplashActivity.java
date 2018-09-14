package docent.namsanhanok.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.IOException;
import java.net.Socket;
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
    boolean isOnServer_;

    public static Context mContext;

    PrettyDialog downloadDialog;
    PrettyDialog requestDataDialog;
    PrettyDialog ServerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.d("check1", "SplashAct_onCreate : " + Application.getInstance().getOnServer());
        init();
        isServerConnected();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkInternet()) {
                    networking();
                } else {
                    showAlertDataDialog();
                }

            }
        }, 3000);
    }

    public void isServerConnected(){
        new Thread() {
            public void run(){
                try{
                    Socket socket = new Socket("175.123.138.125", 8070);
                    isOnServer_ = socket.isConnected();
                    Application.getInstance().setOnServer(isOnServer_);
                    Log.d("check1", "SC.Sever is On? : " + Application.getInstance().getOnServer());
                } catch (IOException e){
                    e.printStackTrace();
                    Application.getInstance().setOnServer(false);
                    Log.d("check1", "IOException_SC.Sever is On? : " + Application.getInstance().getOnServer());
                }
            }
        }.start();
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
                if(Application.getInstance().getOnServer()){ //서버 연결
                    if(checkFolder()){ //폴더가 있을 때
                        try {
                            checkVersion();
                        }catch (IndexOutOfBoundsException e){ //wifi로그인 안되어있을 때
                            showAlertDataDialog();
                        }
                    }
                    else{ //폴더가 없을 때
                        if(packageList.size()==0){
                            showAlertServerDialog();
                        }else{
                            startDownloading();
                        }
                    }
                }
                else{
                    if(checkFolder()){
                        activityFinish();
                    }
                    else{
                        showAlertServerDialog();
                    }
                }
//                checkVersion();
//                try{
//                    checkVersion();
//                }catch(IndexOutOfBoundsException e){
////                    e.printStackTrace();
//                    //로그인 해야되는 WIFI시 로그인 안되어 있을 때
//                    showAlertDataDialog();
//                }
            }
        }.execute();
    }
    public boolean checkFolder() {
        if(packageList.size()!=0) {
            //폴더가 삭제되었는지 확인
            PackageData packageData = packageList.get(0);
            String savePath = Environment.getExternalStorageDirectory() + File.separator + packageData.package_title + "/";

            File dir = new File(savePath);

            if (dir.exists()) { //상위 디렉토리가 존재할 경우
                return true;
            } else { //존재하지 않을 경우
                return false;
            }
        }else{
            return false;
        }
    }

//    public boolean checkFolder() {
//            //폴더가 삭제되었는지 확인
//            PackageData packageData = packageList.get(0);
//            String savePath = Environment.getExternalStorageDirectory() + File.separator + packageData.package_title + "/";
//
//            File dir = new File(savePath);
//
//            if (dir.exists()) { //상위 디렉토리가 존재할 경우
//                return true;
//            } else { //존재하지 않을 경우
//                return false;
//            }
//
//    }

    public void checkVersion() {
        dbVersion = packageList.get(0).package_version; // ko 버전은 0
        String curVersion = prefs.getString("curVersion", "0.0.0");
        isFirstRun = prefs.getBoolean("isFirstRun", true);

        if (!curVersion.equals(dbVersion)) {
            startDownloading();
        }
        else{ //homeActivity로 가라
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
        downloadDialog = new PrettyDialog(SplashActivity.this);
        downloadDialog.setCancelable(false);
        downloadDialog
                .setMessage(getResources().getString(R.string.downloadAlertMessage))
                .setIcon(R.drawable.pdlg_icon_info)
                .setIconTint(R.color.dark_blue)
                .addButton("확인",
                        R.color.pdlg_color_white,
                        R.color.dark_blue,
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
        requestDataDialog = new PrettyDialog(SplashActivity.this);
        requestDataDialog
                .setMessage(getResources().getString(R.string.request))
                .setIcon(R.drawable.pdlg_icon_info)
                .setIconTint(R.color.dark_blue)
                .addButton("확인",
                        R.color.pdlg_color_white,
                        R.color.dark_blue,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                requestDataDialog.dismiss();
                                finish();
                            }
                        }
                )
                .show();
    }

    public void showAlertServerDialog() {
        ServerDialog = new PrettyDialog(SplashActivity.this);
        ServerDialog
                .setMessage(getResources().getString(R.string.serverConnected))
                .setIcon(R.drawable.pdlg_icon_info)
                .setIconTint(R.color.dark_blue)
                .addButton("확인",
                        R.color.pdlg_color_white,
                        R.color.dark_blue,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                ServerDialog.dismiss();
                                finish();
                            }
                        }
                )
                .show();
    }

    public void activityFinish() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean checkInternet() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobile.isConnected() || wifi.isConnected()) {
            return true;
        } else {
            return false;
        }
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
