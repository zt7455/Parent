package com.shengliedu.parent.new_synclass;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;

import com.alibaba.fastjson.JSON;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.ImageAdapter;
import com.shengliedu.parent.bean.BeanTextBook;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


public class SynBookActivity extends BaseActivity {
	private int content_id;
	private int bookId;

	private Gallery gallery;
	public ImageAdapter imageAdapter;
	private List<BeanTextBook> dataBook=new ArrayList<BeanTextBook>();
	private List<String> urls = new ArrayList<String>(); //
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		bookId = getIntent().getIntExtra("bookId",0);
		content_id = getIntent().getIntExtra("content_id",0);
		setBack();
		gallery=getView(R.id.gallery);
		imageAdapter = new ImageAdapter(urls, this);
		gallery.setAdapter(imageAdapter);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				dataBook=JSON.parseArray((String) msg.obj, BeanTextBook.class);
				if (dataBook!=null) {
					urls.clear();
					for (int i = 0; i < dataBook.size(); i++) {
//						urls.add(dataBook.get(i).host+dataBook.get(i).link);
						urls.add(dataBook.get(i).link);
						imageAdapter.notifyDataSetChanged();
					}
				}
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("outlineId", content_id);
		map1.put("bookId", bookId);
		doGet(Config1.MAIN_BOOK_IP+ "/oe?", map1,
				new ResultCallback() {
					@Override
					public void onResponse(Call call, Response response, String json) {
						Message message=Message.obtain();
						message.what=1;
						message.obj=json;
						handlerReq.sendMessage(message);
					}

					@Override
					public void onFailure(Call call, IOException exception) {
						handlerReq.sendEmptyMessage(2);
					}
				});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_textbook;
	}

}
