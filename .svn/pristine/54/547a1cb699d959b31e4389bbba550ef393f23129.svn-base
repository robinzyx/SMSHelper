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
import 	android.widget.AdapterView;

public class LifeActivity extends Activity implements OnClickListener, AdapterView.OnItemClickListener{

	private TextView mMainBack;
	private ListView mLifeGrid;
	private LayoutInflater mInflater;
	private String[] mLifeUrlArray = new String[]{
			"http://a.10086.cn/pams2/m/s.do?gId=300000863435&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300001141606&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300002797930&c=172&j=l&p=72&src=90.620009.100867588", 
			"http://a.10086.cn/pams2/m/s.do?gId=300002617098&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300000004294&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300002584247&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300002734449&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300002582359&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300000038424&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300000008459&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300002437539&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300000034255&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300001502369&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300002757446&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300000004296&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300000004151&c=172&j=l&p=72&src=90.620009.100867588",
			"http://a.10086.cn/pams2/m/s.do?gId=300001201593&c=172&j=l&p=72&src=90.620009.100867588"
			
	};
	private int[] mLifeIconRes = new int[]{R.drawable.life1, R.drawable.life17, R.drawable.life2, R.drawable.life3, R.drawable.life4, R.drawable.life5,
			R.drawable.life6, R.drawable.life7, R.drawable.life8, R.drawable.life9, R.drawable.life10, 
			R.drawable.life11, R.drawable.life12, R.drawable.life13, R.drawable.life14, R.drawable.life15, R.drawable.life16};

	private int[] mLifeCodeRes = new int[]{
			R.drawable.life_code1, R.drawable.life_code17, R.drawable.life_code2, R.drawable.life_code3, R.drawable.life_code4, R.drawable.life_code5,
			R.drawable.life_code6, R.drawable.life_code7, R.drawable.life_code8, R.drawable.life_code9, R.drawable.life_code10, 
			R.drawable.life_code11, R.drawable.life_code12, R.drawable.life_code13, R.drawable.life_code14, R.drawable.life_code15, R.drawable.life_code16
	};
	
	private String[] mLifeDisplayArray;
	private String[] mLifeNoteArray;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.life);
		mInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mLifeGrid = (ListView) findViewById(R.id.mLifeGrid);
		mLifeDisplayArray = this.getResources().getStringArray(R.array.life_display);
		mLifeNoteArray = this.getResources().getStringArray(R.array.life_note);
		mMainBack.setOnClickListener(this);
		ContentAdapter contentAdapter = new ContentAdapter(this);
		mLifeGrid.setAdapter(contentAdapter);
		mLifeGrid.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mMainBack)) {
			this.finish();
		}
		if(v.getId() == R.id.mLifeDown){
			int number = (Integer)v.getTag();
			Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(mLifeUrlArray[number]));
			this.startActivity(intent);
		}
		if(v.getId() == R.id.mLifeIcon){
			int number = (Integer)v.getTag();
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), LifeDetailActivity.class);
			intent.putExtra("life_icon", mLifeIconRes[number]);
			intent.putExtra("life_name", mLifeDisplayArray[number]);
			intent.putExtra("life_note", mLifeNoteArray[number]);
			intent.putExtra("life_note_icon", mLifeCodeRes[number]);
			intent.putExtra("life_url", mLifeUrlArray[number]);
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
		
		private Context mContext;
		private ViewHolder mViewHolder;
		
		public ContentAdapter(Context c)
		{
			mContext = c;
			
		}

		// 获取图片的个数
		public int getCount()
		{
			return mLifeDisplayArray.length;
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
			mViewHolder = new ViewHolder();
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.life_item, null);
				mViewHolder.lifeIcon = (ImageView)convertView.findViewById(R.id.mLifeIcon);
				mViewHolder.lifeNote = (TextView) convertView.findViewById(R.id.mLifeNote);
				mViewHolder.lifeDown = (TextView) convertView.findViewById(R.id.mLifeDown);
				mViewHolder.lifeDisplay = (TextView) convertView.findViewById(R.id.mLifeDisplay);
			}
			else{
				mViewHolder = (ViewHolder)convertView.getTag();
			}
			mViewHolder.lifeIcon.setBackgroundResource(mLifeIconRes[position]);
			mViewHolder.lifeDisplay.setText(mLifeDisplayArray[position]);
			mViewHolder.lifeNote.setText(mLifeNoteArray[position]);
			mViewHolder.lifeDown.setOnClickListener(LifeActivity.this);
			mViewHolder.lifeIcon.setOnClickListener(LifeActivity.this);
			mViewHolder.lifeDown.setTag(position);
			mViewHolder.lifeIcon.setTag(position);
			convertView.setTag(mViewHolder);
			return convertView;
		}
		
		public final class ViewHolder {  
			public ImageView lifeIcon;
			public TextView lifeDisplay;
			public TextView lifeNote;
			public TextView lifeDown;
		}  

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), LifeDetailActivity.class);
		intent.putExtra("life_icon", mLifeIconRes[arg2]);
		intent.putExtra("life_name", mLifeDisplayArray[arg2]);
		intent.putExtra("life_note", mLifeNoteArray[arg2]);
		intent.putExtra("life_note_icon", mLifeCodeRes[arg2]);
		intent.putExtra("life_url", mLifeUrlArray[arg2]);
		this.startActivity(intent);
	}
}
