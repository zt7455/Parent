package com.shengliedu.parent.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.parent.R;

public class AswerItemAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private Map<Integer, Boolean> map;
	private List<String> items;
	Context context;

	public AswerItemAdapter(Context context, Map<Integer, Boolean> map,List<String> items) {
		super();
		this.map = map;
		this.items = items;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = layoutInflater.inflate(R.layout.select_list, null);
			holder.select_a = (TextView) arg1.findViewById(R.id.select_a);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
			holder.select_a.setText(items.get(arg0));
		if (map.get(arg0) != null) {
			arg1.setBackgroundDrawable(context.getResources().getDrawable(
					R.mipmap.button2));
		} else {
			arg1.setBackgroundDrawable(context.getResources().getDrawable(
					R.mipmap.button1));
		}
		return arg1;
	}

	static class ViewHolder {
		TextView select_a;
	}

}
