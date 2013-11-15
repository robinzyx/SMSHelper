package com.ginwave.smshelper.more;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ginwave.smshelper.R;
import com.ginwave.smshelper.ReplyRecordProvider;
import com.ginwave.smshelper.R.string;
import com.ginwave.smshelper.localsms.SmsOnePersonReader;
import com.umeng.analytics.MobclickAgent;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SetDataSource {

	public static boolean mSetAutoBegin = false;
	public static String mAutoReplyMessage;
	public static String mAutoReplyTime;
	public static String[] mManualReplyName = { "manual1", "manual2",
			"manual3", "manual4", "manual5" };
	public static int[] mManualReplyDefaultValue = {
			R.string.multichoicedialogitem1, R.string.multichoicedialogitem2,
			R.string.multichoicedialogitem3, R.string.multichoicedialogitem4,
			R.string.multichoicedialogitem5 };
	public static String AUTOREPLY_BEGIN_YEAR = "auto_reply_begin_year";
	public static String AUTOREPLY_BEGIN_MONTH = "auto_reply_begin_month";
	public static String AUTOREPLY_BEGIN_DAY = "auto_reply_begin_day";
	public static String AUTOREPLY_END_YEAR = "auto_reply_end_year";
	public static String AUTOREPLY_END_MONTH = "auto_reply_end_month";
	public static String AUTOREPLY_END_DAY = "auto_reply_end_day";
	public static String SMSCOUNT = "MmsCount";
	public static String SMSTWO = "SmsTwo";
	public static String SMSFIVE = "SmsFive";
	public static String SMSTEN = "SmsTen";
	public static String MMSTHREE = "MmsThree";
	public static String MMSFIVE = "MmsFive";
	public static String MMSEIGHT = "MmsEight";
	public static String SMSSIGN = "SmsSign";
	public static String SMSCALL = "SmsCall";
	public static String SMSFILTER = "SmsFilter";
	public static String SMSLIB = "SmsLib";
	public static String SMSREPLY = "SmsReply";
	public static String SMSTRANSFER = "SmsTransfer";
	public static String MMSCOUNT = "MMSCount";
	public static String EXCLUSIVE_SMS = "exclusive_sms_count";
	public static String EXCLUSIVE_MMS = "exclusive_mms_count";
	public static String FLOWFIVE="FlowFive";
	public static String FLOWTEN="FlowTen";
	public static String FlOWTWENTY="FlowTwenty";
	public static String FLOWTHIRTY="FlowThirty";
	public static String FLOWFIFTY="FowFifty";
	public static String FLOWHUNDRED="FlowHundred";
	public static String FLOWTWOHUNDRED="FlowTwoHundred";
	public static String OIL_FLOW="oil_flow_count";
	public static final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	public static List<String> mPhoneNumberList;
	public static String mMessage;
	public static int mPhoneNumberSize;
	public static int mPresentPhoneNumber;
	public static Long mPresentId;
	public static final String url = "http://211.139.94.78:9008/sms";
	public static final String[] mSmsInfoArray = new String[] { "sms_info_1",
			"sms_info_2", "sms_info_3", "sms_info_4", "sms_info_5",
			"sms_info_6", "sms_info_7", "sms_info_8", "sms_info_9",
			"sms_info_10", "sms_info_11", "sms_info_12", "sms_info_13",
			"sms_info_14", "sms_info_15", "sms_info_16", "sms_info_17",
			"sms_info_18", "sms_info_19", "sms_info_20" };

	public static void setAutoReplyFlag(Context pContext, boolean pFlag) {
		SharedPreferences.Editor spf = pContext.getSharedPreferences("SMS", 0)
				.edit();
		spf.putBoolean("value", pFlag);
		spf.commit();
	}

	public static boolean getAutoReplyFlag(Context pContext) {
		SharedPreferences spf = pContext.getSharedPreferences("SMS", 0);
		return spf.getBoolean("value", false);
	}

	public static void setAutoReplayMessage(Context pContext, String pValue) {
		SharedPreferences.Editor spf = pContext.getSharedPreferences("SMS", 0)
				.edit();
		spf.putString("message", pValue);
		spf.commit();
	}

	public static String getAutoReplyMessage(Context pContext) {
		SharedPreferences spf = pContext.getSharedPreferences("SMS", 0);
		return spf.getString("message",
				pContext.getString(R.string.set_autoreply_message_content));
	}

	public static void setManualReplyContent(Context pContext, String pName,
			String pContent) {
		SharedPreferences.Editor spf = pContext.getSharedPreferences("SMS", 0)
				.edit();
		spf.putString(pName, pContent);
		spf.commit();
	}

	public static String getManualReplyContent(Context pContext, String pName) {
		String value;
		SharedPreferences spf = pContext.getSharedPreferences("SMS", 0);
		value = spf.getString(pName, null);
		Log.i("xiao", "name = " + pName);
		Log.i("xiao", "value = " + value);
		if (value != null) {
			return value;
		}
		for (int i = 0; i < SetDataSource.mManualReplyName.length; i++) {
			Log.i("xiao", "i = " + i + "  replyName = "
					+ SetDataSource.mManualReplyName[i]);
			if (SetDataSource.mManualReplyName[i].equals(pName)) {
				Log.i("xiao", "equals");
				return pContext.getString(mManualReplyDefaultValue[i]);
			}
		}
		return pContext.getString(R.string.multichoicedialogitem1);
	}

	public static void setAutoReplayTime(Context pContext, String pValue) {
		SharedPreferences.Editor spf = pContext.getSharedPreferences("SMS", 0)
				.edit();
		spf.putString("time", pValue);
		spf.commit();
	}

	public static String getAutoReplyTime(Context pContext) {
		SharedPreferences spf = pContext.getSharedPreferences("SMS", 0);
		return spf.getString("time", "15");
	}

	public static YearMonthObject getPresentYearMonth() {
		Calendar ca = Calendar.getInstance();
		YearMonthObject yearMonthObject = new YearMonthObject();
		yearMonthObject.mYear = ca.get(Calendar.YEAR);
		yearMonthObject.mMonth = ca.get(Calendar.MONTH) + 1;
		yearMonthObject.mDay = ca.get(Calendar.DATE);
		return yearMonthObject;
	}

	public static void setManualReplyFirstTimeOpenFlag(Context pContext,
			boolean pFlag) {
		SharedPreferences.Editor spf = pContext.getSharedPreferences("SMS", 0)
				.edit();
		spf.putBoolean("manual_first_open", pFlag);
		spf.commit();
	}

	public static boolean getManualReplyFirstTimeOpenFlag(Context pContext) {
		SharedPreferences spf = pContext.getSharedPreferences("SMS", 0);
		return spf.getBoolean("manual_first_open", true);
	}

	public static void setAutoReplySettedTime(Context pContext, String pName,
			int pValue) {
		SharedPreferences.Editor spf = pContext.getSharedPreferences("SMS", 0)
				.edit();
		spf.putInt(pName, pValue);
		spf.commit();
	}

	public static int getAutoReplySettedTime(Context pContext, String pName) {
		SharedPreferences spf = pContext.getSharedPreferences("SMS", 0);
		return spf.getInt(pName, -1);
	}

	public static void setAutoReplyByTimeFlag(Context pContext, boolean pFlag) {
		SharedPreferences.Editor spf = pContext.getSharedPreferences("SMS", 0)
				.edit();
		spf.putBoolean("reply_by_time", pFlag);
		spf.commit();
	}

	public static boolean getAutoReplyByTimeFlag(Context pContext) {
		SharedPreferences spf = pContext.getSharedPreferences("SMS", 0);
		return spf.getBoolean("reply_by_time", false);
	}

	public static void storeStaticsDetails(Context pContext, long pId,
			String pNumber, Boolean pState) {
		ContentResolver contentResolver = pContext.getContentResolver();
		String date = getFormattedCalendar(pContext);
		ContentValues values = new ContentValues();
		values.put(
				ReplyRecordProvider.StaticsDetailDataColumn.STATICS_DETAIL_COLUMN_ID,
				pId);
		values.put(
				ReplyRecordProvider.StaticsDetailDataColumn.STATICS_DETAIL_COLUMN_NUMBER,
				pNumber);
		values.put(
				ReplyRecordProvider.StaticsDetailDataColumn.STATICS_DETAIL_COLUMN_STATE,
				pState);
		Uri uri = contentResolver.insert(
				ReplyRecordProvider.STATICS_DETAIL_CONTENT_URI, values);
		
		  Log.i("xiaobian", "insert " + uri.toString()); 
//		  ContentUris.parseId(uri);
		 
		Cursor cursor = pContext.getContentResolver().query(
				ReplyRecordProvider.STATICS_DETAIL_CONTENT_URI, null, null,
				null, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				Log.i("xiao",
						"_id = " + cursor.getString(0) + " 2 = "
								+ cursor.getString(1) + " 3 = "
								+ cursor.getString(2));
			}
			Log.i("xiao", "======");
		} else {
			Log.i("xiao", "cursor == null");
		}
	}

	public static long storeStatics(Context pContext, int pNumber,
			String pMessage) {
		ContentResolver contentResolver = pContext.getContentResolver();
		String date = getFormattedCalendar(pContext);
		ContentValues values = new ContentValues();
		values.put(ReplyRecordProvider.StaticsDataColumn.STATICS_COLUMN_DATE,
				date);
		values.put(ReplyRecordProvider.StaticsDataColumn.STATICS_COLUMN_NUMBER,
				pNumber);
		values.put(
				ReplyRecordProvider.StaticsDataColumn.STATICS_COLUMN_MESSAGE,
				pMessage);
		Uri uri = contentResolver.insert(
				ReplyRecordProvider.STATICS_CONTENT_URI, values);
		Log.i("xiao", "insert " + uri.toString());
		Long id = ContentUris.parseId(uri);
		Log.i("xiao", "id = " + id);
		/*
		 * Cursor cursor = pContext.getContentResolver().query(uri, null,
		 * "_id = ?", new String[]{id}, null); if(cursor != null){
		 * while(cursor.moveToNext()){ Log.i("xiao", "_id = " +
		 * cursor.getString(0) + " 2 = " + cursor.getString(1) + " 3 = " +
		 * cursor.getString(2)); } }
		 */
		return id;
	}

	public static Long storeStatics(Context pContext, int pNumber,
			String pMessage, String pPhoneNumber) {
		ContentResolver contentResolver = pContext.getContentResolver();
		String date = getFormattedCalendar(pContext);
		ContentValues values = new ContentValues();
		values.put(ReplyRecordProvider.StaticsDataColumn.STATICS_COLUMN_DATE,
				date);
		values.put(ReplyRecordProvider.StaticsDataColumn.STATICS_COLUMN_NUMBER,
				pNumber);
		values.put(
				ReplyRecordProvider.StaticsDataColumn.STATICS_COLUMN_MESSAGE,
				pMessage);
		values.put(
				ReplyRecordProvider.StaticsDataColumn.STATICS_COLUMN_PHONENUMBER,
				pPhoneNumber);
		Uri uri = contentResolver.insert(
				ReplyRecordProvider.STATICS_CONTENT_URI, values);
		Log.i("xiao", "insert " + uri.toString());
		/*
		 * String id = Long.toString(ContentUris.parseId(uri)); Cursor cursor =
		 * pContext.getContentResolver().query(uri, null, "_id = ?", new
		 * String[] { id }, null); if (cursor != null) { while
		 * (cursor.moveToNext()) { Log.i("xiao", "_id = " + cursor.getString(0)
		 * + " 2 = " + cursor.getString(1) + " 3 = " + cursor.getString(2)); } }
		 */
		Long id = ContentUris.parseId(uri);
		return id;
	}

	public static void deleteStatics(Context pContext, String pId) {
		String where = "_id = ?";
		String[] selectionArgs = { pId };
		ContentResolver contentResolver = pContext.getContentResolver();
		int delete1 = contentResolver.delete(
				ReplyRecordProvider.STATICS_CONTENT_URI, where, selectionArgs);
		int delete2 = contentResolver.delete(
				ReplyRecordProvider.STATICS_DETAIL_CONTENT_URI, where,
				selectionArgs);
		Log.i("xiao", "delete1 = " + delete1);
		Log.i("xiao", "delete2 = " + delete2);
	}

	public static String getFormattedCalendar(Context pContext) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		Date date = calendar.getTime();
		int hour = date.getHours();
		int minutes = date.getMinutes();
		int seconds = date.getSeconds();
		return year + pContext.getString(R.string.year) + month
				+ pContext.getString(R.string.month) + day
				+ pContext.getString(R.string.day) + hour
				+ pContext.getString(R.string.hour) + minutes
				+ pContext.getString(R.string.minute) + seconds
				+ pContext.getString(R.string.second);

	}

	public static void sendMessage(Context pContext,
			ArrayList<String> phonenos, String msgStr) {
		Toast.makeText(pContext, pContext.getString(R.string.send_sending),
				Toast.LENGTH_LONG).show();
		new Thread(new MsgSentManager(pContext, phonenos, msgStr)).start();
	}

	public static String getFormattedPhoneNumber(ArrayList<String> phoneList) {
		String tmpPhone = "";
		for (int i = 0; i < phoneList.size(); i++) {
			if (i == 0) {
				tmpPhone = phoneList.get(i);
			} else {
				tmpPhone += ";";
				tmpPhone += phoneList.get(i);
			}
		}
		return tmpPhone;
	}

	public static ArrayList<String> getSeperatePhoneNumber(String pPhoneNumber) {
		String[] tmpPhoneNumber = pPhoneNumber.split(";");
		ArrayList<String> tmpSeperatePhone = new ArrayList<String>();
		for (int i = 0; i < tmpPhoneNumber.length; i++) {
			tmpSeperatePhone.add(tmpPhoneNumber[i]);
		}
		return tmpSeperatePhone;
	}

	public static class MsgSentManager implements Runnable {

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
			Log.v("fang", "sendMsgs");
			Log.i("xiao", "number = " + phonenos.get(0) + " msgstr = " + msgstr);
			for (String str : phonenos) {

				sendOneMsg(context, str, msgstr);
			}
			return true;
		}

		public boolean sendOneMsg(Context context, String phoneno, String msgstr) {
			Log.v("fang", "sendOneMsg");
			MobclickAgent.onEvent(context, SetDataSource.SMSCOUNT);
			SmsManager smsManager = SmsManager.getDefault();
			PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
					new Intent(SENT_SMS_ACTION), 0);

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
			if (list != null && list.size() > 0 && phoneno != null
					&& phoneno.length() > 0) {
				// SetDataSource.storeStatics(context, 1, msgstr);
				smsManager.sendMultipartTextMessage(phoneno, null, list,
						sendPIIntent, null);
			}
			return true;

		}

		public void run() {
			// TODO Auto-generated method stub

			sendMsgs(c, list, mesgstr);
		}

	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String getSmsContentString(String pType, String pPageNum) {
		return "<?xml version=\"1.0\"?>" + "<req type=" + "\"1\">"
				+ "<sort_name>" + pType + "</sort_name>"
				+ "<page_count>20</page_count>" + "<page_num>" + pPageNum
				+ "</page_num>" + "</req>";
	}

	public static String getUserPointsString(String pNumber) {
		return "<req type=\"2\">" + "<terminal_id>" + pNumber
				+ "</terminal_id>" + "</req>";
	}

	public static String getUserDefinedString(String pNumber) {
		return "<req type=\"3\">" + "<terminal_id>" + pNumber
				+ "</terminal_id>" + "</req>";
	}

	public static String getPostResult(Context pContext, String pRequest,
			String pFileName) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		request.addHeader("Content-Type", "text/xml");
		request.addHeader("charset", "gbk");
		StringEntity s;
		String result = null;
		try {
			s = new StringEntity(pRequest, "gbk");
			request.setEntity(s);
			HttpResponse re = httpclient.execute(request);
			result = EntityUtils.toString(re.getEntity(), "gbk");
			FileOutputStream out = pContext.openFileOutput(pFileName, 0);
			out.write(result.getBytes());
			out.close();
			Log.i("xiao", "result = " + result);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("xiao", "UnsupportedEncodingException " + e.getMessage());
		} catch (ClientProtocolException e) {
			Log.i("xiao", "ClientProtocolException " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("xiao", "IOEXception " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public class SmsForwardUserInfo {
		public String mPhoneNumber;
		public String mUserPoint;
		public String mUserRank;
	}

	public static String getSmsListParseFromString(Context pContext,
			String pFileName, List<String> pSmsList) {
		String status = "1";
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		// 首先找到xml文件
		factory = DocumentBuilderFactory.newInstance();
		try {
			// 找到xml，并加载文档
			builder = factory.newDocumentBuilder();
			document = builder.parse(pContext.openFileInput(pFileName));
			// 找到根Element
			Element root = document.getDocumentElement();
			Log.i("xiao", "getSmsListParseFromString");
			status = root.getAttribute("status");
			if (status.equals("1") || status.equals("2")) {

				return status;
			}
			NodeList nodeList = root.getChildNodes();
			Log.i("xiao", " childNode length = " + nodeList.getLength());
			for (int i = 0; i < nodeList.getLength(); i++) {
				pSmsList.add(nodeList.item(i).getFirstChild().getNodeValue());
			}
		} catch (SAXException e) {
			e.printStackTrace();
			Log.i("xiao", "SAXException " + e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.i("xiao", "ParserConfigurationException " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			Log.i("xiao", "IOEXception " + e.getMessage());
			e.printStackTrace();
		}
		return status;
	}

	public static int getDefinedUserParseFromString(Context pContext,
			String pFileName) {
		String status = null;
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		int result = -2;
		// 首先找到xml文件
		factory = DocumentBuilderFactory.newInstance();
		try {
			// 找到xml，并加载文档
			builder = factory.newDocumentBuilder();
			document = builder.parse(pContext.openFileInput(pFileName));
			// document =
			// builder.parse(pContext.getResources().getAssets().open(pFileName));
			// 找到根Element
			Log.i("xiao", "ducoment");
			Element root = document.getDocumentElement();
			Log.i("xiao", "value = " + root.getAttribute("status"));
			Log.i("xiao", "root");
			NodeList nodes = root.getElementsByTagName("is_dz");
			Log.i("xiao", "nodes length = " + nodes.getLength());
			// 遍历根节点所有子节点,rivers 下所有river
			for (int i = 0; i < nodes.getLength(); i++) {
				// 获取river元素节点
				Element element = (Element) (nodes.item(i));
				// 获取river中name属性值
				status = element.getFirstChild().getNodeValue();
				Log.i("xiao", "value = " + status);
				if (status.equals("2")) {
					result = -1;
				}
				result = Integer.parseInt(status);
				/*
				 * String is_dz = holidayElement.getElementsByTagName("is_dz")
				 * .item(0).getFirstChild().getNodeValue(); result =
				 * Integer.parseInt(is_dz);
				 */
			}
		} catch (SAXException e) {
			e.printStackTrace();
			Log.i("xiao", "SAXException " + e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.i("xiao", "ParserConfigurationException " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			Log.i("xiao", "IOEXception " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public static String[] getUserInfoParseFromString(Context pContext,
			String pFileName) {
		String status = null;
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		int result = -2;
		// 首先找到xml文件
		String userInfo[] = new String[2];
		factory = DocumentBuilderFactory.newInstance();
		try {
			// 找到xml，并加载文档
			builder = factory.newDocumentBuilder();
			document = builder.parse(pContext.openFileInput(pFileName));
			// 找到根Element
			Element root = document.getDocumentElement();
			if (root.getAttribute("status").equals("0")) {
				Element nodes1_terminalId = (Element) root
						.getElementsByTagName("terminal_id").item(0);
				Element nodes1_integral = (Element) root.getElementsByTagName(
						"integral").item(0);
				Element nodes1_rank = (Element) root.getElementsByTagName(
						"rank").item(0);
				userInfo[0] = nodes1_integral.getFirstChild().getNodeValue();
				userInfo[1] = nodes1_rank.getFirstChild().getNodeValue();
				return userInfo;
			}
		} catch (SAXException e) {
			e.printStackTrace();
			Log.i("xiao", "SAXException " + e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.i("xiao", "ParserConfigurationException " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			Log.i("xiao", "IOEXception " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static void setSmsForwardStoredNumber(Context pContext,
			String pNumber) {
		SharedPreferences.Editor spf = pContext.getSharedPreferences("SMS", 0)
				.edit();
		spf.putString("sms_forward_number", pNumber);
		spf.commit();
	}

	public static String getSmsForwardStoredNumber(Context pContext) {
		SharedPreferences spf = pContext.getSharedPreferences("SMS", 0);
		return spf.getString("sms_forward_number", null);
	}

	public static void setSmsForwardStoredNumberFlag(Context pContext,
			boolean pFlag) {
		SharedPreferences.Editor spf = pContext.getSharedPreferences("SMS", 0)
				.edit();
		spf.putBoolean("sms_forward_number_flag", pFlag);
		spf.commit();
	}

	public static boolean getSmsForwardStoredNumberFlag(Context pContext) {
		SharedPreferences spf = pContext.getSharedPreferences("SMS", 0);
		return spf.getBoolean("sms_forward_number_flag", false);
	}
	
	public static Bitmap getBitmapForUrl(String pUrl) {
		HttpGet httpRequest = new HttpGet(pUrl);
		Bitmap bitmap = null;
		// ȡ��HttpClient ����
		HttpClient httpclient = new DefaultHttpClient();
		try {
			// ����httpClient ��ȡ��HttpRestponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// ȡ�������Ϣ ȡ��HttpEntiy
				HttpEntity httpEntity = httpResponse.getEntity();
				// ���һ��������
				InputStream is = httpEntity.getContent();
				// Log.i("xiao", "get it "+ is.read(buffer));
				bitmap = BitmapFactory.decodeStream(is);
				is.close();
				if (bitmap == null) {
					Log.i("xiao", "bitmap == null");
				} else {
					Log.i("xiao", "bitmap !! null");
				}
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	public static String getSplashVersion(String pUrl) {
		String result = "";
		String ss = null;
		JSONObject jObject = null;
		/* ????????????? */
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("year", "1980"));
		InputStream is = null;
		// http post
		/* ???????HttpClient????????? */
		HttpClient httpclient = new DefaultHttpClient();
		Log.i("xiao", "1 ");
		/* ???????HttpPost????? */
		HttpGet httpget = new HttpGet(pUrl);
		Log.i("xiao", "2 ");
		/* ???????????? */
		/*
		 * try { httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); }
		 * catch (UnsupportedEncodingException e1) { // TODO Auto-generated
		 * catch block Log.i("xiao", "UnsupportedEncodingException " +
		 * e1.getMessage()); e1.printStackTrace(); }
		 */
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.i("xiao", "4 ");
		if (response != null && response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			try {
				is = entity.getContent();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				Log.i("xiao", "IllegalStateException " + e1.getMessage());
				e1.printStackTrace();
			} catch (IOException e1) {
				Log.i("xiao", "IOException " + e1.getMessage());
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "/n");
				}
				is.close();
				result = sb.toString();
				Log.i("xiao", "result = " + result);
				return result;

			} catch (Exception e) {
				Log.i("xiao", "Exception2 " + e.getMessage());
			}
		}
		return null;
		// parse json data
		// return messageItemDataList;
	}
}
