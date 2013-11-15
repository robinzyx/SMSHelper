package com.ginwave.smshelper.readcontacts;

import com.umeng.analytics.MobclickAgent;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.ginwave.smshelper.MultiSelectTab;
import com.ginwave.smshelper.R;
import com.ginwave.smshelper.SmsSendByGroup;
import com.ginwave.smshelper.readcontacts.MultiContactListView.Mycomparator;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class EachGroupListView extends Activity implements OnClickListener {

	private ListView mContactList;
	private TextView mEmptyTextView;
	private Button mAddBtn;
	private Button mBackBtn;
	private Cursor mCursor = null;
	private EachGroupAdapter mContactAdapter;
	private ArrayList<EachGroupInfo> mListViewData = new ArrayList<EachGroupInfo>();
	private String mNumList = "";
	protected String[] autoContact = null;
	protected String[] wNumStr = null;
	private static final int DIALOG_KEY = 0;
	private TextView mMainBack;
	private Button mAllBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listviewforeachgroup);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mContactList = (ListView) findViewById(R.id.list);
		mEmptyTextView = (TextView) findViewById(R.id.empty);
		mAddBtn = (Button) findViewById(R.id.btn_add);
		mBackBtn = (Button) findViewById(R.id.btn_back);
		mMainBack.setOnClickListener(this);
		mEmptyTextView.setVisibility(View.GONE);
		mAllBtn = (Button) findViewById(R.id.button1);
		mAllBtn.setText("全选");
		mAllBtn.setOnClickListener(this);

		Intent intent = getIntent();
		ArrayList<Integer> grouplist = intent
				.getIntegerArrayListExtra("groupids");
		Log.v("heiheifang", "from intent groupid is " + grouplist.toString());
		new GetContactTask().execute(grouplist);

		mContactList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View view,
					int position, long id) {
				LinearLayout ll = (LinearLayout) view;
				CheckBox cb = (CheckBox) ll.getChildAt(0).findViewById(
						R.id.check);

				if (cb.isChecked()) {
					cb.setChecked(false);
					mNumList = mNumList.replace(
							"," + mListViewData.get(position).getTelNumber(),
							"");
					mListViewData.get(position).isChecked = false;
					mAllBtn.setText("全选");
				} else {

					cb.setChecked(true);
					mNumList += ","
							+ mListViewData.get(position).getTelNumber();
					mListViewData.get(position).isChecked = true;
				}
			}
		});

		mAddBtn.setOnClickListener(btnClick);
		mBackBtn.setOnClickListener(btnClick);
	}

	private OnClickListener btnClick = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_add:
				if(MultiSelectTab.NeedSendResult){
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					String bundleStr = mNumList;
					if (bundleStr != "") {
						bundleStr = bundleStr.substring(1);
					}
					Log.i("xiao", "numberStr = " + bundleStr);
					bundle.putString("numberStr", bundleStr);
					intent.putExtras(bundle);
					setResult(1, intent);
				}
				else{
					Log.v("heiheifang", mNumList);
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					String bundleStr = mNumList;
					if (bundleStr != "") {
						bundleStr = bundleStr.substring(1);
					}
					Log.i("xiao", "numberStr = " + bundleStr);
					bundle.putString("number", bundleStr);
					intent.putExtras(bundle);
					intent.setClass(getApplicationContext(), SmsSendByGroup.class);
					startActivity(intent);
				}
				finish();
				break;

			case R.id.btn_back:
				finish();
				break;
			}
		}
	};

	private class GetContactTask extends
			AsyncTask<ArrayList<Integer>, String, String> {
		public String doInBackground(ArrayList<Integer>... params) {
			Log.v("heiheifang", "in asynstask groupid is " + params[0]);
			getAllContactsByGroupId(params[0]);
			return "";
		}

		@Override
		protected void onPreExecute() {
			showDialog(DIALOG_KEY);
		}

		@Override
		public void onPostExecute(String Re) {
			if (mListViewData.size() == 0) {
				mEmptyTextView.setVisibility(View.VISIBLE);
			} else {
				Comparator comp = new Mycomparator();
				Collections.sort(mListViewData, comp);

				mNumList = GetNotInContactNumber(wNumStr, mListViewData)
						+ mNumList;
				mContactAdapter = new EachGroupAdapter(EachGroupListView.this,
						mListViewData);
				mContactList.setAdapter(mContactAdapter);
				mContactList.setTextFilterEnabled(true);

				autoContact = new String[mListViewData.size()];
				for (int c = 0; c < mListViewData.size(); c++) {
					autoContact[c] = mListViewData.get(c).ContactName + "("
							+ mListViewData.get(c).TelNumber + ")";
				}

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						EachGroupListView.this,
						android.R.layout.simple_dropdown_item_1line,
						autoContact);
			}
			removeDialog(DIALOG_KEY);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_KEY: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage(this.getText(R.string.loading_contacts));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}

	private boolean IsContain(ArrayList<EachGroupInfo> list, String un) {
		for (int i = 0; i < list.size(); i++) {
			if (un.equals(list.get(i).TelNumber)) {
				return true;
			}
		}
		return false;
	}

	private boolean IsAlreadyCheck(String[] Str, String un) {
		if (Str != null) {
			for (int i = 0; i < Str.length; i++) {
				if (un.equals(Str[i])) {
					return true;
				}
			}
		}
		return false;
	}

	private String GetNotInContactNumber(String[] Str,
			ArrayList<EachGroupInfo> list) {
		String re = "";
		if (Str != null) {
			for (int i = 0; i < Str.length; i++) {
				if (IsUserNumber(Str[i])) {
					for (int l = 0; l < list.size(); l++) {
						if (Str[i].equals(list.get(l).TelNumber)) {
							Str[i] = "";
							break;
						}
					}
					if (!Str[i].equals("")) {
						re += "," + Str[i];
					}
				}
			}
		}
		return re;
	}

	public class Mycomparator implements Comparator {
		public int compare(Object o1, Object o2) {
			EachGroupInfo c1 = (EachGroupInfo) o1;
			EachGroupInfo c2 = (EachGroupInfo) o2;
			Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
			if(c1.ContactName == null){
				return -1;
			}
			if(c2.ContactName == null){
				return 1;
			}
			return cmp.compare(c1.ContactName, c2.ContactName);
		}

	}

	public static boolean IsUserNumber(String num) {
		boolean re = false;
		if (num.length() >= 11) {
			if (num.startsWith("1")) {
				re = true;
			}

		}
		return re;
	}

	public static String GetNumber(String num2) {
		String num;
		if (num2 != null) {
			System.out.println(num2);
			num = num2.replaceAll("-", "");
			if (num.startsWith("+86")) {
				num = num.substring(3);
			} else if (num.startsWith("86")) {
				num = num.substring(2);
			} else if (num.startsWith("86")) {
				num = num.substring(2);
			}
		} else {
			num = "";
		}
		return num;
	}

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
			for (int i = 0; i < mContactList.getChildCount(); i++) {
				Log.i("XXX", "Contact " + i);
				View view = mContactList.getChildAt(i);
				CheckBox cb = (CheckBox) view.findViewById(R.id.check);
				cb.setChecked(true);
			}
			mNumList = "";
			for (int i = 0; i < mListViewData.size(); i++) {
				mListViewData.get(i).isChecked = true;
				mNumList += "," + mListViewData.get(i).getTelNumber();
			}
			button.setText("取消全选");
		} else if (button.getText().toString().equals("取消全选")) {
			Log.v("heiheifang", "quxiaoquanxuan");
			for (int i = 0; i < mContactList.getChildCount(); i++) {
				Log.i("XXX", "Contact " + i);
				View view = mContactList.getChildAt(i);
				CheckBox cb = (CheckBox) view.findViewById(R.id.check);
				cb.setChecked(false);
			}
			mNumList = "";
			for (int i = 0; i < mListViewData.size(); i++) {
				mListViewData.get(i).isChecked = false;
			}
			button.setText("全选");
		}
	}

	public void getAllContactsByGroupId(ArrayList<Integer> groupList) {
		String[] RAW_PROJECTION = new String[] { ContactsContract.Data.RAW_CONTACT_ID };

		Iterator<Integer> iterator = groupList.iterator();
		StringBuilder groupids_temp = new StringBuilder("(");
		while (iterator.hasNext()) {
			groupids_temp.append(iterator.next() + ",");
		}
		groupids_temp.setCharAt(groupids_temp.length() - 1, ')');
		String groupids = groupids_temp.toString();
		Log.v("heiheifang", "groupids is " + groupids);
		if(groupids == null || groupids.length() < 1){
			return ;
		}
		String RAW_CONTACTS_WHERE = ContactsContract.Data.DATA1 + " IN "
				+ groupids + " and " + ContactsContract.Data.MIMETYPE + "="
				+ "'" + GroupMembership.CONTENT_ITEM_TYPE + "'";

		Cursor cursor = getContentResolver().query(
				ContactsContract.Data.CONTENT_URI, RAW_PROJECTION,
				RAW_CONTACTS_WHERE, null, "data1 asc");
		Log.v("heiheifang", "count is " + cursor.getCount());

		while (cursor.moveToNext()) {
			int col = cursor.getColumnIndex("raw_contact_id");
			int raw_contact_id = cursor.getInt(col);
			Log.v("heiheifang", "raw_contact_id is " + raw_contact_id);
			GetLocalContact(raw_contact_id);
		}

		cursor.close();
	}

	private void GetLocalContact(int rawid) {
		ContentResolver cr = getContentResolver();
		EachGroupInfo ce = new EachGroupInfo();

		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		Cursor dataCursor = getContentResolver().query(dataUri, null,
				"raw_contact_id=?", new String[] { rawid + "" }, null);

		while (dataCursor.moveToNext()) {
			String data1_temp = dataCursor.getString(dataCursor
					.getColumnIndex("data1"));
			String data1 = GetNumber(data1_temp);
			String mime = dataCursor.getString(dataCursor
					.getColumnIndex("mimetype"));

			if ("vnd.android.cursor.item/phone_v2".equals(mime)) {
				ce.setTelNumber(data1);
			} else if ("vnd.android.cursor.item/name".equals(mime)) {
				ce.setContactName(data1);
			}
		}
		ce.setIsChecked(false);
		dataCursor.close();
		mListViewData.add(ce);
		ce = null;
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
