package docent.namsanhanok;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import docent.namsanhanok.Home.SplashActivity;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class ShowWiFiMonitor extends BroadcastReceiver
{
    private Activity activity;
    PrettyDialog requestDataDialog;

    public ShowWiFiMonitor() {
        super();
    }
    public ShowWiFiMonitor(Activity activity) {
        this.activity = activity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action= intent.getAction();

        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            try {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
                NetworkInfo _wifi_network =
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if(_wifi_network != null) {
                    // wifi, 3g 둘 중 하나라도 있을 경우
                    if(_wifi_network != null && activeNetInfo != null){

                    }
                    // wifi, 3g 둘 다 없을 경우
                    else{
                        Log.d("check5", "dialog 나옴");
                        activity.recreate();
                    }
                }
            } catch (Exception e) {
                Log.i("ULNetworkReceiver", e.getMessage());
            }
        }
    }

//    public void showAlertDataDialog() {
//        requestDataDialog = new PrettyDialog(activity);
//        requestDataDialog
//                .setMessage(activity.getResources().getString(R.string.wifi_disconnect))
//                .setIcon(R.drawable.pdlg_icon_info)
//                .setIconTint(R.color.dark_blue)
//                .addButton("확인",
//                        R.color.pdlg_color_white,
//                        R.color.dark_blue,
//                        new PrettyDialogCallback() {
//                            @Override
//                            public void onClick() {
//                                requestDataDialog.dismiss();
//                            }
//                        }
//                )
//                .show();
//    }


}
