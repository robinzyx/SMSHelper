package com.ginwave.smshelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ginwave.mms.async.AsyncImageLoader3;
import com.ginwave.mms.async.ImageCallback;
import com.ginwave.smshelper.HolidayDataSource.HolidayItem;
import com.ginwave.smshelper.localsms.LocalSmsSendActivity;
import com.ginwave.smshelper.pojos.Holiday;
import com.ginwave.smshelper.pojos.Mms;
import com.ginwave.smshelper.pojos.Sms;
import com.ginwave.smshelper.provider.Provider;
import com.umeng.analytics.MobclickAgent;

public class HolidayGrid extends Activity implements OnClickListener,
		AnimationListener {

	private static final String TAG = null;
	private ConnectivityManager manager;
	private static String url = "http://219.246.178.120:8080/android/updata.do?method=execute";
	private static String urlToServer = "http://219.246.178.120:8080/android/commit.do?method=execute";
	private LinearLayout mHolidayLayout;
	private TextView[] mHolidayIconArray;
	private LinearLayout mHolidayLayoutItem[];
	private Animation mHolidayLayoutTranslateDownAnimation[];
	private Animation mHolidayLayoutTranslateUpAnimation[];
	private Animation mHolidayDetailTranslateDownAnimation;
	private Animation mHolidayDetailTranslateUpAnimation;
	private int mScreenWidth;
	private int mScreenHeight;
	private LinearLayout mHolidayDetailLayout;
	private TextView mHolidayDetailArray[];
	private TextView mHolidayDetailAbout;
	private TextView mHolidayDetailMessage;
	private TextView mHolidayDetailHistory;
	private TextView mHolidayDetailWords;
	private TextView mHolidayDetailSongs;
	private ListView mHolidayMessageList;
	private TextView mHolidayOtherValue;
	private TextView mHolidayDetailIcon;
	private TextView mHolidayDetailBack;
	private Button msglib;
	private Button colmsglib;
	private int mHolidayIconId[];
	private int mHolidayIconBigId[];
	private int mHolidayIconTextId[];
	private int mPresentSelectionItem;
	private int mPresentClickItem;
	private LayoutInflater mInflater;
	private HolidayDetailAdapter mHolidayDetailAdapter;
	private HolidayItem mHolidayItem;
	private Resources mResources;
	private String[] mXmlArray;
	private TextView mMainBack;
	private List<String> mMmsArray;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.holiday_grid);
		mResources = this.getResources();
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainBack.setOnClickListener(this);
		mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		mHolidayDetailLayout = (LinearLayout) findViewById(R.id.mHolidayDetailLayout);
		mHolidayDetailArray = new TextView[5];
		mHolidayDetailArray[0] = (TextView) findViewById(R.id.mHolidayDetailMessage);
		mHolidayDetailArray[0].setOnClickListener(this);
		mHolidayDetailArray[1] = (TextView) findViewById(R.id.mHolidayDetailAbout);
		mHolidayDetailArray[1].setOnClickListener(this);
		mHolidayDetailArray[2] = (TextView) findViewById(R.id.mHolidayDetailHistory);
		mHolidayDetailArray[2].setOnClickListener(this);
		mHolidayDetailArray[3] = (TextView) findViewById(R.id.mHolidayDetailWords);
		mHolidayDetailArray[3].setOnClickListener(this);
		mHolidayDetailArray[4] = (TextView) findViewById(R.id.mHolidayDetailSongs);
		mHolidayDetailArray[4].setOnClickListener(this);
		mHolidayMessageList = (ListView) findViewById(R.id.mHolidayMessageList);
		mHolidayOtherValue = (TextView) findViewById(R.id.mHolidayOtherValue);
		mHolidayDetailBack = (TextView) findViewById(R.id.mHolidayDetailBack);
		mHolidayDetailIcon = (TextView) findViewById(R.id.mHolidayDetailIcon);

		mHolidayDetailBack.setOnClickListener(this);
		mHolidayIconArray = new TextView[28];
		mHolidayLayoutItem = new LinearLayout[7];
		mHolidayLayoutTranslateDownAnimation = new TranslateAnimation[7];
		mHolidayLayoutTranslateUpAnimation = new TranslateAnimation[7];
		msglib = (Button) findViewById(R.id.msg_lib);
		colmsglib = (Button) findViewById(R.id.Colmsg_lib);

		mHolidayLayout = (LinearLayout) findViewById(R.id.mHolidayLayout);
		mScreenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		mScreenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		mHolidayIconId = new int[] { R.drawable.holiday1, R.drawable.holiday2,
				R.drawable.holiday3, R.drawable.holiday4, R.drawable.holiday5,
				R.drawable.holiday6, R.drawable.holiday7, R.drawable.holiday8,
				R.drawable.holiday9, R.drawable.holiday10,
				R.drawable.holiday11, R.drawable.holiday12,
				R.drawable.holiday13, R.drawable.holiday14,
				R.drawable.holiday15, R.drawable.holiday16,
				R.drawable.holiday17, R.drawable.holiday18,
				R.drawable.holiday19, R.drawable.holiday20,
				R.drawable.holiday21, R.drawable.holiday22,
				R.drawable.holiday23, R.drawable.holiday24,
				R.drawable.holiday25, R.drawable.holiday26,
				R.drawable.holiday27, R.drawable.holiday28 };
		mHolidayIconBigId = new int[] { R.drawable.holiday1_big,
				R.drawable.holiday2_big, R.drawable.holiday3_big,
				R.drawable.holiday4_big, R.drawable.holiday5_big,
				R.drawable.holiday6_big, R.drawable.holiday7_big,
				R.drawable.holiday8_big, R.drawable.holiday9_big,
				R.drawable.holiday10_big, R.drawable.holiday11_big,
				R.drawable.holiday12_big, R.drawable.holiday13_big,
				R.drawable.holiday14_big, R.drawable.holiday15_big,
				R.drawable.holiday16_big, R.drawable.holiday17_big,
				R.drawable.holiday18_big, R.drawable.holiday19_big,
				R.drawable.holiday20_big, R.drawable.holiday21_big,
				R.drawable.holiday22_big, R.drawable.holiday23_big,
				R.drawable.holiday24_big, R.drawable.holiday25_big,
				R.drawable.holiday26_big, R.drawable.holiday27_big,
				R.drawable.holiday28_big };
		mHolidayIconTextId = new int[] { R.string.holiday1, R.string.holiday2,
				R.string.holiday3, R.string.holiday4, R.string.holiday5,
				R.string.holiday6, R.string.holiday7, R.string.holiday8,
				R.string.holiday9, R.string.holiday10, R.string.holiday11,
				R.string.holiday12, R.string.holiday13, R.string.holiday14,
				R.string.holiday15, R.string.holiday16, R.string.holiday17,
				R.string.holiday18, R.string.holiday19, R.string.holiday20,
				R.string.holiday21, R.string.holiday22, R.string.holiday23,
				R.string.holiday24, R.string.holiday25, R.string.holiday26,
				R.string.holiday27, R.string.holiday28 };
		// mXmlArray = new String[] { "holiday1.xml", "holiday2.xml",
		// "holiday3.xml", "holiday4.xml", "holiday5.xml", "holiday6.xml",
		// "holiday7.xml", "holiday8.xml", "holiday9.xml",
		// "holiday10.xml", "holiday11.xml", "holiday12.xml",
		// "holiday13.xml", "holiday14.xml", "holiday15.xml",
		// "holiday16.xml", "holiday17.xml", "holiday18.xml",
		// "holiday19.xml", "holiday20.xml", "holiday21.xml",
		// "holiday22.xml", "holiday23.xml", "holiday24.xml",
		// "holiday25.xml", "holiday26.xml", "holiday27.xml",
		// "holiday28.xml" };
		mXmlArray = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"19", "11", "12", "13", "14", "15", "16", "17", "18", "19",
				"20", "21", "22", "23", "24", "25", "26", "27", "28" };
		msglib.setBackgroundResource(R.drawable.button_pressed);
		msglib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				controlHolidayDetailContent(0);
				mHolidayDetailArray[0].setText("短信");
				mMmsArray.clear();
				mMmsArray.addAll(mHolidayItem.getHolidayItem1Value());
				Log.i("XXX", "sms "
						+ mHolidayItem.getHolidayItem1Value().size());
				msglib.setBackgroundResource(R.drawable.button_pressed);
				colmsglib.setBackgroundResource(R.drawable.button_normal);
				mHolidayDetailAdapter.notifyDataSetChanged();
			}
		});
		colmsglib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				controlHolidayDetailContent(0);
				mHolidayDetailArray[0].setText("彩信");
				mMmsArray.clear();
				if (mHolidayItem.getHolidayItem6Value() != null) {
					Log.v("zhu", "zhu" + mHolidayItem.getHolidayItem6Value());
					mMmsArray.addAll(mHolidayItem.getHolidayItem6Value());
				}
				colmsglib.setBackgroundResource(R.drawable.button_pressed);
				msglib.setBackgroundResource(R.drawable.button_normal);
				mHolidayDetailAdapter.notifyDataSetChanged();
			}
		});

		int tmpNumber;
		for (int i = 0; i < mHolidayLayout.getChildCount(); i++) {
			tmpNumber = i * 4;
			View view = mHolidayLayout.getChildAt(i);
			mHolidayLayoutItem[i] = (LinearLayout) view;
			mHolidayIconArray[tmpNumber] = (TextView) view
					.findViewById(R.id.mHolidayOne);
			mHolidayIconArray[tmpNumber]
					.setCompoundDrawablesWithIntrinsicBounds(null,
							mResources.getDrawable(mHolidayIconId[tmpNumber]),
							null, null);
			mHolidayIconArray[tmpNumber].setText(mHolidayIconTextId[tmpNumber]);
			mHolidayIconArray[tmpNumber].setTag(tmpNumber);
			mHolidayIconArray[tmpNumber].setOnClickListener(this);
			mHolidayIconArray[tmpNumber + 1] = (TextView) view
					.findViewById(R.id.mHolidayTwo);
			mHolidayIconArray[tmpNumber + 1]
					.setCompoundDrawablesWithIntrinsicBounds(null, mResources
							.getDrawable(mHolidayIconId[tmpNumber + 1]), null,
							null);
			mHolidayIconArray[tmpNumber + 1]
					.setText(mHolidayIconTextId[tmpNumber + 1]);
			mHolidayIconArray[tmpNumber + 1].setTag(tmpNumber + 1);
			mHolidayIconArray[tmpNumber + 1].setOnClickListener(this);
			mHolidayIconArray[tmpNumber + 2] = (TextView) view
					.findViewById(R.id.mHolidayThree);
			mHolidayIconArray[tmpNumber + 2]
					.setCompoundDrawablesWithIntrinsicBounds(null, mResources
							.getDrawable(mHolidayIconId[tmpNumber + 2]), null,
							null);
			mHolidayIconArray[tmpNumber + 2]
					.setText(mHolidayIconTextId[tmpNumber + 2]);
			mHolidayIconArray[tmpNumber + 2].setTag(tmpNumber + 2);
			mHolidayIconArray[tmpNumber + 2].setOnClickListener(this);
			mHolidayIconArray[tmpNumber + 3] = (TextView) view
					.findViewById(R.id.mHolidayFour);
			mHolidayIconArray[tmpNumber + 3]
					.setCompoundDrawablesWithIntrinsicBounds(null, mResources
							.getDrawable(mHolidayIconId[tmpNumber + 3]), null,
							null);
			mHolidayIconArray[tmpNumber + 3]
					.setText(mHolidayIconTextId[tmpNumber + 3]);
			mHolidayIconArray[tmpNumber + 3].setTag(tmpNumber + 3);
			mHolidayIconArray[tmpNumber + 3].setOnClickListener(this);
		}
		for (int j = 0; j < mHolidayLayoutItem.length; j++) {
			Log.i("xiao", "top = " + mHolidayLayoutItem[j].getTop());
			TranslateAnimation translateAnimation = new TranslateAnimation(0,
					0, (int) mHolidayLayoutItem[j].getTop(), mScreenHeight);
			translateAnimation.setDuration(1000);
			translateAnimation.setRepeatCount(0);
			mHolidayLayoutTranslateDownAnimation[j] = translateAnimation;
		}
		for (int k = 0; k < mHolidayLayoutItem.length; k++) {
			TranslateAnimation translateAnimation = new TranslateAnimation(0,
					0, mScreenHeight, (int) mHolidayLayoutItem[k].getTop());
			translateAnimation.setDuration(1000);
			translateAnimation.setRepeatCount(0);
			mHolidayLayoutTranslateUpAnimation[k] = translateAnimation;
		}
		mHolidayDetailTranslateDownAnimation = new TranslateAnimation(0, 0,
				mHolidayDetailLayout.getTop(), mScreenHeight);
		mHolidayDetailTranslateDownAnimation.setDuration(1000);
		mHolidayDetailTranslateDownAnimation.setRepeatCount(0);
		mHolidayDetailTranslateUpAnimation = new TranslateAnimation(0, 0,
				mScreenHeight, mHolidayDetailLayout.getTop());
		mHolidayDetailTranslateUpAnimation.setDuration(1000);
		mHolidayDetailTranslateUpAnimation.setRepeatCount(0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mMainBack)) {
			this.finish();
			return;
		}
		if (v.equals(mHolidayDetailBack)) {
			mHolidayDetailLayout
					.startAnimation(mHolidayDetailTranslateDownAnimation);
			mHolidayDetailTranslateDownAnimation.setAnimationListener(this);
			// setDetailLayoutInvisible();
			return;
		}
		if (v instanceof LinearLayout
				&& mHolidayDetailArray[0].getText().toString().equals("短信")) {
			TextView textView = (TextView) v
					.findViewById(R.id.mHolidayDetailItem);
			String text = textView.getText().toString();
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			intent.setClass(getApplicationContext(), LocalSmsSendActivity.class);
			bundle.putString("xm", text);
			bundle.putString("phonenumber", "");
			intent.putExtras(bundle);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return;
		} else if (v instanceof LinearLayout
				&& mHolidayDetailArray[0].getText().toString().equals("彩信")) {
			TextView textView = (TextView) v
					.findViewById(R.id.mHolidayDetailItem);
			String text = textView.getText().toString();
			String extStorageDirectory = Environment
					.getExternalStorageDirectory().toString();
			File file = new File(extStorageDirectory, "mms.gif");
			String picturePath = file.getAbsolutePath();
			FileOutputStream outStream;
			InputStream inputStream = null;
			int position = (Integer) v.getTag();
			try {
				AssetManager assetManager = getAssets();
				Log.v("zhu", "" + mHolidayItem.getHolidayName());
				inputStream = assetManager.open(chineseToEnglish(mHolidayItem
						.getHolidayName()) + position + ".gif");
				Log.v("zhu", "inputStream" + inputStream + "position"
						+ position);
				outStream = new FileOutputStream(file);
				byte[] bt = new byte[1024];
				int count;
				while ((count = inputStream.read(bt)) > 0) {
					outStream.write(bt, 0, count);
				}
				inputStream.close();
				outStream.flush();
				outStream.close();
			} catch (FileNotFoundException e) {
				Log.i("xiao", "FileNotFoundException " + e.getMessage());
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						R.string.holiday_mms_read_exception, Toast.LENGTH_SHORT)
						.show();
				return;
			} catch (IOException e) {
				Log.i("xiao", "IOException " + e.getMessage());
				Toast.makeText(getApplicationContext(),
						R.string.holiday_mms_read_exception, Toast.LENGTH_SHORT)
						.show();
				e.printStackTrace();
				return;
			}
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), HolidayGridSendMms.class);
			intent.putExtra("content", text);
			intent.putExtra("picture", picturePath);
			this.startActivity(intent);
			return;
		}

		TextView textView = (TextView) v;
		for (int j = 0; j < mHolidayDetailArray.length; j++) {
			if (textView.equals(mHolidayDetailArray[j])) {
				mPresentSelectionItem = j;
				controlHolidayDetailItem();
				controlHolidayDetailContent();
				return;
			}
		}
		Log.i("xiao", "tag = " + textView.getTag());
		mPresentClickItem = (Integer) textView.getTag();
		for (int i = 6; i >= 0; i--) {
			mHolidayLayoutItem[i]
					.startAnimation(mHolidayLayoutTranslateDownAnimation[i]);
			if (i == 0) {
				mHolidayLayoutTranslateDownAnimation[i]
						.setAnimationListener(this);
			}
		}
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub
		if (arg0.equals(mHolidayDetailTranslateDownAnimation)) {
			setDetailLayoutInvisible();
		} else {
			setDetailLayoutVisible();
		}
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub

	}

	private void setDetailLayoutInvisible() {
		mHolidayDetailLayout.setVisibility(View.INVISIBLE);
		mHolidayLayout.setVisibility(View.VISIBLE);
		for (int i = 6; i >= 0; i--) {
			mHolidayLayoutItem[i]
					.startAnimation(mHolidayLayoutTranslateUpAnimation[i]);
		}
	}

	private void setDetailLayoutVisible() {
		mHolidayDetailLayout.setVisibility(View.VISIBLE);
		mHolidayLayout.setVisibility(View.INVISIBLE);
		mHolidayDetailLayout.startAnimation(mHolidayDetailTranslateUpAnimation);
		initDetailLayoutView();
		mPresentSelectionItem = 0;
		controlHolidayDetailItem();
		controlHolidayDetailContent();
	}

	private Holiday queryHoliday(int id) {
		Holiday holiday = null;
		Cursor c = null;
		try {
			c = getContentResolver().query(
					Provider.HolidayColumns.CONTENT_URI,
					new String[] { Provider.HolidayColumns.HOLIDAYNAME,

					Provider.HolidayColumns.ITEMONETITLE,
							Provider.HolidayColumns.ITEMONECONT,

							Provider.HolidayColumns.ITEMTWOTITLE,
							Provider.HolidayColumns.ITEMTWOCONT,

							Provider.HolidayColumns.ITEMTHREETITLE,
							Provider.HolidayColumns.ITEMTHREECONT,

							Provider.HolidayColumns.ITEMFOURTITLE,
							Provider.HolidayColumns.ITEMFOURCONT, },
					Provider.HolidayColumns.HOLIDAYID + "=?",
					new String[] { id + "" }, null);
			// Log.v("zhu", "c=" + c.moveToFirst());
			if (c != null && c.moveToFirst()) {
				// Log.v("zhu", "move to next");
				// while(c.moveToNext()){
				holiday = new Holiday();
				holiday.setH_name(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.HOLIDAYNAME)));
				// Log.i("zhu", "6.name=" + holiday.getH_name());
				holiday.setItemOneTitle(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMONETITLE)));
				// Log.i("zhu", "5.name=" + holiday.getItemOneTitle());
				holiday.setItemOneCont(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMONECONT)));

				// Log.i("zhu", "5.name=" + holiday.getItemOneCont());
				holiday.setItemTwoTitle(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMTWOTITLE)));
				// Log.i("zhu", "2.name=" + holiday.getItemTwoTitle());
				holiday.setItemTwoCont(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMTWOCONT)));
				// Log.i("zhu", "2.name=" + holiday.getItemTwoCont());
				holiday.setItemThreeTitle(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMTHREETITLE)));
				// Log.i("zhu", "3.name=" + holiday.getItemThreeTitle());
				holiday.setItemThreeCont(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMTHREECONT)));
				// Log.i("zhu", "3.name=" + holiday.getItemThreeCont());
				holiday.setItemFourTitle(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMFOURTITLE)));
				// Log.i("zhu", "4.name=" + holiday.getItemFourTitle());
				holiday.setItemFourCont(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMFOURCONT)));
				// Log.i("zhu", "4.name=" + holiday.getItemFourCont());

			} else {
				Log.i("zhu", "query failure!");
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return holiday;
	}

	private List<String> querySms(int h_id) {

		List<String> smsList = new ArrayList<String>();
		Sms sms = null;
		Cursor c = null;
		try {
			c = getContentResolver().query(Provider.SmsColumns.CONTENT_URI,
					new String[] { Provider.SmsColumns.SMSCONT, },
					Provider.SmsColumns.HOLIDAYID + "=?",
					new String[] { h_id + "" }, null);
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				sms = new Sms();
				sms.setSms_cont(c.getString(c
						.getColumnIndexOrThrow(Provider.SmsColumns.SMSCONT)));
				smsList.add(sms.getSms_cont());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return smsList;
	}

	private List<Sms> querySmsObj(int h_id) {
		Log.v("zhu", "hid=" + h_id);

		List<Sms> smsListObj = new ArrayList<Sms>();
		Sms sms = null;
		Cursor c = null;
		try {
			c = getContentResolver().query(
					Provider.SmsColumns.CONTENT_URI,
					new String[] { Provider.SmsColumns.SMSID,
							Provider.SmsColumns.SMSCONT },
					Provider.SmsColumns.HOLIDAYID + "=?",
					new String[] { h_id + "" }, null);
			Log.v("zhu",
					"节日" + h_id + "有短信" + c.getCount() + "条" + c.moveToFirst());

			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				sms = new Sms();
				sms.setSms_cont(c.getString(c
						.getColumnIndexOrThrow(Provider.SmsColumns.SMSCONT)));
				sms.setSms_id(c.getInt(c
						.getColumnIndexOrThrow(Provider.SmsColumns.SMSID)));
				sms.setHoliday(c.getInt(c
						.getColumnIndexOrThrow(Provider.SmsColumns.HOLIDAYID)));
				Log.i("zhu",
						"querySmsObj sms.getSms_cont()=" + sms.getSms_cont());
				smsListObj.add(sms);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		Log.v("zhu", "smsListObj=" + smsListObj.size());
		return smsListObj;
	}

	private int delSms(int h_id) {
		int delCount = 0;
		try {
			delCount = getContentResolver().delete(
					Provider.SmsColumns.CONTENT_URI,
					Provider.SmsColumns.HOLIDAYID + "=?",
					new String[] { h_id + "" });
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return delCount;
	}

	private List<String> queryMms(int h_id) {

		List<String> mmsList = new ArrayList<String>();
		Mms mms = null;
		Cursor c = null;
		try {
			c = getContentResolver().query(
					Provider.MmsColumns.CONTENT_URI,
					new String[] { Provider.MmsColumns.MMSCONT,
							Provider.MmsColumns.MMSPIC },
					Provider.SmsColumns.HOLIDAYID + "=?",
					new String[] { h_id + "" }, null);
			while (c.moveToNext()) {
				mms = new Mms();
				mms.setMms_cont(c.getString(c
						.getColumnIndexOrThrow(Provider.MmsColumns.MMSCONT)));
				mms.setMms_pic(c.getString(c
						.getColumnIndexOrThrow(Provider.MmsColumns.MMSPIC)));
				mmsList.add(mms.getMms_cont());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return mmsList;
	}

	private List<HolidayItem> getHolidayInfoFromXml(Context pContext, int h_id) {

		String HOLIDAY = "holiday";
		String NAME = "name";
		String ITEM1NAME = "item1name";
		String ITEM1VALUE = "item1value";
		String ITEM2NAME = "item2name";
		String ITEM2VALUE = "item2value";
		String ITEM3NAME = "item3name";
		String ITEM3VALUE = "item3value";
		String ITEM4NAME = "item4name";
		String ITEM4VALUE = "item4value";
		String ITEM5NAME = "item5name";
		String ITEM5VALUE = "item5value";
		String ITEM6NAME = "item6name";
		String ITEM6VALUE = "item6value";

		Holiday myholiday = queryHoliday(h_id);

		List<HolidayItem> holidays = new ArrayList<HolidayItem>();
		HolidayItem holiday = new HolidayItem();
		holiday.setHolidayName(myholiday.getH_name());
		holiday.setHolidayItem2Name(myholiday.getItemOneTitle());
		holiday.setHolidayItem2Value(myholiday.getItemOneCont());

		holiday.setHolidayItem3Name(myholiday.getItemTwoTitle());
		holiday.setHolidayItem3Value(myholiday.getItemTwoCont());

		holiday.setHolidayItem4Name(myholiday.getItemThreeTitle());
		holiday.setHolidayItem4Value(myholiday.getItemThreeCont());

		holiday.setHolidayItem5Name(myholiday.getItemFourTitle());
		holiday.setHolidayItem5Value(myholiday.getItemFourCont());

		List<String> smsList = querySms(h_id);
		Log.v("zhu", "smsList.size=" + smsList.size());

		holiday.setHolidayItem1Name("短信");
		holiday.setHolidayItem1Value(smsList);

		List<String> mmsList = queryMms(h_id);
		holiday.setHolidayItem6Value(mmsList);

		// DocumentBuilderFactory factory = null;
		// DocumentBuilder builder = null;
		// Document document = null;
		// InputStream inputStream = null;
		// // 首先找到xml文件
		// factory = DocumentBuilderFactory.newInstance();
		// try {
		// // 找到xml，并加载文档
		// builder = factory.newDocumentBuilder();
		//
		//
		// try {
		// inputStream = pContext.openFileInput(fileName);
		// } catch (FileNotFoundException e) {
		// inputStream = pContext.getResources().getAssets()
		// .open(fileName);
		// }
		//
		// document = builder.parse(inputStream);
		// // 找到根Element
		// Element root = document.getDocumentElement();
		// NodeList nodes = root.getElementsByTagName(HOLIDAY);
		// Log.v("heiheifang", "nodes.getLength() " + nodes.getLength());
		// // 遍历根节点所有子节点,rivers 下所有river
		// HolidayItem holiday = null;
		// for (int i = 0; i < nodes.getLength(); i++) {
		// holiday = new HolidayItem();
		// // 获取river元素节点
		// Element holidayElement = (Element) (nodes.item(i));
		// // 获取river中name属性值
		// Log.i("xiao", "name = " + holidayElement.getAttribute(NAME));
		// holiday.setHolidayName(holidayElement.getAttribute(NAME));
		//
		// Element holidayItem1 = (Element) holidayElement
		// .getElementsByTagName(ITEM1NAME).item(0);
		// holiday.setHolidayItem1Name(holidayItem1.getAttribute(NAME));
		// NodeList item1ValueNodes = holidayItem1
		// .getElementsByTagName(ITEM1VALUE);
		// List<String> item1ValueList = new ArrayList<String>();
		// for (int j = 0; j < item1ValueNodes.getLength(); j++) {
		// Element item1Element = (Element) (item1ValueNodes.item(j));
		// item1ValueList.add(item1Element.getFirstChild()
		// .getNodeValue());
		// }
		// holiday.setHolidayItem1Value(item1ValueList);
		//
		// Element holidayItem2Name = (Element) holidayElement
		// .getElementsByTagName(ITEM2NAME).item(0);
		// Log.i("xiao", "name2 = " + holidayItem2Name.getAttribute(NAME));
		// holiday.setHolidayItem2Name(holidayItem2Name.getAttribute(NAME));
		// Element holidayItem2Value = (Element) holidayItem2Name
		// .getElementsByTagName(ITEM2VALUE).item(0);
		// holiday.setHolidayItem2Value(holidayItem2Value.getFirstChild()
		// .getNodeValue());
		//
		// Element holidayItem3Name = (Element) holidayElement
		// .getElementsByTagName(ITEM3NAME).item(0);
		// holiday.setHolidayItem3Name(holidayItem3Name.getAttribute(NAME));
		// Log.i("xiao", "name3 = " + holidayItem3Name.getAttribute(NAME));
		// Element holidayItem3Value = (Element) holidayItem3Name
		// .getElementsByTagName(ITEM3VALUE).item(0);
		// holiday.setHolidayItem3Value(holidayItem3Value.getFirstChild()
		// .getNodeValue());
		//
		// Element holidayItem4Name = (Element) holidayElement
		// .getElementsByTagName(ITEM4NAME).item(0);
		// holiday.setHolidayItem4Name(holidayItem4Name.getAttribute(NAME));
		// Element holidayItem4Value = (Element) holidayItem4Name
		// .getElementsByTagName(ITEM4VALUE).item(0);
		// holiday.setHolidayItem4Value(holidayItem4Value.getFirstChild()
		// .getNodeValue());
		//
		// Element holidayItem5Name = (Element) holidayElement
		// .getElementsByTagName(ITEM5NAME).item(0);
		// holiday.setHolidayItem5Name(holidayItem5Name.getAttribute(NAME));
		// Element holidayItem5Value = (Element) holidayItem5Name
		// .getElementsByTagName(ITEM5VALUE).item(0);
		// holiday.setHolidayItem5Value(holidayItem5Value.getFirstChild()
		// .getNodeValue());
		//
		// Element holidayItem6 = (Element) holidayElement
		// .getElementsByTagName(ITEM6NAME).item(0);
		// if (holidayItem6 != null) {
		// Log.v("heiheifang", "hhhhhhhhhhhhhhhhhhhhh");
		// holiday.setHolidayItem6Name(holidayItem6.getAttribute(NAME));
		// NodeList item6ValueNodes = holidayItem6
		// .getElementsByTagName(ITEM6VALUE);
		// Log.v("heiheifang", "item6ValueNodes " +
		// item6ValueNodes.getLength());
		//
		// List<String> item6ValueList = new ArrayList<String>();
		// for (int j = 0; j < item6ValueNodes.getLength(); j++) {
		// Element item6Element = (Element) (item6ValueNodes
		// .item(j));
		// item6ValueList.add(item6Element.getFirstChild()
		// .getNodeValue());
		// }
		// holiday.setHolidayItem6Value(item6ValueList);
		// }

		holidays.add(holiday);
		// }
		// } catch (SAXException e) {
		// e.printStackTrace();
		// Log.i("xiao", "SAXException " + e.getMessage());
		// } catch (ParserConfigurationException e) {
		// Log.i("xiao", "ParserConfigurationException " + e.getMessage());
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO 自动生成的 catch 块
		// e.printStackTrace();
		// } finally {
		// try {
		// if (inputStream != null) {
		// inputStream.close();
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		return holidays;
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
			// setNetwork();
			Log.v("zhu", "开启网络更新内容");
		} else {
			isNetworkAvailable();
		}
		Log.v("zhu", "zhu" + flag);

		return flag;
	}

	/**
	 * 网络未连接时，调用设置方法
	 */
	// private void setNetwork() {
	// Toast.makeText(this, "wifi is closed!", Toast.LENGTH_SHORT).show();
	//
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// builder.setIcon(R.drawable.ic_launcher);
	// builder.setTitle("网络提示信息");
	// builder.setMessage("网络不可用，如果继续，请先设置网络！");
	// builder.setPositiveButton("设置", new OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// Intent intent = null;
	// /**
	// * 判断手机系统的版本！如果API大于10 就是3.0+ 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
	// */
	// if (android.os.Build.VERSION.SDK_INT > 10) {
	// intent = new Intent(
	// android.provider.Settings.ACTION_WIFI_SETTINGS);
	// } else {
	// intent = new Intent();
	// ComponentName component = new ComponentName(
	// "com.android.settings",
	// "com.android.settings.WirelessSettings");
	// intent.setComponent(component);
	// intent.setAction("android.intent.action.VIEW");
	// }
	// startActivity(intent);
	// }
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	//
	// builder.setNegativeButton("取消", new OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// }
	// });
	// builder.create();
	// builder.show();
	// }

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

	private void getDataFromServer(String url, int item) {
		url += "&parameter=up&item=" + item;
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
					if (!out.equals("unneed")) {
						try {
							JSONObject jsonObjectHolidayArray = new JSONObject(
									out);
							Holiday holiday = constructorJsonStringToObject(jsonObjectHolidayArray
									.get("holiday").toString());
							int h = updateHoliday(holiday);
							int h_id = holiday.getH_id();
							Log.v("zhu", "得到的节日id是" + h_id);
							List<Sms> getSmsList = holiday.getSmslist();
							delSms(h_id);
							if (getSmsList != null) {
								for (Sms sms : getSmsList) {
									insertSms(sms);
								}
							}
							List<Mms> getMmsList = holiday.getMmslist();
							delMms(h_id);
							if (getMmsList != null) {
								for (Mms mms : getMmsList) {
									insertMms(mms);
								}
							}
							sendMsgToServer(urlToServer,h_id);
						} catch (JSONException e) {
							e.printStackTrace();
						}
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

	private void sendMsgToServer(String urlToServer, int h_id) {
		url += "&parameter=commit&item=" + h_id;
		Log.v("zhu", "url=" + urlToServer);
		HttpClient client = new DefaultHttpClient();
		Log.v("zhu", "client=" + client);
		HttpPost request;
		try {
			request = new HttpPost(new URI(urlToServer));
			Log.v("zhu", "request=" + request);
			HttpResponse response = client.execute(request);
			Log.v("zhu", "response=" + response);
			// 判断请求是否成功
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity);
					Log.v("zhu","commit 返回内容为"+out);
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

	private int delMms(int h_id) {
		int delCount = 0;
		try {
			delCount = getContentResolver().delete(
					Provider.MmsColumns.CONTENT_URI,
					Provider.MmsColumns.HOLIDAYID + "=?",
					new String[] { h_id + "" });
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return delCount;
	}

	protected int insertSms(Sms sms) {
		Log.v("zhu", "插入sms");
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

	private int updateHoliday(Holiday holiday) {
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
		String where = Provider.HolidayColumns.HOLIDAYID + " = ? ";
		String[] selectionArgs = new String[] { holiday.getH_id() + "" };
		int rowid = getContentResolver().update(
				Provider.HolidayColumns.CONTENT_URI, values, where,
				selectionArgs);

		return rowid;
	}

	private int updateSms(Sms sms) {
		ContentValues values = new ContentValues();
		values.put(Provider.SmsColumns.SMSCONT, sms.getSms_cont());
		values.put(Provider.SmsColumns.HOLIDAYID, sms.getHoliday());
		String where = Provider.SmsColumns.SMSID + " = ? ";
		String[] selectionArgs = new String[] { sms.getSms_id() + "" };
		Log.v("zhu", "zhu getsms_id" + sms.getSms_id());
		int rowid = getContentResolver().update(
				Provider.SmsColumns.CONTENT_URI, values, where, selectionArgs);

		return rowid;
	}

	private int updateMms(Mms mms) {
		ContentValues values = new ContentValues();
		values.put(Provider.MmsColumns.MMSCONT, mms.getMms_cont());
		values.put(Provider.MmsColumns.HOLIDAYID, mms.getHoliday());
		values.put(Provider.MmsColumns.MMSPIC, mms.getMms_pic());
		String where = Provider.MmsColumns.MMSID + " = ? ";
		String[] selectionArgs = new String[] { mms.getMms_id() + "" };
		int rowid = getContentResolver().update(
				Provider.MmsColumns.CONTENT_URI, values, where, selectionArgs);
		return rowid;
	}

	private void initDetailLayoutView() {
		if (checkNetworkState()) {
			getDataFromServer(url, mPresentClickItem + 1);
		}
		List<HolidayItem> holidayList = getHolidayInfoFromXml(getBaseContext(),
				mPresentClickItem + 1);
		Log.v("zhu", "mPresentClickItem" + mPresentClickItem);
		mHolidayItem = null;
		Log.i("xiao", "size = " + holidayList.size());
		if (holidayList.size() > 0) {
			Log.i("xiao", "size > 0");
			mHolidayItem = holidayList.get(0);
			mHolidayDetailArray[0].setText(mHolidayItem.getHolidayItem1Name()
					.trim());
			mHolidayDetailArray[1].setText(mHolidayItem.getHolidayItem2Name()
					.trim());
			mHolidayDetailArray[2].setText(mHolidayItem.getHolidayItem3Name()
					.trim());
			mHolidayDetailArray[3].setText(mHolidayItem.getHolidayItem4Name()
					.trim());
			mHolidayDetailArray[4].setText(mHolidayItem.getHolidayItem5Name()
					.trim());
			mHolidayOtherValue.setText(mHolidayItem.getHolidayItem2Value()
					.trim());
		}
		Log.i("XXX", mHolidayDetailArray[0].getText().toString());
		mMmsArray = new ArrayList<String>();
		if (mHolidayDetailArray[0].getText().toString().equals("短信")) {
			mMmsArray.addAll(mHolidayItem.getHolidayItem1Value());

		} else {
			if (mHolidayItem.getHolidayItem6Name() != null) {
				Log.v("zhu", "mHolidayItem.getHolidayItem6Name()"
						+ mHolidayItem.getHolidayItem6Name());
				mMmsArray.addAll(mHolidayItem.getHolidayItem6Value());

			} else {
				Log.v("zhu", "mHolidayItem.getHolidayItem6Name()"
						+ mHolidayItem.getHolidayItem6Name());
				mMmsArray.addAll(mHolidayItem.getHolidayItem1Value());
			}
		}
		mHolidayDetailAdapter = new HolidayDetailAdapter(mMmsArray,
				mHolidayItem.getHolidayName());

		mHolidayDetailIcon.setCompoundDrawablesWithIntrinsicBounds(null,
				mResources.getDrawable(mHolidayIconBigId[mPresentClickItem]),
				null, null);
		Log.i("xiao", "name = " + mHolidayItem.getHolidayName());
		mHolidayDetailIcon.setText(mHolidayItem.getHolidayName());
	}

	private void controlHolidayDetailItem() {
		for (int i = 0; i < 5; i++) {
			if (i == mPresentSelectionItem) {
				mHolidayDetailArray[i]
						.setBackgroundResource(R.drawable.holiday_one_selected);
			} else {
				mHolidayDetailArray[i]
						.setBackgroundResource(R.drawable.holiday_one);
			}
		}
	}

	private void controlHolidayDetailContent() {
		controlHolidayDetailContent(mPresentSelectionItem);
	}

	private void controlHolidayDetailContent(int i) {
		switch (i) {
		case 0:
			mHolidayMessageList.setVisibility(View.VISIBLE);
			mHolidayOtherValue.setVisibility(View.INVISIBLE);
			mHolidayMessageList.setAdapter(mHolidayDetailAdapter);
			Utility.setListViewHeightBasedOnChildren(mHolidayMessageList);
			break;
		case 1:
			mHolidayMessageList.setVisibility(View.INVISIBLE);
			mHolidayOtherValue.setVisibility(View.VISIBLE);
			mHolidayOtherValue.setText(mHolidayItem.getHolidayItem2Value());
			break;
		case 2:
			mHolidayMessageList.setVisibility(View.INVISIBLE);
			mHolidayOtherValue.setVisibility(View.VISIBLE);
			mHolidayOtherValue.setText(mHolidayItem.getHolidayItem3Value());
			break;
		case 3:
			mHolidayMessageList.setVisibility(View.INVISIBLE);
			mHolidayOtherValue.setVisibility(View.VISIBLE);
			mHolidayOtherValue.setText(mHolidayItem.getHolidayItem4Value());
			break;
		case 4:
			mHolidayMessageList.setVisibility(View.INVISIBLE);
			mHolidayOtherValue.setVisibility(View.VISIBLE);
			mHolidayOtherValue.setText(mHolidayItem.getHolidayItem5Value());
			break;
		}
	}

	public class HolidayDetailAdapter extends BaseAdapter {

		private List<String> mMessageList;
		private String mHolidayName;

		public HolidayDetailAdapter(List<String> pMessageList, String name) {
			mMessageList = pMessageList;
			mHolidayName = name;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mMessageList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mMessageList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (mHolidayDetailArray[0].getText().toString().equals("短信")) {
				View view = mInflater.inflate(R.layout.holiday_detail_item,
						null);
				TextView textView = (TextView) view
						.findViewById(R.id.mHolidayDetailItem);
				textView.setText(mMessageList.get(position));
				view.setOnClickListener(HolidayGrid.this);
				view.setTag(position);
				return view;
			} else {
				View view = mInflater.inflate(
						R.layout.holiday_detail_item_caixin, null);
				TextView textView = (TextView) view
						.findViewById(R.id.mHolidayDetailItem);
				textView.setText(mMessageList.get(position));
				ImageView imageView = (ImageView) view.findViewById(R.id.img);
				AssetManager assetManager = getAssets();
				InputStream inputStream = null;
				try {
					inputStream = assetManager
							.open(chineseToEnglish(mHolidayName) + (++position)
									+ ".gif");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				imageView.setBackgroundDrawable(Drawable.createFromStream(
						inputStream, "tackpic"));
				view.setOnClickListener(HolidayGrid.this);
				view.setTag(position);
				return view;
			}

		}

	}

	private String chineseToEnglish(String name) {
		String englishName = null;
		if (name.equals("春节"))
			englishName = "spring";
		if (name.equals("情人节"))
			englishName = "valentine";
		if (name.equals("元宵节"))
			englishName = "lantern";
		if (name.equals("端午节")) {
			englishName = "dragon";
		}
		if (name.equals("儿童节")) {
			englishName = "children";
		}
		if (name.equals("父亲节")) {
			englishName = "father";
		}
		if (name.equals("妇女节")) {
			englishName = "woman";
		}
		if (name.equals("感恩节")) {
			englishName = "thanks";
		}
		if (name.equals("光棍节")) {
			englishName = "bachelor";
		}
		if (name.equals("国庆节")) {
			englishName = "national";
		}
		if (name.equals("建党节")) {
			englishName = "found_party";
		}
		if (name.equals("建军节")) {
			englishName = "found_army";
		}
		if (name.equals("教师节")) {
			englishName = "teacher";
		}
		if (name.equals("劳动节")) {
			englishName = "work";
		}
		if (name.equals("母亲节")) {
			englishName = "mother";
		}
		if (name.equals("七夕节")) {
			englishName = "lover";
		}
		if (name.equals("青年节")) {
			englishName = "young";
		}
		if (name.equals("清明节")) {
			englishName = "tomb_sweep";
		}
		if (name.equals("圣诞节")) {
			englishName = "christmas";
		}
		if (name.equals("万圣节")) {
			englishName = "halloween";
		}
		if (name.equals("愚人节")) {
			englishName = "fool";
		}
		if (name.equals("植树节")) {
			englishName = "plant";
		}
		if (name.equals("中秋节")) {
			englishName = "mid_autumn";
		}
		if (name.equals("重阳节")) {
			englishName = "double_nine";
		}
		return englishName;
	}

	public static class Utility {
		public static void setListViewHeightBasedOnChildren(ListView listView) {
			ListAdapter listAdapter = listView.getAdapter();
			if (listAdapter == null) {
				// pre-condition
				return;
			}

			int totalHeight = 0;
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}

			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			listView.setLayoutParams(params);
		}
	}

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { // TODO
	 * Auto-generated method stub if (keyCode == KeyEvent.KEYCODE_BACK) {
	 * AlertDialog.Builder builder = new AlertDialog.Builder( HolidayGrid.this);
	 * builder.setTitle(R.string.exitdialog_title);
	 * builder.setMessage(R.string.exitdialog_content);
	 * builder.setPositiveButton(R.string.exitdialog_ok, new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) { //
	 * TODO Auto-generated method stub HolidayGrid.this.finish(); } });
	 * builder.setNegativeButton(R.string.exitdialog_cancel, new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) { //
	 * TODO Auto-generated method stub
	 * 
	 * } }); builder.show(); } return super.onKeyDown(keyCode, event); }
	 */
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& mHolidayDetailLayout.getVisibility() == View.VISIBLE) {
			mHolidayDetailLayout
					.startAnimation(mHolidayDetailTranslateDownAnimation);
			mHolidayDetailTranslateDownAnimation.setAnimationListener(this);
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	// 采用Handler+Thread+封装外部接口
	private void loadImage5(final String url, final int id) {
		// 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
		Drawable cacheImage = AsyncImageLoader3.loadDrawable(url,
				new ImageCallback() {
					// 请参见实现：如果第一次加载url时下面方法会执行
					public void imageLoaded(Drawable imageDrawable) {
						((ImageView) findViewById(id))
								.setImageDrawable(imageDrawable);
						Log.v("cacheImage", "imageDrawable" + imageDrawable);
					}
				});
		Log.v("cacheImage", "cacheImage" + cacheImage);
		if (cacheImage != null) {
			((ImageView) findViewById(id)).setImageDrawable(cacheImage);
		}
	}

}
