package com.shengliedu.parent.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

public class BaseTools {

	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/** 获取屏幕的高度 */
	public final static int getWindowsHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/*
	 * map转化为json
	 */
	public static String Map2Json(Map<String, Object> param) {
		Object paraValue;
		StringBuilder sBuilder = new StringBuilder("");
		Set<String> keySet = param.keySet();
		int count = keySet.size();
		int i = 0;
		if (count > 0) {
			sBuilder.append("{");
			for (String key : keySet) {
				i++;
				sBuilder.append("\"");
				sBuilder.append(key);
				sBuilder.append("\"");
				paraValue = param.get(key);
				sBuilder.append(":");
				sBuilder.append("\"");
				sBuilder.append(String.valueOf(paraValue));
				sBuilder.append("\"");
				if (i != count) {
					sBuilder.append(",");
				}

			}
			// String temp =
			// sBuilder.toString().substring(0,sBuilder.length()-1);
			// sBuilder.replace(sBuilder.length()-1, sBuilder.length()-1, "");
			sBuilder.append("}");
		}
		return sBuilder.toString();
	}

	/*
	 * Bitmap转化为二进制流
	 */
	public static byte[] getBitmapByte(Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	/**
	 * 用来获取手机拨号上网（包括CTWAP和CTNET）时由PDSN分配给手机终端的源IP地址。
	 * 
	 * @return
	 */
	public static String getPsdnIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						// if (!inetAddress.isLoopbackAddress() && inetAddress
						// instanceof Inet6Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

	/*
	 * 截取字符串
	 */
	public static List<String> string2list(String message, String sp) {
		List<String> tempList = new ArrayList<String>();
		String temp = "";
		for (int i = 0; i < message.length(); i++) {
			if (message.substring(i, i + 1).equals(sp)) {
				tempList.add(temp);
				temp = "";
			} else {
				temp = temp + message.substring(i, i + 1);
			}
		}
		if (temp.equals("") == false) {
			tempList.add(temp);
			temp = "";
		}
		return tempList;

	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		// 验证邮箱的正则表达式
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");

		Matcher matcher = pattern.matcher(email);

		return matcher.matches();
		// return true;// 邮箱名合法，返回true
		// return false;// 邮箱名不合法，返回false
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail2(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证固定电话
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean checkPhone(String phone) {
		String cString = "^\\d{3}-?\\d{8}|\\d{4}-?\\d{8}$";
		Pattern p = Pattern.compile(cString);
		Matcher matcher = p.matcher(phone);
		return matcher.matches();
	}

	/**
	 * 检测手机号是否合法
	 */
	public static boolean isMobileNO(String mobiles) {
		boolean flag = false;
		try {

			Pattern p = Pattern.compile("^0?(13[0-9]|15[012356789]|18[0123456789]|14[57])[0-9]{8}$");

			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public static boolean checkQQ(String QQ) {
		String ss = "^\\d{5,10}$";
		Pattern p = Pattern.compile(ss);
		Matcher matcher = p.matcher(QQ);
		return matcher.matches();
	}

	/**
	 * 时间格式化 yyyy-mm-dd ----> yyyy/MM/dd
	 * 
	 * @param time2
	 * @return
	 */
	public static String timeFormat(String time2) {
		Date date = null;
		String newD = null;
		try {
			if (time2 != null && !"".equals(time2)) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				date = format.parse(time2);
				format = new SimpleDateFormat("yyyy/MM/dd");
				newD = format.format(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newD;
	}

	// 设置过滤字符函数(过滤掉我们不需要的字符)

	public static String stringFilter(String str) throws PatternSyntaxException {
		// String regEx = "[/\\:*?<>|\"\n\t]";
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\s]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("");

	}

	// 快速点击工具类
	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
