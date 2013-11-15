package com.ginwave.smshelper;

import java.util.ArrayList;

import com.ginwave.smshelper.more.SetDataSource;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

public class SmsForwardRegisterActivity extends Activity implements OnClickListener{

	private TextView mMainBack;
	//private WebView mSmsForwardRegisterNote;
	private TextView mSmsForwardRegisterNormal;
	private TextView mSmsForwardRegisterHigh;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sms_forward_register);
		setupViews();
	}

	private void setupViews() {
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		//mSmsForwardRegisterNote = (WebView) findViewById(R.id.mSmsForwardRegisterNote);
		mSmsForwardRegisterNormal = (TextView) findViewById(R.id.mSmsForwardRegisterNormal);
		mSmsForwardRegisterHigh = (TextView) findViewById(R.id.mSmsForwardRegisterHigh);
		//mSmsForwardRegisterNote.getSettings().setDefaultTextEncodingName("gbk");
		//mSmsForwardRegisterNote.loadUrl("file:///android_asset/01_02.htm");
		mSmsForwardRegisterNormal.setOnClickListener(this);
		mSmsForwardRegisterHigh.setOnClickListener(this);
		mMainBack.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		ArrayList<String> phones = new ArrayList<String>();
		if(v.equals(mSmsForwardRegisterNormal)){
			phones.add("10658585");
			SetDataSource.sendMessage(getApplicationContext(), phones, "ZC");
		}
		if(v.equals(mSmsForwardRegisterHigh)){
			phones.add("106585858");
			SetDataSource.sendMessage(getApplicationContext(), phones, "GJ");
		}
		if(v.equals(mMainBack)){
			this.finish();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		MobclickAgent.onResume(this);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
