package com.ginwave.smshelper.readcontacts;
import com.umeng.analytics.MobclickAgent;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.ginwave.smshelper.MainActivity;
import com.ginwave.smshelper.MultiSelectTab;
import com.ginwave.smshelper.R;
import com.ginwave.smshelper.SMSLibsSendActivity;
import com.ginwave.smshelper.SmsSendByGroup;

import android.app.ActionBar;
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
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.MenuItem;
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

public class MultiContactListViewNoTitle extends Activity implements OnClickListener {
	private ListView mContactList;
	private TextView mEmptyTextView;
	private Button mAddBtn;
	private Button mBackBtn;
	private Cursor mCursor = null;
	private MultiContactAdapter mContactAdapter;
	private ArrayList<MultiContactInfo> mListViewData = new ArrayList<MultiContactInfo>();
	private String mNumList = "";
	protected String[] autoContact = null;
	protected String[] wNumStr = null;
	private static final int DIALOG_KEY = 0;
	private ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listviewformulticontactnotitle);
		mContactList = (ListView) findViewById(R.id.list);
		mEmptyTextView = (TextView) findViewById(R.id.empty);
		mAddBtn = (Button) findViewById(R.id.btn_add);
		mBackBtn = (Button) findViewById(R.id.btn_back);
		mEmptyTextView.setVisibility(View.GONE);
		/*
		 * Intent intent = this.getIntent(); Bundle bundle = intent.getExtras();
		 * String wNumberStr = bundle.getString("wNumberStr").replace("", ",");
		 * wNumStr = wNumberStr.split(","); // Intent intent=new Intent(); final
		 * String xm = bundle.getString("xm");
		 */
		// StringBuilder sb = new StringBuilder();
		// sb.append("" + xm + "\n");
		/* Log.i("xm", "xm4" + xm); */

		// bundle.putString("wNumberStr", wNumberStr);
		// Intent intent=new Intent();
		/*
		 * bundle.putString("xm", xm); intent.putExtras(bundle); Log.i("xm",
		 * "xm5" + xm);
		 */
		new GetContactTask().execute("");

		mContactList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View view,
					int position, long id) {
				LinearLayout ll = (LinearLayout) view;
				CheckBox cb = (CheckBox) ll.getChildAt(0).findViewById(
						R.id.check);

				if (cb.isChecked()) {
					cb.setChecked(false);
					mNumList = mNumList.replace(
							"," + mListViewData.get(position).getUserNumber(),
							"");
					mListViewData.get(position).isChecked = false;
				} else {

					cb.setChecked(true);
					mNumList += ","
							+ mListViewData.get(position).getUserNumber();
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
					Log.i("xiao", "ssssssssnumberStr = " + bundleStr);
					bundle.putString("numberStr", bundleStr);
					intent.putExtras(bundle);
					MultiContactListViewNoTitle.this.getParent().setResult(1, intent);
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
				/* MultiContactListView.this.getParent().setResult(1, intent); */

				// intent.setClass(ContactListView.this,SendMessageActivity.class);
				// startActivity(intent);
				finish();
				break;

			case R.id.btn_back:
				finish();
				break;
			}
		}
	};

	private class GetContactTask extends AsyncTask<String, String, String> {
		public String doInBackground(String... params) {
			GetLocalContact();
			GetSimContact("content://icc/adn");
			GetSimContact("content://sim/adn");
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
				mContactAdapter = new MultiContactAdapter(
						MultiContactListViewNoTitle.this, mListViewData);
				mContactList.setAdapter(mContactAdapter);
				mContactList.setTextFilterEnabled(true);

				autoContact = new String[mListViewData.size()];
				for (int c = 0; c < mListViewData.size(); c++) {
					autoContact[c] = mListViewData.get(c).contactName + "("
							+ mListViewData.get(c).userNumber + ")";
				}

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						MultiContactListViewNoTitle.this,
						android.R.layout.simple_dropdown_item_1line,
						autoContact);
			}
			if(mProgressDialog != null && mProgressDialog.isShowing()){
				mProgressDialog.dismiss();
			}
			//removeDialog(DIALOG_KEY);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_KEY: {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage(this.getText(R.string.loading_contacts));
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(true);
			return mProgressDialog;
		}
		}
		return null;
	}

	private void GetLocalContact() {
		ContentResolver cr = getContentResolver();
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				MultiContactInfo cci = new MultiContactInfo();
				int nameFieldColumnIndex = cursor
						.getColumnIndex(PhoneLookup.DISPLAY_NAME);
				cci.contactName = cursor.getString(nameFieldColumnIndex);
				int id = cursor.getInt(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				Cursor phone = cr.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ "=?", new String[] { Integer.toString(id) },
						null);

				while (phone.moveToNext()) {
					String strPhoneNumber = phone
							.getString(phone
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					if(strPhoneNumber != null){
						System.out.println(strPhoneNumber);
						cci.userNumber = GetNumber(strPhoneNumber);
						cci.isChecked = false;
	
						if (!IsContain(mListViewData, cci.userNumber)) {
							if (IsUserNumber(strPhoneNumber)) {
								if (IsAlreadyCheck(wNumStr, cci.userNumber)) {
									cci.isChecked = true;
									mNumList += "," + cci.userNumber;
								}
								mListViewData.add(cci);
							}
						}
					}
				}

				phone.close();
			}
			cursor.close();
		}
	}

	private void GetSimContact(String add) {
		try {
			Intent intent = new Intent();
			intent.setData(Uri.parse(add));
			Uri uri = intent.getData();
			mCursor = getContentResolver().query(uri, null, null, null, null);
			if (mCursor != null) {
				while (mCursor.moveToNext()) {
					MultiContactInfo sci = new MultiContactInfo();
					int nameFieldColumnIndex = mCursor.getColumnIndex("name");
					sci.contactName = mCursor.getString(nameFieldColumnIndex);
					int numberFieldColumnIndex = mCursor
							.getColumnIndex("number");
					sci.userNumber = mCursor.getString(numberFieldColumnIndex);

					sci.userNumber = GetNumber(sci.userNumber);
					sci.isChecked = false;

					if (IsUserNumber(sci.userNumber)) {
						if (!IsContain(mListViewData, sci.userNumber)) {
							if (IsAlreadyCheck(wNumStr, sci.userNumber)) {
								sci.isChecked = true;
								mNumList += "," + sci.userNumber;
							}
							mListViewData.add(sci);
						}
					}
				}
				mCursor.close();
			}
		} catch (Exception e) {
			Log.i("eoe", e.toString());
		}
	}

	private boolean IsContain(ArrayList<MultiContactInfo> list, String un) {
		for (int i = 0; i < list.size(); i++) {
			if (un.equals(list.get(i).userNumber)) {
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
			ArrayList<MultiContactInfo> list) {
		String re = "";
		if (Str != null) {
			for (int i = 0; i < Str.length; i++) {
				if (IsUserNumber(Str[i])) {
					for (int l = 0; l < list.size(); l++) {
						if (Str[i].equals(list.get(l).userNumber)) {
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
			MultiContactInfo c1 = (MultiContactInfo) o1;
			MultiContactInfo c2 = (MultiContactInfo) o2;
			Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
			return cmp.compare(c1.contactName, c2.contactName);
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
		if (v.getId() == R.id.button1) {
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
				mNumList += "," + mListViewData.get(i).getUserNumber();
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
	// Intent intent = new Intent(this, SMSLibsSendActivity.class);
	// startActivity(intent);
	// finish();
	// return true;
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	//
	// }

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
