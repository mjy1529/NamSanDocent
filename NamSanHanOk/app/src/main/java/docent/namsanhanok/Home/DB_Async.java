package docent.namsanhanok.Home;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DB_Async extends AsyncTask<String, String, String> {


    private String FileName;
    String savePath = Environment.getExternalStorageDirectory() + File.separator + "namsangol/";
    private String zipFile;  //저장된 zip 파일 위치
    private String unzipLocation; //압출을 풀 위치


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
            FileName = savePath + "/namsangol_v1.0.0.zip";
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
        String source = FileName;
        String destination = savePath + "/namsangol_v1.0.0";

        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(destination);
        } catch (ZipException e) {
            e.printStackTrace();
        }

        super.onPostExecute(obj);

    }
}
