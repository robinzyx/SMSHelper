package com.ginwave.smshelper.more;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ginwave.mms.pdu.CharacterSets;
import com.ginwave.mms.pdu.EncodedStringValue;
import com.ginwave.mms.pdu.HttpUtils;
import com.ginwave.mms.pdu.PduBody;
import com.ginwave.mms.pdu.PduComposer;
import com.ginwave.mms.pdu.PduPart;
import com.ginwave.mms.pdu.SendReq;
import com.ginwave.smshelper.util.MmsSendReceiver;
import com.umeng.analytics.MobclickAgent;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class AutoReplyService extends Service {
	MessageReceiver messageReceiver;
	private String mFilePath;
	private String mMmsReceiverNumber;
	private String mMmsSubject;
	private ConnectivityManager mConnMgr;
	//private PowerManager.WakeLock mWakeLock;
	private boolean mAboutSendMms = false;
	private SendMmsReceiver mSendMmsReceiver;
	private MmsSendReceiver mMmsSendStatusReceiver;
	public static String SemdMmsStatusAction = "com.ginwave.send_mms";
	public static String SEND_MMS = "com.ginwave.smshelper.sendmms";
	private ConnectivityBroadcastReceiver mConnectedReceiver;
	public String mmscUrl = "http://mmsc.monternet.com";
	// public static String mmscUrl = "http://www.baidu.com/";
	public String mmsProxy = "10.0.0.172";
	public int mmsProt = 80;
	private String BaseSplashUrl = "http://202.201.0.162:8888/sms_data";
	private LoadSplashRunnable mLoadRunnable;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		mConnMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		messageReceiver = new MessageReceiver();
		registerReceiver(messageReceiver, new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED"));
		mSendMmsReceiver = new SendMmsReceiver();
		registerReceiver(mSendMmsReceiver, new IntentFilter(
				SEND_MMS));
		mConnectedReceiver = new ConnectivityBroadcastReceiver();
		registerReceiver(mConnectedReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
		mMmsSendStatusReceiver = new MmsSendReceiver();
		registerReceiver(mMmsSendStatusReceiver, new IntentFilter(SemdMmsStatusAction));
		super.onCreate();
		/*mLoadRunnable = new LoadSplashRunnable();
		Thread thread = new Thread(mLoadRunnable);
		thread.run();*/
	}
	
	private int getStoredValue(String pKey){
		SharedPreferences preference = this.getSharedPreferences("AppGuide", MODE_PRIVATE);
		return preference.getInt(pKey, 0);
	}
	
	private void setStoredValue(String pKey, int pValue){
		SharedPreferences preference = this.getSharedPreferences("AppGuide", MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putInt(pKey, pValue);
		editor.commit();
	}
	
	public class LoadSplashRunnable implements Runnable{
		private String PATH = "path";
		private String VERSION = "version";
		private String BASEURL = "http://202.201.0.162:8888";
		private String PIC[] = {"pic1", "pic2"};
		private String LOCALPIC[] = {"pic1.png", "pic2.png"};
		class BitmapUrlObject{
			public String mBitmapUrl;
			public int mBitmapVersion;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String result = SetDataSource.getSplashVersion(BaseSplashUrl);
			JSONArray jArray;
			List<BitmapUrlObject> bitmapUrlList = new ArrayList<BitmapUrlObject>();
			try {
				jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					BitmapUrlObject bitmapObjct = new BitmapUrlObject();
					JSONObject json_data = jArray.getJSONObject(i);
					if(!json_data.isNull(PATH)){
						Log.i("xiao", "path = " + (BASEURL + json_data.getString(PATH)));
						bitmapObjct.mBitmapUrl = BASEURL + json_data.getString(PATH);
					}
					if(!json_data.isNull(VERSION)){
						Log.i("xiao", "version = " + json_data.getInt(VERSION));
						bitmapObjct.mBitmapVersion = json_data.getInt(VERSION);
					}
					bitmapUrlList.add(bitmapObjct);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for( int j = 0; j < bitmapUrlList.size(); j++){
				BitmapUrlObject bitmapObject = bitmapUrlList.get(j);
				Log.i("xiao", " " + j + " value = " + bitmapObject.mBitmapUrl);
				int version = getStoredValue(PIC[j % 2]);
				if(version < bitmapObject.mBitmapVersion){
					Bitmap bitmap = SetDataSource.getBitmapForUrl(bitmapObject.mBitmapUrl);
					FileOutputStream localFileOutputStream1;
					try {
						localFileOutputStream1 = openFileOutput(LOCALPIC[j % 2], 0);
					

						Bitmap.CompressFormat localCompressFormat = Bitmap.CompressFormat.PNG;

						bitmap.compress(localCompressFormat, 100, localFileOutputStream1);

						localFileOutputStream1.close();

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch(IOException e){
						e.printStackTrace();
					}
				}
			}
		}
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(messageReceiver);
		unregisterReceiver(mMmsSendStatusReceiver);
		super.onDestroy();
	}

	public class SendMmsReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("xiao", "onReceive");
			mFilePath = intent.getStringExtra("filepath");
			mMmsReceiverNumber = intent.getStringExtra("number");
			mMmsSubject = intent.getStringExtra("subject");
			Log.i("xiao", "mMmsSubject = " + mMmsSubject);
			/*NetworkInfo info = mConnMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE_MMS);
            if(info == null){
            	Log.i("xiao", "info == null");
            	return ;
            }
            int type = info.getType();
            Log.i("xiao", "type = " + type);
            switch(type){
            case 0:
            	Log.i("xiao", "SendMmsReceiver0000000000");
            	break;
            case 1:
            	Log.i("xiao", "SendMmsReceiver111111");
            	break;
            case 2:
            	Log.i("xiao", "SendMmsReceiver222222222222");
            	//if(mAboutSendMms){
            		Log.i("xiao", "SendMmsReceivermAboutSendMms");
            		sendMms();
            		//mAboutSendMms = false;
                //}
            	return ;
            }*/
			
			/*PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
	        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MMS Connectivity");
	        mWakeLock.setReferenceCounted(false);*/
	        try{
	        	beginMmsConnectivity();
	        }
	        catch(Exception e){
	        	Log.i("xiao", "Exception = " + e.getMessage());
	        }
	        mAboutSendMms = true;
		}
		
		
		
		/*private void releaseWakeLock() {
	        // Don't release the wake lock if it hasn't been created and acquired.
	        if (mWakeLock != null && mWakeLock.isHeld()) {
	            mWakeLock.release();
	        }
	    }
		
		private void acquireWakeLock() {
	        // It's okay to double-acquire this because we are not using it
	        // in reference-counted mode.
	        mWakeLock.acquire();
	    }
		
		private synchronized void createWakeLock() {
	        // Create a new wake lock if we haven't made one yet.
	        if (mWakeLock == null) {
	            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
	            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MMS Connectivity");
	            mWakeLock.setReferenceCounted(false);
	        }
	        acquireWakeLock();
	    }*/
	}
	
	protected int beginMmsConnectivity() throws IOException {
        // Take a wake lock so we don't fall asleep before the message is downloaded.
        //createWakeLock();

        int result = mConnMgr.startUsingNetworkFeature(
                ConnectivityManager.TYPE_MOBILE, "enableMMS");
        Log.i("xiao", "result = " + result);
        switch(result){
        case 0:
        	NetworkInfo info = mConnMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE_MMS);
            if(info == null){
            	Log.i("xiao", "info == null");
            	return result;
            }
            int type = info.getType();
            Log.i("xiao", "type = " + type);
            switch(type){
            case 0:
            	Log.i("xiao", "0000000000");
            	break;
            case 1:
            	Log.i("xiao", "111111");
            	break;
            case 2:
            	Log.i("xiao", "222222222222");
            	//if(mAboutSendMms){
            		Log.i("xiao", "mAboutSendMms");
            		//sendMms();
            		//mAboutSendMms = false;
                //}
            	break;
            }
        	return result;
        case 1:
        	Log.i("xiao", "successssssssssssssssssssssss");
        	return result;
        case -1:
        	Log.i("xiao", "errorrrrrrrrrrrrrrrrrrrrrrrrrrr");
        	return result;
        }

        throw new IOException("Cannot establish MMS connectivity");
    }
	
	protected void endMmsConnectivity() {
        try {
            
            // cancel timer for renewal of lease
            if (mConnMgr != null) {
                mConnMgr.stopUsingNetworkFeature(
                        ConnectivityManager.TYPE_MOBILE,
                        "enableMMS");
            }
        } finally {
            //releaseWakeLock();
        }
    }
	
	private class ConnectivityBroadcastReceiver extends BroadcastReceiver {
        
		
		
		@Override
        public void onReceive(Context context, Intent intent) {
			Log.i("xiao", "network change onReceive");
            NetworkInfo info = mConnMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE_MMS);
            if(info == null){
            	Log.i("xiao", "info == null");
            	return ;
            }
            int type = info.getType();
            Log.i("xiao", "type = " + type);
            switch(type){
            case 0:
            case 1:
            	MobclickAgent.reportError(getApplicationContext(), "NetWork not changed"); 
            	sendMmsStatus(false);
            	Log.i("xiao", "111111");
            	break;
            case 2:
            	Log.i("xiao", "222222222222");
            	if(mAboutSendMms){
            		Log.i("xiao", "mAboutSendMms");
            		sendMms();
            		mAboutSendMms = false;
            		endMmsConnectivity();
                }
            	break;
            }
            
        }
        
        
    }
	
	private void sendMmsStatus(boolean pFlag){
		Intent intent = new Intent();
		intent.putExtra("isSuccess", pFlag);
		intent.putExtra("subject", mMmsSubject);
		Log.i("xiao", "subject = " + mMmsSubject);
		intent.putExtra("number", mMmsReceiverNumber);
		intent.setAction(SemdMmsStatusAction);
		this.sendBroadcast(intent);
	}
	
	private void sendMms(){
		String subject = mMmsSubject;
		//String subject = "HelloWorld";
		Log.i("xiao", "number = " + mMmsReceiverNumber);
		//String recipient = mMmsReceiverNumber;// 138xxxxxxx
		String recipient = mMmsReceiverNumber;
		Log.i("xiao", "mFilePath = " + mFilePath);
		final SendReq sendRequest = new SendReq();
		final EncodedStringValue[] sub = EncodedStringValue.extract(subject);
		if (sub != null && sub.length > 0) {
			sendRequest.setSubject(sub[0]);
		}
		final EncodedStringValue[] phoneNumbers = EncodedStringValue
				.extract(recipient);
		if (phoneNumbers != null && phoneNumbers.length > 0) {
			sendRequest.addTo(phoneNumbers[0]);
		}
		final PduBody pduBody = new PduBody();
		final PduPart part = new PduPart();
		part.setName("sample".getBytes());
		part.setContentType("image/png".getBytes());
		String furl = "file://mnt/sdcard/mms.gif";
		Log.i("xiao", "furl = " + furl);
		final PduPart partPdu = new PduPart();
		partPdu.setCharset(CharacterSets.UTF_8);// UTF_16
		partPdu.setName(part.getName());
		partPdu.setContentType(part.getContentType());
		partPdu.setDataUri(Uri.parse(furl));
		pduBody.addPart(partPdu);

		sendRequest.setBody(pduBody);
		final PduComposer composer = new PduComposer(getApplicationContext(),
				sendRequest);
		final byte[] bytesToSend = composer.make();

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				
				try {
					Log.i("xiao", "run begin");
					HttpUtils.httpConnection(getApplicationContext(), -1L,
							mmscUrl, bytesToSend, HttpUtils.HTTP_POST_METHOD, true, mmsProxy, mmsProt);
					Log.i("xiao", "run over");
					sendMmsStatus(true);
					//
				} catch (IOException e) {
					MobclickAgent.reportError(getApplicationContext(), e.getMessage()); 
					sendMmsStatus(false);
					Log.i("xiao", "Exception = " + e.getMessage());
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
}
