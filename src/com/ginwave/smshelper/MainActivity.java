package com.ginwave.smshelper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;

import com.ginwave.smshelper.localsms.SmsReader;
import com.ginwave.smshelper.more.SetDataSource;
import com.ginwave.smshelper.more.SettingForSMS;
import com.ginwave.smshelper.pojos.Holiday;
import com.ginwave.smshelper.pojos.Mms;
import com.ginwave.smshelper.pojos.Sms;
import com.ginwave.smshelper.provider.Provider;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener{

	private TabHost mTabHost;
	private ImageView mMainMore;
	
	private static final String TAG = "MainActivity";
	private static String url = "http://219.246.178.120:8080/android/initgetdata.do?method=execute";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
	
		
		mTabHost = (TabHost) findViewById(R.id.tabhost);
		mMainMore = (ImageView)findViewById(R.id.mMainMore);
		mMainMore.setOnClickListener(this);
		LocalActivityManager mlam = new LocalActivityManager(this, false);
		mlam.dispatchCreate(savedInstanceState);
		mTabHost.setup(mlam);

		/*mTabHost.addTab(mTabHost
				.newTabSpec("tab1")
				.setIndicator(getResources().getString(R.string.tab1))
				.setContent(
						new Intent(getApplicationContext(), SmsReader.class)));*/
		createTab(getResources().getString(R.string.tab1), new Intent(getApplicationContext(), SmsReader.class));
		createTab(getResources().getString(R.string.tab2), new Intent(getApplicationContext(), HolidayGrid.class));
		/*mTabHost.addTab(mTabHost
				.newTabSpec("tab2")
				.setIndicator(getResources().getString(R.string.tab2))
				.setContent(
						new Intent(getApplicationContext(),
								HolidayGrid.class)));*/

		/*mTabHost.addTab(mTabHost
				.newTabSpec("tab3")
				.setIndicator(getResources().getString(R.string.tab3))
				.setContent(
						new Intent(getApplicationContext(), SettingForSMS.class)));*/
		SetDataSource.mSetAutoBegin = SetDataSource
				.getAutoReplyFlag(getApplicationContext());
		SetDataSource.mAutoReplyMessage = SetDataSource
				.getAutoReplyMessage(getApplicationContext());
	}
	
	
	
	
	private void createTab(String text ,Intent intent){          
		mTabHost.addTab(mTabHost.newTabSpec(text).setIndicator(createTabView(text)).setContent(intent));
    }

    private View createTabView(String text) {
            View view = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
            TextView tv = (TextView) view.findViewById(R.id.tv_tab);
            tv.setText(text);
            return view;
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		SetDataSource.mAutoReplyMessage = data.getStringExtra("value");
		SetDataSource.setAutoReplayMessage(getApplicationContext(),
				SetDataSource.mAutoReplyMessage);
		Intent lIntent = new Intent("com.ginwave.message_changed");
		this.sendBroadcast(lIntent);
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.equals(mMainMore)){
			Intent lIntent = new Intent();
			lIntent.setClass(getApplicationContext(), SettingForSMS.class);
			this.startActivity(lIntent);
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
