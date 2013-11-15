package com.ginwave.smshelper.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 保存数据库中的常量
 * @author jacp
 *
 */
public class Provider {
	public static final Uri AUTHORITY_URI = Uri.parse("content://com.ginwave.provider.demo.common");
	// 这里只有一个authority
	public static final String AUTHORITY = "com.ginwave.provider.demo.common";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ginwave.demo";

    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.ginwave.demo";

	public static final class HolidayColumns implements BaseColumns {
		// 注意这个地方和下面LeaderColumns类中CONTENT_URI一样，用的是同一个AUTHORITY
		public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/holidays");
		public static final String TABLE_NAME = "holiday";
		public static final String HOLIDAYID="h_id";
		public static final String HOLIDAYNAME = "h_name";
		public static final String CREATETIME = "create_time";
		public static final String MODIFYTIME = "modify_time";
		public static final String ICON = "icon";
		public static final String ISUP = "is_up";
		public static final String MONTH = "month";
		public static final String TYPE = "type";
		public static final String ITEMONETITLE = "itemOneTitle";
		public static final String ITEMONECONT = "itemOneCont";
		public static final String ITEMTWOTITLE = "itemTwoTitle";
		public static final String ITEMTWOCONT = "itemTwoCont";
		public static final String ITEMTHREETITLE = "itemThreeTitle";
		public static final String ITEMTHREECONT = "itemThreeCont";
		public static final String ITEMFOURTITLE = "itemFourTitle";
		public static final String ITEMFOURCONT = "itemFourCont";
		
	}
	public static final class SmsColumns implements BaseColumns {
		public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/smss");
		public static final String TABLE_NAME = "sms";
		public static final String SMSID = "sms_id";
		public static final String SMSCONT = "sms_cont";
		public static final String HOLIDAYID = "h_id";
		
	}
	
	public static final class MmsColumns implements BaseColumns {
		public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/mmss");
		public static final String TABLE_NAME = "mms";
		public static final String MMSID = "mms_id";
		public static final String MMSCONT = "mms_cont";
		public static final String MMSPIC = "mms_pic";
		public static final String HOLIDAYID = "h_id";
		
	}
	
}
