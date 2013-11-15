package com.ginwave.smshelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseActivity extends Activity {
	private LayoutInflater mInflater;
	private Button mFrameTopLeftButton;
	private TextView mFrameTopMiddleTextView;
	private Button mFrameTopRightButton;
	
	private LinearLayout mLinearLayout;
	private LinearLayout mLinearTest;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void setupViews(int id){
		mInflater = (LayoutInflater)this.getLayoutInflater();
		
		mLinearLayout = (LinearLayout)mInflater.inflate(R.layout.base, null);
		mLinearTest = (LinearLayout)mLinearLayout.findViewById(R.id.mTest);
		mFrameTopLeftButton = (Button)mLinearLayout.findViewById(R.id.frame_top_left);
		mFrameTopMiddleTextView = (TextView)mLinearLayout.findViewById(R.id.frame_top_middle);
		mFrameTopRightButton = (Button)mLinearLayout.findViewById(R.id.frame_top_right);
		
		mFrameTopLeftButton.setOnClickListener(new ClickFrameTopViewListener());
		mFrameTopRightButton.setOnClickListener(new ClickFrameTopViewListener());
		LinearLayout localLinear = (LinearLayout)mInflater.inflate(id, null);
		mLinearTest.addView(localLinear);
		setContentView(mLinearLayout);
	}
	
	public void setFrameTopLeftButtonDTI(int pDrawableId, String pText){
		mFrameTopLeftButton.setVisibility(View.VISIBLE);
		mFrameTopLeftButton.setBackgroundResource(pDrawableId);
		mFrameTopLeftButton.setText(pText);
	}
	
	public void setFrameTopLeftButtonTI(String pText){
		mFrameTopLeftButton.setVisibility(View.VISIBLE);
		mFrameTopLeftButton.setText(pText);
	}
	
	public void setFrameTopMiddleTextView(String pText){
		mFrameTopMiddleTextView.setText(pText);
	}
	
	public void setFrameTopRightButtonDTI(int pDrawableId, String pText){
		mFrameTopRightButton.setVisibility(View.VISIBLE);
		mFrameTopRightButton.setBackgroundResource(pDrawableId);
		mFrameTopRightButton.setText(pText);
	}
	
	public void setFrameTopRightButtonTI(String pText){
		mFrameTopRightButton.setVisibility(View.VISIBLE);
		mFrameTopRightButton.setText(pText);
	}
	
	public void handleClickOnFrameTopLeftButton(){
		
	}
	
	public void handleClickOnFrameTopRightButton(){
		
	}
	
	class ClickFrameTopViewListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			if((TextView)v == mFrameTopLeftButton){
				handleClickOnFrameTopLeftButton();
			}
			if((TextView)v == mFrameTopRightButton){
				handleClickOnFrameTopRightButton();
			}
		}
		
	}
}
