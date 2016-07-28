package com.example.tacademy.netproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText etURL, etOutput;
    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.button :
                    doAction1();
                    break;
            }
        }
    };
    ProgressDialog progressDialog = null;
    void doAction1(){
        progressDialog = ProgressDialog.show(this, "","작업중");
        new JobThread().start();
    }
    Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            switch (msg.what){
                case 999 :
                    etOutput.setText(msg.obj.toString());
                    break;
                case 888 :
                    etOutput.setText("응답코드 에러 : " + msg.arg1);
                    break;
                case 777 :
                    Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    class JobThread extends Thread{
        public void run(){
//            String webURL = etURL.getText().toString();

            String name = "korea 한국";
            String age = "30살";
            String webURL = "http://192.168.204.139/sam/sam.jsp?name=korea한국&age=30살";
            String baseURL = "http://192.168.204.139/sam/sam.jsp?";
            String queryString = "name=";
            String webURL1 = "";
            try{
                name = URLEncoder.encode(name, "utf-8");
                age = URLEncoder.encode(age, "utf-8");
                queryString += name + "&age=";
                queryString += age;
                webURL1 = baseURL + queryString;
                Log.v(TAG, "webURL1 : " + webURL1);
            }catch(UnsupportedEncodingException e){

            }
            URL url = null;
            try{
                url = new URL(webURL1);
//                url = new URL(webURL);
            }catch(MalformedURLException e){
                Log.v(TAG, "형식 오류 : " + e);
                return;
            }
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            Message msg = uiHandler.obtainMessage();
            try{
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "Android");
                connection.setRequestProperty("name1", "korea한글");
                int code = connection.getResponseCode();

                switch (code){
                    case 200 :
//                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"euc-kr"));
                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String data = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        while( (data = reader.readLine()) != null ){
                            stringBuilder.append(data).append("\n");
                        }
                        msg.what = 999;
                        msg.obj = stringBuilder.toString();
                        uiHandler.sendMessage(msg);
                        break;
                    default:
                        msg.what = 888;
                        msg.arg1 = code;
                        uiHandler.sendMessage(msg);
                        break;
                }
            }catch(IOException e){
                Log.v(TAG, "error : " + e);
                msg.what = 777;
                msg.obj = e.getMessage();
                uiHandler.sendMessage(msg);
            }finally {
                if(reader != null){
                   try{
                       reader.close();
                   }catch(IOException e){
                   }
                }
                if(connection != null){
                      connection.disconnect();
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(handler);

        etURL = (EditText)findViewById(R.id.editText);
        etOutput = (EditText)findViewById(R.id.editText2);
    }
}
