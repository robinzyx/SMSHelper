package com.ginwave.smshelper.localsms;

import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import com.ginwave.smshelper.HolidayGridChooseSMS;
import com.ginwave.smshelper.MessageBase;
import com.ginwave.smshelper.MessageMms;
import com.ginwave.smshelper.MessageSms;
import com.ginwave.smshelper.R;
import com.ginwave.smshelper.SmsSendByGroup;
import com.ginwave.smshelper.more.SetDataSource;
import com.ginwave.smshelper.util.SendSuccessReceiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.widget.CursorAdapter;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SmsOnePersonReader extends Activity implements
		View.OnClickListener {
	private static final Uri THREADS_URI = Uri
			.parse("content://mms-sms/conversations?simple=true");

	// projection
	private static final String[] ALL_THREADS_PROJECTION = { "_id", "date",
			"message_count", "recipient_ids", "snippet", "snippet_cs", "read",
			"type", "error", "has_attachment" };
	// columns, not used
	private static final int ID = 0;
	private static final int DATE = 1;
	private static final int MESSAGE_COUNT = 2;
	private static final int RECIPIENT_IDS = 3;
	private static final int SNIPPET = 4;
	private static final int SNIPPET_CS = 5;
	private static final int READ = 6;
	private static final int TYPE = 7;
	private static final int ERROR = 8;
	private static final int HAS_ATTACHMENT = 9;
	private ListView mListview;
	private LayoutInflater mInflater;
	private String mPhoneNumber;
	private AlertDialog mAlterDialog;
	private TextView mMainBack;
	private TextView mChooseMms;
	private EditText mMmsContent;
	private TextView mSendMms;
	private static final int DIALOG_KEY = 0;
	private Cursor mCursor;
	private String mPersonName;
	private String mPersonNumber;
	private String mThreadId;
	private SendSuccessReceiver mReceiver;
	private static final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	private List<MessageBase> mMessageList;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			removeDialog(DIALOG_KEY);
			super.handleMessage(msg);
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listviewforlocaloneperson);
		mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		mListview = (ListView) findViewById(R.id.listview);
		Intent lIntent = this.getIntent();
		Bundle lBundle = lIntent.getExtras();
		mPersonName = lBundle.getString("name");
		mPersonNumber = lBundle.getString("number");
		mThreadId = lBundle.getString("thread_id");
		new GetMmsTask().execute("");
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mChooseMms = (TextView) findViewById(R.id.mChooseMms);
		mMmsContent = (EditText) findViewById(R.id.mMmsContent);
		mSendMms = (TextView) findViewById(R.id.mSendMms);
		mMainBack.setOnClickListener(this);
		mChooseMms.setOnClickListener(this);
		mSendMms.setOnClickListener(this);
		mReceiver = new SendSuccessReceiver();
		IntentFilter deliverFilter = new IntentFilter();
		deliverFilter.addAction(SENT_SMS_ACTION);
		this.registerReceiver(mReceiver, deliverFilter);
		mMessageList = new ArrayList<MessageBase>();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	private class GetMmsTask extends AsyncTask<String, String, String> {
		public String doInBackground(String... params) {
			// mCursor = getSmsInPhone(mPersonNumber);
			readSmsMms();
			return "";
		}

		@Override
		protected void onPreExecute() {
			showDialog(DIALOG_KEY);
		}

		@Override
		public void onPostExecute(String Re) {
			Log.i("xiao", "onPostExecute");
			mListview.setAdapter(new SmsMmsAdapter(getApplicationContext()));
			mListview.setSelection(mListview.getAdapter().getCount() - 1);
			/*mListview.setAdapter(new SmsCursorAdapter(getApplicationContext(),
					mCursor, mHandler));
			mListview.setSelection(mListview.getAdapter().getCount() - 1);*/
			removeDialog(DIALOG_KEY);
		}

		public void logs(String pWhere) {
			Log.i("xiao", pWhere);
		}

		private void readSmsMms() {
			readSms(mThreadId);
			readMms(mThreadId);
			sortSmsMms();
		}

		private void sortSmsMms() {
			Collections.sort(mMessageList, new ComparatorMessage());
			for( int i = 0; i < mMessageList.size(); i++){
				MessageBase message = mMessageList.get(i);
				if(message instanceof MessageMms){
					MessageMms messageMms = (MessageMms)message;
					if(messageMms.mBitmap == null){
						logs("=== null");
					}
					else{
						logs("=== !!!null");
					}
				}
			}
		}

		public class ComparatorMessage implements Comparator {
			public int compare(Object arg0, Object arg1) {
				MessageBase message0 = (MessageBase) arg0;
				MessageBase message1 = (MessageBase) arg1;
				int flag = ((Long) message0.mDate)
						.compareTo((Long) message1.mDate);
				return flag;
			}
		}

		private void readSms(String thread_id) {
			Uri sThreadMessageUri = Uri.parse("content://sms/");
			Cursor cursor = getContentResolver().query(sThreadMessageUri, null,
					" thread_id = ? ", new String[] { thread_id }, "date DESC");
			if (cursor != null) {
				//logs("cursor !!!!!!!!NNNNNNNNNNNNNNNNNNNULL");
				while (cursor.moveToNext()) {
					MessageSms messageSms = new MessageSms();
					messageSms.mThreadId = thread_id;
					messageSms.mIsSms = true;
					
					messageSms.mBody = cursor.getString(11);
					messageSms.mId = cursor.getString(0);
					messageSms.mDate = cursor.getLong(4) / 1000;
					messageSms.mType = cursor.getInt(cursor.getColumnIndex("type"));
					logs("sms body = " + messageSms.mBody + " types = " + messageSms.mType);
					if(messageSms.mType == 1){
						messageSms.mIsInbox = true;
						//logs("iiiiiiiiiiiii");
					}
					else{
						//logs("ooooooooooooooo");
						messageSms.mIsInbox = false;
					}
					mMessageList.add(messageSms);
				}
				cursor.close();
			} else {
				//logs("cursor ======NNNNNNNNNNNNNNNNNNNULL");
			}
			//logs(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}

		private void readMms(String thread_id) {
			// logs("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			Uri sThreadMessageUri = Uri.parse("content://mms/");
			Cursor cursor = getContentResolver().query(sThreadMessageUri, null,
					" thread_id = ? ", new String[] { thread_id }, "date DESC");
			if (cursor != null) {
				//logs("cursor !!!!!!!!NNNNNNNNNNNNNNNNNNNULL "
						//+ cursor.getCount());
				while (cursor.moveToNext()) {
					MessageMms messageMms = new MessageMms();
					messageMms.mThreadId = thread_id;
					messageMms.mIsSms = false;
					//logs("sub = " + cursor.getString(6));
					messageMms.mId = cursor.getString(0);
					messageMms.mDate = cursor.getLong(2);
					messageMms.mType = cursor.getInt(12);
					if(cursor.getInt(3) == 1){
						//logs("iiiiiiiiiiiiiiiiii");
						messageMms.mIsInbox = true;
					}
					else{
						//logs("ooooooooooooooooooooo");
						messageMms.mIsInbox = false;
					}
					//logs("mDate = " + cursor.getString(2));
					try {
						messageMms.mSubject = new String(cursor.getString(6)
								.getBytes("ISO8859_1"), "utf-8");
						logs("sub iso = "
								+ new String(cursor.getString(6).getBytes(
										"ISO8859_1"), "utf-8"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logs("Exception ________________" + e.getMessage());
						e.printStackTrace();
					}
					readMmsBodyAndBitmap(cursor.getString(0), messageMms);
					mMessageList.add(messageMms);
				}
				cursor.close();
			} else {
				logs("cursor ======NNNNNNNNNNNNNNNNNNNULL");
			}
		}

		private void readMmsBodyAndBitmap(String pId, MessageMms pMessageMms) {
			String selectionPart = "mid = ?";

			Uri sThreadMessageUri = Uri.parse("content://mms/part");
			Cursor cursor = getContentResolver().query(sThreadMessageUri, null,
					selectionPart, new String[] { pId }, null);
			logs("MmsPart ");
			if (cursor != null) {
				logs("cursor size = " + cursor.getCount());
				while (cursor.moveToNext()) {

					logs("_id = "
							+ cursor.getString(cursor.getColumnIndex("_id")));
					logs("mid = "
							+ cursor.getString(cursor.getColumnIndex("mid")));
					String partId = cursor.getString(cursor
							.getColumnIndex("_id"));
					String type = cursor.getString(cursor.getColumnIndex("ct"));
					if ("text/plain".equals(type)) {
						String data = cursor.getString(cursor
								.getColumnIndex("_data"));
						String body;
						if (data != null) {
							// implementation of this method below
							body = getMmsTexts(partId);
						} else {
							body = cursor.getString(cursor
									.getColumnIndex("text"));
						}
						if (body != null) {
							logs("body = " + body);
						}
						pMessageMms.mBody = body;
					}
					pMessageMms.mPartId = partId;
					if(pMessageMms.mBitmap == null){
						Bitmap bitmap = getMmsImage(partId);
						if(bitmap == null){
							logs("llllbitmap === null");
						}
						else{
							logs("llllbitmap !!!! null");
						}
						pMessageMms.mBitmap = bitmap;
					}
					
				}
				cursor.close();
			}
		}

		private Bitmap getMmsImage(String _id) {
			Uri partURI = Uri.parse("content://mms/part/" + _id);
			InputStream is = null;
			Bitmap bitmap = null;
			try {
				is = getContentResolver().openInputStream(partURI);
				bitmap = BitmapFactory.decodeStream(is);
			} catch (IOException e) {
				logs("IOException1 " + e.getMessage());
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						logs("IOException2 " + e.getMessage());
					}
				}
			}
			return bitmap;
		}

		private String getMmsTexts(String id) {
			Uri partURI = Uri.parse("content://mms/part/" + id);
			InputStream is = null;
			StringBuilder sb = new StringBuilder();
			try {
				is = getContentResolver().openInputStream(partURI);
				if (is != null) {
					InputStreamReader isr = new InputStreamReader(is, "UTF-8");
					BufferedReader reader = new BufferedReader(isr);
					String temp = reader.readLine();
					while (temp != null) {
						sb.append(temp);
						temp = reader.readLine();
					}
				}
			} catch (IOException e) {
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
			return sb.toString();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_KEY: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage(this.getText(R.string.loading_sms));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}

	public class SmsMmsAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context mContext;
		private MessageBase mMessageBase;
		private MessageMms mMessageMms;
		private MessageSms mMessageSms;
		private ViewHolder mViewHolder;

		public SmsMmsAdapter(Context pContext) {
			mContext = pContext;
			mInflater = LayoutInflater.from(pContext);
			mMessageBase = new MessageBase();
			mMessageMms = new MessageMms();
			mMessageSms = new MessageSms();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mMessageList.size();
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
			mMessageBase = mMessageList.get(position);
			if (convertView == null) {
				mViewHolder = new ViewHolder();
				if (mMessageBase.mIsInbox) {
					convertView = mInflater.inflate(R.layout.list_say_he_item,
							parent, false);
				} else {
					convertView = mInflater.inflate(R.layout.list_say_me_item,
							parent, false);
				}
				
			} else {
				if (mMessageBase.mIsInbox) {
					if (convertView.getId() != R.id.mListSayHeItem) {
						convertView = mInflater.inflate(
								R.layout.list_say_he_item, parent, false);
						mViewHolder = new ViewHolder();
					} else {
						mViewHolder = (ViewHolder) convertView.getTag();
					}
				} else {
					if (convertView.getId() != R.id.mListSayMeItem) {
						convertView = mInflater.inflate(
								R.layout.list_say_me_item, parent, false);
						mViewHolder = new ViewHolder();
					} else {
						mViewHolder = (ViewHolder) convertView.getTag();
					}
				}
			}
			mMessageBase = mMessageList.get(position);
			mViewHolder.mBody = (TextView) convertView
					.findViewById(R.id.mMmsContent);
			mViewHolder.mMmsBitmap = (ImageView) convertView.findViewById(R.id.mMmsBitmap);
			mViewHolder.mMmsSubject = (TextView) convertView.findViewById(R.id.mMmsSubject);
			mViewHolder.mMmsSendTime = (TextView) convertView.findViewById(R.id.mMmsSendTime);
			if(mMessageBase.mIsSms){
				mMessageSms = (MessageSms) mMessageBase;
				mViewHolder.mBody.setText(mMessageSms.mBody);
				Log.i("xiao", "body = " + mMessageSms.mBody);
				mViewHolder.mMmsSendTime.setText(getSendTime(mMessageSms.mDate));
				mViewHolder.mMmsBitmap.setVisibility(View.GONE);
				mViewHolder.mMmsSubject.setVisibility(View.GONE);
			}
			else{
				
				mMessageMms = (MessageMms) mMessageBase;
				if(mMessageMms.mSubject == null){
					mViewHolder.mMmsSubject.setText(mContext.getString(R.string.subject_null));
				}
				else{
					mViewHolder.mMmsSubject.setText(mContext.getString(R.string.mms_subject_begintag) 
						+ mMessageMms.mSubject + mContext.getString(R.string.mms_subject_endtag));
				}
				Log.i("xiao", "body = " + mMessageMms.mBody);
				mViewHolder.mBody.setText(mMessageMms.mBody);
				mViewHolder.mMmsSendTime.setText(getSendTime(mMessageSms.mDate));
				mViewHolder.mMmsBitmap.setVisibility(View.VISIBLE);
				mViewHolder.mMmsSubject.setVisibility(View.VISIBLE);
				
				//Bitmap bitmap = getMmsImage(mMessageMms.mPartId);
				/*if(bitmap != null){
					logs("bitmap != null");
					mViewHolder.mMmsBitmap.setImageBitmap(bitmap);
				}
				else{
					logs("bitmap == null");
					mViewHolder.mMmsBitmap.setImageBitmap(null);
				}*/
				Date date = new Date(mMessageMms.mDate);
				logs("Y:" + date.getYear() + "M:" + date.getMonth() + "D:" + date.getDay());
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(mMessageMms.mDate * 1000);
				date = calendar.getTime();
				logs("Y:" + calendar.get(Calendar.YEAR) + "M:" + calendar.get(Calendar.MONTH) + "D:" + calendar.get(Calendar.DAY_OF_MONTH));
				mViewHolder.mMmsBitmap.setImageBitmap(mMessageMms.mBitmap);
				logs("getView subject = " + mMessageMms.mBody);
				if(mMessageMms.mBitmap != null){
					logs("mBitmap != null");
				}	
				else{
					logs("mBitmap == null");
				}
			}
			convertView.setTag(mViewHolder);
			return convertView;
		}
		
		private String getSendTime(Long pTimes){
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(mMessageMms.mDate * 1000);
			return mContext.getString(R.string.send_time) + calendar.get(Calendar.YEAR) + mContext.getString(R.string.year) 
					+ calendar.get(Calendar.MONTH) + mContext.getString(R.string.month)
					+ calendar.get(Calendar.DAY_OF_MONTH) + mContext.getString(R.string.day);
		}

		public final class ViewHolder {
			public TextView mBody;
			public ImageView mMmsBitmap;
			public TextView mMmsSubject;
			public TextView mMmsSendTime;
		}
		
		public void logs(String pWhere) {
			Log.i("xiao", pWhere);
		}
		
		private Bitmap getMmsImage(String _id) {
			Uri partURI = Uri.parse("content://mms/part/" + _id);
			InputStream is = null;
			Bitmap bitmap = null;
			try {
				is = getContentResolver().openInputStream(partURI);
				bitmap = BitmapFactory.decodeStream(is);
			} catch (IOException e) {
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
			return bitmap;
		}
	}

	static class SmsCursorAdapter extends CursorAdapter {
		private LayoutInflater mInflater;
		private Context mContext;
		private int mNameFieldColumnIndex = -100;
		private int mNumberFieldColumnIndex = -100;
		private int mSmsBodyColumnIndex = -100;
		private int mDataColumnIndex = -100;
		private int mTypeColumnIndex = -100;
		private int mTypeValue;
		private Handler mHandler;

		public SmsCursorAdapter(Context context, Cursor c, Handler handler) {
			super(context, c, true);
			mContext = context;
			mHandler = handler;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final String ADDRESS = "address";
			if (mNameFieldColumnIndex == -100) {
				mNameFieldColumnIndex = cursor.getColumnIndex("person");
				mNumberFieldColumnIndex = cursor.getColumnIndex(ADDRESS);
				mSmsBodyColumnIndex = cursor.getColumnIndex("body");
				mDataColumnIndex = cursor.getColumnIndex("date");
				mTypeColumnIndex = cursor.getColumnIndex("type");
			}
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			RelativeLayout lLayout = (RelativeLayout) view;
			TextView lMmsContent;
			if (cursor != null) {
				Log.i("xiao", "cursorrrrrrrrrrrrrrrrrrrrrrrr");
				Log.i("xiao",
						"name = " + cursor.getString(mNameFieldColumnIndex));
				Log.i("xiao",
						"number = " + cursor.getString(mNumberFieldColumnIndex));
				Log.i("xiao", "body = " + cursor.getString(mSmsBodyColumnIndex));
				Log.i("xiao", "date = " + cursor.getLong(mDataColumnIndex));
				mTypeValue = cursor.getInt(mTypeColumnIndex);
				/*
				 * if(mTypeValue == 1){ vi.inflate(R.layout.list_say_he_item,
				 * lLayout, true); } else{ vi.inflate(R.layout.list_say_me_item,
				 * lLayout, true); }
				 */
				lMmsContent = (TextView) lLayout.findViewById(R.id.mMmsContent);
				lMmsContent.setText(cursor.getString(mSmsBodyColumnIndex));
				mHandler.sendEmptyMessage(0);

			} else {
				Log.i("xiao", "eeeeeeeeeeeeeeeeeeeeeeee");
			}

		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			if (mTypeColumnIndex == -100) {
				mTypeColumnIndex = cursor.getColumnIndex("type");
			}
			mTypeValue = cursor.getInt(mTypeColumnIndex);
			if (mTypeValue == 1) {
				return mInflater.inflate(R.layout.list_say_he_item, parent,
						false);
			}
			return mInflater.inflate(R.layout.list_say_me_item, parent, false);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (data != null) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				String text = bundle.getString("content");
				mMmsContent.setText(text);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mMainBack)) {
			this.finish();
			return;
		}
		if (v.equals(mChooseMms)) {
			Intent lIntent = new Intent();
			showMultiChoiceDialog(mPersonNumber);
			return;
		}
		if (v.equals(mSendMms)) {
			String message = mMmsContent.getText().toString();
			if (mPhoneNumber == null) {
				mPhoneNumber = mPersonNumber;
			}
			if (mPhoneNumber.equals("")) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.send_toast),
						Toast.LENGTH_SHORT).show();
				return;
			}
			ArrayList<String> nums = new ArrayList<String>();
			nums.add(mPhoneNumber);
			new Thread(new MsgSentManager(getApplicationContext(), nums,
					message)).start();
			Toast.makeText(this, this.getString(R.string.send_sending),
					Toast.LENGTH_LONG).show();
			return;
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
			case 3:
			case 4:
				/*
				 * intent.setClass(getApplicationContext(),
				 * LocalSmsSendActivity.class); bundle.putString( "xm",
				 * textView.getText().toString());
				 * bundle.putString("phonenumber", mPhoneNumber);
				 * intent.putExtras(bundle);
				 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 * startActivity(intent);
				 */
				mMmsContent.setText(textView.getText().toString());
				break;
			case 5:
				intent.setClass(this, HolidayGridChooseSMS.class);
				intent.putExtra("phonenumber", mPhoneNumber);
				this.startActivityForResult(intent, 0);
				break;
			/*
			 * case 4: intent.setClass(getApplicationContext(),
			 * LocalSmsSendActivity.class); bundle.putString("xm", "");
			 * bundle.putString("phonenumber", mPhoneNumber);
			 * intent.putExtras(bundle);
			 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 * startActivity(intent); break;
			 */
			}
			mAlterDialog.dismiss();
		}
	}

	private void showMultiChoiceDialog(String phonenum) {
		mPhoneNumber = phonenum;
		View view = mInflater.inflate(R.layout.reply_method_layout, null);
		ListView listView = (ListView) view.findViewById(R.id.mReplyList);
		listView.setAdapter(new ReplyMethodAdapter());
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SmsOnePersonReader.this);
		builder.setTitle(getResources().getString(
				R.string.multichoicedialogtitle));

		builder.setView(view);
		builder.setCancelable(true);
		mAlterDialog = builder.create();
		mAlterDialog.show();
	}

	public class ReplyMethodAdapter extends BaseAdapter {

		private List<String> mReplyContentList;

		public ReplyMethodAdapter() {
			mReplyContentList = new ArrayList<String>();
			/*
			 * mReplyContentList.add(getResources().getString(
			 * R.string.multichoicedialogitem1));
			 * mReplyContentList.add(getResources().getString(
			 * R.string.multichoicedialogitem2));
			 * mReplyContentList.add(getResources().getString(
			 * R.string.multichoicedialogitem3));
			 * mReplyContentList.add(getResources().getString(
			 * R.string.multichoicedialogitem4));
			 */
			for (int i = 0; i < 5; i++) {
				mReplyContentList.add(SetDataSource.getManualReplyContent(
						getApplicationContext(),
						SetDataSource.mManualReplyName[i]));
			}
			mReplyContentList.add(
					mReplyContentList.size(),
					getApplicationContext().getString(
							R.string.multichoicedialogitem6));
			/*
			 * mReplyContentList.add(getResources().getString(
			 * R.string.multichoicedialogitem5));
			 */
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mReplyContentList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mReplyContentList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = mInflater.inflate(R.layout.reply_method_item, null);
			TextView textView = (TextView) view
					.findViewById(R.id.mReplyMethodItemText);
			textView.setText(mReplyContentList.get(position));
			if (position != 5) {
				TextView numView = (TextView) view.findViewById(R.id.num);
				numView.setText(++position + "");
			}
			view.setOnClickListener(SmsOnePersonReader.this);
			view.setTag(position);
			return view;
		}

	}

	private class MsgSentManager implements Runnable {

		private Context c;
		private ArrayList<String> list;
		private String mesgstr;

		public MsgSentManager(Context context, ArrayList<String> phonenos,
				String msgstr) {
			c = context;
			list = phonenos;
			this.mesgstr = msgstr;

		}

		public boolean sendMsgs(Context context, ArrayList<String> phonenos,
				String msgstr) {
			Long id = SetDataSource.storeStatics(context, phonenos.size(), msgstr);
			SetDataSource.mPhoneNumberList = list;
			SetDataSource.mPhoneNumberSize = list.size() - 1;
			SetDataSource.mPresentPhoneNumber = 0;
			SetDataSource.mPresentId = id;
			for (String str : phonenos) {
				sendOneMsg(context, str, msgstr);
			}
			return true;
		}
		
		public boolean sendOneMsg(Context context, String phoneno, String msgstr) {
			MobclickAgent.onEvent(context, SetDataSource.SMSCOUNT); 
			SmsManager smsManager = SmsManager.getDefault();
			Intent intent = new Intent(SENT_SMS_ACTION);
			PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
					intent, 0);
			ArrayList<PendingIntent> sendPIIntent = new ArrayList<PendingIntent>();
			sendPIIntent.add(sentPI);
			ArrayList<String> list = smsManager.divideMessage(msgstr); 
			if(list != null && list.size() > 0 && phoneno != null && phoneno.length() > 0){
				smsManager.sendMultipartTextMessage(phoneno, null, list, sendPIIntent, null);
			}
			return true;

		}

		public void run() {
			// TODO Auto-generated method stub
			sendMsgs(c, list, mesgstr);
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
