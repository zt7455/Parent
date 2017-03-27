package com.shengliedu.parent.adapter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.ImageArr;
import com.shengliedu.parent.bean.ShaiClass;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.HandlerMessageObj;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

@SuppressLint("UseSparseArrays")
public class ShaiListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<ShaiClass> list;
	private BaseActivity context;

	public ShaiListAdapter(BaseActivity context, List<ShaiClass> list) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		Log.v("TAG", "2"+list.size());
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ResourceAsColor")
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.v("TAG", "3");
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.shai_item, null);
			viewHolder.layout = (RelativeLayout) convertView
					.findViewById(R.id.shaishai_item);
			viewHolder.shai_kemu = (TextView) convertView
					.findViewById(R.id.shai_kemu);
			viewHolder.shai_1 = (TextView) convertView
					.findViewById(R.id.shai_1);
			viewHolder.shai_2 = (TextView) convertView
					.findViewById(R.id.shai_2);
			viewHolder.shai_3 = (TextView) convertView
					.findViewById(R.id.shai_3);
			viewHolder.shai_4 = (TextView) convertView
					.findViewById(R.id.shai_4);
			viewHolder.shai_5 = (TextView) convertView
					.findViewById(R.id.shai_5);
			viewHolder.shai_tuijian = (TextView) convertView
					.findViewById(R.id.shai_tuijian);
			viewHolder.zan_num = (TextView) convertView
					.findViewById(R.id.zan_number);
			viewHolder.zan_icon = (ImageView) convertView
					.findViewById(R.id.zan_icon);
			viewHolder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.item_img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.shai_kemu.setText(list.get(position).subject_name);
		viewHolder.shai_1.setText(list.get(position).outline_name);
		viewHolder.shai_2.setText(list.get(position).user_realname);
		viewHolder.shai_3.setText(list.get(position).classroom_name);
		viewHolder.zan_num.setText(list.get(position).support+"");
		viewHolder.shai_tuijian.setText(list.get(position).from);
		if (position % 3 == 0) {
			viewHolder.layout
					.setBackgroundResource(R.drawable.top_corner_shuxue);
		} else if (position % 3 == 1) {
			viewHolder.layout
					.setBackgroundResource(R.drawable.top_corner_yuwen);
		} else if (position % 3 == 2) {
			viewHolder.layout
					.setBackgroundResource(R.drawable.top_corner_yingyu);
		}
		if (list.get(position).isSupport == 1) {
			viewHolder.zan_icon.setImageResource(R.mipmap.zan_icon1);
			viewHolder.zan_icon.setOnClickListener(null);
		} else if (list.get(position).isSupport == 0) {
			viewHolder.zan_icon.setImageResource(R.mipmap.zan_icon);
			viewHolder.zan_icon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Map<String, Object> map =new HashMap<String, Object>();
					map.put("detailId", list.get(position).id);
					map.put("userId", App.userInfo.id+"");
					context.doGet(Config1.getInstance().DIANZAN(), map,
							new ResultCallback() {
								@Override
								public void onResponse(Call call, Response response, String json) {
									Message message=Message.obtain();
									message.what=1;
									HandlerMessageObj handlerMessageObj=new HandlerMessageObj();
									handlerMessageObj.setImageView(viewHolder.zan_icon);handlerMessageObj.setTextView(viewHolder.zan_num);
									handlerMessageObj.setJson(json);
									message.obj=handlerMessageObj;
									handlerReq.sendMessage(message);
								}

								@Override
								public void onFailure(Call call, IOException exception) {

								}
							});
				}
			});
		}
		List<ImageArr> imageArrs = list.get(position).imageArr;
		viewHolder.linearLayout.removeAllViews();
		for (int i = 0; i < imageArrs.size(); i++) {
			ImageLoader imageLoader=ImageLoader.getInstance();
			ImageView imageView = new ImageView(context);
			imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			imageView.setScaleType(ScaleType.FIT_CENTER);
			imageLoader.displayImage(Config1.getInstance().IP+imageArrs.get(i).link, imageView);
			viewHolder.linearLayout.addView(imageView);
		}	
		return convertView;
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				HandlerMessageObj handlerMessageObj= (HandlerMessageObj) msg.obj;
				String json=handlerMessageObj.getJson();
				TextView textView=handlerMessageObj.getTextView();
				ImageView imageView=handlerMessageObj.getImageView();
				JSONObject result= JSON.parseObject(json);
				textView.setText(result.getString("data"));
				imageView.setOnClickListener(null);
				imageView.setImageResource(R.mipmap.zan_icon1);
			}else if (msg.what==2){

			}
		}
	};
	public static class ViewHolder {
		RelativeLayout layout;
		LinearLayout linearLayout;
		public TextView shai_kemu, shai_1, shai_2, shai_3, shai_4, shai_5,
				shai_tuijian, zan_num;
		public ImageView  zan_icon;
	}
}
