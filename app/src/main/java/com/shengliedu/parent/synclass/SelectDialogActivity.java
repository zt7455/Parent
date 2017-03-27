package com.shengliedu.parent.synclass;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.view.SlidingSelectPicker;
import com.shengliedu.parent.view.SlidingSelectPicker.OnsSelectedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SelectDialogActivity extends BaseActivity {
	private SlidingSelectPicker cityPicker;
	private List<String> data = new ArrayList<String>();
	private int type = 0;
	private int activityId;
	private int coursewareContentId;
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				toast("评价成功");
				Intent intent = new Intent();
				intent.putExtra("data", (String) msg.obj);
				setResult(131, intent);
				finish();
			}else if (msg.what==2){

			}else if (msg.what==3){
				toast("评价成功");
				Intent intent = new Intent();
				intent.putExtra("data", (String) msg.obj);
				setResult(133, intent);
				finish();
			}else if (msg.what==4){

			}
		}
	};
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		cityPicker = (SlidingSelectPicker) findViewById(R.id.citypicker);

		type = getIntent().getIntExtra("type", 0);
		activityId = getIntent().getIntExtra("activityId", 0);
		switch (type) {
		case 11:
			for (int i = Config1.SCORE_STRINGS.length - 1; i >= 0; i--) {
				data.add(Config1.SCORE_STRINGS[i]);
			}
			cityPicker.setTitle("请评分");
			break;
		case 22:
			coursewareContentId = getIntent().getIntExtra("coursewareContentId", 0);
			for (int i = 0; i < Config1.LEVEL_STRINGS.length; i++) {
				data.add(Config1.LEVEL_STRINGS[i]);
			}
			cityPicker.setTitle("请评价");
			break;

		default:
			break;
		}

		cityPicker.setDatas(data);
		cityPicker.setOnSelectedListener(new OnsSelectedListener() {

			@Override
			public void selected(final String city) {
				if (type ==11) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("studentId", App.childInfo.id + "");
					map.put("activityId", activityId + "");
					map.put("score", city);
					doPost(Config1.getInstance().PINGJIATEACHER(), map,
							new ResultCallback() {
								@Override
								public void onResponse(Call call, Response response, String json) {
									Message message=Message.obtain();
									message.what=1;
									message.obj=city;
									handlerReq.sendMessage(message);
								}

								@Override
								public void onFailure(Call call, IOException exception) {
									handlerReq.sendEmptyMessage(2);
								}
							});
				}else if (type ==22) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("studentId", App.childInfo.id + "");
					map.put("parentId", App.userInfo.id + "");
					map.put("activityId", activityId + "");
					map.put("coursewareContentId", coursewareContentId+"");
					map.put("answer", city);
					doPost(Config1.getInstance().PINGJIACHILD(), map,
							new ResultCallback() {
								@Override
								public void onResponse(Call call, Response response, String json) {
									Message message=Message.obtain();
									message.what=3;
									message.obj=city;
									handlerReq.sendMessage(message);
								}

								@Override
								public void onFailure(Call call, IOException exception) {
									handlerReq.sendEmptyMessage(4);
								}
							});
				}
			}
		});
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_selectdialog;
	}

}
