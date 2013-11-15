package com.ginwave.smshelper;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.ginwave.smshelper.more.ListViewForAuto;
import com.ginwave.smshelper.more.SetDataSource;
import com.ginwave.smshelper.more.SettingForSMS;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SmsForwardActivity extends Activity implements OnClickListener, OnItemClickListener{

	private TextView mMainBack;
	private TextView mSmsForwardLogin;
	private TextView mSmsForwardRegister;
	private EditText mSmsForwardPhone;
	private CheckBox mSmsForwardRemember;
	private RelativeLayout mSmsForwardLogining;
	private WebView mSmsForwardRewardRule;
	private ReadFinishHandler mReadFinish;
	private String mPhoneNumber;
	private int mDefinedUser = 0;
	private String mUserPoint;
	private String mUserRank;
	private String mUserLevel;
	private boolean mSmsForwardStoredNumberFlag;
	private String mSmsForwardStoredNumber;
	private boolean mSmsForwardCancelLoad = false;

	private String[] mSmsForwardDredgeAction = new String[]{"ZC", "GJ"};
	private String[] mSmsForwardDredgeNumber = new String[]{"10658585", "106585858"};
	private String[] mSmsForwardDredgeNote;
/*	private ListView mSmsForwardDredgeList;*/
	private LayoutInflater mInflater;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sms_forward);
		setupViews();
	}

	private void setupViews() {
		mInflater = this.getLayoutInflater();
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainBack.setOnClickListener(this);
		mSmsForwardLogin = (TextView) findViewById(R.id.mSmsForwardLogin);
		mSmsForwardRegister = (TextView) findViewById(R.id.mSmsForwardRegister);
		mSmsForwardPhone = (EditText) findViewById(R.id.mSmsForwardPhone);
		mSmsForwardRemember = (CheckBox) findViewById(R.id.mSmsForwardRemember);
		mSmsForwardLogining = (RelativeLayout) findViewById(R.id.mSmsForwardLogining);
		mSmsForwardRewardRule = (WebView) findViewById(R.id.mSmsForwardRewardRule);
		mSmsForwardLogin.setOnClickListener(this);
		mSmsForwardRegister.setOnClickListener(this);
		mReadFinish = new ReadFinishHandler();
		mSmsForwardRemember.setOnCheckedChangeListener(new MyCheckChangeListener());
		mSmsForwardStoredNumberFlag = SetDataSource.getSmsForwardStoredNumberFlag(getApplicationContext());
		if(mSmsForwardStoredNumberFlag){
			mSmsForwardRemember.setChecked(true);
		}
		else{
			mSmsForwardRemember.setChecked(false);
		}
		Log.i("xiao", "mSmsForwardStoredNumberFlag = " + mSmsForwardStoredNumberFlag);
		mSmsForwardStoredNumber = SetDataSource.getSmsForwardStoredNumber(getApplicationContext());
		mSmsForwardPhone.setText(mSmsForwardStoredNumber);
		mSmsForwardCancelLoad = false;
		mSmsForwardDredgeNote = this.getResources().getStringArray(R.array.sms_forward_dredge_note);
		mSmsForwardRewardRule.getSettings().setDefaultTextEncodingName("gbk");
		mSmsForwardRewardRule.loadUrl("file:///android_asset/01_02.htm");
		mSmsForwardRewardRule.setBackgroundColor(0); 
		/*mSmsForwardDredgeList = new ListView(this);
		SmsForwardDredgeAdapter smsForwardDredgeAdapter = new SmsForwardDredgeAdapter();
		mSmsForwardDredgeList.setAdapter(smsForwardDredgeAdapter);
		mSmsForwardDredgeList.setBackgroundResource(R.color.white);
		mSmsForwardDredgeList.setOnItemClickListener(this);*/
	}
	
	private class MyCheckChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			mSmsForwardStoredNumberFlag = isChecked;
			Log.i("xiao", "onCheckedChanged mSmsForwardStoredNumberFlag = " + mSmsForwardStoredNumberFlag);
		}
		
	}

	@Override
	public void onClick(View v) {
		mPhoneNumber = mSmsForwardPhone.getText().toString().trim();
		// TODO Auto-generated method stub
		SetDataSource.setSmsForwardStoredNumberFlag(getApplicationContext(), mSmsForwardStoredNumberFlag);
		if(mSmsForwardStoredNumberFlag){
			SetDataSource.setSmsForwardStoredNumber(getApplicationContext(), mPhoneNumber);
		}
		else{
			SetDataSource.setSmsForwardStoredNumber(getApplicationContext(), null);
		}
		if(v.equals(mSmsForwardLogin)){
			
			if(mPhoneNumber.length() < 11){
				Toast.makeText(getApplicationContext(), R.string.sms_forward_phone_input_error, Toast.LENGTH_LONG).show();
				return ;
			}
			else if(SetDataSource.isNetworkAvailable(getApplicationContext())){
				mSmsForwardLogining.setVisibility(View.VISIBLE);
				new Thread(new ReadDataRunnable()).start();
			}
			else{
				Toast.makeText(getApplicationContext(), R.string.sms_forward_network_note, Toast.LENGTH_LONG).show();
			}
		}
		if(v.equals(mSmsForwardRegister)){
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), SmsForwardRegisterActivity.class);
			SmsForwardActivity.this.startActivity(intent);
		}
		if(v.equals(mMainBack)){
			this.finish();
		}
	}
	
	class ReadFinishHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				if(!mSmsForwardCancelLoad){
					Intent lIntent = new Intent();
					lIntent.putExtra("user", mPhoneNumber);
					lIntent.putExtra("user_point", mUserPoint);
					lIntent.putExtra("user_rank", mUserRank);
					lIntent.putExtra("user_level", mUserLevel);
					lIntent.setClass(getApplicationContext(), SmsForwardDetailActivity.class);
					SmsForwardActivity.this.startActivity(lIntent);
				}
				break;
			case 1:
				ArrayList<String> phone = new ArrayList<String>();
				phone.add("10658585");
				SetDataSource.sendMessage(getApplicationContext(), phone, "ZC");
				if(!mSmsForwardCancelLoad){
					Intent lIntent = new Intent();
					lIntent.putExtra("user", mPhoneNumber);
					lIntent.putExtra("user_point", mUserPoint);
					lIntent.putExtra("user_rank", mUserRank);
					lIntent.putExtra("user_level", mUserLevel);
					lIntent.setClass(getApplicationContext(), SmsForwardDetailActivity.class);
					SmsForwardActivity.this.startActivity(lIntent);
				}
				break;
			case 2:
				mSmsForwardLogining.setVisibility(View.INVISIBLE);
				createDredgeDialog(SmsForwardActivity.this, SmsForwardActivity.this.getString(R.string.sms_forward_dredge));
				break;
			}
			super.handleMessage(msg);
		}
	}
	
	class ReadDataRunnable implements Runnable{
		@Override
		public void run() {
			String result = SetDataSource.getPostResult(SmsForwardActivity.this, SetDataSource.getUserDefinedString(mPhoneNumber), "defined_user.xml");
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
				Log.i("xiao", "11");
				SetDataSource.getPostResult(SmsForwardActivity.this, SetDataSource.getUserPointsString(mPhoneNumber), "user_info.xml");
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
				mReadFinish.sendEmptyMessage(0);
				break;
			case 2:
				Log.i("xiao", "222");
				SetDataSource.getPostResult(SmsForwardActivity.this, SetDataSource.getUserPointsString(mPhoneNumber), "user_info.xml");
				String info2[] = SetDataSource.getUserInfoParseFromString(getApplicationContext(), "user_info.xml");
				if(info2 != null){
					mUserPoint = info2[0];
					mUserRank = info2[1];
				}
				mUserLevel = getApplicationContext().getString(R.string.sms_forward_user_high);
				mReadFinish.sendEmptyMessage(0);
				break;
			}
		}
	}
	
	class ReadTextMessageThread extends Thread {
		public void run() {
			/*new Runnable(){
				
			}.run();*/
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mSmsForwardLogining.setVisibility(View.INVISIBLE);
		MobclickAgent.onResume(this);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mSmsForwardCancelLoad = true;
		super.onDestroy();
	}
	
	public void createDredgeDialog(final Context context, final String pTitle) {
		Log.i("xiao", "createDredgeDialog");
		View view = mInflater.inflate(R.layout.sms_forward_note_register, null);
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle(pTitle).setCancelable(true).setMessage(R.string.sms_forward_note_register)
				.setPositiveButton(R.string.sms_forward_register, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(), SmsForwardRegisterActivity.class);
						SmsForwardActivity.this.startActivity(intent);
						dialog.dismiss();
					}
					
				})
				.create();
		dialog.show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		ArrayList<String> phones = new ArrayList<String>();
		phones.add(mSmsForwardDredgeNumber[arg2]);
		Log.i("xiao", "number = " + mSmsForwardDredgeNumber[arg2]);
		Log.i("xiao", "action = " + mSmsForwardDredgeAction[arg2]);
		//SetDataSource.sendMessage(getApplicationContext(), phones, mSmsForwardDredgeAction[arg2]);
	}
	
	private class SmsForwardDredgeAdapter extends BaseAdapter {
		

		public SmsForwardDredgeAdapter() {
			// TODO Auto-generated constructor stub
			
		}

		public final class ViewHolder {
			public TextView mSmsForwardDredgeNoteView;
			public TextView mSmsForwardDredgeActionView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mSmsForwardDredgeNote.length;
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
			ViewHolder viewHolder = new ViewHolder();
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.sms_forward_dredge, null);
				viewHolder.mSmsForwardDredgeNoteView = (TextView) convertView
						.findViewById(R.id.mSmsForwardDredgeNote);
				viewHolder.mSmsForwardDredgeActionView = (TextView) convertView
						.findViewById(R.id.mSmsForwardDredgeAction);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.mSmsForwardDredgeNoteView.setText(mSmsForwardDredgeNote[position]);
			convertView.setTag(viewHolder);
			/*convertView = mInflater.inflate(
					R.layout.sms_forward_detail_item, null);
			mViewHolder.mIndividualMessage = (TextView) convertView
					.findViewById(R.id.mSmsForwardDetailMessage);
			mViewHolder.mIndividualMessage.setText(mSmsItemMessageList.get(position));*/
			return convertView;
		}
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
