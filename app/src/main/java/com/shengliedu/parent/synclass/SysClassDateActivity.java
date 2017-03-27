package com.shengliedu.parent.synclass;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.ClassPlanDateAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.ClassPlanDate;
import com.shengliedu.parent.bean.ClassPlanDateOB;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SysClassDateActivity extends BaseActivity {
	private TextView begins_tva, begins_center, begins_tvb;
	private List<ClassPlanDate> dates = new ArrayList<ClassPlanDate>();
	private ClassPlanDateAdapter adapter;
	private ListView listView;
	private int studentId;
	private int semesterId;
	private ClassPlanDateOB classPlanDateOB;

	private ImageView begins_la,begins_rb;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		studentId = App.childInfo.id;
		semesterId = App.loginInfo.currentSemesterId;
		setBack();
		showTitle("上课日期选择");
		begins_la = (ImageView) findViewById(R.id.begins_la);
		begins_rb = (ImageView) findViewById(R.id.begins_rb);
		begins_tva = (TextView) findViewById(R.id.begins_tva);
		begins_center = (TextView) findViewById(R.id.begins_center);
		begins_tvb = (TextView) findViewById(R.id.begins_tvb);
		listView = (ListView) findViewById(R.id.begins_list);
		adapter = new ClassPlanDateAdapter(SysClassDateActivity.this, dates);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("date", dates.get(arg2).date);
				setResult(101, intent);
				finish();
			}
		});
		begins_tva.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (classPlanDateOB != null) {
					if (classPlanDateOB.pSemesterId == 0) {
						toast("已经第一个学期了");
					} else {
						semesterId = classPlanDateOB.pSemesterId;
						getDatas();
					}
				}
			}
		});
		begins_la.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (classPlanDateOB != null) {
					if (classPlanDateOB.pSemesterId == 0) {
						toast("已经第一个学期了");
					} else {
						semesterId = classPlanDateOB.pSemesterId;
						getDatas();
					}
				}
			}
		});
		begins_tvb.setOnClickListener(new OnClickListener() {
			@Override
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (classPlanDateOB != null) {
					if (classPlanDateOB.nSemesterId == 0) {
						toast("已经最后学期了");
					} else {
						semesterId = classPlanDateOB.nSemesterId;
						getDatas();
					}
				}
			}
		});
		begins_rb.setOnClickListener(new OnClickListener() {
			@Override
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (classPlanDateOB != null) {
					if (classPlanDateOB.nSemesterId == 0) {
						toast("已经最后学期了");
					} else {
						semesterId = classPlanDateOB.nSemesterId;
						getDatas();
					}
				}
			}
		});
		begins_center.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("date", App.loginInfo.date);
				setResult(101, intent);
				finish();
			}
		});
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				classPlanDateOB = JSON.parseObject(result.getString("data"),
						ClassPlanDateOB.class);
				if (classPlanDateOB != null) {
					showTitle(classPlanDateOB.showName);
					dates.clear();
					dates.addAll(classPlanDateOB.dates);
					adapter.notifyDataSetChanged();
				}
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", studentId);
		map.put("semesterId", semesterId);
		doGet(Config1.getInstance().SYNCLASSDATE(), map,
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
		return R.layout.activity_sysclass_date;
	}

}
