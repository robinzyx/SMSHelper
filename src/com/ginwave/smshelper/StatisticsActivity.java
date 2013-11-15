package com.ginwave.smshelper;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.ginwave.smshelper.more.ListViewForAuto;
import com.ginwave.smshelper.more.SetDataSource;
import com.ginwave.smshelper.more.SettingForSMS;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatisticsActivity extends Activity implements OnClickListener, OnItemClickListener{

	private TextView mMainBack;
	private TextView mMainClear;
	private LinearLayout mBottomLayout;
	private Button mStaticsAdd;
	private Button mStaticsBack;
	private ListView mStaticList;
	private StaticsAdapter mStaticsAdapter;
	private ProgressDialog mProgressDialog = null;
	private boolean mStaticsClearFlag = false;
	private List<Boolean> mStaticsClearFlagArray;
	private List<String> mStaticsClearIdArray;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.statics);
		setupViews();
		new GetMmsTask().execute("");
	}

	private void setupViews() {
		mStaticList = (ListView) findViewById(R.id.mStaticsList);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainClear = (TextView) findViewById(R.id.mMainClear);
		mBottomLayout = (LinearLayout) findViewById(R.id.mBottomLayout);
		mStaticsAdd = (Button) findViewById(R.id.mStaticsAdd);
		mStaticsBack = (Button) findViewById(R.id.mStaticsBack);
		mMainBack.setOnClickListener(this);
		mMainClear.setOnClickListener(this);
		mStaticsAdd.setOnClickListener(this);
		mStaticsBack.setOnClickListener(this);
		mStaticList.setOnItemClickListener(this);
	}

	private class StaticsAdapter extends CursorAdapter {
		
		private ViewHolder mViewHolder;
		private LayoutInflater mInflater;
		private Context mContext;
		private int mPosition;
		
		public StaticsAdapter(Context context, Cursor c) {
			super(context, c);
			mContext = context;
			// TODO Auto-generated constructor stub
			mViewHolder = new ViewHolder();
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mStaticsClearFlagArray = new ArrayList<Boolean>();
			mStaticsClearIdArray = new ArrayList<String>();
			if(c != null){
				while(c.moveToNext()){
					mStaticsClearFlagArray.add(false);
					mStaticsClearIdArray.add(c.getString(0));
				}
			}
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			
			return mInflater.inflate(R.layout.list_statics_item, parent, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			if(view == null){
				view = mInflater.inflate(R.layout.list_statics_item, null, false);
			}
			mPosition = cursor.getPosition();
			StatisticsTransportData data = new StatisticsTransportData();
			data.mId = cursor.getString(0);
			data.mStaticDate = cursor.getString(1);
			data.mStaticNumber = cursor.getString(2);
			data.mStaticMessage = cursor.getString(3);
			mViewHolder.mStaticsDate = (TextView)view.findViewById(R.id.mStaticsDate);
			mViewHolder.mStaticsNumber = (TextView)view.findViewById(R.id.mStaticsNumber);
			mViewHolder.mStaticsMessage = (TextView)view.findViewById(R.id.mStaticsMessage);
			mViewHolder.mStaticsCheckBox = (CheckBox)view.findViewById(R.id.mStaticsChecked);
			mViewHolder.mStaticsDate.setText(data.mStaticDate);
			mViewHolder.mStaticsNumber.setText(data.mStaticNumber);
			mViewHolder.mStaticsMessage.setText(handleMessage(data.mStaticMessage));
			if(mStaticsClearFlag){
				mViewHolder.mStaticsCheckBox.setVisibility(View.VISIBLE);
				if(mStaticsClearFlagArray.get(mPosition)){
					mViewHolder.mStaticsCheckBox.setChecked(true);
				}
				else{
					mViewHolder.mStaticsCheckBox.setChecked(false);
				}
			}
			else{
				mViewHolder.mStaticsCheckBox.setVisibility(View.INVISIBLE);
			}
			view.setTag(data);
		}
		
		public final class ViewHolder{
			  public TextView  mStaticsDate;
			  public TextView  mStaticsNumber;
			  public TextView  mStaticsMessage;
			  public CheckBox mStaticsCheckBox;
		} 
		
		private String handleMessage(String pString){
			if(pString.length() <= 8){
				return pString;
			}
			else{
				String message = pString.substring(0, 7);
				return message + mContext.getString(R.string.points);
			}
		}
	}

	private class GetMmsTask extends AsyncTask<String, String, String> {
		private Cursor mCursor;
		private final int RECIPIENT_IDS = 3;
		private final int SNIPPET = 4;
		private String mRecipient;
		private String mSnippet;
		private String mNumber;
		private Long mSnippetCS;
		private String mName;
		private String mThreadId;

		public String doInBackground(String... params) {
			mCursor = getContentResolver().query(
					ReplyRecordProvider.STATICS_CONTENT_URI, null, null, null,
					null);
			return "";
		}

		@Override
		protected void onPreExecute() {
			showProgress();
		}

		@Override
		public void onPostExecute(String Re) {
			Log.i("xiao", "mCursor size = " + mCursor.getCount());
			mStaticsAdapter = new StaticsAdapter(
					getApplicationContext(), mCursor);
			mStaticList.setAdapter(mStaticsAdapter);
			// removeDialog(DIALOG_KEY);
			hideProgress();
		}
	}
	
	
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

	private void showProgress() {
		onCreateDialog(0, null);
		mProgressDialog.show();
	}

	/**
	 * Hides the progress UI for a lengthy operation.
	 */
	private void hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mMainBack)) {
			this.finish();
		}
		if(v.equals(mMainClear)){
			if(mMainClear.getText().equals(this.getString(R.string.clear))){
				mStaticsClearFlag = true;
				mMainClear.setText(this.getString(R.string.select_all));
				mBottomLayout.setVisibility(View.VISIBLE);
				mStaticsAdapter.notifyDataSetChanged();
			}
			else{
				if(mStaticsClearFlagArray != null){
					for( int i = 0; i < mStaticsClearFlagArray.size(); i++){
						mStaticsClearFlagArray.set(i, !mStaticsClearFlagArray.get(i));
					}
					mStaticsAdapter.notifyDataSetChanged();
				}
				if(mMainClear.getText().toString().equals(this.getString(R.string.select_all))){
					mMainClear.setText(R.string.select_cancel);
					Log.i("xiao", "select cancel");
				}
				else{
					Log.i("xiao", "select all");
					mMainClear.setText(R.string.select_all);
				}
			}
		}
		if(v.equals(mStaticsAdd)){
			for( int i = 0; i < mStaticsClearFlagArray.size(); i++){
				if(mStaticsClearFlagArray.get(i)){
					Log.i("xiao", "i = " + i);
					SetDataSource.deleteStatics(this, mStaticsClearIdArray.get(i));
				}
			}
			this.finish();
		}
		if(v.equals(mStaticsBack)){
			mStaticsClearFlag = false;
			mMainClear.setText(this.getString(R.string.clear));
			mBottomLayout.setVisibility(View.INVISIBLE);
			if(mStaticsClearFlagArray != null){
				for( int i = 0; i < mStaticsClearFlagArray.size(); i++){
					mStaticsClearFlagArray.set(i, false);
				}
			}
			mStaticsAdapter.notifyDataSetChanged();
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(mStaticsClearFlag){
			mStaticsClearFlagArray.set(arg2, !mStaticsClearFlagArray.get(arg2));
			mStaticsAdapter.notifyDataSetChanged();
		}
		else{
			Intent intent = new Intent();
			StatisticsTransportData data = (StatisticsTransportData)arg1.getTag();
			Bundle bundle = new Bundle();
			bundle.putString("id", data.mId);
			bundle.putString("date", data.mStaticDate);
			bundle.putString("message", data.mStaticMessage);
			intent.putExtras(bundle);
			intent.setClass(getApplicationContext(), StatisticsDetailActivity.class);
			this.startActivity(intent);
		}
	}

}
