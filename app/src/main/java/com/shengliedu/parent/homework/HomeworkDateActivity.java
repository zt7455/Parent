package com.shengliedu.parent.homework;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.HomeWorkDateAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.HomeWorkDate;
import com.shengliedu.parent.bean.HomeWorkDateOB;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class HomeworkDateActivity extends BaseActivity {
	private TextView after_tva, after_center, after_tvb;
	private ListView listView;
	private List<HomeWorkDate> dataList = new ArrayList<HomeWorkDate>();
	private HomeWorkDateAdapter adapter;

	private int studentId;
	private int semesterId;
	private HomeWorkDateOB homeWorkDateOB;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		studentId = App.childInfo.id;
		semesterId = App.loginInfo.currentSemesterId;
		setBack();
		showTitle("课后作业日期选择");
		after_tva = (TextView) findViewById(R.id.after_tva);
		after_center = (TextView) findViewById(R.id.after_center);
		after_tvb = (TextView) findViewById(R.id.after_tvb);
		listView = (ListView) findViewById(R.id.after_list);
		adapter = new HomeWorkDateAdapter(this, dataList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("date", dataList.get(arg2).date);
				setResult(222, intent);
				finish();
			}
		});
		after_tva.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (homeWorkDateOB != null) {
					if (homeWorkDateOB.pSemesterId == 0) {
						toast("已经第一个学期了");
					} else {
						semesterId = homeWorkDateOB.pSemesterId;
						getDatas();
					}
				}
			}
		});
		after_tvb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (homeWorkDateOB != null) {
					if (homeWorkDateOB.nSemesterId == 0) {
						toast("已经最后一个学期了");
					} else {
						semesterId = homeWorkDateOB.nSemesterId;
						getDatas();
					}
				}
			}
		});
		after_center.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("date", App.loginInfo.date);
				setResult(222, intent);
				finish();
			}
		});
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", studentId);
		map.put("semesterId", semesterId);
		doGet(Config1.getInstance().HOMEWORKDATE(), map, new ResultCallback() {
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
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				homeWorkDateOB = JSON.parseObject(result.getString("data"),
						HomeWorkDateOB.class);
				if (homeWorkDateOB != null) {
					showTitle(homeWorkDateOB.cSemesterName);
					dataList.clear();
					dataList.addAll(homeWorkDateOB.dateList);
					adapter.notifyDataSetChanged();
				}
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_homeworkdate;
	}

}
