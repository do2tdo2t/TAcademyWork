package com.example.tacademy.connectproject.com.example.tacademy.connectproject.comm;

import android.util.Log;

/**
 * Created by Tacademy on 2016-07-26.
 */
public class LogMananger {
    private static  final String TAG = "MainActivity";
    private static final boolean DEBUG = true;
    public static void pringLog(String msg){
        if(DEBUG) {
            Log.v(TAG, msg);
        }
    }
}
