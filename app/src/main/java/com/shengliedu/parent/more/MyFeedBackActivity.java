package com.shengliedu.parent.more;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.FeedBackAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.Beanfeedback;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.widght.CustomDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class MyFeedBackActivity extends BaseActivity {
	private ListView listView;
	private FeedBackAdapter adapter;
	private List<Beanfeedback> feedbackinfos = new ArrayList<Beanfeedback>();

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("我的建议");
		listView = (ListView) findViewById(R.id.feedback_list);
		adapter = new FeedBackAdapter(this, feedbackinfos);
		listView.setAdapter(adapter);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				CustomDialog.Builder customDialog = new CustomDialog.Builder(
						context);
				customDialog.setMessage("亲,是否删除该建议?");
				customDialog.setTitle("提示");
				customDialog.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									int which) {
								// 设置你的操作事项
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("id", feedbackinfos.get(arg2).id);
								doGet(Config1.getInstance().DELETEFEEDBACK(), map,
										new ResultCallback() {
											@Override
											public void onResponse(Call call, Response response, String json) {
												runOnUiThread(new Runnable() {
													@Override
													public void run() {
														dialog.dismiss();
													}
												});
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
						});
				customDialog.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				customDialog.create().show();

				return false;
			}
		});
	}
	Handler handlerReq=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				toast("删除建议成功");
				getDatas();
			} else if (msg.what == 2) {
			}else if (msg.what == 3) {
				JSONObject result=JSON.parseObject((String) msg.obj);
				List<Beanfeedback> feedbackinfos1 = new ArrayList<Beanfeedback>();
				feedbackinfos1 = JSON.parseArray(result.getString("data"),
						Beanfeedback.class);
				feedbackinfos.clear();
				feedbackinfos.addAll(feedbackinfos1);
				adapter.notifyDataSetChanged();
			}else if (msg.what == 4) {
			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", App.userInfo.id + "");
		doGet(Config1.getInstance().FEEDBACK(), map, new ResultCallback() {
			@Override
			public void onResponse(Call call, Response response, String json) {
				Message message=Message.obtain();
				message.what=3;
				message.obj=json;
				handlerReq.sendMessage(message);
			}

			@Override
			public void onFailure(Call call, IOException exception) {
				handlerReq.sendEmptyMessage(4);
			}
		});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_myfeedback;
	}

}
