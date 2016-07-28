package com.kimjw;

import java.util.ArrayList;
import java.util.Set;

import com.kimjw.comm.LogManager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class DeviceActivity extends Activity {
	BluetoothAdapter btAdapter = null;

	ListView list = null;
	ArrayList<MyItem> data = new ArrayList<MyItem>();
	MyAdapter adapter = null;
	Set<BluetoothDevice> devices = null;

	View.OnClickListener bHandler  = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.button1 :
				doDiscovery();
				break;
			}
		}
	};
	void doDiscovery(){
		if(btAdapter.isDiscovering()){
			btAdapter.cancelDiscovery();
		}
		btAdapter.startDiscovery();
	}
	BroadcastReceiver receiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
				LogManager.logPrint( "주변장비 검색 완료");
			}else if(action.equals(BluetoothDevice.ACTION_FOUND)){
				BluetoothDevice device 
				= intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				LogManager.logPrint( "이름 : " + device.getName());
				try{
					doAddItem(device);
				}catch(Exception e){
					LogManager.logPrint( "삽입 에러 : " + e);
				}
				adapter.notifyDataSetChanged();
			}

		}

	};
	@Override
	protected void onPause() {
		unregisterReceiver(receiver);
		super.onPause();
	}
	@Override
	protected void onResume() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothDevice.ACTION_FOUND);

		registerReceiver(receiver, filter);
		super.onResume();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.device);

		list = (ListView)findViewById(R.id.listView1);
		findViewById(R.id.button1).setOnClickListener(bHandler);

		btAdapter = BluetoothAdapter.getDefaultAdapter();
		devices =  btAdapter.getBondedDevices();

		//기존 블루투스 장비 검색 리스트 출력
//				if(devices != null && devices.size() > 0){
//					doShowList();
//				}

		adapter = new MyAdapter(this, R.layout.item, data);

		list.setAdapter(adapter);

		//		주변 블루투스 장비가 앱이 설치된 장비를 검색할 수 있도록 하는 소스
		//		if(btAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
		//			Intent dIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		//			dIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		//			startActivity(dIntent);
		//		}
		findViewById(R.id.button1).setOnClickListener(bHandler);
	}
	void doShowList(){
		for(BluetoothDevice device : devices){
			LogManager.logPrint( "주소 : " + device.getAddress() + 
					" 이름 : " + device.getName());
			doInitItem(device);
		}
	}
	void doInitItem(BluetoothDevice device){
		MyItem item = new MyItem();
		item.setAddress(device.getAddress());
		item.setName(device.getName());
		data.add(item);

	}
	void doAddItem(BluetoothDevice device) throws Exception{
		boolean flag = true;
		for(MyItem item  : data){ //검색된 device 목록에 존재 여부 검색
			if(device.getName().equals(item.getName())){
				flag = false;
				break;
			}
		}
		if(flag){
			MyItem item = new MyItem();
			item.setAddress(device.getAddress());
			item.setName(device.getName());
			data.add(item);
		}
	}


}
