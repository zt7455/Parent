package com.shengliedu.parent.more.jiatiao;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.JiaTiaoListAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.JiaTiaoClass;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class NoteActivity extends BaseActivity {
	private List<JiaTiaoClass> dataList = new ArrayList<JiaTiaoClass>();
	private ListView jiatiao_list;
	private String studentId, parent,semester;
	private JiaTiaoListAdapter adapter;
	Handler handlerReq=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				JSONObject result=JSON.parseObject((String) msg.obj);
				List<JiaTiaoClass> dataList1 = JSON.parseArray(
						result.getString("data"), JiaTiaoClass.class);
				dataList.clear();
				dataList.addAll(dataList1);
				adapter.notifyDataSetChanged();
			} else if (msg.what == 2) {
			}
		}
	};
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		studentId = App.childInfo.id + "";
		parent = App.userInfo.id + "";
		semester = App.loginInfo.currentSemesterId+ "";
		setBack();
		showTitle("请假条");
		jiatiao_list = (ListView) findViewById(R.id.note_list);
		adapter = new JiaTiaoListAdapter(this, dataList);
		jiatiao_list.setAdapter(adapter);
		jiatiao_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
					Intent intent = new Intent(NoteActivity.this,
							JiaTiaoDetailActivity.class);
					intent.putExtra("id", dataList.get(arg2).id);
					startActivity(intent);
			}
		});
		setRightImage(R.mipmap.add, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, QingJiaActivity.class);
				startActivityForResult(intent, 400);
			}
		});
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 400 && arg1 == 441) {
			getDatas();
		}
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("child", studentId);
		map.put("parent", parent);
		map.put("semester", semester);
		doGet(Config1.getInstance().NOTELIST(), map,new ResultCallback() {
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
		return R.layout.activity_note;
	}
}
