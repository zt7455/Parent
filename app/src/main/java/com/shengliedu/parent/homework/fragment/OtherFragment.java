package com.shengliedu.parent.homework.fragment;

import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.HomeWorkDetailSub;
import com.shengliedu.parent.homework.HomeWorkDetailActivity;
import com.shengliedu.parent.synclass.FlashHtmlActivity;
import com.shengliedu.parent.synclass.interf.NextOrLast;
import com.shengliedu.parent.util.CallOtherOpeanFile;
import com.shengliedu.parent.util.ZZKTHttpDownLoad;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OtherFragment extends Fragment {
	private View view;
	private TextView record_biaoti, record_link, record_file, record_content;
	private RelativeLayout select_last, select_next;

	private HomeWorkDetailSub homeWorkDetailSub;
	private String coursewareContentId;
	private String hourId;
	private String content_id;
	private String studentId;
	private String date;
	private NextOrLast nextOrLast;

	@SuppressLint("ValidFragment")
	public OtherFragment(HomeWorkDetailSub homeWorkDetailSub,
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
	public OtherFragment(){
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_other, container, false);
		nextOrLast = (NextOrLast) getActivity();
		initViews();
		return view;
	}
	Handler handlerReq = new Handler(){
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
			}
		}
	};
	private void initViews() {
		// TODO Auto-generated method stub
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
							String downLoadUrl = homeWorkDetailSub.file;
							if (homeWorkDetailSub.file.endsWith(".doc")
									|| homeWorkDetailSub.file.endsWith(".docx")
									|| homeWorkDetailSub.file.endsWith(".ppt")
									|| homeWorkDetailSub.file.endsWith(".pptx")
									|| homeWorkDetailSub.file.endsWith(".xls")
									|| homeWorkDetailSub.file.endsWith(".xlsx")) {
								downLoadUrl = "http://"
										+ homeWorkDetailSub.fileHost
										+ "/file/download/id="
										+ homeWorkDetailSub.fileId;
							}
							ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
							zzktHttpDownLoad.downloads(
									downLoadUrl,
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
	}
}
