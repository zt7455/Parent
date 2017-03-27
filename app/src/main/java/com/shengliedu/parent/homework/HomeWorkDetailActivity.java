package com.shengliedu.parent.homework;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.HomeWorkDetailQ;
import com.shengliedu.parent.bean.HomeWorkDetailSub;
import com.shengliedu.parent.bean.HomeWorkInfo;
import com.shengliedu.parent.homework.fragment.DuoXuanFragment;
import com.shengliedu.parent.homework.fragment.OtherFragment;
import com.shengliedu.parent.homework.fragment.PaiXuFragment;
import com.shengliedu.parent.homework.fragment.PhotoFragment;
import com.shengliedu.parent.homework.fragment.RecordFragment;
import com.shengliedu.parent.homework.fragment.SanLianXianFragment;
import com.shengliedu.parent.homework.fragment.ShuangLianXianFragment;
import com.shengliedu.parent.homework.fragment.SingleChoiceFragment;
import com.shengliedu.parent.homework.fragment.TextFragment;
import com.shengliedu.parent.homework.fragment.VideoFragment;
import com.shengliedu.parent.synclass.interf.NextOrLast;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class HomeWorkDetailActivity extends BaseActivity implements NextOrLast {
	private String studentId;
	private String coursewareContentId;
	private String hourId;
	private String content_id;
	private String content_type;
	private String questionType;
	private String submitType;
	private String questionContentType;//0表示正常题。1表示pdf题
	private String date;
	private String file;
	private String link;
	private String content;
	private String part;

	private HomeWorkDetailSub homeWorkDetailSub;
	private HomeWorkDetailQ homeWorkDetailQ;

	private String videoPath;
	public int result;
	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		studentId = App.childInfo.id + "";
		coursewareContentId = getIntent().getExtras().getString(
				"coursewareContentId");
		hourId = getIntent().getExtras().getString("hourId");
		content_id = getIntent().getExtras().getString("content_id");
		content_type = getIntent().getExtras().getString("content_type");
		questionType = getIntent().getExtras().getString("questionType");
		submitType = getIntent().getExtras().getString("submitType");
		questionContentType = getIntent().getExtras().getString("questionContentType");
		part = getIntent().getExtras().getString("part");
		date = getIntent().getExtras().getString("date");
		file = getIntent().getExtras().getString("file");
		link = getIntent().getExtras().getString("link");
		content = getIntent().getExtras().getString("content");
		setBackSetResult(result);
		showTitle("作业详情");
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				if ("2".equals(content_type)) {
					homeWorkDetailQ = JSON.parseObject(
							result.getString("data"),
							HomeWorkDetailQ.class);
					if (homeWorkDetailQ != null) {
						// ，单选1，多选2，判断3，排序4，连线5
//								if ("0".equals(questionContentType)) {
						if ("1".equals(questionType)
								|| "3".equals(questionType)) {
							// ContentId, awser, sID, hourId,
							// questionId
							getSupportFragmentManager()
									.beginTransaction()
									.replace(
											R.id.fragment_content,
											new SingleChoiceFragment(
													homeWorkDetailQ,
													coursewareContentId,
													studentId, hourId,
													content_id, date,questionContentType))
									.commit();
						} else if ("2".equals(questionType)) {
							getSupportFragmentManager()
									.beginTransaction()
									.replace(
											R.id.fragment_content,
											new DuoXuanFragment(
													homeWorkDetailQ,
													coursewareContentId,
													studentId, hourId,
													content_id, date,questionContentType))
									.commit();
						} else if ("4".equals(questionType)) {
							getSupportFragmentManager()
									.beginTransaction()
									.replace(
											R.id.fragment_content,
											new PaiXuFragment(
													homeWorkDetailQ,
													coursewareContentId,
													studentId, hourId,
													content_id, date,questionContentType))
									.commit();
						} else if ("5".equals(questionType)
								&& "2".equals(homeWorkDetailQ.answer.lineCount)) {
							getSupportFragmentManager()
									.beginTransaction()
									.replace(
											R.id.fragment_content,
											new ShuangLianXianFragment(
													homeWorkDetailQ,
													coursewareContentId,
													studentId, hourId,
													content_id, date,questionContentType))
									.commit();
						} else if ("5".equals(questionType)
								&& "3".equals(homeWorkDetailQ.answer.lineCount)) {
							getSupportFragmentManager()
									.beginTransaction()
									.replace(
											R.id.fragment_content,
											new SanLianXianFragment(
													homeWorkDetailQ,
													coursewareContentId,
													studentId, hourId,
													content_id, date,questionContentType)).commit();
						}else if ("10".equals(questionType)) {

						}
					} else {
						toast("暂无数据");
					}
				} else if ("1".equals(content_type)) {
					// } else {
					homeWorkDetailSub = JSON.parseObject(
							result.getString("data"),
							HomeWorkDetailSub.class);
					if (homeWorkDetailSub != null) {
						// 1文本，2图片，3音频，4视频（对应题干后面的小图标） ----提交类型
						// 5 ----其他类型
						// ---------1文本，2图片，3音频，4视频（对应题干后面的小图标）
						// ----无需提交类型
						//subType  1, fileId .  4, link .  5  文本
						if ("1".equals(submitType)) {
							getSupportFragmentManager()
									.beginTransaction()
									.replace(
											R.id.fragment_content,
											new TextFragment(
													homeWorkDetailSub,
													coursewareContentId,
													studentId, hourId,
													content_id, date))
									.commit();
						} else if ("2".equals(submitType)) {
							getSupportFragmentManager()
									.beginTransaction()
									.replace(
											R.id.fragment_content,
											new PhotoFragment(
													homeWorkDetailSub,
													coursewareContentId,
													studentId, hourId,
													content_id, date))
									.commit();
						} else if ("3".equals(submitType)) {
							getSupportFragmentManager()
									.beginTransaction()
									.replace(
											R.id.fragment_content,
											new RecordFragment(
													homeWorkDetailSub,
													coursewareContentId,
													studentId, hourId,
													content_id, date))
									.commit();
						} else if ("4".equals(submitType)) {
							fragment = new VideoFragment(
									homeWorkDetailSub,
									coursewareContentId, studentId,
									hourId, content_id, date);
							getSupportFragmentManager()
									.beginTransaction()
									.replace(R.id.fragment_content,
											fragment).commit();
						} else if ("5".equals(submitType)) {
							getSupportFragmentManager()
									.beginTransaction()
									.replace(
											R.id.fragment_content,
											new OtherFragment(
													homeWorkDetailSub,
													coursewareContentId,
													studentId, hourId,
													content_id, date))
									.commit();
						}
						if (homeWorkDetailSub.quality == 1) {
							setRightText("优");
						} else if (homeWorkDetailSub.quality == 2) {
							setRightText("良");
						} else if (homeWorkDetailSub.quality == 2) {
							setRightText("中");
						} else if (homeWorkDetailSub.quality == 2) {
							setRightText("差");
						} else {
							setRightText("");
						}
					} else {
						toast("暂无数据");
					}
				}
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", studentId);
		if ("2".equals(content_type)) {
			map.put("questionId", content_id);
		} else if ("1".equals(content_type)) {
			map.put("questionId", "0");
		}
		map.put("coursewareContentId", coursewareContentId);
		map.put("hourId", hourId);
		map.put("date", date);
		map.put("part", part);
		doGet(Config1.getInstance().HOMEWORKDETAIL(), map,
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
						handlerReq.sendEmptyMessage(2);
					}
				});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_homework_detail;
	}

	@Override
	public void nextOrlast(int nol) {
		// TODO Auto-generated method stub
		switch (nol) {
		case 1:// last
			Log.v("TAG", "content_type=" + content_type);
			if ("2".equals(content_type)) {
				if (homeWorkDetailQ != null) {
					HomeWorkInfo info = homeWorkDetailQ.phw;
					if (info == null) {
						toast("已经是第一题了");
					} else {
						coursewareContentId = info.coursewareContentId + "";
						content_id = info.content_id + "";
						content_type = info.content_type + "";
						submitType = info.submitType + "";
						questionType = info.questionType + "";
						hourId = info.hourId + "";
						getDatas();
					}
				}
			} else if ("1".equals(content_type)) {
				if (homeWorkDetailSub != null) {
					HomeWorkInfo info = homeWorkDetailSub.phw;
					if (info == null) {
						toast("已经是第一题了");
					} else {
						coursewareContentId = info.coursewareContentId + "";
						content_id = info.content_id + "";
						content_type = info.content_type + "";
						submitType = info.submitType + "";
						questionType = info.questionType + "";
						hourId = info.hourId + "";
						getDatas();
					}
				}
			}
			break;
		case 2:// next
			if ("2".equals(content_type)) {
				if (homeWorkDetailQ != null) {
					HomeWorkInfo info = homeWorkDetailQ.nhw;
					if (info == null) {
						toast("已经是最后一题了");
					} else {
						coursewareContentId = info.coursewareContentId + "";
						content_id = info.content_id + "";
						content_type = info.content_type + "";
						submitType = info.submitType + "";
						questionType = info.questionType + "";
						questionContentType = info.questionContentType + "";
						hourId = info.hourId + "";
						getDatas();
					}
				}
			} else if ("1".equals(content_type)) {
				if (homeWorkDetailSub != null) {
					HomeWorkInfo info = homeWorkDetailSub.nhw;
					if (info == null) {
						toast("已经是最后一题了");
					} else {
						coursewareContentId = info.coursewareContentId + "";
						content_id = info.content_id + "";
						content_type = info.content_type + "";
						submitType = info.submitType + "";
						questionType = info.questionType + "";
						questionContentType = info.questionContentType + "";
						hourId = info.hourId + "";
						getDatas();
					}
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private VideoFragment fragment;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 200:
			if (resultCode == RESULT_OK) {
				// 成功
				videoPath = data.getStringExtra("path");
				Toast.makeText(HomeWorkDetailActivity.this,
						"存储路径为:" + videoPath, Toast.LENGTH_SHORT).show();
				fragment.playVisible();
				fragment.setBitamap();
				// 通过路径获取第一帧的缩略图并显示
				// Bitmap bitmap = Utils.createVideoThumbnail(videoPath);
				// BitmapDrawable drawable = new BitmapDrawable(bitmap);
				// drawable.setTileModeXY(Shader.TileMode.REPEAT ,
				// Shader.TileMode.REPEAT);
				// drawable.setDither(true);
				// btnPlay.setBackgroundDrawable(drawable);
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

}
