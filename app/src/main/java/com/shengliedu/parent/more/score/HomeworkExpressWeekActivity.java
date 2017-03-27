package com.shengliedu.parent.more.score;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.BeanExpress;
import com.shengliedu.parent.bean.Chengji;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class HomeworkExpressWeekActivity extends BaseActivity {

	private TextView after_tva, after_center, after_tvb;
	private int week = 0;
	private List<Chengji> subjectArr = new ArrayList<Chengji>();
	private List<Chengji> weekArr = new ArrayList<Chengji>();

	private String subjectId;
	private String studentId;
	private String semesterId;
	private String date;

	private String swDate;
	private String ewDate;
	private RadioGroup mGroup;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		studentId = App.childInfo.id + "";
		semesterId = App.loginInfo.currentSemesterId + "";
		date = App.loginInfo.date;

		setBack();
		showTitle("作业表现");
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				BeanExpress express = JSON.parseObject(
						result.getString("data"), BeanExpress.class);
				if (express != null) {
					weekArr.clear();
					weekArr.addAll(express.weekArr);
					subjectArr.clear();
					subjectArr.addAll(express.subjectArr);
					initView();
				} else {
					toast("暂无数据");
				}
				setRightImage(R.mipmap.yuetongji, new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(HomeworkExpressWeekActivity.this,HomeworkExpressMonthActivity.class);
						startActivity(intent);
					}
				});
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		// semesterId=3&date=2014-09-09&studentId=115
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("studentId", studentId);
		map1.put("semesterId", semesterId);
		map1.put("date", date);
		doGet(Config1.getInstance().WEEK_SUBJECT(), map1, new ResultCallback() {
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

	private void initView() {
		for (int i = 0; i < weekArr.size(); i++) {
			if (weekArr.get(i).isCurrent == 1) {
				week = i;
				swDate = weekArr.get(i).sDate;
				ewDate = weekArr.get(i).eDate;
			}
		}
		mGroup = (RadioGroup) findViewById(R.id.rg);
		if (subjectArr != null && subjectArr.size() > 0) {
			for (int i = 0; i < subjectArr.size(); i++) {
				RadioButton button = (RadioButton) LayoutInflater.from(this)
						.inflate(R.layout.radio_without_button, null);
				button.setText(subjectArr.get(i).name);
				// button.setTag(i);
				button.setId(subjectArr.get(i).id);
				// button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 20, 0);
				mGroup.addView(button);
			}
			mGroup.check(subjectArr.get(0).id);
			mGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup arg0, int arg1) {
					// TODO Auto-generated method stub
					subjectId = arg1 + "";
					getSupportFragmentManager()
							.beginTransaction()
							.replace(
									R.id.ak_content,
									new WebFragment(
											HomeworkExpressWeekActivity.this,
											subjectId, studentId, swDate,
											ewDate, 3)).commit();
				}
			});
		} else {
			toast("暂无数据");
			return;
		}
		subjectId = subjectArr.get(0).id + "";
		getSupportFragmentManager()
				.beginTransaction()
				.replace(
						R.id.ak_content,
						new WebFragment(HomeworkExpressWeekActivity.this,
								subjectId, studentId, swDate, ewDate, 3))
				.commit();
		after_tva = (TextView) findViewById(R.id.after_tva);
		after_center = (TextView) findViewById(R.id.after_center);
		after_tvb = (TextView) findViewById(R.id.after_tvb);
		after_tva.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for (int j = 0; j < weekArr.size(); j++) {
					if (week == j) {
						if (week != 0) {
							swDate = weekArr.get(j - 1).sDate;
							ewDate = weekArr.get(j - 1).eDate;
							week--;
							if (subjectArr != null && subjectArr.size() > 0) {
								subjectId = subjectArr.get(0).id + "";
								mGroup.check(subjectArr.get(0).id);
							} else {
								subjectId = "0";
							}

							getSupportFragmentManager()
									.beginTransaction()
									.replace(
											R.id.ak_content,
											new WebFragment(
													HomeworkExpressWeekActivity.this,
													subjectId, studentId,
													swDate, ewDate, 3))
									.commit();
							break;
						}else {
							toast("已经是第一个周了");
						}
					} 
				}
			}
		});
		after_center.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for (int j = 0; j < weekArr.size(); j++) {
					if (weekArr.get(j).isCurrent == 1) {
						swDate = weekArr.get(j).sDate;
						ewDate = weekArr.get(j).eDate;
						semesterId = weekArr.get(j).id + "";
						week = j;
						if (subjectArr != null && subjectArr.size() > 0) {
							subjectId = subjectArr.get(0).id + "";
							mGroup.check(subjectArr.get(0).id);
						} else {
							subjectId = "0";
						}
						getSupportFragmentManager()
								.beginTransaction()
								.replace(
										R.id.ak_content,
										new WebFragment(
												HomeworkExpressWeekActivity.this,
												subjectId, studentId, swDate,
												ewDate, 3)).commit();
						break;
					}
				}
			}
		});
		after_tvb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v("TAG", "b=" + week);
				for (int j = 0; j < weekArr.size(); j++) {
					if (week == j) {
						if (week != weekArr.size() - 1) {
							swDate = weekArr.get(j + 1).sDate;
							ewDate = weekArr.get(j + 1).eDate;
							week++;
							if (subjectArr != null && subjectArr.size() > 0) {
								subjectId = subjectArr.get(0).id + "";
								mGroup.check(subjectArr.get(0).id);
							} else {
								subjectId = "0";
							}
							getSupportFragmentManager()
							.beginTransaction()
							.replace(
									R.id.ak_content,
									new WebFragment(
											HomeworkExpressWeekActivity.this,
											subjectId, studentId, swDate,
											ewDate, 3)).commit();
							break;
						} else {
							toast("最后一周了");
						}
					}
				}
			}
		});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_classexpress_week;
	}
}
