package com.shengliedu.parent.showdesign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
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
import com.shengliedu.parent.adapter.SynQuestionAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.SynQuestion;
import com.shengliedu.parent.pdf.ShowPdfFromUrlActivity;
import com.shengliedu.parent.synclass.SynSeletQuestionAnalyticalActivity;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.HtmlImage;
import com.shengliedu.parent.util.NumberToCode;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SynSingleActivity extends BaseActivity {
	private String type;
	private String name;
	private String title;
	private String questionContentType;
	private String content_host;
	private int school;
	private int from;
	private int id;

	public TextView select_title, pandun, zhengquelv, syn_question_head;
	public Button select_btn;
	private String ques = "";
	private String rques = "";
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private ListView listView;
	private SynQuestionAdapter adapter;
	private List<SynQuestion> dataList = new ArrayList<SynQuestion>();
	private LinearLayout synsingleLin,xuanxianglinear;
	private TextView select_pdf;
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
		questionContentType = (String) getIntent().getExtras().get(
				"questionContentType");
		title = (String) getIntent().getExtras().get(
				"title");
		setBack();
		showTitle(Html.fromHtml(title)+"");
		synsingleLin = (LinearLayout) findViewById(R.id.synsingleLin);
		xuanxianglinear = (LinearLayout) findViewById(R.id.xuanxianglinear);
		select_title = (TextView) findViewById(R.id.syn_question_title);
		select_pdf = (TextView) findViewById(R.id.select_pdf);
		pandun = (TextView) findViewById(R.id.syn_question_pandun);
		listView = (ListView) findViewById(R.id.syn_question_lv);
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
				iteIntent.setClass(SynSingleActivity.this,
						SynSeletQuestionAnalyticalActivity.class);
				if (TextUtils.isEmpty(dataList.get(0).explanation)) {
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
				List<SynQuestion> dataListTemp = JSON.parseArray(
						result.getString("data"), SynQuestion.class);
				if (dataListTemp != null) {
					dataList.clear();
					dataList.addAll(dataListTemp);
					// Spanned spanned =
					// Html.fromHtml(dataList.get(0).question.question,new
					// MyImageGetter(SynSingleActivity.this,select_title),null);
					select_title.setText(Html.fromHtml(HtmlImage.deleteSrc(dataList.get(0).question.question)));
					// select_title.setMovementMethod(LinkMovementMethod.getInstance());
					String name = dataList.get(0).question.question;
					List<String> imgList = HtmlImage.getImgSrc(name);
					if (imgList.size() > 0) {
						for (int j = 0; j < imgList.size(); j++) {
							final String url = imgList.get(j);
							ImageView imageView = new ImageView(
									SynSingleActivity.this);
							imageView.setScaleType(ScaleType.FIT_CENTER);
							imageView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
							ImageLoader imageLoader=ImageLoader.getInstance();
							imageLoader.displayImage(url, imageView);
							imageView.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated
									// method stub
									HtmlImage htmlImage=new HtmlImage();
									htmlImage.showDialog(SynSingleActivity.this,url);
								}
							});
							synsingleLin.addView(imageView);
						}
					}
					if (dataList.get(0).studentAnswer != null
							&& dataList.get(0).studentAnswer.size() > 0) {
						ques = dataList.get(0).studentAnswer.get(0).title;
					}
					if ("1".equals(questionContentType)) {
						select_pdf.setVisibility(View.VISIBLE);
						select_pdf.setText("查看PDF题目");
						select_pdf.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent intent=new Intent(SynSingleActivity.this,ShowPdfFromUrlActivity.class);
								intent.putExtra("url", Config1.IP + dataList.get(0).question.contentLink);
								startActivity(intent);
							}
						});
					} else {
						select_pdf.setVisibility(View.GONE);
					}
					rques = dataList.get(0).correct.get(0).title;
					// String per = dataList.get(0).percentage;
					// Log.e("per", per);
					if (ques != null) {
						map.put(NumberToCode.codeToNumber(ques), true);
					}
					if (!App.listIsEmpty(dataList)&&!App.listIsEmpty(dataList.get(0).answer.answers)
							&& !App.listIsEmpty(dataList.get(0).answer.answers.get(0))) {
						xuanxianglinear.setVisibility(View.VISIBLE);
						adapter = new SynQuestionAdapter(
								SynSingleActivity.this, dataList, map);
						listView.setAdapter(adapter);
					}else {
						xuanxianglinear.setVisibility(View.GONE);
					}
//							if (!"".equals(ques)) {
//								if (ques.equals(rques)) {
//									zhengquelv.setText("您孩子的答案是" + ques
//											+ ",正确答案是" + rques + ",回答正确！"
//											+ "全班回答正确率 " + per + "%");
//
//								} else {
//									zhengquelv.setText("您孩子的答案是" + ques
//											+ ",正确答案是" + rques + ",回答错误！"
//											+ "全班回答正确率 " + per + "%");
//								}
//
//							} else {
//								zhengquelv.setText("您孩子未回答 正确答案是" + rques
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
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("id", id+"");
		map1.put("school", school+"");
		map1.put("from", from+"");
		map1.put("content_type", 2);
		doGet(content_host + Config1.getInstance().SHOWCLASSSINGLE(), map1,
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
		return R.layout.activity_synsingle;
	}

}
