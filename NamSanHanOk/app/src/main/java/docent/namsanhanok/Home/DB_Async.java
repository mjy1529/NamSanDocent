package docent.namsanhanok.Home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import docent.namsanhanok.R;


public class DB_Async extends AsyncTask<String, String, String> {
    private String FileName;
    String savePath = Environment.getExternalStorageDirectory() + File.separator + "namsangol/";
    File file;

    ProgressBar downloadBar;
    TextView curPercent;
    TextView downloadStatus;

    @Override
    protected void onPreExecute() {
        downloadBar = SplashActivity.downloadBar;
        curPercent = SplashActivity.curPercent;
        downloadBar.setProgress(0);
        downloadStatus = SplashActivity.downloadStatus;
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        File dir = new File(savePath);
        //상위 디렉토리가 존재하지 않을 경우 생성
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            Log.d("check", "savePath : " + savePath);

            String DownloadURL = "http://175.123.138.125:8070/down/ko/namsangol_v1.0.0.zip";
            FileName = savePath + "/namsangol_v1.0.0.zip";

            URL url = new URL(DownloadURL);
            URLConnection connection = url.openConnection();
            connection.connect();
            int sizeOfFile = connection.getContentLength();
            InputStream inputStream = new BufferedInputStream(url.openStream());

            Log.d("check", "http check ok : ");
            file = new File(FileName);
            OutputStream out = new FileOutputStream(file);
            saveRemoteFile(inputStream, out, sizeOfFile);

            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveRemoteFile(InputStream is, OutputStream os, int sizeOfFile) throws IOException {
        int count = 0;
        byte data[] = new byte[1024];
        long total = 0;

        while((count = is.read(data)) != -1) {
            total += count;
            publishProgress("" + (int)((total * 100) / sizeOfFile));
            os.write(data, 0, count);
        }
        os.flush();
    }

    @Override
    protected void onPostExecute(String obj) {
        String source = FileName;
        String destination = savePath;

        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(destination);

            if (zipFile.getProgressMonitor().getResult() == ProgressMonitor.RESULT_SUCCESS) {
                ((SplashActivity) SplashActivity.mContext).activityFinish();
                Log.d("check", "Result_Success");
                file.delete();
            }

        } catch (ZipException e) {
            e.printStackTrace();
        }
        downloadStatus.setText(R.string.downloadComplete);
        SplashActivity.prefs.edit().putString("curVersion", SplashActivity.dbVersion).apply();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        downloadBar.setProgress(Integer.parseInt(values[0]));
        curPercent.setText(values[0]+"%");
    }
}
