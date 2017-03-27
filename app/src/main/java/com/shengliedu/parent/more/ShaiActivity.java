package com.shengliedu.parent.more;

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
import com.shengliedu.parent.adapter.ShaiListAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.ShaiClass;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class ShaiActivity extends BaseActivity implements
		OnRefreshListener2<ListView> {
	private List<ShaiClass> dataList = new ArrayList<ShaiClass>();
	private PullToRefreshListView shaishai_list;
	private ShaiListAdapter adapter;
	private int page = 1;
	private String classroomId="";
	private String parentId;
	private boolean isPull = true;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		classroomId = App.childInfo.classroomId + "";
		parentId = App.userInfo.id + "";
		setBack();
		showTitle("晒晒晒");
		shaishai_list = (PullToRefreshListView) findViewById(R.id.shaishai_list);
		shaishai_list.setMode(Mode.BOTH);
		adapter = new ShaiListAdapter(this, dataList);
		shaishai_list.setAdapter(adapter);
		shaishai_list.setOnRefreshListener(this);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("classroomId", classroomId);
		map.put("parentId", parentId);
		map.put("pageNumber", page);
		doGet(Config1.getInstance().SHAISHAI(), map,new ResultCallback() {
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
	Handler handlerReq=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				JSONObject result=JSON.parseObject((String) msg.obj);
				List<ShaiClass> dataList1 = new ArrayList<ShaiClass>();
				dataList1=JSON.parseArray(result.getString("data"), ShaiClass.class);
				if (!isPull) {
					dataList.clear();
				}
				dataList.addAll(dataList1);
				adapter.notifyDataSetChanged();
				shaishai_list.onRefreshComplete();
			} else if (msg.what == 2) {
			}
		}
	};
	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_shaishai;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		page = 1;
		isPull = false;
		getDatas();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		page++;
		isPull = true;
		getDatas();
	}
}
