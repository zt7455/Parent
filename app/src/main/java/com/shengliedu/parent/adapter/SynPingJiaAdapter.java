package com.shengliedu.parent.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.PingJiaOption;
import com.shengliedu.parent.util.MyImageGetter;

public class SynPingJiaAdapter extends BaseAdapter {
	List<PingJiaOption> afc;
	private LayoutInflater layoutInflater;
	private Context context;

	public SynPingJiaAdapter(Context context, List<PingJiaOption> afc) {
		super();
		this.afc = afc;
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
			arg1 = layoutInflater.inflate(R.layout.syn_select_question_list,
					null);
			holder.syn_select_a = (TextView) arg1
					.findViewById(R.id.syn_select_a);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		holder.syn_select_a.setGravity(Gravity.LEFT);
		Spanned spanned = Html.fromHtml(afc.get(arg0).content,new MyImageGetter(context,holder.syn_select_a),null);
		holder.syn_select_a.setText(spanned + "("+afc.get(arg0).score+"分)");
		holder.syn_select_a.setPadding(20, 0, 0, 0);
		if (arg0 % 3 == 0) {
			holder.syn_select_a.setTextColor(context.getResources().getColor(
					R.color.shuxue));
		} else if (arg0 % 3 == 1) {
			holder.syn_select_a.setTextColor(context.getResources().getColor(
					R.color.seagreen));
		} else if (arg0 % 3 == 2) {
			holder.syn_select_a.setTextColor(context.getResources().getColor(
					R.color.yingyu));
		}
		return arg1;
	}

	static class ViewHolder {
		TextView syn_select_a;
	}

}
