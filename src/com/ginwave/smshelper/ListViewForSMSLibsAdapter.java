package com.ginwave.smshelper;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewForSMSLibsAdapter extends BaseAdapter {

	private List<String> mItems;
	private LayoutInflater mInflater;

	public ListViewForSMSLibsAdapter(Context context, List<String> items) {
		this.mItems = items;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = mInflater.inflate(R.layout.listforsmslibsitem, null);
		}
		TextView text = (TextView) view.findViewById(R.id.list_item_text);
		text.setText(mItems.get(position));
		return view;
	}

	public void addItem(String item) {
		mItems.add(item);
	}

}
