package com.shengliedu.parent.new_synclass;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;

import com.alibaba.fastjson.JSON;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.BeanTextBook;
import com.shengliedu.parent.showdesign.ZiYuanFragment;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SynZiYuanActivity extends BaseActivity {
	private int content_id;
	private int bookId;

	private List<BeanTextBook> dataBook = new ArrayList<BeanTextBook>();
	private List<String> urls = new ArrayList<String>(); //
	private ViewPager textbook_pager;
	private List<Fragment> fragments;
	private ViewPagerAdapter adapter;
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				dataBook = JSON.parseArray((String) msg.obj,
						BeanTextBook.class);
				if (dataBook != null) {
					fragments = new ArrayList<Fragment>();
					for (int i = 0; i < dataBook.size(); i++) {
//						ZiYuanFragment fragment = new ZiYuanFragment(dataBook
//								.get(i).host + dataBook.get(i).link);
						ZiYuanFragment fragment = new ZiYuanFragment(dataBook.get(i).link);
						fragments.add(fragment);
					}
					adapter = new ViewPagerAdapter(getSupportFragmentManager());
					textbook_pager.setAdapter(adapter);
				}
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		bookId = getIntent().getIntExtra("bookId",0);
		content_id = getIntent().getIntExtra("content_id",0);
		setBack();
		textbook_pager = (ViewPager) findViewById(R.id.textbook_pager);
		textbook_pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				position = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

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
		return R.layout.activity_ziyuan;
	}

	int position;
	private ZiYuanFragment fragment;

	class ViewPagerAdapter extends FragmentPagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			fragment = (ZiYuanFragment) fragments.get(arg0);
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragments.size();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			fragment = (ZiYuanFragment) adapter.getItem(position);
			Log.v("TAG", "1");
			boolean b=fragment.onKeyDown(keyCode, event);
			if (b) {
				return b;
			}else {
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}