package com.ginwave.smshelper.more;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.ginwave.smshelper.ReplyRecordProvider;
import com.ginwave.smshelper.SmsSendByGroup;
import com.ginwave.smshelper.localsms.LocalSmsSendActivity;
import com.umeng.analytics.MobclickAgent;

public class MessageReceiver extends BroadcastReceiver {

	private String tag = "xiao";
	private static final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	private Handler mHandler = new Handler();
	private ContentResolver mReplyRecordResolver;
	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		SmsMessage[] msg = null;
		mContext = context;
		mReplyRecordResolver = context.getContentResolver();
		boolean autoEnable = SetDataSource.getAutoReplyFlag(context);
		Log.i("xiao", "autoEnable is " + autoEnable);
		// DataProvider dp=new DataProvider();
		/*
		 * Context context2=null; try { context2 =
		 * context.createPackageContext("benbmw.max.messagePurification",
		 * Context.CONTEXT_IGNORE_SECURITY); } catch (NameNotFoundException e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		String[] keywords = { "节日快乐", "值此", "佳节", "衷心", "祝", "愿", "祝愿", "恭喜",
				"节日", "前途", "思念", "问候", "礼物", "顺", "棒", "好", "兴", "笑", "美满",
				"祝福", "平安", "吉祥", "快乐", "开心", "如意", "发财", "健康", "幸福", "团圆",
				"安康", "好运", "美好", "欢笑", "欢喜", "顺利" };

		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {

			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdusObj = (Object[]) bundle.get("pdus");
				msg = new SmsMessage[pdusObj.length];
				Log.i(tag, "pdusObj.length = " + pdusObj.length);
				for (int i = 0; i < pdusObj.length; i++)
					msg[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
			}

			for (int i = 0; i < msg.length; i++) {
				if(msg[i] == null){
					continue;
				}
				String msgTxt = msg[i].getMessageBody();
				Log.i(tag, "i = " + i);
				Log.i(tag, "number = " + msg[i].getDisplayOriginatingAddress());

				for (int j = 0; j < keywords.length; j++) {
					if (msgTxt.indexOf(keywords[j]) >= 0) {
						Log.i(tag, "start auto1");
						if (autoEnable) {
							if(!checkPhoneNumber(msg[i].getDisplayOriginatingAddress())){
								Log.i(tag, "checkPhoneNumber illegal");
								return ;
							}
							else{
								Log.i(tag, "checkPhoneNumber legal");
							}
							if(!checkPresentTimeInSettedValue()){
								Log.i(tag, "out of setted date");
								return ;
							}
							else{
								Log.i(tag, "in setted date");
							}
							if(IsReplyInTwentyFourHour(msg[i].getDisplayOriginatingAddress())){
								Log.i(tag, "ReplyInTwentyFourHour");
								return ;
							}
							else{
								Log.i(tag, "storeReplyTime");
								storeReplyTimeToDB(msg[i].getDisplayOriginatingAddress());
							}
							Log.i(tag, "before");
							printDB();
							Log.i(tag, "after");
							cleanOverTimedDB();
							printDB();
							Log.v("heiheifang", "start auto2");
							// msg[i].getDisplayOriginatingAddress(),
							// null, SetDataSource.mAutoReplyMessage,
							// null, null);
							ArrayList<String> nums = new ArrayList<String>();

							nums.add(msg[i].getDisplayOriginatingAddress());

							mHandler.postDelayed(new MsgSentManager(context,
									nums, SetDataSource.mAutoReplyMessage),
									Long.parseLong(SetDataSource
											.getAutoReplyTime(context)) * 1000);
							// new Thread(new MsgSentManager(context, nums,
							// SetDataSource.mAutoReplyMessage)).start();
						}
						break;
					}
				}
			}

		}
	}
	
	private boolean checkPresentTimeInSettedValue(){
		if(SetDataSource.getAutoReplyByTimeFlag(mContext)){
			YearMonthObject beginObject = new YearMonthObject();
			YearMonthObject endObject = new YearMonthObject();
			YearMonthObject presentObject = new YearMonthObject();
			beginObject.mYear = SetDataSource.getAutoReplySettedTime(mContext, SetDataSource.AUTOREPLY_BEGIN_YEAR);
			if(beginObject.mYear < 0){
				return true;
			}
			else{
				beginObject.mMonth = SetDataSource.getAutoReplySettedTime(mContext, SetDataSource.AUTOREPLY_BEGIN_MONTH);
				beginObject.mDay = SetDataSource.getAutoReplySettedTime(mContext, SetDataSource.AUTOREPLY_BEGIN_DAY);
				endObject.mYear = SetDataSource.getAutoReplySettedTime(mContext, SetDataSource.AUTOREPLY_END_YEAR);
				if(endObject.mYear < 0){
					return true;
				}
				else{
					endObject.mMonth = SetDataSource.getAutoReplySettedTime(mContext, SetDataSource.AUTOREPLY_END_MONTH);
					endObject.mDay = SetDataSource.getAutoReplySettedTime(mContext, SetDataSource.AUTOREPLY_END_DAY);
					presentObject = SetDataSource.getPresentYearMonth();
					Log.i("xiao", "begin year = " + beginObject.mYear + " month = " + beginObject.mMonth + " day = " + beginObject.mDay);
					Log.i("xiao", "present year = " + presentObject.mYear + " month = " + presentObject.mMonth + " day = " + presentObject.mDay);
					Log.i("xiao", "end year = " + endObject.mYear + " month = " + endObject.mMonth + " day = " + endObject.mDay);
					Date beginDate = new Date(beginObject.mYear, beginObject.mMonth, beginObject.mDay);
					Date presentDate =  new Date(presentObject.mYear, presentObject.mMonth, presentObject.mDay);
					Date endDate = new Date(endObject.mYear, endObject.mMonth, endObject.mDay);
					if(presentDate.getTime() >= beginDate.getTime() && presentDate.getTime() <= endDate.getTime()){
						return true;
					}
					else{
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private boolean checkPhoneNumber(String pNumber){
		if(pNumber.length() < 11){
			return false;
		}
		else{
			return true;
		}
	}
	
	private void printDB(){
		Cursor c = mReplyRecordResolver.query(ReplyRecordProvider.CONTENT_URI, null, null, null, null);
		int i = 0;
		String mNumber;
		int mYear;
		int mMonth;
		int mDay;
		while (c != null && c.moveToNext()) {
			mNumber = c.getString(c
					.getColumnIndex(ReplyRecordProvider.DataColumn.COLUMN_NUMBER));
			mYear = c.getInt(c
					.getColumnIndex(ReplyRecordProvider.DataColumn.COLUMN_YEAR));
			mMonth = c.getInt(c
					.getColumnIndex(ReplyRecordProvider.DataColumn.COLUMN_MONTH));
			mDay = c.getInt(c
					.getColumnIndex(ReplyRecordProvider.DataColumn.COLUMN_DAY));
			Log.i(tag, "i = " + i + " mNumber = " + mNumber);
			Log.i(tag, "i = " + i + " mYear = " + mYear);
			Log.i(tag, "i = " + i + " mMonth = " + mMonth);
			Log.i(tag, "i = " + i + " mDay = " + mDay);
			i++;
		}
		if(c != null){
			c.close();
		}
	}
	
	private void cleanOverTimedDB(){
		Cursor c = mReplyRecordResolver.query(ReplyRecordProvider.CONTENT_URI, null, null, null, null);
		YearMonthObject storedYearMonthObject = new YearMonthObject();
		YearMonthObject presentYearMonthObject = SetDataSource.getPresentYearMonth();
		while(c != null && c.moveToNext()){
				storedYearMonthObject.mId = c.getInt(c.getColumnIndex(ReplyRecordProvider.DataColumn.COLUMN_ID));
				storedYearMonthObject.mYear = c.getInt(c.getColumnIndex(ReplyRecordProvider.DataColumn.COLUMN_YEAR));
				storedYearMonthObject.mMonth = c.getInt(c.getColumnIndex(ReplyRecordProvider.DataColumn.COLUMN_MONTH));
				storedYearMonthObject.mDay = c.getInt(c.getColumnIndex(ReplyRecordProvider.DataColumn.COLUMN_DAY));
				if(presentYearMonthObject.mYear >= storedYearMonthObject.mYear &&
						presentYearMonthObject.mMonth >= storedYearMonthObject.mMonth && 
						presentYearMonthObject.mDay > storedYearMonthObject.mDay){
					mReplyRecordResolver.delete(ReplyRecordProvider.CONTENT_URI, ReplyRecordProvider.DataColumn.COLUMN_ID + " = ? ", new String[]{Integer.toString(storedYearMonthObject.mId)});
				}
		}
	}
	
	private void storeReplyTimeToDB(String pNumber){
		YearMonthObject presentYearMonthObject = SetDataSource.getPresentYearMonth();
		ContentValues values = new ContentValues();
		values.put(ReplyRecordProvider.DataColumn.COLUMN_NUMBER,
				pNumber);
		values.put(ReplyRecordProvider.DataColumn.COLUMN_YEAR, presentYearMonthObject.mYear);
		values.put(ReplyRecordProvider.DataColumn.COLUMN_MONTH, presentYearMonthObject.mMonth);
		values.put(ReplyRecordProvider.DataColumn.COLUMN_DAY, presentYearMonthObject.mDay);
		mReplyRecordResolver.insert(
				ReplyRecordProvider.CONTENT_URI, values);
	}
	
	private boolean IsReplyInTwentyFourHour(String pNumber){
		Cursor c = mReplyRecordResolver.query(ReplyRecordProvider.CONTENT_URI, null, null, null, null);
		String number;
		YearMonthObject storedYearMonthObject = new YearMonthObject();
		YearMonthObject presentYearMonthObject = SetDataSource.getPresentYearMonth();
		while(c != null && c.moveToNext()){
			number = c.getString(c.getColumnIndex(ReplyRecordProvider.DataColumn.COLUMN_NUMBER));
			if(number.equals(pNumber)){
				storedYearMonthObject.mYear = c.getInt(c.getColumnIndex(ReplyRecordProvider.DataColumn.COLUMN_YEAR));
				storedYearMonthObject.mMonth = c.getInt(c.getColumnIndex(ReplyRecordProvider.DataColumn.COLUMN_MONTH));
				storedYearMonthObject.mDay = c.getInt(c.getColumnIndex(ReplyRecordProvider.DataColumn.COLUMN_DAY));
				if(presentYearMonthObject.mYear == storedYearMonthObject.mYear && 
						presentYearMonthObject.mMonth == storedYearMonthObject.mMonth &&
						presentYearMonthObject.mDay == storedYearMonthObject.mDay){
					return true;
				}
			}
		}
		if(c != null){
			c.close();
		}
		return false;
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
			
			SetDataSource.mPhoneNumberList = list;
			SetDataSource.mPhoneNumberSize = list.size() - 1;
			SetDataSource.mPresentPhoneNumber = 0;
			
			for (String str : phonenos) {
				Long id = SetDataSource.storeStatics(context, phonenos.size(), msgstr, str);
				SetDataSource.mPresentId = id;
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

}
