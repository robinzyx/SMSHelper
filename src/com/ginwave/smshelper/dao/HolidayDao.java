package com.ginwave.smshelper.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ginwave.smshelper.db.DbOpenHelper;
import com.ginwave.smshelper.service.HolidayService;

public class HolidayDao implements HolidayService {

	private DbOpenHelper helper = null;

	public HolidayDao(Context context) {
		helper = new DbOpenHelper(context);

	}

	@Override
	public boolean addHoliday(Object[] params) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			String sql = "insert into holiday(name,item1,item2,item3,item4) values(?,?,?,?,?)";
			database = helper.getWritableDatabase();
			database.execSQL(sql, params);
			flag = true;

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (database != null) {
				database.close();
			}

		}
		return flag;
	}

	@Override
	public boolean deleteHoliday(Object[] params) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			String sql = "delete from holiday where id = ?";
			database = helper.getWritableDatabase();
			database.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.close();
			}

		}
		return flag;
	}

	@Override
	public boolean updateHoliday(Object[] params) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			String sql = "update holiday set name=?,item1=?,item2=?,item3=?,item4=? where id = ?";

			database = helper.getWritableDatabase();
			database.execSQL(sql, params);
			flag = true;

		} catch (Exception e) {

		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public Map<String, String> viewHoliday(String[] selectionArgs) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase database = null;
		try {
			String sql = "select * from holiday where id = ?";
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
	public List<Map<String, String>> listHolidayMaps(String[] selectionArgs) {
		// TODO Auto-generated method stub
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from holiday";
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
