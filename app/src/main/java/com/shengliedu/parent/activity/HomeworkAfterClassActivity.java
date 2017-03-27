package com.shengliedu.parent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.HomeWorkAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.HasView;
import com.shengliedu.parent.bean.HomeWorkBean;
import com.shengliedu.parent.homework.HomeWorkDetailActivity;
import com.shengliedu.parent.homework.HomeworkDateActivity;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class HomeworkAfterClassActivity extends BaseActivity{
	private Button tv_yiwangtimu;
	private TextView yiyuedianji;
	private ExpandableListView expandableListView;
	private HomeWorkAdapter adapter;
	private List<HomeWorkBean> dataList = new ArrayList<HomeWorkBean>();

	private int studentId;
	private String date;

	private HasView hasView;
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				result.getIntValue("data");
				if (result.getIntValue("data") == 0) {
					yiyuedianji.setText("未阅本次作业");
				} else if (result.getIntValue("data") == 1) {
					yiyuedianji.setText("已阅本次作业");
					yiyuedianji.setOnClickListener(null);
				}
			}else if (msg.what==2){

			}else if (msg.what==3){
				JSONObject result=JSON.parseObject((String) msg.obj);
				hasView = JSON.parseObject(result.getString("data"),
						HasView.class);
				if (hasView != null) {
					if ("0".equals(hasView.hasView)) {
						yiyuedianji.setText("未阅本次作业");
					} else if ("1".equals(hasView.hasView)) {
						yiyuedianji.setText("已阅本次作业");
						yiyuedianji.setOnClickListener(null);
					}
					dataList.clear();
					dataList.addAll(hasView.homework);
					adapter.notifyDataSetChanged();
					if (dataList != null && dataList.size() > 0) {
						for (int i = 0; i < dataList.size(); i++) {
							expandableListView.expandGroup(i);
						}
					}
				}
			}else if (msg.what==4){

			}
		}
	};
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		studentId = App.childInfo.id;
		date = App.loginInfo.date;
		setBack();
		showTitle("作业");
		yiyuedianji = (TextView) findViewById(R.id.yiyuedianji);
		tv_yiwangtimu = (Button) findViewById(R.id.tv_yiwangtimu);
		yiyuedianji.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("studentId", studentId);
				map.put("date", date);
				doGet(Config1.getInstance().HOMEWORKYUE(), map, new ResultCallback() {
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
		});
		tv_yiwangtimu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				 TODO Auto-generated method stub
				 Intent iteIntent = new Intent();
				 iteIntent.setClass(HomeworkAfterClassActivity.this,
				 HomeworkDateActivity.class);
				 startActivityForResult(iteIntent, 200);
			}
		});
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		expandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int arg0) {
						// TODO Auto-generated method stub
						expandableListView.expandGroup(arg0, false);
					}
				});
		expandableListView.setChildDivider(getResources().getDrawable(R.mipmap.ic_split));
		adapter = new HomeWorkAdapter(this, dataList,date);
		expandableListView.setAdapter(adapter);
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
					int arg3, long arg4) {
				// TODO Auto-generated method stub
				// 当content_type == 2时，teaching.name为题干,teaching.questionType为题型，单选1，多选2，判断3，排序4，连线5
				// 当content_type == 3时，teaching.name为题干，submitType为提交的类型，1文本，2图片，3音频，4视频（对应题干后面的小图标）,5无需提交
				String content_type =dataList.get(arg2).homework
						.get(arg3).content_type;
				String content_id =dataList.get(arg2).homework
						.get(arg3).content_id;
				String hourId =dataList.get(arg2).homework
						.get(arg3).hourId;
				String coursewareContentId=dataList.get(arg2).homework
						.get(arg3).id;
				String questionType=dataList.get(arg2).homework
						.get(arg3).teaching.questionType;
				String submitType=dataList.get(arg2).homework
						.get(arg3).submitType;
				String questionContentType=dataList.get(arg2).homework
						.get(arg3).teaching.questionContentType;
				String file=dataList.get(arg2).homework
						.get(arg3).teaching.file;
				String link=dataList.get(arg2).homework
						.get(arg3).teaching.link;
				String content=dataList.get(arg2).homework
						.get(arg3).teaching.content;
				String part=dataList.get(arg2).part;
				Intent intent=new Intent(HomeworkAfterClassActivity.this,HomeWorkDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("coursewareContentId",coursewareContentId);
				bundle.putString("hourId",hourId);
				bundle.putString("content_id", content_id);
				bundle.putString("content_type", content_type);
				bundle.putString("questionType", questionType);
				bundle.putString("submitType", submitType);
				bundle.putString("questionContentType", questionContentType);
				bundle.putString("date", date);
				bundle.putString("file", file);
				bundle.putString("link", link);
				bundle.putString("part", part);
				bundle.putString("content", content);
				intent.putExtras(bundle);
				startActivityForResult(intent, 200);
				return false;
			}
		});
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", studentId);
		map.put("date", date);
		doGet(Config1.getInstance().HOMEWORKLIST(), map, new ResultCallback() {
			@Override
			public void onResponse(Call call, Response response, String json) {
				Message message=Message.obtain();
				message.what=3;
				message.obj=json;
				handlerReq.sendMessage(message);
			}

			@Override
			public void onFailure(Call call, IOException exception) {
				handlerReq.sendEmptyMessage(4);
			}
		});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_homework;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.v("TAG", "requestCode="+requestCode+"resultCode="+resultCode);
		if (requestCode==200 && resultCode==222) {
			date=data.getStringExtra("date");
			getDatas();
		}else if (requestCode==200 && resultCode==223) {
			getDatas();
		}else if (requestCode==200 && resultCode==213) {
			getDatas();
		}
	}
}
