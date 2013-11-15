package com.ginwave.smshelper;

import com.umeng.analytics.MobclickAgent;
import com.ginwave.smshelper.more.ListViewForAuto;
import com.ginwave.smshelper.more.SetDataSource;
import com.ginwave.smshelper.more.SettingForSMS;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import 	android.widget.AdapterView;

public class LifeDetailActivity extends Activity implements OnClickListener{

	private TextView mMainBack;
	private TextView mMainTitle;
	private LayoutInflater mInflater;
	private int mIcon;
	private String mName;
	private String mNote;
	private int mNoteIcon;
	private String mUrl;
	private ImageView mLifeDetailIcon;
	private TextView mLifeDetailDisplay;
	private TextView mLifeDetailNote;
	private ImageView mLifeDetailNoteIcon;
	private TextView mLifeDetailNoteShare;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.life_detail);
		dataFromIntent();
		setupViews();
	}
	
	private void dataFromIntent(){
		Intent intent = this.getIntent();
		mIcon = intent.getIntExtra("life_icon", R.drawable.life1);
		mName = intent.getStringExtra("life_name");
		mNote = intent.getStringExtra("life_note");
		mNoteIcon = intent.getIntExtra("life_note_icon", R.drawable.life_code1);
		mUrl = intent.getStringExtra("life_url");
	}
	
	private void setupViews(){
		mInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainTitle = (TextView) findViewById(R.id.mMainTitle);
		mLifeDetailIcon = (ImageView) findViewById(R.id.mLifeDetailIcon);
		mLifeDetailDisplay = (TextView) findViewById(R.id.mLifeDetailDisplay);
		mLifeDetailNote = (TextView) findViewById(R.id.mLifeDetailNote);
		mLifeDetailNoteIcon = (ImageView) findViewById(R.id.mLifeDetailNoteIcon);
		mLifeDetailNoteShare = (TextView) findViewById(R.id.mLifeDetailNoteShare);
		mMainBack.setOnClickListener(this);
		
		mLifeDetailIcon.setBackgroundResource(mIcon);
		mLifeDetailDisplay.setText(mName);
		mLifeDetailNote.setText(mNote);
		mLifeDetailNoteIcon.setBackgroundResource(mNoteIcon);
		mMainTitle.setText(mName);
		mLifeDetailNoteShare.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mMainBack)) {
			this.finish();
		}
		if(v.equals(mLifeDetailNoteShare)){
			Intent intent = new Intent(Intent.ACTION_SEND);

			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, "分享好软件");
			intent.putExtra(Intent.EXTRA_TEXT, "我发现" + mName + "很好玩，赶紧下载试用吧~ " + mUrl);
			startActivity(Intent.createChooser(intent, getTitle()));
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
