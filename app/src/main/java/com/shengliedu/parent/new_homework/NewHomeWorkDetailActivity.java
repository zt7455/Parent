package com.shengliedu.parent.new_homework;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.activity.bean.Homework;
import com.shengliedu.parent.adapter.AswerItemAdapter;
import com.shengliedu.parent.adapter.NewCameraAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.BeanYunIp;
import com.shengliedu.parent.homework.MovieActivity;
import com.shengliedu.parent.new_homework.bean.NewHomeworkAnswer;
import com.shengliedu.parent.new_homework.bean.NewHomeworkDetail;
import com.shengliedu.parent.pdf.ShowPdfFromUrlActivity;
import com.shengliedu.parent.synclass.FlashHtmlActivity;
import com.shengliedu.parent.synclass.SynSeletQuestionAnalyticalActivity;
import com.shengliedu.parent.util.AudioRecorder;
import com.shengliedu.parent.util.CallOtherOpeanFile;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.HandlerMessageObj;
import com.shengliedu.parent.util.HtmlImage;
import com.shengliedu.parent.util.NumberToCode;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.util.UploadUtil;
import com.shengliedu.parent.util.Utils;
import com.shengliedu.parent.util.ZZKTHttpDownLoad;
import com.shengliedu.parent.view.NoScrollGridView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewHomeWorkDetailActivity extends BaseActivity implements View.OnClickListener,MediaPlayer.OnCompletionListener {
	private int location;
	private List<Homework> homeworks;
	private Homework currentHome;
	private BeanYunIp yunIp;
	private String currentIp;
	private String asnwer="";

	private LinearLayout mainLin,questionLin,textLin,cameraLin,recordLin,vedioLin;
	private TextView select_main,select_question,select_pdf,select_answer,select_centerjx,text_edit,record_link,record_file,record_content;
	private RelativeLayout select_center,leftRel,rightRel;
	private ImageView yanjing;
	private NewHomeworkDetail newHomeworkDetail;
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private NoScrollGridView item_aswer;
	public Button select_btn;
	private int result;

	//照片
	private NewCameraAdapter newCameraAdapter;


	private VideoView mVideoView;
	private ImageView recordIv;
	private ImageButton recordPlayIv;
	private String videoPath;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		homeworks= (List<Homework>) getIntent().getExtras().getSerializable("homeworklist");
		location=getIntent().getExtras().getInt("location");
		if (homeworks!=null){
			currentHome=homeworks.get(location);
		}

		setBack();
		mainLin=getView(R.id.mainLin);
		questionLin=getView(R.id.questionLin);
		select_main=getView(R.id.select_main);
		select_question=getView(R.id.select_question);
		select_pdf=getView(R.id.select_pdf);
		select_answer=getView(R.id.select_answer);
		item_aswer=getView(R.id.item_aswer);
		select_btn=getView(R.id.select_btn);
		select_centerjx=getView(R.id.select_centerjx);
		yanjing=getView(R.id.yanjing);
		textLin=getView(R.id.textLin);
		text_edit=getView(R.id.text_edit);
		cameraLin=getView(R.id.cameraLin);
		recordLin=getView(R.id.recordLin);
		vedioLin=getView(R.id.vedioLin);
		mVideoView = getView(R.id.video_view);
		recordIv = getView(R.id.recordIv);
		recordPlayIv = getView(R.id.recordPlayIv);
		select_center = getView(R.id.select_center);
		leftRel = getView(R.id.leftRel);
		rightRel = getView(R.id.rightRel);
		record_file = getView(R.id.record_file);
		record_link = getView(R.id.record_link);
		record_content = getView(R.id.record_content);
		leftRel.setOnClickListener(new View.OnClickListener() {

										  @Override
										  public void onClick(View view) {
if (homeworks!=null){
	if (location-1>=0){
		location--;
		currentHome=homeworks.get(location);
		getDatas();
	}else {
		toast("第一题了");
	}
}
										  }
									  });
		rightRel.setOnClickListener(new View.OnClickListener() {

										  @Override
										  public void onClick(View view) {
											  if (homeworks!=null){
												  if (location+1<=homeworks.size()-1){
													  location++;
													  currentHome=homeworks.get(location);
													  getDatas();
												  }else {
													  toast("最后一题了");
												  }

											  }
										  }
									  });
		select_center.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (currentHome != null && currentHome.content_type == 2) {
					Intent iteIntent = new Intent();
					Bundle bundle = new Bundle();
					if (newHomeworkDetail != null && !TextUtils.isEmpty(newHomeworkDetail.explanation)) {
						bundle.putString("explanation", newHomeworkDetail.explanation);
						iteIntent.putExtras(bundle);
						iteIntent.setClass(NewHomeWorkDetailActivity.this,
								SynSeletQuestionAnalyticalActivity.class);
						startActivity(iteIntent);
					} else {
						toast("没有解析");
					}
				} else if (currentHome != null && currentHome.content_type == 1) {
					if (!TextUtils.isEmpty(currentHome.answer)) {
						Toast.makeText(NewHomeWorkDetailActivity.this, "不能重复答题", Toast.LENGTH_SHORT).show();
					} else {
					Log.e("提交的时候", "currentHome.submitType=" + currentHome.submitType);
					int type = 0;
					if (currentHome.part == 1) {
						type = 8;
					} else if (currentHome.part == 3) {
						type = 9;
					}
					if (currentHome.submitType == 1) {// 1文本，2图片，3音频，4视频（对应题干后面的小图标） ----提交类型
						if (!TextUtils.isEmpty(text_edit.getText().toString())) {
							asnwer = text_edit.getText().toString();
							String t = "\"type\"";
							String d = "\"data\"";
							StringBuilder builder = new StringBuilder();
							builder.append("[");
							builder.append("{");
							builder.append(t + ":");
							builder.append(6);
							builder.append("," + d + ":\"");
							builder.append(asnwer);
							builder.append("\"}");
							builder.append("]");
							final String awser = builder.toString();
							Log.v("TAG", "awser=" + awser);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("activity", currentHome.activity);
							map.put("content", currentHome.id);
							map.put("student", App.childInfo.id);
							map.put("type", type);
							map.put("answer", awser);
							doPost(Config1.getInstance()
									.POST_HOMEWORK_ASWER(), map, new ResultCallback() {
								@Override
								public void onResponse(Call call, Response response, String json) {
									toast("提交成功");
									homeworks.get(location).answer = awser;
									Message message = Message.obtain();
									message.what = 5;
									message.obj = json;
									handlerReq.sendMessage(message);
								}

								@Override
								public void onFailure(Call call, IOException exception) {
									handlerReq.sendEmptyMessage(6);
								}
							});
						} else {
							toast("请输入文本作业");
						}
					} else if (currentHome.submitType == 2) {
						if (fileList.size()>1){
							final String t = "\"type\"";
							final String d = "\"data\"";
							final StringBuilder builder = new StringBuilder();
							builder.append("[");
							final int finalType = type;
							new Thread() {

								@Override
								public void run() {

									for (int i = 0; i < fileList.size(); i++) {
										if (i != 0) {
											builder.append("{");
											builder.append(t + ":");
											builder.append(7);
											builder.append("," + d + ":\"");
											UploadUtil u = new UploadUtil();
											handlerReq.sendEmptyMessage(11);
											String a = u.uploadFile(new File(fileList.get(i)), Config1
															.getInstance().FILEUPLOAD(),
													currentHome.content_id + "", App.childInfo.id + "");
											handlerReq.sendEmptyMessage(10);
											JSONObject object = JSON.parseObject(a);
											builder.append(object.getString("link"));
											builder.append("\"}");
											if (i != fileList.size() - 1) {
												builder.append(",");
											}
										}

									}
									builder.append("]");
									final String anwser = builder.toString();
									Log.v("TAG", "awser=" + anwser);
									HandlerMessageObj handlerMessageObj = new HandlerMessageObj();
									handlerMessageObj.setType(finalType + "");
									handlerMessageObj.setAnwser(anwser);
									Message message = Message.obtain();
									message.what = 111;
									message.obj = handlerMessageObj;
									handlerReq.sendMessage(message);
								}
							}.start();

						}else {
							toast("请选择图片");
						}

					}else if (currentHome.submitType == 3) {
						final File file=new File(getAmrPath());
						if (file.exists()){
						final String t = "\"type\"";
						final String d = "\"data\"";
						final StringBuilder builder = new StringBuilder();
						builder.append("[");
						final int finalType = type;
						new Thread() {
							@Override
							public void run() {
										builder.append("{");
										builder.append(t + ":");
										builder.append(8);
										builder.append("," + d + ":\"");
										UploadUtil u = new UploadUtil();
								handlerReq.sendEmptyMessage(11);
										String a = u.uploadFile(file, Config1
														.getInstance().FILEUPLOAD(),
												currentHome.content_id + "", App.childInfo.id + "");
								handlerReq.sendEmptyMessage(10);
										JSONObject object = JSON.parseObject(a);
										builder.append(object.getString("link"));
										builder.append("\"}");
								builder.append("]");
								final String anwser = builder.toString();
								Log.v("TAG", "awser=" + anwser);
								HandlerMessageObj handlerMessageObj = new HandlerMessageObj();
								handlerMessageObj.setType(finalType + "");
								handlerMessageObj.setAnwser(anwser);
								Message message = Message.obtain();
								message.what = 111;
								message.obj = handlerMessageObj;
								handlerReq.sendMessage(message);
							}
						}.start();

						}else {
							toast("请录音");
						}
					}else if (currentHome.submitType == 4) {
						if (!TextUtils.isEmpty(videoPath)){
						final String t = "\"type\"";
						final String d = "\"data\"";
						final StringBuilder builder = new StringBuilder();
						builder.append("[");
						final int finalType = type;
						new Thread() {
							@Override
							public void run() {
								builder.append("{");
								builder.append(t + ":");
								builder.append(9);
								builder.append("," + d + ":\"");
								UploadUtil u = new UploadUtil();
								handlerReq.sendEmptyMessage(11);
								String a = u.uploadFile(new File(videoPath), Config1
												.getInstance().FILEUPLOAD(),
										currentHome.content_id + "", App.childInfo.id + "");
								handlerReq.sendEmptyMessage(10);
								JSONObject object = JSON.parseObject(a);
								builder.append(object.getString("link"));
								builder.append("\"}");
								builder.append("]");
								final String anwser = builder.toString();
								Log.v("TAG", "awser=" + anwser);
								HandlerMessageObj handlerMessageObj = new HandlerMessageObj();
								handlerMessageObj.setType(finalType + "");
								handlerMessageObj.setAnwser(anwser);
								Message message = Message.obtain();
								message.what = 111;
								message.obj = handlerMessageObj;
								handlerReq.sendMessage(message);
							}
						}.start();
						}else {
							toast("请录像");
						}
					}
				}
			}
			}
		});
		select_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					if (currentHome!=null) {
						int wrong = 1;
						int type = 0;
						if (currentHome.part == 1) {
							type = 8;
						} else if (currentHome.part == 3) {
							type = 9;
						}
						if (newHomeworkDetail != null && !TextUtils.isEmpty(newHomeworkDetail.correct)) {
							List<NewHomeworkDetail> corrects = JSON.parseArray(newHomeworkDetail.correct, NewHomeworkDetail.class);
							if (corrects != null && corrects.size() > 0) {
								String a = corrects.get(0).title;
								if (asnwer.equals(a)) {
									wrong = 0;
								}
							}
						}
						if (!TextUtils.isEmpty(currentHome.answer)) {
							Toast.makeText(NewHomeWorkDetailActivity.this, "不能重复答题", Toast.LENGTH_SHORT).show();
						} else {
							if (currentHome.content_type == 2) {//习题
								if (TextUtils.isEmpty(asnwer)) {
									Toast.makeText(NewHomeWorkDetailActivity.this, "请答题", Toast.LENGTH_SHORT).show();
								} else {
								String t = "\"type\"";
								String d = "\"data\"";
								StringBuilder builder = new StringBuilder();
								builder.append("[");
								builder.append("{");
								builder.append(t + ":");
								builder.append(newHomeworkDetail.type);
								builder.append("," + d + ":\"");
								builder.append(asnwer);
								builder.append("\"}");
								builder.append("]");
								final String awser = builder.toString();
								Log.v("TAG", "awser=" + awser);
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("activity", currentHome.activity);
								map.put("content", currentHome.id);
								map.put("student", App.childInfo.id);
								map.put("type", type);
								map.put("wrong", wrong);
								map.put("answer", awser);
								doPost(Config1.getInstance()
										.POST_HOMEWORK_ASWER(), map, new ResultCallback() {
									@Override
									public void onResponse(Call call, Response response, String json) {
										toast("提交成功");
										homeworks.get(location).answer = awser;
										Message message = Message.obtain();
										message.what = 5;
										message.obj = json;
										handlerReq.sendMessage(message);
									}

									@Override
									public void onFailure(Call call, IOException exception) {
										handlerReq.sendEmptyMessage(6);
									}
								});
							}

							} else if (currentHome.content_type == 1) {//素材，判断提交类型

							}
						}
					}
			}

		});
		recordIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 启动拍摄的Activity
				if (currentHome!=null && !TextUtils.isEmpty(currentHome.answer)) {
					toast("不能重复答题");

				} else {
					Intent intent = new Intent(NewHomeWorkDetailActivity.this, MovieActivity.class);
					startActivityForResult(intent, 202);
				}
			}

		});
		recordPlayIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 启动拍摄的Activity
				initVedioView();
			}

		});
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				List<BeanYunIp> beanYunIps=JSON.parseArray((String) msg.obj,BeanYunIp.class);
				if (beanYunIps!=null && beanYunIps.size()>0){
					yunIp = beanYunIps.get(0);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("isSingle", 1);
					currentIp="http://" + yunIp.ip+ "/";
		    doGet(currentIp+ "ques/"+currentHome.content_id+"?", map,
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
			}else if (msg.what==2){

			}else if (msg.what==3){
				if (currentHome.content_type==2) {
				List<NewHomeworkDetail> newHomeworkDetails=JSON.parseArray((String) msg.obj,NewHomeworkDetail.class);
				if (newHomeworkDetails!=null && newHomeworkDetails.size()>0){
					newHomeworkDetail=newHomeworkDetails.get(0);
					final NewHomeworkDetail question= JSON.parseObject(newHomeworkDetail.question,NewHomeworkDetail.class);
					if (question!=null && !TextUtils.isEmpty(question.main)){
						mainLin.setVisibility(View.VISIBLE);
							deal_text(question.main,mainLin,select_main);
					}else {
						mainLin.setVisibility(View.GONE);
					}
					if (question!=null && !TextUtils.isEmpty(question.question)){
						questionLin.setVisibility(View.VISIBLE);
						deal_text(question.question,questionLin,select_question);
						if (!TextUtils.isEmpty(question.contentLink) && question.contentLink.toLowerCase().endsWith(".pdf")){
							select_pdf.setVisibility(View.VISIBLE);
							select_pdf.setText("查看PDF题目");
							select_pdf.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									Intent intent=new Intent(NewHomeWorkDetailActivity.this,ShowPdfFromUrlActivity.class);
									intent.putExtra("url", currentIp + question.contentLink);
									startActivity(intent);
								}
							});
						}else {
							select_pdf.setVisibility(View.GONE);
						}
					}else {
						questionLin.setVisibility(View.GONE);
					}
						select_centerjx.setText("查看解析");
						yanjing.setVisibility(View.VISIBLE);
						select_btn.setVisibility(View.VISIBLE);
						NewHomeworkDetail answer = JSON.parseObject(newHomeworkDetail.answer, NewHomeworkDetail.class);
						if (answer != null && answer.answers != null && answer.answers.size() > 0) {
							select_answer.setVisibility(View.VISIBLE);
							String answers = "";
							for (int i = 0; i < answer.answers.size(); i++) {
								answers += answer.answers.get(i).get(0).title + "、" + answer.answers.get(i).get(0).content + "\n";
							}
							select_answer.setText(answers);

						} else {
							select_answer.setVisibility(View.GONE);
						}
						List<String> items = new ArrayList<String>();
						if (!App.listIsEmpty(answer.answers)
								&& !App.listIsEmpty(answer.answers.get(0))) {
							for (int i = 0; i < answer.answers.size(); i++) {
								items.add(answer.answers.get(i).get(0).title);
							}
						} else {
							if (newHomeworkDetail.type == 1|| newHomeworkDetail.type == 2) {
								for (int i = 0; i < 6; i++) {
									items.add(NumberToCode.numberToCode(i));
								}
							} else if (newHomeworkDetail.type == 3) {
								items.add("A");
								items.add("D");
							}
						}
						if (currentHome != null && !TextUtils.isEmpty(currentHome.answer)) {
							List<NewHomeworkAnswer> newHomeworkAnswers = JSON.parseArray(currentHome.answer, NewHomeworkAnswer.class);
							if (newHomeworkAnswers != null && newHomeworkAnswers.size() > 0) {
								asnwer = newHomeworkAnswers.get(0).data;
								Log.v("dddd","asnwer="+asnwer);
								if (newHomeworkAnswers.get(0).type == 1) {
									if (!TextUtils.isEmpty(asnwer)) {
										map.put(NumberToCode.codeToNumber(asnwer), true);
									}
								} else if (newHomeworkAnswers.get(0).type == 2) {
									if (!TextUtils.isEmpty(asnwer)) {
										for (int i = 0; i < asnwer.length(); i++) {
											map.put(NumberToCode.codeToNumber(asnwer.charAt(i) + ""), true);
										}
									}
								} else if (newHomeworkAnswers.get(0).type == 3) {
									if (!TextUtils.isEmpty(asnwer)) {
										if ("A".equals(answer)) {
											map.put(0, true);
										} else {
											map.put(1, true);
										}
									}
								}
							}
						}
						final AswerItemAdapter adapter = new AswerItemAdapter(NewHomeWorkDetailActivity.this,
								map, items);
						item_aswer.setVisibility(View.VISIBLE);
						item_aswer.setAdapter(adapter);
						item_aswer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
													long arg3) {
								if (newHomeworkDetail.type == 1) {
									map.clear();
									map.put(arg2, true);
									asnwer = NumberToCode.numberToCode(arg2);
								}
								if (newHomeworkDetail.type == 2) {
									if (map.containsKey(arg2)) {
										map.remove(arg2);
										asnwer.replace(NumberToCode.numberToCode(arg2), "");
									} else {
										map.put(arg2, true);
										asnwer += NumberToCode.numberToCode(arg2);
									}
								}
								if (newHomeworkDetail.type == 3) {
									map.clear();
									if (arg2 == 0) {
										asnwer = "A";
									} else if (arg2 == 1) {
										asnwer = "D";
									}
									map.put(arg2, true);
								}
								adapter.notifyDataSetChanged();
							}
						});
				   			}else {
								toast("作业获取失败");
							}
					}else if (currentHome.content_type==1){
						select_centerjx.setText("提交作业");
						yanjing.setVisibility(View.GONE);
						select_btn.setVisibility(View.GONE);
					    showContentType1File();
						Log.e("currentHome.submitType","currentHome.submitType="+currentHome.submitType);
						if (currentHome.submitType==1){// 1文本，2图片，3音频，4视频（对应题干后面的小图标） ----提交类型
							textLin.setVisibility(View.VISIBLE);
							final List<NewHomeworkAnswer> newHomeworkAnswers = JSON.parseArray(currentHome.answer, NewHomeworkAnswer.class);
							if (newHomeworkAnswers != null && newHomeworkAnswers.size() > 0) {
								NewHomeworkAnswer newHomeworkAnswer=newHomeworkAnswers.get(0);
								text_edit.setText(newHomeworkAnswer.data+"");
							}
						}else if (currentHome.submitType==2){
							cameraLin.setVisibility(View.VISIBLE);
							item_aswer.setVisibility(View.VISIBLE);
//							if (!TextUtils.isEmpty(currentHome.answer)) {
								final List<NewHomeworkAnswer> newHomeworkAnswers = JSON.parseArray(currentHome.answer, NewHomeworkAnswer.class);
								fileList.clear();
								if (newHomeworkAnswers != null && newHomeworkAnswers.size() > 0) {
									for (int i = 0; i < newHomeworkAnswers.size(); i++) {
										fileList.add(currentIp + newHomeworkAnswers.get(i).data);
									}
									i = 0;
								}else {
									i = 1;
									fileList.add(R.mipmap.camera_jiahao + "");
								}
								newCameraAdapter=new NewCameraAdapter(NewHomeWorkDetailActivity.this,fileList);
								item_aswer.setAdapter(newCameraAdapter);
								item_aswer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
										if (newHomeworkAnswers != null && newHomeworkAnswers.size() > 0) {
											showDialog(fileList.get(i), i);
										}else {
											if (i==0){
												showPop();
											}else {
												showDialog(fileList.get(i), i);
											}
										}
									}
								});
