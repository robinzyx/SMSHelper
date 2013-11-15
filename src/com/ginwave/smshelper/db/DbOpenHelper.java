package com.ginwave.smshelper.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {
	//数据库名称
	private static final String DATABASE_NAME = "Smsmms.db";
	//数据库版本号
    private final static int DATABASE_VERSION = 1;
	private static final String colsmscontent = null;
	private static final String colsmsID = null;
	private String viewEmps;
	
	public DbOpenHelper(Context context){
		
		//调用父类构造方法创建数据库
      super(context, DATABASE_NAME,null,DATABASE_VERSION);
		
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql="create table holiday(id integer primary key autoincrement,name varchar(64),item1 varchar(64),item2 varchar(64),item3 varchar(64),item4 varchar(64),creat_time varchar(64),modify_time varchar(64))";
	    String table="create table sms(id integer primary key autoincrement,content varchar(64),constraint id_fk foreign key (id) references holiday (id))";
	    String table1="create table mms(id integer primary key autoincrement,icon varchar(64))";
	    
		db.execSQL(sql);
		db.execSQL(table);
		db.execSQL(table1);
		
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		

	}
	

}
