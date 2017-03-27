package com.shengliedu.parent.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	private static final int SECOND = 1000;
	private static final int MINUTE = 60 * SECOND;
	private static final int HOUR = 60 * MINUTE;
	private static final int DAY = 24 * HOUR;

	public static String parseTime(long ms) {
		StringBuffer text = new StringBuffer("");
		if (ms > DAY) {
			text.append(ms / DAY).append("天");
			ms %= DAY;
		}
		if (ms > HOUR) {
			text.append(ms / HOUR).append("时");
			ms %= HOUR;
		}
		if (ms > MINUTE) {
			text.append(ms / MINUTE).append("分");
			ms %= MINUTE;
		}
		if (ms > SECOND) {
			text.append(ms / SECOND).append("秒");
			ms %= SECOND;
		}

		return text.toString();
	}

	public static String parseDate(long date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
		return simpleDateFormat.format(new Date(date));
	}

	public static String parseDate2(long date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		return simpleDateFormat.format(new Date(date));
	}

	public static String parseDateToBars(long date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
		return simpleDateFormat.format(new Date(date));
	}
}
