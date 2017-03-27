package com.shengliedu.parent.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.R;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.Teachbook;
import com.shengliedu.parent.util.Config1;

/**
 * 
 * 
 */
public class BookMenuAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<Teachbook> list;
	private Context context;
	private View[] views;
	private DisplayImageOptions options;
	public BookMenuAdapter(Context context, List<Teachbook> list) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
		views=new View[list.size()+1];
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.mipmap.no_fm)
		.showImageOnFail(R.mipmap.no_fm)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.build();
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

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if (views[position] == null) {
			viewHolder = new ViewHolder();
			views[position] = layoutInflater.inflate(R.layout.book_menu_item,
					null);
			viewHolder.book_menu_icon = (ImageView) views[position]
					.findViewById(R.id.book_menu_icon);
			viewHolder.book_menu_name = (TextView) views[position]
					.findViewById(R.id.book_menu_name);
			views[position].setTag(viewHolder);
//			viewHolder.update(); 
		} else {
			viewHolder = (ViewHolder) views[position].getTag();
		}
		StringBuilder bookInfo=new StringBuilder();
		if (list.get(position).schoolstage!=0) {
			bookInfo.append(App.getSchoolstageNameForId(list.get(position).schoolstage));
		}else {
			bookInfo.append("无学段信息");
		}
		if (list.get(position).subject!=0) {
			bookInfo.append(App.getSubjectNameForId(list.get(position).subject)+"\n");
		}else {
			bookInfo.append("无学科信息"+"\n");
		}
		if (!TextUtils.isEmpty(list.get(position).name)) {
			bookInfo.append(list.get(position).name.split("-")[1]+"册\n");
		}else {
			bookInfo.append("无年纪册别信息"+"\n");
		}
		if (list.get(position).editionVersion!=0) {
			bookInfo.append(App.getEditionVersionNameForId(list.get(position).editionVersion));
		}else {
			bookInfo.append("无版本信息");
		}
		viewHolder.book_menu_name.setText(bookInfo.toString());
		if (!TextUtils.isEmpty(list.get(position).cover)) {
			final ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Config1.MAIN_BOOK_IP+"/"+list.get(position).cover, viewHolder.book_menu_icon,options);
		}else {
			viewHolder.book_menu_icon.setImageResource(R.mipmap.no_fm);
		}
		return views[position];
	}

	private static class ViewHolder {
		TextView book_menu_name;
		ImageView book_menu_icon;
//		public void update() {  
//		book_menu_icon.getViewTreeObserver().addOnGlobalLayoutListener(  
//		        new OnGlobalLayoutListener() {  
//		            public void onGlobalLayout() {  
//		                int position = (Integer) book_menu_icon.getTag();  
//
//		// 这里是保证同一行的item高度是相同的！！也就是同一行是齐整的 height相等   
//		if (position > 0) {  
//		        View v = (View) book_menu_name.getTag();  
//		    int height = v.getHeight();  
//		    View view = gv.getChildAt(position - 1);  
//		    int lastheight = view.getHeight();  
//		// 得到同一行的最后一个item和前一个item想比较，把谁的height大，就把两者中                                                                // height小的item的高度设定为height较大的item的高度一致，也就是保证同一                                                                 // 行高度相等即可   
//		                        if (height > lastheight) {  
//		                            view.setLayoutParams(new GridView.LayoutParams(  
//		                                    GridView.LayoutParams.FILL_PARENT,  
//		                                    height));  
//		                        } else if (height < lastheight) {  
//		                            v.setLayoutParams(new GridView.LayoutParams(  
//		                                    GridView.LayoutParams.FILL_PARENT,  
//		                                    lastheight));  
//		                        }  
//		                    }  
//		                }  
//		            });  
//		}
	}
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
		views=new View[list.size()+1];
	}
}
