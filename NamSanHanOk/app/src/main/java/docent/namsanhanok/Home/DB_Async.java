package docent.namsanhanok.Home;

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


import docent.namsanhanok.R;

import static android.content.Context.MODE_PRIVATE;

public class DB_Async extends AsyncTask<String, String, String> {
    final String TAG = "DB_ASync";
    private String fileName_;
    private String savePath_;
    File file;


    ProgressBar downloadBar;
    TextView curPercent;
    TextView downloadStatus;

    PackageData packageData_;

    DB_Async(PackageData packageData) {
        packageData_ = packageData;
        savePath_ = Environment.getExternalStorageDirectory() + File.separator + packageData_.package_title + "/";
        fileName_ = savePath_ + packageData_.package_title + "_v" + packageData_.package_version + ".zip"; // savePath + "/namsangol_v1.0.0.zip";
    }

    @Override
    protected void onPreExecute() {
        downloadBar = SplashActivity.downloadBar;
        curPercent = SplashActivity.curPercent;
        downloadBar.setProgress(0);
        downloadStatus = SplashActivity.downloadStatus;
        super.onPreExecute();
    }

    private void setDirEmpty(String path) {
        File dir = new File(path);
        File[] childFileList = dir.listFiles();
        if (dir.exists()) {
            for (File childFile : childFileList) {
                if (childFile.isDirectory()) {
                    setDirEmpty(childFile.getAbsolutePath()); //하위 디렉토리
                } else {
                    childFile.delete(); //하위 파일
                }
            }
            dir.delete();
        }
    }

    @Override
    protected String doInBackground(String... params) {

        File dir = new File(savePath_);
        //상위 디렉토리가 존재하지 않을 경우 생성
        if (!dir.exists()) {
            dir.mkdirs();
        } else {
            setDirEmpty(savePath_);
            dir.mkdirs();
        }

        try {
            Log.d(TAG, "savePath : " + savePath_);

            //String DownloadURL = "http://175.123.138.125:8070/down/ko/namsangol_v1.0.0.zip";
//            InputStream inputStream = new URL(DownloadURL).openStream();

            URL url = new URL(packageData_.package_url);
//            URLConnection connection = url.openConnection();
//            connection.connect();
//            int sizeOfFile = connection.getContentLength();
//            InputStream inputStream = new BufferedInputStream(url.openStream());
            InputStream inputStream = null;

            URLConnection connection = url.openConnection();
            connection.connect();
            int sizeOfFile = connection.getContentLength();
            try{
                inputStream = new BufferedInputStream(url.openStream());
            }catch (FileNotFoundException e){
                Log.d("check3", "DB_FileNotFoundException");
                e.printStackTrace();
            }
            Log.d(TAG, "http check ok : ");
            file = new File(fileName_);

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

        while ((count = is.read(data)) != -1) {
            total += count;
            publishProgress("" + (int) ((total * 100) / sizeOfFile));
//            os.write(count);
            os.write(data, 0, count);
        }
        os.flush();
    }

    @Override
    protected void onPostExecute(String obj) {
        String source = fileName_;
        String destination = savePath_;

        try {
            ZipFile zipFile = new ZipFile(source);

            zipFile.extractAll(destination);
            if (zipFile.getProgressMonitor().getResult() == ProgressMonitor.RESULT_SUCCESS) {
                ((SplashActivity) SplashActivity.mContext).activityFinish();
                Log.d(TAG, "Result_Success");
                file.delete();
            }

            downloadStatus.setText(R.string.downloadComplete);
            SplashActivity.prefs.edit().putString("curVersion", SplashActivity.dbVersion).apply();

        } catch (ZipException e) {
            e.printStackTrace();
            //zip file size == 0
            file.delete();
            ((SplashActivity)SplashActivity.mContext).showAlertServerDialog();
        }


    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        downloadStatus.setText(R.string.downloading);
        downloadBar.setProgress(Integer.parseInt(values[0]));
        curPercent.setText(values[0] + "%");
    }


}
