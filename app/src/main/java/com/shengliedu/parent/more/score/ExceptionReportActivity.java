package com.shengliedu.parent.more.score;

import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.ExceptionAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.BeanExceptionReport;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class ExceptionReportActivity extends BaseActivity implements
OnRefreshListener2<ListView> {
	private PullToRefreshListView school_noti_list;
	private List<BeanExceptionReport> notifies = new ArrayList<BeanExceptionReport>();
	private ExceptionAdapter adapter;
	private int page = 1;
	private boolean pull;

	private String studentId = "";
	private String date = "";
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("异动报告");
		studentId = App.childInfo.id + "";
		date = App.loginInfo.date;
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		school_noti_list = (PullToRefreshListView) findViewById(R.id.school_noti_list);
		school_noti_list.setMode(Mode.BOTH);
		adapter = new ExceptionAdapter(this, notifies);
		school_noti_list.setAdapter(adapter);
		school_noti_list.setOnRefreshListener(this);
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				List<BeanExceptionReport> notifies1 = new ArrayList<BeanExceptionReport>();
				notifies1 = JSON.parseArray(result.getString("data"), BeanExceptionReport.class);
				if (pull) {
					notifies.clear();
				}
				notifies.addAll(notifies1);
				adapter.notifyDataSetChanged();
				school_noti_list.onRefreshComplete();
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("studentId", studentId);
		map.put("currentDate", date);
		map.put("pageNumber", page);
		doGet(Config1.getInstance().EXCEPTION(), map, new ResultCallback() {
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
		return R.layout.activity_exception_report;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		pull = true;
		page = 1;
		getDatas();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		pull = false;
		page++;
		getDatas();
	}
}
