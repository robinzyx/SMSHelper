package com.ginwave.smshelper;
import com.umeng.analytics.MobclickAgent;

import com.ginwave.smshelper.localsms.SmsReader;
import com.ginwave.smshelper.readcontacts.GroupListView;
import com.ginwave.smshelper.readcontacts.GroupListViewNoTitle;
import com.ginwave.smshelper.readcontacts.MultiContactListView;
import com.ginwave.smshelper.readcontacts.MultiContactListViewNoTitle;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class MultiSelectTab extends Activity implements OnClickListener{
	
	private TabHost mTabHost;
	private Intent mIntent1;
	private Intent mIntent2;
	private TextView mMainBack;
	public static boolean NeedSendResult = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.multi_select_tab);
		mTabHost = (TabHost) findViewById(R.id.tabhost);
		mMainBack = (TextView)findViewById(R.id.mMainBack);
		mMainBack.setOnClickListener(this);
		Intent lIntent = this.getIntent();
		if(lIntent == null){
			NeedSendResult = false;
			Log.i("xiao", "falsefffffffffff");
		}
		else{
			Log.i("xiao", "trueeeeeeeeeeeeee");
			NeedSendResult = true;
		}
		LocalActivityManager mlam = new LocalActivityManager(this, false);
		mlam.dispatchCreate(savedInstanceState);
		mTabHost.setup(mlam);
		mIntent2 = new Intent(getApplicationContext(), MultiContactListViewNoTitle.class);
		mIntent1 = new Intent(getApplicationContext(), GroupListViewNoTitle.class);
		createTab(getResources().getString(R.string.group), mIntent1);
		createTab(getResources().getString(R.string.multiselect), mIntent2);
		/*mTabHost.addTab(mTabHost
				.newTabSpec("tab1")
				.setIndicator(getResources().getString(R.string.group))
				.setContent(mIntent1));

		mTabHost.addTab(mTabHost
				.newTabSpec("tab2")
				.setIndicator(getResources().getString(R.string.multiselect))
				.setContent(mIntent2));*/
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("xiao", "onDestroy..............");
		NeedSendResult = false;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("xiao", "GroupListView onActivityResult");
		this.setResult(1, data);
		this.finish();
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
