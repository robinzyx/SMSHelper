package com.ginwave.smshelper.util;




import com.ginwave.smshelper.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SendSuccessToastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i("xiao", "onReceive");
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			Toast.makeText(context,
					context.getResources().getString(R.string.sendsuccess),
					Toast.LENGTH_SHORT).show();
			break;
		default:
			Toast.makeText(context,
					context.getResources().getString(R.string.sendfailed),
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
