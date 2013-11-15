package com.ginwave.smshelper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ginwave.smshelper.more.AutoReplyService;
import com.umeng.analytics.MobclickAgent;

public class HolidayGridSendMms extends Activity implements OnClickListener{

	private TextView mHolidayGridSendMms;
	private TextView mHolidayGridShareMms;
	private GameView mHolidayGridGameView;
	private TextView mMainBack;
	private EditText mInputNumber;
	private ImageView mMultiChooseButton;
	private String mFilePath;
	private String mMmsText;
	private EditText mMmsSubject;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.holiday_grid_send_mms);
		
		Intent intent = this.getIntent();
		mMmsText = intent.getStringExtra("content");
		mFilePath = intent.getStringExtra("picture");
		Log.i("xiao", "mFilePath = " + mFilePath);
		mMainBack = (TextView) findViewById(R.id.mMainBack);
		mMainBack.setOnClickListener(this);
		mHolidayGridGameView = (GameView) findViewById(R.id.mHolidayGridGameView);
		mHolidayGridGameView.setGifImage(mFilePath);
		mHolidayGridSendMms = (TextView) findViewById(R.id.mHolidayGridSendMms);
		mHolidayGridSendMms.setOnClickListener(this);
		mInputNumber = (EditText)findViewById(R.id.mInputNumber);
		mMultiChooseButton = (ImageView) findViewById(R.id.mMultiChooseButton);
		mMultiChooseButton.setOnClickListener(this);
		mMmsSubject = (EditText) findViewById(R.id.mSubject);
		mHolidayGridShareMms = (TextView) findViewById(R.id.mHolidayGridShareMms);
		mHolidayGridShareMms.setOnClickListener(this);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("xiao", "onActivityResult");
		String numberStr = null;
		if (data != null) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				numberStr = bundle.getString("numberStr");
				mInputNumber.setText(numberStr);
			}
		}
	}
	
	private void share(){
		Intent intent= new Intent(Intent.ACTION_SEND, Uri.fromParts("mmsto",
				"", null));
		intent.setType("image/gif");
		intent.putExtra(
				Intent.EXTRA_STREAM,
				Uri.parse("file://" + mFilePath));
		intent.putExtra("subject", "佳节快乐！");
		intent.putExtra("sms_body", mMmsText);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if(v.equals(mMultiChooseButton)){
			Intent lIntent = new Intent();
			lIntent.setClass(getApplicationContext(), MultiSelectTab.class);
			lIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivityForResult(lIntent, 1);
		}
		if(v.equals(mMainBack)){
			this.finish();
		}
		if(v.equals(mHolidayGridShareMms)){
			share();
		}
		if(v.equals(mHolidayGridSendMms)){
			String number = mInputNumber.getText().toString();
			String subject = mMmsSubject.getText().toString();
			if(number.length() <= 0){
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.sending),
						Toast.LENGTH_SHORT).show();
				return ;
			}
			/*Intent intent= new Intent(Intent.ACTION_SEND, Uri.fromParts("mmsto",
					"", null));
			intent.setType("image/gif");
			intent.putExtra(
					Intent.EXTRA_STREAM,
					Uri.parse("file://" + mFilePath));
			intent.putExtra("subject", "佳节快乐！");
			intent.putExtra("sms_body", mMmsText);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);*/
			Intent intent = new Intent();
			intent.setAction(AutoReplyService.SEND_MMS);
			intent.putExtra("filepath", mFilePath);
			intent.putExtra("number", number);
			intent.putExtra("subject", subject);
			this.sendBroadcast(intent);
			Log.i("xiao", "sendBroadcast");
			this.finish();
		}
		// TODO Auto-generated method stub
		/*if (v.equals(mMainBack)) {
			this.finish();
			return;
		}
		if (v.equals(mHolidayDetailBack)) {
			mHolidayDetailLayout
					.startAnimation(mHolidayDetailTranslateDownAnimation);
			mHolidayDetailTranslateDownAnimation.setAnimationListener(this);
			// setDetailLayoutInvisible();
			return;
		}*/
		/*else if (v instanceof LinearLayout
				&& mHolidayDetailArray[0].getText().toString().equals("彩信")) {
			TextView textView = (TextView) v
					.findViewById(R.id.mHolidayDetailItem);
			ImageView image = (ImageView) v.findViewById(R.id.img);
			String text = textView.getText().toString();
			String extStorageDirectory = Environment
					.getExternalStorageDirectory().toString();
			Intent intent;
			if (image != null && image.getBackground() != null) {
				Log.i("xiao", "image != null no setClass");
				intent = new Intent(Intent.ACTION_SEND, Uri.fromParts("mmsto",
						"", null));
				// intent.setClassName("com.android.mms",
				// "com.android.mms.ui.ComposeMessageActivity");

				Bitmap bm = ((BitmapDrawable) image.getBackground())
						.getBitmap();
				intent.setType("image/gif");
				File file = new File(extStorageDirectory, "mms.gif");
				FileOutputStream outStream;
				InputStream inputStream;
				int position = (Integer) v.getTag();
				try {
					AssetManager assetManager = getAssets();
					inputStream = assetManager
							.open(chineseToEnglish(mHolidayItem
									.getHolidayName()) + position + ".gif");
					outStream = new FileOutputStream(file);
					byte[] bt = new byte[1024];
					int count;
					while ((count = inputStream.read(bt)) > 0) {
						outStream.write(bt, 0, count);
					}
					inputStream.close();
					outStream.flush();
					outStream.close();
					
					 * inputStream = assetManager
					 * .open(chineseToEnglish(mHolidayName) + (++position) +
					 * ".gif");
					 
					
					 * bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
					 * 
					 * outStream.flush(); outStream.close();
					 
					intent.putExtra(
							Intent.EXTRA_STREAM,
							Uri.parse("file://" + extStorageDirectory
									+ "/mms.gif"));

				} catch (FileNotFoundException e) {
					Log.i("xiao", "FileNotFoundException " + e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					Log.i("xiao", "IOException " + e.getMessage());
					e.printStackTrace();
				}
			} else {
				Log.i("xiao", "image == null");
				intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
						"mmsto", "", null));
			}
			intent.putExtra("subject", "佳节快乐！");
			intent.putExtra("sms_body", text);
			// intent.setType("image/*");
			// startActivity(intent);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return;
		}

		TextView textView = (TextView) v;
		for (int j = 0; j < mHolidayDetailArray.length; j++) {
			if (textView.equals(mHolidayDetailArray[j])) {
				mPresentSelectionItem = j;
				controlHolidayDetailItem();
				controlHolidayDetailContent();
				return;
			}
		}*/
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
