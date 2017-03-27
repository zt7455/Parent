package com.shengliedu.parent.homework.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.HomeWorkDetailSub;
import com.shengliedu.parent.bean.StudentAnswer;
import com.shengliedu.parent.homework.HomeWorkDetailActivity;
import com.shengliedu.parent.synclass.FlashHtmlActivity;
import com.shengliedu.parent.synclass.interf.NextOrLast;
import com.shengliedu.parent.util.AudioRecorder;
import com.shengliedu.parent.util.CallOtherOpeanFile;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.util.UploadUtil;
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

public class RecordFragment extends Fragment {
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

	@SuppressLint("ValidFragment")
	public RecordFragment(HomeWorkDetailSub homeWorkDetailSub,
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
	public RecordFragment(){
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_record, container, false);
		nextOrLast = (NextOrLast) getActivity();
		initViews();
		return view;
	}

	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				((HomeWorkDetailActivity) getActivity())
						.showRoundProcessDialogCancel();
				CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
				callOtherOpeanFile.openFile(
						getActivity(), (File) msg.obj);
			}else if (msg.what==2){
				((HomeWorkDetailActivity) getActivity())
						.showRoundProcessDialogCancel();
			}else if (msg.what==3){
				((HomeWorkDetailActivity)getActivity()).result=223;
				((HomeWorkDetailActivity) getActivity()).setBackSetResult(223);
				((HomeWorkDetailActivity) getActivity())
						.toast("上传成功！");
			}else if (msg.what==4){
			}else if (msg.what==5){
				initRecord();
			}else if (msg.what==6){
				initRecord();
			}
		}
	};
	private void initViews() {
		// TODO Auto-generated method stub
		doSomething();
		shangchuan = (Button) view.findViewById(R.id.shangchuan);
		record_biaoti = (TextView) view.findViewById(R.id.record_biaoti);
		record_link = (TextView) view.findViewById(R.id.record_link);
		record_file = (TextView) view.findViewById(R.id.record_file);
		record_content = (TextView) view.findViewById(R.id.record_content);
		record_biaoti.setText(homeWorkDetailSub.name);
		if (homeWorkDetailSub.subType == 1) {
			if (!TextUtils.isEmpty(homeWorkDetailSub.file)) {
				record_file.setVisibility(View.VISIBLE);
				record_file.setText("点击查看附件");
				record_file.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						File file0 = new File(Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/zzkt/parent/");
						if (!file0.exists()){
							file0.mkdirs();
						}
						// TODO Auto-generated method stub
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
											handlerReq.sendEmptyMessage(2);
										}

										@Override
										public void onResponse(Call call, Response response) throws IOException {
											Message message = Message.obtain();
											message.what = 1;
											message.obj = new File(Environment.getExternalStorageDirectory()
													.getAbsolutePath()
													+ "/zzkt/parent/"
													+ homeWorkDetailSub.file
													.substring(homeWorkDetailSub.file
															.lastIndexOf("/") + 1));
											handlerReq.sendMessage(message);
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
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				StringBuffer s = new StringBuffer();
				s.append("[");
				if (getAmrPath().endsWith(".3gp")
						|| getAmrPath().endsWith(".amr")
						|| getAmrPath().endsWith(".mp3")) {
					File file = new File(getAmrPath());
					Log.v("TAG", getAmrPath().toString());
					handler.sendEmptyMessage(10);
					UploadUtil u = new UploadUtil();
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
		}.start();
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
								message.what=3;
								message.obj=json;
								handlerReq.sendMessage(message);
							}

							@Override
							public void onFailure(Call call, IOException exception) {
								handlerReq.sendEmptyMessage(4);
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
		player = (Button) view.findViewById(R.id.record_play);
		// 播放
		if (new File(RecordPath).exists()) {
			player.setBackgroundResource(R.mipmap.play);
		}
		player.setOnClickListener(new OnClickListener() {
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
								.setOnCompletionListener(new OnCompletionListener() {

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
		record = (Button) view.findViewById(R.id.record_image);

		// 录音
		record.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (homeWorkDetailSub.quality == 0) {
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
				} else {
					showWarning2();
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
		dialog = new Dialog(getActivity(), R.style.DialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.my_dialog);
		dialog_img = (ImageView) dialog.findViewById(R.id.dialog_img);
		dialog.show();
	}

	// 录音时间太短时Toast显示
	void showWarnToast() {
		Toast toast = new Toast(getActivity());
		LinearLayout linearLayout = new LinearLayout(getActivity());
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setPadding(20, 20, 20, 20);

		// 定义一个ImageView
		ImageView imageView = new ImageView(getActivity());
		imageView.setImageResource(R.mipmap.voice_to_short); // 图标

		TextView mTv = new TextView(getActivity());
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

	private void doSomething() {
		// // TODO Auto-generated method stub
		// File file=new File(RecordPath);
		// if (!file.exists()) {
		// try {
		// file.createNewFile();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		List<StudentAnswer> list = homeWorkDetailSub.studentAnswer;
		if (list != null && list.size() > 0
				&& !TextUtils.isEmpty(list.get(0).link)) {
			String url = Config1.getInstance().IP + list.get(0).link;
			Log.v("TAG", "url=" + url);
			ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
			zzktHttpDownLoad.downloads(url,RecordPath,
					new Callback() {
						@Override
						public void onFailure(Call call, IOException e) {
							handlerReq.sendEmptyMessage(6);
						}

						@Override
						public void onResponse(Call call, Response response) throws IOException {
							handlerReq.sendEmptyMessage(5);
						}
					});

		} else {
			Log.v("TAG", "else=");
			initRecord();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		scanOldFile();
	}
}
