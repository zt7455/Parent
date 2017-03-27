package com.shengliedu.parent.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.SheJi;
import com.shengliedu.parent.util.MyImageGetter;

import java.util.List;

public class SynClassSheJiAdapter extends BaseExpandableListAdapter {

	private List<SheJi> group;
	private LayoutInflater layoutInflater;
	Context context;

	public SynClassSheJiAdapter(Context context, List<SheJi> group) {
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
			convertView = layoutInflater.inflate(
					R.layout.synclass_sheji_list_item_content, null);
			holder.sheji_sc_content = (TextView) convertView
					.findViewById(R.id.sheji_sc_content);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

//		holder.sheji_sc_content.setText(Html.fromHtml(group.get(groupPosition).content
//				.get(childPosition).content));
		Spanned spanned = Html.fromHtml(group.get(groupPosition).content
				.get(childPosition).content,new MyImageGetter(context,holder.sheji_sc_content),null);
		holder.sheji_sc_content.setText(spanned);
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
		return group.get(groupPosition);
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
			convertView = layoutInflater.inflate(
					R.layout.synclass_sheji_list_item, null);

			holder.sc_sheji_left = (TextView) convertView
					.findViewById(R.id.sc_sheji_left);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.sc_sheji_left.setText(group.get(groupPosition).title);
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
		TextView sc_sheji_left;
		TextView sheji_sc_content;
	}

}
