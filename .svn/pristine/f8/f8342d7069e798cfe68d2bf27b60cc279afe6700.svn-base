package com.ginwave.smshelper;

import java.util.ArrayList;

import com.umeng.analytics.MobclickAgent;
import com.ginwave.smshelper.SmsForwardActivity.ReadFinishHandler;
import com.ginwave.smshelper.more.ListViewForAuto;
import com.ginwave.smshelper.more.SetDataSource;
import com.ginwave.smshelper.more.SettingForSMS;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SmsForwardDetailActivity extends Activity implements
		OnClickListener, OnItemClickListener{

	private TextView mMainBack;
	private TextView mMainTitle;
	private TextView mMainSetting;
	private TextView mSmsForwardDetailUserNumber;
	private TextView mSmsForwardDetailUserPoint;
	private TextView mSmsForwardDetailUserRank;
	private ListView mSmsForwardDetailItemList;
	private String mUserPoint;
	private String mUserRank;
	private String mUser;
	private String mUserLevel;
	private String[] mItems;
	private String[] mItemTypes = {"1", "3", "4", "5", "6", "7"};
	private int[] mDrawables = new int[]{R.drawable.sms_forward_bless, R.drawable.sms_forward_love, R.drawable.sms_forward_smile, 
			R.drawable.sms_forward_encourage, R.drawable.sms_forward_game, R.drawable.sms_forward_else};
	private ProgressBar mSmsForwardDetailProgress;
	private TextView mSmsForwardDetailFresh;
	private ReadFinishHandler mReadFinish;
	private int mDefinedUser = 0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sms_forward_detail);
		readFromIntent();
		setupViews();
	}

	private void readFromIntent() {
		Intent intent = this.getIntent();
		if(intent != null){
			mUser = intent.getStringExtra("user");
			mUserPoint = intent.getStringExtra("user_point");
			mUserRank = intent.getStringExtra("user_rank");
			mUserLevel = intent.getStringExtra("user_level");
		}
	}

	private void setupViews() {
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainTitle = (TextView) findViewById(R.id.mMainTitle);
		mMainSetting = (TextView) findViewById(R.id.mMainSetting);
		mSmsForwardDetailUserNumber = (TextView) findViewById(R.id.mSmsForwardDetailUserNumber);
		mSmsForwardDetailUserPoint = (TextView) findViewById(R.id.mSmsForwardDetailUserPoint);
		mSmsForwardDetailUserRank = (TextView) findViewById(R.id.mSmsForwardDetailUserRank);
		mSmsForwardDetailItemList = (ListView) findViewById(R.id.mSmsForwardDetailItemList);
		mSmsForwardDetailProgress = (ProgressBar) findViewById(R.id.mSmsForwardDetailProgress);
		mSmsForwardDetailFresh = (TextView) findViewById(R.id.mSmsForwardDetailFresh);
		mMainBack.setOnClickListener(this);
		mMainSetting.setOnClickListener(this);
		mSmsForwardDetailUserNumber.setText(this.getString(R.string.sms_forward_number) + mUser);
		mSmsForwardDetailUserPoint.setText(this.getString(R.string.sms_forward_pont) + mUserPoint);
		mSmsForwardDetailUserRank.setText(this.getString(R.string.sms_forward_rank) + mUserRank);
		mItems = this.getResources().getStringArray(
				R.array.sms_forward_detail_items);
		SmsForwardAdapter adapter = new SmsForwardAdapter();
		mSmsForwardDetailItemList.setAdapter(adapter);
		mSmsForwardDetailItemList.setOnItemClickListener(this);
		mSmsForwardDetailProgress.setVisibility(View.INVISIBLE);
		mSmsForwardDetailFresh.setOnClickListener(this);
		mReadFinish = new ReadFinishHandler();
	}

	private class SmsForwardAdapter extends BaseAdapter {

		private ViewHolder mViewHolder;
		private LayoutInflater mInflater;

		public SmsForwardAdapter() {
			// TODO Auto-generated constructor stub
			mViewHolder = new ViewHolder();
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public final class ViewHolder {
			public TextView mIndividualMessage;
			public ImageView mIndividualIcon;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mItems.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.sms_forward_detail_item, null);
				mViewHolder.mIndividualMessage = (TextView) convertView
						.findViewById(R.id.mSmsForwardDetailMessage);
				mViewHolder.mIndividualIcon = (ImageView) convertView
						.findViewById(R.id.mSmsForwardDetailIcon);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			mViewHolder.mIndividualMessage.setText(mItems[position]);
			mViewHolder.mIndividualIcon.setBackgroundResource(mDrawables[position]);
			convertView.setTag(mViewHolder);
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.equals(mMainBack)){
			this.finish();
		}
		if(v.equals(mMainSetting)){
			Intent intent = new Intent();
			intent.putExtra("level", "1");
			intent.setClass(getApplicationContext(), SmsForwardDetailSettingActivity.class);
			this.startActivity(intent);
		}
		if(v.equals(mSmsForwardDetailFresh)){
			mSmsForwardDetailProgress.setVisibility(View.VISIBLE);
			new ReadTextMessageThread().run();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("item_value", mItems[arg2]);
		intent.putExtra("item_type", mItemTypes[arg2]);
		intent.setClass(getApplicationContext(), SmsForwardDetailItemActivity.class);
		this.startActivity(intent);
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	class ReadFinishHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			mSmsForwardDetailUserNumber.setText(SmsForwardDetailActivity.this.getString(R.string.sms_forward_number) + mUser);
			mSmsForwardDetailUserPoint.setText(SmsForwardDetailActivity.this.getString(R.string.sms_forward_pont) + mUserPoint);
			mSmsForwardDetailUserRank.setText(SmsForwardDetailActivity.this.getString(R.string.sms_forward_rank) + mUserRank);
			mSmsForwardDetailProgress.setVisibility(View.INVISIBLE);
			super.handleMessage(msg);
		}
	}
	
	class ReadTextMessageThread extends Thread {
		public void run() {
			String result = SetDataSource.getPostResult(SmsForwardDetailActivity.this, SetDataSource.getUserDefinedString(mUser), "defined_user.xml");
			if(result == null){
				return ;
			}
			mDefinedUser = SetDataSource.getDefinedUserParseFromString(getApplicationContext(), "defined_user.xml");
			switch(mDefinedUser){
			case -2:
				Log.i("xiao", "parse exception");
				break;
			case -1:
				Log.i("xiao", "post error");
				break;
			case 0:
				Log.i("xiao", "00");
				mReadFinish.sendEmptyMessage(2);
				/*
				mUserPoint = getApplication
				Context().getString(R.string.sms_forward_no_point);
				mUserRank = getApplicationContext().getString(R.string.sms_forward_no_rank);
				mUserLevel = getApplicationContext().getString(R.string.sms_forward_user_undefined);
				mReadFinish.sendEmptyMessage(1);*/
				break;
			case 1:
				mReadFinish.sendEmptyMessage(2);
				/*Log.i("xiao", "11");
				SetDataSource.getPostResult(SmsForwardDetailActivity.this, SetDataSource.getUserPointsString(mUser), "user_info.xml");
				String info1[] = SetDataSource.getUserInfoParseFromString(getApplicationContext(), "user_info.xml");
				if(info1 != null){
					Log.i("xiao", "info1 != null");
					Log.i("xiao", "info[0] = " + info1[0]);
					mUserPoint = info1[0];
					mUserRank = info1[1];
					Log.i("xiao", "info[1] = " + info1[1]);
				}
				else{
					Log.i("xiao", "elseeeeeeeeeeeeeeeeee");
				}
				mUserLevel = getApplicationContext().getString(R.string.sms_forward_user_normal);
				mReadFinish.sendEmptyMessage(0);*/
				break;
			case 2:
				/*Log.i("xiao", "222");
				String info2[] = SetDataSource.getUserInfoParseFromString(getApplicationContext(), SetDataSource.getUserPointsString(mUser));
				if(info2 != null){
					mUserPoint = info2[0];
					mUserRank = info2[1];
				}
				mUserLevel = getApplicationContext().getString(R.string.sms_forward_user_high);
				mReadFinish.sendEmptyMessage(0);*/
				mReadFinish.sendEmptyMessage(2);
				break;
			}
			
		}
	}
}
