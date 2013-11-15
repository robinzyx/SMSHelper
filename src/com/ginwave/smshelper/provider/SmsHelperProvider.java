package com.ginwave.smshelper.provider;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.ginwave.smshelper.database.DatabaseHelper;

/**
 * 对programmer和leader表进行操作的ContentProvider
 * 
 * @author jacp
 * 
 */
public class SmsHelperProvider extends ContentProvider {

	private static HashMap<String, String> sHolidayProjectionMap;
	private static HashMap<String, String> sSmsProjectionMap;
	private static HashMap<String, String> sMmsProjectionMap;

	private static final int HOLIDAYS = 1;
	private static final int HOLIDAYS_ID = 2;

	// 这里要增加匹配项
	private static final int SMSS = 3;
	private static final int SMSS_ID = 4;

	private static final int MMSS = 5;
	private static final int MMSS_ID = 6;

	private static final UriMatcher sUriMatcher;

	private DatabaseHelper mOpenHelper;

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String orderBy = null;
		switch (sUriMatcher.match(uri)) { // 这里要对不同表的匹配结果做不同处理
		case HOLIDAYS:
		case HOLIDAYS_ID:
			qb.setTables(Provider.HolidayColumns.TABLE_NAME);
			break;
		case SMSS:
		case SMSS_ID:
			qb.setTables(Provider.SmsColumns.TABLE_NAME);
			break;
		case MMSS:
		case MMSS_ID:
			qb.setTables(Provider.MmsColumns.TABLE_NAME);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		switch (sUriMatcher.match(uri)) {
		case HOLIDAYS:
			qb.setProjectionMap(sHolidayProjectionMap);
			break;
		case MMSS:
			qb.setProjectionMap(sMmsProjectionMap);
			break;
		case SMSS:
			qb.setProjectionMap(sSmsProjectionMap);
			break;

		case HOLIDAYS_ID:
			qb.setProjectionMap(sHolidayProjectionMap);
			qb.appendWhere(Provider.HolidayColumns.HOLIDAYID + "="
					+ uri.getPathSegments().get(1));
			break;

		case MMSS_ID:
			qb.setProjectionMap(sMmsProjectionMap);
			qb.appendWhere(Provider.MmsColumns.MMSID + "="
					+ uri.getPathSegments().get(1));
			break;
		case SMSS_ID:
			qb.setProjectionMap(sSmsProjectionMap);
			qb.appendWhere(Provider.SmsColumns.SMSID + "="
					+ uri.getPathSegments().get(1));
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// Get the database and run the query
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);

		// Tell the cursor what uri to watch, so it knows when its source data
		// changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) { // 这里也要增加匹配项
		case HOLIDAYS:
		case MMSS:
		case SMSS:
			return Provider.CONTENT_TYPE;
		case HOLIDAYS_ID:
		case MMSS_ID:
		case SMSS_ID:
			return Provider.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		final ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		String tableName = "";
		String nullColumn = "";
		final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>(); 
//	    ContentValues value = new ContentValues();
		switch (sUriMatcher.match(uri)) { // 这里要对不同表的匹配结果做不同处理
		case HOLIDAYS:
			
			tableName = Provider.HolidayColumns.TABLE_NAME;
			nullColumn = Provider.HolidayColumns.HOLIDAYNAME;
			  
		    
			// Make sure that the fields are all set
			if (values.containsKey(Provider.HolidayColumns.HOLIDAYNAME) == false) {
				values.put(Provider.HolidayColumns.HOLIDAYNAME, "");
			}

			if (values.containsKey(Provider.HolidayColumns.CREATETIME) == false) {
				values.put(Provider.HolidayColumns.CREATETIME, "");
			}

			if (values.containsKey(Provider.HolidayColumns.MODIFYTIME) == false) {
				values.put(Provider.HolidayColumns.MODIFYTIME, "");
			}

			if (values.containsKey(Provider.HolidayColumns.MONTH) == false) {
				values.put(Provider.HolidayColumns.MONTH, "");
			}
			if (values.containsKey(Provider.HolidayColumns.ICON) == false) {
				values.put(Provider.HolidayColumns.ICON, "");
			}
			if (values.containsKey(Provider.HolidayColumns.ISUP) == false) {
				values.put(Provider.HolidayColumns.ISUP, "");
			}
			if (values.containsKey(Provider.HolidayColumns.TYPE) == false) {
				values.put(Provider.HolidayColumns.TYPE, "");
			}
			if (values.containsKey(Provider.HolidayColumns.ITEMONETITLE) == false) {
				values.put(Provider.HolidayColumns.ITEMONETITLE, "");
			}
			if (values.containsKey(Provider.HolidayColumns.ITEMONECONT) == false) {
				values.put(Provider.HolidayColumns.ITEMONECONT, "");
			}
			if (values.containsKey(Provider.HolidayColumns.ITEMTWOTITLE) == false) {
				values.put(Provider.HolidayColumns.ITEMTWOTITLE, "");
			}
			if (values.containsKey(Provider.HolidayColumns.ITEMTWOCONT) == false) {
				values.put(Provider.HolidayColumns.ITEMTWOCONT, "");
			}
			if (values.containsKey(Provider.HolidayColumns.ITEMTHREETITLE) == false) {
				values.put(Provider.HolidayColumns.ITEMTHREETITLE, "");
			}
			if (values.containsKey(Provider.HolidayColumns.ITEMTHREECONT) == false) {
				values.put(Provider.HolidayColumns.ITEMTHREECONT, "");
			}
			if (values.containsKey(Provider.HolidayColumns.ITEMFOURTITLE) == false) {
				values.put(Provider.HolidayColumns.ITEMFOURTITLE, "");
			}
			if (values.containsKey(Provider.HolidayColumns.ITEMFOURCONT) == false) {
				values.put(Provider.HolidayColumns.ITEMFOURCONT, "");
			}
			 
			break;
		case MMSS:
			tableName = Provider.MmsColumns.TABLE_NAME;
			nullColumn = Provider.MmsColumns.MMSCONT;
			// Make sure that the fields are all set
			if (values.containsKey(Provider.MmsColumns.MMSCONT) == false) {
				values.put(Provider.MmsColumns.MMSCONT, "");
			}
			if (values.containsKey(Provider.MmsColumns.HOLIDAYID) == false) {
				values.put(Provider.MmsColumns.HOLIDAYID, "");
			}
			

			break;
		case SMSS:
			tableName = Provider.SmsColumns.TABLE_NAME;
			nullColumn = Provider.SmsColumns.SMSCONT;
			// Make sure that the fields are all set
			if (values.containsKey(Provider.SmsColumns.SMSCONT) == false) {
				values.put(Provider.SmsColumns.SMSCONT, "");
			}
			if (values.containsKey(Provider.SmsColumns.HOLIDAYID) == false) {
				values.put(Provider.SmsColumns.HOLIDAYID, "");
			}
		
			
			break;
		default:
			// Validate the requested uri
			throw new IllegalArgumentException("Unknown URI " + uri);

		}
		

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(tableName, nullColumn, values);
		
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(uri, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) { // 这里要对不同表的匹配结果做不同处理，注意下面用到的表名不要弄错了
		case HOLIDAYS:
			count = db.delete(Provider.HolidayColumns.TABLE_NAME, where,
					whereArgs);
			break;

		case HOLIDAYS_ID:
			String holidayId = uri.getPathSegments().get(1);
			count = db.delete(Provider.HolidayColumns.TABLE_NAME,
					Provider.HolidayColumns.HOLIDAYID
							+ "="
							+ holidayId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		case MMSS:
			count = db.delete(Provider.MmsColumns.TABLE_NAME, where, whereArgs);
			break;

		case MMSS_ID:
			String mmsId = uri.getPathSegments().get(1);
			count = db.delete(
					Provider.MmsColumns.TABLE_NAME,
					Provider.MmsColumns.MMSID
							+ "="
							+ mmsId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case SMSS:
			count = db.delete(Provider.SmsColumns.TABLE_NAME, where, whereArgs);
			break;

		case SMSS_ID:
			String smsId = uri.getPathSegments().get(1);
			count = db.delete(
					Provider.SmsColumns.TABLE_NAME,
					Provider.SmsColumns.SMSID
							+ "="
							+ smsId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) { // 这里要对不同表的匹配结果做不同处理，注意下面用到的表名不要弄错了
		case HOLIDAYS:
			count = db.update(Provider.HolidayColumns.TABLE_NAME, values,
					where, whereArgs);
			break;

		case HOLIDAYS_ID:
			String holidayId = uri.getPathSegments().get(1);
			count = db.update(Provider.HolidayColumns.TABLE_NAME, values,
					Provider.HolidayColumns.HOLIDAYID
							+ "="
							+ holidayId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case MMSS:
			count = db.update(Provider.MmsColumns.TABLE_NAME, values, where,
					whereArgs);
			break;

		case MMSS_ID:
			String mmsId = uri.getPathSegments().get(1);
			count = db.update(
					Provider.MmsColumns.TABLE_NAME,
					values,
					Provider.MmsColumns.MMSID
							+ "="
							+ mmsId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case SMSS:
			count = db.update(Provider.SmsColumns.TABLE_NAME, values, where,
					whereArgs);
			break;

		case SMSS_ID:
			String smsId = uri.getPathSegments().get(1);
			Log.v("zhu", "smsId"+smsId);
			count = db.update(
					Provider.SmsColumns.TABLE_NAME,
					values,
					Provider.SmsColumns.SMSID
							+ "="
							+ smsId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	

	@Override
	public ContentProviderResult[] applyBatch(
			ArrayList<ContentProviderOperation> operations)
			throws OperationApplicationException {
		 SQLiteDatabase db = mOpenHelper.getWritableDatabase(); 
         db.beginTransaction();//开始事务 
         Log.v("zhu", "事务处理 开始 is operations"+System.currentTimeMillis()+operations);
         try{ 
        	
                  ContentProviderResult[]results = super.applyBatch(operations); 
                  db.setTransactionSuccessful();//设置事务标记为successful 
                  return results; 
         }finally { 
                  db.endTransaction();//结束事务
                  Log.v("zhu", "事务处理 结束 is operations"+operations+System.currentTimeMillis());
         } 
	}



	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(Provider.AUTHORITY, "holidays", HOLIDAYS);
		sUriMatcher.addURI(Provider.AUTHORITY, "holidays/#", HOLIDAYS_ID);

		// 这里要增加另一张表的匹配项
		sUriMatcher.addURI(Provider.AUTHORITY, "smss", SMSS);
		sUriMatcher.addURI(Provider.AUTHORITY, "smss/#", SMSS_ID);

		sUriMatcher.addURI(Provider.AUTHORITY, "mmss", MMSS);
		sUriMatcher.addURI(Provider.AUTHORITY, "mmss/#", MMSS_ID);

		// 保存所有表用到的字段
		sHolidayProjectionMap = new HashMap<String, String>();
		sHolidayProjectionMap.put(Provider.HolidayColumns.HOLIDAYID,
				Provider.HolidayColumns.HOLIDAYID);
		sHolidayProjectionMap.put(Provider.HolidayColumns.HOLIDAYNAME,
				Provider.HolidayColumns.HOLIDAYNAME);
		sHolidayProjectionMap.put(Provider.HolidayColumns.CREATETIME,
				Provider.HolidayColumns.CREATETIME);
		sHolidayProjectionMap.put(Provider.HolidayColumns.CREATETIME,
				Provider.HolidayColumns.CREATETIME);
		sHolidayProjectionMap.put(Provider.HolidayColumns.MODIFYTIME,
				Provider.HolidayColumns.MODIFYTIME);
		sHolidayProjectionMap.put(Provider.HolidayColumns.ICON,
				Provider.HolidayColumns.ICON);
		sHolidayProjectionMap.put(Provider.HolidayColumns.ISUP,
				Provider.HolidayColumns.ISUP);
		sHolidayProjectionMap.put(Provider.HolidayColumns.TYPE,
				Provider.HolidayColumns.TYPE);
		sHolidayProjectionMap.put(Provider.HolidayColumns.MONTH,
				Provider.HolidayColumns.MONTH);
		sHolidayProjectionMap.put(Provider.HolidayColumns.ITEMONETITLE,
				Provider.HolidayColumns.ITEMONETITLE);
		sHolidayProjectionMap.put(Provider.HolidayColumns.ITEMONECONT,
				Provider.HolidayColumns.ITEMONECONT);
		sHolidayProjectionMap.put(Provider.HolidayColumns.ITEMTWOTITLE,
				Provider.HolidayColumns.ITEMTWOTITLE);
		sHolidayProjectionMap.put(Provider.HolidayColumns.ITEMTWOCONT,
				Provider.HolidayColumns.ITEMTWOCONT);
		sHolidayProjectionMap.put(Provider.HolidayColumns.ITEMTHREETITLE,
				Provider.HolidayColumns.ITEMTHREETITLE);
		sHolidayProjectionMap.put(Provider.HolidayColumns.ITEMTHREECONT,
				Provider.HolidayColumns.ITEMTHREECONT);
		sHolidayProjectionMap.put(Provider.HolidayColumns.ITEMFOURTITLE,
				Provider.HolidayColumns.ITEMFOURTITLE);
		sHolidayProjectionMap.put(Provider.HolidayColumns.ITEMFOURCONT,
				Provider.HolidayColumns.ITEMFOURCONT);

		sSmsProjectionMap = new HashMap<String, String>();
		sSmsProjectionMap.put(Provider.SmsColumns.SMSID,
				Provider.SmsColumns.SMSID);
		sSmsProjectionMap.put(Provider.SmsColumns.SMSCONT,
				Provider.SmsColumns.SMSCONT);
		sSmsProjectionMap.put(Provider.SmsColumns.HOLIDAYID,
				Provider.SmsColumns.HOLIDAYID);

		sMmsProjectionMap = new HashMap<String, String>();
		sMmsProjectionMap.put(Provider.MmsColumns.MMSID,
				Provider.MmsColumns.MMSID);
		sMmsProjectionMap.put(Provider.MmsColumns.MMSCONT,
				Provider.MmsColumns.MMSCONT);
		sMmsProjectionMap.put(Provider.MmsColumns.MMSPIC,
				Provider.MmsColumns.MMSPIC);
		sMmsProjectionMap.put(Provider.MmsColumns.HOLIDAYID,
				Provider.MmsColumns.HOLIDAYID);

	}

	
}
