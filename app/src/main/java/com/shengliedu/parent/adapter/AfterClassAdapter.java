package com.shengliedu.parent.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.HomeWorkDetailQ;
import com.shengliedu.parent.bean.SynAnswers;
import com.shengliedu.parent.util.MyImageGetter;

import java.util.ArrayList;
import java.util.List;

public class AfterClassAdapter extends BaseAdapter {
	private HomeWorkDetailQ afc;
	private LayoutInflater layoutInflater;
//	private Map<Integer, Boolean> map;
	Context context;
	List<List<SynAnswers>> la = new ArrayList<List<SynAnswers>>();

	public AfterClassAdapter(Context context, HomeWorkDetailQ afc) {
		super();
		this.afc = afc;
		this.context = context;
		la = afc.answer.answers;
		for (int i = 0; i < la.get(0).size(); i++) {
			Log.e("212312312312312312323", la.get(0).get(i).toString());
		}
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return afc.answer.answers.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return afc.answer.answers.get(arg0);
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
		if (afc.answer != null && afc.answer.answers != null
				&& afc.answer.answers.size() != 0
				&& afc.answer.answers.get(0) != null
				&& afc.answer.answers.get(0).size() != 0) {
			Spanned spanned = Html.fromHtml(
					afc.answer.answers.get(arg0).get(0).title + ". "
							+ afc.answer.answers.get(arg0).get(0).content,
					new MyImageGetter(context, holder.select_a), null);
			holder.select_a.setText(spanned);
		}

//		if (map.get(arg0) != null) {
//			arg1.setBackgroundDrawable(context.getResources().getDrawable(
//					R.mipmap.button2));
//		} else {
//			arg1.setBackgroundDrawable(context.getResources().getDrawable(
//					R.mipmap.button1));
//		}
		return arg1;
	}

	static class ViewHolder {
		TextView select_a;
	}

}
