package com.kimjw;



import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kimjw.comm.BlueService;
import com.kimjw.comm.LogManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {
	BluetoothAdapter adapter  = null;	
	private static final int BT_ENABLED = 1 ;
	private static final int BT_CONNECTED = 2 ;
	String remoteName = "temp";

	BluetoothSocket bs = null;
	boolean onAir = false;
	DataInputStream dis = null;
	DataOutputStream dos = null;
	Button btnOn,btnOff;
	ImageView ledImage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.button1).setOnClickListener(bHandler);
		btnOn = (Button)findViewById(R.id.button2);
		btnOn.setOnClickListener(bHandler);
		btnOff = (Button)findViewById(R.id.button3);
		btnOff.setOnClickListener(bHandler);
		ledImage = (ImageView)findViewById(R.id.imageView1);
		btnOn.setEnabled(false);
		btnOff.setEnabled(false);
		
		adapter = BluetoothAdapter.getDefaultAdapter();
		if(adapter == null){
			Toast.makeText(this, "블루투스를 지원하는 않습니다.", Toast.LENGTH_SHORT).show();
			LogManager.logPrint( "블루투스를 지원하는 않습니다." );
		}else{
			LogManager.logPrint( "블루투스 장비가 지원됨");
		}

		if(!adapter.isEnabled()){
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(intent, BT_ENABLED);
		}
	}
	View.OnClickListener bHandler = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.button1 :
				Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
				startActivityForResult(intent, BT_CONNECTED);
				break;
			case R.id.button2 :
				sendData('n');
				break;
			case R.id.button3 :
				sendData('f');
				break;				
			}

		}
	};
	void sendData(char command){
		try{
//			1byte 문자 전송 방식
			dos.writeByte((byte)command);
			
//			3바이트 프로토콜 동신 방식 
//			dos.writeByte((byte)command);
//			dos.writeByte((byte)3);
//			dos.writeByte((byte)'k');
//			dos.writeByte((byte)10);  //커스텀 통신 END 처리 문자열
//			dos.flush();
		}catch(Exception e){
			LogManager.logPrint( "보내기 오류 : " +  e);
		}
	}
	class ChatThread extends Thread{
		public void run(){
			String str = "";
			Message msg = handler.obtainMessage();
			byte data = 0;
			//추가 소스
			byte[] tempData = new byte[255];
			int tempIdx = 0;
			try{

				dis = new DataInputStream(bs.getInputStream());
				dos = new DataOutputStream(bs.getOutputStream());

				while(onAir){
					data = dis.readByte();
					//추가 소스  커스텀 통신 모듈 처리
//					tempData[tempIdx++] = data;
//					byte[] sendBrr = null;
//					if(data == '\n'){
//						msg = handler.obtainMessage();
//						msg.what = 200;
//						msg.arg1 = tempData[0];
//						sendBrr = new byte[tempIdx-1];
//						System.arraycopy(tempData, 0, sendBrr, 0,sendBrr.length);
//						msg.obj = sendBrr;
//						handler.sendMessage(msg);	
//						tempIdx = 0;
//					}
					
					msg = handler.obtainMessage();
					msg.what = 200;
					msg.arg1 = data;
					handler.sendMessage(msg);
				}
			}catch(Exception e){
				LogManager.logPrint( "통신 오류 : " + e);
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
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 100 :
				remoteName = bs.getRemoteDevice().getName();
				btnOn.setEnabled(true);
				btnOff.setEnabled(true);
				Toast.makeText(MainActivity.this, remoteName + "장비에 접속되었습니다.", Toast.LENGTH_SHORT).show();
				break;
			case 200 :
				changeImage(msg.arg1);
				//커스텀 통신 모듈 처리
//				byte[] receiveData = (byte[])msg.obj;
//				for(int i = 0 ; i< receiveData.length; i++){
//					LogManager.logPrint( i +" :   " + receiveData[i]);
//				}
				break;
			}
		}

	};
	void changeImage(int data){
		LogManager.logPrint( "data : " + data);
		char command = (char)data;
		switch(command){
		case 'n':
			ledImage.setImageResource(R.drawable.on);
			break;
		case 'f':
			ledImage.setImageResource(R.drawable.off);
			break;
		}
	}

	@Override
	protected void onStop() {
		if(bs != null){
			if(bs.isConnected()){
				try{
					bs.close();
				}catch(IOException e){}
			}
		}
		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case BT_ENABLED :
			switch(resultCode){
			case RESULT_CANCELED :
				LogManager.logPrint( "블루투스 사용하지 않음");
				break;
			case RESULT_OK :
				LogManager.logPrint( "블루투스 켜기 준비");
				break;
			}
			break;
		case BT_CONNECTED :
			switch(resultCode){
			case RESULT_OK :

				remoteName = data.getStringExtra("name");				
				data.getStringExtra("address");
				LogManager.logPrint( "클라이언트 접속 시도");
				new ConnectThread(data.getStringExtra("address")).start();
				break;
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	class ConnectThread extends Thread{
		String address; 
		BluetoothDevice device = null;
		ConnectThread(String address){

			if(adapter.isDiscovering()){
				adapter.cancelDiscovery();
			}

			this.address = address;
			device = adapter.getRemoteDevice(address);
		}
		public void run(){
			Message msg = handler.obtainMessage();
			try{
				msg.what = 100;
				msg.obj = "클라이언트  서비스 생성시작";
				handler.sendMessage(msg);

				bs = device.createRfcommSocketToServiceRecord(BlueService.MY_UUID_SECURE);

				bs.connect();

				msg = handler.obtainMessage();
				msg.what = 100;
				msg.obj = "접속 성공";
				handler.sendMessage(msg);

				LogManager.logPrint( "접속 성공");
				onAir  = true;
				new ChatThread().start();

			}catch(Exception e){
				LogManager.logPrint( "클라이언트 접속 오류 : " + e);
			}

		}
	}
}
