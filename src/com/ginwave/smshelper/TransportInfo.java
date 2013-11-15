package com.ginwave.smshelper;

public class TransportInfo {
	private String mNumber;
	private String mName;
	private String mThreadId;
	public TransportInfo(String pNumber, String pName, String pThreadId){
		mNumber = pNumber;
		mName = pName;
		mThreadId = pThreadId;
	}
	
	public String getNumber(){
		return mNumber;
	}
	
	public String getName(){
		return mName;
	}
	
	public String getThreadId(){
		return mThreadId;
	}
}
