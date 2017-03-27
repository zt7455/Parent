package com.shengliedu.parent.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.BeanYunIp;
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
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class WelcomeActivity extends BaseActivity {

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(SharedPreferenceTool
				.getUserName(WelcomeActivity.this))) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.v("TAG",
							"name="
									+ SharedPreferenceTool
											.getUserName(WelcomeActivity.this));
					Log.v("TAG",
							"pass="
									+ SharedPreferenceTool
											.getPassword(WelcomeActivity.this));
					Map<String, String> map = new HashMap<String, String>();
					map.put("platform", "1");
					map.put("devtype", "1");
					map.put("name", SharedPreferenceTool
							.getUserName(WelcomeActivity.this));
					map.put("password",
							MD5Util.getMD5String(
									SharedPreferenceTool
											.getPassword(WelcomeActivity.this))
									.toUpperCase());
					doGet(Config1.MAIN_DC, map, new ResultCallback() {
						@Override
						public void onResponse(Call call, Response response, String json) {
							if (!"{}".equals(json)){
								Message message=Message.obtain();
								message.what=1;
								message.obj=json;
								handlerReq.sendMessage(message);
							}else {
								handlerReq.sendEmptyMessage(2);
							}
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
					Intent ite = new Intent(WelcomeActivity.this,
							LoginActivity.class);
					startActivity(ite);
					finish();
				}
			}, 2000);
		}
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
						Config1.getInstance().IP = "http://"
								+ yunIp.ip + "/";
						// Config1.getInstance().IP =
						// "http://192.168.1.207/";
						Config1.getInstance().CHATIP = yunIp.ip;
						if (yunIp.ip.contains(":20780")) {
							Constants.SERVER_HOST = "192.168.1.207";
							Constants.SERVER_NAME = "192.168.1.207";
						}
						Log.v("TAG",
								"Config1.IP="
										+ Config1.getInstance().IP);
						Log.v("TAG", "Config1.LOGIN="
								+ Config1.getInstance().LOGIN());
						Constants.init();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("name", SharedPreferenceTool
								.getUserName(WelcomeActivity.this));
						map.put("password", SharedPreferenceTool
								.getPassword(WelcomeActivity.this));
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
								handlerReq.sendEmptyMessage(2);
							}
						});

					} else {
						toast("云里面此用户Ip为空");
					}
				} else {
					toast("云服务器无此数据");
				}
			}else if (msg.what==2){
				Intent intent = new Intent(WelcomeActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			}else if (msg.what==3){
				try {
					showRoundProcessDialogCancel();
					JSONObject o = (JSONObject) JSONObject
							.parse((String) msg.obj);
					if (o.getString("code")
							.equals("1000")) {
						App.loginInfo = JSON
								.parseObject(
										o.getString("data"),
										LoginInfo.class);
						App.userInfo = App.loginInfo.user;
						if (App.userInfo != null) {
							if (App.userInfo.type==4){
								Log.v("TAG",
										"loginuser:"
												+ App.userInfo.id);
								App.dealLoginInfo();
								loginAccount(App.userInfo.name,
										SharedPreferenceTool
												.getPassword(WelcomeActivity.this));
							 }else {
							   toast("您不是家长！");
					     	}
						}
					} else {
						toast(o.getString("message"));
						Intent ite = new Intent(
								WelcomeActivity.this,
								LoginActivity.class);
						startActivity(ite);
						finish();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					// callBack.onFailure("系统或网络错误");
					Intent intent = new Intent(
							WelcomeActivity.this,
							LoginActivity.class);
					startActivity(intent);
					finish();
				}
			}else if (msg.what==4){

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
		return R.layout.activity_welcome;
	}

	private void loginAccount(final String userName, final String password) {
		new XmppLoadThread(this) {

			@Override
			protected Object load() {
				Log.v("TAG", "userName=" + userName);
				Log.v("TAG", "password=" + password);
				boolean isSuccess = XmppConnection.getInstance().login(
						userName, password);
				Log.v("TAG", "isSuccess=" + isSuccess);
				if (isSuccess) {
					Constants.USER_NAME = userName;
					Constants.PWD = password;
				}
				return isSuccess;
			}

			@Override
			protected void result(Object o) {
				boolean isSuccess = (Boolean) o;
				App.chatLogin = isSuccess;
				MyAndroidUtil
						.editXmlByString(Constants.LOGIN_ACCOUNT, userName);
				MyAndroidUtil.editXmlByString(Constants.LOGIN_PWD, password);
				loadPush();
				if (App.loginInfo.children != null
						&& App.loginInfo.children.size() > 0) {
					if (App.loginInfo.children.size() == 1) {
						App.childInfo = App.loginInfo.children.get(0);
						Intent intent = new Intent(WelcomeActivity.this,
								MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						finish();
					} else {
						Intent intent = new Intent(WelcomeActivity.this,
								SelectChildActivity.class);
						intent.putExtra("time", 0);
						startActivityByAniamtion(intent);
						finishActivityByAniamtion();
					}
				} else {
					toast("您的账号没有关联孩子");
				}
			}

		};
	}

	public void loadPush() {
		// TODO Auto-generated method stub
		Log.v("TAG", "loadPush");
		if (!AppValue.isServiceRunning(getApplicationContext(),
				"com.shengliedu.parent.service.PushService")) {
			Log.v("TAG", "loadPush1");
			Intent intent = new Intent(getApplicationContext(),
					PushService.class);
			intent.putExtra("pid", App.userInfo.id);
			intent.putExtra("reqUrl", Config1.getInstance().REAL_TIME_NOTICE());
			startService(intent);
		} else {
			PushService.pid = App.userInfo.id;
			PushService.reqUrl = Config1.getInstance().REAL_TIME_NOTICE();
		}
	}

}
