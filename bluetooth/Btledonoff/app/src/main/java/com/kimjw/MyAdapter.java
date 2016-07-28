package com.kimjw;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	
	DeviceActivity context;
	int layout;
	ArrayList<MyItem> data = null;
	public MyAdapter(DeviceActivity context, int layout, ArrayList<MyItem> data){
		this.context = context;
		this.layout = layout;
		this.data =data;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		if(cView == null){
			cView = View.inflate(context, layout, null);
		}
		
		final MyItem item = data.get(position);
		TextView tv = (TextView)cView.findViewById(R.id.textView1);
		tv.setText(item.getName());
		
		tv = (TextView)cView.findViewById(R.id.textView2);
		tv.setText(item.getAddress());
		
		cView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra("name", item.getName());
				data.putExtra("address", item.getAddress());
				context.setResult(context.RESULT_OK, data);
				context.finish();
			}
		});
		
		
		return cView;
	}

}
