package com.shengliedu.parent.chat.activitys;

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
import com.shengliedu.parent.bean.BeanNotify;
import com.shengliedu.parent.chat.adapter.NotifyAdapter;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SchoolNotifyActivity extends BaseActivity implements
		OnRefreshListener2<ListView> {
	private PullToRefreshListView school_noti_list;
	private List<BeanNotify> notifies = new ArrayList<BeanNotify>();
	private NotifyAdapter adapter;
	private int page = 1;
	private boolean pull;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("学校通知");
		school_noti_list = (PullToRefreshListView) findViewById(R.id.school_noti_list);
		school_noti_list.setMode(Mode.BOTH);
		adapter = new NotifyAdapter(this, notifies);
		school_noti_list.setAdapter(adapter);
		school_noti_list.setOnRefreshListener(this);
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				List<BeanNotify> notifies1 = new ArrayList<BeanNotify>();
				notifies1 = JSON.parseArray(result.getString("data"), BeanNotify.class);
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
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page + "");
		doGet(Config1.getInstance().NOTIFICATION(), map,
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
		return R.layout.activity_schoolnotify;
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
		pull = true;
		page++;
		getDatas();
	}
}
