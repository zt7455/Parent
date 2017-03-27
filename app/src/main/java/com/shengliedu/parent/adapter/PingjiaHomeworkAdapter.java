package com.shengliedu.parent.adapter;

import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.parent.R;

public class PingjiaHomeworkAdapter extends BaseAdapter {
	private String[] afc;
	private LayoutInflater layoutInflater;
	private Map<Integer, Boolean> map;
	Context context;

	public PingjiaHomeworkAdapter(Context context, String[] afc,
			Map<Integer, Boolean> map) {
		super();
		this.afc = afc;
		this.map = map;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return afc.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return afc[arg0];
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
			holder.select_a.setText(afc[arg0]);

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
