package com.ginwave.smshelper.readcontacts;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Iterator;

import com.ginwave.smshelper.MultiSelectTab;
import com.ginwave.smshelper.R;
import com.ginwave.smshelper.SmsSendByGroup;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class GroupListView extends ListActivity implements OnClickListener {

	private ListView listView;
	private TextView mMainBack;
	private ArrayList<GroupInfo> mListViewData = new ArrayList<GroupInfo>();
	private GroupAdapter mAdapter;
	private ArrayList<Integer> mGroupList = new ArrayList<Integer>();
	private Button mAddBtn;
	private Button mBackBtn;
	private Button mAllBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listviewforgroup);
		mAddBtn = (Button) findViewById(R.id.btn_add);
		mBackBtn = (Button) findViewById(R.id.btn_back);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mAllBtn = (Button) findViewById(R.id.button1);
		mMainBack.setOnClickListener(this);
		mAllBtn.setText("全选");
		mAllBtn.setOnClickListener(this);
		mAddBtn.setOnClickListener(btnClick);
		mBackBtn.setOnClickListener(btnClick);
		listView = getListView();
		initAdapter();
		setListAdapter(mAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				//CheckBox cb = (CheckBox) view.findViewById(R.id.ischecked);
				LinearLayout ll = (LinearLayout) view;
				CheckBox cb = (CheckBox) ll.getChildAt(0).findViewById(
						R.id.ischecked);
				if (cb.isChecked()) {
					cb.setChecked(false);
					deleteListData(i);
					mListViewData.get(i).setChecked(false);
					mAllBtn.setText("全选");
				} else {
					cb.setChecked(true);
					mGroupList.add(mListViewData.get(i).getGroupId());
					Log.v("heiheifang", "after add item: " + mGroupList.toString());	
					mListViewData.get(i).setChecked(true);
				}
			}
		});

	}

	private void deleteListData(int i) {
		Iterator<Integer> iterator = mGroupList.iterator();
		while (iterator.hasNext()) {
			Integer e = iterator.next();
			if (e == mListViewData.get(i).getGroupId()) {
				iterator.remove();
				break;
			}
		}
	}

	private void initAdapter() {
		ContentResolver cr = getContentResolver();
		Cursor cursor = cr.query(ContactsContract.Groups.CONTENT_URI,
				new String[] { ContactsContract.Groups._ID,
						ContactsContract.Groups.TITLE }, null, null, null);
		if (cursor != null && cursor.getCount() != 0) {
			while (cursor.moveToNext()) {
				GroupInfo info = new GroupInfo();
				info.setGroupName(cursor.getString(cursor
						.getColumnIndex(ContactsContract.Groups.TITLE)));
				info.setGroupId(Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(ContactsContract.Groups._ID))));
				info.setChecked(false);
				mListViewData.add(info);
			}
			cursor.close();
		}
		mAdapter = new GroupAdapter(this, mListViewData);
	}

	private OnClickListener btnClick = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_add:
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putIntegerArrayList("groupids", mGroupList);
				Log.v("heiheifang", "send to: " + mGroupList.toString());
				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(),
						EachGroupListView.class);
				if(MultiSelectTab.NeedSendResult){
					GroupListView.this.getParent().startActivityForResult(intent, 1);
				}
				else{
					GroupListView.this.startActivity(intent);
				}
				break;

			case R.id.btn_back:
				finish();
				break;
			}
		}
	};
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mMainBack)) {
			this.finish();
		} else if (v.getId() == R.id.button1) {
			selectAll(v);
		}
	}

	public void selectAll(View v) {
		Button button = (Button) v;
		if (button.getText().toString().equals("全选")) {
			Log.v("heiheifang", "quanxuan");
			for (int i = 0; i < listView.getChildCount(); i++) {
				View view = listView.getChildAt(i);
				CheckBox cb = (CheckBox) view.findViewById(R.id.ischecked);
				cb.setChecked(true);
			}
			mGroupList.clear();
			for (int i = 0; i < mListViewData.size(); i++) {
				mListViewData.get(i).setChecked(true);
				mGroupList.add(mListViewData.get(i).getGroupId());
			}
			button.setText("取消全选");
		} else if (button.getText().toString().equals("取消全选")) {
			Log.v("heiheifang", "quxiaoquanxuan");
			for (int i = 0; i < listView.getChildCount(); i++) {
				View view = listView.getChildAt(i);
				CheckBox cb = (CheckBox) view.findViewById(R.id.ischecked);
				cb.setChecked(false);
			}
			mGroupList.clear();
			for (int i = 0; i < mListViewData.size(); i++) {
				mListViewData.get(i).setChecked(false);
			}
			button.setText("全选");
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

}
