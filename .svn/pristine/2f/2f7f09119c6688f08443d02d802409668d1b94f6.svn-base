package com.ginwave.smshelper;

import com.umeng.analytics.MobclickAgent;
import com.ginwave.smshelper.more.ListViewForAuto;
import com.ginwave.smshelper.more.SetDataSource;
import com.ginwave.smshelper.more.SettingForSMS;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DoorActivity extends Activity implements OnClickListener {

	private TextView mMainBack;
	private ListView mDoorGrid;
	private LayoutInflater mInflater;
	private String[] mDoorUrlArray;
	private String[] mDoorNoteArray;
	private int[] mDoorIcon = new int[]{R.drawable.door_zhangshang, R.drawable.door_wuxianchengshi, R.drawable.door_12580, 
			R.drawable.door_muzhileyuan, R.drawable.door_feixin, R.drawable.door_mm, R.drawable.door_139, 
			R.drawable.door_miguyinyue, R.drawable.door_shoujichonglang, R.drawable.door_yidongweibo, 
			R.drawable.door_shoujishipin, R.drawable.door_shoujidongman, R.drawable.door_youxidating};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.door);
		mInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mDoorGrid = (ListView) findViewById(R.id.mDoorGrid);
		mMainBack.setOnClickListener(this);
		mDoorUrlArray = this.getResources().getStringArray(R.array.door_url);
		mDoorNoteArray = this.getResources().getStringArray(R.array.door_note);
		ContentAdapter contentAdapter = new ContentAdapter(this);
		mDoorGrid.setAdapter(contentAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mMainBack)) {
			this.finish();
		}
		if(v.getId() == R.id.mDoorItem){
			int number = (Integer)v.getTag();
			Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(mDoorUrlArray[number]));
			this.startActivity(intent);
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	public class ContentAdapter extends BaseAdapter
	{
		private String[] mDoorDisplayArray;
		
		private Context mContext;
		public ContentAdapter(Context c)
		{
			mContext = c;
			mDoorDisplayArray = mContext.getResources().getStringArray(R.array.door_display);
		}

		// 获取图片的个数
		public int getCount()
		{
			return mDoorDisplayArray.length;
		}

		// 获取图片在库中的位置
		public Object getItem(int position)
		{
			return position;
		}


		// 获取图片ID
		public long getItemId(int position)
		{
			return position;
		}


		public View getView(int position, View convertView, ViewGroup parent)
		{
			TextView doorDisplay;
			TextView doorNote;
			ImageView doorIcon;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.door_item, null);
			}
			doorDisplay = (TextView)convertView.findViewById(R.id.mDoorDisplay);
			doorNote = (TextView)convertView.findViewById(R.id.mDoorNote);
			doorIcon = (ImageView) convertView.findViewById(R.id.mDoorIcon);
			doorDisplay.setText(mDoorDisplayArray[position]);
			doorNote.setText(mDoorNoteArray[position]);
			doorIcon.setBackgroundResource(mDoorIcon[position]);
			convertView.setTag(position);
			convertView.setOnClickListener(DoorActivity.this);
			return convertView;
		}
	}
}
