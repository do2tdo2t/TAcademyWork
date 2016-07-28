package com.example.vollytest;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity {

	ListView list;
	MyAdpater adapter = null;
	ArrayList<MyItem> data = new ArrayList<MyItem>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String urls[] = { 
				"/6101/6853156632_6374976d38_c.jpg",
				"/7232/6913504132_a0fce67a0e_c.jpg",
				"/4133/5096108108_df62764fcc_b.jpg",
				"/4074/4789681330_2e30dfcacb_b.jpg",
				"/8208/8219397252_a04e2184b2.jpg",
				"/8483/8218023445_02037c8fda.jpg",
				"/8335/8144074340_38a4c622ab.jpg",
				"/8060/8173387478_a117990661.jpg",
				"/8056/8144042175_28c3564cd3.jpg",
				"/8183/8088373701_c9281fc202.jpg",
				"/8185/8081514424_270630b7a5.jpg",
				"/8462/8005636463_0cb4ea6be2.jpg",
				"/8306/7987149886_6535bf7055.jpg",
				"/8444/7947923460_18ffdce3a5.jpg",
				"/8182/7941954368_3c88ba4a28.jpg",
				"/8304/7832284992_244762c43d.jpg",
				"/8163/7709112696_3c7149a90a.jpg",
				"/7127/7675112872_e92b1dbe35.jpg",
				"/7111/7429651528_a23ebb0b8c.jpg",
				"/8288/7525381378_aa2917fa0e.jpg",
				"/5336/7384863678_5ef87814fe.jpg",
				"/7102/7179457127_36e1cbaab7.jpg",
				"/7086/7238812536_1334d78c05.jpg",
				"/7243/7193236466_33a37765a4.jpg",
				"/7251/7059629417_e0e96a4c46.jpg",
				"/7084/6885444694_6272874cfc.jpg"
		};
		MyItem item = null;
		for(String s : urls){
			item = new MyItem();
			item.setImgName(s);
			item.setTitle(s);
			data.add(item);
		}
		adapter = new MyAdpater(this, R.layout.item, data);
		list = (ListView)findViewById(R.id.listView1);
		list.setAdapter(adapter);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
