package com.ginwave.smshelper;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

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
import android.content.res.Resources;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatisticsDetailActivity extends Activity implements OnClickListener {

	private TextView mMainBack;
	private ExpandableListView mStaticDetailList;
	private StaticsAdapter mStaticsAdapter;
	private ProgressDialog mProgressDialog = null;
	private StatisticsTransportData mData;
	private String[] groupArray;
	private StatisticsDetailData child; 
    private LayoutInflater mInflater;
    private Resources mResources;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.statics_detail);
		readFromIntent();
		setupViews();
		new GetMmsTask().execute("");
	}
	
	private void readFromIntent(){
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		mData = new StatisticsTransportData();
		mData.mStaticDate = bundle.getString("date");
		mData.mStaticMessage = bundle.getString("message");
		mData.mId = bundle.getString("id");
	}

	private void setupViews() {
		mInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		mResources = this.getResources();
		mStaticDetailList = (ExpandableListView) findViewById(R.id.mStaticDetailListView);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainBack.setOnClickListener(this);
		/*StaticsAdapter adapter = new StaticsAdapter(this);
		mStaticList.setAdapter(adapter);*/
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
					ReplyRecordProvider.STATICS_DETAIL_CONTENT_URI, null, "_id = ?", new String[]{mData.mId},
					null);
			return "";
		}

		@Override
		protected void onPreExecute() {
			showProgress();
		}

		@Override
		public void onPostExecute(String Re) {
			Log.i("xiaobian", "mCursor size = " + mCursor.getCount());
			mStaticsAdapter = new StaticsAdapter(
					getApplicationContext(), mCursor);
			groupArray = mResources.getStringArray(R.array.statics_detail_name);
			child = new StatisticsDetailData();
			if(mCursor != null){
				String number = "";
				child.mStaticDate = mData.mStaticDate;
				child.mStaticMessage = mData.mStaticMessage;
				child.mStaticNumber = Integer.toString(mCursor.getCount());
				child.mStaticPhoneNumber = new ArrayList<String>();
				child.mStaticPhoneState = new ArrayList<String>();
				
				mCursor.moveToFirst();
				for(int i=0;i<mCursor.getCount();i++){
					child.mStaticPhoneNumber.add(mCursor.getString(1));
					child.mStaticPhoneState.add(mCursor.getString(2));
					Log.i("xiaobian", "state = " + mCursor.getString(2)+"num="+mCursor.getString(1));
					mCursor.moveToNext();
				}
			}
			ExpandableAdapter expandableAdapter = new ExpandableAdapter(StatisticsDetailActivity.this);
			mStaticDetailList.setAdapter(expandableAdapter);
			hideProgress();
		}
	}
	
	private class StaticsAdapter extends CursorAdapter {
		
		private ViewHolder mViewHolder;
		private LayoutInflater mInflater;
		
		public StaticsAdapter(Context context, Cursor c) {
			// TODO Auto-generated constructor stub
			super(context, c);
			mViewHolder = new ViewHolder();
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			mData.mId = cursor.getString(0);
			mData.mStaticPhoneNumber = cursor.getString(1);
			
			mViewHolder.mStaticsDate = (TextView)view.findViewById(R.id.mStaticsDate);
			mViewHolder.mStaticsNumber = (TextView)view.findViewById(R.id.mStaticsNumber);
			mViewHolder.mStaticsMessage = (TextView)view.findViewById(R.id.mStaticsMessage);
			mViewHolder.mStaticsDate.setText(mData.mStaticDate);
			mViewHolder.mStaticsNumber.setText(mData.mStaticPhoneNumber);
			mViewHolder.mStaticsMessage.setText(mData.mStaticMessage);
			view.setTag(mData);
		}
		
		public final class ViewHolder{
			  public TextView  mStaticsDate;
			  public TextView  mStaticsNumber;
			  public TextView  mStaticsMessage;
		} 
	}
	
	public class ExpandableAdapter extends BaseExpandableListAdapter    
    {    
        Activity activity;    
        public ExpandableAdapter(Activity a)  
        {    
            activity = a;    
        }  
        public Object getChild(int groupPosition, int childPosition)    
        {    
            return child;    
        }    
        public long getChildId(int groupPosition, int childPosition)    
        {    
            return childPosition;    
        }    
        public int getChildrenCount(int groupPosition)    
        {    
            return 1;    
        }     
   
        public Object getGroup(int groupPosition)    
        {    
            return groupArray[groupPosition];    
        }    
        public int getGroupCount()    
        {    
            return groupArray.length;  
        }    
        public long getGroupId(int groupPosition)    
        {    
            return groupPosition;    
        }    
        public View getGenericView(String string)    
        {  
        	View view = mInflater.inflate(R.layout.list_static_detail_group, null);
        	TextView mIndividualDetailGroup = (TextView)view.findViewById(R.id.mStaticDetailGroup);
        	mIndividualDetailGroup.setText(string);
            return view;    
        }    
        public View getGenericView(StatisticsDetailData pChild, int pChildPosition)    
        {  
        	View view = null;
            switch(pChildPosition){
            case 0:
            	view = mInflater.inflate(R.layout.list_static_detail_item, null);
            	TextView intro = (TextView)view.findViewById(R.id.mStaticDetailIntro);
            	intro.setText(child.mStaticDate);
            	break;
            case 1:
            	view = mInflater.inflate(R.layout.list_static_detail_item, null);
            	TextView fee = (TextView)view.findViewById(R.id.mStaticDetailIntro);
            	fee.setText(child.mStaticNumber);
            	break;
            case 2:
            	view = mInflater.inflate(R.layout.list_static_detail_item_layout, null);
            	LinearLayout linear = (LinearLayout)view.findViewById(R.id.mStaticDetailLayout);
            	for( int i = 0; i < child.mStaticPhoneNumber.size(); i++){
            		View layout = (View)mInflater.inflate(R.layout.list_static_detail_item_special, null);
            		TextView number = (TextView)layout.findViewById(R.id.mStaticDetailNumber);
            		TextView state = (TextView)layout.findViewById(R.id.mStaticDetailStatus);
            		number.setText(child.mStaticPhoneNumber.get(i));
            		number.setTag(i);
            		String stateValue = child.mStaticPhoneState.get(i);
            		if(stateValue.endsWith("1")){
            			state.setText(activity.getString(R.string.state_success));
            		}
            		else{
            			state.setText(activity.getString(R.string.state_failure));
            		}
            		state.setTag(i);
            		linear.addView(layout);
            	}
            	break;
            case 3:
            	view = mInflater.inflate(R.layout.list_static_detail_item, null);
            	TextView content = (TextView)view.findViewById(R.id.mStaticDetailIntro);
            	content.setText(child.mStaticMessage);
            	break;
            }
            return view;    
        }    
        public boolean hasStableIds()    
        {    
            return false;    
        }    
        public boolean isChildSelectable(int groupPosition, int childPosition)    
        {    
            return true;    
        }
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Log.i("xiao", "childPosition = " + childPosition);
            return getGenericView(child, groupPosition);
		}
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			String string = groupArray[groupPosition];    
	        return getGenericView(string);
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
	
	/*public class StaticsAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context mContext;
		private ViewHolder mViewHolder;

		public StaticsAdapter(Context pContext) {
			mContext = pContext;
			mInflater = LayoutInflater.from(pContext);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.mStaticPhoneNumberList.size();
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
			if (convertView == null) {
				mViewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.list_statics_item,
						parent, false);
				mViewHolder.mDate = (TextView)convertView.findViewById(R.id.mStaticsDate);
				mViewHolder.mPhoneNumber = (TextView)convertView.findViewById(R.id.mStaticsNumber);
				mViewHolder.mMessage = (TextView)convertView.findViewById(R.id.mStaticsMessage);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			mViewHolder.mDate.setText(mData.mStaticDate);
			mViewHolder.mPhoneNumber.setText(mData.mStaticPhoneNumberList.get(position));
			mViewHolder.mMessage.setText(mData.mStaticMessage);
			convertView.setTag(mViewHolder);
			return convertView;
		}
		
		public final class ViewHolder {
			public TextView mDate;
			public TextView mPhoneNumber;
			public TextView mMessage;
		}
	}*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mMainBack)) {
			this.finish();
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

}
