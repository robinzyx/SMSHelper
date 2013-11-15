package com.ginwave.smshelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ginwave.smshelper.localsms.SmsOnePersonReader;
import com.ginwave.smshelper.more.SetDataSource;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SubSms extends ListActivity implements View.OnClickListener {

	private ListView mListView;
	ArrayAdapter<String> mArrayAdapter;
	SimpleAdapter mAdapter;
	List<String> mStringList = new ArrayList<String>();;
	String[] mSubsmsValue;
	String[] mSubsmsText;
	String[] mSubsmsTextInfo;
	private TextView mMainBack;
	private BroadcastReceiver mBroadcastReceiver;
	private int mPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subsms);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainBack.setOnClickListener(this);
		mListView = getListView();
		// mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
		// R.layout.simple_list_item_1);
		Resources res = getResources();
		mSubsmsText = res.getStringArray(R.array.subsms_text);
		mSubsmsTextInfo = res.getStringArray(R.array.subsms_text_info);

		for (String s : mSubsmsText) {
			mStringList.add(s);
			// mArrayAdapter.add(s);
		}
		SimpleAdapter mAdapter = new SimpleAdapter(getApplicationContext(),
				getData(), R.layout.simple_list_item_1, new String[] { "text",
						"img" }, new int[] { R.id.tv1, R.id.iv1 });

		mSubsmsValue = res.getStringArray(R.array.subsms_value);
		// mSubsmsValue = res.getStringArray(R.array.subsms_value_ct);

		mListView.setAdapter(mAdapter);
		mBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context _context, Intent _intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(SubSms.this, "短信发送成功", Toast.LENGTH_SHORT)
							.show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				case SmsManager.RESULT_ERROR_RADIO_OFF:
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(SubSms.this, "短信发送失败", Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		};
		registerReceiver(mBroadcastReceiver,
				new IntentFilter("SENT_SMS_ACTION"));
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		int n=0;
		for (String s : mSubsmsText) {
			
			map = new HashMap<String, Object>();
			map.put("text", s);
			if (n == 0|| n == 1)
				map.put("img", R.drawable.recommend);
			// mArrayAdapter.add(s);
			list.add(map);
			n++;
		}
		return list;

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Bundle bundle = new Bundle();
		bundle.putInt("id", position);
		showDialog(position);
		mPosition = position;
		Log.i("XXX",
				"item click " + position + " " + Arrays.toString(mSubsmsValue));

	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		String s = mSubsmsValue[id];
		Dialog dialog = new AlertDialog.Builder(SubSms.this).setTitle("短信包")
				.setMessage(mSubsmsTextInfo[id])
				.setPositiveButton("确定", new DialogOnClickListener(s))
				.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).create();
		return dialog;

	}

	private class DialogOnClickListener implements OnClickListener {
		private String string;

		public DialogOnClickListener(String s) {
			this.string = s;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (mPosition) {
			case 0:
				MobclickAgent.onEvent(SubSms.this, SetDataSource.EXCLUSIVE_SMS);
				break;
			case 1:
				MobclickAgent.onEvent(SubSms.this, SetDataSource.EXCLUSIVE_MMS);
				break;
			case 2:
				MobclickAgent.onEvent(SubSms.this, SetDataSource.SMSTWO);
				break;
			case 3:
				MobclickAgent.onEvent(SubSms.this, SetDataSource.SMSFIVE);
				break;
			case 4:
				MobclickAgent.onEvent(SubSms.this, SetDataSource.SMSTEN);
				break;
			case 5:
				MobclickAgent.onEvent(SubSms.this, SetDataSource.MMSTHREE);
				break;
			case 6:
				MobclickAgent.onEvent(SubSms.this, SetDataSource.MMSFIVE);
				break;
			case 7:
				MobclickAgent.onEvent(SubSms.this, SetDataSource.MMSEIGHT);
				break;
			}
			SmsManager smsManager = SmsManager.getDefault();
			String SENT_SMS_ACTION = "SENT_SMS_ACTION";
			Intent sentIntent = new Intent(SENT_SMS_ACTION);
			PendingIntent sentPI = PendingIntent.getBroadcast(SubSms.this, 0,
					sentIntent, 0);
			
			smsManager.sendTextMessage("10086", null, string, sentPI, null);
			Log.i("XXX", string);

		}

	}

	public void finish(View v) {
		this.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mMainBack)) {
			this.finish();
		}
	}

}
