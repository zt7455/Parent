package com.shengliedu.parent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.activity.bean.Homework;
import com.shengliedu.parent.activity.bean.HomeworkTree;
import com.shengliedu.parent.adapter.NewHomeWorkAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.new_homework.NewHomeWorkDetailActivity;
import com.shengliedu.parent.new_homework.NewHomeworkDateActivity;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class HomeworkAfterClass2Activity extends BaseActivity{
    private String date;
    private int classroom;
	private int student;
	private RelativeLayout lnshang;
	private TextView planbelow_time, planbelow_xingqi;
	private List<Homework> homeworks=new ArrayList<>();
	private List<HomeworkTree> homeworkTrees=new ArrayList<>();
	private NewHomeWorkAdapter adapter;
	private ExpandableListView expandableListView;
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				homeworkTrees.clear();
				homeworks= JSON.parseArray((String) msg.obj,Homework.class);
				if (homeworks!=null&&homeworks.size()>0){
					String a="";
					for (int i=0;i<homeworks.size();i++){
						if (i==homeworks.size()-1){
							a+=homeworks.get(i).id;
						}else {
							a+=homeworks.get(i).id+",";
						}
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("content", a);
					map.put("student", student);
					map.put("type", "8,9");
					doGet(Config1.getInstance().HOMEWORK_ASWER(), map, new ResultCallback() {
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
				}else {
					adapter.notifyDataSetChanged();
				}
			}else if (msg.what==2){

			}else if (msg.what==3){
				List<Homework> homeworksTemp= JSON.parseArray((String) msg.obj,Homework.class);
				if (homeworksTemp!=null&&homeworksTemp.size()>0){
					for (int i=0;i<homeworksTemp.size();i++){
						for (int j=0;j<homeworks.size();j++){
							if (homeworksTemp.get(i).content==homeworks.get(j).id){
								homeworks.get(j).content=homeworksTemp.get(i).id;
								homeworks.get(j).answer=homeworksTemp.get(i).answer;
							}
						}
					}
				}
				List<Integer> subject=new ArrayList<>();
				for (int i=0;i<homeworks.size();i++){
					if (!subject.contains(homeworks.get(i).subject)){
                        subject.add(homeworks.get(i).subject);
					}
				}
				for (int j=0;j<subject.size();j++){
					for (int i=0;i<homeworks.size();i++){
						if (homeworks.get(i).subject==subject.get(j)){
							HomeworkTree homeworkTree=new HomeworkTree();
							homeworkTree.part=homeworks.get(i).part;
							homeworkTree.subject=homeworks.get(i).subject;
							homeworkTree.subjectpart=App.getSubjectNameForId(homeworks.get(i).subject)+"-"+(homeworks.get(i).part==1?"课前":"课后")+"作业";
							homeworkTree.homeworks=new ArrayList<>();
							if (homeworkTrees.contains(homeworkTree)){

							}else {
								homeworkTrees.add(homeworkTree);
							}
						}
					}
				}
				for (int i=0;i<homeworkTrees.size();i++){
					for (int j=0;j<homeworks.size();j++){
						if (homeworkTrees.get(i).part==homeworks.get(j).part&&homeworkTrees.get(i).subject==homeworks.get(j).subject){
							homeworkTrees.get(i).homeworks.add(homeworks.get(j));
						}
					}
				}
				homeworks.clear();
				adapter.notifyDataSetChanged();
				if (homeworkTrees != null && homeworkTrees.size() > 0) {
					for (int i = 0; i < homeworkTrees.size(); i++) {
						expandableListView.expandGroup(i);
					}
				}
			}else if (msg.what==4){

			}
		}
	};
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		date=App.loginInfo.date;
		classroom=App.childInfo.classroomId;
		student=App.childInfo.id;
		setBack();
		showTitle("作业");
		lnshang = (RelativeLayout) findViewById(R.id.lnshang);
		planbelow_time = (TextView) findViewById(R.id.planbelow_time);
		planbelow_xingqi = (TextView) findViewById(R.id.planbelow_xingqi);
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		planbelow_time.setText(date);
		try {
			planbelow_xingqi.setText(dayForWeek(date));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter=new NewHomeWorkAdapter(this,homeworkTrees);
		expandableListView.setAdapter(adapter);
		// 选择原有日期列表
		lnshang.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent iteIntent = new Intent(HomeworkAfterClass2Activity.this,
						NewHomeworkDateActivity.class);
				startActivityForResult(iteIntent, 200);
			}
		});
		expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
										int arg3, long arg4) {
				// TODO Auto-generated method stub

				Intent intent=new Intent(HomeworkAfterClass2Activity.this,NewHomeWorkDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("homeworklist", (Serializable) homeworkTrees.get(arg2).homeworks);
				bundle.putInt("location",arg3);
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
//		getDateHomework=1,date=2017-02-20,classroom=1或1,2
		map.put("classroom", classroom);
		map.put("date", date);
		map.put("getDateHomework", 1);
		doGet(Config1.getInstance().HOMEWORK_CONTENT(), map, new ResultCallback() {
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
		return R.layout.activity_new_homework;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.v("TAG", "requestCode="+requestCode+"resultCode="+resultCode);
		if (requestCode==200 && resultCode==222) {
			String date=data.getStringExtra("date");
			if (date!=null&&!date.equals(this.date)){
				this.date=date;
				getDatas();
				planbelow_time.setText(date);
				try {
					planbelow_xingqi.setText(dayForWeek(date));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if (requestCode==200 && resultCode==223) {
			getDatas();
		}else if (requestCode==200 && resultCode==213) {
			getDatas();
		}
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
}
