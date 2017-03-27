package com.shengliedu.parent.synclass;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.SynQuestionAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.SynQuestion;
import com.shengliedu.parent.pdf.ShowPdfFromUrlActivity;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.HtmlImage;
import com.shengliedu.parent.util.NumberToCode;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.view.NoScrollListview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SynDuoXuanActivity extends BaseActivity {
	private String date;
	private String content_host;
	private String questionTypeName;
	private String questionContentType;
	private int studentId;
	private int coursewareContentId;
	private int hourId;
	
	public TextView select_title, pandun, zhengquelv, syn_question_head;
	public Button select_btn;
	private String ques = "";
	private String rques = "";
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private NoScrollListview listView;
	private SynQuestionAdapter adapter;
	private List<SynQuestion> dataList=new ArrayList<SynQuestion>();
	private LinearLayout suoxuanLin,xuanxianglinear;
	private TextView select_pdf;
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
		questionContentType = (String) getIntent().getExtras().get(
				"questionContentType");
		setBack();
		showTitle(questionTypeName);
		xuanxianglinear = (LinearLayout) findViewById(R.id.xuanxianglinear);
		suoxuanLin = (LinearLayout) findViewById(R.id.suoxuanLin);
		select_title = (TextView) findViewById(R.id.syn_question_title);
		select_pdf = (TextView) findViewById(R.id.select_pdf);
		pandun = (TextView) findViewById(R.id.syn_question_pandun);
		listView = (NoScrollListview) findViewById(R.id.syn_question_lv);
		select_btn = (Button) findViewById(R.id.jiexi);
		zhengquelv = (TextView) findViewById(R.id.zhengquelv);
		select_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent iteIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("explanation", dataList.get(0).explanation);
				iteIntent.putExtras(bundle);
				iteIntent.setClass(SynDuoXuanActivity.this,
						SynSeletQuestionAnalyticalActivity.class);
				startActivity(iteIntent);
			}
		});
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				List<SynQuestion> dataListTemp = JSON.parseArray(result.getString("data"), SynQuestion.class);
				if (dataListTemp != null) {
					dataList.clear();
					dataList.addAll(dataListTemp);
					Spanned spanned = Html.fromHtml(HtmlImage.deleteSrc(dataList.get(0).question.question));
					select_title.setText(spanned);
					String name = dataList.get(0).question.question;
					List<String> imgList = HtmlImage.getImgSrc(name);
					if (imgList.size() > 0) {
						for (int j = 0; j < imgList.size(); j++) {
							final String url = imgList.get(j);
							ImageView imageView = new ImageView(
									SynDuoXuanActivity.this);
							imageView.setScaleType(ScaleType.FIT_CENTER);
							imageView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
							ImageLoader.getInstance().displayImage(url, imageView);
							imageView.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated
									// method stub
									HtmlImage htmlImage=new HtmlImage();
									htmlImage.showDialog(SynDuoXuanActivity.this,url);
								}
							});
							suoxuanLin.addView(imageView);
						}
					}
					if (dataList.get(0)!=null &&dataList.get(0).studentAnswer!=null&&dataList.get(0).studentAnswer.size()>0) {
						ques = dataList.get(0).studentAnswer.get(0).title;
					}
					rques = dataList.get(0).correct.get(0).title;
					Log.e("ques", "haha" + ques);
					Log.e("rques", rques);
					String per = dataList.get(0).percentage;
					Log.e("per", per);
					if (ques != null) {
						for (int i = 0; i < ques.length(); i++) {
							String code = String.valueOf(ques.charAt(i));
							map.put(NumberToCode.codeToNumber(code), true);
						}
					}
					if ("1".equals(questionContentType)) {
						select_pdf.setVisibility(View.VISIBLE);
						select_pdf.setText("查看PDF题目");
						select_pdf.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent intent=new Intent(SynDuoXuanActivity.this,ShowPdfFromUrlActivity.class);
								intent.putExtra("url", Config1.IP + dataList.get(0).question.contentLink);
								startActivity(intent);
							}
						});
					} else {
						select_pdf.setVisibility(View.GONE);
					}
					if (!App.listIsEmpty(dataList)&&!App.listIsEmpty(dataList.get(0).answer.answers)
							&& !App.listIsEmpty(dataList.get(0).answer.answers.get(0))) {
						xuanxianglinear.setVisibility(View.VISIBLE);
						adapter = new SynQuestionAdapter(
								SynDuoXuanActivity.this, dataList, map);
						listView.setAdapter(adapter);
					}else {
						xuanxianglinear.setVisibility(View.GONE);
					}
					if (!"".equals(ques)) {
						if (ques.equals(rques)) {
							zhengquelv.setText("您孩子的答案是" + ques + ",正确答案是" + rques
									+ ",回答正确！" + "全班回答正确率 " + per + "%");

						} else {
							zhengquelv.setText("您孩子的答案是" + ques + ",正确答案是" + rques
									+ ",回答错误！" + "全班回答正确率 " + per + "%");
						}

					} else {
						zhengquelv.setText("您孩子未回答 正确答案是" + rques + " 全班回答正确率 "
								+ per + "%");
					}
				}
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("studentId", studentId);
		map1.put("hourId", hourId);
		map1.put("date", date);
		map1.put("coursewareContentId", coursewareContentId);
		doGet(content_host + Config1.getInstance().SYNCLASSSINGLE(), map1,
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
		return R.layout.activity_syn_duoxuan;
	}

}
