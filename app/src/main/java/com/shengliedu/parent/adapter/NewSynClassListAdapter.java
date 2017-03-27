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
import com.shengliedu.parent.app.App;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressLint("UseSparseArrays")
public class NewSynClassListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<SynClass> list;
	private Context context;
	public NewSynClassListAdapter(Context context, List<SynClass> list) {
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

			(R.layout.new_synclasslist_item, null);
			// viewHolder.child = (TextView) convertView.findViewById
			//
			// (R.id.child);
			viewHolder.sub = (TextView) convertView
					.findViewById(R.id.sync_sub);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.sync_name);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.sync_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		convertView.setBackgroundResource(R.mipmap.loginback);
		viewHolder.sub.setText(App.getSubjectNameForId(list.get(position).subject));
		if (App.getSubjectNameForId(list.get(position).subject).length()>3){
			viewHolder.sub.setEms(2);
		}else {
			viewHolder.sub.setEms(1);
		}
		viewHolder.name.setText(list.get(position).name+"");
		if (list.get(position).date != 0) {
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
			viewHolder.time.setText(formatter.format(new Date(list.get(position).date*1000)));
		} else {
			viewHolder.time.setText("");
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView sub, name, time;
	}
}
