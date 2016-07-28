package com.example.tacademy.aquerypostproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText et;
    AQuery aq;
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
        String url = "http://192.168.204.181/sam/sam1.jsp";

        Map<String, String> params = new HashMap<String, String>();
        params.put("name","korea한글");
        params.put("age","23");
        params.put("address","seoul");
        aq.ajax(url, params, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                et.setText(object);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(handler);

        et = (EditText)findViewById(R.id.editText);
        aq = new AQuery(this);

    }
}
