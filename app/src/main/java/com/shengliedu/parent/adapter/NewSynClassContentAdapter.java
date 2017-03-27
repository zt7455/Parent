package com.shengliedu.parent.adapter;

import android.text.Html;
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
import com.shengliedu.parent.new_synclass.bean.BeanSynClassTree;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.HtmlImage;

import java.util.List;

public class NewSynClassContentAdapter extends BaseExpandableListAdapter {

	private List<BeanSynClassTree> synClassesTrees;
	private LayoutInflater layoutInflater;
	BaseActivity context;

	public NewSynClassContentAdapter(BaseActivity context, List<BeanSynClassTree> synClassesTrees) {
		super();
		this.synClassesTrees = synClassesTrees;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return synClassesTrees.get(arg0).beanSynClasses.get(arg1);
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
			holder.imgLin = (LinearLayout) convertView
					.findViewById(R.id.imgLin);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imgLin.removeAllViews();
		JSONObject object= JSON.parseObject(synClassesTrees.get(groupPosition).beanSynClasses
				.get(childPosition).teaching);
		deal_text(object.getString("name"),holder.imgLin,holder.plan_info);
			holder.plan_jieshu.setVisibility(View.GONE);
		String tixing="";
		if (synClassesTrees.get(groupPosition).beanSynClasses
				.get(childPosition).content_type==1){
			tixing="[素材题]";
		}else if (synClassesTrees.get(groupPosition).beanSynClasses
				.get(childPosition).content_type==2){
			if (object.getInteger("questionType")==1){
				tixing="[单选题]";
			}else if (object.getInteger("questionType")==2){
				tixing="[多选题]";
			}else if (object.getInteger("questionType")==3){
				tixing="[判断题]";
			}else {
				tixing="[其他题]";
			}
		}
		holder.plan_tileixing.setText(tixing);
		return convertView;
	}
	private void deal_text(String text, LinearLayout linearLayout, TextView textView){
		textView.setText(Html.fromHtml(HtmlImage
				.deleteSrc(text)));
		List<String> imgList = HtmlImage.getImgSrc(text);
		ImageLoader utils = ImageLoader.getInstance();
		if (imgList.size() > 0) {
			linearLayout.removeAllViews();
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
		return synClassesTrees.get(groupPosition).beanSynClasses.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return synClassesTrees.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return synClassesTrees.size();
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
		holder.sc_right.setVisibility(View.GONE);
		holder.sc_left.setText(synClassesTrees.get(groupPosition).catalogName+"");

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
		LinearLayout imgLin;
		ImageView imageview_plan;
		TextView sc_left, sc_right;
		TextView plan_info, plan_jieshu, plan_tileixing;
	}

}
