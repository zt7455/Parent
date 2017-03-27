package com.shengliedu.parent.synclass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.ShejiAdapter;
import com.shengliedu.parent.bean.Beike;
import com.shengliedu.parent.bean.SynClass;
import com.shengliedu.parent.util.CallOtherOpeanFile;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.util.ZZKTHttpDownLoad;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SynClassSheJiActivity extends BaseActivity {
	private TextView title, info, time, dynamic;
	private ShejiAdapter adapter;
	private List<Beike> dataList=new ArrayList<Beike>();
	private SynClass sysClass;
	private ExpandableListView expandableListView;
	private int hourId;
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
				List<Beike> classPlansTemp = JSON.parseArray(
						result.getString("data"), Beike.class);
				if (classPlansTemp != null) {
					dataList.clear();
					dataList.addAll(classPlansTemp);
					adapter.notifyDataSetChanged();
					for (int i = 0; i < dataList.size(); i++) {
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
		hourId=getIntent().getExtras().getInt("hourId");
		sysClass = (SynClass) getIntent().getExtras().get("synClass");
		setBack();
		showTitle("教学设计");
		title = (TextView) findViewById(R.id.sync_sheji_title);
		info = (TextView) findViewById(R.id.sync_sheji_info);
		time = (TextView) findViewById(R.id.sync_sheji_time);
		dynamic = (TextView) findViewById(R.id.sync_sheji_dynamic);
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView_syn_sheji);
		adapter = new ShejiAdapter(
				SynClassSheJiActivity.this, dataList);
		expandableListView.setAdapter(adapter);
		expandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@SuppressLint("NewApi")
					@Override
					public void onGroupCollapse(int arg0) {
						// TODO Auto-generated method stub
						expandableListView.expandGroup(arg0, false);
					}
				});
		expandableListView
		.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0,
					View arg1, int arg2, int arg3, long arg4) {
				// TODO Auto-generated method stub
				Intent intent = null;
				Bundle bundle = null;
				int content_type = dataList.get(arg2).content
						.get(arg3).content_type;
				int id = dataList.get(arg2).content.get(arg3).id;
				int subType = dataList.get(arg2).content.get(arg3).subType;
				String questionType = dataList.get(arg2).content
						.get(arg3).questionType+"";
				String questionContentType = dataList.get(arg2).content
						.get(arg3).questionContentType+"";
				int school = dataList.get(arg2).content.get(arg3).school;
				int from = dataList.get(arg2).content.get(arg3).from;
				int book = dataList.get(arg2).content.get(arg3).book;
				String content_host = dataList.get(arg2).content
						.get(arg3).content_host;
				String name = dataList.get(arg2).content.get(arg3).name;
				String title = dataList.get(arg2).content.get(arg3).title;
				final String link = dataList.get(arg2).content.get(arg3).link;
				String lineCount = dataList.get(arg2).content.get(arg3).lineCount;
				switch (content_type) {
				case 0:// 手动添加文本：{content_type = 0，name}
					break;
				case 1:// 素材：{content_type =
						// 1，name，link，id，school,subType}，subType说明：1素材，2试卷，4连接，5文本，6百宝箱
					intent = new Intent();
					bundle = new Bundle();
					bundle.putString("plan_info", name);
					bundle.putInt("content_type", content_type);
					if (subType == 1) {
						if (!TextUtils.isEmpty(link)) {
							if (link.toLowerCase().endsWith(".ppt")
									|| link.toLowerCase().endsWith(
											".pptx")
									|| link.toLowerCase().endsWith(
											".doc")
									|| link.toLowerCase().endsWith(
											".docx")
									|| link.toLowerCase().endsWith(
											".xls")
									|| link.toLowerCase().endsWith(
											".xlsx")
									|| link.toLowerCase().endsWith(
											".txt")) {
								Log.v("TAG", content_host
										+ "file/download?id=" + id);
								Log.v("TAG",
										Environment
												.getExternalStorageDirectory()
												.getAbsolutePath()
												+ "/zzkt/teacher/"
												+ link.substring(link
														.lastIndexOf("/") + 1));
								File file0 = new File(Environment
										.getExternalStorageDirectory()
										.getAbsolutePath()
										+ "/zzkt/parent/");
								if (!file0.exists()){
									file0.mkdirs();
								}
								File file = new File(
										Environment
												.getExternalStorageDirectory()
												.getAbsolutePath()
												+ "/zzkt/parent/"
												+ link.substring(link
														.lastIndexOf("/") + 1));
								if (!file.exists()) {

									showRoundProcessDialog(SynClassSheJiActivity.this);
									ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
									zzktHttpDownLoad.downloads(
											content_host
													+ "file/download?id="
													+ id,
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
									callOtherOpeanFile.openFile(
											SynClassSheJiActivity.this, file);
								}
							} else if (link.endsWith(".html")
									|| link.endsWith(".htm")
									|| link.endsWith(".swf")) {
								bundle.putString("html", link);
								bundle.putString("content_host",
										content_host);
								intent.putExtras(bundle);
								intent.setClass(SynClassSheJiActivity.this,
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
								intent.setClass(SynClassSheJiActivity.this,
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
								Log.v("TAG", "url="
										+ (content_host + link)
										+ "mimeType=" + mimeType);
								mediaIntent.setDataAndType(
										Uri.parse(content_host
												+ link), mimeType);
								startActivity(mediaIntent);
								// DownLoadOpen downLoadOpen = new
								// DownLoadOpen(
								// activity);
								// downLoadOpen.downOpen(content_host
								// + "file/download?id=" +
								// content_id, link,
								// size);
								// bundle.putString("html", link);
								// intent.putExtras(bundle);
								// intent.setClass(activity,
								// MediaPlayerActivity.class);
								// startActivity(intent);
							}
						}
					} else if (subType == 4) {
						bundle.putString("html", link);
						bundle.putString("content_host",
								content_host);
						intent.putExtras(bundle);
						intent.setClass(SynClassSheJiActivity.this,
								FlashHtmlActivity.class);
						startActivity(intent);
					} else if (subType == 5) {
						bundle.putString("explanation", link);
						intent.putExtras(bundle);
						intent.setClass(
								SynClassSheJiActivity.this,
								SynSeletQuestionAnalyticalActivity.class);
						startActivity(intent);
					} else if (subType == 6) {
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
					} else {

					}

					break;
				case 2:// 习题：{content_type =
						// 2，name，id，school，questionType，from},from说明：1表示本地，2表示云端
						// 向后台请求相关数据
					intent = new Intent();
					bundle = new Bundle();
					bundle.putInt("school", school);
					bundle.putInt("content_type", content_type);
					bundle.putString("type", questionType+"");
					bundle.putString("questionContentType", questionContentType+"");
					bundle.putString("name", name);
					bundle.putString("title", title);
					bundle.putInt("from",
							from);
					bundle.putString("content_host", content_host);
					bundle.putInt("id", id);
					if ("1".equals(questionType)
							|| "3".equals(questionType)) {
						intent.putExtras(bundle);
						intent.setClass(SynClassSheJiActivity.this,
								com.shengliedu.parent.showdesign.SynSingleActivity.class);
						startActivity(intent);
					} else if ("2".equals(questionType)
							|| "4".equals(questionType)) {
						intent.putExtras(bundle);
						intent.setClass(SynClassSheJiActivity.this,
								com.shengliedu.parent.showdesign.SynDuoXuanActivity.class);
						startActivity(intent);
					} else if ("5".equals(questionType)
							&& "2".equals(lineCount)) {
						intent.putExtras(bundle);
						intent.setClass(SynClassSheJiActivity.this,
								com.shengliedu.parent.showdesign.SynShuangLianXianActivity.class);
						startActivity(intent);
					} else if ("5".equals(questionType)
							&& "3".equals(lineCount)) {
						intent.putExtras(bundle);
						intent.setClass(SynClassSheJiActivity.this,
								com.shengliedu.parent.showdesign.SynSanLianXianActivity.class);
						startActivity(intent);
					} else if ("10".equals(questionType)) {
						intent.putExtras(bundle);
						intent.setClass(SynClassSheJiActivity.this,
								com.shengliedu.parent.showdesign.SynFillBlanksActivity.class);
						startActivity(intent);
					}

					break;
				case 8:// 电子课本：{content_type = 8，name，id，book}
					intent = new Intent();
					bundle = new Bundle();
					bundle.putInt("id", id);
					bundle.putInt("book", book);
					bundle.putString("name", name);
					bundle.putInt("content_type", content_type);
					bundle.putString("content_host", content_host);
					intent.putExtras(bundle);
					intent.setClass(SynClassSheJiActivity.this,
							com.shengliedu.parent.showdesign.SynNetTextBookActivity.class);
					startActivity(intent);
					break;
				case 9:// 教辅资料：{content_type = 9，name，id，book}
				case 10:// 示范资源：{content_type = 10，name，id，book}
					intent = new Intent();
					bundle = new Bundle();
					bundle.putInt("id", id);
					bundle.putInt("book", book);
					bundle.putString("name", name);
					bundle.putInt("content_type", content_type);
					bundle.putString("content_host", content_host);
					intent.putExtras(bundle);
					intent.setClass(SynClassSheJiActivity.this,
							com.shengliedu.parent.showdesign.SynZiYuanActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
				return true;
			}
		});
		title.setText(sysClass.title);
		info.setText(sysClass.outlineName);
		time.setText(sysClass.timeScopeStr);
		String Status = sysClass.status;
		if ("0".equals(Status)) {
			dynamic.setText("[已结束]");
			dynamic.setTextColor(Color.GRAY);
		} else if ("1".equals(Status)) {
			dynamic.setText("[正进行]");
			dynamic.setTextColor(Color.YELLOW);
		} else {
			dynamic.setText("[未开始]");
			dynamic.setTextColor(Color.GRAY);
		}
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hourId", hourId);
		doGet(Config1.getInstance().SYNCLASSTEACHER(), map,
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
		return R.layout.activity_synclass_sheji;
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
