package com.ginwave.smshelper;

import com.umeng.analytics.MobclickAgent;

import com.ginwave.smshelper.localsms.SmsReader;
import com.ginwave.smshelper.more.SetDataSource;
import com.ginwave.smshelper.more.SettingForSMS;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class ProviderTestActivity extends Activity {

	private ContentProvider mProvider;
	private String tag = "xiao";
	private String mNumber;
	private int mYear;
	private int mMonth;
	private int mDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		Cursor c = this.getContentResolver().query(
				ReplyRecordProvider.CONTENT_URI, null, null, null, null);
		if (c != null) {
			Log.i(tag, "c != null " + c.getCount());
			printCursor(c);
			c.close();
		}
		else{
			Log.i(tag, "c -=== null ");
		}
		insertValue();
		Cursor c1 = this.getContentResolver().query(
				ReplyRecordProvider.CONTENT_URI, null, null, null, null);
		printCursor(c1);
	}

	private void insertValue() {
		for (int i = 0; i < 10; i++) {
			ContentValues values = new ContentValues();
			values.put(ReplyRecordProvider.DataColumn.COLUMN_NUMBER,
					"i " + i);
			values.put(ReplyRecordProvider.DataColumn.COLUMN_YEAR, i);
			values.put(ReplyRecordProvider.DataColumn.COLUMN_MONTH, i);
			values.put(ReplyRecordProvider.DataColumn.COLUMN_DAY, i);
			this.getContentResolver().insert(
					ReplyRecordProvider.CONTENT_URI, values);
		}
	}

	private void printCursor(Cursor c) {
		int i = 0;
		while (c.moveToNext()) {
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
	}
}
