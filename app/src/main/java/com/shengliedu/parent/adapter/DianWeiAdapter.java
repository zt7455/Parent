package com.shengliedu.parent.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.BeanExamScore;

public class DianWeiAdapter extends BaseAdapter {
	private Context con;
	private List<BeanExamScore> dianweiData;

	public DianWeiAdapter(Context con, List<BeanExamScore> dianweiData) {
		mInflater = LayoutInflater.from(con);
		this.dianweiData = dianweiData;
		this.con = con;
	}

	@Override
	public int getCount() {
		return dianweiData.size();
	}

	private LayoutInflater mInflater;

	@Override
	public Object getItem(int position) {
		return position;
	}

	private static class ViewHolder {
		private TextView tv_count, tv_ren;
		private ImageView zheer;
		private LinearLayout linearLayout;

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.dianwei_list_item, null);
			vh = new ViewHolder();
			vh.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
			vh.tv_ren = (TextView) convertView.findViewById(R.id.tv_ren);
			vh.zheer = (ImageView) convertView.findViewById(R.id.zheer);
			vh.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.point_lay);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Log.v("TAG", "dianweiData.size()=" + dianweiData.size());
		vh.tv_count.setText(formatNumber(""
				+ dianweiData.get(position).scoreName));
		vh.tv_ren.setText("(" + dianweiData.get(position).scoreCount + "人)");
		Log.v("TAG", "position=" + position);
		vh.linearLayout.removeAllViews();
		for (int i = 0; i < dianweiData.get(position).scoreCount; i++) {
			ImageView imageView = new ImageView(con);
			imageView.setImageResource(R.mipmap.point_greed);
			vh.linearLayout.addView(imageView);
		}
		if (dianweiData.get(position).isIn == 1) {
			vh.zheer.setVisibility(View.VISIBLE);
		} else {
			vh.zheer.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	private String formatNumber(String numString) {
		// TODO Auto-generated method stub
		if (numString.length() == 1) {
			return "  " + numString;
		} else if (numString.length() == 2) {
			return " " + numString;
		}
		return numString;
	}
}