package com.shengliedu.parent.showdesign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.SynLianXianAdapter;
import com.shengliedu.parent.bean.AfterClassLianXian;
import com.shengliedu.parent.synclass.SynSeletQuestionAnalyticalActivity;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.HtmlImage;
import com.shengliedu.parent.util.NumberToCode;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.view.NoScrollListview;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SynSanLianXianActivity extends BaseActivity {
	private String type;
	private String name;
	private String content_host;
	private int school;
	private int from;
	private int id;

	public TextView select_title, pandun, zhengquelv, syn_question_head;
	public Button select_btn;
	private String ques = "";
	private String rques = "";
	private Map<Integer, Boolean> map2 = new HashMap<Integer, Boolean>();
	private Map<Integer, Boolean> map1 = new HashMap<Integer, Boolean>();
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private ListView listView;
	private NoScrollListview listView1;
	private NoScrollListview listView2;
	private AfterClassLianXian data;
	private LinearLayout sanlianxLin;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		id = (Integer) getIntent().getExtras().get(
				"id");
		type = (String) getIntent().getExtras().get("type");
		content_host = (String) getIntent().getExtras().get("content_host");
		school = (Integer) getIntent().getExtras().get("school");
		from = (Integer) getIntent().getExtras().get("from");
		name = (String) getIntent().getExtras().get(
				"name");
		setBack();
		showTitle(Html.fromHtml(name)+"");
		sanlianxLin = (LinearLayout) findViewById(R.id.sanlianxLin);
		select_title = (TextView) findViewById(R.id.syn_question_title);
		pandun = (TextView) findViewById(R.id.syn_question_pandun);
		listView = (NoScrollListview) findViewById(R.id.syn_question_lv);
		listView1 = (NoScrollListview) findViewById(R.id.syn_question_lv1);
		listView2 = (NoScrollListview) findViewById(R.id.syn_question_lv2);
		select_btn = (Button) findViewById(R.id.jiexi);
		zhengquelv = (TextView) findViewById(R.id.zhengquelv);
		select_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent iteIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("explanation", data.explanation);
				iteIntent.putExtras(bundle);
				iteIntent.setClass(SynSanLianXianActivity.this,
						SynSeletQuestionAnalyticalActivity.class);
				if (TextUtils.isEmpty(data.explanation)) {
					toast("没有解析");
				}else {
					startActivity(iteIntent);
				}
			}
		});
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				List<AfterClassLianXian> dataListTemp = JSON
						.parseArray(result.getString("data"),
								AfterClassLianXian.class);
				if (dataListTemp != null) {
					data = dataListTemp.get(0);
					select_title.setText(Html.fromHtml(HtmlImage
							.deleteSrc(data.question.question)));
					String name = data.question.question;
					List<String> imgList = HtmlImage.getImgSrc(name);
					if (imgList.size() > 0) {
						for (int j = 0; j < imgList.size(); j++) {
							final String url = imgList.get(j);
							ImageView imageView = new ImageView(
									SynSanLianXianActivity.this);
							imageView
									.setScaleType(ScaleType.FIT_CENTER);
							imageView
									.setLayoutParams(new LayoutParams(
											android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
											android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
							ImageLoader imageLoader=ImageLoader.getInstance();
							imageLoader.displayImage(url, imageView);
							imageView
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated
											// method stub
											HtmlImage htmlImage = new HtmlImage();
											htmlImage
													.showDialog(
															SynSanLianXianActivity.this,
															url);
										}
									});
							sanlianxLin.addView(imageView);
						}
					}
					if (data.studentAnswer != null
							&& data.studentAnswer.size() > 0) {
						ques = data.studentAnswer.get(0).title;
					}
					rques = data.correct.get(0).title;
					Log.e("ques", "haha" + ques);
					Log.e("rques", rques);
					String per = data.percentage;
//							Log.e("per", per);
					if (ques != null && ques.length() == 3) {
						map.put(NumberToCode.codeToNumber(ques
								.substring(0, 1)), true);
						map1.put(NumberToCode.codeToNumber(ques
								.substring(1, 2)), true);
						map2.put(NumberToCode.codeToNumber(ques
								.substring(2, 3)), true);
					}
					SynLianXianAdapter adapter = new SynLianXianAdapter(
							SynSanLianXianActivity.this,
							data.answer.answersF.get(0).items, map);
					listView.setAdapter(adapter);
					SynLianXianAdapter adapter1 = new SynLianXianAdapter(
							SynSanLianXianActivity.this,
							data.answer.answersF.get(1).items, map1);
					listView1.setAdapter(adapter1);
					SynLianXianAdapter adapter2 = new SynLianXianAdapter(
							SynSanLianXianActivity.this,
							data.answer.answersF.get(2).items, map2);
					listView2.setAdapter(adapter2);
					String rqueStr = "";
					if (data.correct != null & data.correct.size() != 0) {
						rques = data.correct.get(0).title;
						for (int i = 0; i < data.correct.size(); i++) {
							rqueStr = rqueStr
									+ data.correct.get(i).title + " ";
						}
					}
					int i = 0;
					String text = "";
					if (data.studentAnswer != null
							&& data.studentAnswer.size() > 0) {
						for (int j = 0; j < data.studentAnswer.size(); j++) {
							if (rqueStr.contains(data.studentAnswer
									.get(i).title)) {
								i++;
							}
							text = text
									+ data.studentAnswer.get(j).title
									+ " ";
						}

					}
//							if (!"".equals(rqueStr)) {
//								if (data.correct.size() == i) {
//									zhengquelv.setText("您学生的答案是" + text
//											+ ",正确答案是" + rqueStr + ",回答正确！"
//											+ "全班回答正确率 " + per + "%");
//
//								} else {
//									zhengquelv.setText("您学生的答案是" + text
//											+ ",正确答案是" + rqueStr + ",回答错误！"
//											+ "全班回答正确率 " + per + "%");
//								}
//
//							} else {
//								zhengquelv.setText("您的学生未回答 正确答案是" + rqueStr
//										+ " 全班回答正确率 " + per + "%");
//							}

				}
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("id", id+"");
		map3.put("school", school+"");
		map3.put("from", from+"");
		map3.put("content_type", 2);
		doGet(content_host + Config1.getInstance().SHOWCLASSSINGLE(), map3,
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
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_syn_sanlianxian;
	}

}
