package com.shengliedu.parent.homework.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.HomeWorkDetailSub;
import com.shengliedu.parent.bean.StudentAnswer;
import com.shengliedu.parent.homework.HomeWorkDetailActivity;
import com.shengliedu.parent.synclass.FlashHtmlActivity;
import com.shengliedu.parent.synclass.interf.NextOrLast;
import com.shengliedu.parent.util.CallOtherOpeanFile;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;
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

public class TextFragment extends Fragment {
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

	private EditText editText;

	@SuppressLint("ValidFragment")
	public TextFragment(HomeWorkDetailSub homeWorkDetailSub,
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
	public TextFragment(){
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_text, container, false);
		nextOrLast = (NextOrLast) getActivity();
		initViews();
		return view;
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
	private void initViews() {
		// TODO Auto-generated method stub
		editText = (EditText) view.findViewById(R.id.text_edit);
		doSomething();
		shangchuan = (Button) view.findViewById(R.id.shangchuan);
		record_biaoti = (TextView) view.findViewById(R.id.record_biaoti);
		record_link = (TextView) view.findViewById(R.id.record_link);
		record_file = (TextView) view.findViewById(R.id.record_file);
		record_content = (TextView) view.findViewById(R.id.record_content);
		record_biaoti.setText(homeWorkDetailSub.name);
		if (homeWorkDetailSub.subType==1) {
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
								.getExternalStorageDirectory().getAbsolutePath()
								+ "/zzkt/parent/"
								+ homeWorkDetailSub.file
										.substring(homeWorkDetailSub.file
												.lastIndexOf("/") + 1));
						if (!file.exists()) {
							((HomeWorkDetailActivity) getActivity())
									.showRoundProcessDialog(getActivity());
							ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
							zzktHttpDownLoad.downloads(
									"http://"+homeWorkDetailSub.fileHost
									+ "/file/download?id="
									+ homeWorkDetailSub.fileId
									,
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
											message.what=1;
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
											handlerReq.sendEmptyMessage(2);
										}
									});

						} else {
							CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
							callOtherOpeanFile.openFile(getActivity(), file);
						}
					}
				});
			}
		}else if (homeWorkDetailSub.subType==4) {
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
		}else if (homeWorkDetailSub.subType==5) {
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
				if (!TextUtils.isEmpty(editText.getText().toString())) {
					if (homeWorkDetailSub.quality == 0) {
						sendMessage("[{\"content\":\""
								+ editText.getText().toString() + "\"}]");
					} else {
						showWarning2();
					}
				} else {
					Toast.makeText(getActivity(), "请输入文本信息！", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void doSomething() {
		// TODO Auto-generated method stub
		List<StudentAnswer> list = homeWorkDetailSub.studentAnswer;
		if (list != null && list.size() > 0
				&& !TextUtils.isEmpty(list.get(0).content)) {
			String content = list.get(0).content;
			editText.setText(content);
			editText.clearFocus();
		}
	}

	public void sendMessage(String msg) {
		Map<String,Object> map=new HashMap<>();
		map.put("studentId", studentId);
		map.put("questionId", "0");
		map.put("coursewareContentId", coursewareContentId);
		map.put("hourId", hourId);
		map.put("studentAnswer", msg);
		map.put("date", date);
		((HomeWorkDetailActivity) getActivity()).doPost(Config1.getInstance()
				.HOMEWORKSUBMIT(), map, new ResultCallback() {
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
				((HomeWorkDetailActivity) getActivity()).toast("上传成功！");
			}else if (msg.what==4){

			}
		}
	};
}
