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
import com.shengliedu.parent.bean.ClassPlanDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ClassPlanDateAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<ClassPlanDate> list;

	public ClassPlanDateAdapter(Context context, List<ClassPlanDate> pc) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.list = pc;
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
			convertView = layoutInflater.inflate(R.layout.sysclass_date_list,
					null);
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

		viewHolder.begins_time.setText(list.get(position).date);
		try {
			viewHolder.begins_xingqi.setText(dayForWeek(list.get(position)
					.date));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int status = list.get(position).status;

		if (status==0) {
			viewHolder.begins_yiwancheng.setText("[已结束]");
			viewHolder.begins_yiwancheng.setTextColor(Color.GRAY);
		} else if (status==1) {
			viewHolder.begins_yiwancheng.setText("[进行中]");
			viewHolder.begins_yiwancheng.setTextColor(Color.GREEN);
		} else if (status==2) {
			viewHolder.begins_yiwancheng.setText("[未开始]");
			viewHolder.begins_yiwancheng.setTextColor(Color.RED);
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
