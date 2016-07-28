package com.example.tacademy.saxproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.tacademy.saxproject.com.example.tacademy.saxproject.vo.Weather;
import com.example.tacademy.saxproject.com.example.tacademy.saxproject.vo.com.example.tacademy.saxproject.parser.WeatherSAXParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
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
    void doAction1(){
        new JobThread().start();
    }
    class JobThread extends Thread{
        public void run(){
            String url ="http://www.kma.go.kr/XML/weather/sfc_web_map.xml";
            URL weatherURL = null;
            HttpURLConnection connection = null;
            try{
                weatherURL = new URL(url);
                connection = (HttpURLConnection)weatherURL.openConnection();
                int code = connection.getResponseCode();
                switch (code){
                    case 200 :
                        WeatherSAXParser parser = new WeatherSAXParser();
                        Weather weather = parser.parse(connection.getInputStream());
                        Log.v(TAG, weather.toString());
                        break;
                }
            }catch (IOException e){

            }finally {

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(handler);
    }
}
