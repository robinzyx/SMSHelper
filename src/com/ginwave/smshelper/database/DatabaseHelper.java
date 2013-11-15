package com.ginwave.smshelper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ginwave.smshelper.provider.Provider;

/**
 * 直接操作数据库类
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "sms.db";
	private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL("create table IF NOT EXISTS holiday(h_id INTEGER  PRIMARY KEY AUTOINCREMENT,"
				+ "create_time date,"
				+ "h_name varchar(18),"
				+ "icon varchar(255),"
				+ "is_up bit(1),"
				+ "itemOneTitle varchar(20),"
				+ "itemOneCont varchar(3000),"
				+ "itemTwoTitle varchar(20),"
				+ "itemTwoCont varchar(3000),"
				+ "itemThreeTitle varchar(20),"
				+ "itemThreeCont varchar(3000),"
				+ "itemFourTitle varchar(20),"
				+ "itemFourCont varchar(3000),"
				+ "modify_time date,"
				+ "month varchar(255)," + "type varchar(20))");
		db.execSQL("create table IF NOT EXISTS sms(sms_id INTEGER  PRIMARY KEY AUTOINCREMENT,"
				+ "sms_cont varchar(255),"
				+ "h_id INTEGER,"
				+ "FOREIGN KEY(h_id) REFERENCES holiday(h_id))");
		db.execSQL("create table IF NOT EXISTS mms(mms_id INTEGER  PRIMARY KEY AUTOINCREMENT,"
				+ "mms_cont varchar(255),"
				+ "mms_pic varchar(255),"
				+ "h_id INTEGER,"
				+ "FOREIGN KEY(h_id) REFERENCES holiday(h_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS holiday");
        db.execSQL("DROP TABLE IF EXISTS sms");
        db.execSQL("DROP TABLE IF EXISTS mms");
        onCreate(db);
    }
}