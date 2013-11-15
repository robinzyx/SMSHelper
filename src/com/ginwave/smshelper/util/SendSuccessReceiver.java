package com.ginwave.smshelper.util;


import com.ginwave.smshelper.R;
import com.ginwave.smshelper.more.SendMessageService;
import com.ginwave.smshelper.more.SetDataSource;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SendSuccessReceiver extends BroadcastReceiver {
	private boolean mHasNotified = false;
	private boolean mDoNotNotifyAnyMore = false;
	
	public void setNotifyFlag(){
		mHasNotified = true;
		mDoNotNotifyAnyMore = true;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
//		Toast.makeText(context,"SendMessage:"+intent.getStringExtra("number"),1).show();
		Log.i("xiao", "onReceive");
		long id = intent.getLongExtra("id", -1);
		String number = intent.getStringExtra("number");
		boolean isLast = intent.getBooleanExtra("islast", false);
		boolean notifyFlag = intent.getBooleanExtra("notifyFlag", false);
		Log.i("xiao", "id = " + id);
		Log.i("xiao", "number = " + number + " isLast = " + isLast);
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			Log.i("xiao", "okkkkkkkkkkkkk mHasNotified = " + mHasNotified);
			if(mHasNotified){
				if(mDoNotNotifyAnyMore){
					Log.i("xiao", "timess");
					Toast.makeText(context,
						context.getResources().getString(R.string.sendsuccess_by_multi),
						Toast.LENGTH_SHORT).show();
					mDoNotNotifyAnyMore = false;
				}
			}
			else{
				Toast.makeText(context,
					context.getResources().getString(R.string.sendsuccess),
					Toast.LENGTH_SHORT).show();
			}
			if(SetDataSource.mPresentPhoneNumber <= SetDataSource.mPhoneNumberSize){
//				SetDataSource.storeStaticsDetails(context, SetDataSource.mPresentId, SetDataSource.mPhoneNumberList.get(SetDataSource.mPresentPhoneNumber), true);
				//edit by yongbinbin 2013/6/20
			
			}
			//if(SetDataSource.mPresentPhoneNumber == SetDataSource.mPhoneNumberSize){
				((Activity)context).finish();
			//}
			break;
		default:
			Log.i("xiao", "defffffffffffffff");
			Toast.makeText(context,
					context.getResources().getString(R.string.sendfailed),
					Toast.LENGTH_SHORT).show();
			if(SetDataSource.mPresentPhoneNumber <= SetDataSource.mPhoneNumberSize){
				SetDataSource.storeStaticsDetails(context, SetDataSource.mPresentId, SetDataSource.mPhoneNumberList.get(SetDataSource.mPresentPhoneNumber), true);
			}
			//if(SetDataSource.mPresentPhoneNumber == SetDataSource.mPhoneNumberSize){
				((Activity)context).finish();
			//}
			break;
		}
		SetDataSource.mPresentPhoneNumber++;
		// Toast.makeText(context,
		// context.getResources().getString(R.string.sendsuccess),
		// Toast.LENGTH_SHORT).show();
	}

}
