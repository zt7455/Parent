package com.shengliedu.parent.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.BeanExamList;
import com.shengliedu.parent.bean.BeanExamScore;

public class ScoreAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<BeanExamScore> list;
	private Context context;

	public ScoreAdapter(Context context, BeanExamList beanExamList) {
		// TODO Auto-generated constructor stub
		this.list = beanExamList.subjectScoreList;
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
			convertView = layoutInflater.inflate(R.layout.score_list, parent,false);
			viewHolder.kemu = (TextView) convertView
					.findViewById(R.id.kemu);
			viewHolder.chengji = (TextView) convertView
					.findViewById(R.id.chengji1);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Log.v("TAG", "kemu="+viewHolder.kemu);
		Log.v("TAG", "chengji="+viewHolder.chengji);
		Log.v("TAG", "name="+list.get(position).name);
		Log.v("TAG", "score="+list.get(position).score);
		viewHolder.kemu.setText(list.get(position).name);
		viewHolder.chengji.setText(""+list.get(position).score);
		return convertView;
	}

	public static class ViewHolder {
		public TextView kemu,chengji;
	}
}
