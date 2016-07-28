package com.example.tacademy.chatclientproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText etMsg, etOutput;
    Socket socket = null;
    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button:
                    doAction1();
                    break;
                case R.id.button2:
                    doAction2();
                    break;
                case R.id.button3:
                    doAction3();
                    break;
            }
        }
    };
    void senddMsg(String msg){
        try{
            dos.writeChar('D');
            dos.writeUTF(msg);
            dos.flush();
        }catch (IOException e){
            
        }
    }
    void doAction3(){
        String msg = etMsg.getText().toString();
        senddMsg(msg);
    }
    void closeConection(){
        if(dis != null){
            try{
                dis.close();
            }catch(IOException e){}
        }
        if(dos != null){
            try{
                dis.close();
            }catch(IOException e){}
        }
        onAir = false;
        if(socket != null){
            try{
                socket.close();
            }catch(IOException e){}
        }
    }
    void doAction2(){
        senddMsg("/q");
        closeConection();
    }
    void doAction1(){
       new ConnectThread().start();
    }

    Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 100 :
                    etOutput.append(msg.obj.toString() + "\n");
                    break;
                case 200 :
                    etOutput.append("오류 : " + msg.obj.toString() + "\n");
                    break;
                case 300 :
                    etOutput.append("받은문자 : " + msg.obj.toString() + "\n");
                    break;
            }
        }
    };
    boolean onAir = false;
    class ReadThread extends Thread{
        public void run(){
            String data = "";
            Message msg = null;
            while( onAir ){

                try{
                    data = dis.readUTF();
                    msg = uiHandler.obtainMessage();
                    msg.what = 300 ;
                    msg.obj = data;
                    uiHandler.sendMessage(msg);
                }catch(IOException e){
                    msg = uiHandler.obtainMessage();
                    msg.what = 200;
                    msg.obj = e.getMessage();
                    uiHandler.sendMessage(msg);
                    onAir = false;
                }
            }
        }
    }
    DataInputStream dis = null;
    DataOutputStream dos = null;
    class ConnectThread extends Thread{
        public void run(){
            Message msg = uiHandler.obtainMessage();
            try {
                socket = new Socket("192.168.204.139", 12345);
                msg.what = 100;
                msg.obj = "접속 성공";
                uiHandler.sendMessage(msg);
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                msg = uiHandler.obtainMessage();
                msg.what = 100;
                msg.obj = "입출력 준비완료";
                uiHandler.sendMessage(msg);
                onAir = true;
                new ReadThread().start();
                msg = uiHandler.obtainMessage();
                msg.what = 100;
                msg.obj = "전송준비완료";
                uiHandler.sendMessage(msg);
            }catch(Exception e) {
                Log.v(TAG, "error : " + e);
                msg = uiHandler.obtainMessage();
                msg.what = 200;
                msg.obj = e.getMessage();
                uiHandler.sendMessage(msg);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(handler);
        findViewById(R.id.button2).setOnClickListener(handler);
        findViewById(R.id.button3).setOnClickListener(handler);

        etMsg = (EditText)findViewById(R.id.editText);
        etOutput = (EditText)findViewById(R.id.editText2);
    }
}
