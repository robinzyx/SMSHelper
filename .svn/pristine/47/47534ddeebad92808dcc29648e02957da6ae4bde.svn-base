package com.ginwave.smshelper.localsms;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ginwave.smshelper.ConversationInfo;
import com.ginwave.smshelper.HolidayGridChooseSMS;
import com.ginwave.smshelper.R;
import com.ginwave.smshelper.TransportInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.widget.CursorAdapter;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;

public class SmsSearchReader extends Activity implements View.OnClickListener {
	private ListView mListview;
	private LayoutInflater mInflater;
	private String mPhoneNumber;
	private AlertDialog mAlterDialog;
	private TextView mMainBack;
	private static final int DIALOG_KEY = 0;
	private String mSearchContent = null;
	private EditText mEditText;
	private ConversationSearchAdapter mConversationSearchAdapter;
	private List<ConversationInfo> mBaseInfoList;
	private List<ConversationInfo> mAdapterInfoList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listviewforlocalsmssearch);
		mBaseInfoList = SmsReader.cloneConversationList();
		if (mBaseInfoList.size() > 0) {
			mBaseInfoList.remove(0);
		}
		mAdapterInfoList = SmsReader.cloneConversationList(mBaseInfoList);
		mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		mListview = (ListView) findViewById(R.id.listview);
		mConversationSearchAdapter = new ConversationSearchAdapter(
				this.getApplicationContext());
		mListview.setAdapter(mConversationSearchAdapter);
		mEditText = (EditText) findViewById(R.id.mEditText);
		mEditText.addTextChangedListener(new SearchTextWatcher());
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainBack.setOnClickListener(this);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				/*
				 * showMultiChoiceDialog(((TextView) ((ViewGroup) arg1)
				 * .findViewById
				 * (R.id.recipient_textview)).getText().toString());
				 */
				TransportInfo lInfo = (TransportInfo) arg1.getTag();
				Intent lIntent = new Intent();
				Bundle lBundle = new Bundle();
				Log.i("xiao", "number = " + lInfo.getNumber());
				Log.i("xiao", "name = " + lInfo.getName());
				lBundle.putString("name", lInfo.getName());
				lBundle.putString("number", lInfo.getNumber());
				lIntent.putExtras(lBundle);
				lIntent.setClass(getApplicationContext(),
						SmsOnePersonReader.class);
				SmsSearchReader.this.startActivity(lIntent);
			}
		});
		try {
			// testGetContacts();
		} catch (Exception e) {
			Log.i("xiao", "Exception e = " + e.getMessage());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class SearchTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			handleAdapterData(arg0.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		public void handleAdapterData(String pContent) {
			mSearchContent = pContent;
			List<ConversationInfo> lConversationList = new ArrayList<ConversationInfo>();
			ConversationInfo lInfo = null;
			ConversationInfo conversation = new ConversationInfo();
			lConversationList.add(conversation);
			if (SmsReader.mConversationInfoPreviousList != null) {
				if (pContent != null && pContent.length() > 0) {
					for (int i = 1; i < SmsReader.mConversationInfoPreviousList
							.size(); i++) {
						lInfo = SmsReader.mConversationInfoPreviousList.get(i);
						if (lInfo.mName.contains(pContent)
								|| lInfo.mNumber.contains(pContent)) {
							try {
								lConversationList.add(lInfo.clone());
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} else {
					for (int j = 1; j < SmsReader.mConversationInfoPreviousList
							.size(); j++) {
						lInfo = SmsReader.mConversationInfoPreviousList.get(j);
						try {
							lConversationList.add(lInfo.clone());
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				mAdapterInfoList.clear();
				mAdapterInfoList = lConversationList;
				mConversationSearchAdapter.notifyDataSetChanged();
			}
		}

	}

	public class ConversationSearchAdapter extends BaseAdapter {
		private ViewHolder mHolder = null;
		private ConversationInfo mInfo = null;
		private Context mContext;
		private TransportInfo mTransportInfo;

		public ConversationSearchAdapter(Context pContext) {
			mContext = pContext;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mAdapterInfoList.size() - 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mAdapterInfoList.get(position + 1);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			position = position + 1;
			if (position == 0) {
				convertView = mInflater.inflate(
						R.layout.listforlocalsmsitemheader, null, false);
				EditText editText = (EditText) convertView
						.findViewById(R.id.mLocalSearchView);
				editText.setText(mSearchContent);
				// editText.addTextChangedListener(new SearchTextWatcher());
			} else {

				mInfo = mAdapterInfoList.get(position);
				if (convertView == null
						|| (convertView != null && convertView.getId() == R.id.mHead)) {
					convertView = mInflater.inflate(
							R.layout.listforlocalsmsitem, null, false);
					mHolder = new ViewHolder();
				}
				mHolder.mLocalSmsIconText = (TextView) convertView
						.findViewById(R.id.mLocalSmsIconText);
				mHolder.mRecipientTime = (TextView) convertView
						.findViewById(R.id.mRecipientTime);
				mHolder.mRecipientTextView = (TextView) convertView
						.findViewById(R.id.mRecipientTextview);
				mHolder.mSnippetTextView = (TextView) convertView
						.findViewById(R.id.mSnippetTextview);
				if (mInfo.mName != null && mInfo.mName.length() > 0) {
					mTransportInfo = new TransportInfo(mInfo.mNumber,
							mInfo.mName, mInfo.mThreadId);
					mHolder.mLocalSmsIconText.setText(mInfo.mName.subSequence(
							0, 1));
					mHolder.mLocalSmsIconText
							.setBackgroundResource(R.drawable.bg_photo_normal);
					mHolder.mRecipientTextView.setText(mInfo.mName);
				} else {
					mTransportInfo = new TransportInfo(mInfo.mNumber, "", mInfo.mThreadId);
					mHolder.mLocalSmsIconText.setText("");
					mHolder.mLocalSmsIconText
							.setBackgroundResource(R.drawable.bg_photo_default);
					mHolder.mRecipientTextView.setText(mInfo.mNumber);
				}
				if (mInfo.mSnippetCS == 0) {
					mHolder.mSnippetTextView.setText(mInfo.mSnippet);
				} else {
					try {
						mHolder.mSnippetTextView.setText(new String(
								mInfo.mSnippet.getBytes("ISO8859_1"), "utf-8"));
					} catch (UnsupportedEncodingException e) {
					}
				}
				convertView.setTag(mTransportInfo);
			}
			return convertView;
		}

		public final class ViewHolder {
			public TextView mLocalSmsIconText;
			public TextView mRecipientTime;
			public TextView mRecipientTextView;
			public TextView mSnippetTextView;
		}

		public String getContactNameByPhone(String number) {
			String name = null;
			Uri personUri = Uri.withAppendedPath(
					ContactsContract.PhoneLookup.CONTENT_FILTER_URI, number);
			Cursor cur = mContext.getContentResolver()
					.query(personUri,
							new String[] { PhoneLookup.DISPLAY_NAME }, null,
							null, null);
			if (cur.moveToFirst()) {
				int nameIdx = cur.getColumnIndex(PhoneLookup.DISPLAY_NAME);

				name = cur.getString(nameIdx);
				cur.close();
			}
			return name;
		}

		public String getDisplayNameByNumber(String pNumber) {
			String name = null;
			Uri uri = Uri.parse("content://com.android.contacts/data/phones");
			ContentResolver resolver = mContext.getContentResolver();
			Cursor phoneCursor = resolver.query(uri, null, "data1=?",
					new String[] { pNumber }, null);
			Log.i("xiao", "number = " + pNumber + " size = " + pNumber.length());
			// Log.i("xiao", "phoneCursor size = " + phoneCursor.getCount());
			if (phoneCursor != null) {
				while (phoneCursor.moveToNext()) {
					for (int i = 0; i < phoneCursor.getColumnCount(); i++) {
						// Log.i("xiao", "Column = " +
						// phoneCursor.getColumnName(i));
					}
					Log.i("xiao",
							"contactid = "
									+ phoneCursor.getInt(phoneCursor
											.getColumnIndex("contact_id")));
					String phone = phoneCursor.getString(phoneCursor
							.getColumnIndex("data1"));
					Log.i("xiao", "phone = " + phone);
					name = phoneCursor.getString(phoneCursor
							.getColumnIndex("display_name"));
					Log.i("xiao", "phone = " + name);
				}
			}
			return name;
		}

		int getThreadId(String number) {
			final String SMS_URI_ALL = "content://sms/";
			final String ADDRESS = "address";
			final String THREAD_ID = "thread_id";
			final Uri URI = Uri.parse(SMS_URI_ALL);
			int threadId = 0;
			// ��ݵ绰����ȥ����
			ContentResolver cr = mContext.getContentResolver();
			String[] projection = new String[] { THREAD_ID, ADDRESS };

			Cursor cur = cr.query(URI, projection, ADDRESS + " = ?",
					new String[] { number }, null);// asc

			int threadIdColumn = cur.getColumnIndex(THREAD_ID);
			if (cur != null) {

				while (cur.moveToNext()) {
					// ��ûỰ��thread_id
					threadId = cur.getInt(threadIdColumn);
					Log.v("smsLog", "thread id=" + threadId);
				}
				cur.close();
			}

			return threadId;

		}

		public void getSmsInPhone(String number) {
			final String SMS_URI_ALL = "content://sms/";
			final String ADDRESS = "address";
			final String THREAD_ID = "thread_id";
			final Uri URI = Uri.parse(SMS_URI_ALL);
			int thread_id = getThreadId(number);
			// ��ö���_id ���� ���� �������� ʱ�� �� ����
			String[] projection = new String[] { "_id", ADDRESS, "person",
					"body", "date", "type" };
			ContentResolver cr = mContext.getContentResolver();
			Cursor cur = cr.query(URI, projection, THREAD_ID + " = ?",
					new String[] { Integer.toString(thread_id) }, "date desc");

			int nameFieldColumnIndex = cur.getColumnIndex("person");
			int numberFieldColumnIndex = cur.getColumnIndex(ADDRESS);
			int smsbodyColumn = cur.getColumnIndex("body");
			int dateColumn = cur.getColumnIndex("date");
			int typeColumn = cur.getColumnIndex("type");
			if (cur != null) {

				while (cur.moveToNext()) {
					Log.i("xiao",
							"name = " + cur.getString(nameFieldColumnIndex));
					Log.i("xiao",
							"number = " + cur.getString(numberFieldColumnIndex));
					Log.i("xiao", "body = " + cur.getString(smsbodyColumn));
					Log.i("xiao", "date = " + cur.getLong(dateColumn));
					Log.i("xiao", "type = " + cur.getInt(typeColumn));
				}
				cur.close();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SmsSearchReader.this);
			builder.setTitle(R.string.exitdialog_title);
			builder.setMessage(R.string.exitdialog_content);
			builder.setPositiveButton(R.string.exitdialog_ok,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							SmsSearchReader.this.finish();
						}
					});
			builder.setNegativeButton(R.string.exitdialog_cancel,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			builder.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mMainBack)) {
			this.finish();
		} else {
			int position = (Integer) v.getTag();
			TextView textView = (TextView) v
					.findViewById(R.id.mReplyMethodItemText);
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			switch (position) {
			case 0:
			case 1:
			case 2:
				intent.setClass(getApplicationContext(),
						LocalSmsSendActivity.class);
				bundle.putString("xm", textView.getText().toString());
				bundle.putString("phonenumber", mPhoneNumber);
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case 3:
				intent.setClass(SmsSearchReader.this,
						HolidayGridChooseSMS.class);
				intent.putExtra("phonenumber", mPhoneNumber);
				SmsSearchReader.this.startActivity(intent);
				break;
			case 4:
				intent.setClass(getApplicationContext(),
						LocalSmsSendActivity.class);
				bundle.putString("xm", "");
				bundle.putString("phonenumber", mPhoneNumber);
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			}
			mAlterDialog.dismiss();
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
