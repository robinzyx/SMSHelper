package com.ginwave.smshelper.more;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;

import com.ginwave.smshelper.ListViewForSMSLibsAdapter;
import com.ginwave.smshelper.R;
import com.ginwave.smshelper.db.DBHelper;
import com.ginwave.smshelper.localsms.LocalSmsSendActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class ListViewForAuto extends ListActivity implements OnScrollListener {

	private ListView mListView;
	public ArrayList<String> mListData = new ArrayList<String>();
	private ListViewForSMSLibsAdapter mAdapter;
	private DBHelper mDB;
	private SQLiteDatabase mSqLiteDatabase;
	private Cursor mCursor;
	private int visibleLastIndex = 0;
	private int visibleItemCount;

	private Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listviewforsmslibs_fromlocal);
		mListView = getListView();
		mDB = new DBHelper(getApplicationContext());
		initAdapter();
		setListAdapter(mAdapter);

		mListView.setOnScrollListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("value", mListData.get(arg2));
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				setResult(1, intent);
				finish();
			}
		});

	}

	private void initAdapter() {
		ArrayList<String> temp_items = new ArrayList<String>();
		try {
			mDB.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mSqLiteDatabase = mDB.openDataBase();
		mCursor = mSqLiteDatabase.query("spring", null, null, null, null, null,
				null, null);
		if (mCursor != null && mCursor.getCount() != 0) {
			while (mCursor.moveToNext()) {
				mListData.add(mCursor.getString(mCursor
						.getColumnIndex("message")));
			}

		}
		mCursor.close();
		for (int i = 0; i < mListData.size(); i++) {
			temp_items.add(mListData.get(i));
		}
		mAdapter = new ListViewForSMSLibsAdapter(this, temp_items);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = mAdapter.getCount() - 1;
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			loadMore(view);
		}
	}

	public void loadMore(View view) {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				loadData();
				mAdapter.notifyDataSetChanged();
			}
		}, 2000);
	}

	private void loadData() {
		int count = mAdapter.getCount();
		int j = mListData.size();
		for (int i = count; i <= j - count; i++) {
			mAdapter.addItem(mListData.get(i));
		}
	}

	// @Override
	// protected void onStart() {
	// // TODO Auto-generated method stub
	// super.onStart();
	// try{
	// ActionBar actionBar = this.getActionBar();
	// actionBar.setDisplayHomeAsUpEnabled(true);
	// } catch(Exception e) {
	//
	// }
	//
	//
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // TODO Auto-generated method stub
	// switch (item.getItemId()) {
	// case android.R.id.home:
	// Intent intent = new Intent(this, MainActivity.class);
	// startActivity(intent);
	// finish();
	// return true;
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	//
	// }

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
