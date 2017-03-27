package com.shengliedu.parent.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.SynClass;
import com.shengliedu.parent.util.Config1;

import java.util.List;

@SuppressLint("UseSparseArrays")
public class SynClassListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<SynClass> list;
	private Context context;
	public SynClassListAdapter(Context context, List<SynClass> list) {
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

			(R.layout.synclasslist_item, null);
			// viewHolder.child = (TextView) convertView.findViewById
			//
			// (R.id.child);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.sync_title);
			viewHolder.info = (TextView) convertView
					.findViewById(R.id.sync_info);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.sync_time);
			viewHolder.dynamic = (TextView) convertView
					.findViewById(R.id.sync_dynamic);
			viewHolder.syn_pingjia = (TextView) convertView
					.findViewById(R.id.syn_pingjia);
			viewHolder.sync_imageview = (ImageView) convertView
					.findViewById(R.id.sync_imageview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		convertView.setBackgroundResource(R.mipmap.loginback);
		viewHolder.title.setText(list.get(position).title);
		if (list.get(position).outlineName != null) {
			viewHolder.info.setText(list.get(position).outlineName);
		} else {
			viewHolder.info.setText("");
		}
		viewHolder.time.setText(list.get(position).timeScopeStr);
		String Status = list.get(position).status;
		if ("0".equals(Status)) {
			viewHolder.dynamic.setText("[已完结]");
			viewHolder.dynamic.setTextColor(Color.GRAY);
			convertView.setBackgroundResource(R.mipmap.loginback);
		} else if ("1".equals(Status)) {
			viewHolder.dynamic.setText("[进行中]");
			viewHolder.dynamic.setTextColor(Color.YELLOW);
			convertView.setBackgroundResource(R.color.jingxingzhong);
			convertView.setBackgroundResource(R.color.lowblue);
		} else {
			viewHolder.dynamic.setText("[未开始]");
			viewHolder.dynamic.setTextColor(Color.GRAY);
			convertView.setBackgroundResource(R.mipmap.loginback);
		}
		if (!TextUtils.isEmpty(list.get(position).image)) {
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Config1.getInstance().IP+list.get(position).image, viewHolder.sync_imageview);
		}
		int score = list.get(position).score;
		if (score == 0) {
			viewHolder.syn_pingjia.setText("未评");
			viewHolder.syn_pingjia.setTextColor(Color.GRAY);
		} else if (score >= 60 && score < 70) {
			viewHolder.syn_pingjia.setText(score + "");
			viewHolder.syn_pingjia.setTextColor(context.getResources().getColor(R.color.red));
		} else if (score >= 70 && score < 80) {
			viewHolder.syn_pingjia.setText(score + "");
			viewHolder.syn_pingjia.setTextColor(context.getResources().getColor(R.color.yellow));
		} else if (score >= 80 && score < 90) {
			viewHolder.syn_pingjia.setText(score + "");
			viewHolder.syn_pingjia.setTextColor(context.getResources().getColor(R.color.greenyellow));
		} else if (score >= 90) {
			viewHolder.syn_pingjia.setText(score + "");
			viewHolder.syn_pingjia.setTextColor(context.getResources().getColor(R.color.green));
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView title, info, time, dynamic,syn_pingjia;
		public ImageView sync_imageview;

	}
}
