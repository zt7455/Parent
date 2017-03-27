package com.shengliedu.parent.synclass;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
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
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.SynQuestion;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.HtmlImage;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SynFillBlanksActivity extends BaseActivity {
	private String date;
	private String questionTypeName;
	private String content_host;
	private int studentId;
	private int coursewareContentId;
	private int hourId;

	public TextView select_title, pandun, zhengquelv, syn_question_head;
	public Button select_btn;
	private List<SynQuestion> dataList = new ArrayList<SynQuestion>();
	private LinearLayout synsingleLin;
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
									SynFillBlanksActivity.this);
							imageView.setScaleType(ScaleType.FIT_CENTER);
							imageView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
							ImageLoader.getInstance().displayImage(url, imageView);
							imageView.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated
									// method stub
									HtmlImage htmlImage=new HtmlImage();
									htmlImage.showDialog(SynFillBlanksActivity.this,url);
								}
							});
							synsingleLin.addView(imageView);
						}
					}
				}
			}else if (msg.what==2){

			}
		}
	};
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
		synsingleLin = (LinearLayout) findViewById(R.id.synsingleLin);
		select_title = (TextView) findViewById(R.id.syn_question_title);
		pandun = (TextView) findViewById(R.id.syn_question_pandun);
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
				iteIntent.setClass(SynFillBlanksActivity.this,
						SynSeletQuestionAnalyticalActivity.class);
				startActivity(iteIntent);
			}
		});
	}

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
		return R.layout.activity_syn_fill_blanks;
	}

}
