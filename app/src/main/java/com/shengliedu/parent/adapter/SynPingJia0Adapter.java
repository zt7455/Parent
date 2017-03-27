package com.shengliedu.parent.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.SynPingJia;
import com.shengliedu.parent.util.MyImageGetter;
import com.shengliedu.parent.view.NoScrollListview;

public class SynPingJia0Adapter extends BaseAdapter {
	List<SynPingJia> afc;
	private LayoutInflater layoutInflater;
	private Context context;

	public SynPingJia0Adapter(Context context, List<SynPingJia> afc) {
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
		View view = arg1 = layoutInflater.inflate(R.layout.ketangpingjia_item,
				null);
		TextView syn_question_title=(TextView) view.findViewById(R.id.syn_question_title);
		Spanned spanned = Html.fromHtml(afc.get(arg0).content,new MyImageGetter(context,syn_question_title),null);
		syn_question_title.setText((arg0+1) +"、"+spanned);
		NoScrollListview listview=(NoScrollListview) view.findViewById(R.id.lv);
		SynPingJiaAdapter adapter = new SynPingJiaAdapter(
				context, afc.get(arg0).option);
		listview.setAdapter(adapter);
		return view;
	}

	static class ViewHolder {
	}

}
