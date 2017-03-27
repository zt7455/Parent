package com.shengliedu.parent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.BeanLeiDa;

import java.util.HashMap;
import java.util.List;

public class HorizontalListViewAdapter extends BaseAdapter{
	private List<BeanLeiDa> dataList;
	private HashMap<Integer, Boolean> map;
	private Context con;
	public HorizontalListViewAdapter(Context con,List<BeanLeiDa> dataList,HashMap<Integer, Boolean> map){
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
			convertView = mInflater.inflate(R.layout.horizontallistview_item, null);
			vh.tv_kemu=(TextView) convertView.findViewById(R.id.tv_kemu);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder)convertView.getTag();
		}
		vh.tv_kemu.setText(dataList.get(position).subjectName);
		if (map.get(position)!=null) {
			vh.tv_kemu.setBackgroundColor(con.getResources().getColor(R.color.seagreen));
		}else {
			vh.tv_kemu.setBackgroundColor(con.getResources().getColor(R.color.transparent));
		}
		return convertView;
	}
}