package com.shengliedu.parent.synclass;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.PingjiaKe;
import com.shengliedu.parent.adapter.SynPingJia0Adapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.SynPingJia;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.view.NoScrollListview;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SynPingJiaActivity extends BaseActivity {
	private String date;
	private String content_host;
	private String questionTypeName;
	private int studentId;
	private int coursewareContentId;
	private int hourId;

	public TextView select_title, syn_question_head, position;
	public TextView jiazhang_pingjia, go_pingjia;
	private TextView zuyuan_pingjia, self_pingjia, laoshi_pingjia,
			zong_pingjia;
	public Button select_btn;
	private NoScrollListview lv;
	private String name = "";
	private List<SynPingJia> ac;
	
	private LinearLayout syn_linear,pj_lin;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		coursewareContentId = (Integer) getIntent().getExtras().get(
				"coursewareContentId");
		studentId = App.childInfo.id;
		date = (String) getIntent().getExtras().get("date");
		content_host = (String) getIntent().getExtras().get("content_host");
		hourId = (Integer) getIntent().getExtras().get("hours");
		questionTypeName = (String) getIntent().getExtras().get(
				"questionTypeName");
		setBack();
		showTitle(questionTypeName);
		syn_linear = (LinearLayout) findViewById(R.id.syn_linear);
		pj_lin = (LinearLayout) findViewById(R.id.pj_lin);
		select_title = (TextView) findViewById(R.id.syn_question_title);
		zuyuan_pingjia = (TextView) findViewById(R.id.zuyuan_pingjia);
		self_pingjia = (TextView) findViewById(R.id.self_pingjia);
		laoshi_pingjia = (TextView) findViewById(R.id.teacher_pingjia);
		zong_pingjia = (TextView) findViewById(R.id.zong_pingjia);
		position = (TextView) findViewById(R.id.position);
		jiazhang_pingjia = (TextView) findViewById(R.id.jiazhang_pingjia);
		go_pingjia = (TextView) findViewById(R.id.go_pingjia);
		lv = (NoScrollListview) findViewById(R.id.syn_question_lv);
		select_btn = (Button) findViewById(R.id.jiexi);
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				final PingjiaKe pingjiaKe = JSON.parseObject(
						result.getString("data"), PingjiaKe.class);
				if (pingjiaKe != null) {
					syn_linear.setVisibility(View.VISIBLE);
					pj_lin.setVisibility(View.VISIBLE);
					ac = pingjiaKe.formItemList;
					SynPingJia0Adapter adapter = new SynPingJia0Adapter(
							SynPingJiaActivity.this, ac);
					lv.setAdapter(adapter);
					zuyuan_pingjia.setText(pingjiaKe.otherScore + "/"
							+ pingjiaKe.fullScore);
					self_pingjia.setText(pingjiaKe.selfScore + "/"
							+ pingjiaKe.fullScore);
					laoshi_pingjia.setText(pingjiaKe.teacherScore + "/"
							+ pingjiaKe.fullScore);
					zong_pingjia.setText(pingjiaKe.totalScore + "/"
							+ pingjiaKe.fullScore);
					position.setText(pingjiaKe.position);
					String score=pingjiaKe.parentScore;
					jiazhang_pingjia.setText(score);
					go_pingjia.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(context, SelectDialogActivity.class);
							intent.putExtra("type", 22);
							intent.putExtra("activityId", pingjiaKe.activityId);
							intent.putExtra("coursewareContentId", coursewareContentId);
							// startActivityForResultByAniamtion(intent, 11);
							startActivityForResult(intent, 100);
						}
					});
				}else {
					toast("暂无数据");
				}

			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("studentId", studentId);
		map3.put("hourId", hourId);
		map3.put("date", date);
		map3.put("coursewareContentId", coursewareContentId);
		doGet(content_host + Config1.getInstance().SYNCLASSSINGLE(), map3,
				new ResultCallback() {
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
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 100 &&arg1 == 133) {
			jiazhang_pingjia.setText(arg2.getStringExtra("data"));
		}
	}
	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_syn_pingjia;
	}

}
