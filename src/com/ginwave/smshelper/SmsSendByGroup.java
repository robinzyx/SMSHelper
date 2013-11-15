package com.ginwave.smshelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import com.ginwave.smshelper.HolidayGridChooseSMS;
import com.ginwave.smshelper.R;
import com.ginwave.smshelper.localsms.SmsOnePersonReader;
import com.ginwave.smshelper.more.SendMessageService;
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
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SmsSendByGroup extends Activity implements View.OnClickListener{
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
	private LayoutInflater mInflater;
	private AlertDialog mAlterDialog;
	private TextView mMainBack;
	private TextView mChooseMms;
	private EditText mMmsContent;
	private EditText mMmsNumber;
	private TextView mSendMms;
	private static final int DIALOG_KEY = 0;
	private Cursor mCursor;
	private String mPersonNumber = "";
	private static final String SENT_SMS_ACTION = "com.ginwave.SENT_SMS_ACTION";
	private SendSuccessReceiver mReceiver;
	/*private ProgressBar mSendProgress;
	private int mPresentSendProgress;
	private ProgressHandler mProgressHandler;
	private AlertDialog mProgressDialog;*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sendbygroup);
		mInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);    
		Intent lIntent = this.getIntent();
		if(lIntent != null){
			Bundle lBundle = lIntent.getExtras();
			if(lBundle != null){
				mPersonNumber = lBundle.getString("number");
			}
		}
		mMainBack = (TextView)findViewById(R.id.mMainBack);
		mChooseMms = (TextView)findViewById(R.id.mChooseMms);
		mMmsContent = (EditText)findViewById(R.id.mMmsContent);
		mSendMms = (TextView)findViewById(R.id.mSendMms);
		mMmsNumber = (EditText)findViewById(R.id.mMmsNumber);
		mMainBack.setOnClickListener(this);
		mChooseMms.setOnClickListener(this);
		mSendMms.setOnClickListener(this);
		mMmsNumber.setText(mPersonNumber);
		//mProgressHandler = new ProgressHandler();
		initSemdByGroupDialog();
		mReceiver = new SendSuccessReceiver();
		IntentFilter sendFilter = new IntentFilter();
		sendFilter.addAction(SENT_SMS_ACTION);
		mReceiver.setNotifyFlag();
		this.registerReceiver(mReceiver, sendFilter);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(data != null){
			Bundle bundle = data.getExtras();
			if(bundle != null){
				String text = bundle.getString("content");
				mMmsContent.setText(text);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.equals(mMainBack)){
			this.finish();
			return ;
		}
		if(v.equals(mChooseMms)){
			Intent lIntent = new Intent();
			showMultiChoiceDialog(mPersonNumber);
			return ;
		}
		if(v.equals(mSendMms)){
			mPersonNumber = mMmsNumber.getText().toString();
			String message = mMmsContent.getText().toString();
			/*Message messageItem = new Message();
			messageItem.what = 0;
			mProgressHandler.sendMessage(messageItem);*/
			if (mPersonNumber.equals("")) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.send_toast),
						Toast.LENGTH_SHORT).show();
				return;
			}
			String[] mPersonNumbers = mPersonNumber.split(",");
			ArrayList<String> nums = new ArrayList<String>();
			for( int i = 0; i < mPersonNumbers.length; i++){
				Log.i("xiao", "numbers = " + i + " value = " + mPersonNumbers[i]);
				nums.add(mPersonNumbers[i]);
			}
			
			new Thread(new MsgSentManager(getApplicationContext(), nums,
					message)).start();
			Toast.makeText(this,
					this.getString(R.string.send_sending), Toast.LENGTH_LONG)
					.show();
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
			case 3:
			case 4:
				/*intent.setClass(getApplicationContext(),
					LocalSmsSendActivity.class);
				bundle.putString(
					"xm", textView.getText().toString());
				bundle.putString("phonenumber", mPhoneNumber);
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);*/
				mMmsContent.setText(textView.getText().toString());
				break;
			case 5:
				intent.setClass(this,
					HolidayGridChooseSMS.class);
				intent.putExtra("phonenumber", mPersonNumber);
				this.startActivityForResult(intent, 0);
				break;
			/*case 4:
				intent.setClass(getApplicationContext(),
					LocalSmsSendActivity.class);
				bundle.putString("xm", "");
				bundle.putString("phonenumber", mPhoneNumber);
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;*/
			}
			mAlterDialog.dismiss();
		}
	}
	
	
	
	private void initSemdByGroupDialog(){
		/*AlertDialog.Builder builder = new AlertDialog.Builder(
				SmsSendByGroup.this);
		builder.setTitle(getResources().getString(
				R.string.multichoicedialogtitle));
		View view = mInflater.inflate(R.layout.progress_bar_layout, null);
		mSendProgress = (ProgressBar)view.findViewById(R.id.mMmsSendProgress);
		builder.setView(view);
		builder.setCancelable(true);
		mProgressDialog = builder.create();*/
		
	}
	
	private void showSendByGroupDialog(int pMax){
		Log.i("xiao", "pMax = " + pMax);
		//mSendProgress.setMax(pMax);
		//mProgressDialog.show();
	}
	
	private void dismissSendByGroupDialog(){
		//mProgressDialog.dismiss();
	}
	
	private void showMultiChoiceDialog(String phonenum) {
		mPersonNumber = phonenum;
		View view = mInflater.inflate(R.layout.reply_method_layout, null);
		ListView listView = (ListView)view.findViewById(R.id.mReplyList);
		listView.setAdapter(new ReplyMethodAdapter());
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SmsSendByGroup.this);
		builder.setTitle(getResources().getString(
				R.string.multichoicedialogtitle));
		
		builder.setView(view);
		builder.setCancelable(true);
		mAlterDialog = builder.create();
		mAlterDialog.show();
	}
	
	public class ReplyMethodAdapter extends BaseAdapter{
		
		private List<String> mReplyContentList;
		
		public ReplyMethodAdapter(){
			mReplyContentList = new ArrayList<String>();
			/*mReplyContentList.add(getResources().getString(
					R.string.multichoicedialogitem1));
			mReplyContentList.add(getResources().getString(
					R.string.multichoicedialogitem2));
			mReplyContentList.add(getResources().getString(
					R.string.multichoicedialogitem3));
			mReplyContentList.add(getResources().getString(
					R.string.multichoicedialogitem4));*/
			for( int i = 0; i < 5; i++){
				mReplyContentList.add(SetDataSource.getManualReplyContent(getApplicationContext(), SetDataSource.mManualReplyName[i]));
			}
			mReplyContentList.add(mReplyContentList.size(), getApplicationContext().getString(R.string.multichoicedialogitem6));
			/*mReplyContentList.add(getResources().getString(
					R.string.multichoicedialogitem5));*/
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
			TextView textView = (TextView)view.findViewById(R.id.mReplyMethodItemText);
			textView.setText(mReplyContentList.get(position));
			view.setOnClickListener(SmsSendByGroup.this);
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
		//edit by yongbinbin 2013/6/20		
				SetDataSource.storeStaticsDetails(context, SetDataSource.mPresentId, str, true);
				
			}
			return true;
		}
		
		public boolean sendOneMsg(Context context, String phoneno, String msgstr) {
			MobclickAgent.onEvent(context, SetDataSource.SMSCOUNT); 
			SmsManager smsManager = SmsManager.getDefault();
			Intent intent = new Intent(SENT_SMS_ACTION);
			PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
					intent, 0);
			intent.putExtra("notifyFlag", true);
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
	
	class ProgressHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Log.i("xiao", "handler Message msg.what = " + msg.what);
			switch(msg.what){
			case 0:
				//showSendByGroupDialog(100);
				break;
			case 1:
				//dismissSendByGroupDialog();
				break;
			default:
				Log.i("xiao", "default ");
				//Log.i("xiao", "mPresentSendProgress = " + mPresentSendProgress);
				//mSendProgress.setProgress(mPresentSendProgress);
				break;
			}
			
			super.handleMessage(msg);
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
