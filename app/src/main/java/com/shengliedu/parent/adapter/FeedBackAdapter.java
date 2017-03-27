package com.shengliedu.parent.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.Beanfeedback;

public class FeedBackAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<Beanfeedback> list;
	private Context context;

	public FeedBackAdapter(Context context, List<Beanfeedback> list) {
		// TODO Auto-generated constructor stub
		this.list = list;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.feedback_list, null);
			viewHolder.nikename = (TextView) convertView
					.findViewById(R.id.feedback_name);
			viewHolder.feedback_date = (TextView) convertView
					.findViewById(R.id.feedback_date);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.feedback_date.setText(list.get(position).addDateStr);
		viewHolder.nikename.setText(list.get(position).suggestion);
		return convertView;
	}

	public static class ViewHolder {
		public TextView nikename,feedback_date;
	}
}
