package com.shengliedu.parent.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.BeanYunIp;
import com.shengliedu.parent.bean.LoginInfo;
import com.shengliedu.parent.chat.constant.Constants;
import com.shengliedu.parent.chat.util.MyAndroidUtil;
import com.shengliedu.parent.chat.util.Tool;
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

public class LoginActivity extends BaseActivity implements OnClickListener {
	private EditText edit_name, edit_pass;
	private Button btn_login;
private String name,pass;
	// private MessageDao messageDao;
	// private RecentDao recentDao;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		showTitle("登录");
		// messageDao=new MessageDao(LoginActivity.this);
		// recentDao=new RecentDao(LoginActivity.this);
		edit_name = (EditText) findViewById(R.id.edit_name);
		edit_pass = (EditText) findViewById(R.id.edit_pass);
		// edit_name.setText("70");
		// edit_pass.setText("123456");
		btn_login = (Button) findViewById(R.id.btn_logining);
		btn_login.setOnClickListener(this);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_login;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_logining:
			name = edit_name.getText().toString();
			pass = edit_pass.getText().toString();
			if (TextUtils.isEmpty(name)) {
				Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT)
						.show();
				return;
			} else if (TextUtils.isEmpty(pass)) {
				Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT)
						.show();
				return;
			} else {
				if (!TextUtils.isEmpty(SharedPreferenceTool
						.getUserName(LoginActivity.this))) {
					if (name.equals(SharedPreferenceTool
							.getUserName(LoginActivity.this))) {
					} else {
						// int md=messageDao.deleteDatebase();
						// int rd=recentDao.deleteDatebase();
						// Log.v("TAG", "md="+md);	
						// Log.v("TAG", "rd="+rd);
					}
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", name);
				map.put("platform", "1");
				map.put("devtype", "1");
				map.put("password", MD5Util.getMD5String(pass).toUpperCase());
				//
				doGet(Config1.MAIN_DC, map, new ResultCallback() {
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
			break;

		default:
			break;
		}
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				BeanYunIp yunIp = JSON.parseObject((String) msg.obj,BeanYunIp.class);
				if (yunIp != null) {
					App.beanSchoolInfo=yunIp;
					if (!TextUtils.isEmpty(yunIp.ip)) {
						Log.v("TAG", "yunIp.ip=" + yunIp.ip);
						Config1.getInstance().IP = "http://" + yunIp.ip
								+ "/";
						// Config1.getInstance().IP =
						// "http://192.168.1.207/";
						// Config1.getInstance().IP =
						// "http://192.168.1.50/";
						Config1.getInstance().CHATIP = yunIp.ip;
						if (yunIp.ip.contains(":20780")) {
							Constants.SERVER_HOST = "192.168.1.207";
							Constants.SERVER_NAME = "192.168.1.207";
						}
						Constants.init();
						// Config1.getInstance().CHATIP="192.168.1.50";
						Log.v("TAG","Config1.IP="
								+ Config1.getInstance().IP);
						Log.v("TAG", "Config1.LOGIN="
								+ Config1.getInstance().LOGIN());
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("name", name);
						map.put("password", pass);
						doPost(Config1.getInstance().LOGIN(), map,
								new ResultCallback() {
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
					} else {
						toast("云里面此用户Ip为空");
					}
				} else {
					toast("云服务器无此数据");
				}
			}else if (msg.what==2){

			}else if (msg.what==3){
				JSONObject result=JSON.parseObject((String) msg.obj);
				App.loginInfo = JSON.parseObject(
						result.getString("data"),
						LoginInfo.class);
				App.userInfo = App.loginInfo.user;
				if (App.userInfo != null) {
					Log.v("TAG", "loginuser:"
							+ App.userInfo.id);
					if (App.userInfo.type == 4) {
						App.dealLoginInfo();
						SharedPreferenceTool
								.setUserName(
										LoginActivity.this,
										App.userInfo.name);
						SharedPreferenceTool
								.setPassword(
										LoginActivity.this,
										pass);
						Log.v("TAG", "Chat:name="
								+ name + ",pass="
								+ pass);
						loginAccount(App.userInfo.name, pass);
					}else {
						toast("您不是家长！");
					}
				}
			}else if (msg.what==4){

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
				App.chatLogin=isSuccess;
				if (isSuccess) {
					
				} else {
					Tool.initToast(LoginActivity.this, getResources()
							.getString(R.string.login_error));
				}
				MyAndroidUtil.editXmlByString(Constants.LOGIN_ACCOUNT,
						userName);
				MyAndroidUtil
						.editXmlByString(Constants.LOGIN_PWD, password);
				loadPush();
				if (App.loginInfo.children!=null && App.loginInfo.children.size()>0) {
					if (App.loginInfo.children.size() == 1) {
						App.childInfo = App.loginInfo.children.get(0);
						Intent intent = new Intent(LoginActivity.this,
								MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						finish();
					}else {
						Intent intent = new Intent(LoginActivity.this,
								SelectChildActivity.class);
						intent.putExtra("time", 0);
						startActivityByAniamtion(intent);
						finishActivityByAniamtion();
					}
				}else {
					toast("您的账号没有关联孩子");
				}
			}

		};
	}


	public void loadPush() {
		// TODO Auto-generated method stub
		if (!AppValue.isServiceRunning(getApplicationContext(),
				"com.shengliedu.parent.service.PushService")) {
			Intent intent = new Intent(getApplicationContext(),
					PushService.class);
			intent.putExtra("pid", App.userInfo.id);
			intent.putExtra("reqUrl", Config1.getInstance().REAL_TIME_NOTICE());
			startService(intent);
		}else {
			PushService.pid=App.userInfo.id;
			PushService.reqUrl=Config1.getInstance().REAL_TIME_NOTICE();
		}
	}
	
	
}
