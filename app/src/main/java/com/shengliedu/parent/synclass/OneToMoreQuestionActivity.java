package com.shengliedu.parent.synclass;

import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.SynQuestion;
import com.shengliedu.parent.synclass.fragment.OneToMoreQuestionFragment;
import com.shengliedu.parent.synclass.interf.NextOrLast;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class OneToMoreQuestionActivity extends BaseActivity implements
		NextOrLast {
	private String date;
	private String content_host;
	private String questionTypeName;
	private int studentId;
	private int coursewareContentId;
	private int hourId;
	private List<SynQuestion> dataList = new ArrayList<SynQuestion>();
	private boolean isPrevious;
	private boolean isNext;
	private int position;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		coursewareContentId = (Integer) getIntent().getExtras().get(
				"coursewareContentId");
		studentId = App.childInfo.id;
		date = (String) getIntent().getExtras().get("date");
		content_host = (String) getIntent().getExtras().get("content_host");
		hourId = (Integer) getIntent().getExtras().get("hours");
		questionTypeName = (String) getIntent().getExtras().get("questionTypeName");
		setBack();
		showTitle(Html.fromHtml(questionTypeName) + "");
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				List<SynQuestion> dataListTemp = JSON.parseArray(
						result.getString("data"), SynQuestion.class);
				if (dataListTemp != null && dataListTemp.size() > 0) {
					dataList.clear();
					dataList.addAll(dataListTemp);
					position = 0;
					SynQuestion data = dataList.get(position);
					Log.v("TAG", "1=" + dataList.size());
					if (position != 0) {
						isPrevious = true;
					} else {
						isPrevious = false;
					}
					if (position != dataList.size() - 1) {
						isNext = true;
					} else {
						isNext = false;
					}
					getSupportFragmentManager()
							.beginTransaction()
							.replace(
									R.id.fragment_content,
									new OneToMoreQuestionFragment(data,
											isPrevious, isNext))
							.commit();
				}
			}else if (msg.what==2){
			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("studentId", studentId);
		map2.put("hourId", hourId);
		map2.put("date", date);
		map2.put("coursewareContentId", coursewareContentId);
		doGet(content_host + Config1.getInstance().SYNCLASSSINGLE(), map2, new ResultCallback() {
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
			}});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_syn_oneformorequestion;
	}

	@Override
	public void nextOrlast(int nol) {
		// TODO Auto-generated method stub
		switch (nol) {
		case 1:
			position --;
			if (position != 0) {
				isPrevious = true;
			} else {
				isPrevious = false;
			}
			if (position != dataList.size() - 1) {
				isNext = true;
			} else {
				isNext = false;
			}
			getSupportFragmentManager()
			.beginTransaction()
			.replace(
					R.id.fragment_content,
					new OneToMoreQuestionFragment(dataList.get(position),
							isPrevious, isNext))
			.commit();
			break;
		case 2:
			position ++;
			if (position != 0) {
				isPrevious = true;
			} else {
				isPrevious = false;
			}
			if (position != dataList.size() - 1) {
				isNext = true;
			} else {
				isNext = false;
			}
			getSupportFragmentManager()
			.beginTransaction()
			.replace(
					R.id.fragment_content,
					new OneToMoreQuestionFragment(dataList.get(position),
							isPrevious, isNext))
			.commit();
			break;
		default:
			break;
		}
	}
}
