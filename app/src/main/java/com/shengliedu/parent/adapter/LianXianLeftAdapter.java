package com.shengliedu.parent.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.LianXianItems;
import com.shengliedu.parent.util.MyImageGetter;

public class LianXianLeftAdapter extends BaseAdapter {
	private List<LianXianItems> afc;
	private LayoutInflater layoutInflater;
	private Map<Integer, Boolean> map;
	Context context;

	public LianXianLeftAdapter(Context context, List<LianXianItems> afc,
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
		return afc.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return afc.get(arg0);
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
		String itemCode = null;
		switch (arg0) {
		case 0:
			itemCode = "A. ";
			break;
		case 1:
			itemCode = "B. ";
			break;
		case 2:
			itemCode = "C. ";
			break;
		case 3:
			itemCode = "D. ";
			break;
		case 4:
			itemCode = "E. ";
			break;
		case 5:
			itemCode = "F. ";
			break;
		case 6:
			itemCode = "G. ";
			break;
		case 7:
			itemCode = "H. ";
			break;
		case 8:
			itemCode = "I. ";
			break;
		case 9:
			itemCode = "J. ";
			break;
		default:
			break;
		}
		if (afc != null && afc.size() != 0) {
//			holder.select_a.setText(itemCode
//					+ Html.fromHtml(afc.get(arg0).content));
			Spanned spanned = Html.fromHtml(afc.get(arg0).content,new MyImageGetter(context,holder.select_a),null);
			holder.select_a.setText(spanned);
		}

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
