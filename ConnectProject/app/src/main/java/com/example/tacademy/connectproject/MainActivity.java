package com.example.tacademy.connectproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.tacademy.connectproject.com.example.tacademy.connectproject.comm.LogMananger;

public class MainActivity extends AppCompatActivity {

    ConnectivityManager manager;
    WifiManager wifiManager;
    EditText et;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            et.append( intent.getStringExtra("state") + "\n");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.tacademy.connectproject.intent.action.CONNECT_STATE");
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        LogMananger.pringLog("typeName : " + info.getTypeName());
        NetworkInfo.DetailedState state = info.getDetailedState();

        String aciion = ConnectivityManager.CONNECTIVITY_ACTION;
        LogMananger.pringLog("name : " + state.name());
        et = (EditText)findViewById(R.id.editText);

    }
}
