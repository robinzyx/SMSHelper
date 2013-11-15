package com.ginwave.smshelper;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.ginwave.smshelper.localsms.SmsOnePersonReader;
import com.ginwave.smshelper.more.ListViewForAuto;
import com.ginwave.smshelper.more.SetDataSource;
import com.ginwave.smshelper.more.SettingForSMS;
import com.ginwave.smshelper.util.SendSuccessReceiver;
import com.ginwave.smshelper.util.SendSuccessToastReceiver;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class IndividualDetailActivity extends Activity implements OnClickListener, OnItemClickListener{

	private TextView mMainBack;
	private TextView mMainName;
	private ExpandableListView mIndividualDetailList;
	private String[] mIndividualListData;
	private String[] groupArray;
    private List<IndividualDetailData> childArray;
    private Resources mResources;
    private int mPresentSelection;
    private String mPresentDetailName;
    private int[][] mChildId = {
    						{R.string.individual_detail_six_intro, R.string.individual_detail_six_fee, R.string.individual_detail_six_note, R.array.individual_detail_six_action, 
    						R.array.individual_detail_six_setting, R.array.individual_detail_six_setting_value, R.array.individual_detail_six_setting_flag},
    						{R.string.individual_detail_five_intro, R.string.individual_detail_five_fee, R.string.individual_detail_five_note, R.array.individual_detail_five_action, 
            				R.array.individual_detail_five_setting, R.array.individual_detail_five_setting_value, R.array.individual_detail_five_setting_flag}, 
            				{R.string.individual_detail_one_intro, R.string.individual_detail_one_fee, R.string.individual_detail_one_note, R.array.individual_detail_one_action, 
            				R.array.individual_detail_one_setting, R.array.individual_detail_one_setting_value, R.array.individual_detail_one_setting_flag},
    						{R.string.individual_detail_two_intro, R.string.individual_detail_two_fee, R.string.individual_detail_two_note, R.array.individual_detail_two_action, 
        					R.array.individual_detail_two_setting, R.array.individual_detail_two_setting_value, R.array.individual_detail_two_setting_flag},
    						{R.string.individual_detail_three_intro, R.string.individual_detail_three_fee, R.string.individual_detail_three_note, R.array.individual_detail_three_action, 
        					R.array.individual_detail_three_setting, R.array.individual_detail_three_setting_value, R.array.individual_detail_three_setting_flag}};
	private LayoutInflater mInflater;
	private TextView mIndividualDetailActionOpen;
	private TextView mIndividualDetailActionClose;
	private int mIndividualDetailSettingTextID = R.id.mIndividualDetailSettingText;
	private IndividualDetailData mIndividualDetailData;
	private SendSuccessToastReceiver mReceiver;
	private static final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getFromIntent();
		setContentView(R.layout.individual_detail);
		
		setupViews();
		
		mReceiver = new SendSuccessToastReceiver();
		IntentFilter deliverFilter = new IntentFilter();
		deliverFilter.addAction(SENT_SMS_ACTION);
		this.registerReceiver(mReceiver, deliverFilter);
		
		Log.i("xiao", "mPresentSelection = " + mPresentSelection);
	}
    
	
	
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
    	this.unregisterReceiver(mReceiver);
		super.onDestroy();
	}



	private void getFromIntent(){
    	Intent intent = this.getIntent();
    	mPresentSelection = intent.getIntExtra("individual_item", 0);
    	mPresentDetailName = intent.getStringExtra("individual_name");
    }

	private void setupViews() {
		mInflater = this.getLayoutInflater();
		mIndividualDetailList = (ExpandableListView) findViewById(R.id.mIndividualDetailListView);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainName = (TextView) findViewById(R.id.mMainName);
		mMainBack.setOnClickListener(this);
		mMainName.setText(mPresentDetailName);
		groupArray = this.getResources().getStringArray(R.array.individual_detail_business);
		loadChildArray();
		ExpandableAdapter adapter = new ExpandableAdapter(this);
		mIndividualDetailList.setAdapter(adapter);
	}
	
	private void loadChildArray(){
		mResources = this.getResources();
		childArray = new ArrayList<IndividualDetailData>();
		//for(int i = 0; i < mChildId.length; i++){;
			List<IndividualDetailData> individualDetail = new ArrayList<IndividualDetailData>();
			IndividualDetailData individual = new IndividualDetailData();
			individual.mIndividualDetailIntro = mResources.getString(mChildId[mPresentSelection][0]);
			individual.mIndividualDetailFee = mResources.getString(mChildId[mPresentSelection][1]);
			individual.mIndividualDetailNote = mResources.getString(mChildId[mPresentSelection][2]);
			individual.mIndividualDetailAction = mResources.getStringArray(mChildId[mPresentSelection][3]);
			individual.mIndividualDetailSetting = mResources.getStringArray(mChildId[mPresentSelection][4]);
			individual.mIndividualDetailSettingValue = mResources.getStringArray(mChildId[mPresentSelection][5]);
			individual.mIndividualDetailSettingFlag = mResources.getStringArray(mChildId[mPresentSelection][6]);
			mIndividualDetailData = individual;
			childArray.add(individual);
		//}
	} 
	
	public class IndividualDetailSettingAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context mContext;
		private String[] mContentArray; 
		private ViewHolder mViewHolder;

		public IndividualDetailSettingAdapter(Context pContext, String[] pArray) {
			mContext = pContext;
			mContentArray = pArray;
			mInflater = LayoutInflater.from(pContext);
			Log.i("xiao", "size = " + mContentArray.length);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mContentArray.length;
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
			convertView = mInflater.inflate(R.layout.list_individual_detail_setting_item, null);
			TextView individualDetailSettingContent = (TextView)convertView.findViewById(R.id.mIndividualDetailSettingContent);
			individualDetailSettingContent.setText(mContentArray[position]);
			return convertView;
		}
		
		public final class ViewHolder {
			public TextView mIndividualDetailSettingContent;
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
            return childArray.get(groupPosition);    
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
        	View view = mInflater.inflate(R.layout.list_individual_detail_group, null);
        	TextView mIndividualDetailGroup = (TextView)view.findViewById(R.id.mIndividualDetailGroup);
        	mIndividualDetailGroup.setText(string);
            return view;    
        }    
        public View getGenericView(IndividualDetailData pIndividual, int pChildPosition)    
        {  
        	View view = null;
            switch(pChildPosition){
            case 0:
            	view = mInflater.inflate(R.layout.list_individual_detail_intro, null);
            	TextView intro = (TextView)view.findViewById(R.id.mIndividualDetailIntro);
            	intro.setText(pIndividual.mIndividualDetailIntro);
            	break;
            case 1:
            	view = mInflater.inflate(R.layout.list_individual_detail_intro, null);
            	TextView fee = (TextView)view.findViewById(R.id.mIndividualDetailIntro);
            	fee.setText(pIndividual.mIndividualDetailFee);
            	break;
            case 2:
            	view = mInflater.inflate(R.layout.list_individual_detail_intro, null);
            	TextView note = (TextView)view.findViewById(R.id.mIndividualDetailIntro);
            	note.setText(pIndividual.mIndividualDetailNote);
            	break;
            case 3:
            	view = mInflater.inflate(R.layout.list_individual_detail_action, null);
            	mIndividualDetailActionOpen = (TextView)view.findViewById(R.id.mIndividualDetailActionOpen);
            	mIndividualDetailActionClose = (TextView)view.findViewById(R.id.mIndividualDetailActionClose);
            	mIndividualDetailActionOpen.setOnClickListener(IndividualDetailActivity.this);
            	mIndividualDetailActionClose.setOnClickListener(IndividualDetailActivity.this);
            	break;
            case 4:
            	view = mInflater.inflate(R.layout.list_individual_detail_setting, null);
            	LinearLayout linear = (LinearLayout)view.findViewById(R.id.mIndividualDetailSettingLinearLayout);
            	for( int i = 0; i < pIndividual.mIndividualDetailSetting.length; i++){
            		TextView textView = (TextView)mInflater.inflate(R.layout.list_individual_detail_setting_text, null);
            		textView.setText(pIndividual.mIndividualDetailSetting[i]);
            		textView.setTag(i);
            		textView.setOnClickListener(IndividualDetailActivity.this);
            		linear.addView(textView);
            	}
            	/*ListView detailSetting = (ListView)view.findViewById(R.id.mIndividualDetailSettingList);
            	IndividualDetailSettingAdapter individualAdapter = new IndividualDetailSettingAdapter(activity, pIndividual.mIndividualDetailSetting);
            	detailSetting.setAdapter(individualAdapter);*/
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
			IndividualDetailData individual = childArray.get(0);
			Log.i("xiao", "childPosition = " + childPosition);
            return getGenericView(individual, groupPosition);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mMainBack)) {
			this.finish();
		}
		if(v.equals(mIndividualDetailActionOpen)){
			Log.i("xiao", "onClickopen...............");
			ArrayList<String> phoneNumber = new ArrayList<String>();
			phoneNumber.add("10086");
			SetDataSource.sendMessage(getApplicationContext(), phoneNumber, mIndividualDetailData.mIndividualDetailAction[0]);
			switch(mPresentSelection){
			case 0:
				MobclickAgent.onEvent(this,
						SetDataSource.SMSSIGN);
				break;
			case 1:
				MobclickAgent.onEvent(this,
						SetDataSource.SMSTRANSFER);
				
				break;
			case 2:
				MobclickAgent.onEvent(this,
						SetDataSource.SMSREPLY);
				
				break;
			case 3:
				MobclickAgent.onEvent(this,
						SetDataSource.SMSLIB);
				break;
			case 4:
				MobclickAgent.onEvent(this,
						SetDataSource.SMSFILTER);
				break;
			case 5:
				
				break;
			}
		}
		if(v.equals(mIndividualDetailActionClose)){
			Log.i("xiao", "onClickclose...............");
			ArrayList<String> phoneNumber = new ArrayList<String>();
			phoneNumber.add("10086");
			SetDataSource.sendMessage(getApplicationContext(), phoneNumber, mIndividualDetailData.mIndividualDetailAction[1]);
		}
		if(v.getId() == mIndividualDetailSettingTextID){
			TextView textView = (TextView)v;
			int number = (Integer)textView.getTag();
			if(mIndividualDetailData.mIndividualDetailSettingFlag[number] == "0" ||
					mIndividualDetailData.mIndividualDetailSettingFlag[number].equals("0")){
				ArrayList<String> phoneNumber = new ArrayList<String>();
				phoneNumber.add("10658544");
				SetDataSource.sendMessage(getApplicationContext(), phoneNumber, mIndividualDetailData.mIndividualDetailSettingValue[number]);
			}
			if(mIndividualDetailData.mIndividualDetailSettingFlag[number] == "1" ||
					mIndividualDetailData.mIndividualDetailSettingFlag[number].equals("1")){
				createInputValueDialog(this, mIndividualDetailData.mIndividualDetailSetting[number], number);
			}
		}
	}
	
	public void createInputValueDialog(final Context context, String pTitle, final int pNumber) {
			View view = mInflater.inflate(R.layout.list_individual_detail_setting_input_value, null);
			final EditText mIndividualDetailSettingInput = (EditText)view.findViewById(R.id.mIndividualDetailSettingInput);
			AlertDialog dialog = new AlertDialog.Builder(context).setTitle(pTitle).setCancelable(true).setPositiveButton(
				 context.getString(R.string.select_ok), new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String inputValue = mIndividualDetailSettingInput.getText().toString();
						if(inputValue != null && inputValue.length() > 0){
							ArrayList<String> phoneNumber = new ArrayList<String>();
							phoneNumber.add("10658544");
							String value = mIndividualDetailData.mIndividualDetailSettingValue[pNumber] + inputValue;
							SetDataSource.sendMessage(getApplicationContext(), phoneNumber, value);
							dialog.dismiss();
						}
					}
					 
				 })
				 .setNegativeButton(context.getString(R.string.select_cancel), new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
					}
					 
				 }).setView(view).create();
			dialog.show();
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
	}

}
