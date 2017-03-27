package com.shengliedu.parent.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.BeanExceptionReport;

public class ExceptionAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<BeanExceptionReport> list;
	private Context context;

	public ExceptionAdapter(Context context, List<BeanExceptionReport> list) {
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
			convertView = layoutInflater.inflate(R.layout.exception_item, null);
			viewHolder.exception_time = (TextView) convertView
					.findViewById(R.id.exception_time);
			viewHolder.exception_tt = (TextView) convertView
					.findViewById(R.id.exception_tt);
			viewHolder.exception_content = (TextView) convertView
					.findViewById(R.id.exception_content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.exception_time.setText(list.get(position).date);
		viewHolder.exception_tt.setText(list.get(position).name);
		viewHolder.exception_content.setText(list.get(position).content);
		return convertView;
	}

	public static class ViewHolder {
		public TextView exception_time,exception_tt,exception_content;
	}
}
