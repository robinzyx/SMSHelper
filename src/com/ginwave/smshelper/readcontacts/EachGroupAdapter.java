package com.ginwave.smshelper.readcontacts;

import java.util.ArrayList;

import com.ginwave.smshelper.R;
import com.ginwave.smshelper.readcontacts.MultiContactAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class EachGroupAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	ArrayList<EachGroupInfo> itemList;

	public EachGroupAdapter(Context context,
			ArrayList<EachGroupInfo> itemList) {
		mInflater = LayoutInflater.from(context);
		this.itemList = itemList;
	}

	public ArrayList<EachGroupInfo> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<EachGroupInfo> itemList) {
		this.itemList = itemList;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return itemList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		convertView = mInflater.inflate(R.layout.listformulticontactitem, null);
		holder = new ViewHolder();
		holder.mname = (TextView) convertView.findViewById(R.id.mname);
		holder.msisdn = (TextView) convertView.findViewById(R.id.msisdn);
		holder.check = (CheckBox) convertView.findViewById(R.id.check);

		convertView.setTag(holder);
		holder.mname.setText(itemList.get(position).getContactName());
		holder.msisdn.setText(" " + itemList.get(position).getTelNumber());
		holder.check.setChecked(itemList.get(position).getIsChecked());
		return convertView;
	}

	class ViewHolder {
		TextView mname;
		TextView msisdn;
		CheckBox check;
	}

}
