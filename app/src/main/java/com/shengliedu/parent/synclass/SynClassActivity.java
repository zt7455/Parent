package com.shengliedu.parent.synclass;

import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.shengliedu.parent.adapter.SynClassAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.SynQuestion2;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.HtmlImage;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SynClassActivity extends BaseActivity {
	private String date;
	private String questionTypeName;
	private String content_host;
	private int studentId;
	private int coursewareContentId;
	private int hourId;

	public TextView select_title, pandun, syn_question_head,syn_class_type,syn_class_note;
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private ListView listView;
	private SynClassAdapter adapter;
	private SynQuestion2 dataList = new SynQuestion2();
	private LinearLayout synsingleLin;

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
		listView = (ListView) findViewById(R.id.syn_question_lv);
		syn_class_type = (TextView) findViewById(R.id.syn_class_type);
		syn_class_note = (TextView) findViewById(R.id.syn_class_note);
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				dataList = JSON.parseObject(
						result.getString("data"), SynQuestion2.class);
				if (dataList != null) {
					// Spanned spanned =
					// Html.fromHtml(dataList.get(0).question.question,new
					// MyImageGetter(SynSingleActivity.this,select_title),null);
					select_title.setText(Html.fromHtml(HtmlImage.deleteSrc(dataList.content)));
					syn_class_note.setText("注释:"+"\n"+(TextUtils.isEmpty(dataList.note)?"无注释":Html.fromHtml(HtmlImage.deleteSrc(dataList.note))));
					// select_title.setMovementMethod(LinkMovementMethod.getInstance());
					String name = dataList.content;
					List<String> imgList = HtmlImage.getImgSrc(name);
					if (imgList.size() > 0) {
						for (int j = 0; j < imgList.size(); j++) {
							final String url = imgList.get(j);
							ImageView imageView = new ImageView(
									SynClassActivity.this);
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
									htmlImage.showDialog(SynClassActivity.this,url);
								}
							});
							synsingleLin.addView(imageView);
						}
					}
					adapter = new SynClassAdapter(
							SynClassActivity.this, dataList.option);
					listView.setAdapter(adapter);
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
		return R.layout.activity_syn_class;
	}

}
