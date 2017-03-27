package com.shengliedu.parent.more.jiatiao;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.BeanJie;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.view.NoScrollGridView;
import com.shengliedu.parent.view.PickerView;
import com.shengliedu.parent.view.PickerView.onSelectListener;
import com.shengliedu.parent.view.PopupWindowUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class QingJiaActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout start_btn, end_btn, sure_rel;
	private LinearLayout anjie_lay;
	private TextView start_time, end_time;
	private RadioGroup typeGroup;
	private Button jia_all, jia_shangwu, jia_xiawu;
	private EditText jia_content;
	private HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private HashMap<Integer, Boolean> map1 = new HashMap<Integer, Boolean>();
	private PopupWindow PopupWindow;
	private int jia_type = -1;
	private NoScrollGridView gridView;
	private String studentId;
	private String parentId;
	private String date;
	private String scopeList="";
	private String start;
	private String end;
	private String content;

	private List<BeanJie> jies;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		studentId = App.childInfo.id + "";
		parentId = App.userInfo.id + "";
		date = App.loginInfo.date;
		setBack();
		showTitle("请认真填写请假条");
		anjie_lay = (LinearLayout) findViewById(R.id.anjie_lay);
		start_btn = (RelativeLayout) findViewById(R.id.start_rel);
		sure_rel = (RelativeLayout) findViewById(R.id.sure_rel);
		end_btn = (RelativeLayout) findViewById(R.id.end_rel);
		start_time = (TextView) findViewById(R.id.start_time);
		end_time = (TextView) findViewById(R.id.end_time);
		typeGroup = (RadioGroup) findViewById(R.id.type_group);
		jia_all = (Button) findViewById(R.id.jia_all);
		jia_shangwu = (Button) findViewById(R.id.jia_shangwu);
		jia_xiawu = (Button) findViewById(R.id.jia_xiawu);
//		jia_anjie = (Button) findViewById(R.id.jia_anjie);
		gridView = (NoScrollGridView) findViewById(R.id.jia_grid);
		jia_content = (EditText) findViewById(R.id.jia_content);
		for (int i = 0; i < 8; i++) {
			map1.put(i, false);
		}
		initDate(start_time);
		initDate(end_time);

		start_btn.setOnClickListener(this);
		end_btn.setOnClickListener(this);
		typeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.shijia_btn:
					jia_type = 1;
					break;
				case R.id.bingjia_btn:
					jia_type = 2;
					break;
				default:
					break;
				}
			}
		});
		sure_rel.setOnClickListener(this);
		jia_all.setOnClickListener(this);
		jia_shangwu.setOnClickListener(this);
		jia_xiawu.setOnClickListener(this);
//		jia_anjie.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.start_rel:
			initDateView(start_time);
			PopupWindow = PopupWindowUtils.showCamerPopupWindow(dateSelected,
					R.layout.activity_qingjia, this);
			break;
		case R.id.end_rel:
			initDateView(end_time);
			PopupWindow = PopupWindowUtils.showCamerPopupWindow(dateSelected,
					R.layout.activity_qingjia, this);
			break;
		case R.id.jia_all:
			anjie_lay.setVisibility(View.GONE);
			map.clear();
			map.put(0, true);
			break;
		case R.id.jia_shangwu:
			anjie_lay.setVisibility(View.GONE);
			map.clear();
			map.put(1, true);
			break;
		case R.id.jia_xiawu:
			anjie_lay.setVisibility(View.GONE);
			map.clear();
			map.put(2, true);
			break;
