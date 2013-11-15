package com.ginwave.smshelper;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.ginwave.smshelper.HolidayDataSource.HolidayItem;


public class SaxHoliday extends DefaultHandler {

	private List<HolidayItem> holidayList = null;
	private HolidayItem holiday = null;
	private String tagname;

	// 开始解析XML文档
	@Override
	public void startDocument() throws SAXException {
		// 初始化list
		holidayList = new ArrayList<HolidayItem>();
		super.startDocument();

	}

	StringBuffer sb = new StringBuffer();

	// 开始处理节点,在这里获得节点属性值的（节点属性）
	@Override
	public void startElement(String uri, String localName, String qName,

	Attributes attributes) throws SAXException {

		sb.delete(0, sb.length());
		if (localName.equals("holiday")) {
			holiday = new HolidayItem();
			holiday.setHolidayName(attributes.getValue("name"));
			// Log.v("localName holiday",
			// "holiday"+holiday+"localName"+localName);
		}
		this.tagname = localName;
		super.startElement(uri, localName, qName, attributes);
	} // 处理没有属性的节点（节点内容）

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (this.tagname != "") {

			if (this.tagname.equals("item1name")) {
				sb.append(ch, start, length);
				holiday.setHolidayItem1Name(sb.toString());
//				Log.v("zhu",
//						"holidayItem1Name=" + holiday.getHolidayItem1Name());

			}
			if (this.tagname.equals("item1value")) {
				sb.append(ch, start, length);
				PartString partString = new PartString();
				partString.getList(sb.toString());
				holiday.setHolidayItem1Value(partString.getList(sb.toString()));
//				Log.v("zhu", "item1value="
//						+ holiday.getHolidayItem1Value().size());

			}

			if (this.tagname.equals("item2name")) {
				sb.append(ch, start, length);
				holiday.setHolidayItem2Name(sb.toString());
				// Log.v("zhu","holidayItem2Name="+holiday.getmHolidayItem2Name());

			}
			if (this.tagname.equals("item2value")) {
				sb.append(ch, start, length);
				holiday.setHolidayItem2Value(sb.toString());
				// Log.v("zhu","item2value="+holiday.getmHolidayItem2Value());

			}

			if (this.tagname.equals("item3name")) {
				sb.append(ch, start, length);
				holiday.setHolidayItem3Name(sb.toString());
				// Log.v("zhu","holidayItem3Name="+holiday.getmHolidayItem3Name());

			}
			if (this.tagname.equals("item3value")) {
				sb.append(ch, start, length);
				holiday.setHolidayItem3Value(sb.toString());
				// Log.v("zhu","item3value="+holiday.getmHolidayItem3Value());

			}

			if (this.tagname.equals("item4name")) {
				sb.append(ch, start, length);
				holiday.setHolidayItem4Name(sb.toString());
				// Log.v("zhu","holidayItem4Name="+holiday.getmHolidayItem4Name());

			}
			if (this.tagname.equals("item4value")) {
				sb.append(ch, start, length);
				holiday.setHolidayItem4Value(sb.toString());
				// Log.v("zhu","item4value="+holiday.getmHolidayItem4Value());

			}
			if (this.tagname.equals("item5name")) {
				sb.append(ch, start, length);
				holiday.setHolidayItem5Name(sb.toString());
				// Log.v("zhu","holidayItem5Name="+holiday.getmHolidayItem5Name());

			}
			if (this.tagname.equals("item5value")) {
				sb.append(ch, start, length);
				holiday.setHolidayItem5Value(sb.toString());
				// Log.v("zhu","item5value="+holiday.getmHolidayItem5Value());

			}
			if (this.tagname.equals("item6name")) {
				sb.append(ch, start, length);
				holiday.setHolidayItem6Name(sb.toString());
				// Log.v("zhu","holidayItem5Name="+holiday.getmHolidayItem5Name());

			}
			if (this.tagname.equals("item6value")) {
				sb.append(ch, start, length);
				PartString partString = new PartString();
				partString.getList(sb.toString());
				holiday.setHolidayItem6Value(partString.getList(sb.toString()));
				// Log.v("zhu","item5value="+holiday.getmHolidayItem5Value());

			}
		}
		super.characters(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// 一个元素结束后往list里加入一个user对象
		if (localName.equals("holiday")) {
			holidayList.add(holiday);
		}
		super.endElement(uri, localName, qName);
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	// 返回user list对象

	public List<HolidayItem> getHolidayList() {
		return holidayList;
	}

}
