package com.shengliedu.parent.homework.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.HomeWorkDetailSub;
import com.shengliedu.parent.bean.StudentAnswer;
import com.shengliedu.parent.homework.HomeWorkDetailActivity;
import com.shengliedu.parent.homework.MovieActivity;
import com.shengliedu.parent.synclass.FlashHtmlActivity;
import com.shengliedu.parent.synclass.interf.NextOrLast;
import com.shengliedu.parent.util.CallOtherOpeanFile;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.util.UploadUtil;
import com.shengliedu.parent.util.Utils;
import com.shengliedu.parent.util.ZZKTHttpDownLoad;
import com.shengliedu.parent.widght.CustomDialog;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class VideoFragment extends Fragment implements OnCompletionListener {
	private View view;
	private TextView record_biaoti, record_link, record_file, record_content;
	private Button shangchuan;
	private RelativeLayout select_last, select_next;

	private HomeWorkDetailSub homeWorkDetailSub;
	private String coursewareContentId;
	private String hourId;
	private String content_id;
	private String studentId;
	private String date;
	private NextOrLast nextOrLast;
	private VideoView mVideoView;
	private ImageView recordIv;
	private ImageButton recordPlayIv;

	@SuppressLint("ValidFragment")
	public VideoFragment(HomeWorkDetailSub homeWorkDetailSub,
						 String coursewareContentId, String studentId, String hourId,
						 String content_id, String date) {
		// TODO Auto-generated constructor stub
		this.homeWorkDetailSub = homeWorkDetailSub;
		this.coursewareContentId = coursewareContentId;
		this.studentId = studentId;
		this.hourId = hourId;
		this.content_id = content_id;
		this.date = date;
	}
	public VideoFragment(){
		super();
	}
	private static final String TAG = "VedioActivity";

	private String videoPath;

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
		videoPath = ((HomeWorkDetailActivity) getActivity()).getVideoPath();
		Bitmap bitmap = Utils.createVideoThumbnail(videoPath);
		BitmapDrawable drawable = new BitmapDrawable(bitmap);
		recordPlayIv.setBackground(drawable);
	}

	/**
	 * 
	 */
	private void initView() {
		mVideoView = (VideoView) view.findViewById(R.id.video_view);
		recordIv = (ImageView) view.findViewById(R.id.recordIv);
		recordPlayIv = (ImageButton) view.findViewById(R.id.recordPlayIv);
		recordIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 启动拍摄的Activity
				if (homeWorkDetailSub.quality == 0) {
					Intent intent = new Intent(getActivity(), MovieActivity.class);
					getActivity().startActivityForResult(intent, 200);
				} else {
					showWarning2();
				}
			}

		});
		recordPlayIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 启动拍摄的Activity
				initVedioView();
			}

		});
		shangchuan = (Button) view.findViewById(R.id.shangchuan);
		record_biaoti = (TextView) view.findViewById(R.id.record_biaoti);
		record_biaoti.setText(homeWorkDetailSub.name);
		select_last = (RelativeLayout) view.findViewById(R.id.select_last1);
		select_next = (RelativeLayout) view.findViewById(R.id.select_next1);
		select_last.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				nextOrLast.nextOrlast(1);
			}
		});
		select_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				nextOrLast.nextOrlast(2);
			}
		});
		shangchuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (homeWorkDetailSub.quality == 0) {
					submitFile();
				} else {
					showWarning2();
				}
			}
		});

	}

	private void showWarning2() {
		CustomDialog.Builder customDialog = new CustomDialog.Builder(
				getActivity());
		customDialog.setMessage("请注意,老师评价完作业不能更改");
		customDialog.setTitle("提示");
		customDialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		customDialog.create().show();
	}

	private void submitFile() {
		// TODO Auto-generated method stub
		videoPath = ((HomeWorkDetailActivity) getActivity()).getVideoPath();
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				if (!TextUtils.isEmpty(videoPath)) {

					StringBuffer s = new StringBuffer();
					s.append("[");
					File file = new File(videoPath);
					if (file.length() > 0) {
						UploadUtil u = new UploadUtil();
						handler.sendEmptyMessage(10);
						String a = u.uploadFile(file, Config1.getInstance()
								.FILEUPLOAD(), content_id, studentId);
						s.append(a);
						handler.sendEmptyMessage(11);
					}
					s.append("]");
					Message message = Message.obtain();
					message.obj = s.toString();
					message.what = 0;
					handler.sendMessage(message);
				}
			}
		}.start();
	}

	public void initVedioView() {
		videoPath = ((HomeWorkDetailActivity) getActivity()).getVideoPath();
		if (!TextUtils.isEmpty(videoPath)) {
			// 播放相应的视频
			mVideoView.setMediaController(new MediaController(getActivity()));
			mVideoView.setOnCompletionListener(this);
			mVideoView.setVideoURI(Uri.parse(videoPath));
			mVideoView.start();
			recordPlayIv.setVisibility(View.GONE);
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				Map<String,Object> map=new HashMap<>();
				map.put("studentId", studentId);
				map.put("questionId", "0");
				map.put("coursewareContentId",
						coursewareContentId);
				map.put("hourId", hourId);
				map.put("studentAnswer", (String) msg.obj);
				map.put("date", date);
				((HomeWorkDetailActivity) getActivity()).doPost(Config1
						.getInstance().HOMEWORKSUBMIT(), map,
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
			} else if (msg.what == 10) {
				((HomeWorkDetailActivity) getActivity())
						.showRoundProcessDialog(getActivity());
			} else if (msg.what == 11) {
				((HomeWorkDetailActivity) getActivity())
						.showRoundProcessDialogCancel();
			}
		}
	};
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				((HomeWorkDetailActivity)getActivity()).result=223;
				((HomeWorkDetailActivity) getActivity()).setBackSetResult(223);
				((HomeWorkDetailActivity) getActivity())
						.toast("上传成功！");
			}else if (msg.what==2){

			}else if (msg.what==3){
				((HomeWorkDetailActivity) getActivity())
						.showRoundProcessDialogCancel();
				CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
				callOtherOpeanFile.openFile(
						getActivity(), (File) msg.obj);
			}else if (msg.what==4){
				((HomeWorkDetailActivity) getActivity())
						.showRoundProcessDialogCancel();
			}
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_video, container, false);
		// 选择支持半透明模式,在有surfaceview的activity中使用。
		nextOrLast = (NextOrLast) getActivity();
		getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
		doSomething();
		initView();
		return view;
	}

	private void doSomething() {
		// TODO Auto-generated method stub
		record_link = (TextView) view.findViewById(R.id.record_link);
		record_file = (TextView) view.findViewById(R.id.record_file);
		record_content = (TextView) view.findViewById(R.id.record_content);
		if (homeWorkDetailSub.subType == 1) {
			if (!TextUtils.isEmpty(homeWorkDetailSub.file)) {
				record_file.setVisibility(View.VISIBLE);
				record_file.setText("点击查看附件");
				record_file.setOnClickListener(new OnClickListener() {

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
						File file = new File(Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/zzkt/parent/"
								+ homeWorkDetailSub.file
										.substring(homeWorkDetailSub.file
												.lastIndexOf("/") + 1));
						if (!file.exists()) {
							((HomeWorkDetailActivity) getActivity())
									.showRoundProcessDialog(getActivity());
							ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
							zzktHttpDownLoad.downloads(
									"http://" + homeWorkDetailSub.fileHost
											+ "/file/download?id="
											+ homeWorkDetailSub.fileId,
									Environment.getExternalStorageDirectory()
											.getAbsolutePath()
											+ "/zzkt/parent/"
											+ homeWorkDetailSub.file
													.substring(homeWorkDetailSub.file
															.lastIndexOf("/") + 1),
									new Callback() {
										@Override
										public void onFailure(Call call, IOException e) {
											Message message=Message.obtain();
											message.what=3;
											message.obj=new File(Environment.getExternalStorageDirectory()
													.getAbsolutePath()
													+ "/zzkt/parent/"
													+ homeWorkDetailSub.file
													.substring(homeWorkDetailSub.file
															.lastIndexOf("/") + 1));
											handlerReq.sendMessage(message);
										}

										@Override
										public void onResponse(Call call, Response response) throws IOException {
											handlerReq.sendEmptyMessage(4);
										}
									});

						} else {
							CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
							callOtherOpeanFile.openFile(getActivity(), file);
						}
					}
				});
			}
		} else if (homeWorkDetailSub.subType == 4) {
			if (!TextUtils.isEmpty(homeWorkDetailSub.link)) {
				record_link.setVisibility(View.VISIBLE);
				record_link.setText(homeWorkDetailSub.link);
				record_link.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putString("plan_info", "链接");
						bundle.putString("html", homeWorkDetailSub.link);
						bundle.putString("content_host", "");
						intent.putExtras(bundle);
						intent.setClass(getActivity(), FlashHtmlActivity.class);
						getActivity().startActivity(intent);
					}
				});
			}
		} else if (homeWorkDetailSub.subType == 5) {
			if (!TextUtils.isEmpty(homeWorkDetailSub.content)) {
				record_content.setVisibility(View.VISIBLE);
				record_content.setText(homeWorkDetailSub.content);
			}
		}

		List<StudentAnswer> list = homeWorkDetailSub.studentAnswer;
		if (list != null && list.size() > 0) {
			((HomeWorkDetailActivity) getActivity()).setVideoPath(Config1
					.getInstance().IP + list.get(0).link);
		}
	}

	@Override
	public void onCompletion(MediaPlayer player) {
		// TODO Auto-generated method stub
		recordPlayIv.setVisibility(View.VISIBLE);
	}
}
