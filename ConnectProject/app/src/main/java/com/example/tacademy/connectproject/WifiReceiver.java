package com.example.tacademy.connectproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.tacademy.connectproject.com.example.tacademy.connectproject.comm.LogMananger;

public class WifiReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        int type = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE,0);
        NetworkInfo info = manager.getNetworkInfo(type);
        NetworkInfo.DetailedState detailedState = info.getDetailedState();
        LogMananger.pringLog("state : " + detailedState.name());

        Intent sendIntent =
                new Intent("com.example.tacademy.connectproject.intent.action.CONNECT_STATE");
        sendIntent.putExtra("state", detailedState.name());
        context.sendBroadcast(sendIntent);
    }
}
