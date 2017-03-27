package com.shengliedu.parent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.Chengji;

import java.util.HashMap;
import java.util.List;

public class SubListViewAdapter extends BaseAdapter{
	private List<Chengji> dataList;
	private HashMap<Integer, Boolean> map;
	private Context con;
	public SubListViewAdapter(Context con,List<Chengji> dataList,HashMap<Integer, Boolean> map){
		mInflater=LayoutInflater.from(con);
		this.dataList=dataList;
		this.con = con;
		this.map = map;
	}
	@Override
	public int getCount() {
		return dataList.size();
	}
	private LayoutInflater mInflater;
	@Override
	public Object getItem(int position) {
		return position;
	}
	private ViewHolder vh 	 =new ViewHolder();
	private static class ViewHolder {
		private TextView tv_kemu ;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.sub_item, parent,false);
			vh.tv_kemu=(TextView) convertView.findViewById(R.id.tv_kemu);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder)convertView.getTag();
		}
		vh.tv_kemu.setText(dataList.get(position).name);
		if (map.get(position)!=null) {
			vh.tv_kemu.setBackgroundColor(con.getResources().getColor(R.color.seagreen));
		}else {
			vh.tv_kemu.setBackgroundColor(con.getResources().getColor(android.R.color.transparent));
		}
		return convertView;
	}
}