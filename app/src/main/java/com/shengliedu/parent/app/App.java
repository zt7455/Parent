package com.shengliedu.parent.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.shengliedu.parent.bean.BeanYunIp;
import com.shengliedu.parent.bean.ChildInfo;
import com.shengliedu.parent.bean.ClassRoom;
import com.shengliedu.parent.bean.GradeSubjectArr;
import com.shengliedu.parent.bean.IdName;
import com.shengliedu.parent.bean.LoginInfo;
import com.shengliedu.parent.bean.UserInfo;
import com.shengliedu.parent.chat.constant.Constants;
import com.shengliedu.parent.chat.constant.ImgConfig;
import com.shengliedu.parent.chat.xmpp.XmppConnection;
import com.shengliedu.parent.util.Config;
import com.tencent.smtt.sdk.QbSdk;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author zt10 This is app about shengliedu education system
 */
public class App extends Application implements UncaughtExceptionHandler {
	private static App _pussApplication;
	private static int MEM_CACHE_SIZE = 2 * 1024 * 1024;
	public static int APP_RUN_TIMES = 0;

	public static double lat = 23.117055306224895;
	public static double lon = 113.2759952545166;

	public static App getInstance() {
		return _pussApplication;
	}

	public static SharedPreferences sharedPreferences;
	public static BeanYunIp beanSchoolInfo;
	public static UserInfo userInfo = new UserInfo();
	public static List<IdName> gradeDic;// 年级字典
	public static List<IdName> subjectDic;// 学科字典
	public static List<ClassRoom> classroomDic;// 班级字典
	public static List<IdName> schoolstageArr;// 学段字典
	public static List<IdName> editionVersionArr;// 版本字典
	public static LoginInfo loginInfo = new LoginInfo();
	public static ChildInfo childInfo = new ChildInfo();
	public static boolean chatLogin;
	@Override
	public void onCreate() {
		super.onCreate();
		APP_RUN_TIMES++;
		_pussApplication = this;
		Config.init(this);
//		initImageLoader(getApplicationContext());
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

			@Override
			public void onViewInitFinished(boolean arg0) {
				// TODO Auto-generated method stub
				Log.e("app", " onViewInitFinished is " + arg0);
			}

			@Override
			public void onCoreInitFinished() {
				// TODO Auto-generated method stub

			}
		};
		QbSdk.initX5Environment(getApplicationContext(), cb);
		// 全局未知异常捕获
		// Thread.setDefaultUncaughtExceptionHandler(this);
		sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES,
				Context.MODE_PRIVATE);
		ImgConfig.initImageLoader();

		new Timer().schedule(new TimerTask() { // 1秒后开始，5分钟上传一次自己的位置
					@Override
					public void run() {
						if (sharedPreferences.getBoolean("isShare", true)) {
							uploadAdr();
						}
					}
				}, 1000, Constants.UPDATE_TIME);

	}
	
	public void uploadAdr() {
		if (Constants.loginUser != null
				&& (lat != 23.117055306224895 || lon != 113.2759952545166)) { //
			Constants.loginUser.vCard.setField("latAndlon", lat + "," + lon);
			XmppConnection.getInstance().changeVcard(Constants.loginUser.vCard);
		}
	}

	public void clearAdr() {
		if (Constants.loginUser != null) {
			Constants.loginUser.vCard.setField("latAndlon",
					4.9E-324 + "," + 4.9E-324);
			XmppConnection.getInstance().changeVcard(Constants.loginUser.vCard);
		}
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}

