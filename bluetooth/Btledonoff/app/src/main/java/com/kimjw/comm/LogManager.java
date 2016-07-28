package com.kimjw.comm;

import android.util.Log;

public class LogManager {
	private static final String TAG = "BTLEDONOFF";
	private static final boolean DEBUG = true;
	public static void logPrint(String tag){
		if(DEBUG){
			Log.v(TAG, tag);
		}
	}
}
