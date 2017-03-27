package com.shengliedu.parent.more.score;

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
import com.shengliedu.parent.bean.Chengji;
import com.shengliedu.parent.bean.Semesters;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class HomeworkExpressMonthActivity extends BaseActivity {
	private TextView after_tva, after_center, after_tvb;
	private int xueqi = 0;
	private List<Chengji> subject = new ArrayList<Chengji>();
	private List<Semesters> semester = new ArrayList<Semesters>();

	private String subjectId;
	private String studentId;
	private String semesterId;

	private RadioGroup mGroup;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		studentId = App.childInfo.id + "";
		setBack();
		showTitle("作业表现");
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				semester = JSON.parseArray(result.getString("data"),
						Semesters.class);
				Log.v("TAG", "1");
				if (semester != null && semester.size() > 0) {
					Log.v("TAG", "2");
					initView();
				} else {
					toast("暂无数据");
				}
				setRightImage(R.mipmap.zhoubaobiao, new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finishActivityByAniamtion();
					}
				});
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("studentId", studentId);
		doGet(Config1.getInstance().SEMESTER_SUBJECT(), map1, new ResultCallback() {
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
		for (int i = 0; i < semester.size(); i++) {
			if (semester.get(i).state == 1) {
				xueqi = i;
				semesterId = semester.get(i).id + "";
				subject = semester.get(xueqi).subjectArr;
				Log.v("TAG", "3");
			}
		}
		mGroup = (RadioGroup) findViewById(R.id.rg);
		if (subject != null && subject.size() > 0) {
			for (int i = 0; i < subject.size(); i++) {
				RadioButton button = (RadioButton) LayoutInflater.from(this)
						.inflate(R.layout.radio_without_button, null);
				button.setText(subject.get(i).name);
				// button.setTag(i);
				button.setId(subject.get(i).id);
				// button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 20, 0);
				mGroup.addView(button);
			}
			mGroup.check(subject.get(0).id);
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
											HomeworkExpressMonthActivity.this,
											subjectId, studentId, semesterId, 3))
							.commit();
				}
			});
		} else {
			toast("暂无数据");
			return;
		}
		subjectId = subject.get(0).id + "";
		getSupportFragmentManager()
				.beginTransaction()
				.replace(
						R.id.ak_content,
						new WebFragment(HomeworkExpressMonthActivity.this,
								subjectId, studentId, semesterId, 3)).commit();
		after_tva = (TextView) findViewById(R.id.after_tva);
		after_center = (TextView) findViewById(R.id.after_center);
		after_tvb = (TextView) findViewById(R.id.after_tvb);
		after_tva.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for (int j = 0; j < semester.size(); j++) {
					if (xueqi == j) {
						if (xueqi != 0) {
							semesterId = semester.get(j - 1).id + "";
							subject = semester.get(j - 1).subjectArr;
							xueqi--;
							Log.v("TAG", "a2=" + xueqi);
							mGroup.clearCheck();
							mGroup.removeAllViews();
							for (int i = 0; i < subject.size(); i++) {
								RadioButton button = (RadioButton) LayoutInflater
										.from(HomeworkExpressMonthActivity.this)
										.inflate(R.layout.radio_without_button,
												null);
								button.setText(subject.get(i).name);
								// button.setTag(i);
								button.setId(subject.get(i).id);
								// button.setCompoundDrawablesWithIntrinsicBounds(0,
								// 0,
								// 20, 0);
								mGroup.addView(button);
							}
							if (subject != null && subject.size() > 0) {
								subjectId = subject.get(0).id + "";
								mGroup.check(subject.get(0).id);
							} else {
								subjectId = "0";
							}
							// mGroup.setOnCheckedChangeListener(new
							// OnCheckedChangeListener() {
							//
							// @Override
							// public void onCheckedChanged(RadioGroup arg0,
							// int arg1) {
							// // TODO Auto-generated method stub
							// subjectId = arg1 + "";
							// getSupportFragmentManager()
							// .beginTransaction()
							// .replace(
							// R.id.ak_content,
							// new WebFragment(
							// HomeworkExpressMonthActivity.this,
							// subjectId,
							// studentId,
							// semesterId, 3))
							// .commit();
							// }
							// });

							getSupportFragmentManager()
									.beginTransaction()
									.replace(
											R.id.ak_content,
											new WebFragment(
													HomeworkExpressMonthActivity.this,
													subjectId, studentId,
													semesterId, 3)).commit();
							break;

						} else {
							toast("已经是第一个学期了");
						}
					}
				}
			}
		});
		after_center.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for (int j = 0; j < semester.size(); j++) {
					if (semester.get(j).state == 1) {
						if (xueqi != j) {

							semesterId = semester.get(j).id + "";
							subject = semester.get(j).subjectArr;
							xueqi = j;
							mGroup.clearCheck();
							mGroup.removeAllViews();
							for (int i = 0; i < subject.size(); i++) {
								RadioButton button = (RadioButton) LayoutInflater
										.from(HomeworkExpressMonthActivity.this)
										.inflate(R.layout.radio_without_button,
												null);
								button.setText(subject.get(i).name);
								// button.setTag(i);
								button.setId(subject.get(i).id);
								// button.setCompoundDrawablesWithIntrinsicBounds(0,
								// 0,
								// 20, 0);
								mGroup.addView(button);
							}
							if (subject != null && subject.size() > 0) {
								subjectId = subject.get(0).id + "";
								mGroup.check(subject.get(0).id);
							} else {
								subjectId = "0";
							}
							// mGroup.setOnCheckedChangeListener(new
							// OnCheckedChangeListener() {
							//
							// @Override
							// public void onCheckedChanged(RadioGroup arg0,
							// int arg1) {
							// // TODO Auto-generated method stub
							// subjectId = arg1 + "";
							// getSupportFragmentManager()
							// .beginTransaction()
							// .replace(
							// R.id.ak_content,
							// new WebFragment(
							// HomeworkExpressMonthActivity.this,
							// subjectId, studentId,
							// semesterId, 3))
							// .commit();
							// }
							// });
							if (subject != null && subject.size() > 0) {
								subjectId = subject.get(0).id + "";
							} else {
								subjectId = "0";
							}
							getSupportFragmentManager()
									.beginTransaction()
									.replace(
											R.id.ak_content,
											new WebFragment(
													HomeworkExpressMonthActivity.this,
													subjectId, studentId,
													semesterId, 3)).commit();
							break;
						}
					}
				}
			}
		});
		after_tvb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v("TAG", "b=" + xueqi);
				for (int j = 0; j < semester.size(); j++) {
					if (xueqi == j) {
						if (xueqi != semester.size() - 1) {
							semesterId = semester.get(j + 1).id + "";
							subject = semester.get(j + 1).subjectArr;
							xueqi++;
							Log.v("TAG", "b2=" + xueqi);
							mGroup.clearCheck();
							mGroup.removeAllViews();
							for (int i = 0; i < subject.size(); i++) {
								RadioButton button = (RadioButton) LayoutInflater
										.from(HomeworkExpressMonthActivity.this)
										.inflate(R.layout.radio_without_button,
												null);
								button.setText(subject.get(i).name);
								// button.setTag(i);
								button.setId(subject.get(i).id);
								// button.setCompoundDrawablesWithIntrinsicBounds(0,
								// 0,
								// 20, 0);
								mGroup.addView(button);
							}
							if (subject != null && subject.size() > 0) {
								subjectId = subject.get(0).id + "";
								mGroup.check(subject.get(0).id);
							} else {
								subjectId = "0";
							}
							// mGroup.setOnCheckedChangeListener(new
							// OnCheckedChangeListener() {
							//
							// @Override
							// public void onCheckedChanged(RadioGroup arg0,
							// int arg1) {
							// // TODO Auto-generated method stub
							// Log.v("TAG", "arg1=" + arg1);
							// subjectId = arg1 + "";
							// getSupportFragmentManager()
							// .beginTransaction()
							// .replace(
							// R.id.ak_content,
							// new WebFragment(
							// HomeworkExpressMonthActivity.this,
							// subjectId,
							// studentId,
							// semesterId, 3))
							// .commit();
							// }
							// });
							if (subject != null && subject.size() > 0) {
								subjectId = subject.get(0).id + "";
							} else {
								subjectId = "0";
							}
							getSupportFragmentManager()
									.beginTransaction()
									.replace(
											R.id.ak_content,
											new WebFragment(
													HomeworkExpressMonthActivity.this,
													subjectId, studentId,
													semesterId, 3)).commit();
							break;
						} else {
							toast("最后一学期了");
						}
					}
				}
			}
		});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_everyscore;
	}
}
