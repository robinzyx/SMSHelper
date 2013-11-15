package com.ginwave.smshelper;

import com.umeng.analytics.MobclickAgent;
import com.ginwave.smshelper.more.ListViewForAuto;
import com.ginwave.smshelper.more.SetDataSource;
import com.ginwave.smshelper.more.SettingForSMS;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AboutActivity extends Activity implements OnClickListener {

	private TextView mMainBack;
	private WebView mWebContent;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainBack.setOnClickListener(this);
		mWebContent = (WebView) findViewById(R.id.mWebContent);
		mWebContent.getSettings().setDefaultTextEncodingName("gbk");
		mWebContent.loadUrl("file:///android_asset/01_01.htm");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mMainBack)) {
			this.finish();
		}
	}

	public void share(View v) {
		/*Intent intent = new Intent(Intent.ACTION_SEND);

		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享好软件");
		intent.putExtra(Intent.EXTRA_TEXT, "向你推荐个发短信的神器，助您轻松拜年，节日无忧！");
		startActivity(Intent.createChooser(intent, getTitle()));*/
		String[] reciver = new String[] { "18298358753@139.com", "13609360641@139.com"};
		String[] mySbuject = new String[] { this.getString(R.string.feedback) };
		String myCc = "cc";
		String mybody = "";
		Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
		myIntent.setType("plain/text");
		myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
		myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);
		myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySbuject);
		myIntent.putExtra(android.content.Intent.EXTRA_TEXT, mybody);
		startActivity(Intent.createChooser(myIntent, this.getString(R.string.feedback)));
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
