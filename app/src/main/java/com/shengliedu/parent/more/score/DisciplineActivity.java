package com.shengliedu.parent.more.score;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class DisciplineActivity extends BaseActivity {

	private String studentId;
	public String semesterId = "";
	private String smDate;
	private String emDate;
	private String swDate;
	private String ewDate;
	private String dayDate;
	private TextView after_tva, after_center, after_tvb;
	private int w;
	private int m;
	private int d;
	private List<Chengji> week =new ArrayList<Chengji>();
	private List<Chengji> month =new ArrayList<Chengji>();
	private int page=1;

	private RadioGroup rg;
	private RadioButton rbMonth;
	private RadioButton rbDay;
	private RadioButton rbWeek;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		studentId = App.childInfo.id + "";
		dayDate = App.loginInfo.date;
		semesterId= App.loginInfo.currentSemesterId+"";
		setBack();
		showTitle("在校纪律情况");
	}

	private void initView() {
		if (week != null && week.size() != 0) {
			for (int i = 0; i < week.size(); i++) {
				if (week.get(i).isCurrent == 1) {
					swDate = week.get(i).sDate;
					ewDate = week.get(i).eDate;
					w = i;
					break;
				}
			}
		}
		if (month != null && month.size() != 0) {
			for (int i = 0; i < month.size(); i++) {
				if (month.get(i).isCurrent == 1) {
					// if (i != (MainActivity.children.getSemester().size() -
					// 1)) {
					smDate = month.get(i).sDate;
					emDate = month.get(i).eDate;
					m = i;
					break;
				}
			}
		}
		after_tva = (TextView) findViewById(R.id.after_tva);
		after_center = (TextView) findViewById(R.id.after_center);
		after_tvb = (TextView) findViewById(R.id.after_tvb);
		rg = (RadioGroup) findViewById(R.id.rg);
		rbMonth = (RadioButton) findViewById(R.id.month);
		rbDay = (RadioButton) findViewById(R.id.day);
		rbWeek = (RadioButton) findViewById(R.id.week);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.month:
					if (page==0) {
					}else {
						page = 0;
						after_tva.setText("上个月");
						after_tvb.setText("下个月");
						after_center.setText("返回本月");
						getSupportFragmentManager()
						.beginTransaction()
						.replace(
								R.id.ak_content,
								new WebFragment(
										DisciplineActivity.this, 1,studentId, smDate,
										emDate)).commit();
					}
					break;
				case R.id.day:
					if (page==1) {
					}else {
						page = 1;
						after_tva.setText("上一天");
						after_tvb.setText("下一天");
						after_center.setText("返回今天");
						getSupportFragmentManager()
						.beginTransaction()
						.replace(
								R.id.ak_content,
								new WebFragment(
										DisciplineActivity.this, 1,studentId, dayDate,
										dayDate)).commit();
					}
					break;
				case R.id.week:
					if (page==2) {
					}else {
						after_tva.setText("上一周");
						after_tvb.setText("下一周");
						after_center.setText("返回本周");
						page = 2;
						getSupportFragmentManager()
						.beginTransaction()
						.replace(
								R.id.ak_content,
								new WebFragment(
										DisciplineActivity.this,1,studentId, swDate,
										ewDate)).commit();
					}
					break;
				default:
					break;
				}
			}
		});
		rg.check(R.id.day);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(
				R.id.ak_content,
				new WebFragment(
						DisciplineActivity.this, 1,studentId, dayDate,
						dayDate)).commit();
		after_tva.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				switch (page) {
				case 0:
					if (month != null && month.size() != 0) {
						for (int i = 0; i < month.size(); i++) {
							if (m == i) {
								if (i != 0) {
								smDate = month.get(i-1).sDate;
								emDate = month.get(i-1).eDate;
								m = i-1;
								break;
								}else {
									Toast.makeText(DisciplineActivity.this, "已经是第一月了", Toast.LENGTH_SHORT)
									.show();
								}
							}
						}
					}
					getSupportFragmentManager()
					.beginTransaction()
					.replace(
							R.id.ak_content,
							new WebFragment(
									DisciplineActivity.this, 1,studentId, smDate,
									emDate)).commit();
					break;
				case 1:
					dayDate=deleteDay(dayDate);
					getSupportFragmentManager()
					.beginTransaction()
					.replace(
							R.id.ak_content,
							new WebFragment(
									DisciplineActivity.this, 1,studentId, dayDate,
									dayDate)).commit();
					break;
				case 2:
					if (week != null && week.size() != 0) {
						for (int i = 0; i < week.size(); i++) {
							if (w == i) {
								if (i != 0) {
								swDate = week.get(i-1).sDate;
								ewDate = week.get(i-1).eDate;
								w = i-1;
								break;
								}else {
									Toast.makeText(DisciplineActivity.this, "已经是第一周了", Toast.LENGTH_SHORT)
									.show();
								}
							}
						}
					}
					getSupportFragmentManager()
					.beginTransaction()
					.replace(
							R.id.ak_content,
							new WebFragment(
									DisciplineActivity.this, 1,studentId, swDate,
									ewDate)).commit();
					break;
				default:
					break;
				}
			}
		});
		after_tvb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				switch (page) {
				case 0:
					if (month != null && month.size() != 0) {
						for (int i = 0; i < month.size(); i++) {
							if (m == i) {
								if (i != month.size()-1) {
								smDate = month.get(i+1).sDate;
								emDate = month.get(i+1).eDate;
								m = i+1;
								break;
								}else {
									Toast.makeText(DisciplineActivity.this, "已经是最后一月了", Toast.LENGTH_SHORT)
									.show();
								}
							}
						}
					}
					getSupportFragmentManager()
					.beginTransaction()
					.replace(
							R.id.ak_content,
							new WebFragment(
									DisciplineActivity.this, 1,studentId, smDate,
									emDate)).commit();
					break;
				case 1:
					dayDate=addDay(dayDate);
					getSupportFragmentManager()
					.beginTransaction()
					.replace(
							R.id.ak_content,
							new WebFragment(
									DisciplineActivity.this, 1,studentId, dayDate,
									dayDate)).commit();
					break;
				case 2:
					if (week != null && week.size() != 0) {
						for (int i = 0; i < week.size(); i++) {
							if (w == i) {
								if (i != week.size()-1) {
								swDate = week.get(i+1).sDate;
								ewDate = week.get(i+1).eDate;
								w = i+1;
								break;
								}else {
									Toast.makeText(DisciplineActivity.this, "已经是最后一周了", Toast.LENGTH_SHORT)
									.show();
								}
							}
						}
					}
					getSupportFragmentManager()
					.beginTransaction()
					.replace(
							R.id.ak_content,
							new WebFragment(
									DisciplineActivity.this, 1,studentId, swDate,
									ewDate)).commit();
					break;
				default:
					break;
				}
			}
		});
		after_center.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				switch (page) {
				case 0:
					if (month != null && month.size() != 0) {
						for (int i = 0; i < month.size(); i++) {
							if (month.get(i).isCurrent == 1) {
								// if (i != (MainActivity.children.getSemester().size() -
								// 1)) {
								smDate = month.get(i).sDate;
								emDate = month.get(i).eDate;
								m = i;
								break;
							}
						}
					}
					getSupportFragmentManager()
					.beginTransaction()
					.replace(
							R.id.ak_content,
							new WebFragment(
									DisciplineActivity.this, 1,studentId, smDate,
									emDate)).commit();
					break;
				case 1:
					dayDate=App.loginInfo.date;
					getSupportFragmentManager()
					.beginTransaction()
					.replace(
							R.id.ak_content,
							new WebFragment(
									DisciplineActivity.this, 1,studentId, dayDate,
									dayDate)).commit();
					break;
				case 2:
					if (week != null && week.size() != 0) {
						for (int i = 0; i < week.size(); i++) {
							if (week.get(i).isCurrent == 1) {
								// if (i != (MainActivity.children.getSemester().size() -
								// 1)) {
								swDate = week.get(i).sDate;
								ewDate = week.get(i).eDate;
								w = i;
								break;
							}
						}
					}
					getSupportFragmentManager()
					.beginTransaction()
					.replace(
							R.id.ak_content,
							new WebFragment(
									DisciplineActivity.this, 1,studentId, swDate,
									ewDate)).commit();
					break;
				default:
					break;
				}
			}
		});

	}

	private String addDay(String date) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        Date dt;
		try {
			dt = sdf.parse(date);
			Calendar rightNow = Calendar.getInstance();  
			rightNow.setTime(dt);  
			rightNow.add(Calendar.DAY_OF_YEAR, 1);// 日期加10天  
			return sdf.format(rightNow.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;  
	}
	
	private String deleteDay(String date) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        Date dt;
		try {
			dt = sdf.parse(date);
			Calendar rightNow = Calendar.getInstance();  
			rightNow.setTime(dt);  
			rightNow.add(Calendar.DAY_OF_YEAR, -1);// 日期加10天  
			return sdf.format(rightNow.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;  
	}
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("studentId", studentId);
		map1.put("semesterId", semesterId);
		map1.put("date", dayDate);
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
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				BeanExpress express = JSON.parseObject(
						result.getString("data"), BeanExpress.class);
				if (express != null) {
					week.clear();
					week.addAll(express.weekArr);
					month.clear();
					month.addAll(express.monthArr);
					initView();
				} else {
					toast("暂无数据");
				}
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_discipline;
	}

}
