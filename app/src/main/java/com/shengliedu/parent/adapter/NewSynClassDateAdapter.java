package com.shengliedu.parent.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.activity.bean.SynClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class NewSynClassDateAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<SynClass> list;

	public NewSynClassDateAdapter(Context context, List<SynClass> list) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.list = list;
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

	@SuppressLint("ResourceAsColor")
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate

			(R.layout.new_sysclass_date_list, null);
			// viewHolder.child = (TextView) convertView.findViewById
			//
			// (R.id.child);
			viewHolder.begins_xingqi = (TextView) convertView
					.findViewById(R.id.begins_xingqi);
			viewHolder.begins_time = (TextView) convertView
					.findViewById(R.id.begins_time);
			viewHolder.begins_xingqi = (TextView) convertView
					.findViewById(R.id.begins_xingqi);
			viewHolder.begins_yiwancheng = (TextView) convertView
					.findViewById(R.id.begins_yiwancheng);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.begins_time.setText(list.get(position).dateStr);
		try {
			viewHolder.begins_xingqi.setText(dayForWeek(list.get(position)
					.dateStr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	public static class ViewHolder {
		public TextView begins_time, begins_xingqi, begins_yiwancheng;

	}

	private String dayForWeek(String pTime) throws Exception {
		String[] week = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		String dayForWeek = "";
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			dayForWeek = week[0];
		} else {
			dayForWeek = week[(c.get(Calendar.DAY_OF_WEEK) - 1)];
		}
		return dayForWeek;
	}
}
