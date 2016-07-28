package com.example.tacademy.jsonproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.androidquery.AQuery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ListView listView;
    ProductAdapter adapter;
    ProductList productList;
    AQuery aq;
    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.button :
                    doAction2();
                    break;
            }
        }
    };
    void doAction2(){
        new Thread(){
            public void run(){
                doAction1();
            }
        }.start();
    }
    void doAction1(){
        String url = "http://192.168.204.129/sam1/t1.jsp";
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        URL webURL = null;
        int code = 0;
        try{
            webURL = new URL(url);
            connection = (HttpURLConnection)webURL.openConnection();
            code = connection.getResponseCode();
            switch(code){
                case 200 :
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String data = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while( ( data = reader.readLine() ) != null){
                        stringBuilder.append(data).append('\n');
                    }
                    String result = stringBuilder.toString();
                    ProductList productList = ProductListJsonParser.parse(result);
                    Log.v(TAG, "result : " + productList.toString());
//                    Log.v(TAG, "result : " + result);
                    break;
                default:
                    Log.v(TAG, "code error : " + code);
                    break;
            }
        }catch(IOException e){

        }finally {

        }
//        aq.ajax(url, String.class, new AjaxCallback<String>(){
//            @Override
//            public void callback(String url, String object, AjaxStatus status) {
//                Log.v(TAG, object);
//            }
//        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(handler);
        aq = new AQuery(this);
    }
}
