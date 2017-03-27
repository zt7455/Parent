package com.shengliedu.parent.new_synclass;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.activity.bean.Homework;
import com.shengliedu.parent.activity.bean.SynClass;
import com.shengliedu.parent.adapter.NewSynClassContentAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.BeanYunIp;
import com.shengliedu.parent.new_synclass.bean.BeanSynClass;
import com.shengliedu.parent.new_synclass.bean.BeanSynClassTree;
import com.shengliedu.parent.synclass.FlashHtmlActivity;
import com.shengliedu.parent.synclass.PicActivity;
import com.shengliedu.parent.synclass.SynSeletQuestionAnalyticalActivity;
import com.shengliedu.parent.util.CallOtherOpeanFile;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.HandlerMessageObj;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.util.ZZKTHttpDownLoad;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ClassCotentActivity extends BaseActivity {
	private TextView sync_sub, sync_name, sync_time;
	private SynClass sysClass;
	private int hourId;
	private int activity;
	private boolean zhuantai;
	private ExpandableListView expandableListView;
	private NewSynClassContentAdapter adapter;

	private List<BeanSynClass> synClasses=new ArrayList<>();
	private List<BeanSynClassTree> synClassesTrees=new ArrayList<>();
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				synClassesTrees.clear();
				synClasses= JSON.parseArray((String) msg.obj,BeanSynClass.class);
				if (synClasses!=null&&synClasses.size()>0){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("activity", activity);
					map.put("student", App.childInfo.id);
					map.put("type", "5,6,7");
					doGet(Config1.getInstance().HOMEWORK_ASWER(), map, new ResultCallback() {
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
				}else {
					adapter.notifyDataSetChanged();
				}

			}else if (msg.what==2){

			}else if (msg.what==3){
				List<Homework> homeworksTemp= JSON.parseArray((String) msg.obj,Homework.class);
				if (homeworksTemp!=null&&homeworksTemp.size()>0){
					for (int i=0;i<homeworksTemp.size();i++){
						for (int j=0;j<synClasses.size();j++){
							if (homeworksTemp.get(i).content==synClasses.get(j).id){
								synClasses.get(j).content=homeworksTemp.get(i).id;
								synClasses.get(j).evaluate=homeworksTemp.get(i).evaluate;
							}
						}
					}
				}
				for (int i=0;i<synClasses.size();i++){
					BeanSynClassTree beanSynClassTree=new BeanSynClassTree();
					beanSynClassTree.catalogId=synClasses.get(i).catalogId;
					beanSynClassTree.catalogName=synClasses.get(i).catalogName;
					beanSynClassTree.beanSynClasses=new ArrayList<>();
					if (!synClassesTrees.contains(beanSynClassTree)){
						synClassesTrees.add(beanSynClassTree);
					}
				}
				for (int j=0;j<synClassesTrees.size();j++){
					for (int i=0;i<synClasses.size();i++){
						if (synClasses.get(i).catalogId==synClassesTrees.get(j).catalogId){
							synClassesTrees.get(j).beanSynClasses.add(synClasses.get(i));
						}
					}
				}
				synClasses.clear();
				adapter.notifyDataSetChanged();
				if (synClassesTrees != null && synClassesTrees.size() > 0) {
					for (int i = 0; i < synClassesTrees.size(); i++) {
						expandableListView.expandGroup(i);
					}
				}
			}else if (msg.what==4){

			}else if (msg.what==5){
				HandlerMessageObj handlerMessageObj= (HandlerMessageObj) msg.obj;
				List<BeanYunIp> beanYunIps=JSON.parseArray(handlerMessageObj.getJson(),BeanYunIp.class);
				String link=handlerMessageObj.getLink();
				String type=handlerMessageObj.getType();
				final String fileLink=handlerMessageObj.getPath();
				if (beanYunIps!=null && beanYunIps.size()>0){
					BeanYunIp yunIp = beanYunIps.get(0);
					Intent intent=new Intent();
					Bundle bundle=new Bundle();
					if ("素材文件".equals(type)){
						if (fileLink.toLowerCase().endsWith(".ppt")
								|| fileLink.toLowerCase().endsWith(".pptx")
								|| fileLink.toLowerCase().endsWith(".doc")
								|| fileLink.toLowerCase().endsWith(".docx")
								|| fileLink.toLowerCase().endsWith(".xls")
								|| fileLink.toLowerCase().endsWith(".xlsx")
								|| fileLink.toLowerCase().endsWith(".txt")) {
							Log.v("TAG",
									Environment
											.getExternalStorageDirectory()
											.getAbsolutePath()
											+ "/zzkt/parent/"
											+ fileLink.substring(fileLink
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
									+ fileLink.substring(fileLink
									.lastIndexOf("/") + 1));
							if (!file.exists()) {
								showRoundProcessDialog(ClassCotentActivity.this);
								ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
								zzktHttpDownLoad.downloads(
										"Http://"+yunIp.ip+"/"
												+ link,
										Environment
												.getExternalStorageDirectory()
												.getAbsolutePath()
												+ "/zzkt/parent/"
												+ fileLink.substring(fileLink
												.lastIndexOf("/") + 1),
										new okhttp3.Callback() {
											@Override
											public void onFailure(Call call, IOException e) {
												handlerReq.sendEmptyMessage(8);
											}

											@Override
											public void onResponse(Call call, Response response) throws IOException {
												Message message=Message.obtain();
												message.what=7;
												message.obj=new File(Environment
														.getExternalStorageDirectory()
														.getAbsolutePath()
														+ "/zzkt/parent/"
														+ fileLink.substring(fileLink
														.lastIndexOf("/") + 1));
												handlerReq.sendMessage(message);
											}
										});

							} else {
								CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
								callOtherOpeanFile.openFile(context,
										file);
							}

						} else if (fileLink.endsWith(".html")
								|| fileLink.endsWith(".htm")
								|| fileLink.endsWith(".swf")) {
							bundle.putString("html", fileLink);
							bundle.putString("content_host",
									fileLink);
							intent.putExtras(bundle);
							intent.setClass(ClassCotentActivity.this,
									FlashHtmlActivity.class);
							startActivity(intent);
						} else if (fileLink.endsWith(".PNG")
								|| fileLink.endsWith(".png")
								|| fileLink.endsWith(".jpg")
								|| fileLink.endsWith(".JPG")) {
							bundle.putString("html", fileLink);
							bundle.putString("content_host",
									"Http://"+yunIp.ip+"/");
							intent.putExtras(bundle);
							intent.setClass(ClassCotentActivity.this,
									PicActivity.class);
							startActivity(intent);
						} else if (fileLink.endsWith(".mp4")
								|| fileLink.endsWith(".mp3")
								|| fileLink.endsWith(".rmvb")
								|| fileLink.endsWith(".avi")
								|| fileLink.endsWith(".wmv")
								|| fileLink.endsWith(".3gp")) {
							try {
							String mimeType = getMIMEType("Http://"+yunIp.ip+"/"
									+ fileLink);
							Intent mediaIntent = new Intent(
									Intent.ACTION_VIEW);
							mediaIntent
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							mediaIntent.setDataAndType(
									Uri.parse("Http://"+yunIp.ip+"/" + fileLink),
									mimeType);
							startActivity(mediaIntent);
						} catch (ActivityNotFoundException e) {
							Toast.makeText(context, "sorry，mimeType附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
						}
						}
					}else if ("素材链接".equals(type)){
						bundle.putString("html", link);
						bundle.putString("content_host", "Http://"+yunIp.ip+"/");
						intent.putExtras(bundle);
						intent.setClass(ClassCotentActivity.this,
								FlashHtmlActivity.class);
						startActivity(intent);
					}
				}
			}else if (msg.what==6){

			}else if (msg.what==7){
				showRoundProcessDialogCancel();
				CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
				callOtherOpeanFile.openFile(context,
						(File) msg.obj);
			}else if (msg.what==8){
				showRoundProcessDialogCancel();
			}
		}
	};
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		sysClass = (SynClass) getIntent().getExtras().get("synClass");
		activity = getIntent().getIntExtra("activity",0);
		setBackSetResult(102);
		sync_sub = getView(R.id.sync_sub);
		sync_name = getView(R.id.sync_name);
		sync_time = getView(R.id.sync_time);
		if (sysClass!=null){
			hourId = sysClass.hour;
			showTitle(App.getSubjectNameForId(sysClass.subject));
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
			sync_sub.setText(App.getSubjectNameForId(sysClass.subject));
			sync_name.setText(sysClass.name+"");
			sync_time.setText(formatter.format(new Date(sysClass.date*1000)));
		}
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView_syn);
		adapter = new NewSynClassContentAdapter(ClassCotentActivity.this, synClassesTrees);
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
		expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int arg2, int arg3, long arg4) {
				// TODO Auto-generated method stub
				Intent intent = null;
				Bundle bundle = null;
				JSONObject object=JSON.parseObject(synClassesTrees.get(arg2).beanSynClasses.get(arg3).teaching);
				final int content_id = synClassesTrees.get(arg2).beanSynClasses.get(arg3).content_id;
				int content_type = synClassesTrees.get(arg2).beanSynClasses.get(arg3).content_type;

				int from = synClassesTrees.get(arg2).beanSynClasses.get(arg3).from;
				int school = synClassesTrees.get(arg2).beanSynClasses.get(arg3).school;
				final String name = object.getString("name")+"";
				final String fileLink = object.getString("file")+"";
				String link = object.getString("link")+"";
					switch (content_type) {
					case 1:// 素材
						int subType = object.getInteger("subType");
						intent = new Intent();
						bundle = new Bundle();
						if (subType == 1) {
							if (!TextUtils.isEmpty(fileLink)) {
								if (from==1){
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("schoolinfo_id", school);
									doGet(Config1.getInstance().GETSCHOOLINFO(), map,
											new ResultCallback() {
												@Override
												public void onResponse(Call call, Response response, String json) {
													Message message = Message.obtain();
													message.what = 5;
													HandlerMessageObj handlerMessageObj=new HandlerMessageObj();
													handlerMessageObj.setLink("file/download?id="+ content_id);
													handlerMessageObj.setJson(json);
													handlerMessageObj.setType("素材文件");
													handlerMessageObj.setPath(fileLink);
													message.obj = handlerMessageObj;
													handlerReq.sendMessage(message);
												}

												@Override
												public void onFailure(Call call, IOException exception) {
													handlerReq.sendEmptyMessage(6);
												}
											});
								}else if (from==2){
									if (fileLink.toLowerCase().endsWith(".ppt")
											|| fileLink.toLowerCase().endsWith(".pptx")
											|| fileLink.toLowerCase().endsWith(".doc")
											|| fileLink.toLowerCase().endsWith(".docx")
											|| fileLink.toLowerCase().endsWith(".xls")
											|| fileLink.toLowerCase().endsWith(".xlsx")
											|| fileLink.toLowerCase().endsWith(".txt")) {
										Log.v("TAG",
												Environment
														.getExternalStorageDirectory()
														.getAbsolutePath()
														+ "/zzkt/parent/"
														+ fileLink.substring(fileLink
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
												+ fileLink.substring(fileLink
												.lastIndexOf("/") + 1));
										if (!file.exists()) {
											showRoundProcessDialog(ClassCotentActivity.this);
											ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
											zzktHttpDownLoad.downloads(
													Config1.getInstance().IP
															+ "file/download?id="
															+ content_id,
													Environment
															.getExternalStorageDirectory()
															.getAbsolutePath()
															+ "/zzkt/parent/"
															+ fileLink.substring(fileLink
															.lastIndexOf("/") + 1),
													new Callback() {
														@Override
														public void onFailure(Call call, IOException e) {
															handlerReq.sendEmptyMessage(8);
														}

														@Override
														public void onResponse(Call call, Response response) throws IOException {
															Message message=Message.obtain();
															message.what=7;
															message.obj=new File(Environment
																	.getExternalStorageDirectory()
																	.getAbsolutePath()
																	+ "/zzkt/parent/"
																	+ fileLink.substring(fileLink
																	.lastIndexOf("/") + 1));
															handlerReq.sendMessage(message);
														}
													});

										} else {
											CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
											callOtherOpeanFile.openFile(context,
													file);
										}

									} else if (fileLink.endsWith(".html")
											|| fileLink.endsWith(".htm")
											|| fileLink.endsWith(".swf")) {
										bundle.putString("html", fileLink);
										bundle.putString("content_host",
												fileLink);
										intent.putExtras(bundle);
										intent.setClass(ClassCotentActivity.this,
												FlashHtmlActivity.class);
										startActivity(intent);
									} else if (fileLink.endsWith(".PNG")
											|| fileLink.endsWith(".png")
											|| fileLink.endsWith(".jpg")
											|| fileLink.endsWith(".JPG")) {
										bundle.putString("html", fileLink);
										bundle.putString("content_host",
												Config1.getInstance().IP);
										intent.putExtras(bundle);
										intent.setClass(ClassCotentActivity.this,
												PicActivity.class);
										startActivity(intent);
									} else if (fileLink.endsWith(".mp4")
											|| fileLink.endsWith(".mp3")
											|| fileLink.endsWith(".rmvb")
											|| fileLink.endsWith(".avi")
											|| fileLink.endsWith(".wmv")
											|| fileLink.endsWith(".3gp")) {
										try {
										String mimeType = getMIMEType(Config1.getInstance().IP
												+ fileLink);
										Intent mediaIntent = new Intent(
												Intent.ACTION_VIEW);
										mediaIntent
												.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										mediaIntent.setDataAndType(
												Uri.parse(Config1.getInstance().IP + fileLink),
												mimeType);
										startActivity(mediaIntent);
										} catch (ActivityNotFoundException e) {
											Toast.makeText(context, "sorry，mimeType附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
										}
									}
								}
								}
						} else if (subType == 4) {
							if (from==1){
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("schoolinfo_id", school);
								doGet(Config1.getInstance().GETSCHOOLINFO(), map,
										new ResultCallback() {
											@Override
											public void onResponse(Call call, Response response, String json) {
												Message message = Message.obtain();
												message.what = 5;
												HandlerMessageObj handlerMessageObj=new HandlerMessageObj();
												handlerMessageObj.setLink("file/download?id="+ content_id);
												handlerMessageObj.setJson(json);
												handlerMessageObj.setType("素材链接");
												handlerMessageObj.setPath(fileLink);
												message.obj = handlerMessageObj;
												handlerReq.sendMessage(message);
											}

											@Override
											public void onFailure(Call call, IOException exception) {
												handlerReq.sendEmptyMessage(6);
											}
										});
							}else if (from==2){
								bundle.putString("html", link);
								bundle.putString("content_host", "");
								intent.putExtras(bundle);
								intent.setClass(ClassCotentActivity.this,
										FlashHtmlActivity.class);
								startActivity(intent);
							}

						} else if (subType == 5) {
								bundle.putString("explanation", link);
								intent.putExtras(bundle);
								intent.setClass(ClassCotentActivity.this,
										SynSeletQuestionAnalyticalActivity.class);
								startActivity(intent);

						} else {

						}

						break;
					case 2:// 习题
						// 单选1
						// 多选2
						// 判断3
						// 排序4
						// 连线5
						// 填空10
						// 问答11
						intent = new Intent();
						bundle = new Bundle();
						bundle.putSerializable("SynClass",synClassesTrees.get(arg2).beanSynClasses.get(arg3));
						intent.putExtras(bundle);
						intent.setClass(ClassCotentActivity.this,
								NewSynClassDetailActivity.class);
						startActivity(intent);
						break;
					case 3:// 作业
						Log.v("TAG", "type=" + 3);
						break;
					case 4:// 文本
						Log.v("TAG", "type=" + 4);
						intent = new Intent();
						bundle = new Bundle();
						bundle.putString("plan_info", name);
						bundle.putString("explanation", link);
						intent.putExtras(bundle);
						intent.setClass(ClassCotentActivity.this,
								SynSeletQuestionAnalyticalActivity.class);
						startActivity(intent);
						break;
					case 5:// 链接
//						intent = new Intent();
//						bundle = new Bundle();
//						bundle.putString("plan_info", name);
//						bundle.putString("html", link);
//						bundle.putString("content_host", content_host);
//						intent.putExtras(bundle);
//						intent.setClass(ClassCotentActivity.this,
//								FlashHtmlActivity.class);
//						startActivity(intent);
						break;
					case 6:// 试卷(没有)
						break;
					case 7:// 评价
						break;
					case 8:// 电子课本
						final int bookId=object.getInteger("bookId");
							intent = new Intent();
							intent.putExtra("bookId", bookId);
							intent.putExtra("content_id", content_id);
							intent.setClass(ClassCotentActivity.this,
									SynBookActivity.class);
							startActivity(intent);
						break;
					case 9:// 教师用书
					case 10:// 示范资源
						Log.v("TAG", "type=" + 10);
						final int bookId2=object.getInteger("bookId");
						intent = new Intent();
						intent.putExtra("bookId", bookId2);
						intent.putExtra("content_id", content_id);
						intent.setClass(ClassCotentActivity.this,
								SynZiYuanActivity.class);
						startActivity(intent);
						break;
					case 11:// 课堂活动
//						Log.v("TAG", "type=" + 11);
//						intent = new Intent();
//						bundle = new Bundle();
//						bundle.putInt("hours", hourId);
//						bundle.putString("date", date);
//						bundle.putString("type", questionType);
//						bundle.putInt("coursewareContentId",
//								coursewareContentId);
//						bundle.putString("content_host", content_host);
//						bundle.putString("questionTypeName", questionTypeName);
//
//						intent.putExtras(bundle);
//						intent.setClass(ClassCotentActivity.this,
//								SynClassActivity.class);
//						startActivity(intent);
						break;
					default:
						break;
					}
				return true;
			}
		});
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
//		if (arg0 == 100 && arg1 == 131) {
//			String date = arg2.getStringExtra("data");
//		}
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("part", 2);
		map.put("hour", hourId);
		doGet(Config1.getInstance().HOMEWORK_CONTENT(), map,
				new ResultCallback() {
					@Override
					public void onResponse(Call call, Response response, String json) {
						Message message=Message.obtain();
						message.what=1;
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
		return R.layout.activity_new_synclasscontent;
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
