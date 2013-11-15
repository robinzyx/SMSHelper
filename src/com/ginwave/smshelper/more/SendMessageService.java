package com.ginwave.smshelper.more;

import java.util.ArrayList;

import com.ginwave.smshelper.SmsSendByGroup;
import com.umeng.analytics.MobclickAgent;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;

public class SendMessageService extends Service {
	private static final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	private SendMessageHandler mSendMessageHandler;
	private Long mId;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mSendMessageHandler = new SendMessageHandler();
		mId = SetDataSource.storeStatics(this, SetDataSource.mPhoneNumberList.size(), SetDataSource.mMessage);
		mSendMessageHandler.sendEmptyMessage(0);
	}
	
	class SendMessageHandler extends Handler{
		int i = 0;
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				Log.i("xiao", "message == 0 i = " + i);
				if(i == SetDataSource.mPhoneNumberList.size() - 1){
					mSendMessageHandler.sendEmptyMessageDelayed(0, 1000);
					sendOneMsg(SendMessageService.this, SetDataSource.mPhoneNumberList.get(i), SetDataSource.mMessage, mId, true);
				}
				else if(i < SetDataSource.mPhoneNumberList.size() - 1){
					mSendMessageHandler.sendEmptyMessageDelayed(0, 1000);
					sendOneMsg(SendMessageService.this, SetDataSource.mPhoneNumberList.get(i), SetDataSource.mMessage, mId, false);
				}
				else{
					mSendMessageHandler.sendEmptyMessageDelayed(1, 1000);
				}
				i++;
				break;
			case 1:
				Log.i("xiao", "message == 1");
				break;
			}
		}
}
	
	public boolean sendMsgs(Context context, ArrayList<String> phonenos,
			String msgstr) {
		Log.v("fang", "sendMsgs");
		//mPresentSendProgress = 0;
		
		Log.i("xiao", "size = " + phonenos.size());
		Long id = SetDataSource.storeStatics(context, phonenos.size(), msgstr);
		//SetDataSource.storeStatics(context, phonenos.size(), msgstr, SetDataSource.getFormattedPhoneNumber(phonenos));
		int i = 0;
		for (String str : phonenos) {
			//mPresentSendProgress++;
			//Log.i("xiao", "mPresentProgress = " + mPresentSendProgress);
			//mProgressHandler.sendEmptyMessage(mPresentSendProgress, 2000);
			/*Message message = new Message();
			message.what = 10;
			mProgressHandler.sendMessageDelayed(message, 1000);*/
			Log.i("xiao", "i = " + i + " str = " + str + " id = " + id);
			if(i == phonenos.size() - 1){
				Log.i("xiao", "true");
				sendOneMsg(context, str, msgstr, id, true);
			}
			else{
				Log.i("xiao", "false");
				sendOneMsg(context, str, msgstr, id, false);
			}
			i++;
		}
		Message dismiss = new Message();
		dismiss.what = 1;
		//mProgressHandler.sendMessageDelayed(dismiss, 1000);
		//
		return true;
	}
	
	public boolean sendOneMsg(Context context, String phoneno, String msgstr, Long id, boolean pIsLast) {
		Log.v("fang", "sendOneMsg");
		MobclickAgent.onEvent(this, SetDataSource.SMSCOUNT); 
		SmsManager smsManager = SmsManager.getDefault();
		Intent intent = new Intent(SENT_SMS_ACTION);
		intent.putExtra("id", id);
		intent.putExtra("number", phoneno);
		intent.putExtra("islast", pIsLast);
		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// // DeliverPI涓轰簡鑾峰緱瀵规柟鎺ュ彈鍒颁箣鍚庤繑鍥炵殑鎶ュ憡鐨�
		// //
		// 鎺ユ敹鎶ュ憡锛氬氨鏄彂閫佹柟鐨勭煭淇″彂閫佸埌瀵规柟鎵嬫満涓婁箣鍚庯紝瀵规柟鎵嬫満浼氳繑鍥炵粰杩愯惀鍟嗕竴涓俊鍙凤紝鍛婄煡杩愯惀鍟嗘敹鍒扮煭淇★紝杩愯惀鍟嗗啀鎶婅繖涓俊鍙峰彂缁欏彂閫佹柟锛屽彂閫佹柟寰楀埌杩欎釜淇″彿涔嬪悗锛�
		// Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
		// PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0,
		// deliverIntent, 0);
		// smsManager
		ArrayList<PendingIntent> sendPIIntent = new ArrayList<PendingIntent>();
		sendPIIntent.add(sentPI);
		ArrayList<String> list = smsManager.divideMessage(msgstr); // 鍥犱负涓�潯鐭俊鏈夊瓧鏁伴檺鍒讹紝鍥犳瑕佸皢闀跨煭淇℃媶鍒�
		if(list != null && list.size() > 0 && phoneno != null && phoneno.length() > 0){
			smsManager.sendMultipartTextMessage(phoneno, null, list, sendPIIntent, null);
		}
		return true;

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
