package com.ginwave.smshelper;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.ginwave.smshelper.localsms.LocalSmsSendActivity;
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
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SmsForwardDetailSettingActivity extends Activity implements
		OnClickListener, OnItemClickListener{

	private TextView mMainBack;
	private TextView mMainTitle;
	private ListView mSmsForwardSettingList;
	private String[] mSmsForwardSetting;
	private String[] mSmsForwardSettingNote;
	private int[] mSmsForwardSettingFlag = {0, 0, 1, 1, 0, 0, 0, 0};
	private String[] mSmsForwardSettingNumber;
	private String[] mSmsForwardSettingAction;
	private LayoutInflater mInflater;
	private SmsForwardSettingAdapter mSmsForwardSettingAdapter;
	private String mSmsForwardSettingPresentLevel = "1";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sms_forward_setting);
		dataFromIntent();
		setupViews();
	}

	private void dataFromIntent() {
		Intent intent = this.getIntent();
		if(intent != null){
			mSmsForwardSettingPresentLevel = intent.getStringExtra("level");
		}
		if(mSmsForwardSettingPresentLevel.equals("2")){
			mSmsForwardSettingNumber = this.getResources().getStringArray(R.array.sms_forward_setting_high_number);
		}
		else{
			mSmsForwardSettingNumber = this.getResources().getStringArray(R.array.sms_forward_setting_normal_number);
		}
		mSmsForwardSettingAction = this.getResources().getStringArray(R.array.sms_forward_setting_action);
	}
	
	private void setupViews() {
		mSmsForwardSetting = this.getResources().getStringArray(R.array.sms_forward_setting);
		mSmsForwardSettingNote = this.getResources().getStringArray(R.array.sms_forward_setting_note);
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainTitle = (TextView) findViewById(R.id.mMainTitle);
		mSmsForwardSettingList = (ListView) findViewById(R.id.mSmsForwardSettingList);
		mMainBack.setOnClickListener(this);
		//mMainTitle.setText(mSmsItemValue);
		mSmsForwardSettingAdapter = new SmsForwardSettingAdapter();
		mSmsForwardSettingList.setAdapter(mSmsForwardSettingAdapter);
		mSmsForwardSettingList.setOnItemClickListener(this);
	}
	
	public void createInputValueDialog(final Context context, final String pTitle, final String pNote, final String pNumber, final String pAction) {
		View view = mInflater.inflate(R.layout.sms_forward_setting_input_value, null);
		final TextView mSmsForwardSettingNote = (TextView)view.findViewById(R.id.mSmsForwardSettingNote);
		final EditText mSmsForwardSettingInput = (EditText)view.findViewById(R.id.mSmsForwardSettingInput);
		mSmsForwardSettingNote.setText(pNote);
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle(pTitle).setCancelable(true).setPositiveButton(
			 context.getString(R.string.select_ok), new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String inputValue = mSmsForwardSettingInput.getText().toString();
					if(inputValue != null && inputValue.length() > 0){
						ArrayList<String> phones = new ArrayList<String>();
						phones.add(pNumber);
						Log.i("xiao", "pNumber = " + pNumber + " action = " + (pAction + inputValue));
						//SetDataSource.sendMessage(getApplicationContext(), phones, pAction + inputValue);
						dialog.dismiss();
					}
				}
				 
			 })
			 .setNegativeButton(context.getString(R.string.select_cancel), new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					arg0.dismiss();
				}
				 
			 }).setView(view).create();
		dialog.show();
	}
	
	public void createNoteDialog(final Context context, final String pTitle, final String pNote, final String pNumber, final String pAction) {
		View view = mInflater.inflate(R.layout.sms_forward_setting_note, null);
		final TextView mSmsForwardSettingNote = (TextView)view.findViewById(R.id.mSmsForwardSettingNote);
		mSmsForwardSettingNote.setText(pNote);
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle(pTitle).setCancelable(true).setPositiveButton(
			 context.getString(R.string.select_ok), new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					ArrayList<String> phones = new ArrayList<String>();
					phones.add(pNumber);
					Log.i("xiao", "pNumber = " + pNumber + " action = " + pAction);
					SetDataSource.sendMessage(getApplicationContext(), phones, pAction);
				}
				 
			 })
			 .setNegativeButton(context.getString(R.string.select_cancel), new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					arg0.dismiss();
				}
				 
			 }).setView(view).create();
		dialog.show();
	}
	
	private class SmsForwardSettingAdapter extends BaseAdapter {
		

		public SmsForwardSettingAdapter() {
			// TODO Auto-generated constructor stub
			
		}

		public final class ViewHolder {
			public TextView mIndividualMessage;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mSmsForwardSetting.length;
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
						R.layout.sms_forward_detail_item, null);
				viewHolder.mIndividualMessage = (TextView) convertView
						.findViewById(R.id.mSmsForwardDetailMessage);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.mIndividualMessage.setText(mSmsForwardSetting[position]);
			convertView.setTag(viewHolder);
			/*convertView = mInflater.inflate(
					R.layout.sms_forward_detail_item, null);
			mViewHolder.mIndividualMessage = (TextView) convertView
					.findViewById(R.id.mSmsForwardDetailMessage);
			mViewHolder.mIndividualMessage.setText(mSmsItemMessageList.get(position));*/
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.equals(mMainBack)){
			this.finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.i("xiao", "onItemClick");
		if(mSmsForwardSettingFlag[arg2] == 0){
			createNoteDialog(this, mSmsForwardSetting[arg2], mSmsForwardSettingNote[arg2], mSmsForwardSettingNumber[arg2], mSmsForwardSettingAction[arg2]);
		}
		else{
			createInputValueDialog(this, mSmsForwardSetting[arg2], mSmsForwardSettingNote[arg2], mSmsForwardSettingNumber[arg2], mSmsForwardSettingAction[arg2]);
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
}
