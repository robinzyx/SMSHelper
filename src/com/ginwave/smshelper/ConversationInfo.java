package com.ginwave.smshelper;

import android.util.Log;

public class ConversationInfo {
	public String mRecipient;
	public String mSnippet;
	public String mNumber;
	public Long mSnippetCS;
	public String mName;
	public String mThreadId;
	@Override
	public ConversationInfo clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		ConversationInfo conversationInfo = new ConversationInfo();
		conversationInfo.mRecipient = this.mRecipient;
		conversationInfo.mSnippet = this.mSnippet;
		conversationInfo.mNumber = this.mNumber;
		conversationInfo.mSnippetCS = this.mSnippetCS;
		conversationInfo.mName = this.mName;
		conversationInfo.mThreadId = this.mThreadId;
		return conversationInfo;
	}
	
	public ConversationInfo(){
		
	}
	
	public ConversationInfo(String pRecipient, String pSnippet, String pNumber, 
			Long pCS, String pName, String pThreadId){
		mRecipient = pRecipient;
		mSnippet = pSnippet;
		mNumber = pNumber;
		mSnippetCS = pCS;
		mName = pName;
		mThreadId = pThreadId;
		Log.i("xiao", "mNumber = " + mNumber);
	}
}
