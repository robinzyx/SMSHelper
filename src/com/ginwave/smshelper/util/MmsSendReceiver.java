package com.ginwave.smshelper.util;

import java.util.ArrayList;
import java.util.List;


import com.ginwave.smshelper.more.SendMessageService;
import com.ginwave.smshelper.more.SetDataSource;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class MmsSendReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		boolean isSuccess = intent.getBooleanExtra("isSuccess", false);
		String subject = intent.getStringExtra("subject");
		Log.i("xiao", "subject = " + subject);
		String number = intent.getStringExtra("number");
		List<String> phones = new ArrayList<String>();
		phones.add(number);
		Long id = SetDataSource.storeStatics(context, phones.size(), subject);
		if(isSuccess){
			/*Toast.makeText(context, context.getResources().getString(R.string.mms_send_success),
				Toast.LENGTH_SHORT).show();*/
			SetDataSource.storeStaticsDetails(context, id, number, true);
		}
		else{
			SetDataSource.storeStaticsDetails(context, id, number, false);
			/*Toast.makeText(context, context.getResources().getString(R.string.mms_send_error),
					Toast.LENGTH_SHORT).show();*/
		}
	}

}
