package com.ginwave.smshelper;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ViewFlipper;

public class AppGuide extends Activity implements OnGestureListener {

	private int[] imgs = { R.drawable.appguide1, R.drawable.appguide2, R.drawable.appguide3 };
	private GestureDetector gestureDetector;
	private ViewFlipper viewFlipper;
	private Context context;
	private int FLING_MIN_DISTANCE = 80;
	private int position = 0;// current position

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appguide);
		context = getApplicationContext();
		gestureDetector = new GestureDetector(context, this);
		viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		for (int i = 0; i < imgs.length; i++) {
			ImageView imageView = new ImageView(context);
			imageView.setImageResource(imgs[i]);
			imageView.setScaleType(ScaleType.FIT_XY);
			viewFlipper.addView(imageView);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return gestureDetector.onTouchEvent(event);
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE) {
			position++;
			Log.v("fang", position + "");
			if (position == 3) {
				Intent intent = new Intent(context, SkipActivity.class);
				startActivity(intent);
				finish();
			} else {
				viewFlipper.setInAnimation(AnimationUtils.loadAnimation(
						context, R.anim.right_in));
				viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
						context, R.anim.none));
				viewFlipper.showNext();
			}
		}
		if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE) {
			position--;
			Log.v("fang", position + "");
			if (position < 0) {
				position = 0;
			} else {
				viewFlipper.setInAnimation(AnimationUtils.loadAnimation(
						context, R.anim.left_in));
				viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
						context, R.anim.none));
				viewFlipper.showPrevious();
			}
		}
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
