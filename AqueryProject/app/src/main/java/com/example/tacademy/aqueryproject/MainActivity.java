package com.example.tacademy.aqueryproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    AQuery aq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aq = new AQuery(this);
        aq.id(R.id.button).text("확인").textColor(Color.RED).textSize(20).clicked(this, "doAction1");
    }


    public void doAction1(View v){
//        Toast.makeText(this, "토스트", Toast.LENGTH_SHORT).show();
        String url = "http://www.google.com/uds/GnewsSearch?q=Obama&v=1.0";
        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try {
                    aq.id(R.id.editText).text(object.getString("responseDetails"));
                }catch(JSONException e){}
            }
        });
    }
//        String url = "http://apis.skplanetx.com/melon/charts/realtime?count=10&page=1&version=1&appKey=31c1e579-b9c2-3697-95d3-ed1ba8a82815&format=json";
//        aq.ajax(url, String.class, new AjaxCallback<String>(){
//            @Override
//            public void callback(String url, String object, AjaxStatus status) {
//                aq.id(R.id.editText).text(object);
//            }
//        });
//    }

}
