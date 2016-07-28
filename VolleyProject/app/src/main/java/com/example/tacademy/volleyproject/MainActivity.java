package com.example.tacademy.volleyproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    RequestQueue volley;
    EditText et;

    public void doAction1(View v){
        String url ="http://apis.skplanetx.com/melon/charts/realtime?count=10&page=1&version=1&appKey=31c1e579-b9c2-3697-95d3-ed1ba8a82815&format=json";
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                et.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        volley.add(request);


        JsonObjectRequest request1 = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String name = response.getJSONObject("melon").getJSONObject("songs").getJSONArray("song").getJSONObject(0).getString("songName");
                    Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
                }catch(JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        ImageRequest request2 =  null;


        volley.add(request);
        volley.add(request1);
        request2.setTag("aaa");
        volley.cancelAll("aaa");
        volley.add(request2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        volley = Volley.newRequestQueue(this);

        et = (EditText)findViewById(R.id.editText);
//        volley.add(request);
    }
}
