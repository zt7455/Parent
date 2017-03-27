package com.shengliedu.parent.chat.util;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;

import com.shengliedu.parent.R;
import com.shengliedu.parent.activity.MainActivity;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.chat.constant.Constants;
import com.shengliedu.parent.chat.dao.NewMsgDbHelper;
import com.shengliedu.parent.chat.view.expression.ExpressionUtil;

public class MyAndroidUtil {
	private static Notification myNoti = new Notification();
	/**
	 * @param context
	 * @param title
	 * @param message
	 * @param icon
	 * @param okBtn
	 * 没有取消功能的了
	 */
	public static void showDialog(Context context ,String title,String message,int icon,DialogInterface.OnClickListener okBtn){
		new AlertDialog.Builder(context)
		.setTitle(title)
		.setIcon(icon)
		.setMessage(message)
		.setPositiveButton("确定",okBtn)
		.setNegativeButton("返回", null).show();
	}
	
	/**
	 * 修改缓存
	 * @param name     一般都name+   actid、或者userId
	 * @param object        要保存的
	 */
	public static void editXml(String name,Object object) {
		Editor editor = App.sharedPreferences.edit();
		if (App.sharedPreferences.getString(name, null) != null) {
			editor.remove(name);
		}
		editor.putString(name, JsonUtil.objectToJson(object));
		editor.commit();
	}
	
	
	/**
	 * 修改缓存
	 * @param name     一般都name+   actid、或者userId
	 * @param result        要保存的
	 */
	public static void editXmlByString(String name,String result) {
		Editor editor = App.sharedPreferences.edit();
		if (App.sharedPreferences.getString(name, null) != null) {
			editor.remove(name);
		}
		editor.putString(name, result);
		editor.commit();
	}
	
	/**
	 * 修改缓存
	 * @param name     一般都name+   actid、或者userId
	 * @params true or fasle        要保存的
	 */
	public static void editXml(String name,boolean is) {
		Editor editor = App.sharedPreferences.edit();
		editor.putBoolean(name, is);
		editor.commit();
	}

	
	public static void removeXml(String name){
		Editor editor = App.sharedPreferences.edit();
		editor.remove(name);
		editor.commit();
	}
	
	public static void clearNoti(){
		myNoti.number = 0;
		NotificationManager manger = (NotificationManager)App.getInstance()
				.getSystemService(Service.NOTIFICATION_SERVICE);
		manger.cancelAll();   
	}
	
	public static void showNoti(String notiMsg){
		//android推送
		Notification.Builder builder = new Notification.Builder(App.getInstance());//新建Notification.Builder对象
		if(notiMsg.contains(Constants.SAVE_IMG_PATH))
			builder.setTicker("[图片]");
		else if(notiMsg.contains(Constants.SAVE_SOUND_PATH))
			builder.setTicker("[语音]");
		else if(notiMsg.contains("[/g0"))
			builder.setTicker("[动画表情]");
		else if(notiMsg.contains("[/f0"))  //适配表情
			builder.setTicker(ExpressionUtil.getText(App.getInstance(), StringUtil.Unicode2GBK(notiMsg)));
		else if(notiMsg.contains("[/a0"))
			builder.setTicker("[位置]");
		else{
			builder.setTicker(notiMsg);
		}
		
		Intent intent = new Intent();   //要跳去的界面
		intent.putExtra("page", 1);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(App.getInstance(), MainActivity.class);
		
		NotificationManager mNotificationManager = 
	    		(NotificationManager) App.getInstance().getSystemService(Service.NOTIFICATION_SERVICE);
		PendingIntent appIntent = PendingIntent.getActivity(App.getInstance(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setSmallIcon(R.mipmap.ic_launcher);
		builder.setContentIntent(appIntent);
		builder.setContentTitle(App.getInstance().getString(R.string.app_name));
		myNoti=builder.getNotification();
		myNoti.flags = Notification.FLAG_SHOW_LIGHTS|Notification.FLAG_AUTO_CANCEL;  //闪光灯
		myNoti.ledARGB= 0xff00ff00;           //绿色
		myNoti.number = NewMsgDbHelper.getInstance(App.getInstance()).getMsgCount();
		
		if (App.sharedPreferences.getBoolean("isShake", true)) {
			myNoti.defaults = Notification.DEFAULT_VIBRATE; // 震动
		}
		if (App.sharedPreferences.getBoolean("isSound", true)) {
			myNoti.defaults = Notification.DEFAULT_SOUND; // 响铃
		}
//		myNoti.setLatestEventInfo(App.getInstance(), App.getInstance().getString(R.string.app_name), myNoti.tickerText, appIntent);
		mNotificationManager.notify(1000, myNoti);
	}
}
