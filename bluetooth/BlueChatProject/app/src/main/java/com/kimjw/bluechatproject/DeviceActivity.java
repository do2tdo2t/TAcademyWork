package com.kimjw.bluechatproject;


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

import java.util.ArrayList;
import java.util.Set;

public class DeviceActivity extends Activity {

    private static final String TAG = "MainActivity";
    BluetoothAdapter adapter = null;

    ListView list = null;
    ArrayList<MyItem> data = new ArrayList<MyItem>();
    MyAdapter adapter1 = null;



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
        if(adapter.isDiscovering()){
            adapter.cancelDiscovery();
        }
        adapter.startDiscovery();
    }
    Set<BluetoothDevice> devices = null;

    BroadcastReceiver receiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                Log.v(TAG, "주변장비 검색 완료");
            }else if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device
                        = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.v(TAG, "이름 : " + device.getName());
                doAddItem(device);
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

        adapter = BluetoothAdapter.getDefaultAdapter();
        devices =  adapter.getBondedDevices();



        adapter1 = new MyAdapter(this, R.layout.item, data);

        list.setAdapter(adapter1);
        doShowList();
        if(adapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent dIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            dIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(dIntent);

        }
        findViewById(R.id.button1).setOnClickListener(bHandler);
    }
    void doShowList(){

        for(BluetoothDevice device : devices){
            Log.v(TAG, "주소 : " + device.getAddress() +
                    " 이름 : " + device.getName());
            doAddItem(device);
        }
    }

    void doAddItem(BluetoothDevice device){
        MyItem item = new MyItem();
        item.setAddress(device.getAddress());
        item.setName(device.getName());
        if(isCheck(item)) {
            data.add(item);
            adapter1.notifyDataSetChanged();
        }
    }
    boolean isCheck(MyItem item){
        boolean flag = true;
        for(MyItem item1 : data){
            if(item1.getName().equals(item.getName())){
                flag = false;
                break;
            }
        }
        return flag;
    }


}