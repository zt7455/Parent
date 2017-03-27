package com.shengliedu.parent.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.R;

import java.util.List;

public class CameraAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<String> list;
	Context context;

	public CameraAdapter(Context context, List<String> list) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
	}

	public int getCount() {
		// TODO Auto-generated method stub
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater
					.inflate(R.layout.camera_gridview, null);
			viewHolder.gridview_img = (ImageView) convertView
					.findViewById(R.id.gridview_img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if ((R.mipmap.camera_jiahao + "").equals(list.get(position))) {
			viewHolder.gridview_img.setImageResource(R.mipmap.camera_jiahao);
		} else {
//			BitmapUtils utils = new BitmapUtils(context);
//			utils.display(viewHolder.gridview_img, list.get(position));
			ImageLoader
			imageLoader=ImageLoader.getInstance();
			DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.build();
			if (list.get(position).contains("http")) {
				imageLoader.displayImage(list.get(position), viewHolder.gridview_img,options);
			}else {
				imageLoader.displayImage("file:/"+list.get(position), viewHolder.gridview_img,options);
			}
		}
		return convertView;
	}

	public static class ViewHolder {
		public ImageView gridview_img;

	}

}
