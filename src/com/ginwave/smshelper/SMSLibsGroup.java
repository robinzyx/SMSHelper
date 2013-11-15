package com.ginwave.smshelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class SMSLibsGroup extends ActivityGroup {
	/** 設定成 static 讓其他的子 Activity 可以存取 */
	public static ActivityGroup group;
	/** Back Stack */
	private ArrayList<View> history;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.history = new ArrayList<View>();
		group = this;

		// ActivityGroup1 只是一個外框，在這個外框中載入其他要用的 Activity
		// 如果沒有這個外框會發生錯誤
		View view = getLocalActivityManager().startActivity(
				"Activity1",
				new Intent(SMSLibsGroup.this, ListViewForSMSLibs.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();
		// 馬上載入真正要執行的 Activity
		replaceView(view);
	}

	/**
	 * 在 ActivityGroup 中切換 Activity
	 * 
	 * @param v
	 */
	public void replaceView(View v) {
		// 可在這插入換頁動畫
		history.add(v);
		setContentView(v);
	}

	/**
	 * 當使用者按下 back 的時候，把之前存起來的 stack 撈回來顯示
	 */
	public void back() {
		// 原本的範例是寫 > 0，但會發生錯誤
		if (history.size() > 1) {
			history.remove(history.size() - 1);
			View v = history.get(history.size() - 1);
			// 可在這插入換頁動畫
			setContentView(v);
		} else {
			// back stack 沒有其他頁面可顯示，直接結束
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			back();
			break;
		}
		return true;
	}
public void onResume()
{
super.onResume();
MobclickAgent.onResume(this);
}
public void onPause()
{
super.onPause();
MobclickAgent.onPause(this);
}

}