package com.shengliedu.parent.book;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.TeachResourceSelectGradeAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.ClassRoom;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class TeachResourceActivity extends BaseActivity {
	private ListView resource_left_List;
	public static List<ClassRoom> grade_Subject_editionVersion = new ArrayList<ClassRoom>();
	public static List<ClassRoom> Subject_editionVersion = new ArrayList<ClassRoom>();
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private TeachResourceSelectGradeAdapter adapter;
	private int gradeId, subjectId,editionVersion;
	private AlertDialog dialog;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		showTitle("教学资源");
		resource_left_List=getView(R.id.resource_left_List);
		adapter=new TeachResourceSelectGradeAdapter(this, Subject_editionVersion, map);
		resource_left_List.setAdapter(adapter);
		resource_left_List.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (!App.listIsEmpty(Subject_editionVersion)) {
					map.clear();
					map.put(arg2, true);
					adapter.notifyDataSetChanged();
					subjectId=Subject_editionVersion.get(arg2).id;
					editionVersion=Subject_editionVersion.get(arg2).editionVersion;
					switchFragment();
				}
			}
		});
	}
	private int which;
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				grade_Subject_editionVersion= JSON.parseArray((String) msg.obj,ClassRoom.class);
				if (!App.listIsEmpty(grade_Subject_editionVersion)) {
					int location=0;
					gradeId=grade_Subject_editionVersion.get(location).id;
					for (int i = 0; i < grade_Subject_editionVersion.size(); i++) {
						if (App.childInfo.gradeId!=0&&App.childInfo.gradeId==grade_Subject_editionVersion.get(i).id){
							gradeId=App.childInfo.gradeId;
							location=i;
						}
					}

//					if (!App.listIsEmpty(App.classroomDic)) {
//						for (int i = 0; i < App.classroomDic.size(); i++) {
//							if (App.childInfo.classroomId==App.classroomDic.get(i).id) {
//								gradeId=App.classroomDic.get(i).grade;
//							}
//						}
//					}
					setRightText(App.getGradeNameForId(gradeId),new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Builder builder3 = new Builder(TeachResourceActivity.this);
							builder3.setTitle("班级");
							// 给对话框设定单选列表项
							int size=grade_Subject_editionVersion.size();
							String[] grades=new String[size];
							for (int i = 0; i < grade_Subject_editionVersion.size(); i++) {
								grades[i]=App.getGradeNameForId(grade_Subject_editionVersion.get(i).id);
								if (App.childInfo.gradeId!=0&&App.childInfo.gradeId==grade_Subject_editionVersion.get(i).id){
									which=i;
								}
							}
							builder3.setSingleChoiceItems(grades,
									which, new AlertDialog.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											TeachResourceActivity.this.which=which;
											gradeId=grade_Subject_editionVersion.get(which).id;
											setRightText(App.getGradeNameForId(gradeId));
											Subject_editionVersion.clear();
											map.clear();
											if (!App.listIsEmpty(grade_Subject_editionVersion.get(which).subjects)) {
												Subject_editionVersion.addAll(grade_Subject_editionVersion.get(which).subjects);
												subjectId=Subject_editionVersion.get(0).id;
												editionVersion=Subject_editionVersion.get(0).editionVersion;
												map.put(0, true);
												adapter.notifyDataSetChanged();
											}else {
												adapter.notifyDataSetChanged();
											}
											switchFragment();
											dialog.dismiss();
										}
									});
							dialog = builder3.show();
							dialog.show();
						}
					});
					Subject_editionVersion.clear();
					Subject_editionVersion.addAll(grade_Subject_editionVersion.get(location).subjects);
					if (!App.listIsEmpty(Subject_editionVersion)) {
						subjectId=Subject_editionVersion.get(0).id;
						editionVersion=Subject_editionVersion.get(0).editionVersion;
						map.clear();
						map.put(0, true);
						adapter.notifyDataSetChanged();
					}

					switchFragment();
				}
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		// editionVersionId=82&gradeId=1&limit=30&offset=0&subjectId=3&typeId=1
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("setUpGradeSubject", 1);
		map.put("schoolstage", App.beanSchoolInfo.schoolstage);
		map.put("region", App.beanSchoolInfo.region);
		doGet(Config1.getInstance().MAIN_BOOK_IP+"/textbook?", map,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(2);
					}

					@Override
					public void onResponse(Call call, Response response, String json)  {
						Message message=Message.obtain();
						message.what=1;
						message.obj=json;
						handlerReq.sendMessage(message);
					}
				});
		switchFragment();
	}

	private void switchFragment() {
		// TODO Auto-generated method stub
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.resource_fragment_content,
						new TeachBookFragment(this, gradeId, subjectId,editionVersion))
				.commit();
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_teach_resource;
	}

}
