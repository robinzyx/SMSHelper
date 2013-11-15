package com.ginwave.smshelper.readcontacts;

import java.util.List;

import com.ginwave.smshelper.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class GroupAdapter extends BaseAdapter {

	private List<GroupInfo> mListData;
	private LayoutInflater mInflater;

	public GroupAdapter(Context context, List<GroupInfo> listdata) {
		this.mListData = listdata;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = mInflater.inflate(R.layout.listforgroupitem, null);
			holder.check = (CheckBox) view.findViewById(R.id.ischecked);
			holder.name = (TextView) view.findViewById(R.id.groupname);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.check.setChecked(mListData.get(position).isChecked());
		holder.name.setText((String) mListData.get(position).getGroupName());
		return view;

	}

	private class ViewHolder {
		private CheckBox check;
		private TextView name;
	}
}
