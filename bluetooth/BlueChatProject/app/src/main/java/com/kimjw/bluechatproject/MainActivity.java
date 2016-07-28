package com.kimjw.bluechatproject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter adapter  = null;
    private static final String TAG = "MainActivity";
    private static final int BT_ENABLED = 1 ;
    private static final int BT_CONNECTED = 2 ;

    EditText etMsg, etOutput;

    View.OnClickListener bHandler = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.button1 :
                    Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button2 :
                    etOutput.append("서버시작1\n");
                    Log.v(TAG, "서버시작1");

                    new ServerThread().start();
                    break;
                case R.id.button3 :
                    Intent intent1 = new Intent(MainActivity.this, DeviceActivity.class);

                    startActivityForResult(intent1, BT_CONNECTED);
                    break;
                case R.id.button4 :
                    sendData();
                    break;
                case R.id.button5 :
                    sendImage();
                    break;

            }

        }
    };

    void sendImage(){
        String str = "sample.png";
        FileInputStream fis = null;
        long size = 0;
        int temp = 0;
        int cnt = 0;
        // 1  sapple.png     3245    01010101010101
        try{
            dos.writeByte(1);
            dos.flush();
            File f = new File(Environment.getExternalStorageDirectory().getPath() + str);

            dos.writeUTF(str);
            dos.flush();
            size = f.length();
            dos.writeLong(size);
            dos.flush();

            fis = new FileInputStream(f);
            cnt = fis.available();

            byte[] arr = new byte[cnt];
            int tempCnt = 0;
            if(cnt == -1){
                while( (temp = fis.read()) != -1){
                    dos.write(temp);
                }
            }else{
                cnt = 0;
                while( (tempCnt = fis.read(arr, 0, arr.length)) >0){
                    dos.write(arr, 0, tempCnt);
                    cnt += tempCnt;
                    if(cnt >= size){
                        break;
                    }
                }
            }


            dos.flush();

            Log.v(TAG, "이미지 전송 성공");
        }catch(Exception e){
            Log.v(TAG, "이미지 전송 오류 : " + e);
        }finally{
            try{
                if(fis != null){
                    fis.close();
                }
            }catch(Exception e){}
        }

    }

    void sendData(){
        try{
            dos.writeByte(0);
            dos.flush();
            String str = etMsg.getText().toString();

            dos.writeUTF(str);
            etOutput.append("me : " + str + "\n");
            etMsg.setText("");
        }catch(Exception e){
            Log.v(TAG, "보내기 오류 : " +  e);
        }
    }
    BluetoothServerSocket bss = null;
    BluetoothSocket bs = null;


    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 100 :
                    etOutput.append(msg.obj + "\n");
                    break;
                case 200 :
                    etOutput.append(remoteName + " : " + msg.obj + "\n");
                    break;
            }
            super.handleMessage(msg);
        }

    };
    String remoteName = "temp";
    DataInputStream dis = null;
    DataOutputStream dos = null;
    boolean onAir = false;
    class ChatThread extends Thread{

        public void run(){
            String str = "";
            Message msg = handler.obtainMessage();
            byte type = 0;
            try{
                remoteName = bs.getRemoteDevice().getName();
                dis = new DataInputStream(bs.getInputStream());
                dos = new DataOutputStream(bs.getOutputStream());

                msg = handler.obtainMessage();
                msg.what = 200;
                msg.obj = "대화준비완료";
                handler.sendMessage(msg);

                while(onAir){
                    type = dis.readByte();
                    switch(type){
                        case 0 :	//문자받아서 처리하기
                            str = dis.readUTF();
                            if( !str.equals("exit") ){
                                msg = handler.obtainMessage();
                                msg.what = 200;
                                msg.obj = str;
                                handler.sendMessage(msg);
                            }else{
                                msg = handler.obtainMessage();
                                msg.what = 200;
                                msg.obj = "상대방이 나감";
                                handler.sendMessage(msg);
                                break;
                            }
                            break;
                        case 1 :
                            // 1  sapple.png     3245    ~~~~~~~~~~~~~

                            downImage();
                            break;
                    }
//					str = dis.readUTF();
//					if( !str.equals("exit") ){
//						msg = handler.obtainMessage();
//						msg.what = 200;
//						msg.obj = str;
//						handler.sendMessage(msg);
//					}else{
//						msg = handler.obtainMessage();
//						msg.what = 200;
//						msg.obj = "상대방이 나감";
//						handler.sendMessage(msg);
//						break;
//					}
                }
            }catch(Exception e){
                Log.v(TAG, "통신 오류 : " + e);
            }finally{
                try{
                    if(dis != null){
                        dis.close();
                    }
                }catch(Exception e){}
                try{
                    if(dos != null){
                        dos.close();
                    }
                }catch(Exception e){}
                try{
                    if(bs != null){
                        bs.close();
                    }
                }catch(Exception e){}
            }
        }
    }
    void downImage() throws Exception {
        // 1  sapple.png     3245    ~~~~~~~~~~~~~

        String fName = dis.readUTF();
        int size = (int)dis.readLong();
        byte[] arr = new byte[size];

        dis.readFully(arr, 0, size);
        // 파일 저장


    }
    class ServerThread extends Thread{
        public void run(){
            Message msg = handler.obtainMessage();
            try{
                bss = adapter.listenUsingRfcommWithServiceRecord("server", BlueService.MY_UUID_SECURE);
                msg.what = 100;
                msg.obj = "서버 생성 성공 대기중";
                handler.sendMessage(msg);

                bs = bss.accept();

                msg = handler.obtainMessage();
                msg.what = 100;
                msg.obj = "접속 성공";
                handler.sendMessage(msg);
                onAir  = true;
                new ChatThread().start();
            }catch(Exception e){
                Log.v(TAG, "서버 오류 : " + e);
            }finally{
                try{
                    if(bss != null){
                        bss.close();
                    }
                }catch(Exception e){}
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(bHandler);
        findViewById(R.id.button2).setOnClickListener(bHandler);
        findViewById(R.id.button3).setOnClickListener(bHandler);
        findViewById(R.id.button4).setOnClickListener(bHandler);
        findViewById(R.id.button5).setOnClickListener(bHandler);

        etMsg = (EditText)findViewById(R.id.editText1);
        etOutput = (EditText)findViewById(R.id.editText2);



        adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null){
            Toast.makeText(this, "블루투스를 지원하는 않습니다.", Toast.LENGTH_SHORT).show();
            Log.v(TAG, "블루투스를 지원하는 않습니다.");
            // 대제 작업 준비
        }else{
            Log.v(TAG, "블루투스 장비가 지원됨");
        }

        if(!adapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BT_ENABLED);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case BT_ENABLED :
                switch(resultCode){
                    case RESULT_CANCELED :
                        Log.v(TAG, "블루투스 사용하지 않음");
                        break;
                    case RESULT_OK :
                        Log.v(TAG, "블루투스 켜기 성공");
                        break;
                }
                break;
            case BT_CONNECTED :
                switch(resultCode){
                    case RESULT_OK :

                        remoteName = data.getStringExtra("name");
                        data.getStringExtra("address");
                        etOutput.append("클라이언트 접속 시도\n");
                        new ConnectThread(data.getStringExtra("address")).start();
                        break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class ConnectThread extends Thread {
        String address;
        BluetoothDevice device = null;

        ConnectThread(String address) {

            if (adapter.isDiscovering()) {
                adapter.cancelDiscovery();
            }

            this.address = address;
            device = adapter.getRemoteDevice(address);
        }

        public void run() {
            Message msg = handler.obtainMessage();
            try {
                msg.what = 100;
                msg.obj = "클라이언트  서비스 생성시작";
                handler.sendMessage(msg);

                bs = device.createRfcommSocketToServiceRecord(BlueService.MY_UUID_SECURE);

                bs.connect();

                msg = handler.obtainMessage();
                msg.what = 100;
                msg.obj = "접속 성공";
                handler.sendMessage(msg);


                onAir = true;
                new ChatThread().start();

            } catch (Exception e) {
                Log.v(TAG, "클라이언트 접속 오류 : " + e);
            }

        }
    }
}
