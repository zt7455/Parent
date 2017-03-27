package com.shengliedu.parent.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.BeanJie;

public class JieAdapter extends BaseAdapter {
	List<BeanJie> jies;
	private LayoutInflater layoutInflater;
	private Map<Integer, Boolean> map;
	Context context;

	public JieAdapter(Context context, List<BeanJie> jies,
			Map<Integer, Boolean> map) {
		super();
		this.jies = jies;
		this.map = map;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jies.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return jies.get(arg0);
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
			arg1 = layoutInflater.inflate(R.layout.anjie_item, null);
			holder.select_a = (Button) arg1.findViewById(R.id.jia0);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		holder.select_a.setFocusable(false);
		holder.select_a.setClickable(false);
		holder.select_a.setText(jies.get(arg0).getName());
		if (map.get(arg0) != null) {
			holder.select_a.setBackgroundDrawable(context.getResources()
					.getDrawable(R.mipmap.anjie_checked));
		} else {
			holder.select_a.setBackgroundDrawable(context.getResources()
					.getDrawable(R.mipmap.anjie_nomal));
		}
		return arg1;
	}

	static class ViewHolder {
		Button select_a;
	}

}
