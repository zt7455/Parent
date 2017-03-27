package com.shengliedu.parent.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float,
 * Long类型的参数 同样调用getParam就能获取到保存在手机里面的数据
 * 
 * 
 */
public class SharedPreferencesUtils {
	public static final String TAG = "SharedPreferencesUtils";
	/**
	 * 保存在手机里面的文件名
	 */
	private static final String FILE_NAME = "share_date";

	public static void setUsername(Context context, String values) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("username", values);
		editor.commit();
	}

	public static String getUsername(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getString("username", null);
	}

	public static void setUserMobile(Context context, String values) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("usermobile", values);
		editor.commit();
	}

	public static String getUserMobile(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getString("usermobile", null);
	}

	public static void setCityJson(Context context, String values) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("cityjson", values);
		editor.commit();
	}

	public static String getCityJson(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getString("cityjson", null);
	}

	public static void setSearchKey(Context context, String values) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("searchkey", values);
		editor.commit();
	}

	public static String getSearchKey(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getString("searchkey", null);
	}

	public static void setPassword(Context context, String values) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("password", values);
		editor.commit();
	}

	public static String getU_Id(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getString("u_id", null);
	}

	public static void setU_Id(Context context, String values) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("u_id", values);
		editor.commit();
	}

	public static boolean isHxLogin(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean("isHxLogin", false);
	}

	public static void setHxLogin(Context context, boolean values) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("isHxLogin", values);
		editor.commit();
	}
	public static void setLoginType(Context context, String values) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("LoginType", values);
		editor.commit();
	}

	public static String getLoginType(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getString("LoginType", null);
	}
	public static String getPassword(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getString("password", null);
	}

	public static void clear(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

	public static boolean getisFirst(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean("isFirst", true);

	}

	public static void setisFirst(Context context, boolean values) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("isFirst", values);
		editor.commit();
	}

}