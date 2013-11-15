package com.ginwave.smshelper.more;
import com.umeng.analytics.MobclickAgent;

import com.ginwave.smshelper.R;
import com.ginwave.smshelper.R.layout;
import com.ginwave.smshelper.R.string;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SettingForSMS extends PreferenceActivity implements
		Preference.OnPreferenceChangeListener {

	private CheckBoxPreference mSetAutoControll;
	public static Preference mSetAutoMessage;
	private Preference mSetAbout;
	private Preference mSetShare;
	private EditText mEditText;
	private String tag = "xiao";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.sms_setting);

		mSetAutoControll = (CheckBoxPreference) findPreference("set_auto_controll");
		mSetAutoMessage = (Preference) findPreference("set_auto_message");
		mSetAbout = (Preference) findPreference("set_about");
		mSetShare = (Preference) findPreference("set_share");

		mSetAutoControll.setOnPreferenceChangeListener(this);
		mSetAutoMessage.setSummary(SetDataSource.mAutoReplyMessage);

	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		if (preference.getKey().equals("set_auto_message")) {
			// Intent lIntent = new Intent();
			// lIntent.setClass(getApplicationContext(), ListViewForAuto.class);
			// this.getParent().startActivityForResult(lIntent, 1);
			showMultiChoiceDialog();
		}
		if (preference.getKey().equals("set_about")) {
			AboutUsDialogBuilder.create(this).show();
		}
		if (preference.getKey().equals("set_share")) {
			String lTitle = this.getString(R.string.set_share);
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, lTitle);
			intent.putExtra(Intent.EXTRA_TEXT,
					this.getString(R.string.set_share_content));
			this.startActivity(Intent.createChooser(intent, lTitle));
		}
		return false;
	}

	public static class AboutUsDialogBuilder {
		public static AlertDialog create(Context context) {

			String aboutTitle = context.getString(R.string.set_about);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout localLinear = (LinearLayout) inflater.inflate(
					R.layout.set_about_us, null);
			return new AlertDialog.Builder(context).setTitle(aboutTitle)
					.setCancelable(true)
					.setPositiveButton(context.getString(R.string.ok), null)
					.setView(localLinear).create();
		}
	}

	private void showMultiChoiceDialog() {
		AlertDialog.Builder temp_builder = new AlertDialog.Builder(
				SettingForSMS.this);
		temp_builder.setTitle(getResources().getString(
				R.string.multichoicedialogtitle));
		temp_builder.setItems(
				new String[] {
						getResources().getString(
								R.string.multichoicedialogitem1),
						getResources().getString(
								R.string.multichoicedialogitem2),
						getResources().getString(
								R.string.multichoicedialogitem3),
						getResources().getString(
								R.string.multichoicedialogitem4) },
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							SetDataSource.mAutoReplyMessage = getResources()
									.getString(R.string.multichoicedialogitem1);
							mSetAutoMessage
									.setSummary(SetDataSource.mAutoReplyMessage);
							SetDataSource.setAutoReplayMessage(
									SettingForSMS.this,
									SetDataSource.mAutoReplyMessage);
							break;
						case 1:
							SetDataSource.mAutoReplyMessage = getResources()
									.getString(R.string.multichoicedialogitem2);
							mSetAutoMessage
									.setSummary(SetDataSource.mAutoReplyMessage);
							SetDataSource.setAutoReplayMessage(
									SettingForSMS.this,
									SetDataSource.mAutoReplyMessage);
							break;
						case 2:
							SetDataSource.mAutoReplyMessage = getResources()
									.getString(R.string.multichoicedialogitem3);
							mSetAutoMessage
									.setSummary(SetDataSource.mAutoReplyMessage);
							SetDataSource.setAutoReplayMessage(
									SettingForSMS.this,
									SetDataSource.mAutoReplyMessage);
							break;
						case 3:
							Intent lIntent = new Intent();
							lIntent.setClass(getApplicationContext(),
									ListViewForAuto.class);
							SettingForSMS.this.getParent()
									.startActivityForResult(lIntent, 1);
							break;
						}
					}
				});
		mEditText = new EditText(getApplicationContext());
		mEditText.setHint(R.string.multichoicedialogitem6);
		mEditText.setTextColor(Color.BLACK);
		temp_builder
				.setView(mEditText)
				.setPositiveButton(R.string.exitdialog_ok,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (mEditText.getText().toString().equals("")) {
									return;
								}
								SetDataSource.mAutoReplyMessage = mEditText
										.getText().toString();
								mSetAutoMessage
										.setSummary(SetDataSource.mAutoReplyMessage);
								SetDataSource.setAutoReplayMessage(
										SettingForSMS.this,
										SetDataSource.mAutoReplyMessage);
							}
						})
				.setNegativeButton(R.string.exitdialog_cancel,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								return;
							}
						});
		temp_builder.setCancelable(true);
		temp_builder.create();
		temp_builder.show();
	}


	@Override
	public boolean onPreferenceChange(Preference arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (arg0.getKey().equals("set_auto_controll")) {
			SetDataSource.mSetAutoBegin = Boolean.parseBoolean(arg1.toString());
			Log.i(tag, "boolean flag = " + SetDataSource.mSetAutoBegin);
			// Log.v(TAG, newValue.toString());
			SetDataSource.setAutoReplyFlag(this, SetDataSource.mSetAutoBegin);
		}
		return true;
	}

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SettingForSMS.this);
			builder.setTitle(R.string.exitdialog_title);
			builder.setMessage(R.string.exitdialog_content);
			builder.setPositiveButton(R.string.exitdialog_ok,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							SettingForSMS.this.finish();
						}
					});
			builder.setNegativeButton(R.string.exitdialog_cancel,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			builder.show();
		}
		return super.onKeyDown(keyCode, event);
	}*/

	public static class MessageChangeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			mSetAutoMessage.setSummary(SetDataSource.mAutoReplyMessage);
		}

	}
public void onResume()
{
super.onResume();
MobclickAgent.onResume(this);
}
public void onPause()
{
super.onPause();
MobclickAgent.onPause(this);
}

}