//	private void initImageLoader(Context context) {
//		MEM_CACHE_SIZE = setMemCacheSizePercent(context, 0.20000000298023224F);
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//				context)
//				.threadPriority(Thread.NORM_PRIORITY - 2)
//				.denyCacheImageMultipleSizesInMemory()
//				.tasksProcessingOrder(QueueProcessingType.FIFO)
//				.discCache(
//						new UnlimitedDiscCache(ImageOptions
//								.getExternalCacheDir(context)))
//				.memoryCache(new LRULimitedMemoryCache(MEM_CACHE_SIZE)).build();
//		// Initialize ImageLoader with configuration.
//		ImageLoader.getInstance().init(config);
//	}
//
//	private int setMemCacheSizePercent(Context context, float percent) {
//		if ((percent < 0.05000000074505806F)
//				|| (percent > 0.80000001192092896F))
//			throw new IllegalArgumentException(
//					"setMemCacheSizePercent - percent must be between 0.05 and 0.8 (inclusive)");
//		return Math
//				.round(1024.0F * 1024.0F * percent * getMemoryClass(context));
//	}
//
//	private int getMemoryClass(Context context) {
//		return ((ActivityManager) context
//				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
//	}

	// 处理登录返回数据
	public static void dealLoginInfo() {
		gradeDic = userInfo.gradeDic;
		subjectDic = userInfo.subjectDic;
		schoolstageArr = userInfo.schoolstageArr;
		editionVersionArr = userInfo.editionVersionArr;
		getTree();
	}
	
	/*
	 * 根据schoolstage在字典中查询学段名称
	 */
	public static String getSchoolstageNameForId(int id) {
		if (!listIsEmpty(schoolstageArr)) {
			for (int i = 0; i < schoolstageArr.size(); i++) {
				if (schoolstageArr.get(i).id == id) {
					return schoolstageArr.get(i).name;
				}
			}
		}
		return "";
	}
	/*
	 * 根据editionVersion在字典中查询书版本名称
	 */
	public static String getEditionVersionNameForId(int id) {
		if (!listIsEmpty(editionVersionArr)) {
			for (int i = 0; i < editionVersionArr.size(); i++) {
				if (editionVersionArr.get(i).id == id) {
					return editionVersionArr.get(i).name;
				}
			}
		}
		return "";
	}
	/*
	 * 根据年级id在字典中查询年级名称
	 */
	public static String getGradeNameForId(int id) {
		if (!listIsEmpty(gradeDic)) {
			for (int i = 0; i < gradeDic.size(); i++) {
				if (gradeDic.get(i).id == id) {
					return gradeDic.get(i).name;
				}
			}
		}
		return "";
	}

	/*
	 * 根据学科id在字典中查询学科名称
	 */
	public static String getSubjectNameForId(int id) {
		if (!listIsEmpty(subjectDic)) {
			for (int i = 0; i < subjectDic.size(); i++) {
				if (subjectDic.get(i).id == id) {
					return subjectDic.get(i).name;
				}
			}
		}
		return "";
	}

	/*
	 * 根据班级id在字典中查询班级名称
	 */
	public static String getClassroomNameForId(int id) {
		if (!listIsEmpty(classroomDic)) {
			for (int i = 0; i < classroomDic.size(); i++) {
				if (classroomDic.get(i).id == id) {
					return classroomDic.get(i).name;
				}
			}
		}
		return "";
	}
	
	public static List<ClassRoom> grade_Subject_editionVersion = new ArrayList<ClassRoom>();
	/*
	 * 获取两级结构数据 年级-学科（出版社）
	 */
	public static void getTree() {
		if ( userInfo != null) {
			List<GradeSubjectArr> ClassRoom1 =  userInfo.gradeSubjectArr;
			List<GradeSubjectArr> ClassRoom2 =  userInfo.gradeSubjectArr;
			if (! listIsEmpty(ClassRoom1)) {
				for (int i = 0; i < ClassRoom1.size(); i++) {
					if (ClassRoom1.get(i).grade!=0) {
					ClassRoom ClassRoom_one=new ClassRoom();
					ClassRoom_one.id=ClassRoom1.get(i).grade;
					List<ClassRoom> ClassRoom_one_evs = new ArrayList<ClassRoom>();
					for (int j = 0; j < ClassRoom2.size(); j++) {
						if (ClassRoom2.get(j).subject!=0&&ClassRoom_one.id==ClassRoom2.get(j).grade) {
							ClassRoom ClassRoom_two=new ClassRoom();
							ClassRoom_two.id=ClassRoom2.get(j).subject;
							ClassRoom_two.editionVersion=ClassRoom2.get(j).editionVersion;
							ClassRoom_one_evs.add(ClassRoom_two);
						}
					}
					Set<ClassRoom> set = new HashSet<ClassRoom>(
							ClassRoom_one_evs);
					ClassRoom_one_evs.clear();
					ClassRoom_one_evs.addAll(set);
					Collections.sort(ClassRoom_one_evs,
							comparator4);
					ClassRoom_one.subjects = ClassRoom_one_evs;
					grade_Subject_editionVersion.add(ClassRoom_one);
				}
			}
				Set<ClassRoom> set = new HashSet<ClassRoom>(
						grade_Subject_editionVersion);
				grade_Subject_editionVersion.clear();
				grade_Subject_editionVersion.addAll(set);
				Collections.sort(grade_Subject_editionVersion,
						comparator4);
			}
		}
	}
	
	private static Comparator<ClassRoom> comparator4 = new Comparator<ClassRoom>() {
		public int compare(ClassRoom s1, ClassRoom s2) {
			return s1.id - s2.id;
		}
	};
	
	
	
	public static boolean listIsEmpty(List<?> list) {
		if (list != null && list.size() > 0) {
			return false;
		}
		return true;
	}
}
