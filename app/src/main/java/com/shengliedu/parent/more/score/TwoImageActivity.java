package com.shengliedu.parent.more.score;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.DianWeiAdapter;
import com.shengliedu.parent.adapter.HorizontalListViewAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.BeanExamScore;
import com.shengliedu.parent.bean.BeanLeiDa;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.view.LDView;
import com.shengliedu.parent.view.NoScrollListview;
import com.shengliedu.parent.widght.HorizontalListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class TwoImageActivity extends BaseActivity {
	private String studentId;
	private int examId;
	private RadioGroup mGroup;
	private HorizontalListView horizontalListView;
	private HorizontalListViewAdapter horizontalListViewAdapter;
	private NoScrollListview noScrollListview;
	private DianWeiAdapter dianWeiAdapter;
	private List<BeanLeiDa> dataList = new ArrayList<BeanLeiDa>();
	private List<BeanExamScore> dianweiData = new ArrayList<BeanExamScore>();
	private HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private String[] subjectName;
	private double[] highestScore;
	private double[] personalScore;
	private double[] averageScore;
	private LDView ldView;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		studentId = App.childInfo.id + "";
		examId = getIntent().getIntExtra("examId", -1);
		setBack();
		initView();
	}

	private void initView() {
		mGroup = (RadioGroup) findViewById(R.id.rg);

		noScrollListview = (NoScrollListview) findViewById(R.id.noScrollListview);
		noScrollListview.setFocusable(false);
		dianWeiAdapter = new DianWeiAdapter(this, dianweiData);
		noScrollListview.setAdapter(dianWeiAdapter);

		ldView = (LDView) findViewById(R.id.ldview);
		showTitle(getIntent().getStringExtra("examName"));
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				List<BeanLeiDa> dataList1 = new ArrayList<BeanLeiDa>();
				dataList1 = JSON.parseArray(result.getString("data"),
						BeanLeiDa.class);
				dataList.clear();
				dataList.addAll(dataList1);
				for (int i = 0; i < dataList.size(); i++) {
					RadioButton button = (RadioButton) LayoutInflater.from(TwoImageActivity.this)
							.inflate(R.layout.radio_without_button, null);
					button.setText(dataList.get(i).subjectName);
					// button.setTag(i);
					button.setId(i);
					// button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 20, 0);
					mGroup.addView(button);
					if (i==0) {
						button.setChecked(true);
					}
				}
				mGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						dianweiData.clear();
						dianweiData.addAll(dataList.get(arg1).scoreList);
						Log.v("TAG", "dianweiData=" + dataList.get(arg1).subjectName);
						dianWeiAdapter.notifyDataSetChanged();
					}
				});
				dianweiData.clear();
				dianweiData.addAll(dataList.get(0).scoreList);
				dianWeiAdapter.notifyDataSetChanged();
				if (dataList != null && dataList.size() > 0) {
					subjectName = new String[dataList.size()];
					highestScore = new double[dataList.size()];
					personalScore = new double[dataList.size()];
					averageScore = new double[dataList.size()];
					for (int i = 0; i < dataList.size(); i++) {
						subjectName[i] = dataList.get(i).subjectName;
						highestScore[i] = dataList.get(i).highestScore;
						personalScore[i] = dataList.get(i).personalScore;
						averageScore[i] = dataList.get(i).averageScore;
					}
					ldView.setAllDate(subjectName, averageScore, highestScore,
							personalScore);
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
		map1.put("examId", examId);
		doGet(Config1.getInstance().RADARDATA(), map1, new ResultCallback() {
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
		return R.layout.activity_two_image;
	}

}
