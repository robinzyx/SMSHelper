package com.ginwave.smshelper.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ginwave.smshelper.db.DbOpenHelper;
import com.ginwave.smshelper.service.SmsService;

public class SmsDao implements SmsService {
	private DbOpenHelper helper = null;

	public SmsDao(Context context) {
		helper = new DbOpenHelper(context);

	}
	@Override
	public boolean addSms(Object[] params) {
		// TODO Auto-generated method stub
		boolean flag1 = false;
		SQLiteDatabase database = null;
		try {
			String sql = "insert into sms(content) values(?)";
			database = helper.getWritableDatabase();
			database.execSQL(sql, params);
			flag1 = true;

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (database != null) {
				database.close();
			}

		}
		return flag1;
	}

	@Override
	public boolean deleteSms(Object[] params) {
		// TODO Auto-generated method stub
		boolean flag1 = false;
		SQLiteDatabase database = null;
		try {
			String sql = "delete from sms where id = ?";
			database = helper.getWritableDatabase();
			database.execSQL(sql, params);
			flag1 = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.close();
			}

		}
		return flag1;
	}

	@Override
	public boolean updateSms(Object[] params) {
		// TODO Auto-generated method stub
		boolean flag1 = false;
		SQLiteDatabase database = null;
		try {
			String sql = "update sms set content=? where id = ?";

			database = helper.getWritableDatabase();
			database.execSQL(sql, params);
			flag1 = true;

		} catch (Exception e) {

		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag1;
	}

	@Override
	public Map<String, String> viewSms(String[] selectionArgs) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase database = null;
		try {
			String sql = "select * from sms where id = ?";
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}

			}
		} catch (Exception e) {

		} finally {
			if (database != null) {
				database.close();
			}
		}
		return map;
	}

	@Override
	public List<Map<String, String>> listSmsyMaps(String[] selectionArgs) {
		// TODO Auto-generated method stub
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from sms";
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";

					}
					map.put(cols_name, cols_value);

				}
				list.add(map);
			}
		} catch (Exception e) {

		} finally {
			if (database != null) {
				database.close();
			}
		}
		return list;
	}

}



