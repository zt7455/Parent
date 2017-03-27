package com.shengliedu.parent.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.ClassPlan;
import com.shengliedu.parent.util.HtmlImage;

import java.util.List;

public class ClassPlanAdapter extends BaseExpandableListAdapter {

	private List<ClassPlan> group;
	private LayoutInflater layoutInflater;
	Context context;

	public ClassPlanAdapter(Context context, List<ClassPlan> group) {
		super();
		this.group = group;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return group.get(arg0).content.get(arg1);
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
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.synclass_content,
					parent, false);
			holder.imageview_plan = (ImageView) convertView
					.findViewById(R.id.imageview_plan);
			holder.plan_info = (TextView) convertView
					.findViewById(R.id.plan_info);
			holder.plan_jieshu = (TextView) convertView
					.findViewById(R.id.plan_jieshu);
			holder.plan_tileixing = (TextView) convertView
					.findViewById(R.id.plan_tileixing);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		Spanned spanned = Html
//				.fromHtml(
//						group.get(groupPosition).content.get(childPosition).teaching.name,
//						new MyImageGetter(context, holder.plan_info), null);
		holder.plan_info.setText(Html.fromHtml(HtmlImage.deleteSrc(group.get(groupPosition).content
				.get(childPosition).teaching.name)));
		// holder.plan_info.setText(Html.fromHtml(group.get(groupPosition)
		// .content.get(childPosition).teaching.name));
		String status = group.get(groupPosition).content.get(childPosition).status;

		if ("1".equals(status)) {
			holder.plan_jieshu.setText("[已结束]");
			holder.plan_jieshu.setTextColor(context.getResources().getColor(
					R.color.green));
		} else {
			holder.plan_jieshu.setText("[未开始]");
			holder.plan_jieshu.setTextColor(Color.RED);
		}
		holder.plan_tileixing.setText(group.get(groupPosition).content
				.get(childPosition).teaching.questionTypeName);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return group.get(groupPosition).content.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return group.get(groupPosition).name;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return group.size();
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
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.synclass_title,
					parent, false);

			holder.sc_left = (TextView) convertView.findViewById(R.id.sc_left);

			holder.sc_right = (TextView) convertView 	 
					.findViewById(R.id.sc_right);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.sc_left.setText(group.get(groupPosition).modeName);
		holder.sc_right.setText(group.get(groupPosition).name);
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
		ImageView imageview_plan;
		TextView sc_left, sc_right;
		TextView plan_info, plan_jieshu, plan_tileixing;
	}

}
