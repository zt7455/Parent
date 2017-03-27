package com.shengliedu.parent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.ChildListAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.BeanYunIp;
import com.shengliedu.parent.bean.ChildInfo;
import com.shengliedu.parent.bean.LoginInfo;
import com.shengliedu.parent.chat.constant.Constants;
import com.shengliedu.parent.chat.util.MyAndroidUtil;
import com.shengliedu.parent.chat.util.XmppLoadThread;
import com.shengliedu.parent.chat.xmpp.XmppConnection;
import com.shengliedu.parent.service.PushService;
import com.shengliedu.parent.util.AppValue;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.MD5Util;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.util.SharedPreferenceTool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SelectChild2Activity extends BaseActivity {
	private ListView child_list;
	private ChildListAdapter adapter;
	private List<ChildInfo> list;
	private int type;
	private String cid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public int setLayout() {
		return R.layout.acti_selechchild2;
	}

	public void initView() {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(SharedPreferenceTool
				.getUserName(SelectChild2Activity.this))) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.v("TAG",
							"name="
									+ SharedPreferenceTool
											.getUserName(SelectChild2Activity.this));
					Log.v("TAG",
							"pass="
									+ SharedPreferenceTool
											.getPassword(SelectChild2Activity.this));
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", SharedPreferenceTool
							.getUserName(SelectChild2Activity.this));
					map.put("password", MD5Util.getMD5String(SharedPreferenceTool
							.getPassword(SelectChild2Activity.this)).toUpperCase());
					doGet(Config1.MAIN_DC, map,new ResultCallback(){

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
			}, 1000);
		} else {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent ite = new Intent(SelectChild2Activity.this,
							LoginActivity.class);
					startActivity(ite);
					finish();
				}
			}, 2000);
		}
	}

	@Override
	public void initViews() {
		type = getIntent().getIntExtra("time", -1);
		cid = getIntent().getStringExtra("cid");
		Log.v("TAG", "cid="+cid);
		Log.v("TAG", "App.loginInfo="+App.loginInfo);
		if (App.loginInfo!=null){
			list = App.loginInfo.children;
			if ( list!=null && list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					if (cid !=null && cid.equals(list.get(i).id+"")) {
						App.childInfo=list.get(i);
						Intent intent = new Intent(SelectChild2Activity.this,
								MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("page", 0);
						startActivity(intent);
						finish();
					}
				}
			}else {
				initView();
			}
		}else {
			initView();
		}
	}

	@Override
	public void getDatas() {

	}

	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				BeanYunIp yunIp = JSON.parseObject(
						(String) msg.obj, BeanYunIp.class);
				if (yunIp != null) {
					App.beanSchoolInfo=yunIp;
					if (!TextUtils.isEmpty(yunIp.ip)) {
						Config1.getInstance().IP = "http://"+yunIp.ip + "/";
//									Config1.getInstance().IP = "http://192.168.1.207/";
						Config1.getInstance().CHATIP=yunIp.ip;
						if (yunIp.ip.contains(":20780")) {
							Constants.SERVER_HOST = "192.168.1.207";
							Constants.SERVER_NAME = "192.168.1.207";
						}
						Constants.init();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("name", SharedPreferenceTool
								.getUserName(SelectChild2Activity.this));
						map.put("password", SharedPreferenceTool
								.getPassword(SelectChild2Activity.this));
doPost(Config1.getInstance().LOGIN(), map, new ResultCallback() {
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
				}
			}else if (msg.what==2){

			}else if (msg.what==3){
				try {
					JSONObject o = (JSONObject) JSONObject
							.parse((String) msg.obj);
					if (o.getString("code")
							.equals("1000")) {
						App.loginInfo = JSON
								.parseObject(
										o.getString("data"),
										LoginInfo.class);
						if (App.userInfo.type==4){
							App.userInfo = App.loginInfo.user;
							loginAccount(App.userInfo.name, SharedPreferenceTool
									.getPassword(SelectChild2Activity.this));
					    }else {
					 	    toast("您不是家长！");
					    }

					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}else if (msg.what==4){
				Intent ite = new Intent(
						SelectChild2Activity.this,
						LoginActivity.class);
				startActivity(ite);
				finish();
			}
		}
	};
	private void loginAccount(final String userName, final String password) {
		new XmppLoadThread(this) {

			@Override
			protected Object load() {
				boolean isSuccess = XmppConnection.getInstance().login(
						userName, password);
				if (isSuccess) {
					Constants.USER_NAME = userName;
					Constants.PWD = password;
				}
				return isSuccess;
			}

			@Override
			protected void result(Object o) {
				boolean isSuccess = (Boolean) o;
				if (isSuccess) {
						MyAndroidUtil.editXmlByString(Constants.LOGIN_ACCOUNT,
								userName);
						MyAndroidUtil.editXmlByString(Constants.LOGIN_PWD, password);
						
						loadPush();
						Intent intent=new Intent(SelectChild2Activity.this,MainActivity.class);
						intent.putExtra("page", 3);
						startActivity(intent);
						finish();
//					Intent intent = new Intent(LoginActivity.this,
//							MainActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//							| Intent.FLAG_ACTIVITY_NEW_TASK);
//					startActivity(intent);
//					finish();
				} else {
//					Tool.initToast(SelectChild2Activity.this, getResources()
//							.getString(R.string.login_error));
				}
			}

		};
	}
	
	public void loadPush() {
		// TODO Auto-generated method stub
		Log.v("TAG", "loadPush");
		if (!AppValue.isServiceRunning(getApplicationContext(), "PushService")) {
			Log.v("TAG", "loadPush1");
			Intent intent = new Intent(
					getApplicationContext(),
					PushService.class);
			intent.putExtra("pid", App.userInfo.id);
			intent.putExtra("reqUrl", Config1.getInstance().REAL_TIME_NOTICE());
			startService(intent);
		}
	}
}
