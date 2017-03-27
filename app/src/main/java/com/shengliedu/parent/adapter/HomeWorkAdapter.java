package com.shengliedu.parent.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.HomeWorkBean;
import com.shengliedu.parent.homework.PingJiaHomeWorkActivity;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.MyImageGetter;

import java.util.List;

public class HomeWorkAdapter extends BaseExpandableListAdapter {

	private List<HomeWorkBean> homework;
	private LayoutInflater layoutInflater;
	private BaseActivity context;
	private String date;

	public HomeWorkAdapter(BaseActivity context, List<HomeWorkBean> homework,String date) {
		super();
		this.homework = homework;
		this.context = context;
		this.date = date;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return homework.get(arg0).homework.get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.homework_list, null);
			holder.camera = (ImageView) convertView.findViewById(R.id.camera);
			holder.topic = (TextView) convertView.findViewById(R.id.topic);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.info = (TextView) convertView.findViewById(R.id.info);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		holder.topic.setText(Html.fromHtml(homework.get(groupPosition).homework
//				.get(childPosition).teaching.name));
		Spanned spanned = Html.fromHtml(homework.get(groupPosition).homework
				.get(childPosition).teaching.name,new MyImageGetter(context,holder.topic),null);
		holder.topic.setText(spanned);
		String content_type = homework.get(groupPosition).homework
				.get(childPosition).content_type;
		String submitType = homework.get(groupPosition).homework
				.get(childPosition).submitType;
		if ("1".equals(content_type) && "2".equals(submitType)) {
			holder.camera.setVisibility(View.VISIBLE);
			holder.camera.setBackgroundResource(R.mipmap.camera);
		} else if ("1".equals(content_type) && "3".equals(submitType)){
			holder.camera.setVisibility(View.VISIBLE);
			holder.camera.setBackgroundResource(R.mipmap.right_record);
		}else if ("1".equals(content_type) && "4".equals(submitType)){
			holder.camera.setVisibility(View.VISIBLE);
			holder.camera.setBackgroundResource(R.mipmap.right_vedio);
		}else {
			holder.camera.setVisibility(View.INVISIBLE);
		}

		holder.info.setText(homework.get(groupPosition).homework
				.get(childPosition).outlineName);
		String isFinish = homework.get(groupPosition).homework
				.get(childPosition).isFinish;
		Log.v("TAG", "name="+(homework.get(groupPosition).homework
				.get(childPosition).teaching.name)+"isFinish="+isFinish);
		if ("2".equals(homework.get(groupPosition).homework.get(childPosition).content_type)) {
			if ("1".equals(isFinish)) {
				holder.state.setText("[已完成]");
				holder.state.setTextColor(context.getResources().getColor(
						R.color.green));
			} else {
				holder.state.setText("[未完成]");
				holder.state.setTextColor(Color.RED);
			}
		}else if (!"2".equals(homework.get(groupPosition).homework.get(childPosition).content_type) && !"5".equals(homework.get(groupPosition).homework.get(childPosition).submitType)) {
			if ("1".equals(isFinish)) {
				holder.state.setText("[已完成]");
				holder.state.setTextColor(context.getResources().getColor(
						R.color.green));
			} else {
				holder.state.setText("[未完成]");
				holder.state.setTextColor(Color.RED);
			}
		}else if (!"2".equals(homework.get(groupPosition).homework.get(childPosition).content_type) && "5".equals(homework.get(groupPosition).homework.get(childPosition).submitType)) {
				holder.state.setText(" ");
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return homework.get(groupPosition).homework.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return homework.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		Log.v("TAG", "group.getHomework().size()=" + homework.size());
		return homework.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate
			(R.layout.homework_title, null);
			holder.sync_imageview = (ImageView) convertView
					.findViewById(R.id.sync_imageview);
			holder.title = (TextView) convertView.findViewById

			(R.id.title);

			holder.dynamic = (TextView) convertView.findViewById

			(R.id.dynamic);
			holder.jiaoshipingjia = (TextView) convertView.findViewById
					
					(R.id.jiaoshipingjia);
			holder.time = (TextView) convertView.findViewById
					
					(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(homework.get(groupPosition).name + ("1".equals(homework.get(groupPosition).part)?"-课前作业":("3".equals(homework.get(groupPosition).part)?"-课后作业":"")));
//		Log.v("TAG", "iii=" + homework.get(groupPosition).teacher.image);
		if (homework.get(groupPosition).teacher!=null&&!TextUtils.isEmpty(homework.get(groupPosition).teacher.image)) {
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Config1.getInstance().IP + homework.get(groupPosition).teacher.image, holder.sync_imageview);
		}
		String pingjia = homework.get(groupPosition).homeworkComment;
		if ("0".equals(pingjia)) {
			holder.jiaoshipingjia.setText("未评");
			holder.jiaoshipingjia.setTextColor(context.getResources().getColor(
					R.color.darkgray));
		} else if ("1".equals(pingjia)) {
			holder.jiaoshipingjia.setText("优");
			holder.jiaoshipingjia.setTextColor(context.getResources().getColor(
					R.color.green));
		} else if ("2".equals(pingjia)) {
			holder.jiaoshipingjia.setText("良");
			holder.jiaoshipingjia.setTextColor(context.getResources().getColor(
					R.color.greenyellow));
		} else if ("3".equals(pingjia)) {
			holder.jiaoshipingjia.setText("中");
			holder.jiaoshipingjia.setTextColor(context.getResources().getColor(
					R.color.yellow));
		} else if ("4".equals(pingjia)) {
			holder.jiaoshipingjia.setText("差");
			holder.jiaoshipingjia.setTextColor(context.getResources().getColor(
					R.color.red));
		} else {
			holder.dynamic.setText(pingjia);
		}
		String parentpingjia = homework.get(groupPosition).parentComment;
		if ("0".equals(parentpingjia)) {
			holder.dynamic.setText("未评");
			holder.dynamic.setTextColor(context.getResources().getColor(
					R.color.darkgray));
		} else if ("1".equals(parentpingjia)) {
			holder.dynamic.setText("优");
			holder.dynamic.setTextColor(context.getResources().getColor(
					R.color.green));
		} else if ("2".equals(parentpingjia)) {
			holder.dynamic.setText("良");
			holder.dynamic.setTextColor(context.getResources().getColor(
					R.color.greenyellow));
		} else if ("3".equals(parentpingjia)) {
			holder.dynamic.setText("中");
			holder.dynamic.setTextColor(context.getResources().getColor(
					R.color.yellow));
		} else if ("4".equals(parentpingjia)) {
			holder.dynamic.setText("差");
			holder.dynamic.setTextColor(context.getResources().getColor(
					R.color.red));
		} else {
			holder.dynamic.setText(pingjia);
		}
		holder.time.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(context,PingJiaHomeWorkActivity.class);
				intent.putExtra("date", date);
				intent.putExtra("subjectId", homework.get(groupPosition).id);
				context.startActivityForResult(intent, 200);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView sync_imageview;
		ImageView camera;
		TextView title, info, time, dynamic,jiaoshipingjia;
		TextView topic, state;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
}
