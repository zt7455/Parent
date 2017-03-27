package com.shengliedu.parent.more.score;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.BeanWeak;
import com.shengliedu.parent.bean.BeanWeakDate;
import com.shengliedu.parent.bean.BeanWeakSummary;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.view.ZhuView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

@SuppressLint({ "ValidFragment", "NewApi" })
public class DuanBanFragment extends Fragment {
	public String semesterId = "";
	private String subjectId;
	private String studentId;
	private View view;
	private LinearLayout lay;
	private TextView label1,label2,label3,label4,label5,label6;
	private ArrayList<Integer> temp1 = new ArrayList<Integer>();
	private ArrayList<Integer> temp2 = new ArrayList<Integer>();
	private ArrayList<Integer> temp3 = new ArrayList<Integer>();
	private ArrayList<String> tittle = new ArrayList<String>();
	private ArrayList<String> vedioUrl = new ArrayList<String>();
	private List<BeanWeakSummary> summaries;
	private BaseActivity activity;
	public DuanBanFragment(BaseActivity activity,String subjectId, String studentId,
			String semesterId) {
		super();
		this.semesterId = semesterId;
		this.subjectId = subjectId;
		this.studentId = studentId;
		this.activity = activity;
	}

	public DuanBanFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_zhu, null);
		initView();
		initData();
		return view;
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				BeanWeak weak = JSON.parseObject(result.getString("data"), BeanWeak.class);
				if (weak != null) {
					List<BeanWeakDate> dates = weak.data;
					if (dates != null && dates.size() > 0) {
						for (int i = 0; i < dates.size(); i++) {
							temp1.add(dates.get(i).easy);
							temp2.add(dates.get(i).normal);
							temp3.add(dates.get(i).hard);
							tittle.add(dates.get(i).title);
							vedioUrl.add(dates.get(i).url);
						}
					}
					summaries = weak.summary;
					Log.v("TAG", "ss");
					initView();
				}
			}else if (msg.what==2){

			}
		}
	};
	private void initData() {
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("studentId", studentId);
		map1.put("semesterId", semesterId);
		map1.put("subjectId", subjectId);
		activity.doGet(Config1.getInstance().WEAKNESS(), map1, new ResultCallback() {
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

	private void initView() {
		if (summaries != null && summaries.size() > 0) {
			view.setVisibility(View.VISIBLE);
			lay = (LinearLayout) view.findViewById(R.id.lay);
			label1 = (TextView) view.findViewById(R.id.label1);
			label2 = (TextView) view.findViewById(R.id.label2);
			label3 = (TextView) view.findViewById(R.id.label3);
			label4 = (TextView) view.findViewById(R.id.label4);
			label5 = (TextView) view.findViewById(R.id.label5);
			label6 = (TextView) view.findViewById(R.id.label6);
			ZhuView ssvv = new ZhuView(activity, temp1, temp2, temp3,
					tittle,vedioUrl);
			LayoutParams lpp = new LayoutParams((tittle.size() + 1) * 200, 560);
			lpp.setMargins(60, 0, 0, 0);
			ssvv.setLayoutParams(lpp);
			Log.v("TAG", "w=" + lpp.width);
			lay.addView(ssvv);
			if (summaries!=null && summaries.size()==3) {
				label1.setText(summaries.get(0).name);
				label2.setText(summaries.get(0).content);
				label3.setText(summaries.get(1).name);
				label4.setText(summaries.get(1).content);
				label5.setText(summaries.get(2).name);
				label6.setText(summaries.get(2).content);
			}
		}else {
			view.setVisibility(View.INVISIBLE);
		}
	}
}
