package com.shengliedu.parent.homework;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.PingjiaHomeworkAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class PingJiaHomeWorkActivity extends BaseActivity{
	private ListView listView;
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private String [] level={"A、优","B、良","C、中","D、差"};
	private PingjiaHomeworkAdapter adapter;
	private Button submit_pingjia;
	private String date;
	private String subjectId;
	private String studentId;
	private String comment;
	private int a=-1;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		studentId=App.childInfo.id+"";
		date=getIntent().getStringExtra("date");
		subjectId=getIntent().getStringExtra("subjectId");
		setBack();
		showTitle("评价作业");
		listView=getView(R.id.lv);
		submit_pingjia=getView(R.id.submit_pingjia);
		adapter = new PingjiaHomeworkAdapter(this,
				level, map);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				map.clear();
				map.put(arg2, true);
				a=arg2;
				adapter.notifyDataSetChanged();
			}
		});
		submit_pingjia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (a!=-1) {
					comment=(a+1)+"";
				}else {
					toast("请选择评价");
					return;
				}
				Log.v("tag", "comment="+comment);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("date",date);
				map.put("studentId",studentId);
				map.put("subjectId",subjectId);
				map.put("comment",comment);
				doPost(Config1.getInstance().PINGJIAHOMEWORK(), map,new ResultCallback() {
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
		});
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				toast("评价成功");
				setResult(213);
				finish();
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_pingjiahomework;
	}

}
