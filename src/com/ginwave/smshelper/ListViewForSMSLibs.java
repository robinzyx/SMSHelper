package com.ginwave.smshelper;
import com.umeng.analytics.MobclickAgent;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.ginwave.smshelper.db.DBHelper;

import java.io.IOException;
import java.util.ArrayList;

public class ListViewForSMSLibs extends ListActivity implements
		OnScrollListener {
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
		setContentView(R.layout.listviewforsmslibs);
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
				intent.setClass(getApplicationContext(),
						SMSLibsSendActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("xm", mListData.get(arg2));
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

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
        Intent intent = getIntent();
        final String flag = intent.getStringExtra("flag");
        if(Integer.parseInt(flag)==0){
            mCursor = mSqLiteDatabase.query("spring", null, null, null, null, null,
                    null, null);
        }
        switch (Integer.parseInt(flag)){
            case 0:
                mCursor = mSqLiteDatabase.query("spring", null, null, null, null, null,
                        null, null);
                break;
            case 1:
                mCursor = mSqLiteDatabase.query("qingrenjie", null, null, null, null, null,
                        null, null);
                break;
            case 2:
                mCursor = mSqLiteDatabase.query("yuanxiaojie", null, null, null, null, null,
                        null, null);
                break;
        }

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

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ListViewForSMSLibs.this);
			builder.setTitle(R.string.exitdialog_title);
			builder.setMessage(R.string.exitdialog_content);
			builder.setPositiveButton(R.string.exitdialog_ok,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							ListViewForSMSLibs.this.finish();
						}
					});
			builder.setNegativeButton(R.string.exitdialog_cancel,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			builder.show();
		}
		return super.onKeyDown(keyCode, event);
	}*/
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
