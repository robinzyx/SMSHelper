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

public class FlowActivity extends ListActivity implements View.OnClickListener {

	private ListView mListView;
	ArrayAdapter<String> mArrayAdapter;
	SimpleAdapter mAdapter;
	List<String> mStringList = new ArrayList<String>();;
	String[] mFlowsmsValue;
	String[] mFlowsmsText;
	String[] mFlowsmsTextInfo;
	private TextView mMainBack;
	private BroadcastReceiver mBroadcastReceiver;
	private int mPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flow);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainBack.setOnClickListener(this);
		mListView = getListView();
		// mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
		// R.layout.simple_list_item_1);
		Resources res = getResources();
		mFlowsmsText = res.getStringArray(R.array.flowsms_text);
		mFlowsmsTextInfo = res.getStringArray(R.array.flowsms_text_info);

		for (String s : mFlowsmsText) {
			mStringList.add(s);
			// mArrayAdapter.add(s);
		}
		SimpleAdapter mAdapter = new SimpleAdapter(getApplicationContext(),
				getData(), R.layout.simple_list_item_1, new String[] { "text",
						"img" }, new int[] { R.id.tv1, R.id.iv1 });

		mFlowsmsValue = res.getStringArray(R.array.flowsms_value);
		// mSubsmsValue = res.getStringArray(R.array.subsms_value_ct);

		mListView.setAdapter(mAdapter);
		mBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context _context, Intent _intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(FlowActivity.this, "短信发送成功", Toast.LENGTH_SHORT)
							.show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				case SmsManager.RESULT_ERROR_RADIO_OFF:
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(FlowActivity.this, "短信发送失败", Toast.LENGTH_SHORT)
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
		for (String s : mFlowsmsText) {
			
			map = new HashMap<String, Object>();
			map.put("text", s);
			/*if (n == 0|| n == 1)
				map.put("img", R.drawable.recommend);*/
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
				"item click " + position + " " + Arrays.toString(mFlowsmsValue));

	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		String s = mFlowsmsValue[id];
		Dialog dialog = new AlertDialog.Builder(FlowActivity.this).setTitle("短信包")
				.setMessage(mFlowsmsTextInfo[id])
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
				MobclickAgent.onEvent(FlowActivity.this, SetDataSource.FLOWFIVE);
				break;
			case 1:
				MobclickAgent.onEvent(FlowActivity.this, SetDataSource.FLOWTEN);
				break;
			case 2:
				MobclickAgent.onEvent(FlowActivity.this, SetDataSource.FlOWTWENTY);
				break;
			case 3:
				MobclickAgent.onEvent(FlowActivity.this, SetDataSource.FLOWTHIRTY);
				break;
			case 4:
				MobclickAgent.onEvent(FlowActivity.this, SetDataSource.FLOWFIFTY);
				break;
			case 5:
				MobclickAgent.onEvent(FlowActivity.this, SetDataSource.FLOWHUNDRED);
				break;
			case 6:
				MobclickAgent.onEvent(FlowActivity.this, SetDataSource.FLOWTWOHUNDRED);
				break;
			case 7:
				MobclickAgent.onEvent(FlowActivity.this, SetDataSource.OIL_FLOW);
				break;
			}
			SmsManager smsManager = SmsManager.getDefault();
			String SENT_SMS_ACTION = "SENT_SMS_ACTION";
			Intent sentIntent = new Intent(SENT_SMS_ACTION);
			PendingIntent sentPI = PendingIntent.getBroadcast(FlowActivity.this, 0,
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
