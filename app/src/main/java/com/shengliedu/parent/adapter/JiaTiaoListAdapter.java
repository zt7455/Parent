package com.shengliedu.parent.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.JiaTiaoClass;
import com.shengliedu.parent.more.jiatiao.QingJiaActivity;

@SuppressLint("UseSparseArrays")
public class JiaTiaoListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<JiaTiaoClass> list;
	private BaseActivity context;

	public JiaTiaoListAdapter(BaseActivity context, List<JiaTiaoClass> list) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
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

			(R.layout.jiatiao_list, null);
			// viewHolder.child = (TextView) convertView.findViewById
			//
			// (R.id.child);
			viewHolder.start_date = (TextView) convertView
					.findViewById(R.id.start_time);
			viewHolder.zhi = (TextView) convertView.findViewById(R.id.zhi);
			viewHolder.end_date = (TextView) convertView
					.findViewById(R.id.end_time);
			viewHolder.type = (TextView) convertView
					.findViewById(R.id.jiatiao_type);
			viewHolder.agree = (TextView) convertView
					.findViewById(R.id.jiatiao_agree);
//			viewHolder.add = (ImageView) convertView
//					.findViewById(R.id.jiatiao_add);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd");

		if ("0".equals(list.get(position).type)) {
//			viewHolder.type.setVisibility(View.GONE);
//			viewHolder.agree.setVisibility(View.GONE);
//			viewHolder.add.setVisibility(View.VISIBLE);
//			viewHolder.start_date.setVisibility(View.INVISIBLE);
//			viewHolder.end_date.setVisibility(View.INVISIBLE);
//			viewHolder.zhi.setVisibility(View.INVISIBLE);
//			viewHolder.add.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					Intent intent = new Intent(context, QingJiaActivity.class);
//					context.startActivityForResult(intent, 400);
//				}
//			});
		} else if ("1".equals(list.get(position).type)) {
			viewHolder.start_date.setVisibility(View.VISIBLE);
			viewHolder.end_date.setVisibility(View.VISIBLE);
			viewHolder.zhi.setVisibility(View.VISIBLE);
			viewHolder.type.setVisibility(View.VISIBLE);
//			viewHolder.add.setVisibility(View.GONE);
			viewHolder.type.setText("事假");
			viewHolder.type.setTextColor(context.getResources().getColor(
					R.color.seagreen));

			if ("0".equals(list.get(position).approve)) {
				viewHolder.agree.setVisibility(View.VISIBLE);
				viewHolder.agree.setText("待同意");
				viewHolder.agree.setTextColor(context.getResources().getColor(
						R.color.red));
			} else if ("1".equals(list.get(position).approve)){
				viewHolder.agree.setVisibility(View.VISIBLE);
				viewHolder.agree.setText("已同意");
				viewHolder.agree.setTextColor(context.getResources().getColor(
						R.color.seagreen));
			}else if ("2".equals(list.get(position).approve)){
				viewHolder.agree.setVisibility(View.VISIBLE);
				viewHolder.agree.setText("已驳回");
				viewHolder.agree.setTextColor(context.getResources().getColor(
						R.color.black));
			}else{
				viewHolder.agree.setVisibility(View.GONE);
			}
			viewHolder.start_date.setText(dateFormat.format(new Date(list.get(
					position).stime * 1000)));
			viewHolder.end_date.setText(dateFormat.format(new Date(list.get(
					position).etime * 1000)));
		} else if ("2".equals(list.get(position).type)) {
			viewHolder.start_date.setVisibility(View.VISIBLE);
			viewHolder.end_date.setVisibility(View.VISIBLE);
			viewHolder.zhi.setVisibility(View.VISIBLE);
			viewHolder.type.setVisibility(View.VISIBLE);
//			viewHolder.add.setVisibility(View.GONE);
			viewHolder.type.setText("病假");
			viewHolder.type.setTextColor(context.getResources().getColor(
					R.color.shuxue));
			if ("0".equals(list.get(position).approve)) {
				viewHolder.agree.setVisibility(View.VISIBLE);
				viewHolder.agree.setText("待同意");
				viewHolder.agree.setTextColor(context.getResources().getColor(
						R.color.red));
			} else if ("1".equals(list.get(position).approve)){
				viewHolder.agree.setVisibility(View.VISIBLE);
				viewHolder.agree.setText("已同意");
				viewHolder.agree.setTextColor(context.getResources().getColor(
						R.color.seagreen));
			}else if ("2".equals(list.get(position).approve)){
				viewHolder.agree.setVisibility(View.VISIBLE);
				viewHolder.agree.setText("已驳回");
				viewHolder.agree.setTextColor(context.getResources().getColor(
						R.color.black));
			}else{
				viewHolder.agree.setVisibility(View.GONE);
			}
			viewHolder.start_date.setText(dateFormat.format(new Date(list.get(
					position).stime * 1000)));
			viewHolder.end_date.setText(dateFormat.format(new Date(list.get(
					position).etime * 1000)));
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView start_date, end_date, type, agree, zhi;
//		public ImageView add;
	}
}
