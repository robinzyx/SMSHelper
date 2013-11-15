package com.ginwave.smshelper;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

import com.ginwave.smshelper.HolidayDataSource.HolidayItem;
import com.ginwave.smshelper.localsms.LocalSmsSendActivity;
import com.ginwave.smshelper.localsms.SmsReader;
import com.ginwave.smshelper.pojos.Holiday;
import com.ginwave.smshelper.provider.Provider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class HolidayGridChooseSMS extends Activity implements OnClickListener, AnimationListener{
	private LinearLayout mHolidayLayout;
	private TextView[] mHolidayIconArray;
	private LinearLayout mHolidayLayoutItem[];
	private Animation mHolidayLayoutTranslateDownAnimation[];
	private Animation mHolidayLayoutTranslateUpAnimation[];
	private Animation mHolidayDetailTranslateDownAnimation;
	private Animation mHolidayDetailTranslateUpAnimation;
	private int mScreenWidth;
	private int mScreenHeight;
	private LinearLayout mHolidayDetailLayout;
	private TextView mHolidayDetailArray[];
	private TextView mHolidayDetailAbout;
	private TextView mHolidayDetailMessage;
	private TextView mHolidayDetailHistory;
	private TextView mHolidayDetailWords;
	private TextView mHolidayDetailSongs;
	private ListView mHolidayMessageList;
	private TextView mHolidayOtherValue;
	private TextView mHolidayDetailIcon;
	private ImageView mHolidayDetailBack;
	private TextView mMainBack;
	private int mHolidayIconId[];
	private int mHolidayIconTextId[];
	private int mPresentSelectionItem;
	private int mPresentClickItem;
	private LayoutInflater mInflater;
	private HolidayDetailAdapter mHolidayDetailAdapter;
	private HolidayItem mHolidayItem;
	private Resources mResources;
	private String[] mXmlArray;
	private String phoneNum = null;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.holiday_grid_choose_sms);
		Intent intent = getIntent();
		if(intent != null){
			phoneNum = intent.getStringExtra("phonenumber");
		}
		mResources = this.getResources();
		mInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		mHolidayDetailLayout = (LinearLayout)findViewById(R.id.mHolidayDetailLayout);
		mHolidayDetailArray = new TextView[5];
		mHolidayDetailArray[0] = (TextView)findViewById(R.id.mHolidayDetailMessage);
		mHolidayDetailArray[0].setOnClickListener(this);
		mHolidayDetailArray[1] = (TextView)findViewById(R.id.mHolidayDetailAbout);
		mHolidayDetailArray[1].setOnClickListener(this);
		mHolidayDetailArray[2] = (TextView)findViewById(R.id.mHolidayDetailHistory);
		mHolidayDetailArray[2].setOnClickListener(this);
		mHolidayDetailArray[3] = (TextView)findViewById(R.id.mHolidayDetailWords);
		mHolidayDetailArray[3].setOnClickListener(this);
		mHolidayDetailArray[4] = (TextView)findViewById(R.id.mHolidayDetailSongs);
		mHolidayDetailArray[4].setOnClickListener(this);
		mHolidayMessageList = (ListView)findViewById(R.id.mHolidayMessageList);
		mHolidayOtherValue = (TextView)findViewById(R.id.mHolidayOtherValue);
		mHolidayDetailBack = (ImageView)findViewById(R.id.mHolidayDetailBack);
		mHolidayDetailIcon = (TextView)findViewById(R.id.mHolidayDetailIcon);
		mMainBack = (TextView)findViewById(R.id.mMainBack);
		mMainBack.setOnClickListener(this);
		mHolidayDetailBack.setOnClickListener(this);
		mHolidayIconArray = new TextView[28];
		mHolidayLayoutItem = new LinearLayout[7];
		mHolidayLayoutTranslateDownAnimation = new TranslateAnimation[7];
		mHolidayLayoutTranslateUpAnimation = new TranslateAnimation[7];
		mHolidayLayout = (LinearLayout)findViewById(R.id.mHolidayLayout);
		mScreenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		mScreenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		mHolidayIconId = new int[]{R.drawable.holiday1, R.drawable.holiday2, R.drawable.holiday3, R.drawable.holiday4,
				R.drawable.holiday5, R.drawable.holiday6, R.drawable.holiday7, R.drawable.holiday8,
				R.drawable.holiday9, R.drawable.holiday10, R.drawable.holiday11, R.drawable.holiday12,
				R.drawable.holiday13, R.drawable.holiday14, R.drawable.holiday15, R.drawable.holiday16,
				R.drawable.holiday17, R.drawable.holiday18, R.drawable.holiday19, R.drawable.holiday20,
				R.drawable.holiday21, R.drawable.holiday22, R.drawable.holiday23, R.drawable.holiday24,
				R.drawable.holiday25, R.drawable.holiday26, R.drawable.holiday27, R.drawable.holiday28};
		mHolidayIconTextId = new int[]{R.string.holiday1, R.string.holiday2, R.string.holiday3, R.string.holiday4,
				R.string.holiday5, R.string.holiday6, R.string.holiday7, R.string.holiday8,
				R.string.holiday9, R.string.holiday10, R.string.holiday11, R.string.holiday12,
				R.string.holiday13, R.string.holiday14, R.string.holiday15, R.string.holiday16,
				R.string.holiday17, R.string.holiday18, R.string.holiday19, R.string.holiday20,
				R.string.holiday21, R.string.holiday22, R.string.holiday23, R.string.holiday24,
				R.string.holiday25, R.string.holiday26, R.string.holiday27, R.string.holiday28};
		mXmlArray = new String[]{"holiday1.xml","holiday2.xml","holiday3.xml","holiday4.xml",
				"holiday5.xml","holiday6.xml","holiday7.xml","holiday8.xml",
				"holiday9.xml","holiday10.xml","holiday11.xml","holiday12.xml",
				"holiday13.xml","holiday14.xml","holiday15.xml","holiday16.xml",
				"holiday17.xml","holiday18.xml","holiday19.xml","holiday20.xml",
				"holiday21.xml","holiday22.xml","holiday23.xml","holiday24.xml",
				"holiday25.xml","holiday26.xml","holiday27.xml","holiday28.xml"};
		int tmpNumber;
		for( int i = 0; i < mHolidayLayout.getChildCount(); i++){
			tmpNumber = i * 4;
			View view = mHolidayLayout.getChildAt(i);
			mHolidayLayoutItem[i] = (LinearLayout)view;
			mHolidayIconArray[tmpNumber] = (TextView)view.findViewById(R.id.mHolidayOne);
			mHolidayIconArray[tmpNumber].setCompoundDrawablesWithIntrinsicBounds(null, mResources.getDrawable(mHolidayIconId[tmpNumber]), null, null);
			mHolidayIconArray[tmpNumber].setText(mHolidayIconTextId[tmpNumber]);
			mHolidayIconArray[tmpNumber].setTag(tmpNumber);
			mHolidayIconArray[tmpNumber].setOnClickListener(this);
			mHolidayIconArray[tmpNumber + 1] = (TextView)view.findViewById(R.id.mHolidayTwo);
			mHolidayIconArray[tmpNumber + 1].setCompoundDrawablesWithIntrinsicBounds(null, mResources.getDrawable(mHolidayIconId[tmpNumber + 1]), null, null);
			mHolidayIconArray[tmpNumber + 1].setText(mHolidayIconTextId[tmpNumber + 1]);
			mHolidayIconArray[tmpNumber + 1].setTag(tmpNumber + 1);
			mHolidayIconArray[tmpNumber + 1].setOnClickListener(this);
			mHolidayIconArray[tmpNumber + 2] = (TextView)view.findViewById(R.id.mHolidayThree);
			mHolidayIconArray[tmpNumber + 2].setCompoundDrawablesWithIntrinsicBounds(null, mResources.getDrawable(mHolidayIconId[tmpNumber + 2]), null, null);
			mHolidayIconArray[tmpNumber + 2].setText(mHolidayIconTextId[tmpNumber + 2]);
			mHolidayIconArray[tmpNumber + 2].setTag(tmpNumber + 2);
			mHolidayIconArray[tmpNumber + 2].setOnClickListener(this);
			mHolidayIconArray[tmpNumber + 3] = (TextView)view.findViewById(R.id.mHolidayFour);
			mHolidayIconArray[tmpNumber + 3].setCompoundDrawablesWithIntrinsicBounds(null, mResources.getDrawable(mHolidayIconId[tmpNumber + 3]), null, null);
			mHolidayIconArray[tmpNumber + 3].setText(mHolidayIconTextId[tmpNumber + 3]);
			mHolidayIconArray[tmpNumber + 3].setTag(tmpNumber + 3);
			mHolidayIconArray[tmpNumber + 3].setOnClickListener(this);
		}
		for( int j = 0; j < mHolidayLayoutItem.length; j++){
			Log.i("xiao", "top = " + mHolidayLayoutItem[j].getTop());
			TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, (int)mHolidayLayoutItem[j].getTop(), mScreenHeight);
			translateAnimation.setDuration(1000);
			translateAnimation.setRepeatCount(0);
			mHolidayLayoutTranslateDownAnimation[j] = translateAnimation;
		}
		for( int k = 0; k < mHolidayLayoutItem.length; k++){
			TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, mScreenHeight, (int)mHolidayLayoutItem[k].getTop());
			translateAnimation.setDuration(1000);
			translateAnimation.setRepeatCount(0);
			mHolidayLayoutTranslateUpAnimation[k] = translateAnimation;
		}
		mHolidayDetailTranslateDownAnimation = new TranslateAnimation(0, 0, mHolidayDetailLayout.getTop(), mScreenHeight);
		mHolidayDetailTranslateDownAnimation.setDuration(1000);
		mHolidayDetailTranslateDownAnimation.setRepeatCount(0);
		mHolidayDetailTranslateUpAnimation = new TranslateAnimation(0, 0, mScreenHeight, mHolidayDetailLayout.getTop());
		mHolidayDetailTranslateUpAnimation.setDuration(1000);
		mHolidayDetailTranslateUpAnimation.setRepeatCount(0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.equals(mMainBack)){
			this.finish();
			return ;
		}
		if(v.equals(mHolidayDetailBack)){
			mHolidayDetailLayout.startAnimation(mHolidayDetailTranslateDownAnimation);
			mHolidayDetailTranslateDownAnimation.setAnimationListener(this);
			//setDetailLayoutInvisible();
			return ;
		}
		if( v instanceof LinearLayout){
			TextView textView = (TextView)v.findViewById(R.id.mHolidayDetailItem);
			String text = textView.getText().toString();
			/*Intent intent = new Intent();
			Bundle bundle = new Bundle();
			intent.setClass(getApplicationContext(),
					LocalSmsSendActivity.class);
				bundle.putString("xm", text);
				if(phoneNum != null){
					bundle.putString("phonenumber", phoneNum);
				}
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				this.finish();*/
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("content", text);
			intent.putExtras(bundle);
			this.setResult(0, intent);
			this.finish();
			return;
		}
		TextView textView = (TextView)v;
		for( int j = 0; j < mHolidayDetailArray.length; j++){
			if(textView.equals(mHolidayDetailArray[j])){
				mPresentSelectionItem = j;
				controlHolidayDetailItem();
				controlHolidayDetailContent();
				return ;
			}
		}
		Log.i("xiao", "tag = " + textView.getTag());
		mPresentClickItem = (Integer)textView.getTag();
		for( int i = 6; i >= 0; i--){
			mHolidayLayoutItem[i].startAnimation(mHolidayLayoutTranslateDownAnimation[i]);
			if(i == 0){
				mHolidayLayoutTranslateDownAnimation[i].setAnimationListener(this);
			}
		}
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub
		if(arg0.equals(mHolidayDetailTranslateDownAnimation)){
			setDetailLayoutInvisible();
		}
		else{
			setDetailLayoutVisible();
		}
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void setDetailLayoutInvisible(){
		mHolidayDetailLayout.setVisibility(View.INVISIBLE);
		mHolidayLayout.setVisibility(View.VISIBLE);
		for( int i = 6; i >= 0; i--){
			mHolidayLayoutItem[i].startAnimation(mHolidayLayoutTranslateUpAnimation[i]);
		}
	}
	
	private void setDetailLayoutVisible(){
		mHolidayDetailLayout.setVisibility(View.VISIBLE);
		mHolidayLayout.setVisibility(View.INVISIBLE);
		mHolidayDetailLayout.startAnimation(mHolidayDetailTranslateUpAnimation);
		initDetailLayoutView();
		mPresentSelectionItem = 0;
		controlHolidayDetailItem();
		controlHolidayDetailContent();
	}
	private  Holiday queryHoliday(int id) {
		Holiday holiday=null;
		Cursor c = null;
		try {
			c = getContentResolver().query(
					Provider.HolidayColumns.CONTENT_URI,
					new String[] { Provider.HolidayColumns.HOLIDAYNAME,
							
							Provider.HolidayColumns.ITEMONECONT,
							Provider.HolidayColumns.ITEMONETITLE,
							Provider.HolidayColumns.ITEMTWOCONT,
							Provider.HolidayColumns.ITEMTWOTITLE,
							Provider.HolidayColumns.ITEMTHREECONT,
							Provider.HolidayColumns.ITEMTHREETITLE,
							Provider.HolidayColumns.ITEMFOURCONT,
							Provider.HolidayColumns.ITEMFOURTITLE,
							Provider.HolidayColumns.CREATETIME,
							Provider.HolidayColumns.ICON,
							Provider.HolidayColumns.ISUP,
							Provider.HolidayColumns.MODIFYTIME,
							Provider.HolidayColumns.MONTH, },
					Provider.HolidayColumns.HOLIDAYID + "=?",
					new String[] { id + "" }, null);
			if (c != null && c.moveToFirst()) {
				holiday = new Holiday();
				holiday.setH_name(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.HOLIDAYNAME)));
				
				holiday.setItemOneCont(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMONECONT)));
				holiday.setItemOneTitle(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMONETITLE)));
				
				holiday.setItemTwoCont(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMTWOCONT)));
				holiday.setItemTwoTitle(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMTWOTITLE)));
				
				holiday.setItemThreeCont(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMTHREECONT)));
				holiday.setItemThreeTitle(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMTHREETITLE)));
				
				holiday.setItemFourCont(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMFOURCONT)));
				holiday.setItemFourTitle(c.getString(c
						.getColumnIndexOrThrow(Provider.HolidayColumns.ITEMFOURTITLE)));
				Log.i("zhu", "leader.name=" + holiday.getItemTwoTitle());
			} else {
				Log.i("zhu", "query failure!");
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return holiday;
	}
	private  List<HolidayItem> getHolidayInfoFromXml(Context pContext,
			int h_id) {
		
		
		String HOLIDAY = "holiday";
		String NAME = "name";
		String ITEM1NAME = "item1name";
		String ITEM1VALUE = "item1value";
		String ITEM2NAME = "item2name";
		String ITEM2VALUE = "item2value";
		String ITEM3NAME = "item3name";
		String ITEM3VALUE = "item3value";
		String ITEM4NAME = "item4name";
		String ITEM4VALUE = "item4value";
		String ITEM5NAME = "item5name";
		String ITEM5VALUE = "item5value";
		String ITEM6NAME = "item6name";
		String ITEM6VALUE = "item6value";
		
		Holiday myholiday=queryHoliday(h_id);

		List<HolidayItem> holidays = new ArrayList<HolidayItem>();
		HolidayItem holiday=new HolidayItem();
		
		
		holiday.setHolidayItem2Name(myholiday.getItemOneTitle());
		holiday.setHolidayItem2Value(myholiday.getItemOneCont());
		holiday.setHolidayItem3Name(myholiday.getItemTwoTitle());
		holiday.setHolidayItem3Value(myholiday.getItemTwoCont());
		holiday.setHolidayItem4Name(myholiday.getItemThreeTitle());
		holiday.setHolidayItem4Value(myholiday.getItemThreeCont());
		holiday.setHolidayItem5Name(myholiday.getItemFourTitle());
		holiday.setHolidayItem5Value(myholiday.getItemFourCont());
		Log.v("zhu", ""+holiday.getHolidayItem3Name()+"myholiday.getItemTwoTitle()"+myholiday.getItemThreeTitle());
		
//		DocumentBuilderFactory factory = null;
//		DocumentBuilder builder = null;
//		Document document = null;
//		InputStream inputStream = null;
//		// 首先找到xml文件
//		factory = DocumentBuilderFactory.newInstance();
//		try {
//			// 找到xml，并加载文档
//			builder = factory.newDocumentBuilder();
//		
//
//			try {
//				inputStream = pContext.openFileInput(fileName);
//			} catch (FileNotFoundException e) {
//				inputStream = pContext.getResources().getAssets()
//						.open(fileName);
//			}
//
//			document = builder.parse(inputStream);
//			// 找到根Element
//			Element root = document.getDocumentElement();
//			NodeList nodes = root.getElementsByTagName(HOLIDAY);
//			Log.v("heiheifang", "nodes.getLength() " + nodes.getLength());
//			// 遍历根节点所有子节点,rivers 下所有river
//			HolidayItem holiday = null;
//			for (int i = 0; i < nodes.getLength(); i++) {
//				holiday = new HolidayItem();
//				// 获取river元素节点
//				Element holidayElement = (Element) (nodes.item(i));
//				// 获取river中name属性值
//				Log.i("xiao", "name = " + holidayElement.getAttribute(NAME));
//				holiday.setHolidayName(holidayElement.getAttribute(NAME));
//
//				Element holidayItem1 = (Element) holidayElement
//						.getElementsByTagName(ITEM1NAME).item(0);
//				holiday.setHolidayItem1Name(holidayItem1.getAttribute(NAME));
//				NodeList item1ValueNodes = holidayItem1
//						.getElementsByTagName(ITEM1VALUE);
//				List<String> item1ValueList = new ArrayList<String>();
//				for (int j = 0; j < item1ValueNodes.getLength(); j++) {
//					Element item1Element = (Element) (item1ValueNodes.item(j));
//					item1ValueList.add(item1Element.getFirstChild()
//							.getNodeValue());
//				}
//				holiday.setHolidayItem1Value(item1ValueList);
//
//				Element holidayItem2Name = (Element) holidayElement
//						.getElementsByTagName(ITEM2NAME).item(0);
//				Log.i("xiao", "name2 = " + holidayItem2Name.getAttribute(NAME));
//				holiday.setHolidayItem2Name(holidayItem2Name.getAttribute(NAME));
//				Element holidayItem2Value = (Element) holidayItem2Name
//						.getElementsByTagName(ITEM2VALUE).item(0);
//				holiday.setHolidayItem2Value(holidayItem2Value.getFirstChild()
//						.getNodeValue());
//
//				Element holidayItem3Name = (Element) holidayElement
//						.getElementsByTagName(ITEM3NAME).item(0);
//				holiday.setHolidayItem3Name(holidayItem3Name.getAttribute(NAME));
//				Log.i("xiao", "name3 = " + holidayItem3Name.getAttribute(NAME));
//				Element holidayItem3Value = (Element) holidayItem3Name
//						.getElementsByTagName(ITEM3VALUE).item(0);
//				holiday.setHolidayItem3Value(holidayItem3Value.getFirstChild()
//						.getNodeValue());
//
//				Element holidayItem4Name = (Element) holidayElement
//						.getElementsByTagName(ITEM4NAME).item(0);
//				holiday.setHolidayItem4Name(holidayItem4Name.getAttribute(NAME));
//				Element holidayItem4Value = (Element) holidayItem4Name
//						.getElementsByTagName(ITEM4VALUE).item(0);
//				holiday.setHolidayItem4Value(holidayItem4Value.getFirstChild()
//						.getNodeValue());
//
//				Element holidayItem5Name = (Element) holidayElement
//						.getElementsByTagName(ITEM5NAME).item(0);
//				holiday.setHolidayItem5Name(holidayItem5Name.getAttribute(NAME));
//				Element holidayItem5Value = (Element) holidayItem5Name
//						.getElementsByTagName(ITEM5VALUE).item(0);
//				holiday.setHolidayItem5Value(holidayItem5Value.getFirstChild()
//						.getNodeValue());
//
//				Element holidayItem6 = (Element) holidayElement
//						.getElementsByTagName(ITEM6NAME).item(0);
//				if (holidayItem6 != null) {
//					Log.v("heiheifang", "hhhhhhhhhhhhhhhhhhhhh");
//					holiday.setHolidayItem6Name(holidayItem6.getAttribute(NAME));
//					NodeList item6ValueNodes = holidayItem6
//							.getElementsByTagName(ITEM6VALUE);
//					Log.v("heiheifang", "item6ValueNodes " + item6ValueNodes.getLength());
//
//					List<String> item6ValueList = new ArrayList<String>();
//					for (int j = 0; j < item6ValueNodes.getLength(); j++) {
//						Element item6Element = (Element) (item6ValueNodes
//								.item(j));
//						item6ValueList.add(item6Element.getFirstChild()
//								.getNodeValue());
//					}
//					holiday.setHolidayItem6Value(item6ValueList);
//				}

				holidays.add(holiday);
//			}
//		} catch (SAXException e) {
//			e.printStackTrace();
//			Log.i("xiao", "SAXException " + e.getMessage());
//		} catch (ParserConfigurationException e) {
//			Log.i("xiao", "ParserConfigurationException " + e.getMessage());
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			try {
//				if (inputStream != null) {
//					inputStream.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		return holidays;
	}
	
	private void initDetailLayoutView(){
		List<HolidayItem> holidayList = getHolidayInfoFromXml(getBaseContext(), mPresentClickItem);
		mHolidayItem = null;
		if(holidayList.size() > 0){
			Log.i("xiao", "size > 0");
			mHolidayItem = holidayList.get(0);
			mHolidayDetailArray[0].setText(mHolidayItem.getHolidayItem1Name());
			mHolidayDetailArray[1].setText(mHolidayItem.getHolidayItem2Name());
			mHolidayDetailArray[2].setText(mHolidayItem.getHolidayItem3Name());
			mHolidayDetailArray[3].setText(mHolidayItem.getHolidayItem4Name());
			mHolidayDetailArray[4].setText(mHolidayItem.getHolidayItem5Name());
			mHolidayOtherValue.setText(mHolidayItem.getHolidayItem2Value());
		}
		Log.i("xiao", "jjjjjjjjjjjjjjjjjjjjjjj");
		mHolidayDetailAdapter = new HolidayDetailAdapter(mHolidayItem.getHolidayItem1Value());
		mHolidayDetailIcon.setCompoundDrawablesWithIntrinsicBounds(null, mResources.getDrawable(mHolidayIconId[mPresentClickItem]), null, null);
		Log.i("xiao", "name = " + mHolidayItem.getHolidayName());
		mHolidayDetailIcon.setText(mHolidayItem.getHolidayName());
	}
	
	
	private void controlHolidayDetailItem(){
		for( int i = 0; i < 5; i++){
			if( i == mPresentSelectionItem){
				mHolidayDetailArray[i].setBackgroundResource(R.drawable.holiday_one_selected);
			}
			else{
				mHolidayDetailArray[i].setBackgroundResource(R.drawable.holiday_one);
			}
		}
	}
	
	private void controlHolidayDetailContent(){
		switch(mPresentSelectionItem){
		case 0:
			mHolidayMessageList.setVisibility(View.VISIBLE);
			mHolidayOtherValue.setVisibility(View.INVISIBLE);
			mHolidayMessageList.setAdapter(mHolidayDetailAdapter);
			break;
		case 1:
			mHolidayMessageList.setVisibility(View.INVISIBLE);
			mHolidayOtherValue.setVisibility(View.VISIBLE);
			mHolidayOtherValue.setText(mHolidayItem.getHolidayItem2Value());
			break;
		case 2:
			mHolidayMessageList.setVisibility(View.INVISIBLE);
			mHolidayOtherValue.setVisibility(View.VISIBLE);
			mHolidayOtherValue.setText(mHolidayItem.getHolidayItem3Value());
			break;
		case 3:
			mHolidayMessageList.setVisibility(View.INVISIBLE);
			mHolidayOtherValue.setVisibility(View.VISIBLE);
			mHolidayOtherValue.setText(mHolidayItem.getHolidayItem4Value());
			break;
		case 4:
			mHolidayMessageList.setVisibility(View.INVISIBLE);
			mHolidayOtherValue.setVisibility(View.VISIBLE);
			mHolidayOtherValue.setText(mHolidayItem.getHolidayItem5Value());
			break;
		}
	}
	
	public class HolidayDetailAdapter extends BaseAdapter{
		
		private List<String> mMessageList;
		
		public HolidayDetailAdapter(List<String> pMessageList){
			mMessageList = pMessageList;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mMessageList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mMessageList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = mInflater.inflate(R.layout.holiday_detail_item, null);
			TextView textView = (TextView)view.findViewById(R.id.mHolidayDetailItem);
			textView.setText(mMessageList.get(position));
			view.setOnClickListener(HolidayGridChooseSMS.this);
			view.setTag(position);
			return view;
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
