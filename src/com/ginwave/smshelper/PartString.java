package com.ginwave.smshelper;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.util.Log;

public class PartString {

	String str = null;

	public List<String> getList(String str) {
		// 方法2 StringTokenizer方法的调用

		StringTokenizer st = new StringTokenizer(str, "/n");
		List<String> smsList = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			
			smsList.add(st.nextToken());
		}
//		Log.v("string", "list size=" + smsList.size());
		return smsList;
	}
	
	
}