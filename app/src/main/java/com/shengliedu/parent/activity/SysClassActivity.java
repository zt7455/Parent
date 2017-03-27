package com.shengliedu.parent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.SynClassListAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.SynClass;
import com.shengliedu.parent.synclass.ClassPlanActivity;
import com.shengliedu.parent.synclass.SysClassDateActivity;
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

public class SysClassActivity extends BaseActivity {
	private ListView synclass_list;
	private RelativeLayout lnshang;
	private TextView planbelow_time, planbelow_xingqi;
	private int studentid;
	private String date;

	private List<SynClass> list = new ArrayList<SynClass>();
	private SynClassListAdapter adapter;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		date = App.loginInfo.date;
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
		adapter = new SynClassListAdapter(SysClassActivity.this, list);
		synclass_list.setAdapter(adapter);
		synclass_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (0 == list.get(arg2).hourId) {
					toast("无授课内容");
				} else {
					Intent iteIntent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putSerializable("synClass", list.get(arg2));
					iteIntent.putExtras(bundle);
					iteIntent.setClass(SysClassActivity.this,
							ClassPlanActivity.class);
					startActivityForResult(iteIntent, 100);
				}

			}
		});
		// 选择原有日期列表
		lnshang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent iteIntent = new Intent(SysClassActivity.this,
						SysClassDateActivity.class);
				startActivityForResult(iteIntent, 100);
			}
		});

	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				List<SynClass> templist = JSON.parseArray(
						result.getString("data"), SynClass.class);
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
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", studentid);
		map.put("date", date);
		doGet(Config1.getInstance().SYNCLASS(), map, new ResultCallback() {
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
		return R.layout.activity_sysclass;
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
			date = data.getStringExtra("date");
			planbelow_time.setText(date);
			getDatas();
		} else if (requestCode == 100 && resultCode == 102) {
			getDatas();
		}
	}
}
