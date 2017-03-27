package com.shengliedu.parent.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceTool {
	public static int getAppBell(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("apptool",
				Context.MODE_PRIVATE);
		return preferences.getInt("bell", 0);
	}

	public static void setAppBell(Context context,int bell) {
		SharedPreferences preferences = context.getSharedPreferences("apptool",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt("bell", bell);
		editor.commit();
	}
	public static void RemoveUserInfo(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("User",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	public static String getChatTime(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("date",
				Context.MODE_PRIVATE);
		return preferences.getString("time", "");
	}

	public static void setChattime(Context context, String time) {
		SharedPreferences preferences = context.getSharedPreferences("date",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("time", time);
		editor.commit();
	}
	public static String getUserName(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("User",
				Context.MODE_PRIVATE);
		return preferences.getString("userName", "");
	}
	
	public static void setUserName(Context context, String userName) {
		SharedPreferences preferences = context.getSharedPreferences("User",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("userName", userName);
		editor.commit();
	}

	public static String getPassword(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("User",
				Context.MODE_PRIVATE);
		return preferences.getString("password", "");
	}

	public static void setPassword(Context context, String password) {
		SharedPreferences preferences = context.getSharedPreferences("User",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("password", password);
		editor.commit();
	}
	
	public static String getAppName(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("App",
				Context.MODE_PRIVATE);
		return preferences.getString("appname", "");
	}

	public static void setAppName(Context context, String appname) {
		SharedPreferences preferences = context.getSharedPreferences("App",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("appname", appname);
		editor.commit();
	}
	public static String getAppImage(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("App",
				Context.MODE_PRIVATE);
		return preferences.getString("image", "");
	}

	public static void setAppImage(Context context, String appimage) {
		SharedPreferences preferences = context.getSharedPreferences("App",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("appimage", appimage);
		editor.commit();
	}
	public static int getAppRunTime(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("App",
				Context.MODE_PRIVATE);
		return preferences.getInt("times", 0);
	}

	public static void setAppRunTime(Context context, int times) {
		SharedPreferences preferences = context.getSharedPreferences("App",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt("times", times);
		editor.commit();
	}
	public static String getAppWel(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("App",
				Context.MODE_PRIVATE);
		return preferences.getString("welcome", "");
	}

	public static void setAppWel(Context context, String welcome) {
		SharedPreferences preferences = context.getSharedPreferences("App",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("welcome", welcome);
		editor.commit();
	}
	public static String getShoolName(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("School",
				Context.MODE_PRIVATE);
		return preferences.getString("name", "");
	}

	public static void setShoolName(Context context, String name) {
		SharedPreferences preferences = context.getSharedPreferences("School",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("name", name);
		editor.commit();
	}
	public static String getShoolImage(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("School",
				Context.MODE_PRIVATE);
		return preferences.getString("image", "");
	}

	public static void setShoolImage(Context context, String image) {
		SharedPreferences preferences = context.getSharedPreferences("School",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("image", image);
		editor.commit();
	}
	public static String getShoolMessage(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("School",
				Context.MODE_PRIVATE);
		return preferences.getString("message", "");
	}

	public static void setShoolMessage(Context context, String message) {
		SharedPreferences preferences = context.getSharedPreferences("School",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("message", message);
		editor.commit();
	}
	public static String getUserBookPage(Context context,String user_bookId) {
		SharedPreferences preferences = context.getSharedPreferences("pbook",
				Context.MODE_PRIVATE);
		return preferences.getString(user_bookId, "");
	}
	
	public static void setUserBookPage(Context context,String user_bookId, String page) {
		SharedPreferences preferences = context.getSharedPreferences("pbook",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(user_bookId, page);
		editor.commit();
	}
}
