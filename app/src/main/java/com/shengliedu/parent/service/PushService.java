package com.shengliedu.parent.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.R;
import com.shengliedu.parent.activity.SelectChild2Activity;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.PushData;
import com.shengliedu.parent.bean.PushData1;
import com.shengliedu.parent.util.SharedPreferenceTool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PushService extends Service {
	// 获取消息线程
	private OkHttpClient okHttpClient;

	// 家长的id
	public static String pid;
	// 请求的地址
	public static String reqUrl;

	// 停止发送请求的按钮
	private boolean stopSend;

	// 存储获取的推送列表
	List<PushData> pushDatas = new ArrayList<PushData>();

	// 点击查看
	private PendingIntent pi = null;

	// 通知栏消息
	private int notificationId = 1000;
	private Notification notification = null;
	private NotificationManager manager = null;

	private Handler handler = new Handler();

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		okHttpClient=new OkHttpClient();
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// 初始化
		if (intent != null) {
			pid = intent.getStringExtra("pid");
			reqUrl = intent.getStringExtra("reqUrl");
			Log.v("TAG", "onStartCommand:" + pid + "--" + reqUrl);
			getServerMessage();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
					if (!stopSend) {
						String result = (String) msg.obj;
						if (!TextUtils.isEmpty(result)) {
							JSONObject jsonObject = JSON
									.parseObject(result);
							if (jsonObject.getString("code").equals("1000")) {
								pushDatas = JSON.parseArray(
										jsonObject.getString("data"),
										PushData.class);
								dealPushList(pushDatas);
							} else {
								// 继续请求
							}
						}
						handler.postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								getServerMessage();
							}
						}, 5000);
					}

			}else if (msg.what==2){
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						getServerMessage();
					}
				}, 5000);
			}
		}
	};
	/**
	 * 从服务器端获取消息
	 * 
	 */
	private void getServerMessage() {
		// TODO Auto-generated method stub
		Log.v("TAG", ":::" + reqUrl + pid);
		Request request = new Request.Builder().url(reqUrl + pid).build();
		okHttpClient.newCall(request).enqueue(new Callback(){
			@Override
			public void onFailure(Call call, IOException e) {
				handlerReq.sendEmptyMessage(2);
			}

			@Override
			public void onResponse(Call call, Response response) {

				try {
					Message message=Message.obtain();
					message.what=1;
					message.obj=response.body().string();
					handlerReq.sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void dealPushList(List<PushData> pushDatas) {
		// TODO Auto-generated method stub
		if (pushDatas != null && pushDatas.size() > 0) {
			for (int i = 0; i < pushDatas.size(); i++) {
				String cname = pushDatas.get(i).realname;
				int cid = pushDatas.get(i).id;
				List<PushData1> messages = pushDatas.get(i).msTypeArr;
				if (messages != null && messages.size() > 0) {
					for (int j = 0; j < messages.size(); j++) {
						String ms = messages.get(j).ms;
						int type = messages.get(j).type;
						int subType = messages.get(j).subType;
						showNotification(cname, type, subType, ms, cid);
					}
				}
			}
		}
	}

	private void showNotification(String realName, int type, int subType,
			String ms, int cid) {
		// TODO Auto-generated method stub
		Notification.Builder builder = new Notification.Builder(App.getInstance());//新建Notification.Builder对象
		builder.setSmallIcon(R.mipmap.ic_launcher);
		String typeStr = "";
		if (type == 1) {
			typeStr = "出勤情况";
			if (SharedPreferenceTool.getAppBell(getApplicationContext()) == 0) {
				builder.setSound(Uri.parse("android.resource://"
						+ getPackageName() + "/" + R.raw.dida));
			} else if (SharedPreferenceTool.getAppBell(getApplicationContext()) == 1) {
				if (subType == 1) {
					builder.setSound(Uri.parse("android.resource://"
							+ getPackageName() + "/" + R.raw.chidao));
				} else if (subType == 2) {
					builder.setSound(Uri.parse("android.resource://"
							+ getPackageName() + "/" + R.raw.zaotui));
				} else if (subType == 3) {
					builder.setSound(Uri.parse("android.resource://"
							+ getPackageName() + "/" + R.raw.queqin));
				}else if (subType == 4) {

				}
			} else if (SharedPreferenceTool.getAppBell(getApplicationContext()) == 2) {
				builder.setSound(Uri.parse("android.resource://"
						+ getPackageName() + "/" + R.raw.silence));
			}

		} else if (type == 2) {
			typeStr = "课堂回答表现";
			if (SharedPreferenceTool.getAppBell(getApplicationContext()) == 0) {
				builder.setSound(Uri.parse("android.resource://"
						+ getPackageName() + "/" + R.raw.dida));
			} else if (SharedPreferenceTool.getAppBell(getApplicationContext()) == 1) {
				if (subType == 1) {
					builder.setSound(Uri.parse("android.resource://"
							+ getPackageName() + "/" + R.raw.feichanghao));
				} else if (subType == 2) {
					builder.setSound(Uri.parse("android.resource://"
							+ getPackageName() + "/" + R.raw.henhao));
				} else if (subType == 3) {
					builder.setSound(Uri.parse("android.resource://"
							+ getPackageName() + "/" + R.raw.good));
				}
			} else if (SharedPreferenceTool.getAppBell(getApplicationContext()) == 2) {
				builder.setSound(Uri.parse("android.resource://"
						+ getPackageName() + "/" + R.raw.silence));
			}
		} else if (type == 3) {
			typeStr = "在校纪律情况";
			if (SharedPreferenceTool.getAppBell(getApplicationContext()) == 0) {
				builder.setSound(Uri.parse("android.resource://"
						+ getPackageName() + "/" + R.raw.dida));
			} else if (SharedPreferenceTool.getAppBell(getApplicationContext()) == 1) {
				if (subType == 1) {
					builder.setSound(Uri.parse("android.resource://"
							+ getPackageName() + "/" + R.raw.jianghua));
				} else if (subType == 2) {
					builder.setSound( Uri.parse("android.resource://"
							+ getPackageName() + "/" + R.raw.daoluan));
				} else if (subType == 3) {
					builder.setSound(Uri.parse("android.resource://"
							+ getPackageName() + "/" + R.raw.kaixiaochai));
				}
			} else if (SharedPreferenceTool.getAppBell(getApplicationContext()) == 2) {
				builder.setSound( Uri.parse("android.resource://"
						+ getPackageName() + "/" + R.raw.silence));
			}
		}
		// 自定义声音 声音文件放在ram目录下，没有此目录自己创建一个
		// notification.sound=Uri.parse("android.resource://" + getPackageName()
		// + "/" +R.raw.mm);
		// notification.defaults = Notification.DEFAULT_ALL; //
		// 使用默认设置，比如铃声、震动、闪灯
		Intent intent = new Intent(getApplicationContext(),
				SelectChild2Activity.class);
		intent.putExtra("cid", cid + "");
		pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
		builder.setContentIntent(pi);
		builder.setContentTitle(realName
				+ "新的" + typeStr);
		builder.setContentTitle(ms);
		notification=builder.getNotification();
		notification.flags = Notification.FLAG_AUTO_CANCEL; // 但用户点击消息后，消息自动在通知栏自动消失

//		notification.setLatestEventInfo(getApplicationContext(), realName
//				+ "新的" + typeStr, ms, pi);

		// 将消息推送到状态栏
		manager.notify(notificationId, notification);
		notificationId++;
	}
}
