package com.ginwave.smshelper.localsms;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import com.ginwave.smshelper.MultiSelectTab;
import com.ginwave.smshelper.R;
import com.ginwave.smshelper.SmsSendByGroup;
import com.ginwave.smshelper.more.SetDataSource;
import com.ginwave.smshelper.util.SendSuccessReceiver;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LocalSmsSendActivity extends Activity implements OnClickListener {
	private static final int CONTACT_GROUP_REQUEST_CODE = 1;
	private static final int CONTACT_REQUEST_CODE = 2;
	private static final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	private Button mSendBtn;
	private EditText mInputNum;
	private EditText mSmsContent;
	private String mNumber;
	private SendSuccessReceiver mReceiver;
	private static String xm;
	private ImageView mMultiChooseButton;
	private Bundle mBundle;
	private TextView mMainBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.localsmssendactivity);

		mReceiver = new SendSuccessReceiver();
		IntentFilter deliverFilter = new IntentFilter();
		deliverFilter.addAction(SENT_SMS_ACTION);
		this.registerReceiver(mReceiver, deliverFilter);
		mReceiver.setNotifyFlag();

		mInputNum = (EditText) findViewById(R.id.input_num);
		mInputNum.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		mSendBtn = (Button) findViewById(R.id.send);
		mSmsContent = (EditText) findViewById(R.id.content);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainBack.setOnClickListener(this);
		mMultiChooseButton = (ImageView) findViewById(R.id.mMultiChooseButton);
		mMultiChooseButton.setOnClickListener(this);

		mBundle = this.getIntent().getExtras();
		mBundle.putString("wNumberStr", mInputNum.getText().toString());
		if (mBundle != null) {
			xm = mBundle.getString("xm");
			final StringBuilder sb = new StringBuilder();
			sb.append("" + xm + "\n");
			mSmsContent.setText(sb.toString().trim());
			Log.v("XXXX", "Phone numbers3:" + mBundle.getString("phonenumber"));
			mInputNum.setText(mBundle.getString("phonenumber"));
		}

		mSendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String message = mSmsContent.getText().toString();
				mNumber = mInputNum.getText().toString();
				if (mNumber.equals("")) {
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.send_toast),
							Toast.LENGTH_SHORT).show();
					return;
				}
				String number[] = mNumber.split(",");
				ArrayList<String> nums = new ArrayList<String>();
				for (int i = 0; i < number.length; i++) {
					nums.add(number[i]);
				}
				new Thread(new MsgSentManager(getApplicationContext(), nums,
						message)).start();
				Toast.makeText(LocalSmsSendActivity.this,
						getString(R.string.send_sending), Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	// @Override
	// protected void onStart() {
	// // TODO Auto-generated method stub
	// super.onStart();
	// ActionBar actionBar = this.getActionBar();
	// actionBar.setDisplayHomeAsUpEnabled(true);
	//
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // TODO Auto-generated method stub
	// switch (item.getItemId()) {
	// case android.R.id.home:
	// Intent intent = new Intent(this, ListViewForLocalSms.class);
	// startActivity(intent);
	// return true;
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	//
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("xiao", "onActivityResult");
		String numberStr = null;
		if (data != null) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				numberStr = bundle.getString("numberStr");
				mInputNum.setText(numberStr);
			}
		}
	}

	private class MsgSentManager implements Runnable {

		private Context c;
		private ArrayList<String> list;
		private String mesgstr;

		public MsgSentManager(Context context, ArrayList<String> phonenos,
				String msgstr) {
			c = context;
			list = phonenos;
			this.mesgstr = msgstr;

		}

		public boolean sendMsgs(Context context, ArrayList<String> phonenos,
				String msgstr) {
			Long id = SetDataSource.storeStatics(context, phonenos.size(), msgstr);
			SetDataSource.mPhoneNumberList = list;
			SetDataSource.mPhoneNumberSize = list.size() - 1;
			SetDataSource.mPresentPhoneNumber = 0;
			SetDataSource.mPresentId = id;
			for (String str : phonenos) {
				sendOneMsg(context, str, msgstr);
			}
			return true;
		}
		
		public boolean sendOneMsg(Context context, String phoneno, String msgstr) {
			MobclickAgent.onEvent(context, SetDataSource.SMSCOUNT); 
			SmsManager smsManager = SmsManager.getDefault();
			Intent intent = new Intent(SENT_SMS_ACTION);
			PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
					intent, 0);
			intent.putExtra("notifyFlag", true);
			ArrayList<PendingIntent> sendPIIntent = new ArrayList<PendingIntent>();
			sendPIIntent.add(sentPI);
			ArrayList<String> list = smsManager.divideMessage(msgstr); 
			if(list != null && list.size() > 0 && phoneno != null && phoneno.length() > 0){
				smsManager.sendMultipartTextMessage(phoneno, null, list, sendPIIntent, null);
			}
			return true;

		}

		public void run() {
			// TODO Auto-generated method stub
			sendMsgs(c, list, mesgstr);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.i("xiao", "onClick....");
		if (v.equals(mMultiChooseButton)) {
			Log.i("xiao", "equals");
			Intent lIntent = new Intent();
			lIntent.putExtras(mBundle);
			lIntent.setClass(getApplicationContext(), MultiSelectTab.class);
			this.startActivityForResult(lIntent, 1);
		}
		if (v.equals(mMainBack)) {
			this.finish();
		}
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(mReceiver);

		super.onDestroy();
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
