package com.shengliedu.parent.util;

/**
 * 相关配置文件
 */
public class Config1 {
	public final static String MAIN_DC="http://dc.yunclass.com/login/login?";
	public final static String MAIN_SL="http://192.168.1.10:83/login/login?";
	public final static String MAIN_BOOK_IP="http://dc.yunclass.com";
//	public final static String MAIN_BOOK_IP="http://192.168.1.10:83";

	public static String IP;
	public static String CHATIP = "";
	// public static final String IP(){ return "http://192.168.1.207/";}
	// public static final String CHATIP(){ return "192.168.1.207";}
	public static  int CHAT_IMPORT = 5222;

	public static final String[] SCORE_STRINGS = { "60", "61", "62", "63",
			"64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74",
			"75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85",
			"86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96",
			"97", "98", "99", "100" };
	public static final String[] LEVEL_STRINGS = { "优", "良", "中", "差" };
	
	private static Config1 config1;
	private Config1() {
		super();
	}

	public static Config1 getInstance(){
		if (config1 == null) {
			config1=new Config1();
		}
		return config1;
	}
	// 登录 Post
	public String LOGIN() {
		return IP + "parent/login";
	}

	// 同步课堂 上课列表信息 GET
	public String SYNCLASS() {
		return IP + "parent/student-date-timetable?";
	}

	// 同步课堂 上课日期列表信息 GET
	public String SYNCLASSDATE() {
		return IP + "parent/student-timetable-date-list?";
	}

	// 同步课堂 课节信息 GET
	public String SYNCLASSPLAN() {
		return IP + "parent/hour-content?";
	}

	// 同步课堂 教学设计 GET
	public String SYNCLASSTEACHER() {
		return IP + "parent/teach-design?";
	}

	// 同步课堂 课节内容 GET
	public String SYNCLASSSINGLE() {
		return "parent/single-activity-resource?";
	}

	// 课后作业 列表展示 GET
	public String HOMEWORKLIST() {
		return IP + "parent/date-homework?";
	}

	// 课后作业 家长评价教师课件接口 POST
	public String PINGJIATEACHER() {
		return IP + "parent/activity-student-comment";
	}

	// 课后作业 家长评价学生活动接口 POST
	public String PINGJIACHILD() {
		return IP + "parent/parent-evaluate";
	}

	// 课后作业 阅状态 GET
	public String HOMEWORKYUE() {
		return IP + "parent/update-homework-status-has-view?";
	}

	// 课后作业 日期选择 GET
	public String HOMEWORKDATE() {
		return IP + "parent/homework-date-list?";
	}

	// 课后作业 详情展示 GET
	public String HOMEWORKDETAIL() {
		return IP + "parent/homework-detail?";
	}

	// 课后作业 提交作业 POST
	public String HOMEWORKSUBMIT() {
		return IP + "parent/homework-answer";
	}

	// 课后作业 家长评价作业 POST
	public String PINGJIAHOMEWORK() {
		return IP + "parent/homework-parent-comment";
	}

	// 课后作业 文件上传 POST
	public String FILEUPLOAD() {
		return IP + "file/upload";
	}

	// 三图一表 折线图 GET
	public String EXAMSCORE() {
		return IP + "parent/exam-ranking-score?";
	}

	// 三图一表 雷达 点位 GET
	public String RADARDATA() {
		return IP + "parent/radar-data?";
	}

	// 学期学科列表 GET
	public String SEMESTER_SUBJECT() {
		return IP + "parent/semester-subject?";
	}

	// 学期 周 月 列表 GET
	public String WEEK_SUBJECT() {
		return IP + "parent/semester-month-week-subject?";
	}

	// 各科成绩 web
	public String SUBJECTEXAM() {
		return IP + "appparent.html#/subjectExam/";
	}

	// 课堂回答表现 周 web
	public String LESSONSHOWWEEK() {
		return IP + "appparent.html#/lessonShow/";
	}

