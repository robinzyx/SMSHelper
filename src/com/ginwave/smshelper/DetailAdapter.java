package com.ginwave.smshelper;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetailAdapter implements ListAdapter {
	private ArrayList<DetailEntity> coll;
	private Context ctx;

	public DetailAdapter(Context context, ArrayList<DetailEntity> coll) {
		ctx = context;
		this.coll = coll;
	}

	public boolean areAllItemsEnabled() {
		return false;
	}

	public boolean isEnabled(int arg0) {
		return false;
	}

	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		DetailEntity entity = coll.get(position);
		int itemLayout = entity.getLayoutID();

		RelativeLayout layout = new RelativeLayout(ctx);
		LayoutInflater vi = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vi.inflate(itemLayout, layout, true);

		TextView tvText = (TextView) layout
				.findViewById(R.id.mMmsContent);
		tvText.setText(entity.getText());
		return layout;
	}

	public int getViewTypeCount() {
		return coll.size();
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isEmpty() {
		return false;
	}

	public void registerDataSetObserver(DataSetObserver observer) {
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

}
