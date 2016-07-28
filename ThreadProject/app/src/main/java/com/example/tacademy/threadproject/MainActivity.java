package com.example.tacademy.threadproject;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText et1, et2;
    ProgressBar progressBar;
    ProgressDialog progressDialog;

    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.button :
                    doAction1();
                    break;
                case R.id.button2 :
                    doAction2();
                    break;
                case R.id.button3 :
                    doAction3();
                    break;
                case R.id.button4 :
                    doAction4();
                    break;
            }
        }
    };
    void doAction3(){
        new JobTask().execute(20, 80, 2);

    }
    class JobTask extends AsyncTask<Integer, Integer, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "","작업중");
        }
        @Override
        protected String doInBackground(Integer... params) {
            int start = params[0];
            int end = params[1];
            int step = params[2];
            int num = start;
            while(num <= end){
                num += step;
                Log.v(TAG, "num : " + num);

                if(num % 10 == 0){
                    publishProgress(num,3,4,5,5);
                }
                SystemClock.sleep(200);
            }
            return "success";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(progressDialog != null){
                if(progressDialog.isShowing()){
                    progressDialog.cancel();
                }
            }
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }




    }

    JobThread1 trd1 = null;
    JobThread2 trd2 = null;
    void doAction2(){
        if(trd2 == null) {
            onAir = true;
            trd2 = new JobThread2();
            trd2.start();
        }
    }
    void doAction1(){
        if(trd1 == null) {
            onAir = true;
            trd1 = new JobThread1();
            trd1.start();
        }
    }
    void stopThread(){
        if(trd1 != null){
            onAir = false;
            try {
                trd1.join();
            }catch (InterruptedException e){}
            trd1 = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopThread();
    }
    Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200 :
                    et1.setText(msg.arg1 + "");
                    break;
                case 300 :
                    et2.setText(msg.arg1 + "");
                    break;
            }

        }
    };
    Handler uiHandler1 = new Handler();
    boolean onAir = false;
    class JobThread2 extends Thread{
        int cnt = 0;
        @Override
        public void run() {
            Message msg = null;

            while( onAir ){
                cnt++;
                Log.v(TAG, "cnt : " + cnt);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        et1.setText(cnt + "");
//                    }
//                });
//                uiHandler1.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        et1.setText(cnt + "");
//                    }
//                });
                msg = uiHandler.obtainMessage();
                msg.what = 200;
                msg.arg1 = cnt;

                uiHandler.sendMessage(msg);
                SystemClock.sleep(700);
            }
        }
    }
    class JobThread1 extends Thread{
        int cnt = 0;
        @Override
        public void run() {
            Message msg = null;

            while( onAir ){
                cnt++;
                Log.v(TAG, "cnt : " + cnt);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        et1.setText(cnt + "");
//                    }
//                });
//                uiHandler1.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        et1.setText(cnt + "");
//                    }
//                });
                msg = uiHandler.obtainMessage();
                msg.what = 300;
                msg.arg1 = cnt;

                uiHandler.sendMessage(msg);
                SystemClock.sleep(500);
            }
        }
    }
    int x, y;
    void doAction4(){
        x++;
        y++;
        et1.setText("x : " + x + ", y : " + y);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
//        SharedPreferences.Editor editor =sp.edit();
//
//        editor.putInt("y",y);
//        editor.commit();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
//        y = sp.getInt("y", 100);
//    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("x", x);
//    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation){
            case Configuration.ORIENTATION_LANDSCAPE :
                break;
            case Configuration.ORIENTATION_PORTRAIT :
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if(savedInstanceState != null){
//            x = savedInstanceState.getInt("x", 100);
//        }
        findViewById(R.id.button).setOnClickListener(handler);
        findViewById(R.id.button2).setOnClickListener(handler);
        findViewById(R.id.button3).setOnClickListener(handler);
        findViewById(R.id.button4).setOnClickListener(handler);

        et1 = (EditText)findViewById(R.id.editText);
        et2 = (EditText)findViewById(R.id.editText2);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }
}
