package com.ginwave.smshelper.pojos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Holiday {


	/*编号*/
	private Integer h_id;
    /*名称*/
    private String h_name;
	/*图标*/
	private String icon=null;
	/*日期*/
	private String month;
	/*类型*/
	private String type;
	/*介绍*/
	private String itemOneTitle;
	private String itemOneCont;
	/*由来*/
	private String itemTwoTitle;
	private String itemTwoCont;
	/*习俗*/
	private String itemThreeTitle;
	private String itemThreeCont;
	/*资料*/
	private String itemFourTitle;
	private String itemFourCont;

	/*创建时间*/
	private Date create_time=new Date();
	/*修改时间*/
	private Date modify_time=new Date();
	/*是否更新*/
	private Boolean is_up=false;
	/*短信*/
	private List<Sms> smslist= new ArrayList<Sms>();
	/*彩信*/
	private List<Mms> mmslist=new ArrayList<Mms>();
	
	private List<String> stringSms=new ArrayList<String>();
	private List<String> stringMms=new ArrayList<String>();
	
	public Integer getH_id() {
		return h_id;
	}
	public void setH_id(Integer h_id) {
		this.h_id = h_id;
	}
	public String getH_name() {
		return h_name;
	}
	public void setH_name(String h_name) {
		this.h_name = h_name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getItemOneTitle() {
		return itemOneTitle;
	}
	public void setItemOneTitle(String itemOneTitle) {
		this.itemOneTitle = itemOneTitle;
	}
	public String getItemOneCont() {
		return itemOneCont;
	}
	public void setItemOneCont(String itemOneCont) {
		this.itemOneCont = itemOneCont;
	}
	public String getItemTwoTitle() {
		return itemTwoTitle;
	}
	public void setItemTwoTitle(String itemTwoTitle) {
		this.itemTwoTitle = itemTwoTitle;
	}
	public String getItemTwoCont() {
		return itemTwoCont;
	}
	public void setItemTwoCont(String itemTwoCont) {
		this.itemTwoCont = itemTwoCont;
	}
	public String getItemThreeTitle() {
		return itemThreeTitle;
	}
	public void setItemThreeTitle(String itemThreeTitle) {
		this.itemThreeTitle = itemThreeTitle;
	}
	public String getItemThreeCont() {
		return itemThreeCont;
	}
	public void setItemThreeCont(String itemThreeCont) {
		this.itemThreeCont = itemThreeCont;
	}
	public String getItemFourTitle() {
		return itemFourTitle;
	}
	public void setItemFourTitle(String itemFourTitle) {
		this.itemFourTitle = itemFourTitle;
	}
	public String getItemFourCont() {
		return itemFourCont;
	}
	public void setItemFourCont(String itemFourCont) {
		this.itemFourCont = itemFourCont;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public Boolean getIs_up() {
		return is_up;
	}
	public void setIs_up(Boolean is_up) {
		this.is_up = is_up;
	}
	public List<Sms> getSmslist() {
		return smslist;
	}
	public void setSmslist(List<Sms> smslist) {
		this.smslist = smslist;
	}
	public List<Mms> getMmslist() {
		return mmslist;
	}
	public void setMmslist(List<Mms> mmslist) {
		this.mmslist = mmslist;
	}

	public List<String> getStringSms() {
		return stringSms;
	}
	public void setStringSms(List<String> stringSms) {
		this.stringSms = stringSms;
	}
	public List<String> getStringMms() {
		return stringMms;
	}
	public void setStringMms(List<String> stringMms) {
		this.stringMms = stringMms;
	}
	
	
	
	
}
