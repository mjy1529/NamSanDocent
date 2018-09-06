package docent.namsanhanok;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


public class ServerConnected {
    Socket socket;
    BufferedWriter networkWriter;
    BufferedReader networkReader;
//    boolean isOnServer;

    public void ServerConneted() {
        ServerAsyncTask.execute(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    public class ServerAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            SocketAddress adr = new InetSocketAddress("175.123.135.125", 8070);
            try {
                socket.connect(adr, 1000);
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                socket = new Socket();
                networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Application.getInstance().setOnServer(true);
                Log.d("check1", "Sever is On? : " + Application.getInstance().getOnServer());
            } catch (IOException e) {
                Application.getInstance().setOnServer(false);
            }
        }
    }

}
