package com.ginwave.smshelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;
/**
 * 数据接口，需要重构方法HolidayDataSource
 * @author lyy
 *
 */
public class HolidayDataSource {
//	public static List<HolidayItem> getHolidayInfoFromXml(Context pContext,
//			String fileName) {
//		String HOLIDAY = "holiday";
//		String NAME = "name";
//		String ITEM1NAME = "item1name";
//		String ITEM1VALUE = "item1value";
//		String ITEM2NAME = "item2name";
//		String ITEM2VALUE = "item2value";
//		String ITEM3NAME = "item3name";
//		String ITEM3VALUE = "item3value";
//		String ITEM4NAME = "item4name";
//		String ITEM4VALUE = "item4value";
//		String ITEM5NAME = "item5name";
//		String ITEM5VALUE = "item5value";
//		String ITEM6NAME = "item6name";
//		String ITEM6VALUE = "item6value";
//
//		List<HolidayItem> holidays = new ArrayList<HolidayItem>();
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
//
//				holidays.add(holiday);
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
//		return holidays;
//	}

	public static class HolidayItem implements Serializable {
		private static final long serialVersionUID = 1L;
		private String mHolidayName;
		private String mHolidayItem1Name;
		private String mHolidayItem2Name;
		private String mHolidayItem3Name;
		private String mHolidayItem4Name;
		private String mHolidayItem5Name;
		private String mHolidayItem6Name;
		private List<String> mHolidayItem1Value;
		private String mHolidayItem2Value;
		private String mHolidayItem3Value;
		private String mHolidayItem4Value;
		private String mHolidayItem5Value;
		private List<String> mHolidayItem6Value;

		public void setHolidayName(String pName) {
			mHolidayName = pName;
		}

		public String getHolidayName() {
			return mHolidayName;
		}

		public void setHolidayItem1Name(String pName) {
			mHolidayItem1Name = pName;
		}

		public void setHolidayItem1Value(List<String> pName) {
			mHolidayItem1Value = pName;
		}

		public String getHolidayItem1Name() {
			return mHolidayItem1Name;
		}

		public List<String> getHolidayItem1Value() {
			return mHolidayItem1Value;
		}

		public void setHolidayItem2Name(String pName) {
			mHolidayItem2Name = pName;
		}

		public void setHolidayItem2Value(String pName) {
			mHolidayItem2Value = pName;
		}

		public String getHolidayItem2Name() {
			return mHolidayItem2Name;
		}

		public String getHolidayItem2Value() {
			return mHolidayItem2Value;
		}

		public void setHolidayItem3Name(String pName) {
			mHolidayItem3Name = pName;
		}

		public void setHolidayItem3Value(String pName) {
			mHolidayItem3Value = pName;
		}

		public String getHolidayItem3Name() {
			return mHolidayItem3Name;
		}

		public String getHolidayItem3Value() {
			return mHolidayItem3Value;
		}

		public void setHolidayItem4Name(String pName) {
			mHolidayItem4Name = pName;
		}

		public void setHolidayItem4Value(String pName) {
			mHolidayItem4Value = pName;
		}

		public String getHolidayItem4Name() {
			return mHolidayItem4Name;
		}

		public String getHolidayItem4Value() {
			return mHolidayItem4Value;
		}

		public void setHolidayItem5Name(String pName) {
			mHolidayItem5Name = pName;
		}

		public void setHolidayItem5Value(String pName) {
			mHolidayItem5Value = pName;
		}

		public String getHolidayItem5Name() {
			return mHolidayItem5Name;
		}

		public String getHolidayItem5Value() {
			return mHolidayItem5Value;
		}

		public void setHolidayItem6Name(String pName) {
			mHolidayItem6Name = pName;
		}

		public void setHolidayItem6Value(List<String> pName) {
			mHolidayItem6Value = pName;
		}

		public String getHolidayItem6Name() {
			return mHolidayItem6Name;
		}

		public List<String> getHolidayItem6Value() {
			return mHolidayItem6Value;
		}
	}
}
