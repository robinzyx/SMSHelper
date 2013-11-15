package com.ginwave.smshelper.localsms;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ginwave.smshelper.GridViewForSMS;
import com.ginwave.smshelper.ListViewForSMSLibsAdapter;
import com.ginwave.smshelper.MainActivity;
import com.ginwave.smshelper.R;
import com.ginwave.smshelper.SMSLibsSendActivity;
import com.ginwave.smshelper.GridViewForSMS.ExpandableAdapter;
import com.ginwave.smshelper.db.DBHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class ListViewForLocalSms extends Activity implements OnClickListener{

	private ExpandableListView mMsgExpanListView;
    private List<List<String>> mChildArray;
    private ArrayList<HashMap<String, Object>> mGroupArray;
    private DBHelper mDB;
	private SQLiteDatabase mSqLiteDatabase;
	private Cursor mCursor;
	private LayoutInflater mInflater;
	private String phoneNum;
	private ImageView mMainBack;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gridviewforsmslibs);
        mInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);    
        Intent intent = getIntent();
        phoneNum = intent.getStringExtra("phonenumber");
        mMsgExpanListView = (ExpandableListView) findViewById(R.id.mMsgExpanListView);
        mMainBack = (ImageView)findViewById(R.id.mMainBack);
        mMainBack.setOnClickListener(this);
        mChildArray = new ArrayList<List<String>>();
        mGroupArray = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 3; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            switch (i) {
                case 0:
                    map.put("ItemImage", R.drawable.icon);
                    map.put("ItemText", "" +getText( R.string.spring));
                    break;
                case 1:
                    map.put("ItemImage", R.drawable.icon);
                    map.put("ItemText", "" +getText(R.string.qingrenjie));
                    break;
                case 2:
                    map.put("ItemImage", R.drawable.icon);
                    map.put("ItemText", "" +getText(R.string.yuanxiaojie));
                    break;
            }
            mGroupArray.add(map);
        }
        initChildList();
        mMsgExpanListView.setAdapter(new ExpandableAdapter(this));
    }
    
    private void initChildList(){
    	mDB = new DBHelper(getApplicationContext());
        try {
			mDB.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        mSqLiteDatabase = mDB.openDataBase();
        mCursor = mSqLiteDatabase.query("spring", null, null, null, null, null,
                null, null);
        List<String> tempArray1 = new ArrayList<String>();
        if (mCursor != null && mCursor.getCount() != 0) {
			while (mCursor.moveToNext()) {
				tempArray1.add(mCursor.getString(mCursor
						.getColumnIndex("message")));
			}
		}
        mChildArray.add(tempArray1);
		mCursor.close();
		 List<String> tempArray2 = new ArrayList<String>();
		mCursor = mSqLiteDatabase.query("qingrenjie", null, null, null, null, null,
                null, null);
		if (mCursor != null && mCursor.getCount() != 0) {
			while (mCursor.moveToNext()) {
				tempArray2.add(mCursor.getString(mCursor
						.getColumnIndex("message")));
			}
		}
		mChildArray.add(tempArray2);
		mCursor.close();
		List<String> tempArray3 = new ArrayList<String>();
		mCursor = mSqLiteDatabase.query("yuanxiaojie", null, null, null, null, null,
                null, null);
		if (mCursor != null && mCursor.getCount() != 0) {
			while (mCursor.moveToNext()) {
				tempArray3.add(mCursor.getString(mCursor
						.getColumnIndex("message")));
			}
		}
		mChildArray.add(tempArray3);
		mCursor.close();
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
            return mChildArray.get(groupPosition).get(childPosition);    
        }    
        public long getChildId(int groupPosition, int childPosition)    
        {    
            return childPosition;    
        }    
        public int getChildrenCount(int groupPosition)    
        {    
            return mChildArray.get(groupPosition).size();    
        }     
   
        public Object getGroup(int groupPosition)    
        {    
            return mGroupArray.get(groupPosition);    
        }    
        public int getGroupCount()    
        {    
            return mGroupArray.size();  
        }    
        public long getGroupId(int groupPosition)    
        {    
            return groupPosition;    
        }    
        public View getGenericView(String string, int pResId)    
        {  
        	View view = mInflater.inflate(R.layout.listforsmslibsitem, null);
            TextView listItemText = (TextView)view.findViewById(R.id.list_item_text);
            ImageView listItemIcon = (ImageView)view.findViewById(R.id.mSmslibIcon);
            listItemText.setText(string);
            listItemIcon.setBackgroundResource(pResId);
        	/*AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(    
                    ViewGroup.LayoutParams.FILL_PARENT, 65);    
            TextView text = new TextView(activity);    
            text.setLayoutParams(layoutParams);    
            text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);    
            text.setPadding(35, 0, 20, 0); 
            text.setTextColor(Color.BLACK);
            text.setText(string);*/    
            return view;    
        }    
        public TextView getGenericView(String string, boolean bool)    
        {  
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(    
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);    
            TextView text = new TextView(activity);    
            text.setLayoutParams(layoutParams);    
            text.setGravity(Gravity.LEFT);    
            text.setPadding(25, 3, 20, 3); 
            text.setTextColor(Color.BLACK);
            text.setText(string); 
            text.setOnClickListener(ListViewForLocalSms.this);
            return text;    
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
			String string = mChildArray.get(groupPosition).get(childPosition);    
            return getGenericView(string, true);
		}
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			String string = (String)mGroupArray.get(groupPosition).get("ItemText");
			int resId = (Integer)mGroupArray.get(groupPosition).get("ItemImage");
	        return getGenericView(string, resId);
		}  
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.equals(mMainBack)){
			this.finish();
		}
		else{
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
				LocalSmsSendActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("xm", ((TextView)v).getText().toString());
			bundle.putString("phonenumber", phoneNum);
			intent.putExtras(bundle);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			this.finish();
		}
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
