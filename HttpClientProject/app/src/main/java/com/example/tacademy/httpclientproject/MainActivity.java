package com.example.tacademy.httpclientproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button :
                    doAction1();
                    break;
            }
        }
    };
    void  doAction1(){
        new JobTask().execute("http://www.w3schools.com/xml/simple.xml");
    }
    class JobTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            HttpClient client = null;
            HttpGet request = null;
            HttpResponse response;
            int code;
            String result = "";
            try{
                client = NetManager.getHttpClient();
                request = NetManager.getGet(url);
                Log.v(TAG, "ok1");
                response = client.execute(request);
                Log.v(TAG, "ok2");
                code = response.getStatusLine().getStatusCode();
                Log.v(TAG, "ok3 : " + code);
                switch(code){
                    case 200 :
                        //
                        InputStream is = response.getEntity().getContent();
//                        int data = 0;
//                        int length = (int)response.getEntity().getContentLength();
//                        byte[] brr = new byte[length];
//                        for(int i = 0; i < brr.length;i++){
//                            brr[i] = (byte) is.read();
//                        }
//                        Bitmap bitmp = BitmapFactory.decodeByteArray(brr,0,brr.length);
//
                        String data = "";
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        StringBuilder stringBuilder = new StringBuilder();

                        while((data = reader.readLine()) != null){
                            stringBuilder.append(data).append("\n");
                        }
                        result = stringBuilder.toString();
//                       result =  EntityUtils.toString(response.getEntity());
                        Log.v(TAG, "ok3 : " + result);
                        break;
                    default :
                        result = "code error : " + code;
                        break;
                }

            }catch(IOException e){
                Log.v(TAG, "e : " + e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v(TAG, s);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(handler);
    }
}
