package com.example.vollytest;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.Volley;

public class MyAdpater extends BaseAdapter {
	Context context;
	ArrayList<MyItem> data = null;
	RequestQueue mRequest = null;
	int layout;
	public MyAdpater(Context context, int layout, ArrayList<MyItem> data){
		this.context = context;
		this.data = data;
		this.layout = layout;
		mRequest = Volley.newRequestQueue(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return data.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	class ViewHolder {
		TextView tv;
		ImageView img;
		ImageContainer imgrequest;
	}
	ImageLoader  imageLoader= null;
	@Override
	public View getView(int pos, View cView, ViewGroup arg2) {
		ViewHolder  holder = null;
		MyItem item = data.get(pos);
		
		if(cView == null){
			cView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.tv = (TextView)cView.findViewById(R.id.textView1);
			holder.img = (ImageView)cView.findViewById(R.id.imageView1);
			
			cView.setTag( holder);
		}else{
			holder = (ViewHolder)cView.getTag();
		}
		holder.tv.setText(item.getTitle());
		imageLoader = new ImageLoader(mRequest, new BitmapLruCache());
		if(holder.imgrequest != null){
			holder.imgrequest.cancelRequest();
		}
		
		holder.imgrequest = imageLoader.get(Common.BASE_URL + item.getImgName(),
				ImageLoader.getImageListener(holder.img, R.drawable.ic_launcher,
						R.drawable.ic_launcher));
//		imageLoader = new ImageLoader(mRequest, new BitmapLruCache());
//		new LoadImageTask(holder.img).execute(item.getImgName());

		return cView;
	}

	class LoadImageTask extends AsyncTask<String, Void, Bitmap>{
		ImageView img;
		LoadImageTask(ImageView img){
			this.img =img;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			URL url = null;
			Bitmap bitmap = null;

			try{
				url = new URL(Common.BASE_URL + params[0]);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				InputStream input = conn.getInputStream();
				return BitmapFactory.decodeStream(input);

			}catch(Exception e){
				Log.v(Common.TAG, "loaderror : " + e);
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			img.setImageBitmap(result);

			super.onPostExecute(result);
		}


	}

}
