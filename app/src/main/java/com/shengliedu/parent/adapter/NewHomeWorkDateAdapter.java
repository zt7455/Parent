package com.shengliedu.parent.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.new_homework.bean.HomeworkDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class NewHomeWorkDateAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<HomeworkDate> list;

	public NewHomeWorkDateAdapter(Context context, List<HomeworkDate> list) {
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

			(R.layout.new_after_date_list, null);
			// viewHolder.child = (TextView) convertView.findViewById
			//
			// (R.id.child);
			viewHolder.after_time = (TextView) convertView
					.findViewById(R.id.after_time);
			viewHolder.after_xingqi = (TextView) convertView
					.findViewById(R.id.after_xingqi);
			viewHolder.qipao = (TextView) convertView.findViewById(R.id.qipao);
			viewHolder.yiwancheng = (TextView) convertView
					.findViewById(R.id.yiwancheng);
			viewHolder.yiyue = (TextView) convertView.findViewById(R.id.yiyue);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.after_time.setText(list.get(position).dateStr);
		try {
			viewHolder.after_xingqi.setText(dayForWeek(list.get(position)
					.dateStr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String left_count = list.get(position).nooversize+"";
		viewHolder.after_time.setText(list.get(position).dateStr);
		// String finish_status = list.get(position).getFinish_status();
		if ("0".equals(left_count)) {
			viewHolder.yiwancheng.setText("[已完成]");
			viewHolder.yiwancheng.setTextColor(Color.GRAY);
			viewHolder.qipao.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.yiwancheng.setText("[未完成]");
			viewHolder.yiwancheng.setTextColor(Color.RED);
			// if ("0".equals(left_count)) {
			// viewHolder.qipao.setVisibility(View.INVISIBLE);
			// } else {
			viewHolder.qipao.setVisibility(View.VISIBLE);
			viewHolder.qipao.setText(left_count);
			// }
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView after_time, after_xingqi, qipao, yiwancheng, yiyue;

	}

	@SuppressLint("SimpleDateFormat")
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
