package com.shengliedu.parent.adapter;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.Teachbook;
import com.shengliedu.parent.util.HandlerMessageObj;
import com.shengliedu.parent.util.ZZKTHttpDownLoad;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BookPageAdapter extends BaseAdapter {
	private List<Teachbook> imageUrls;
	private int bookId;
	private Context context;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public BookPageAdapter(int bookId, List<Teachbook> imageUrls,
			Context context) {
		this.imageUrls = imageUrls;
		this.context = context;
		this.bookId = bookId;
		layoutInflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.mipmap.no_fm).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();
	}

	public int getCount() {
		return imageUrls.size();
	}

	public Object getItem(int position) {
		return imageUrls.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate

			(R.layout.textbook_page_item, null);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.imageView);
			viewHolder.page = (TextView) convertView.findViewById(R.id.page);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final String link = imageUrls.get(position).link;
		// final BitmapUtils butils=new BitmapUtils(context);
		File file0 = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/zzkt/teacher/book/"
				+ bookId
				+ "/");
		if (!file0.exists()){
			file0.mkdirs();
		}
		File file = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/zzkt/teacher/book/"
				+ bookId
				+ "/"
				+ link.substring(link.lastIndexOf("/") + 1));
		if (!file.exists()) {
			ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
			zzktHttpDownLoad.downloads(link, Environment.getExternalStorageDirectory()
							.getAbsolutePath()
							+ "/zzkt/teacher/book/"
							+ bookId
							+ "/"
							+ link.substring(link.lastIndexOf("/") + 1),
					new Callback() {
						@Override
						public void onFailure(Call call, IOException e) {
							handlerReq.sendEmptyMessage(2);
						}

						@Override
						public void onResponse(Call call, Response response) throws IOException {
							Message message=Message.obtain();
							message.what=1;
							HandlerMessageObj handlerMessageObj=new HandlerMessageObj();
							handlerMessageObj.setImageView(viewHolder.imageView);
							handlerMessageObj.setLink(Environment.getExternalStorageDirectory()
									.getAbsolutePath()
									+ "/zzkt/teacher/book/"
									+ bookId
									+ "/"
									+ link.substring(link.lastIndexOf("/") + 1));
							message.obj=handlerMessageObj;
							handlerReq.sendMessage(message);
						}
					});

		} else {
			// butils.display(viewHolder.imageView,arg0.result.getAbsolutePath());
			imageLoader.displayImage("file://" + file.getAbsolutePath(),
					viewHolder.imageView, options);
		}
		viewHolder.page.setText((position + 1) + "/" + imageUrls.size());
		// new CacheImageAsyncTask(viewHolder.imageView,
		// context).execute(imageUrls.get(position));
		return convertView;
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				HandlerMessageObj handlerMessageObj= (HandlerMessageObj) msg.obj;
				String link=handlerMessageObj.getLink();
				ImageView imageView=handlerMessageObj.getImageView();
				imageLoader.displayImage(
						"file:/" + link,
						imageView);
			}else if (msg.what==2){

			}

		}
	};
	class ViewHolder {
		ImageView imageView;
		TextView page;
	}
}