//		case R.id.jia_anjie:
//			map.clear();
//			map.put(4, true);
//			map1.clear();
//			Map<String, Object> mapq = new HashMap<String, Object>();
//			mapq.put("studentId", studentId);
//			mapq.put("date", date);
//			doGet(Config1.getInstance().DATESCOPE(), mapq, new ResultCallBack() {
//
//				@Override
//				public void onSuccess(JSONObject result) {
//					// TODO Auto-generated method stub
//					jies = JSONArray.parseArray(result.getString("data"),
//							BeanJie.class);
//					if (jies != null && jies.size() > 0) {
//						anjie_lay.setVisibility(View.VISIBLE);
//					}
//					final JieAdapter adapter = new JieAdapter(
//							QingJiaActivity.this, jies, map1);
//					gridView.setAdapter(adapter);
//					gridView.setOnItemClickListener(new OnItemClickListener() {
//
//						@Override
//						public void onItemClick(AdapterView<?> arg0, View arg1,
//								int arg2, long arg3) {
//							// TODO Auto-generated method stub
//							if (map1.containsKey(arg2)) {
//								map1.remove(arg2);
//							} else {
//								map1.put(arg2, true);
//							}
//							adapter.notifyDataSetChanged();
//						}
//					});
//				}
//
//				@Override
//				public void onFailure(String result) {
//					// TODO Auto-generated method stub
//
//				}
//			});
//			break;
		case R.id.sure_rel:
			start = start_time.getText().toString() + "";
			end = end_time.getText().toString() + "";
			if (Integer.parseInt(start.substring(0, 4)) > Integer.parseInt(end
					.substring(0, 4))) {
				Log.v("TAG", start.substring(0, 4) + ":" + end.substring(0, 4));
				Toast.makeText(this, "截止时间不能小于开始时间", Toast.LENGTH_SHORT).show();

				break;
			} else if (Integer.parseInt(start.substring(0, 4)) == Integer
					.parseInt(end.substring(0, 4))) {
				if (Integer.parseInt(start.substring(5, 7)) > Integer
						.parseInt(end.substring(5, 7))) {
					Log.v("TAG",
							start.substring(5, 7) + ":" + end.substring(5, 7));
					Toast.makeText(this, "截止时间不能小于开始时间", Toast.LENGTH_SHORT).show();
					break;
				} else if (Integer.parseInt(start.substring(5, 7)) == Integer
						.parseInt(end.substring(5, 7))) {
					if (Integer.parseInt(start.substring(8)) > Integer
							.parseInt(end.substring(8))) {
						Log.v("TAG",
								start.substring(8) + ":" + end.substring(8));
						Toast.makeText(this, "截止时间不能小于开始时间", Toast.LENGTH_SHORT).show();
						break;
					}else if(Integer.parseInt(start.substring(8)) < Integer
							.parseInt(end.substring(8))){
						if (!map.containsKey(0)) {
							Toast.makeText(this, "请假类型选择全天", Toast.LENGTH_SHORT).show();
							break;
						}
					}
				}else {
					if (!map.containsKey(0)) {
						Toast.makeText(this, "请假类型选择全天", Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}else {
				if (!map.containsKey(0)) {
					Toast.makeText(this, "请假类型选择全天",Toast.LENGTH_SHORT).show();
					break;
				}
			}
			if (jia_type == -1) {
				Toast.makeText(this, "请选择请假类型",Toast.LENGTH_SHORT).show();
				break;
			}
			int i = 0;
			boolean ex = false;
			String part = "";
			StringBuilder sb = new StringBuilder();
			while (true) {
				if (map.containsKey(i) && map.get(i)) {
					part = i + "";
					if (i == 4) {
						int j = 0;
						while (true) {
							if (map1.containsKey(j) && map1.get(j)) {
								ex = true;
								sb.append(jies.get(j).getId());
								sb.append(",");
							}
							if (j > 10) {
								break;
							}
							j++;
						}
					} else {
						ex = true;
					}
				}

				if (i > 3) {
					break;
				}
				i++;
			}
			if (!ex) {
				Log.v("TAG", "11");
				Toast.makeText(this, "请选择请假时段", Toast.LENGTH_SHORT).show();
				break;
			}
			content = jia_content.getText().toString() + "";
			if (TextUtils.isEmpty(content)) {
				Toast.makeText(this, "请输入请假事由", Toast.LENGTH_SHORT).show();
				break;
			}
			if (sb.toString().length()>0) {
				scopeList = sb.toString().substring(0,sb.toString().lastIndexOf(","));
			}
			if (scopeList.length() > 0) {
				scopeList = scopeList.substring(0, scopeList.length() - 1);
			}
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("sDate", start);
			map.put("eDate", end);
			map.put("type", jia_type + "");
			map.put("content", content);
			map.put("child", studentId);
			map.put("parent", parentId);
			map.put("part", part);
			map.put("scopeList", scopeList);
			doPost(Config1.getInstance().ADDNOTE(), map, new ResultCallback() {
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
			break;
		default:
			break;
		}
		setBackground();
	}
	Handler handlerReq=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				JSONObject jsonObject= JSON.parseObject((String) msg.obj);
				if (1000==jsonObject.getInteger("code")) {
					toast("提交假条成功");
					setResult(441);
					finish();
				}else {
					toast(jsonObject.getString("message"));
				}
			} else if (msg.what == 2) {
			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_qingjia;
	}

	private void initDateView(final TextView text) {
		// TODO Auto-generated method stub
		// 时间选择器
		layoutInflater = LayoutInflater.from(this);
		dateSelected = layoutInflater.inflate(
				R.layout.appointment_dateselect_windowview2, null);
		// 选择开始时间
		dateSelected.findViewById(R.id.time_cacel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (PopupWindow != null) {
							PopupWindow.dismiss();
						}
					}
				});
		dateSelected.findViewById(R.id.time_exsit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						updateTime(text);
						if (PopupWindow != null) {
							PopupWindow.dismiss();
						}
					}
				});
		date_year = (PickerView) dateSelected
				.findViewById(R.id.appoint_date_year);
		date_month = (PickerView) dateSelected
				.findViewById(R.id.appoint_date_month);
		date_day = (PickerView) dateSelected
				.findViewById(R.id.appoint_date_day);
		// date_hour = (PickerView) dateSelected
		// .findViewById(R.id.appoint_date_hour);
		// date_minute = (PickerView) dateSelected
		// .findViewById(R.id.appoint_date_minute);
		// 选择开始时间
		date_year = (PickerView) dateSelected
				.findViewById(R.id.appoint_date_year);
		date_month = (PickerView) dateSelected
				.findViewById(R.id.appoint_date_month);
		date_day = (PickerView) dateSelected
				.findViewById(R.id.appoint_date_day);
		// date_hour = (PickerView) dateSelected
		// .findViewById(R.id.appoint_date_hour);
		// date_minute = (PickerView) dateSelected
		// .findViewById(R.id.appoint_date_minute);
		initData();
		// initDate(text);
		setData();
	}

	private void setBackground() {
		// TODO Auto-generated method stub
		Log.v("TAG", "bg");
		jia_all.setBackgroundResource(R.mipmap.jia_all);
		jia_shangwu.setBackgroundResource(R.mipmap.jia_shangwu);
		jia_xiawu.setBackgroundResource(R.mipmap.jia_xiawu);
//		jia_anjie.setBackgroundResource(R.mipmap.jia_anjie);
		int i = 0;
		while (true) {
			if (map.containsKey(i) && map.get(i)) {
				switch (i) {
				case 0:
					jia_all.setBackgroundResource(R.mipmap.jia_all_down);
					break;
				case 1:
					jia_shangwu
							.setBackgroundResource(R.mipmap.jia_shangwu_down);
					break;
				case 2:
					jia_xiawu.setBackgroundResource(R.mipmap.jia_xiawu_down);
					break;
				case 3:
//					jia_anjie.setBackgroundResource(R.mipmap.jia_anjie_down);
					break;
				default:
					break;
				}
			}
			if (i > 3) {
				break;
			}
			i++;
		}
	}

	// --------------------------时间选择器----------------

	private View dateSelected;
	private LayoutInflater layoutInflater;
	private int year, month, day, hour, minute;
	private PickerView date_year, date_month, date_day;
	// private PickerView date_hour, date_minute;
	private List<String> years, months, days_b, days_m, days_s, days_ss, hours,
			minutes;

	// private TextView selected_date;// 展示时间的控件

	private void initData() {
		// 年的数组
		years = new ArrayList<String>();
		for (int i = 1949; i < 2100; i++) {
			years.add("" + i);
		}
		// 月的数组
		months = new ArrayList<String>();
		for (int i = 1; i < 13; i++) {
			months.add(i < 10 ? "0" + i : "" + i);
		}
		// 日的数组（31）
		days_b = new ArrayList<String>();
		days_m = new ArrayList<String>();
		days_s = new ArrayList<String>();
		days_ss = new ArrayList<String>();
		for (int i = 1; i < 32; i++) {
			days_b.add(i < 10 ? "0" + i : "" + i);
			if (i < 31) {
				days_m.add(i < 10 ? "0" + i : "" + i);
			}
			if (i < 30) {
				days_s.add(i < 10 ? "0" + i : "" + i);
			}
			if (i < 29) {
				days_ss.add(i < 10 ? "0" + i : "" + i);
			}
		}

		hours = new ArrayList<String>();
		for (int i = 0; i <= 23; i++) {
			hours.add(i < 10 ? "0" + i : "" + i);
		}

		minutes = new ArrayList<String>();
		for (int i = 0; i <= 59; i++) {
			minutes.add(i < 10 ? "0" + i : "" + i);
		}
	}

	private void setData() {
		date_year.setData(years);
		date_year.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				year = Integer.parseInt(text);
				// updateTime(selected_date);
			}
		});
		date_month.setData(months);
		date_month.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				month = Integer.parseInt(text);
				if (year % 4 != 0 && year % 400 != 0) {
					if (month == 2) {
						date_day.setData(days_s);
					} else if (month == 4 || month == 6 || month == 9
							|| month == 11) {
						date_day.setData(days_m);
					} else {
						date_day.setData(days_b);
					}
				} else {
					if (month == 2) {
						date_day.setData(days_ss);
					} else if (month == 4 || month == 6 || month == 9
							|| month == 11) {
						date_day.setData(days_m);
					} else {
						date_day.setData(days_b);
					}
				}
				date_day.setSelected(day + "");
				// updateTime(selected_date);
			}
		});
		date_day.setData(days_b);
		date_day.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				day = Integer.parseInt(text);
				// updateTime(selected_date);
			}
		});

		// date_hour.setData(hours);
		// date_hour.setOnSelectListener(new onSelectListener() {
		// public void onSelect(String text) {
		// hour = Integer.parseInt(text);
		// // updateTime(selected_date);
		// }
		// });
		//
		// date_minute.setData(minutes);
		// date_minute.setOnSelectListener(new onSelectListener() {
		// public void onSelect(String text) {
		// minute = Integer.parseInt(text);
		// // updateTime(selected_date);
		// }
		// });
		// 设置PickerView的起始时间
		date_year.setSelected(year - 1949);
		date_month.setSelected(month - 1);
		date_day.setSelected(day - 1);
		// date_hour.setSelected(hour - 1);
		// date_minute.setSelected(minute - 1);
	}

	public void updateTime(TextView tv) {
		String month_ = "";
		if (month < 10) {
			month_ = "0" + month;
		} else {
			month_ = "" + month;
		}

		String day_ = "";
		if (day < 10) {
			day_ = "0" + day;
		} else {
			day_ = "" + day;
		}
		tv.setText(year + "/" + month_ + "/" + day_);
	}

	/**
	 * 获取系统的当前时间并赋值给TextView
	 */
	private void initDate(TextView text) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String strdate = format.format(date);
		text.setText(strdate);
		// 分别获取当前时间的年月日
		SimpleDateFormat format_year = new SimpleDateFormat("yyyy");
		SimpleDateFormat format_moth = new SimpleDateFormat("MM");
		SimpleDateFormat format_day = new SimpleDateFormat("dd");
		// SimpleDateFormat format_hour = new SimpleDateFormat("hh");
		// SimpleDateFormat format_minute = new SimpleDateFormat("mm");
		year = Integer.parseInt(format_year.format(date));
		month = Integer.parseInt(format_moth.format(date));
		day = Integer.parseInt(format_day.format(date));
		// hour = Integer.parseInt(format_hour.format(date));
		// minute = Integer.parseInt(format_minute.format(date));
	}

	/*
	 * @return转换时间为服务器所需要的类型
	 */
	public String switchTime() {
		String str = year + "/" + month + "/" + day;
		return str;
	}
}
