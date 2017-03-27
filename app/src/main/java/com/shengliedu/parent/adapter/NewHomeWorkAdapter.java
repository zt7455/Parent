package com.shengliedu.parent.adapter;

import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.activity.bean.HomeworkTree;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.HtmlImage;

import java.util.List;

public class NewHomeWorkAdapter extends BaseExpandableListAdapter {

	private List<HomeworkTree> homework;
	private LayoutInflater layoutInflater;
	private BaseActivity context;

	public NewHomeWorkAdapter(BaseActivity context, List<HomeworkTree> homework) {
		super();
		this.homework = homework;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return homework.get(arg0).homeworks.get(arg1);
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
			convertView = layoutInflater.inflate(R.layout.new_homework_list, null);
			holder.camera = (ImageView) convertView.findViewById(R.id.camera);
			holder.topic = (TextView) convertView.findViewById(R.id.topic);
			holder._textLin = (LinearLayout) convertView.findViewById(R.id._textLin);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.outline_name = (TextView) convertView.findViewById(R.id.outline_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		holder.topic.setText(Html.fromHtml(homework.get(groupPosition).homework
//				.get(childPosition).teaching.name));
		String content_type = homework.get(groupPosition).homeworks
				.get(childPosition).content_type+"";
		String submitType = homework.get(groupPosition).homeworks
				.get(childPosition).submitType+"";
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
		JSONObject object= JSON.parseObject(homework.get(groupPosition).homeworks
				.get(childPosition).teaching);
//		Spanned spanned = Html.fromHtml(object.getString("name"),new MyImageGetter(context,holder.topic),null);
//		holder.topic.setText(spanned);
		holder._textLin.removeAllViews();
		deal_text(object.getString("name"),holder._textLin,holder.topic);
		String isFinish = homework.get(groupPosition).homeworks
				.get(childPosition).content+"";
		Log.v("TAG", "name="+(object.getString("name"))+"isFinish="+isFinish);
		if ("2".equals(homework.get(groupPosition).homeworks.get(childPosition).content_type)) {
			if (!"0".equals(isFinish)) {
				holder.state.setText("[已完成]");
				holder.state.setTextColor(context.getResources().getColor(
						R.color.green));
			} else {
				holder.state.setText("[未完成]");
				holder.state.setTextColor(Color.RED);
			}
		}else if (!"2".equals(homework.get(groupPosition).homeworks.get(childPosition).content_type) && !"5".equals(homework.get(groupPosition).homeworks.get(childPosition).submitType)) {
			if (!"0".equals(isFinish)) {
				holder.state.setText("[已完成]");
				holder.state.setTextColor(context.getResources().getColor(
						R.color.green));
			} else {
				holder.state.setText("[未完成]");
				holder.state.setTextColor(Color.RED);
			}
		}else if (!"2".equals(homework.get(groupPosition).homeworks.get(childPosition).content_type) && "5".equals(homework.get(groupPosition).homeworks.get(childPosition).submitType)) {
				holder.state.setText(" ");
		}
		if (!TextUtils.isEmpty(homework.get(groupPosition).homeworks.get(childPosition).unitSectionHour)){
			holder.outline_name.setText(homework.get(groupPosition).homeworks.get(childPosition).unitSectionHour);
		}
		return convertView;
	}

	private void deal_text(String text, LinearLayout linearLayout, TextView textView){
		textView.setText(Html.fromHtml(HtmlImage
				.deleteSrc(text)));
		List<String> imgList = HtmlImage.getImgSrc(text);
		ImageLoader utils = ImageLoader.getInstance();
		if (imgList.size() > 0) {
			for (int j = 0; j < imgList.size(); j++) {
				final String url = imgList.get(j);
				ImageView imageView = new ImageView(context);
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new RelativeLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				utils.displayImage((url.contains("http")?url: (Config1.getInstance().IP+url)), imageView);
				imageView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated
						// method stub
						HtmlImage htmlImage = new HtmlImage();
						htmlImage.showDialog(context, (url.contains("http")?url: (Config1.getInstance().IP+url)));
					}
				});
				linearLayout.addView(imageView);
			}
		}
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return homework.get(groupPosition).homeworks.size();
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
			(R.layout.homework_new_title, parent,false);
			holder.title = (TextView) convertView.findViewById

			(R.id.title);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(homework.get(groupPosition).subjectpart);

		return convertView;
	}

	static class ViewHolder {
		ImageView camera;
		TextView title,outline_name;
		LinearLayout _textLin;
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
