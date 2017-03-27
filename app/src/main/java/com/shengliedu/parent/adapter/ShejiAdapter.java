package com.shengliedu.parent.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.Beike;
import com.shengliedu.parent.util.HtmlImage;

public class ShejiAdapter extends BaseExpandableListAdapter {

	private List<Beike> guide;
	private LayoutInflater layoutInflater;
	Activity context;

	public ShejiAdapter(Activity context, List<Beike> guide) {
		super();
		this.guide = guide;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return guide.get(arg0).content.get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.sheji_child_item,
					parent, false);
			viewHolder.sheji_child_item_text = (TextView) convertView
					.findViewById(R.id.sheji_child_item_text);
			viewHolder.sheji = (LinearLayout) convertView
					.findViewById(R.id.sheji);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.sheji_child_item_text.setText(Html.fromHtml(guide.get(groupPosition).content.get(childPosition).name));
		String main = guide.get(groupPosition).content.get(childPosition).name;
		List<String> imgList = HtmlImage.getImgSrc(main);
		ImageLoader utils = ImageLoader.getInstance();
		if (imgList.size() > 0) {
			for (int j = 0; j < imgList.size(); j++) {
				final String url = imgList.get(j);
				ImageView imageView = new ImageView(context);
				imageView.setScaleType(ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				utils.displayImage(url, imageView);
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated
						// method stub
						HtmlImage htmlImage = new HtmlImage();
						htmlImage.showDialog(context, url);
					}
				});
				viewHolder.sheji.addView(imageView);
			}
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return guide.get(groupPosition).content.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return guide.get(groupPosition).name;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return guide.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.sheji_group_item,
					parent, false);
			viewHolder.sheji_group_item_title = (TextView) convertView
					.findViewById(R.id.sheji_group_item_title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.sheji_group_item_title.setText(guide.get(groupPosition).title);
		return convertView;
	}

	@Override	
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	static class ViewHolder {
		public TextView sheji_group_item_title;
		public TextView sheji_child_item_text;
		public LinearLayout sheji;
	}

}
