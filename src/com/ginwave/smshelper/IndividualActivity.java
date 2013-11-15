package com.ginwave.smshelper;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.ginwave.smshelper.more.ListViewForAuto;
import com.ginwave.smshelper.more.SetDataSource;
import com.ginwave.smshelper.more.SettingForSMS;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IndividualActivity extends Activity implements OnClickListener, OnItemClickListener{

	private TextView mMainBack;
	private ListView mIndividualList;
	private IndividualAdapter mIndividualAdapter;
	private String[] mIndividualListData;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.individual);
		setupViews();
	}

	private void setupViews() {
		mIndividualList = (ListView) findViewById(R.id.mIndividualList);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainBack.setOnClickListener(this);
		mIndividualListData = this.getResources().getStringArray(R.array.individual_business);
		mIndividualAdapter = new IndividualAdapter();
		mIndividualList.setAdapter(mIndividualAdapter);
		mIndividualList.setOnItemClickListener(this);
	}

	private class IndividualAdapter extends BaseAdapter {
		
		private ViewHolder mViewHolder;
		private LayoutInflater mInflater;
		
		public IndividualAdapter() {
			// TODO Auto-generated constructor stub
			mViewHolder = new ViewHolder();
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		public final class ViewHolder{
			  public TextView  mIndividualMessage;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mIndividualListData.length;
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
			if(convertView == null){
				if(position != 0 && position != 1){
					convertView = mInflater.inflate(R.layout.list_individual_item, null);
					mViewHolder.mIndividualMessage = (TextView)convertView.findViewById(R.id.mIndividualMessage);
				}
				else{
					convertView = mInflater.inflate(R.layout.list_individual_item_special, null);
					mViewHolder.mIndividualMessage = (TextView)convertView.findViewById(R.id.mIndividualMessage);
				}
			}
			else{
				if((position != 0 || position != 1)&& convertView.getId() != R.id.mIndividualItemSpecial){
					mViewHolder = (ViewHolder)convertView.getTag();
				}
				else{
					convertView = mInflater.inflate(R.layout.list_individual_item_special, null);
					mViewHolder.mIndividualMessage = (TextView)convertView.findViewById(R.id.mIndividualMessage);
				}
			}
			mViewHolder.mIndividualMessage.setText(mIndividualListData[position]);
			convertView.setTag(mViewHolder);
			return convertView;
		} 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mMainBack)) {
			this.finish();
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent lIntent = new Intent();
		lIntent.setClass(getApplicationContext(), IndividualDetailActivity.class);
		lIntent.putExtra("individual_item", arg2);
		lIntent.putExtra("individual_name", mIndividualListData[arg2]);
		this.startActivity(lIntent);
	}

}
