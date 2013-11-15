package com.ginwave.smshelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class ReplyRecordProvider extends ContentProvider {

	public static final Uri CONTENT_URI = Uri
            .parse("content://com.ginwave.smshelper/records");
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mRecordDB;
	
	public interface DataColumn extends BaseColumns {
		static final String COLUMN_ID = "_id";
        static final String COLUMN_NUMBER = "number";
        static final String COLUMN_YEAR = "year";
        static final String COLUMN_MONTH = "month";
        static final String COLUMN_DAY = "day";
	}

	public static final Uri STATICS_CONTENT_URI = Uri
            .parse("content://com.ginwave.smshelper/statics");
	public interface StaticsDataColumn extends BaseColumns {
		static final String STATICS_COLUMN_ID = "_id";
        static final String STATICS_COLUMN_DATE = "date";
        static final String STATICS_COLUMN_NUMBER = "number";
        static final String STATICS_COLUMN_MESSAGE = "message";
        static final String STATICS_COLUMN_PHONENUMBER = "pnumber";
	}
	
	public static final Uri STATICS_DETAIL_CONTENT_URI = Uri
            .parse("content://com.ginwave.smshelper/static_detail");
	public interface StaticsDetailDataColumn extends BaseColumns {
		static final String STATICS_DETAIL_COLUMN_ID = "_id";
        static final String STATICS_DETAIL_COLUMN_NUMBER = "pnumber";
        static final String STATICS_DETAIL_COLUMN_STATE = "state";
	}
	
	public static String DATABASE_TABLE = "records";
	public static String STATICS_DATABASE_TABLE = "statics";
	public static String STATICS_DETAIL_DATABASE_TABLE = "static_detail";
	private static class DatabaseHelper extends SQLiteOpenHelper {
		private static final String DATABASE_NAME = "reply_record";
		private static final int DATABASE_VERSION = 4;
		private static final String DATABASE_CREATE = "CREATE TABLE records ( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "number TEXT, " +
                "year INTEGER, " + 
                "month INTEGER, " +
                "day INTEGER);";
		private static final String STATICS_DATABASE_CREATE = "CREATE TABLE statics ( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, " +
                "number INTEGER, " + 
                "message TEXT);";
		
		private static final String STATICS_DETAIL_DATABASE_CREATE = "CREATE TABLE static_detail ( " +
                "_id INTEGER, " +
                "pnumber TEXT, " +
                "state BOOLEAN);";
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			createStaticsTable(db);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS records");
			db.execSQL("DROP TABLE IF EXISTS statics");
			db.execSQL("DROP TABLE IF EXISTS static_detail");
			onCreate(db);
			//createStaticsTable(db);
		}
		
		public void createStaticsTable(SQLiteDatabase db){
			db.execSQL(STATICS_DATABASE_CREATE);
			db.execSQL(STATICS_DETAIL_DATABASE_CREATE);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		Log.i("xiao", "delete uri = " + uri.toString());
		int rowCount = 0;
		if(uri.toString().contains(ReplyRecordProvider.CONTENT_URI.toString())){
			Log.i("xiao", "delete contenturl");
			rowCount = mRecordDB.delete(DATABASE_TABLE, selection, selectionArgs);
		}
		if(uri.toString().contains(ReplyRecordProvider.STATICS_CONTENT_URI.toString())){
			Log.i("xiao", "delete staticUrl");
			rowCount = mRecordDB.delete(STATICS_DATABASE_TABLE, selection, selectionArgs);
		}
		if(uri.toString().contains(ReplyRecordProvider.STATICS_DETAIL_CONTENT_URI.toString())){
			Log.i("xiao", "delete static detail url");
			rowCount = mRecordDB.delete(STATICS_DETAIL_DATABASE_TABLE, selection, selectionArgs);
		}
		return rowCount;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		Uri newUri = null;
		Log.i("xiao", "insert uri = " + uri.toString());
		if(uri.toString().contains(ReplyRecordProvider.DATABASE_TABLE)){
			Log.i("xiao", "insert contentUrl");
			long rowId = mRecordDB.insert(DATABASE_TABLE, null, values);
			newUri = Uri.withAppendedPath(CONTENT_URI, ""+rowId);
		    getContext().getContentResolver().notifyChange(CONTENT_URI, null);
		}
		if(uri.toString().contains(ReplyRecordProvider.STATICS_DATABASE_TABLE)){
			Log.i("xiao", "insert else");
			long rowId = mRecordDB.insert(STATICS_DATABASE_TABLE, null, values);
			newUri = Uri.withAppendedPath(STATICS_CONTENT_URI, ""+rowId);
		    getContext().getContentResolver().notifyChange(STATICS_CONTENT_URI, null);
		}
		if(uri.toString().contains(ReplyRecordProvider.STATICS_DETAIL_DATABASE_TABLE)){
			Log.i("xiao", "insert detail");
			long rowId = mRecordDB.insert(STATICS_DETAIL_DATABASE_TABLE, null, values);
			newUri = Uri.withAppendedPath(STATICS_DETAIL_CONTENT_URI, ""+rowId);
		    getContext().getContentResolver().notifyChange(STATICS_DETAIL_CONTENT_URI, null);
		}
		return newUri;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mDbHelper = new DatabaseHelper(this.getContext());
		mRecordDB = mDbHelper.getWritableDatabase();
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Cursor c = null;
		Log.i("xiao", "query uri = " + uri.toString());
		if(uri.toString().contains(ReplyRecordProvider.DATABASE_TABLE)){
			Log.i("xiao", "query contenturl");
			c = mRecordDB.query(DATABASE_TABLE, null, selection, selectionArgs, sortOrder, null, null);
		}
		if(uri.toString().contains(ReplyRecordProvider.STATICS_DATABASE_TABLE)){
			Log.i("xiao", "query else");
			c = mRecordDB.query(STATICS_DATABASE_TABLE, null, selection, selectionArgs, sortOrder, null, null);
		}
		if(uri.toString().contains(ReplyRecordProvider.STATICS_DETAIL_DATABASE_TABLE)){
			c = mRecordDB.query(STATICS_DETAIL_DATABASE_TABLE, null, selection, selectionArgs, sortOrder, null, null);
		}
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
