package com.shengliedu.parent.more.score;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.ScoreAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.BeanExam;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.view.NoScrollListview;
import com.shengliedu.parent.view.ShiView;
import com.shengliedu.parent.view.ShiViews;
import com.shengliedu.parent.view.SyncHorizontalScrollView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class ChartActivity extends BaseActivity {
	private String studentId;
	private SyncHorizontalScrollView hsv;
	private LinearLayout chartlay;
	private LinearLayout lay;
	private SyncHorizontalScrollView chartscrollview;
	private ShiViews chartview;
	private Context context;

	private static ArrayList<Integer> temp = new ArrayList<Integer>();
	private static ArrayList<Integer> temps = new ArrayList<Integer>();
	private static ArrayList<String> time = new ArrayList<String>();
	private ArrayList<String> strs = new ArrayList<String>();
	private List<BeanExam> exams = new ArrayList<BeanExam>();

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		context=this;
		studentId = App.childInfo.id + "";
		setBack();
		showTitle("三图一表");
		initView();
		addLister();
	}

	private void initView() {
		chartscrollview = (SyncHorizontalScrollView) findViewById(R.id.chartscrollview);
		hsv = (SyncHorizontalScrollView) findViewById(R.id.scrollview);
		lay = (LinearLayout) findViewById(R.id.lay);
		chartlay = (LinearLayout) findViewById(R.id.chartlay);
	}

	private void addLister() {
		// TODO Auto-generated method stub
		hsv.setScrollView(chartscrollview);
		chartscrollview.setScrollView(hsv);
	}

	private void initDate() {
		if (time.size()>0) {
			ShiView ssvv = new ShiView(context, temp, temps, time, strs);
			LayoutParams lpp = new LayoutParams((time.size() + 1) * 200, 620);
			lpp.setMargins(60, 0, 0, 0);
			ssvv.setLayoutParams(lpp);
			lay.addView(ssvv);
			for (int i = 0; i < exams.size(); i++) {
				for (int j = 0; j < exams.get(i).examList.size(); j++) {
					NoScrollListview scrollListview = new NoScrollListview(context);
					android.view.ViewGroup.LayoutParams params = new LayoutParams(
							200, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					scrollListview.setLayoutParams(params);
					View v = LayoutInflater.from(this).inflate(
							R.layout.score_list_head, null);
					TextView kemu = (TextView) v.findViewById(R.id.nianji);
					final String semesterName = exams.get(i).semesterName;
					final String examName = exams.get(i).examList.get(j).examName;
					final int examId = exams.get(i).examList.get(j).examId;
					kemu.setText(semesterName + "\n" + examName);
					v.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							 Intent intent = new Intent(ChartActivity.this,
							 TwoImageActivity.class);
							 intent.putExtra("examId", examId);
							 intent.putExtra("examName", examName);
							 startActivity(intent);
						}
					});
					scrollListview.addHeaderView(v);
					ScoreAdapter adapter = new ScoreAdapter(context,
							exams.get(i).examList.get(j));
					scrollListview.setAdapter(adapter);
					chartlay.addView(scrollListview);
				}
			}
		}else {
			toast("暂无数据");
		}
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", studentId);
		doGet(Config1.getInstance().EXAMSCORE(), map, new ResultCallback() {
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
				List<BeanExam> exams1 = new ArrayList<BeanExam>();
				exams.clear();
				exams1 = JSON.parseArray(result.getString("data"),
						BeanExam.class);
				exams.addAll(exams1);
				chartview = (ShiViews) findViewById(R.id.chartview);

				for (int i = 0; i < exams.size(); i++) {
					for (int j = 0; j < exams.get(i).examList.size(); j++) {
					}
				}
				strs.clear();
				time.clear();
				temp.clear();
				temps.clear();
				for (int i = 0; i < exams.size(); i++) {
					if (exams.get(i).examList != null
							&& exams.get(i).examList.size() > 0) {

						for (int j = 0; j < 7; j++) {
							strs.add(""
									+ exams.get(i).examList.get(0).gradeStudentCount
									/ 7 * j);
						}
						for (int j = 0; j < exams.get(i).examList.size(); j++) {
							time.add(exams.get(i).semesterName + ""
									+ exams.get(i).examList.get(j).examName);
							temp.add(exams.get(i).examList.get(j).classroomRanking);
							temps.add(exams.get(i).examList.get(j).gradeRanking);
						}

					}
				}
				chartview.setLeftData(strs);
				initDate();
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_chart;
	}

}