	// 课堂回答表现 月 web
	public String LESSONSHOWMONTH() {
		return IP + "appparent.html#/lessonMonthShow/";
	}

	// 作业表现 周 web
	public String HOMEWORKEXPRESS() {
		return IP + "appparent.html#/homeworkEvaluate/";
	}

	// 作业表现 月 web
	public String HOMEWORKEXPRESSMONTH() {
		return IP + "appparent.html#/homeworkShow/";
	}

	// 在校纪律情况 web
	public String LESSONBEHAVIOR() {
		return IP + "appparent.html#/lessonBehavior/";
	}

	// 出勤情况 web
	public String ATTENDANCE() {
		return IP + "appparent.html#/attendance/";
	}

	// 异动报告 GET
	public String EXCEPTION() {
		return IP + "parent/exception-report?";
	}

	// 知识短板 GET
	public String WEAKNESS() {
		return IP + "parent/point-weakness?";
	}

	// 请假条列表 GET
	public String NOTELIST() {
		return IP + "parent/note-for-leave?";
	}

	// 请假条详情 GET
	public String NOTEDETAIL() {
		return IP + "parent/note-for-leave-detail?";
	}

	// 请假条 课节 GET
	public String DATESCOPE() {
		return IP + "parent/classroom-date-scope?";
	}

	// 请假条 提交请假条 POST
	public String ADDNOTE() {
		return IP + "parent/add-note-for-leave";
	}

	// 获取用户信息 GET
	public String PERSONINFO() {
		return IP + "parent/personal-info?";
	}

	// 更新用户信息 POST
	public String UPDATEUSERINFO() {
		return IP + "parent/personal-info-update";
	}

	// 意见反馈 提交 POST
	public String FEEDSUBMIT() {
		return IP + "parent/feedback-submit";
	}

	// 我的意见 GET
	public String FEEDBACK() {
		return IP + "parent/feedback?";
	}

	// 删除我的意见 GET
	public String DELETEFEEDBACK() {
		return IP + "parent/feedback-remove?";
	}

	// 晒晒 列表 GET
	public String SHAISHAI() {
		return IP + "parent/shai-shai-shai?";
	}

	// 晒晒点赞 GET
	public String DIANZAN() {
		return IP + "parent/support?";
	}

	// 学校信息 GET
	public String SCHOOLINFO() {
		return IP + "parent/school-info";
	}

	// 学校通知 GET
	public String NOTIFICATION() {
		return IP + "parent/notification?";
	}
	// 家长端推送信息获取接口 GET
	public String REAL_TIME_NOTICE() {
		return IP + "parent/real-time-notice?parentId=";
	}
	// 返回要展示资源的相关信息 GET
	public String SHOWCLASSSINGLE() {
		return "teacher/resource-show?";
	}
	// 获取书列表 GET
		public String BOOK_LIST() {
			return MAIN_BOOK_IP+"/book?";
		}

	//-------------------------新的结构-------------------------------
	// 获取activity GET
	public String ACTIVITY_LIST() {
		return IP+"activity?";
	}
	// 获取作业列表 GET
	public String HOMEWORK_CONTENT() {
		return IP+"/coursewareContents?";
	}
	// 获取作业回答详情: GET
	public String HOMEWORK_ASWER() {
		return IP+"/activityStudent?";
	}
	public String POST_HOMEWORK_ASWER() {
		return IP+"/activityStudent";
	}
	// 课后作业 日期选择 GET
	public String NEWHOMEWORKDATE() {
		return IP + "/activityHomework?";
	}
//	学校信息获取接口:
//	请求类型:get
//	请求地址:schoolinfo(注意学校信息存储在云端,所以服务器ip地址为:http://dc.yunclass.com)
//			参数:schoolinfo_id(多个用逗号分隔)
     public String GETSCHOOLINFO() {
	return MAIN_BOOK_IP + "/schoolinfo?";
	 }
}
