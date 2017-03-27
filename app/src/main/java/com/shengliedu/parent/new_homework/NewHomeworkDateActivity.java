package com.shengliedu.parent.new_homework;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.NewHomeWorkDateAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.HomeWorkDate;
import com.shengliedu.parent.bean.Semester;
import com.shengliedu.parent.new_homework.bean.HomeworkDate;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class NewHomeworkDateActivity extends BaseActivity {
	private TextView after_tva, after_center, after_tvb;
	private ListView listView;
	private List<HomeWorkDate> dataList = new ArrayList<HomeWorkDate>();
	private NewHomeWorkDateAdapter adapter;

	private int studentId;
	private int semesterId;
	private String startdate;
	private String enddate;
	private List<Semester> semesters;
	private int semesterLocation;
	private List<HomeworkDate> homeworkDates=new ArrayList<>();
	private List<HomeworkDate> homeworkDatesList=new ArrayList<>();


	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		studentId = App.childInfo.id;
		semesterId = App.loginInfo.currentSemesterId;
		semesters=App.userInfo.semesterArr;
		startdate=App.loginInfo.date;
		enddate=App.loginInfo.date;
		showTitle("作业日期选择");
		if (semesters!=null){
			for (int i=0;i<semesters.size();i++){
				if (semesters.get(i).state==1){
					semesterId=semesters.get(i).id;
					startdate=semesters.get(i).startdate;
					enddate=semesters.get(i).enddate;
					semesterLocation=i;
					showTitle(semesters.get(i).name);
				}
			}
		}
		setBack();
		after_tva = (TextView) findViewById(R.id.after_tva);
		after_center = (TextView) findViewById(R.id.after_center);
		after_tvb = (TextView) findViewById(R.id.after_tvb);
		listView = (ListView) findViewById(R.id.after_list);
		adapter = new NewHomeWorkDateAdapter(this, homeworkDatesList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("date", homeworkDatesList.get(arg2).dateStr);
				setResult(222, intent);
				finish();
			}
		});
		after_tva.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (semesterLocation-1>=0 && semesters.get(semesterLocation-1)!=null){
					semesterLocation--;
					semesterId=semesters.get(semesterLocation).id;
					startdate=semesters.get(semesterLocation).startdate;
					enddate=semesters.get(semesterLocation).enddate;
					showTitle(semesters.get(semesterLocation).name);
					getDatas();
				}else {
					toast("已经第一个学期了");
				}
			}
		});
		after_tvb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (semesterLocation+1<=semesters.size()-1 && semesters.get(semesterLocation+1)!=null){
					semesterLocation++;
					semesterId=semesters.get(semesterLocation).id;
					startdate=semesters.get(semesterLocation).startdate;
					enddate=semesters.get(semesterLocation).enddate;
					showTitle(semesters.get(semesterLocation).name);
					getDatas();
				}else {
					toast("已经最后一个学期了");
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
		map.put("student", studentId);
		map.put("startDate", startdate);
		map.put("endDate", enddate);
		doGet(Config1.getInstance().NEWHOMEWORKDATE(), map, new ResultCallback() {
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
				List<HomeworkDate> homeworkDatesTemp = JSON.parseArray((String) msg.obj,
						HomeworkDate.class);
				homeworkDatesList.clear();
				if (homeworkDatesTemp != null && homeworkDatesTemp.size()>0) {
					homeworkDates.clear();
					for (HomeworkDate homeworkDate:homeworkDatesTemp){
						SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
						String str=simpleDateFormat.format(new Date(homeworkDate.date*1000));
						Log.v("date","date="+str);
						homeworkDate.dateStr=str;
						homeworkDates.add(homeworkDate);
					}
					for (int i=0;i<homeworkDates.size();i++){
						HomeworkDate homeworkDate=homeworkDates.get(i);
						if (homeworkDatesList.contains(homeworkDate)){
							 homeworkDatesList.get(homeworkDatesList.indexOf(homeworkDate)).nooversize+=homeworkDate.left;
						}else {
							homeworkDate.nooversize=homeworkDate.left;
							homeworkDatesList.add(homeworkDate);
						}
					}
				}
				adapter.notifyDataSetChanged();
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_new_homework_date;
	}

}
