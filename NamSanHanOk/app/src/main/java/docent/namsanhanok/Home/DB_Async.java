package docent.namsanhanok.Home;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DB_Async extends AsyncTask<String, String, String> {


    private String fileName;
    String savePath = Environment.getExternalStorageDirectory() + File.separator + "namsangol/";

    @Override
    protected void onPreExecute() {

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

            String DownloadURL = "http://175.123.138.125:8070/down/namsangol_v1.0.0.zip";
            String FileName = savePath + "/namsan.zip";
            InputStream inputStream = new URL(DownloadURL).openStream();

            Log.d("check", "http check ok : ");
            File file = new File(FileName);
           OutputStream out = new FileOutputStream(file);

            Log.d("check", "http check ok2 : ");

            saveRemoteFile(inputStream, out);
            out.close();

        } catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void saveRemoteFile(InputStream is, OutputStream os) throws IOException
    {
        int c = 0;
        while((c = is.read()) != -1)
            os.write(c);
        os.flush();
    }

    @Override
    protected void onPostExecute(String obj) {

       //zip파일 풀기

        super.onPostExecute(obj);

    }



//출처: http://kwon8999.tistory.com/entry/안드로이드-AsyncTask를-이용하여-파일-다운로드 [Kwon's developer]
}
