package com.ginwave.smshelper;

import com.umeng.analytics.MobclickAgent;

import com.ginwave.smshelper.more.ListViewForAuto;
import com.ginwave.smshelper.more.SetDataSource;
import com.ginwave.smshelper.more.SettingForSMS;
import com.ginwave.smshelper.more.YearMonthObject;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AutoReplyActivity extends Activity implements OnClickListener,
		DialogInterface.OnClickListener {

	private EditText mAutoReplyMessage;
	private EditText mAutoReplyTime;
	private CheckBox mAutoReplySwitch;
	private TextView mAutoReplyChoose;
	// private TextView mAutoReplyBeginTime;
	private TextView mMainBack;
	private RelativeLayout mAutoReplyContentLayout;
	private TextView mAutoReplyKeyWords;
	private TextView mAutoReplyKey;
	private CheckBox mAutoReplyByTime;
	private TextView mAutoReplyByTimeBeginNote;
	private TextView mAutoReplyByTimeBeginTime;
	private TextView mAutoReplyByTimeEndNote;
	private TextView mAutoReplyByTimeEndTime;
	private LayoutInflater mInflater;
	private boolean mIsSetBeginTime;
	private DatePicker mDatePicker;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.autoreply);
		mInflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"share", MODE_PRIVATE);

		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		Editor editor = sharedPreferences.edit();
		if (isFirstRun) {
			Log.d("debug", "第一次运行");

			final WindowManager mWm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			LayoutInflater inflater = LayoutInflater.from(this);
			final View view = inflater.inflate(R.layout.dialog_custom, null);

			WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
			mParams.alpha = 0.6f;
			mWm.addView(view, mParams);
			Button button = (Button) view.findViewById(R.id.button);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mWm.removeView(view);
				}
			});

			editor.putBoolean("isFirstRun", false);
			editor.commit();
		} else {
			Log.d("debug", "不是第一次运行");
		}
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mAutoReplyMessage = (EditText) findViewById(R.id.mAutoReplyMessage);
		mAutoReplyTime = (EditText) findViewById(R.id.AutoReplyTimeEt);
		mAutoReplySwitch = (CheckBox) findViewById(R.id.mAutoReplySwitch);
		mAutoReplyChoose = (TextView) findViewById(R.id.mAutoReplyChoose);
		mAutoReplyKeyWords = (TextView) findViewById(R.id.mAutoReplyKeyWords);
		mAutoReplyKey = (TextView) findViewById(R.id.mAutoReplyKey);
		mAutoReplyByTime = (CheckBox) findViewById(R.id.mAutoReplyByTime);
		mAutoReplyByTimeBeginNote = (TextView) findViewById(R.id.mAutoReplyByTimeBeginNote);
		mAutoReplyByTimeBeginTime = (TextView) findViewById(R.id.mAutoReplyByTimeBeginTime);
		mAutoReplyByTimeEndNote = (TextView) findViewById(R.id.mAutoReplyByTimeEndNote);
		mAutoReplyByTimeEndTime = (TextView) findViewById(R.id.mAutoReplyByTimeEndTime);
		mAutoReplyByTime
				.setOnCheckedChangeListener(new AutoReplyByTimeSwitchListener());
		mAutoReplyByTimeBeginNote.setOnClickListener(this);
		mAutoReplyByTimeEndNote.setOnClickListener(this);
		// mAutoReplyBeginTime =
		// (TextView)findViewById(R.id.mAutoReplyBeginTime);
		mAutoReplyContentLayout = (RelativeLayout) findViewById(R.id.mAutoReplyContentLayout);
		mAutoReplySwitch
				.setOnCheckedChangeListener(new AutoReplySwitchListener());
		mAutoReplyChoose.setOnClickListener(this);
		mMainBack.setOnClickListener(this);
		initContent();
	}

	private void initContent() {
		if (SetDataSource.getAutoReplyFlag(getApplicationContext())) {
			mAutoReplySwitch.setChecked(true);
			mAutoReplyMessage.setClickable(true);
			mAutoReplyMessage.setEnabled(true);
			mAutoReplyTime.setClickable(true);
			mAutoReplyTime.setEnabled(true);
			mAutoReplyContentLayout.setEnabled(true);
			mAutoReplyKeyWords.setEnabled(true);
			mAutoReplyKey.setEnabled(true);
			mAutoReplyByTime.setEnabled(true);
			mAutoReplyByTime.setClickable(true);

			initAutoReplyByTimeLayout(true);
			// mAutoReplyBeginTime.setText(getReplyBeginTimeText());
		} else {
			mAutoReplySwitch.setChecked(false);
			mAutoReplyMessage.setClickable(false);
			mAutoReplyMessage.setEnabled(false);
			mAutoReplyTime.setClickable(false);
			mAutoReplyTime.setEnabled(false);
			mAutoReplyContentLayout.setEnabled(false);
			mAutoReplyKeyWords.setEnabled(false);
			mAutoReplyKey.setEnabled(false);
			mAutoReplyByTime.setEnabled(true);
			mAutoReplyByTime.setClickable(false);
			initAutoReplyByTimeLayout(false);
			// mAutoReplyBeginTime.setText(R.string.auto_reply_disabled);
		}
		mAutoReplyMessage.setText(SetDataSource.mAutoReplyMessage);
		mAutoReplyTime.setText(SetDataSource
				.getAutoReplyTime(getApplicationContext()));
	}

	private void initAutoReplyByTimeLayout(boolean pFlag) {
		if (pFlag) {
			if (SetDataSource.getAutoReplyByTimeFlag(getApplicationContext())) {
				mAutoReplyByTime.setChecked(true);
				mAutoReplyByTimeBeginNote.setEnabled(true);
				mAutoReplyByTimeBeginNote.setClickable(true);
				mAutoReplyByTimeBeginTime.setEnabled(true);
				mAutoReplyByTimeEndNote.setEnabled(true);
				mAutoReplyByTimeEndNote.setClickable(true);
				mAutoReplyByTimeEndTime.setEnabled(true);
				String beginTime = getStoredTime(true);
				if (beginTime == null) {
					mAutoReplyByTimeBeginTime
							.setText(R.string.auto_reply_by_time_no);
				} else {
					mAutoReplyByTimeBeginTime.setText(beginTime);
				}
				String endTime = getStoredTime(false);
				if (endTime == null) {
					mAutoReplyByTimeEndTime
							.setText(R.string.auto_reply_by_time_no);
				} else {
					mAutoReplyByTimeEndTime.setText(endTime);
				}
			} else {
				mAutoReplyByTime.setChecked(false);
				mAutoReplyByTimeBeginNote.setEnabled(false);
				mAutoReplyByTimeBeginTime.setEnabled(false);
				mAutoReplyByTimeBeginNote.setClickable(false);
				mAutoReplyByTimeEndNote.setEnabled(false);
				mAutoReplyByTimeEndNote.setClickable(false);
				mAutoReplyByTimeEndTime.setEnabled(false);
			}
		} else {
			mAutoReplyByTime.setChecked(false);
			mAutoReplyByTimeBeginNote.setEnabled(false);
			mAutoReplyByTimeBeginTime.setEnabled(false);
			mAutoReplyByTimeBeginNote.setClickable(false);
			mAutoReplyByTimeEndNote.setEnabled(false);
			mAutoReplyByTimeEndNote.setClickable(false);
			mAutoReplyByTimeEndTime.setEnabled(false);
		}
	}

	private String getStoredTime(boolean pIsBeginTime) {
		String timeText = null;
		int value;
		if (pIsBeginTime) {
			value = SetDataSource
					.getAutoReplySettedTime(getApplicationContext(),
							SetDataSource.AUTOREPLY_BEGIN_YEAR);
			if (value < 0) {
				return timeText;
			} else {
				timeText = ""
						+ SetDataSource.getAutoReplySettedTime(
								getApplicationContext(),
								SetDataSource.AUTOREPLY_BEGIN_YEAR)
						+ this.getString(R.string.year)
						+ SetDataSource.getAutoReplySettedTime(
								getApplicationContext(),
								SetDataSource.AUTOREPLY_BEGIN_MONTH)
						+ this.getString(R.string.month)
						+ SetDataSource.getAutoReplySettedTime(
								getApplicationContext(),
								SetDataSource.AUTOREPLY_BEGIN_DAY)
						+ this.getString(R.string.day);
				return timeText;
			}
		} else {
			value = SetDataSource.getAutoReplySettedTime(
					getApplicationContext(), SetDataSource.AUTOREPLY_END_YEAR);
			if (value < 0) {
				return timeText;
			} else {
				timeText = ""
						+ SetDataSource.getAutoReplySettedTime(
								getApplicationContext(),
								SetDataSource.AUTOREPLY_END_YEAR)
						+ this.getString(R.string.year)
						+ SetDataSource.getAutoReplySettedTime(
								getApplicationContext(),
								SetDataSource.AUTOREPLY_END_MONTH)
						+ this.getString(R.string.month)
						+ SetDataSource.getAutoReplySettedTime(
								getApplicationContext(),
								SetDataSource.AUTOREPLY_END_DAY)
						+ this.getString(R.string.day);
				return timeText;
			}
		}
	}

	private String getPresentTimeText() {
		YearMonthObject object = SetDataSource.getPresentYearMonth();
		return "" + object.mYear + this.getString(R.string.year)
				+ (object.mMonth + 1) + this.getString(R.string.month)
				+ object.mDay + this.getString(R.string.day);
	}

	/*
	 * private String getReplyBeginTimeText(){ String storedValue =
	 * SetDataSource.getAutoBeginTime(getApplicationContext()); String
	 * presentValue = getPresentTimeText(); if(storedValue != null){ return
	 * presentValue + this.getString(R.string.seperator) + storedValue; } return
	 * presentValue + this.getString(R.string.seperator) + presentValue; }
	 */

	public class AutoReplySwitchListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			SetDataSource.setAutoReplyFlag(getApplicationContext(), isChecked);
			initContent();
		}

	}

	public class AutoReplyByTimeSwitchListener implements
			OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			SetDataSource.setAutoReplyByTimeFlag(getApplicationContext(),
					isChecked);
			initContent();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mAutoReplyChoose)) {
			// showMultiChoiceDialog();
			Intent lIntent = new Intent();
			lIntent.setClass(getApplicationContext(),
					HolidayGridChooseSMS.class);
			AutoReplyActivity.this.startActivityForResult(lIntent, 0);
		}
		if (v.equals(mMainBack)) {
			this.finish();
		}
		if (v.equals(mAutoReplyByTimeBeginNote)) {
			mIsSetBeginTime = true;
			showDatePickDialog();
		}
		if (v.equals(mAutoReplyByTimeEndNote)) {
			mIsSetBeginTime = false;
			showDatePickDialog();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			SetDataSource.mAutoReplyMessage = data.getStringExtra("content");
			SetDataSource.setAutoReplayMessage(getApplicationContext(),
					SetDataSource.mAutoReplyMessage);
			// SetDataSource.setAutoBeginTime(getApplicationContext(),
			// getPresentTimeText());
			initContent();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		SetDataSource.mAutoReplyMessage = mAutoReplyMessage.getText()
				.toString();
		SetDataSource.setAutoReplayMessage(getApplicationContext(),
				SetDataSource.mAutoReplyMessage);

		SetDataSource.setAutoReplayTime(getApplicationContext(), mAutoReplyTime
				.getText().toString());
		// SetDataSource.setAutoBeginTime(getApplicationContext(),
		// getPresentTimeText());

		super.onDestroy();
	}

	private void showDatePickDialog() {
		String title;
		if (mIsSetBeginTime) {
			title = getString(R.string.auto_reply_by_time_begin);
		} else {
			title = getString(R.string.auto_reply_by_time_end);
		}
		View view = mInflater.inflate(R.layout.auto_reply_choose_time_dialog,
				null);
		mDatePicker = (DatePicker) view.findViewById(R.id.mAutoReplyDatePicker);
		AlertDialog.Builder temp_builder = new AlertDialog.Builder(this);
		temp_builder.setTitle(title);
		temp_builder.setView(view);
		temp_builder.setPositiveButton(R.string.select_ok, this);
		temp_builder.setNegativeButton(R.string.select_cancel, this);
		temp_builder.setCancelable(true);
		temp_builder.create();
		temp_builder.show();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		String presentValue;
		if (which == DialogInterface.BUTTON_POSITIVE) {
			if (mIsSetBeginTime) {
				presentValue = "" + mDatePicker.getYear()
						+ getString(R.string.year)
						+ (mDatePicker.getMonth() + 1)
						+ getString(R.string.month)
						+ mDatePicker.getDayOfMonth() + getString(R.string.day);
				Log.i("xiao", "presentValue = " + presentValue);
				mAutoReplyByTimeBeginTime.setText(presentValue);
				SetDataSource.setAutoReplySettedTime(getApplicationContext(),
						SetDataSource.AUTOREPLY_BEGIN_YEAR,
						mDatePicker.getYear());
				SetDataSource.setAutoReplySettedTime(getApplicationContext(),
						SetDataSource.AUTOREPLY_BEGIN_MONTH,
						(mDatePicker.getMonth() + 1));
				SetDataSource.setAutoReplySettedTime(getApplicationContext(),
						SetDataSource.AUTOREPLY_BEGIN_DAY,
						mDatePicker.getDayOfMonth());
			} else {
				presentValue = "" + mDatePicker.getYear()
						+ getString(R.string.year)
						+ (mDatePicker.getMonth() + 1)
						+ getString(R.string.month)
						+ mDatePicker.getDayOfMonth() + getString(R.string.day);
				mAutoReplyByTimeEndTime.setText(presentValue);
				SetDataSource
						.setAutoReplySettedTime(getApplicationContext(),
								SetDataSource.AUTOREPLY_END_YEAR,
								mDatePicker.getYear());
				SetDataSource.setAutoReplySettedTime(getApplicationContext(),
						SetDataSource.AUTOREPLY_END_MONTH,
						(mDatePicker.getMonth() + 1));
				SetDataSource.setAutoReplySettedTime(getApplicationContext(),
						SetDataSource.AUTOREPLY_END_DAY,
						mDatePicker.getDayOfMonth());
			}
		}
	}

}
