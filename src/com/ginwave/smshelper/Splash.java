package com.ginwave.smshelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.renderscript.Element;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.ginwave.smshelper.HolidayDataSource.HolidayItem;
import com.ginwave.smshelper.more.AutoReplyService;
import com.ginwave.smshelper.pojos.Holiday;
import com.ginwave.smshelper.pojos.Mms;
import com.ginwave.smshelper.pojos.Sms;
import com.ginwave.smshelper.provider.Provider;
import com.ginwave.smshelper.provider.SmsHelperProvider;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class Splash extends Activity {

	private static final int SPLASH_DELAY_TIME = 5000;
	private SharedPreferences sharedPreferences;
	private boolean isStartOnce;
	private Handler handler;
	private View splash;
	private static final String TAG = "MainActivity";
	// 检测网络连接状态
	private ConnectivityManager manager;
	private static String url = "http://192.168.80.129:8080/android/initgetdata.do?method=execute";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MobclickAgent.setDebugMode(true);
		com.umeng.common.Log.LOG = true;
		MobclickAgent.onError(this);
		setContentView(R.layout.activity_splash);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"InitDB", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isInitDB", true);
		final Editor initeditor = sharedPreferences.edit();
		if (isFirstRun) {
//			if (checkNetworkState()) {
//				getDataFromServer(url);
//			} else {
				new Thread(new Runnable() {
					@Override
					public void run() {
						getAssertDataToStorage();
						getXMLData();
					}
				}).start();
//			}
			initeditor.putBoolean("isInitDB", false);
			initeditor.commit();
		}

		splash = (View) findViewById(R.id.splash);
		startService(new Intent(this, AutoReplyService.class));
		sharedPreferences = getSharedPreferences("AppGuide", MODE_PRIVATE);

		isStartOnce = sharedPreferences.getBoolean("isstartonce", false);
		int times = sharedPreferences.getInt("startTimes", 0);
		if (times % 2 == 0) {
			splash.setBackgroundResource(R.drawable.p1);
		} else {
			splash.setBackgroundResource(R.drawable.p2);
		}
		times++;
		Editor editor = sharedPreferences.edit();
		editor.putInt("startTimes", times);
		editor.commit();
		if (isStartOnce) {
			final View splash = findViewById(R.id.splash);
			Animation fadeIn = AnimationUtils.loadAnimation(this,
					R.anim.fade_in);
			final Animation fadeOut = AnimationUtils.loadAnimation(this,
					R.anim.fade_out);
			splash.startAnimation(fadeIn);
			fadeIn.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					splash.startAnimation(fadeOut);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

			});

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(Splash.this, SkipActivity.class);
					Splash.this.startActivity(intent);
					Splash.this.finish();
				}
			}, SPLASH_DELAY_TIME);
		} else {

			sharedPreferences.edit().putBoolean("isstartonce", true).commit();
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(Splash.this, AppGuide.class);
					Splash.this.startActivity(intent);
					Splash.this.finish();
				}
			}, SPLASH_DELAY_TIME);
		}

	}

	private void getXMLData() {

		SaxHoliday sax = new SaxHoliday();

		SAXParser sp = null;
		try {
			sp = SAXParserFactory.newInstance().newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();

		} catch (SAXException e) {
			e.printStackTrace();

		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		}
		File f = new File("/data/data/com.ginwave.smshelper/files/user.xml");
		if (!f.exists()) {
			Toast.makeText(getApplicationContext(), "not exist user.xml  ",
					Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			sp.parse(f, sax);

		} catch (SAXException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		List<HolidayItem> holidayList = sax.getHolidayList();
		HolidayItem holidayGridSource;
		Holiday holiday;
		for (int i = 0; i < holidayList.size(); i++) {
			final int h_id = i+1;
			holidayGridSource = holidayList.get(i);
			holiday = new Holiday();
			holiday.setH_name(holidayGridSource.getHolidayName());
			holiday.setItemOneTitle(holidayGridSource.getHolidayItem2Name());
			holiday.setItemOneCont(holidayGridSource.getHolidayItem2Value());
			holiday.setItemTwoTitle(holidayGridSource.getHolidayItem3Name());
			holiday.setItemTwoCont(holidayGridSource.getHolidayItem3Value());
			holiday.setItemThreeTitle(holidayGridSource.getHolidayItem4Name());
			holiday.setItemThreeCont(holidayGridSource.getHolidayItem4Value());
			holiday.setItemFourTitle(holidayGridSource.getHolidayItem5Name());
			holiday.setItemFourCont(holidayGridSource.getHolidayItem5Value());
			holiday.setIs_up(false);
			holiday.setStringSms(holidayGridSource.getHolidayItem1Value());
			holiday.setStringMms(holidayGridSource.getHolidayItem6Value());

			Log.v("holiday", "节日名字是:" + holiday.getH_name());
			Log.v("holiday", "节日编号是：" + i);
			Log.v("holiday", "节日1:" + holiday.getItemOneTitle());
			Log.v("holiday", "节日2:" + holiday.getItemTwoTitle());
			Log.v("holiday", "节日3:" + holiday.getItemThreeTitle());
			Log.v("holiday", "节日4:" + holiday.getItemFourTitle());
			Log.v("holiday", "节日1value:" + holiday.getStringSms());
			Log.v("holiday", "节日1value:" + holiday.getStringMms());
			insertHoliday(holiday);
			final List<String> smsContList = holiday.getStringSms();
			final List<String> mmsContList = holiday.getStringMms();
			new Thread(new Runnable() {
				@Override
				public void run() {
					Log.v(TAG, "" + smsContList.size());
					Sms sms = null;
					if (smsContList != null) {
						for (String sms_cont : smsContList) {
							sms = new Sms();
							sms.setSms_cont(sms_cont);
							sms.setHoliday(h_id);
							insertSms(sms);
						}
					}
				}
			}).start();

			new Thread(new Runnable() {

				@Override
				public void run() {
					Mms mms = null;
					if (mmsContList != null) {
						for (String mms_cont : mmsContList) {
							Log.v(TAG, "" + smsContList.size());
							mms = new Mms();
							mms.setMms_cont(mms_cont);
							mms.setHoliday(h_id);
							insertMms(mms);
						}
					}

				}
			}).start();

		}

	}

	public void getAssertDataToStorage() {
		AssetManager assetManager = getAssets();
		InputStream input = null;
		BufferedReader bReader = null;
		FileWriter fw = null;
		try {
			input = assetManager.open("user.xml");
			InputStreamReader inputReader = new InputStreamReader(input,
					"UTF-8");
			bReader = new BufferedReader(inputReader);
			Log.v(TAG, "bReader" + bReader.readLine().length());
			File data = new File(
					"/data/data/com.ginwave.smshelper/files/user.xml");
			if (!data.exists()) {
				data.createNewFile();
				Log.v(TAG, "mkdirs");
				fw = new FileWriter(data);
				String line = "";
				// fw.write("<?xml"+" "+"version="+'"'+1.0+'"'+" "+"encoding="+'"'+"UTF-8"+'"'+"?>");
				while ((line = bReader.readLine()) != null) {
					fw.write(line);
					fw.write("\n");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
					bReader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

	/**
	 * 检测网络是否连接
	 * 
	 * @return
	 */
	private boolean checkNetworkState() {
		boolean flag = false;
		// 得到网络连接信息
		manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// 去进行判断网络是否连接
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		if (!flag) {
			setNetwork();
		} else {
			isNetworkAvailable();
		}
		Log.v("zhu", "zhu" + flag);

		return flag;
	}

	/**
	 * 网络未连接时，调用设置方法
	 */
	private void setNetwork() {
		Toast.makeText(this, "wifi is closed!", Toast.LENGTH_SHORT).show();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("网络提示信息");
		builder.setMessage("网络不可用，如果继续，请先设置网络！");
		builder.setPositiveButton("设置", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;
				/**
				 * 判断手机系统的版本！如果API大于10 就是3.0+ 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
				 */
				if (android.os.Build.VERSION.SDK_INT > 10) {
					intent = new Intent(
							android.provider.Settings.ACTION_WIFI_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName component = new ComponentName(
							"com.android.settings",
							"com.android.settings.WirelessSettings");
					intent.setComponent(component);
					intent.setAction("android.intent.action.VIEW");
				}
				startActivity(intent);
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create();
		builder.show();
	}

	/**
	 * 网络已经连接，然后去判断是wifi连接还是GPRS连接 设置一些自己的逻辑调用
	 */
	private void isNetworkAvailable() {

		State gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (gprs == State.CONNECTED || gprs == State.CONNECTING) {
			Toast.makeText(this, "wifi is open! gprs", Toast.LENGTH_SHORT)
					.show();
		}
		// 判断为wifi状态下才加载广告，如果是GPRS手机网络则不加载！
		if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
			Toast.makeText(this, "wifi is open! wifi", Toast.LENGTH_SHORT)
					.show();
			// loadAdmob();
		}

	}

	private void getDataFromServer(String url) {
		url += "&parameter=init";
		Log.v("zhu", "url=" + url);
		HttpClient client = new DefaultHttpClient();
		Log.v("zhu", "client=" + client);
		HttpPost request;
		try {
			request = new HttpPost(new URI(url));
			Log.v("zhu", "request=" + request);
			HttpResponse response = client.execute(request);
			Log.v("zhu", "response=" + response);
			// 判断请求是否成功
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity);
					try {
						JSONObject jsonObjectHolidayArray = new JSONObject(out);
						JSONArray arrayHoliday = jsonObjectHolidayArray
								.getJSONArray("holidayArray");
						Log.v(TAG, "arrayHoliday" + arrayHoliday.length());
						
						for (int i = 0; i < arrayHoliday.length(); i++) {
							JSONObject jsonObjectHoliday = arrayHoliday
									.getJSONObject(i);
							Holiday holiday = constructorJsonStringToObject(jsonObjectHoliday
									.toString());
							insertHoliday(holiday);
							List<Sms> getSmsList = holiday.getSmslist();
							for (Sms sms : getSmsList) {
								Log.v(TAG, "sms is" + sms.getSms_id());
								insertSms(sms);
							}
							List<Mms> getMmsList = holiday.getMmslist();
							for (Mms mms : getMmsList) {
								insertMms(mms);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Holiday constructorJsonStringToObject(String jsonObjectHoliday) {
		JSONObject object = null;
		Holiday holiday = new Holiday();
		Sms sms;
		Mms mms;
		List<Sms> smsList = new ArrayList<Sms>();
		List<Mms> mmsList = new ArrayList<Mms>();
		try {
			object = new JSONObject(jsonObjectHoliday);
			Log.v("zhu", "holiday name is" + object.getString("h_name"));
			holiday.setH_id(object.getInt("h_id"));
			holiday.setH_name(object.getString("h_name"));
			holiday.setIs_up(object.getBoolean("is_up"));
			holiday.setItemOneTitle(object.getString("itemOneTitle"));
			holiday.setItemOneCont(object.getString("itemOneCont"));
			holiday.setItemTwoTitle(object.getString("itemTwoTitle"));
			holiday.setItemTwoCont(object.getString("itemTwoCont"));
			holiday.setItemThreeTitle(object.getString("itemThreeTitle"));
			holiday.setItemThreeCont(object.getString("itemThreeCont"));
			holiday.setItemFourTitle(object.getString("itemFourTitle"));
			holiday.setItemFourCont(object.getString("itemFourCont"));
			String smslistString = object.getString("sms");
			JSONArray arraySms = new JSONArray(smslistString);
			Log.v(TAG, "array=" + arraySms);
			for (int i = 0; i < arraySms.length(); i++) {
				sms = new Sms();
				JSONObject smsJsonObject = arraySms.getJSONObject(i);
				Log.v(TAG,
						"i=" + i + "array.getJSONObject(i)="
								+ arraySms.getJSONObject(i));
				sms.setSms_id(smsJsonObject.getInt("sms_id"));
				sms.setSms_cont(smsJsonObject.getString("sms_cont"));
				sms.setHoliday(holiday.getH_id());
				smsList.add(sms);
			}
			holiday.setSmslist(smsList);
			String mmslistString = object.getString("mms");
			JSONArray arrayMms = new JSONArray(mmslistString);
			for (int i = 0; i < arrayMms.length(); i++) {
				mms = new Mms();
				JSONObject mmsJsonObject = arrayMms.getJSONObject(i);
				mms.setMms_cont(mmsJsonObject.getString("mms_cont"));
				mms.setMms_id(mmsJsonObject.getInt("mms_id"));
				mms.setMms_pic(mmsJsonObject.getString("mms_pic"));
				mms.setHoliday(holiday.getH_id());
				mmsList.add(mms);

			}
			holiday.setMmslist(mmsList);

			Log.v(TAG, "holiday.getSmslist()=" + holiday.getSmslist());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return holiday;

	}

	private int insertHoliday(Holiday holiday) {
		ContentValues values = new ContentValues();
		values.put(Provider.HolidayColumns.HOLIDAYNAME, holiday.getH_name());
		values.put(Provider.HolidayColumns.ISUP, holiday.getIs_up());
		values.put(Provider.HolidayColumns.ITEMONETITLE,
				holiday.getItemOneTitle());
		values.put(Provider.HolidayColumns.ITEMONECONT,
				holiday.getItemOneCont());
		values.put(Provider.HolidayColumns.ITEMTWOTITLE,
				holiday.getItemTwoTitle());
		values.put(Provider.HolidayColumns.ITEMTWOCONT,
				holiday.getItemTwoCont());
		values.put(Provider.HolidayColumns.ITEMTHREETITLE,
				holiday.getItemThreeTitle());
		values.put(Provider.HolidayColumns.ITEMTHREECONT,
				holiday.getItemThreeCont());
		values.put(Provider.HolidayColumns.ITEMFOURTITLE,
				holiday.getItemFourTitle());
		values.put(Provider.HolidayColumns.ITEMFOURCONT,
				holiday.getItemFourCont());
		
		Uri uri = getContentResolver().insert(
				Provider.HolidayColumns.CONTENT_URI, values);
		Log.i(TAG, "insert uri=" + uri);
		String lastPath = uri.getPathSegments().get(1);
		if (TextUtils.isEmpty(lastPath)) {
			Log.i(TAG, "insert holiday failure!");
		} else {
			Log.i(TAG, "insert holiday success! the id is " + lastPath);
		}

		return Integer.parseInt(lastPath);
	}

	private int insertSms(Sms sms) {
		ContentValues values = new ContentValues();
		values.put(Provider.SmsColumns.SMSCONT, sms.getSms_cont());
		values.put(Provider.SmsColumns.HOLIDAYID, sms.getHoliday());
		Uri uri = getContentResolver().insert(Provider.SmsColumns.CONTENT_URI,
				values);
		Log.i(TAG, "insert uri=" + uri);
		String lastPath = uri.getPathSegments().get(1);
		if (TextUtils.isEmpty(lastPath)) {
			Log.i(TAG, "insert sms failure!");
		} else {
			Log.i(TAG, "insert sms success! the id is " + lastPath);
		}

		return Integer.parseInt(lastPath);
	}

	private int insertMms(Mms mms) {
		ContentValues values = new ContentValues();
		values.put(Provider.MmsColumns.MMSCONT, mms.getMms_cont());
		values.put(Provider.MmsColumns.HOLIDAYID, mms.getHoliday());
		values.put(Provider.MmsColumns.MMSPIC, mms.getMms_pic());
		Uri uri = getContentResolver().insert(Provider.MmsColumns.CONTENT_URI,
				values);
		Log.i(TAG, "insert uri=" + uri);
		String lastPath = uri.getPathSegments().get(1);
		if (TextUtils.isEmpty(lastPath)) {
			Log.i(TAG, "insert failure!");
		} else {
			Log.i(TAG, "insert success! the id is " + lastPath);
		}

		return Integer.parseInt(lastPath);
	}

	private void queryHoliday(int id) {
		Cursor c = null;
		try {
			c = getContentResolver().query(
					Provider.HolidayColumns.CONTENT_URI,
					new String[] { Provider.HolidayColumns.HOLIDAYNAME,
							Provider.HolidayColumns.ITEMFOURCONT,
							Provider.HolidayColumns.ITEMFOURTITLE,
							Provider.HolidayColumns.ITEMONECONT,
							Provider.HolidayColumns.ITEMONETITLE,
							Provider.HolidayColumns.ITEMTHREECONT,
							Provider.HolidayColumns.ITEMTHREETITLE,
							Provider.HolidayColumns.ITEMTWOCONT,
							Provider.HolidayColumns.ITEMTWOTITLE,
							Provider.HolidayColumns.CREATETIME,
							Provider.HolidayColumns.ICON,
							Provider.HolidayColumns.ISUP,
							Provider.HolidayColumns.MODIFYTIME,
							Provider.HolidayColumns.MONTH, },
					Provider.HolidayColumns.HOLIDAYID + "=?",
					new String[] { id + "" }, null);
			if (c != null && c.moveToFirst()) {
				Holiday holiday = new Holiday();
				holiday.setH_name(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.HOLIDAYNAME)));
				holiday.setItemFourCont(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMFOURCONT)));
				holiday.setItemFourTitle(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMFOURTITLE)));
				holiday.setItemOneCont(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMONECONT)));
				holiday.setItemOneTitle(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMONETITLE)));
				holiday.setItemThreeCont(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMTHREECONT)));
				holiday.setItemThreeTitle(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMTHREETITLE)));
				holiday.setItemTwoCont(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMTWOCONT)));
				holiday.setItemTwoTitle(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMTWOTITLE)));
				Log.i(TAG, "leader.name=" + holiday.getH_name());
			} else {
				Log.i(TAG, "query failure!");
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
			}
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
