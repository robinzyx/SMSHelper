package com.ginwave.smshelper;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.ginwave.smshelper.localsms.LocalSmsSendActivity;
import com.ginwave.smshelper.more.ListViewForAuto;
import com.ginwave.smshelper.more.SetDataSource;
import com.ginwave.smshelper.more.SettingForSMS;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SmsForwardDetailItemActivity extends Activity implements
		OnClickListener, OnItemClickListener, OnCreateContextMenuListener,
		OnItemLongClickListener{

	private TextView mMainBack;
	private TextView mMainTitle;
	private TextView mSmsForwardDetailUserPoint;
	private TextView mSmsForwardDetailUserRank;
	private ListView mSmsForwardDetailItemList;
	private String mSmsItemType;
	private String mSmsItemValue;
	private List<String> mSmsItemMessageList;
	private SmsForwardAdapter mAdapter;
	private LayoutInflater mInflater;
	private ResultHandler mResultHandler;
	private int mPresentPage = 1;
	private String mPresentState;
	private final int MENU_FORWARD = 0;
	private final int MENU_SHARE = 1;
	private int mPresentSelection = 0;
	private GetMmsTask mGetMmsTask;
	private ProgressBar mSmsForwardDetailitemProgressBar;
	private TextView mSmsForwardDetailitemMore;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sms_forward_detailitem);
		dataFromIntent();
		setupViews();
		mGetMmsTask = new GetMmsTask();
		mGetMmsTask.execute("");
		mResultHandler = new ResultHandler();
	}

	private void dataFromIntent() {
		Intent intent = this.getIntent();
		if(intent != null){
			mSmsItemValue = intent.getStringExtra("item_value");
			mSmsItemType = intent.getStringExtra("item_type");
			Log.i("xiao", "title = " + mSmsItemValue);
			Log.i("xiao", "mSmsItemType = " + mSmsItemType);
			
		}
	}
	
	private void setupViews() {
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mSmsItemMessageList = new ArrayList<String>();
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainTitle = (TextView) findViewById(R.id.mMainTitle);
		mSmsForwardDetailUserPoint = (TextView) findViewById(R.id.mSmsForwardDetailUserPoint);
		mSmsForwardDetailUserRank = (TextView) findViewById(R.id.mSmsForwardDetailUserRank);
		mSmsForwardDetailItemList = (ListView) findViewById(R.id.mSmsForwardDetailItemList);
		mMainBack.setOnClickListener(this);
		mMainTitle.setText(mSmsItemValue);
		mAdapter = new SmsForwardAdapter();
		mSmsForwardDetailItemList.addFooterView(mInflater.inflate(R.layout.sms_forward_detailitem_more, null));
		mSmsForwardDetailItemList.setAdapter(mAdapter);
		mSmsForwardDetailItemList.setOnItemClickListener(this);
		//mSmsForwardDetailItemList.setOnCreateContextMenuListener(this);
		//mSmsForwardDetailItemList.setOnItemLongClickListener(this);
	}
	
	public void loadMore(View view) {
		mPresentPage++;
		Log.i("xiao", "mPresentPage = " + mPresentPage);
		mSmsForwardDetailitemProgressBar = (ProgressBar)view.findViewById(R.id.mSmsForwardDetailitemProgressBar);
		mSmsForwardDetailitemMore = (TextView)view.findViewById(R.id.mSmsForwardDetailitemMore);
		mSmsForwardDetailitemProgressBar.setVisibility(View.VISIBLE);
		mSmsForwardDetailitemMore.setText(R.string.sms_forward_detailitem_load);
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SetDataSource.getPostResult(getApplicationContext(), SetDataSource.getSmsContentString(mSmsItemType, Integer.toString(mPresentPage)), "sms.xml");
				mPresentState = SetDataSource.getSmsListParseFromString(getApplicationContext(), "sms.xml", mSmsItemMessageList);
				mResultHandler.sendEmptyMessage(0);
			}
			
		}).start();
	}
	
	
	class ResultHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(mPresentState.equals("1") || mPresentState.equals("2")){
				Toast.makeText(getApplicationContext(), R.string.sms_forward_detailitem_no_data, Toast.LENGTH_LONG).show();
				return ;
			}
			else{
				mAdapter.notifyDataSetChanged();
			}
			if(mSmsForwardDetailitemProgressBar != null){
				mSmsForwardDetailitemProgressBar.setVisibility(View.INVISIBLE);
			}
			if(mSmsForwardDetailitemMore != null){
				mSmsForwardDetailitemMore.setText(R.string.sms_forward_detailitem_more);
			}
		}
	}
	
	
	private class GetMmsTask extends AsyncTask<String, String, String> {
		
		public String doInBackground(String... params) {
			SetDataSource.getPostResult(getApplicationContext(), SetDataSource.getSmsContentString(mSmsItemType, Integer.toString(mPresentPage)), "sms.xml");
			SetDataSource.getSmsListParseFromString(getApplicationContext(), "sms.xml", mSmsItemMessageList);
			for( int i = 0; i < mSmsItemMessageList.size(); i++){
				Log.i("xiao", "i = " + i + "Sms = " + mSmsItemMessageList.get(i));
			}
			return "";
		}

		@Override
		protected void onPreExecute() {
			//showDialog(DIALOG_KEY);
			showProgress();
		}

		@Override
		public void onPostExecute(String Re) {
			mAdapter.notifyDataSetChanged();
			hideProgress();
		}
	}
	
	private ProgressDialog mProgressDialog = null;
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("短信加载中...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		// We save off the progress dialog in a field so that we can dismiss
		// it later. We can't just call dismissDialog(0) because the system
		// can lose track of our dialog if there's an orientation change.
		mProgressDialog = dialog;
		return dialog;
	}
	
	private void hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
	
	private void showProgress() {
		onCreateDialog(0, null);
		mProgressDialog.show();
	}

	private class SmsForwardAdapter extends BaseAdapter {
		

		public SmsForwardAdapter() {
			// TODO Auto-generated constructor stub
			
		}

		public final class ViewHolder {
			public TextView mIndividualMessage;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mSmsItemMessageList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = new ViewHolder();
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.sms_forward_detail_item_item, null);
				viewHolder.mIndividualMessage = (TextView) convertView
						.findViewById(R.id.mSmsForwardDetailMessage);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.mIndividualMessage.setText(mSmsItemMessageList.get(position));
			convertView.setTag(viewHolder);
			/*convertView = mInflater.inflate(
					R.layout.sms_forward_detail_item, null);
			mViewHolder.mIndividualMessage = (TextView) convertView
					.findViewById(R.id.mSmsForwardDetailMessage);
			mViewHolder.mIndividualMessage.setText(mSmsItemMessageList.get(position));*/
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.equals(mMainBack)){
			this.finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.i("xiao", "onItemClick");
		mPresentSelection = arg2;
		createActionsDialog(this);
		Log.i("xiao", "mPresentSelection = " + mPresentSelection);
	}
	
	
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
		menu.add(0, MENU_FORWARD, 0, R.string.sms_forward);
		menu.add(0, MENU_SHARE, 0, R.string.set_share);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case MENU_FORWARD:
			Log.i("xiao", "FORWARD mPresentSelection = " + mPresentSelection + " value = " + mSmsItemMessageList.get(mPresentSelection));
			Intent intent1 = new Intent();
			intent1.putExtra("xm", mSmsItemMessageList.get(mPresentSelection));
			intent1.setClass(getApplicationContext(), LocalSmsSendActivity.class);
			this.startActivity(intent1);
			break;
		case MENU_SHARE:
			Log.i("xiao", "SHARE mPresentSelection = " + mPresentSelection + " value = " + mSmsItemMessageList.get(mPresentSelection));
			Intent intent2 = new Intent(Intent.ACTION_SEND);
			intent2.setType("text/plain");
			intent2.putExtra(Intent.EXTRA_SUBJECT, "短信分享");
			intent2.putExtra(Intent.EXTRA_TEXT, mSmsItemMessageList.get(mPresentSelection));
			startActivity(Intent.createChooser(intent2, getTitle()));
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		/*Log.i("xiao", "onItemLongClick");
		mPresentSelection = arg2;
		Log.i("xiao", "mPresentSelection = " + mPresentSelection);*/
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("xiao", "DetailItem onDestroy");
		mGetMmsTask.cancel(true);
		super.onDestroy();
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	public void createActionsDialog(final Context context) {
		Log.i("xiao", "createDredgeDialog");
		AlertDialog dialog = new AlertDialog.Builder(context).setCancelable(true)
				.setItems(R.array.sms_forward_actions, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch(which){
						case 0:
							Intent intent1 = new Intent();
							intent1.putExtra("xm", mSmsItemMessageList.get(mPresentSelection));
							intent1.setClass(getApplicationContext(), LocalSmsSendActivity.class);
							startActivity(intent1);
							break;
						case 1:
							Intent intent2 = new Intent(Intent.ACTION_SEND);
							intent2.setType("text/plain");
							intent2.putExtra(Intent.EXTRA_SUBJECT, "短信分享");
							intent2.putExtra(Intent.EXTRA_TEXT, mSmsItemMessageList.get(mPresentSelection));
							startActivity(Intent.createChooser(intent2, getTitle()));
							break;
						}
					}
				})
				.create();
		dialog.show();
	}
}
