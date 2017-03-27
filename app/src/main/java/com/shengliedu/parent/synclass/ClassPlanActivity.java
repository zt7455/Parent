package com.shengliedu.parent.synclass;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.ClassPlanAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.ClassPlan;
import com.shengliedu.parent.bean.SynClass;
import com.shengliedu.parent.util.CallOtherOpeanFile;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.util.ZZKTHttpDownLoad;
import com.shengliedu.parent.widght.CustomDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ClassPlanActivity extends BaseActivity {
	private TextView title, info, time, dynamic;
	private ImageView jiaoxuesheji, dian, plan_imageview;
	private View xianshi;
	private SynClass sysClass;
	private int studentId, hourId;
	private String date;
	private boolean zhuantai;
	private ExpandableListView expandableListView;
	private ClassPlanAdapter adapter;
	private List<ClassPlan> classPlans = new ArrayList<ClassPlan>();

	private int activityId;
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				showRoundProcessDialogCancel();
				CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
				callOtherOpeanFile
						.openFile(
								context,
								(File) msg.obj);

			}else if (msg.what==2){
				showRoundProcessDialogCancel();
			}else if (msg.what==3){
				JSONObject result=JSON.parseObject((String) msg.obj);
				List<ClassPlan> classPlansTemp = JSON.parseArray(
						result.getString("data"), ClassPlan.class);
				if (classPlansTemp != null) {
					classPlans.clear();
					classPlans.addAll(classPlansTemp);
					adapter.notifyDataSetChanged();
					for (int i = 0; i < classPlans.size(); i++) {
						expandableListView.expandGroup(i, false);
					}
				}
			}else if (msg.what==4){

			}
		}
	};
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		sysClass = (SynClass) getIntent().getExtras().get("synClass");
		hourId = sysClass.hourId;
		activityId = sysClass.activityId;
		studentId = App.childInfo.id;
		date = sysClass.dateStr;
		setBackSetResult(102);
		showTitle(sysClass.title);
		String head = sysClass.image;
		dian = (ImageView) findViewById(R.id.dian);
		plan_imageview = (ImageView) findViewById(R.id.plan_imageview);
		if (!TextUtils.isEmpty(head)) {
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Config1.getInstance().IP + head, plan_imageview);
		}
		xianshi = findViewById(R.id.xianshi);
		jiaoxuesheji = (ImageView) findViewById(R.id.jiaoxuesheji);
		title = (TextView) findViewById(R.id.plan_title);
		info = (TextView) findViewById(R.id.plan_info);
		time = (TextView) findViewById(R.id.syn_pingjia);
		dynamic = (TextView) findViewById(R.id.syn_pingjia_state);

		title.setText(sysClass.title);
		info.setText(sysClass.outlineName);
		time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, SelectDialogActivity.class);
				intent.putExtra("type", 11);
				intent.putExtra("activityId", activityId);
				// startActivityForResultByAniamtion(intent, 11);
				startActivityForResult(intent, 100);
			}
		});
		int score = sysClass.score;
		setScoreState(score);
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView_syn);
		adapter = new ClassPlanAdapter(ClassPlanActivity.this, classPlans);
		expandableListView.setAdapter(adapter);
		expandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int arg0) {
						// TODO Auto-generated method stub
						expandableListView.expandGroup(arg0, false);
					}
				});
		expandableListView.setChildDivider(getResources().getDrawable(
				R.mipmap.ic_split));
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int arg2, int arg3, long arg4) {
				// TODO Auto-generated method stub
				Intent intent = null;
				Bundle bundle = null;
				String status = classPlans.get(arg2).content.get(arg3).status;
				String questionTypeName = classPlans.get(arg2).content
						.get(arg3).teaching.questionTypeName;
				String questionType = classPlans.get(arg2).content.get(arg3).teaching.questionType;
				String plan_info = classPlans.get(arg2).content.get(arg3).teaching.name;
				String info_content = classPlans.get(arg2).content.get(arg3).teaching.content;
				String questionContentType = classPlans.get(arg2).content.get(arg3).teaching.questionContentType;
				int content_type = classPlans.get(arg2).content.get(arg3).content_type;
				int content_id = classPlans.get(arg2).content.get(arg3).content_id;
				String content_host = classPlans.get(arg2).content.get(arg3).content_host;
				String title = classPlans.get(arg2).content.get(arg3).content_host;
				int size = classPlans.get(arg2).content.get(arg3).teaching.size;
				int subType = classPlans.get(arg2).content.get(arg3).teaching.subType;
				int coursewareContentId = classPlans.get(arg2).content
						.get(arg3).id;
				String lineCount = classPlans.get(arg2).content.get(arg3).teaching.lineCount;
				final String link = classPlans.get(arg2).content.get(arg3).teaching.file;
				String link1 = classPlans.get(arg2).content.get(arg3).teaching.link;
				boolean isMultiple = classPlans.get(arg2).content.get(arg3).teaching.isMultiple;
				Log.v("TAG", "p=" + plan_info);
				Log.v("TAG", "isMultiple=" + isMultiple);
				if ("1".equals(status)) {// 已结束
					switch (content_type) {
					case 1:// 素材
						Log.v("TAG", "type=" + 1);
						intent = new Intent();
						bundle = new Bundle();
						bundle.putString("plan_info", plan_info);
						if (subType == 1) {
							if (!TextUtils.isEmpty(link)) {
								if (link.toLowerCase().endsWith(".ppt")
										|| link.toLowerCase().endsWith(".pptx")
										|| link.toLowerCase().endsWith(".doc")
										|| link.toLowerCase().endsWith(".docx")
										|| link.toLowerCase().endsWith(".xls")
										|| link.toLowerCase().endsWith(".xlsx")
										|| link.toLowerCase().endsWith(".txt")) {
									Log.v("TAG", content_host
											+ "file/download?id=" + content_id);
									Log.v("TAG",
											Environment
													.getExternalStorageDirectory()
													.getAbsolutePath()
													+ "/zzkt/parent/"
													+ link.substring(link
															.lastIndexOf("/") + 1));
									File file0 = new File(Environment
											.getExternalStorageDirectory()
											.getAbsolutePath()
											+ "/zzkt/parent/");
									if (!file0.exists()){
										file0.mkdirs();
									}
									File file = new File(Environment
											.getExternalStorageDirectory()
											.getAbsolutePath()
											+ "/zzkt/parent/"
											+ link.substring(link
													.lastIndexOf("/") + 1));
									if (!file.exists()) {

										showRoundProcessDialog(ClassPlanActivity.this);
										ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
										zzktHttpDownLoad.downloads(
												content_host
														+ "file/download?id="
														+ content_id,
												Environment
														.getExternalStorageDirectory()
														.getAbsolutePath()
														+ "/zzkt/parent/"
														+ link.substring(link
														.lastIndexOf("/") + 1),
												new Callback() {
													@Override
													public void onFailure(Call call, IOException e) {
														handlerReq.sendEmptyMessage(2);
													}

													@Override
													public void onResponse(Call call, Response response) throws IOException {
														Message message=Message.obtain();
														message.what=1;
														message.obj=new File(Environment
																.getExternalStorageDirectory()
																.getAbsolutePath()
																+ "/zzkt/parent/"
																+ link.substring(link
																.lastIndexOf("/") + 1));
														handlerReq.sendMessage(message);
													}
												});

									} else {
										CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
										callOtherOpeanFile.openFile(context,
												file);
									}
								} else if (link.endsWith(".html")
										|| link.endsWith(".htm")
										|| link.endsWith(".swf")) {
									bundle.putString("html", link);
									bundle.putString("content_host",
											content_host);
									intent.putExtras(bundle);
									intent.setClass(ClassPlanActivity.this,
											FlashHtmlActivity.class);
									startActivity(intent);
								} else if (link.endsWith(".PNG")
										|| link.endsWith(".png")
										|| link.endsWith(".jpg")
										|| link.endsWith(".JPG")) {
									bundle.putString("html", link);
									bundle.putString("content_host",
											content_host);
									intent.putExtras(bundle);
									intent.setClass(ClassPlanActivity.this,
											PicActivity.class);
									startActivity(intent);
								} else if (link.endsWith(".mp4")
										|| link.endsWith(".mp3")
										|| link.endsWith(".rmvb")
										|| link.endsWith(".avi")
										|| link.endsWith(".wmv")
										|| link.endsWith(".3gp")) {
									String mimeType = getMIMEType(content_host
											+ link);
									Intent mediaIntent = new Intent(
											Intent.ACTION_VIEW);
									mediaIntent
											.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									Log.v("TAG", "url=" + (content_host + link)
											+ "mimeType=" + mimeType);
									mediaIntent.setDataAndType(
											Uri.parse(content_host + link),
											mimeType);
									startActivity(mediaIntent);
									// DownLoadOpen downLoadOpen = new
									// DownLoadOpen(
									// ClassPlanActivity.this);
									// downLoadOpen.downOpen(content_host
									// + "file/download?id=" + content_id, link,
									// size);
									// bundle.putString("html", link);
									// intent.putExtras(bundle);
									// intent.setClass(ClassPlanActivity.this,
									// MediaPlayerActivity.class);
									// startActivity(intent);
								}
							}
						} else if (subType == 4) {
							bundle.putString("html", link1);
							bundle.putString("content_host", "");
							intent.putExtras(bundle);
							intent.setClass(ClassPlanActivity.this,
									FlashHtmlActivity.class);
							startActivity(intent);
						} else if (subType == 5) {
							bundle.putString("explanation", link1);
							intent.putExtras(bundle);
							intent.setClass(ClassPlanActivity.this,
									SynSeletQuestionAnalyticalActivity.class);
							startActivity(intent);
						} else {

						}

						break;
					case 2:// 习题
						Log.v("TAG", "type=" + 2);
						// 单选1
						// 多选2
						// 判断3
						// 排序4
						// 连线5
						// 填空10
						// 问答11

						intent = new Intent();
						bundle = new Bundle();
						bundle.putInt("hours", hourId);
						bundle.putString("date", date);
						bundle.putString("type", questionType);
						bundle.putInt("coursewareContentId",
								coursewareContentId);
						bundle.putString("content_host", content_host);
						bundle.putString("questionTypeName", questionTypeName);
						bundle.putString("questionContentType", questionContentType);
//						if (isMultiple) {
//							bundle.putString("plan_info", plan_info);
//							intent.putExtras(bundle);
//							intent.setClass(ClassPlanActivity.this,
//									OneToMoreQuestionActivity.class);
//							startActivity(intent);
//						} else {
							if ("1".equals(questionType)
									|| "3".equals(questionType)) {
								intent.putExtras(bundle);
								intent.setClass(ClassPlanActivity.this,
										SynSingleActivity.class);
								startActivity(intent);
							} else if ("2".equals(questionType)
									|| "4".equals(questionType)) {
								intent.putExtras(bundle);
								intent.setClass(ClassPlanActivity.this,
										SynDuoXuanActivity.class);
								startActivity(intent);
							} else if ("5".equals(questionType)
									&& "2".equals(lineCount)) {
								intent.putExtras(bundle);
								intent.setClass(ClassPlanActivity.this,
										SynShuangLianXianActivity.class);
								startActivity(intent);
							} else if ("5".equals(questionType)
									&& "3".equals(lineCount)) {
								intent.putExtras(bundle);
								intent.setClass(ClassPlanActivity.this,
										SynSanLianXianActivity.class);
								startActivity(intent);
							} else if ("10".equals(questionType)) {
								intent.putExtras(bundle);
								intent.setClass(ClassPlanActivity.this,
										SynFillBlanksActivity.class);
								startActivity(intent);
							}

//						}
						break;
					case 3:// 作业
						Log.v("TAG", "type=" + 3);
						break;
					case 4:// 文本
						Log.v("TAG", "type=" + 4);
						intent = new Intent();
						bundle = new Bundle();
						bundle.putString("plan_info", plan_info);
						bundle.putString("explanation", info_content);
						intent.putExtras(bundle);
						intent.setClass(ClassPlanActivity.this,
								SynSeletQuestionAnalyticalActivity.class);
						startActivity(intent);
						break;
					case 5:// 链接
						Log.v("TAG", "type=" + 5);
						intent = new Intent();
						bundle = new Bundle();
						bundle.putString("plan_info", plan_info);
						bundle.putString("html", link);
						bundle.putString("content_host", content_host);
						intent.putExtras(bundle);
						intent.setClass(ClassPlanActivity.this,
								FlashHtmlActivity.class);
						startActivity(intent);
						break;
					case 6:// 试卷(没有)
						Log.v("TAG", "type=" + 6);
						break;
					case 7:// 评价
						Log.v("TAG", "type=" + 7);
						intent = new Intent();
						bundle = new Bundle();
						bundle.putInt("hours", hourId);
						bundle.putString("date", date);
						bundle.putString("content_host", content_host);
						bundle.putInt("coursewareContentId",
								coursewareContentId);
						bundle.putString("questionTypeName", plan_info);
						intent.putExtras(bundle);
						intent.setClass(ClassPlanActivity.this,
								SynPingJiaActivity.class);
						startActivity(intent);
						break;
					case 8:// 电子课本
						Log.v("TAG", "type=" + 8);
						intent = new Intent();
						bundle = new Bundle();
						bundle.putInt("hours", hourId);
						bundle.putString("date", date);
						bundle.putString("content_host", content_host);
						bundle.putString("type", questionType);
						bundle.putInt("coursewareContentId",
								coursewareContentId);
						bundle.putString("questionTypeName", plan_info);
						intent.putExtras(bundle);
						intent.setClass(ClassPlanActivity.this,
								SynNetTextBookActivity.class);
						startActivity(intent);
						break;
					case 9:// 教师用书
					case 10:// 示范资源
						Log.v("TAG", "type=" + 10);
						intent = new Intent();
						bundle = new Bundle();
						bundle.putInt("hours", hourId);
						bundle.putString("date", date);
						bundle.putString("content_host", content_host);
						bundle.putString("type", questionType);
						bundle.putInt("coursewareContentId",
								coursewareContentId);
						bundle.putString("questionTypeName", plan_info);
						intent.putExtras(bundle);
						intent.setClass(ClassPlanActivity.this,
								SynZiYuanActivity.class);
						startActivity(intent);
						break;
					case 11:// 课堂活动
						Log.v("TAG", "type=" + 11);
						intent = new Intent();
						bundle = new Bundle();
						bundle.putInt("hours", hourId);
						bundle.putString("date", date);
						bundle.putString("type", questionType);
						bundle.putInt("coursewareContentId",
								coursewareContentId);
						bundle.putString("content_host", content_host);
						bundle.putString("questionTypeName", questionTypeName);

						intent.putExtras(bundle);
						intent.setClass(ClassPlanActivity.this,
								SynClassActivity.class);
						startActivity(intent);
						break;
					default:
						break;
					}
				} else {// 未开始
					showWarning();
				}
				return true;
			}
		});
		jiaoxuesheji.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if ("".equals(date)) {
					Toast.makeText(ClassPlanActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
				} else {
					Intent iteIntent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt("hourId", hourId);
					bundle.putSerializable("synClass", sysClass);
					iteIntent.putExtras(bundle);
					iteIntent.setClass(ClassPlanActivity.this,
							SynClassSheJiActivity.class);
					startActivity(iteIntent);
				}

			}
		});
		dian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (zhuantai == true) {
					xianshi.setVisibility(View.GONE);
					zhuantai = false;
				} else {
					xianshi.setVisibility(View.VISIBLE);
					zhuantai = true;
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 100 && arg1 == 131) {
			String date = arg2.getStringExtra("data");
			int score = Integer.parseInt(date);
			setScoreState(score);
		}
	}

	private void setScoreState(int score) {
		if (score == 0) {
			dynamic.setText("未评");
			dynamic.setTextColor(Color.GRAY);
		} else if (score >= 60 && score < 70) {
			dynamic.setText(score + "");
			dynamic.setTextColor(getResources().getColor(R.color.red));
		} else if (score >= 70 && score < 80) {
			dynamic.setText(score + "");
			dynamic.setTextColor(getResources().getColor(R.color.yellow));
		} else if (score >= 80 && score < 90) {
			dynamic.setText(score + "");
			dynamic.setTextColor(getResources().getColor(R.color.greenyellow));
		} else if (score >= 90) {
			dynamic.setText(score + "");
			dynamic.setTextColor(getResources().getColor(R.color.green));
		}
	}

	private void showWarning() {
		CustomDialog.Builder customDialog = new CustomDialog.Builder(
				ClassPlanActivity.this);
		customDialog
				.setMessage("为确保公平。试题、试\n卷、试题表的内容，在“进行\n中” 或 “未开始”的状态下不\n能显示，请您待课程状态为\n“结束”后，再进行查看。");
		customDialog.setTitle("注意");
		customDialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		customDialog.create().show();
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", studentId);
		map.put("hourId", hourId);
		map.put("date", date);
		doGet(Config1.getInstance().SYNCLASSPLAN(), map,
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
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_synclassplan;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(102);
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public String getMIMEType(String url) {

		String type = "*/*";
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = url.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = url.substring(dotIndex, url.length()).toLowerCase();
		Log.v("TAG", "end" + end);
		if (end == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {

			if (end.equalsIgnoreCase(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	// 可以自己随意添加
	private String[][] MIME_MapTable = {
			// {后缀名，MIME类型}
			{ ".3gp", "video/3gpp" },
			{ ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" },
			{ ".avi", "video/x-msvideo" },
			{ ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" },
			{ ".c", "text/plain" },
			{ ".class", "application/octet-stream" },
			{ ".conf", "text/plain" },
			{ ".cpp", "text/plain" },
			{ ".doc", "application/msword" },
			{ ".docx",
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx",
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ ".exe", "application/octet-stream" },
			{ ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" },
			{ ".h", "text/plain" },
			{ ".htm", "text/html" },
			{ ".html", "text/html" },
			{ ".jar", "application/java-archive" },
			{ ".java", "text/plain" },
			{ ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" },
			{ ".js", "application/x-javascript" },
			{ ".log", "text/plain" },
			{ ".m3u", "audio/x-mpegurl" },
			{ ".m4a", "audio/mp4a-latm" },
			{ ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" },
			{ ".m4u", "video/vnd.mpegurl" },
			{ ".m4v", "video/x-m4v" },
			{ ".mov", "video/quicktime" },
			{ ".mp2", "audio/x-mpeg" },
			{ ".mp3", "audio/mpeg" },
			{ ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" },
			{ ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" },
			{ ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" },
			{ ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" },
			{ ".png", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pptx",
					"application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ ".prop", "text/plain" }, { ".rc", "text/plain" },
			{ ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" },
			{ ".sh", "text/plain" }, { ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
			{ ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
			{ ".wmv", "audio/x-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" },
			{ ".z", "application/x-compress" },
			{ ".zip", "application/x-zip-compressed" }, { "", "*/*" } };
}
