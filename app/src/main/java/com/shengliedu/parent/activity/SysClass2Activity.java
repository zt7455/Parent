package com.shengliedu.parent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.activity.bean.SynClass;
import com.shengliedu.parent.adapter.NewSynClassListAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.new_synclass.ClassCotentActivity;
import com.shengliedu.parent.new_synclass.NewSynClassDateActivity;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SysClass2Activity extends BaseActivity {
	private ListView synclass_list;
	private RelativeLayout lnshang;
	private TextView planbelow_time, planbelow_xingqi;
	private int studentid;
	private int classroom;
	private String date;

	private List<SynClass> list = new ArrayList<SynClass>();
	private NewSynClassListAdapter adapter;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		date = App.loginInfo.date;
		classroom = App.childInfo.classroomId;
		studentid = App.childInfo.id;
		setBack();
		showTitle("同步课堂");
		lnshang = (RelativeLayout) findViewById(R.id.lnshang);
		synclass_list = (ListView) findViewById(R.id.synclass_list);
		planbelow_time = (TextView) findViewById(R.id.planbelow_time);
		planbelow_xingqi = (TextView) findViewById(R.id.planbelow_xingqi);
		planbelow_time.setText(date);
		try {
			planbelow_xingqi.setText(dayForWeek(date));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter = new NewSynClassListAdapter(SysClass2Activity.this, list);
		synclass_list.setAdapter(adapter);
		synclass_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
					Intent iteIntent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putSerializable("synClass", list.get(arg2));
					iteIntent.putExtras(bundle);
				iteIntent.putExtra("activity",list.get(arg2).id);
					iteIntent.setClass(SysClass2Activity.this,
							ClassCotentActivity.class);
					startActivityForResult(iteIntent, 100);
			}
		});
		// 选择原有日期列表
		lnshang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent iteIntent = new Intent(SysClass2Activity.this,
						NewSynClassDateActivity.class);
				startActivityForResult(iteIntent, 100);
			}
		});

	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				List<SynClass> templist = JSON.parseArray((String) msg.obj, SynClass.class);
				list.clear();
				if (templist != null) {
					list.addAll(templist);
				}
				adapter.notifyDataSetChanged();
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		//withName=1
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("classroom", classroom);
		map.put("date", date);
		map.put("withName", 1);
		doGet(Config1.getInstance().ACTIVITY_LIST(), map, new ResultCallback() {
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
		return R.layout.activity_new_sysclass;
	}

	// 根据日期获取星期
	private String dayForWeek(String pTime) throws Exception {
		String[] week = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		String dayForWeek = "";
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			dayForWeek = week[0];
		} else {
			dayForWeek = week[(c.get(Calendar.DAY_OF_WEEK) - 1)];
		}
		return dayForWeek;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 100 && resultCode == 101) {
			String date = data.getStringExtra("date");
			if (!TextUtils.isEmpty(date) && !date.equals(this.date)) {
				planbelow_time.setText(date);
				this.date=date;
				getDatas();
			}
		} else if (requestCode == 100 && resultCode == 102) {
			getDatas();
		}
	}
}