//							}
						}else if (currentHome.submitType==3){
							recordLin.setVisibility(View.VISIBLE);
							final List<NewHomeworkAnswer> newHomeworkAnswers = JSON.parseArray(currentHome.answer, NewHomeworkAnswer.class);
							if (newHomeworkAnswers != null && newHomeworkAnswers.size() > 0) {
								NewHomeworkAnswer newHomeworkAnswer=newHomeworkAnswers.get(0);
								String url=currentIp+newHomeworkAnswer.data;
								ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
								zzktHttpDownLoad.downloads(url,RecordPath,
										new okhttp3.Callback() {
											@Override
											public void onFailure(Call call, IOException e) {
												handlerReq.sendEmptyMessage(8);
											}
											@Override
											public void onResponse(Call call, Response response) throws IOException {
												handlerReq.sendEmptyMessage(7);
											}
										});
							}else {
								initRecord();
							}
						}else if (currentHome.submitType==4){
							vedioLin.setVisibility(View.VISIBLE);
							final List<NewHomeworkAnswer> newHomeworkAnswers = JSON.parseArray(currentHome.answer, NewHomeworkAnswer.class);
							if (newHomeworkAnswers != null && newHomeworkAnswers.size() > 0) {
								NewHomeworkAnswer newHomeworkAnswer=newHomeworkAnswers.get(0);
								videoPath=currentIp+newHomeworkAnswer.data;
							}
						}
					}


			}else if (msg.what==4){

			}else if (msg.what==5){
				result=223;
              setBackSetResult(result);
			}else if (msg.what==6){

			}else if (msg.what==7){
                initRecord();
			}else if (msg.what==8){
				initRecord();
			}else if (msg.what==9){
				NewHomeWorkDetailActivity.this
						.showRoundProcessDialogCancel();
				CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
				callOtherOpeanFile.openFile(
						NewHomeWorkDetailActivity.this, (File) msg.obj);
			}else if (msg.what==10){
				NewHomeWorkDetailActivity.this
						.showRoundProcessDialogCancel();
			}else if (msg.what==11){
				NewHomeWorkDetailActivity.this
						.showRoundProcessDialog(NewHomeWorkDetailActivity.this);
			}else if (msg.what==111){
HandlerMessageObj handlerMessageObj= (HandlerMessageObj) msg.obj;
				String type=handlerMessageObj.getType();
				final String awser=handlerMessageObj.getAnwser();
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("activity", currentHome.activity);
									map.put("content", currentHome.id);
									map.put("student", App.childInfo.id);
									map.put("type", type);
									map.put("answer", awser);
									doPost(Config1.getInstance()
											.POST_HOMEWORK_ASWER(), map, new ResultCallback() {
										@Override
										public void onResponse(Call call, Response response, String json) {
											toast("提交成功");
											homeworks.get(location).answer=awser;
											Message message = Message.obtain();
											message.what = 5;
											message.obj = json;
											handlerReq.sendMessage(message);
										}

										@Override
										public void onFailure(Call call, IOException exception) {
											handlerReq.sendEmptyMessage(6);
										}
									});
			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		map.clear();
		mainLin.removeAllViews();
		questionLin.removeAllViews();
		select_main.setText("");
		select_question.setText("");
		select_answer.setText("");
		select_pdf.setText("");
		text_edit.setText("");
		asnwer="";
		videoPath="";
		currentIp="";
		fileList.clear();
		textLin.setVisibility(View.GONE);
		cameraLin.setVisibility(View.GONE);
		item_aswer.setVisibility(View.GONE);
		select_btn.setVisibility(View.GONE);
		recordLin.setVisibility(View.GONE);
		vedioLin.setVisibility(View.GONE);
		record_file.setVisibility(View.GONE);
		record_link.setVisibility(View.GONE);
		record_content.setVisibility(View.GONE);
		if (currentHome!=null){
			if (currentHome.from==1) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("schoolinfo_id", currentHome.school);
				doGet(Config1.getInstance().GETSCHOOLINFO(), map,
						new ResultCallback() {
							@Override
							public void onResponse(Call call, Response response, String json) {
								Message message = Message.obtain();
								message.what = 1;
								message.obj = json;
								handlerReq.sendMessage(message);
							}

							@Override
							public void onFailure(Call call, IOException exception) {
								handlerReq.sendEmptyMessage(2);
							}
						});
			}else if (currentHome.from==2) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("isSingle", 1);
				currentIp=Config1.getInstance().IP;
				doGet(currentIp+"ques/"+currentHome.content_id+"?", map,
						new ResultCallback() {
							@Override
							public void onResponse(Call call, Response response, String json) {
								Message message=Message.obtain();
								message.what=3;
								message.obj=json;
								handlerReq.sendMessage(message);
							}

							@Override
							public void onFailure(Call call, IOException exception) {handlerReq.sendEmptyMessage(4);

							}
						});
			}
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_new_homework_detail;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case 0:
				if (resultCode == Activity.RESULT_OK
						&& !TextUtils.isEmpty(photoUri.getPath())) {
					fileList.add(photoUri.getPath());
					newCameraAdapter.notifyDataSetChanged();
				}

				break;
			case 1:
				if (data != null) {
					Uri uri = geturi(data);
					Log.v("TAG", "uri:" + uri.getPath());
					if (uri != null) {
						String[] proj = { MediaStore.Images.Media.DATA };
						Cursor actualimagecursor = NewHomeWorkDetailActivity.this
								.getContentResolver().query(uri, proj, null, null,
										null);
						int actual_image_column_index = actualimagecursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						actualimagecursor.moveToFirst();
						String img_path = actualimagecursor
								.getString(actual_image_column_index);
						Log.v("TAG1", img_path);
						fileList.add(img_path);
						newCameraAdapter.notifyDataSetChanged();
					}
				}
				break;
		case 202:
			if (resultCode == RESULT_OK) {
				// 成功
				videoPath = data.getStringExtra("path");
				Toast.makeText(NewHomeWorkDetailActivity.this,
						"存储路径为:" + videoPath, Toast.LENGTH_SHORT).show();
				playVisible();
				setBitamap();
			} else {
				// 失败
			}
			break;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(result);
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void deal_text(String text,LinearLayout linearLayout,TextView textView){
		textView.setText(Html.fromHtml(HtmlImage
				.deleteSrc(text)));
		List<String> imgList = HtmlImage.getImgSrc(text);
		ImageLoader utils = ImageLoader.getInstance();
		if (imgList.size() > 0) {
			linearLayout.removeAllViews();
			for (int j = 0; j < imgList.size(); j++) {
				final String url = imgList.get(j);
				ImageView imageView = new ImageView(this);
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new RelativeLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				utils.displayImage((url.contains("http")?url: (Config1.getInstance().IP+url)), imageView);
				imageView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated
						// method stub
						HtmlImage htmlImage = new HtmlImage();
						htmlImage.showDialog(NewHomeWorkDetailActivity.this, (url.contains("http")?url: (Config1.getInstance().IP+url)));
					}
				});
				linearLayout.addView(imageView);
			}
		}
	}
	private void showContentType1File(){
		JSONObject object= JSON.parseObject(currentHome.teaching);
		final String fileName=object.getString("file");
		final String link=object.getString("link");
		final String name=object.getString("name");
		final String content=object.getString("content");
		if (!TextUtils.isEmpty(name)){
			mainLin.setVisibility(View.VISIBLE);
			deal_text(name,mainLin,select_main);
		}else {
			mainLin.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(content)){
			questionLin.setVisibility(View.VISIBLE);
			deal_text(content,questionLin,select_question);
		}else {
			questionLin.setVisibility(View.GONE);
		}
		int subType=object.getInteger("subType");
		if (subType == 1) {
			if (!TextUtils.isEmpty(fileName)) {
				record_file.setVisibility(View.VISIBLE);
				record_file.setText("点击查看附件");
				record_file.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						File file0 = new File(Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/zzkt/parent/");
						if (!file0.exists()){
							file0.mkdirs();
						}
						final File file = new File(Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/zzkt/parent/"
								+ fileName
								.substring(fileName
										.lastIndexOf("/") + 1));
						if (!file.exists()) {
							NewHomeWorkDetailActivity.this
									.showRoundProcessDialog(NewHomeWorkDetailActivity.this);
							ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
							zzktHttpDownLoad.downloads(
									currentIp+ "file/download?id="
											+ currentHome.content_id,
									Environment.getExternalStorageDirectory()
											.getAbsolutePath()
											+ "/zzkt/parent/"
											+ fileName
											.substring(fileName
													.lastIndexOf("/") + 1),
									new Callback() {
										@Override
										public void onFailure(Call call, IOException e) {
											handlerReq.sendEmptyMessage(10);
										}

										@Override
										public void onResponse(Call call, Response response) throws IOException {
											Message message=Message.obtain();
											message.what=9;
											message.obj=file;
											handlerReq.sendMessage(message);
										}
									});

						} else {
							CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
							callOtherOpeanFile.openFile(NewHomeWorkDetailActivity.this, file);
						}
					}
				});
			}
		} else if (subType == 4) {
			if (!TextUtils.isEmpty(link)) {
				record_link.setVisibility(View.VISIBLE);
				record_link.setText(link);
				record_link.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putString("plan_info", "链接");
						bundle.putString("html", link);
						bundle.putString("content_host", "");
						intent.putExtras(bundle);
						intent.setClass(NewHomeWorkDetailActivity.this, FlashHtmlActivity.class);
						NewHomeWorkDetailActivity.this.startActivity(intent);
					}
				});
			}
		} else if (subType == 5) {
			if (!TextUtils.isEmpty(link)) {
				record_content.setVisibility(View.VISIBLE);
				record_content.setText(link);
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		scanOldFile();
		if (mediaPlayer!=null){
			mediaPlayer=null;
		}
		if (mVideoView!=null) {
			mVideoView=null;
		}
	}

	//-------------------------图片展示------------------------------------
	private PopupWindow pop;
	private List<String> fileList=new ArrayList<>();
	@SuppressLint("InflateParams")
	private void showPop() {
		// TODO Auto-generated method stub
		if (pop == null) {
			View v = LayoutInflater.from(NewHomeWorkDetailActivity.this).inflate(
					R.layout.select_picture, null);

			pop = new PopupWindow(NewHomeWorkDetailActivity.this);
			pop.setContentView(v);
			pop.setWidth(RelativeLayout.LayoutParams.FILL_PARENT);
			pop.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
			pop.setOutsideTouchable(true);
			pop.setFocusable(true);
			pop.setBackgroundDrawable(new ColorDrawable(0));
			Button take_photo = (Button) v.findViewById(R.id.take_photo);
			take_photo.setOnClickListener(this);
			Button xiangce = (Button) v.findViewById(R.id.xiangce);
			xiangce.setOnClickListener(this);
			Button quxiao = (Button) v.findViewById(R.id.quxiao);
			quxiao.setOnClickListener(this);
		}
		pop.showAtLocation(select_centerjx, Gravity.BOTTOM, 0, 0);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.take_photo:
				if (pop != null) {
					pop.dismiss();
				}
				photo();
				break;
			case R.id.xiangce:
				// 从相册中选择
				if (pop != null) {
					pop.dismiss();
				}
				Intent intent = new Intent(
						// 相册
						Intent.ACTION_PICK,
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setType("image/*");
				startActivityForResult(intent, RESULT_LOAD_IMAGE);
				break;
			case R.id.quxiao:
				if (pop != null) {
					pop.dismiss();
				}
				break;
		}
	}

	// ------------------------------------------------------

	private static final int TAKE_PICTURE = 0;
	private static final int RESULT_LOAD_IMAGE = 1;
	private String path = "";
	private Uri photoUri;

	public void photo() {
		try {
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);

			String sdcardState = Environment.getExternalStorageState();
			String sdcardPathDir = Environment
					.getExternalStorageDirectory().getPath() + "/zzkt/";
			File file = null;
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				// 有sd卡，是否有myImage文件夹
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				// 是否有headImg文件
				file = new File(sdcardPathDir + System.currentTimeMillis()
						+ ".JPEG");
			}
			if (file != null) {
				path = file.getPath();
				photoUri = Uri.fromFile(file);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private Dialog d;
	private int i = -1;
	private Button btn_delete_photo;
	private ImageView iv;
	@SuppressLint("InflateParams")
	private void showDialog(final String path1, final int location) {
		// TODO Auto-generated method stub
		d = null;
		if (null == d) {
			View v = LayoutInflater.from(NewHomeWorkDetailActivity.this).inflate(
					R.layout.see_photo, null);
			d = new Dialog(NewHomeWorkDetailActivity.this, R.style.dialogss);
			d.addContentView(v, new RelativeLayout.LayoutParams(NewHomeWorkDetailActivity.this
					.getWindowManager().getDefaultDisplay().getWidth(),
					NewHomeWorkDetailActivity.this.getWindowManager().getDefaultDisplay()
							.getHeight()));
			iv = (ImageView) v.findViewById(R.id.see_photo);
			btn_delete_photo = (Button) v.findViewById(R.id.btn_delete_photo);
			if (i == 0) {
				btn_delete_photo.setVisibility(View.GONE);
			} else if (i == 1) {
				btn_delete_photo.setVisibility(View.VISIBLE);
			}
			iv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					d.dismiss();
				}
			});
		}
		btn_delete_photo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				d.dismiss();
				fileList.remove(location);
				newCameraAdapter.notifyDataSetChanged();
			}
		});
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.build();
		if (path1.contains("http")) {
			imageLoader.displayImage(path1, iv, options);
		} else {
			imageLoader.displayImage("file:/" + path1, iv, options);
		}
		// BitmapUtils utils = new BitmapUtils(getActivity());
		// utils.display(iv, path1);
		d.show();
	}

	public Uri geturi(Intent intent) {
		Uri uri = intent.getData();
		String type = intent.getType();
		if (uri.getScheme().equals("file") && (type.contains("image/"))) {
			String path = uri.getEncodedPath();
			if (path != null) {
				path = Uri.decode(path);
				ContentResolver cr = NewHomeWorkDetailActivity.this.getContentResolver();
				StringBuffer buff = new StringBuffer();
				buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
						.append("'" + path + "'").append(")");
				Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						new String[] { MediaStore.Images.ImageColumns._ID },
						buff.toString(), null, null);
				int index = 0;
				for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
					index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
					// set _id value
					index = cur.getInt(index);
				}
				if (index == 0) {
					// do nothing
				} else {
					Uri uri_temp = Uri
							.parse("content://media/external/images/media/"
									+ index);
					if (uri_temp != null) {
						uri = uri_temp;
						Log.i("urishi", uri.toString());
					}
				}
			}
		}
		return uri;
	}
	//----------------------图片展示Over--------------------------------
	// -----------------------------录音--------------------------
	private Button record;
	private Dialog dialog;
	private AudioRecorder mr;
	private MediaPlayer mediaPlayer;
	private Button player;
	private Thread recordThread;

	private static int MAX_TIME = 60; // 最长录制时间，单位秒，0为无时间限制
	private static int MIX_TIME = 1; // 最短录制时间，单位秒，0为无时间限制，建议设为1

	private static int RECORD_NO = 0; // 不在录音
	private static int RECORD_ING = 1; // 正在录音
	private static int RECODE_ED = 2; // 完成录音

	private static int RECODE_STATE = 0; // 录音的状态

	private static float recodeTime = 0.0f; // 录音的时间
	private static double voiceValue = 0.0; // 麦克风获取的音量值
	private ImageView dialog_img;
	private static boolean playState = false; // 播放状态

	private void initRecord() {
		player = (Button) findViewById(R.id.record_play);
		// 播放
		if (new File(RecordPath).exists()) {
			player.setBackgroundResource(R.mipmap.play);
		}
		player.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!playState) {
					mediaPlayer = new MediaPlayer();
					// String url = "file:///sdcard/zzkt/voice.amr";
					try {
						// 模拟器里播放传url，真机播放传getAmrPath()
						// mediaPlayer.setDataSource(url);
						mediaPlayer.setDataSource(getAmrPath());
						mediaPlayer.prepare();
						mediaPlayer.start();
						player.setBackgroundResource(R.mipmap.play_down);
						playState = true;
						// 设置播放结束时监听
						mediaPlayer
								.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

									@Override
									public void onCompletion(MediaPlayer mp) {
										if (playState) {
											player.setBackgroundResource(R.mipmap.play);
											playState = false;
										}
									}
								});
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.stop();
						playState = false;
					} else {
						playState = false;
					}
					player.setBackgroundResource(R.mipmap.play);
				}

			}
		});
		record = (Button)findViewById(R.id.record_image);

		// 录音
		record.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							if (RECODE_STATE != RECORD_ING) {
								scanOldFile();
								mr = new AudioRecorder("voice");
								RECODE_STATE = RECORD_ING;
								record.setBackgroundResource(R.mipmap.yuyin_down);
								showVoiceDialog();
								try {
									mr.start();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								mythread();
							}

							break;
						case MotionEvent.ACTION_UP:
							if (RECODE_STATE == RECORD_ING) {
								RECODE_STATE = RECODE_ED;
								if (dialog.isShowing()) {
									dialog.dismiss();
								}
								try {
									Thread.sleep(1000);
									mr.stop();
									record.setBackgroundResource(R.mipmap.yuyin);
									voiceValue = 0.0;
								} catch (IOException e) {
									e.printStackTrace();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}

								if (recodeTime < MIX_TIME) {
									showWarnToast();
									// record.setText("按住开始录音");
									RECODE_STATE = RECORD_NO;
								} else {
									// record.setText("录音完成!点击重新录音");
									Log.v("TAG", getAmrPath());
									player.setVisibility(View.VISIBLE);
									player.setBackgroundResource(R.mipmap.play);
								}
							}

							break;
					}
					return false;
			}
		});
	}

	// 删除老文件
	static void scanOldFile() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"zzkt/voice.mp3");
		if (file.exists()) {
			file.delete();
		}
	}

	// 录音时显示Dialog
	void showVoiceDialog() {
		dialog = new Dialog(NewHomeWorkDetailActivity.this, R.style.DialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.my_dialog);
		dialog_img = (ImageView) dialog.findViewById(R.id.dialog_img);
		dialog.show();
	}

	// 录音时间太短时Toast显示
	void showWarnToast() {
		Toast toast = new Toast(NewHomeWorkDetailActivity.this);
		LinearLayout linearLayout = new LinearLayout(NewHomeWorkDetailActivity.this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setPadding(20, 20, 20, 20);

		// 定义一个ImageView
		ImageView imageView = new ImageView(NewHomeWorkDetailActivity.this);
		imageView.setImageResource(R.mipmap.voice_to_short); // 图标

		TextView mTv = new TextView(NewHomeWorkDetailActivity.this);
		mTv.setText("时间太短   录音失败");
		mTv.setTextSize(14);
		mTv.setTextColor(Color.WHITE);// 字体颜色
		// mTv.setPadding(0, 10, 0, 0);

		// 将ImageView和ToastView合并到Layout中
		linearLayout.addView(imageView);
		linearLayout.addView(mTv);
		linearLayout.setGravity(Gravity.CENTER);// 内容居中
		linearLayout.setBackgroundResource(R.mipmap.record_bg);// 设置自定义toast的背景

		toast.setView(linearLayout);
		toast.setGravity(Gravity.CENTER, 0, 0);// 起点位置为中间 100为向下移100dp
		toast.show();
	}

	// 获取文件手机路径
	private String getAmrPath() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"zzkt/voice.mp3");
		return file.getAbsolutePath();
	}

	// 录音计时线程
	void mythread() {
		recordThread = new Thread(ImgThread);
		recordThread.start();
	}

	// 录音Dialog图片随声音大小切换
	void setDialogImage() {
		if (voiceValue < 200.0) {
			dialog_img.setImageResource(R.mipmap.record_animate_01);
		} else if (voiceValue > 200.0 && voiceValue < 400) {
			dialog_img.setImageResource(R.mipmap.record_animate_02);
		} else if (voiceValue > 400.0 && voiceValue < 800) {
			dialog_img.setImageResource(R.mipmap.record_animate_03);
		} else if (voiceValue > 800.0 && voiceValue < 1600) {
			dialog_img.setImageResource(R.mipmap.record_animate_04);
		} else if (voiceValue > 1600.0 && voiceValue < 3200) {
			dialog_img.setImageResource(R.mipmap.record_animate_05);
		} else if (voiceValue > 3200.0 && voiceValue < 5000) {
			dialog_img.setImageResource(R.mipmap.record_animate_06);
		} else if (voiceValue > 5000.0 && voiceValue < 7000) {
			dialog_img.setImageResource(R.mipmap.record_animate_07);
		} else if (voiceValue > 7000.0 && voiceValue < 10000.0) {
			dialog_img.setImageResource(R.mipmap.record_animate_08);
		} else if (voiceValue > 10000.0 && voiceValue < 14000.0) {
			dialog_img.setImageResource(R.mipmap.record_animate_09);
		} else if (voiceValue > 14000.0 && voiceValue < 17000.0) {
			dialog_img.setImageResource(R.mipmap.record_animate_10);
		} else if (voiceValue > 17000.0 && voiceValue < 20000.0) {
			dialog_img.setImageResource(R.mipmap.record_animate_11);
		} else if (voiceValue > 20000.0 && voiceValue < 24000.0) {
			dialog_img.setImageResource(R.mipmap.record_animate_12);
		} else if (voiceValue > 24000.0 && voiceValue < 28000.0) {
			dialog_img.setImageResource(R.mipmap.record_animate_13);
		} else if (voiceValue > 28000.0) {
			dialog_img.setImageResource(R.mipmap.record_animate_14);
		}
	}

	// 录音线程
	private Runnable ImgThread = new Runnable() {

		@Override
		public void run() {
			recodeTime = 0.0f;
			while (RECODE_STATE == RECORD_ING) {
				if (recodeTime >= MAX_TIME && MAX_TIME != 0) {
					imgHandle.sendEmptyMessage(0);
				} else {
					try {
						Thread.sleep(200);
						recodeTime += 0.2;
						if (RECODE_STATE == RECORD_ING) {
							voiceValue = mr.getAmplitude();
							imgHandle.sendEmptyMessage(1);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		Handler imgHandle = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
					case 0:
						// 录音超过15秒自动停止ֹ
						if (RECODE_STATE == RECORD_ING) {
							RECODE_STATE = RECODE_ED;
							if (dialog.isShowing()) {
								dialog.dismiss();
							}
							try {
								mr.stop();
								voiceValue = 0.0;
							} catch (IOException e) {
								e.printStackTrace();
							}

							if (recodeTime < 1.0) {
								showWarnToast();
								record.setText("按住开始录音");
								RECODE_STATE = RECORD_NO;
							} else {
								record.setText("录音完成!点击重新录音");
							}
						}
						break;
					case 1:
						setDialogImage();
						break;
					default:
						break;
				}

			}
		};
	};

	private String RecordPath = Environment.getExternalStorageDirectory()
			+ File.separator + "zzkt/voice.mp3";

	//----------------------录音Over--------------------------------

	//----------------------录像--------------------------------

	public void initVedioView() {
		if (!TextUtils.isEmpty(videoPath)) {
			File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator + "zzkt/video/");
			if (!sampleDir.exists()) {
				sampleDir.mkdirs();
			}
			String name=videoPath.substring(videoPath.lastIndexOf("/")+1,videoPath.indexOf(".mp4")+4);
			Log.v("wwwwwwwwwwwwwwwww","ssssssssssssssssssssss;;;;;"+name);
			final File cache=new File(sampleDir,name);
			if (cache.exists()){
				videoPath=cache.getAbsolutePath();
				Log.v("wwwwwwwwwwwwwwwww","ssssssssssssssssssssss");
			}else {
//				ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
//				zzktHttpDownLoad.downloads(Config1.getInstance().IP + videoPath, cache.getAbsolutePath(), new Callback() {
//					@Override
//					public void onFailure(Call call, IOException e) {
//
//					}
//
//					@Override
//					public void onResponse(Call call, Response response) throws IOException {
//						videoPath=cache.getAbsolutePath();
//						Log.v("wwwwwwwwwwwwwwwww","wwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
//					}
//				});
			}
			// 播放相应的视频
			mVideoView.setMediaController(new MediaController(NewHomeWorkDetailActivity.this));
			mVideoView.setOnCompletionListener(NewHomeWorkDetailActivity.this);
			mVideoView.setVideoURI(Uri.parse(videoPath));
			mVideoView.start();
			recordPlayIv.setVisibility(View.GONE);

		}
	}

	public void playGone() {
		// TODO Auto-generated method stub
		recordPlayIv.setVisibility(View.GONE);
	}

	public void playVisible() {
		// TODO Auto-generated method stub
		recordPlayIv.setVisibility(View.VISIBLE);
	}

	@SuppressLint("NewApi")
	public void setBitamap() {
		// TODO Auto-generated method stub
		Bitmap bitmap = Utils.createVideoThumbnail(videoPath);
		BitmapDrawable drawable = new BitmapDrawable(bitmap);
		recordPlayIv.setBackground(drawable);
	}
	@Override
	public void onCompletion(MediaPlayer player) {
		// TODO Auto-generated method stub
		recordPlayIv.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mediaPlayer!=null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
				playState = false;
			}
		}
		if (mVideoView!=null) {
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
			}
		}
	}
}
