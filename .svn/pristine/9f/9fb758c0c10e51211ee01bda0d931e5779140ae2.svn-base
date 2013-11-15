package com.ginwave.smshelper;
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
import com.ginwave.smshelper.localsms.LocalSmsSendActivity;
import com.ginwave.smshelper.localsms.SmsOnePersonReader;
import com.ginwave.smshelper.localsms.SmsSearchReader;
import com.ginwave.smshelper.more.SetDataSource;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;

public class ManualReplyActivity extends Activity implements View.OnClickListener{
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
	private TextView mMainSetting;
	private static final int DIALOG_KEY = 0;
	private String mSearchContent = null;
	public static List<ConversationInfo> mConversationInfoPreviousList;
	public static List<ConversationInfo> mConversationInfoAdapterList;
	public static ConversationAdapter mConversationAdapter;
	
	private ScrollView mManualSettingPage;
	private TextView mManualSure;
	private TextView mManualCancel;
	private String[] mManualData;
	private EditText[] mManualContent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.manualreply);
		mInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);    
		mListview = (ListView) findViewById(R.id.listview);
		mConversationInfoPreviousList = new ArrayList<ConversationInfo>();
		mConversationInfoAdapterList = new ArrayList<ConversationInfo>();
		new GetMmsTask().execute("");
		mMainBack = (TextView)findViewById(R.id.mMainBack);
		mMainBack.setOnClickListener(this);
		mMainSetting = (TextView)findViewById(R.id.mMainSetting);
		mMainSetting.setText("手动回复设置");
		mMainSetting.setOnClickListener(this);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				/*showMultiChoiceDialog(((TextView) ((ViewGroup) arg1)
						.findViewById(R.id.recipient_textview)).getText().toString());*/
				TransportInfo lInfo = (TransportInfo)arg1.getTag();
				Intent lIntent = new Intent();
				Bundle lBundle = new Bundle();
				Log.i("xiao", "number = " + lInfo.getNumber());
				Log.i("xiao", "name = " + lInfo.getName());
				lBundle.putString("name", lInfo.getName());
				lBundle.putString("number", lInfo.getNumber());
				lBundle.putString("thread_id", lInfo.getThreadId());
				lIntent.putExtras(lBundle);
				lIntent.setClass(getApplicationContext(), SmsOnePersonReader.class);
				ManualReplyActivity.this.startActivity(lIntent);
			}
		});
		try{
			//testGetContacts();
		}
		catch(Exception e){
			Log.i("xiao", "Exception e = " + e.getMessage());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initSettingPage();
	}
	
	private void initSettingPage(){
		mManualSettingPage = (ScrollView)findViewById(R.id.mManualSettingPage);
		mManualSure = (TextView)findViewById(R.id.mManualSure);
		mManualCancel = (TextView)findViewById(R.id.mManualCancel);
		mManualContent = new EditText[5];
		mManualSure.setOnClickListener(this);
		mManualCancel.setOnClickListener(this);
		mManualData = new String[5];
		mManualContent[0] = (EditText)findViewById(R.id.mManualMethodItemOne);
		mManualContent[1] = (EditText)findViewById(R.id.mManualMethodItemTwo);
		mManualContent[2] = (EditText)findViewById(R.id.mManualMethodItemThree);
		mManualContent[3] = (EditText)findViewById(R.id.mManualMethodItemFour);
		mManualContent[4] = (EditText)findViewById(R.id.mManualMethodItemFive);
		setDataForSettingPage();
	}
	
	private void setDataForSettingPage(){
		for( int j = 0; j < mManualContent.length; j++){
			mManualData[j] = SetDataSource.getManualReplyContent(getApplicationContext(), SetDataSource.mManualReplyName[j]);
		}
		for( int i = 0; i < mManualContent.length; i++){
			mManualContent[i].setText(mManualData[i]);
		}
	}
	private ProgressDialog mProgressDialog = null;
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("短信加载中...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		// We save off the progress dialog in a field so that we can dismiss
		// it later. We can't just call dismissDialog(0) because the system
		// can lose track of our dialog if there's an orientation change.
		mProgressDialog = dialog;
		return dialog;
	}
	private void showProgress() {
		onCreateDialog(0, null);
		mProgressDialog.show();
	}

	/**
	 * Hides the progress UI for a lengthy operation.
	 */
	private void hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
	private class GetMmsTask extends AsyncTask<String, String, String> {
		private Cursor mCursor;
		private final int RECIPIENT_IDS = 3;
		private final int SNIPPET = 4;
		private String mRecipient;
		private String mSnippet;
		private String mNumber;
		private Long mSnippetCS;
		private String mName;
		private String mThreadId;
		public String doInBackground(String... params) {
			mCursor = getContentResolver().query(THREADS_URI,
					ALL_THREADS_PROJECTION, null, null, "date DESC");
			return "";
		}

		@Override
		protected void onPreExecute() {
			//showDialog(DIALOG_KEY);
			showProgress();
		}

		@Override
		public void onPostExecute(String Re) {
			Log.i("xiao", "onPostExecute new");
			ConversationInfo conver = new ConversationInfo();
			
			if(mCursor != null){
				mConversationInfoPreviousList.add(conver);
				while(mCursor.moveToNext()){
					mThreadId = mCursor.getString(ID);
					mRecipient = mCursor.getString(RECIPIENT_IDS);
					mSnippetCS = mCursor.getLong(SNIPPET_CS);
					if(mSnippetCS ==0){
						mSnippet = mCursor.getString(SNIPPET);
						Log.i("xiao", "mSnippetCS == 0 mSnippet = " + mSnippet);
					}
					else{
						try {
							mSnippet = new String(
									mCursor.getString(SNIPPET).getBytes("ISO8859_1"), "utf-8");
							Log.i("xiao", "mSnippetCS !! 0   mSnippet = " + mSnippet);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Cursor cr = getContentResolver().query(
							Uri.parse("content://mms-sms/canonical-addresses"),
							new String[] { "_id", "address" }, "_id=?",
							new String[] { mRecipient }, null);
					if(cr != null && cr.moveToFirst()){
						mNumber = cr.getString(1);
						mName = getContactNameByPhone(cr.getString(1));
						Log.i("xiao", "mName = " + mName);
						Log.i("xiao", "mNumber = " + mNumber);
						Log.i("xiao", "_id = " + cr.getInt(cr.getColumnIndex("_id")));
					}
					else{
						mNumber = "";
						mName = null;
					}
					ConversationInfo conversation = new ConversationInfo(mRecipient, mSnippet, mNumber, mSnippetCS, mName, mThreadId);
					mConversationInfoPreviousList.add(conversation);
				}
				for( int i = 0; i < mConversationInfoPreviousList.size(); i++){
					try {
						mConversationInfoAdapterList.add(mConversationInfoPreviousList.get(i).clone());
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				mCursor.close();
			}
			
			mConversationAdapter = new ConversationAdapter(getApplicationContext());
			mListview.setAdapter(mConversationAdapter); 
			//removeDialog(DIALOG_KEY);
			hideProgress();
		}
		
		public String getContactNameByPhone(String number) {
			String name = "";
			Uri personUri = Uri.withAppendedPath(
					ContactsContract.PhoneLookup.CONTENT_FILTER_URI, number);
			
			try{
				Cursor cur = getContentResolver().query(personUri,
		
					new String[] { PhoneLookup.DISPLAY_NAME },
					null, null, null );
			if(cur != null){
				if( cur.moveToFirst() ) {
					int nameIdx = cur.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			
					name = cur.getString(nameIdx);
					
				}
				cur.close();
			}
			}
			catch(Exception e){
				Log.i("xiao", "" + e.getMessage());
			}
			return name;
		}
	}
	
	public class SearchTextWatcher implements TextWatcher{

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
		
		public void handleAdapterData(String pContent){
			mSearchContent = pContent;
			List<ConversationInfo> lConversationList = new ArrayList<ConversationInfo>();
			ConversationInfo lInfo = null;
			ConversationInfo conversation = new ConversationInfo();
			lConversationList.add(conversation);
			if(pContent != null && pContent.length() > 0){
				for( int i = 1; i < mConversationInfoPreviousList.size(); i++){
					lInfo = mConversationInfoPreviousList.get(i);
					if(lInfo.mName.contains(pContent) || lInfo.mNumber.contains(pContent)){
						try {
							lConversationList.add(lInfo.clone());
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			else{
				for( int j = 1; j < mConversationInfoPreviousList.size(); j++){
					lInfo = mConversationInfoPreviousList.get(j);
					try {
						lConversationList.add(lInfo.clone());
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			mConversationInfoAdapterList.clear();
			mConversationInfoAdapterList = lConversationList;
			mConversationAdapter.notifyDataSetChanged();
		}
		
	}
	
	public static List<ConversationInfo> cloneConversationList(){
		List<ConversationInfo> conversationList = new ArrayList<ConversationInfo>();
		ConversationInfo info = null;
		for( int j = 1; j < mConversationInfoPreviousList.size(); j++){
			info = mConversationInfoPreviousList.get(j);
			try {
				conversationList.add(info.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return conversationList;
	}
	
	public static List<ConversationInfo> cloneConversationList(List<ConversationInfo> pInfoList){
		List<ConversationInfo> conversationList = new ArrayList<ConversationInfo>();
		ConversationInfo info = null;
		for( int j = 1; j < pInfoList.size(); j++){
			info = pInfoList.get(j);
			try {
				conversationList.add(info.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return conversationList;
	}
	private String subStringEleven(String orgin) {
		if(orgin.length() > 11) {
			return orgin.substring(0, 11) + "...";
		} else {
			return orgin;
		}
		
	}
	public class ConversationAdapter extends BaseAdapter{
		private ViewHolder mHolder = null;
		private ConversationInfo mInfo = null;
		private Context mContext;
		private TransportInfo mTransportInfo;
		public ConversationAdapter(Context pContext){
			mContext = pContext;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mConversationInfoAdapterList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mConversationInfoAdapterList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(position == 0){
				convertView = mInflater.inflate(R.layout.listforlocalsmsitemheader, null,
						false);
				EditText editText = (EditText)convertView.findViewById(R.id.mLocalSearchView);
				editText.setText(mSearchContent);
				/*editText.setOnClickListener(SmsReader.this);*/
				editText.setOnClickListener(ManualReplyActivity.this);
				//editText.addTextChangedListener(new SearchTextWatcher());
			}
			else{
				
				mInfo = mConversationInfoAdapterList.get(position);
				if(convertView == null || (convertView != null && convertView.getId() == R.id.mHead)){
					convertView = mInflater.inflate(R.layout.listforlocalsmsitem, null,
							false);
					mHolder = new ViewHolder();
				}
				mHolder.mLocalSmsIconText = (TextView)convertView.findViewById(R.id.mLocalSmsIconText);
				mHolder.mRecipientTime = (TextView)convertView.findViewById(R.id.mRecipientTime);
				mHolder.mRecipientTextView = (TextView)convertView.findViewById(R.id.mRecipientTextview);
				mHolder.mSnippetTextView = (TextView)convertView.findViewById(R.id.mSnippetTextview);
				if(mInfo.mName != null && mInfo.mName.length() > 0){
					mTransportInfo = new TransportInfo(mInfo.mNumber, mInfo.mName, mInfo.mThreadId);
					mHolder.mLocalSmsIconText.setText(mInfo.mName.subSequence(0, 1));
					mHolder.mLocalSmsIconText.setBackgroundResource(R.drawable.bg_photo_normal);
					mHolder.mRecipientTextView.setText(mInfo.mName);
				}
				else{
					mTransportInfo = new TransportInfo(mInfo.mNumber, "", mInfo.mThreadId);
					mHolder.mLocalSmsIconText.setText("");
					mHolder.mLocalSmsIconText.setBackgroundResource(R.drawable.bg_photo_default);
					mHolder.mRecipientTextView.setText(subStringEleven(mInfo.mNumber));
				}
				mHolder.mSnippetTextView.setText(mInfo.mSnippet);
				/*if(mInfo.mSnippetCS == 0){
					
				}
				else{
					try {
						mHolder.mSnippetTextView.setText(new String(mInfo.mSnippet
								.getBytes("ISO8859_1"), "utf-8"));
					} catch (UnsupportedEncodingException e) {
					}
				}*/
				convertView.setTag(mTransportInfo);
			}
			return convertView;
		}
		
		public final class ViewHolder{
			  public TextView  mLocalSmsIconText;
			  public TextView  mRecipientTime;
			  public TextView  mRecipientTextView;
			  public TextView  mSnippetTextView;
		} 
		
		public String getContactNameByPhone(String number) {
			String name = null;
			Uri personUri = Uri.withAppendedPath(
					ContactsContract.PhoneLookup.CONTENT_FILTER_URI, number);
			Cursor cur = mContext.getContentResolver().query(personUri,
					new String[] { PhoneLookup.DISPLAY_NAME },
					null, null, null );
			if( cur.moveToFirst() ) {
				int nameIdx = cur.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			
				name = cur.getString(nameIdx);
				cur.close();
			}
			return name;
		}
		
		public String getDisplayNameByNumber(String pNumber){
			String name = null;
			Uri uri=Uri.parse("content://com.android.contacts/data/phones");
			ContentResolver resolver = mContext.getContentResolver();
			Cursor phoneCursor=resolver.query(uri, null, "data1=?", new String[]{pNumber},null);
			Log.i("xiao", "number = " + pNumber + " size = " + pNumber.length());
			//Log.i("xiao", "phoneCursor size = " + phoneCursor.getCount());
			if(phoneCursor != null){
				while(phoneCursor.moveToNext()){
					for( int i = 0; i < phoneCursor.getColumnCount(); i++){
						//Log.i("xiao", "Column = " + phoneCursor.getColumnName(i));
					}
					Log.i("xiao", "contactid = " + phoneCursor.getInt(phoneCursor.getColumnIndex("contact_id")));
					String phone=phoneCursor.getString(phoneCursor.getColumnIndex("data1"));
					Log.i("xiao", "phone = " + phone);
					name = phoneCursor.getString(phoneCursor.getColumnIndex("display_name"));
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
		    int threadId=0; 
		    //��ݵ绰����ȥ���� 
		    ContentResolver cr = mContext.getContentResolver(); 
		    String[] projection = new String[] { THREAD_ID, ADDRESS }; 
		 
		    Cursor cur = cr.query(URI, projection, ADDRESS + " = ?", 
		            new String[] { number }, null);// asc 
		 
		    int threadIdColumn = cur.getColumnIndex(THREAD_ID); 
		    if (cur != null) { 
		 
		        while (cur.moveToNext()) { 
		            //��ûỰ��thread_id 
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
	//��ö���_id ���� ���� �������� ʱ�� �� ����
	        String[] projection = new String[] { "_id", ADDRESS, "person", "body", 
	                "date", "type" }; 
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
	            	Log.i("xiao", "name = " + cur.getString(nameFieldColumnIndex)); 
	            	Log.i("xiao", "number = " + cur.getString(numberFieldColumnIndex)); 
	            	Log.i("xiao", "body = " + cur.getString(smsbodyColumn)); 
	            	Log.i("xiao", "date = " + cur.getLong(dateColumn)); 
	            	Log.i("xiao", "type = " + cur.getInt(typeColumn));
	            } 
	            cur.close(); 
	        } 
	    } 
	}

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SmsReader.this);
			builder.setTitle(R.string.exitdialog_title);
			builder.setMessage(R.string.exitdialog_content);
			builder.setPositiveButton(R.string.exitdialog_ok,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							SmsReader.this.finish();
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
	}*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*if(v instanceof RelativeLayout && ((RelativeLayout)v).getId() == R.id.mHead){
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), SmsSearchReader.class);
			this.startActivity(intent);
			return ;
		}*/
		if(v.equals(mManualSure)){
			mManualSettingPage.setVisibility(View.INVISIBLE);
			String content = "";
			for( int i = 0; i < mManualContent.length; i++){
				content = mManualContent[i].getText().toString();
				if(content != null && content.length() > 0){
					SetDataSource.setManualReplyContent(getApplicationContext(), SetDataSource.mManualReplyName[i], content);
				}
			}
			/*for( int i = 0; i < mManualContentList.getChildCount(); i++){
				editText = (EditText)mManualContentList.getChildAt(i).findViewById(R.id.mManualMethodItemText);
				value = editText.getText().toString();
				if(value != null && value.length() > 0){
					SetDataSource.setManualReplyContent(getApplicationContext(), SetDataSource.mManualReplyName[i], value);
				}
			}*/
			return ;
		}
		if(v.equals(mManualCancel)){
			mManualSettingPage.setVisibility(View.INVISIBLE);
			return ;
		}
		if(v.equals(mMainSetting)){
			setDataForSettingPage();
			mManualSettingPage.setVisibility(View.VISIBLE);
			return ;
		}
		if(v instanceof EditText){
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), SmsSearchReader.class);
			this.startActivity(intent);
			return ;
		}
		if(v.equals(mMainBack)){
			this.finish();
			return ;
		}
		else{
			int position = (Integer)v.getTag();
			TextView textView = (TextView)v.findViewById(R.id.mReplyMethodItemText);
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			switch(position){
			case 0:
			case 1:
			case 2:
				intent.setClass(getApplicationContext(),
					LocalSmsSendActivity.class);
				bundle.putString(
					"xm", textView.getText().toString());
				bundle.putString("phonenumber", mPhoneNumber);
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case 3:
				intent.setClass(ManualReplyActivity.this,
					HolidayGridChooseSMS.class);
				intent.putExtra("phonenumber", mPhoneNumber);
				ManualReplyActivity.this.startActivity(intent);
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
	
	public class ManualTextWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
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
