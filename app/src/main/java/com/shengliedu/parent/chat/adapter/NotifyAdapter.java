package com.shengliedu.parent.chat.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.BeanNotify;
import com.shengliedu.parent.util.MyImageGetter;

public class NotifyAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<BeanNotify> list;
	private Context context;

	public NotifyAdapter(Context context, List<BeanNotify> list) {
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
			convertView = layoutInflater.inflate(R.layout.schoolnotify_item, null);
			viewHolder.message_title = (TextView) convertView
					.findViewById(R.id.message_title);
			viewHolder.jtittle = (TextView) convertView
					.findViewById(R.id.jtittle);
			viewHolder.tz_sj = (TextView) convertView
					.findViewById(R.id.tz_sj);
			viewHolder.tz_nr = (TextView) convertView
					.findViewById(R.id.tz_nr);
			viewHolder.tz_xx = (TextView) convertView
					.findViewById(R.id.tz_xx);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (!TextUtils.isEmpty(list.get(position).title)) {
			viewHolder.message_title.setText(list.get(position).title);
		}else {
			viewHolder.message_title.setText("通知");
		}
		viewHolder.jtittle.setText(list.get(position).addTimeStr);
		viewHolder.jtittle.setText(list.get(position).addTimeStr);
//		viewHolder.tz_nr.setText(Html.fromHtml(list.get(position).content));
		Spanned spanned = Html.fromHtml(list.get(position).content,new MyImageGetter(context,viewHolder.tz_nr),null);
		viewHolder.tz_nr.setText(spanned);
		viewHolder.tz_xx.setText(list.get(position).schoolName);
		viewHolder.tz_sj.setText(list.get(position).addTimeStr);
		return convertView;
	}

	public static class ViewHolder {
		public TextView tz_sj,tz_nr,tz_xx,tz_time,jtittle,message_title;
	}
}
