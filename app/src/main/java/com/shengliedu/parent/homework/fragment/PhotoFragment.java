package com.shengliedu.parent.homework.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.CameraAdapter;
import com.shengliedu.parent.bean.HomeWorkDetailSub;
import com.shengliedu.parent.bean.StudentAnswer;
import com.shengliedu.parent.homework.HomeWorkDetailActivity;
import com.shengliedu.parent.synclass.FlashHtmlActivity;
import com.shengliedu.parent.synclass.interf.NextOrLast;
import com.shengliedu.parent.util.CallOtherOpeanFile;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.util.UploadUtil;
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

public class PhotoFragment extends Fragment implements OnClickListener {
	private View view;
	private TextView camera_biaoti, camera_link, camera_file, camera_content;
	private RelativeLayout shang, xia;
	private Button shangchuan;

	private HomeWorkDetailSub homeWorkDetailSub;
	private String coursewareContentId;
	private String hourId;
	private String content_id;
	private String studentId;
	private String date;
	private NextOrLast nextOrLast;

	private GridView gridView;
	private CameraAdapter adapter;
	private List<String> fileList = new ArrayList<String>();

	private PopupWindow pop;
	private Dialog d;
	private ImageView iv;
	private Button btn_delete_photo;
	private int i = -1;
	private int count = 0;

	@SuppressLint("ValidFragment")
	public PhotoFragment(HomeWorkDetailSub homeWorkDetailSub,
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
	public PhotoFragment(){
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_photo, container, false);
		nextOrLast = (NextOrLast) getActivity();
		initViews();
		return view;
	}

	private void showWarning() {
		CustomDialog.Builder customDialog = new CustomDialog.Builder(
				getActivity());
		customDialog.setMessage("请注意,再次修改提交会删除原来的作业\n是否继续？");
		customDialog.setTitle("提示");
		customDialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						showPop();
						fileList.clear();
						fileList.add(R.mipmap.camera_jiahao + "");
						adapter.notifyDataSetChanged();
					}
				});
		customDialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		customDialog.create().show();
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

	private void doSomething() {
		// TODO Auto-generated method stub
		List<StudentAnswer> fileList1 = homeWorkDetailSub.studentAnswer;
		if (fileList1 != null && fileList1.size() > 0) {
			i = 1;
			count = -1;
			List<String> fileList2 = new ArrayList<String>();
			for (int i = 0; i < fileList1.size(); i++) {
				fileList2.add(Config1.getInstance().IP + fileList1.get(i).link);
			}
			fileList.clear();
			fileList.add(R.mipmap.camera_jiahao + "");
			fileList.addAll(fileList2);
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					if (arg2 == 0) {
							if (homeWorkDetailSub.quality == 0) {
								if (count==-1) {
									showWarning();
								}else {
									showPop();
								}
							} else {
								showWarning2();
							}
					} else {
						showDialog(fileList.get(arg2), arg2);
					}
					count++;
				}
			});
		} else {
			List<String> fileList2 = new ArrayList<String>();
			// for (int i = 0; i < fileList1.size(); i++) {
			// fileList2.add(Config1.getInstance().IP + fileList1.get(i).link);
			// }
			fileList.clear();
			fileList.add(R.mipmap.camera_jiahao + "");
			fileList.addAll(fileList2);
			i = 1;
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					if (arg2 == 0) {
						if (homeWorkDetailSub.quality == 0) {
							if (count==-1) {
								showWarning();
							}else {
								showPop();
							}
						} else {
							showWarning2();
						}
					} else {
						showDialog(fileList.get(arg2), arg2);
					}
					count++;
				}
			});
		}
		adapter.notifyDataSetChanged();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		shangchuan = (Button) view.findViewById(R.id.shangchuan);
		gridView = (GridView) view.findViewById(R.id.gridview_camera);
		camera_biaoti = (TextView) view.findViewById(R.id.camera_biaoti);
		camera_link = (TextView) view.findViewById(R.id.camera_link);
		camera_file = (TextView) view.findViewById(R.id.camera_file);
		camera_content = (TextView) view.findViewById(R.id.camera_content);
		camera_biaoti.setText(homeWorkDetailSub.name);
		if (homeWorkDetailSub.subType == 1) {
			if (!TextUtils.isEmpty(homeWorkDetailSub.file)) {
				camera_file.setVisibility(View.VISIBLE);
				camera_file.setText("点击查看附件");
				camera_file.setOnClickListener(new OnClickListener() {

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
						Log.v("", homeWorkDetailSub.fileHost
								+ "/file/download?id="
								+ homeWorkDetailSub.fileId);
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
				camera_link.setVisibility(View.VISIBLE);
				camera_link.setText(homeWorkDetailSub.link);
				camera_link.setOnClickListener(new OnClickListener() {
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
				camera_content.setVisibility(View.VISIBLE);
				camera_content.setText(homeWorkDetailSub.content);
			}
		}

		shang = (RelativeLayout) view.findViewById(R.id.shang);
		xia = (RelativeLayout) view.findViewById(R.id.xia);
		fileList.add(R.mipmap.camera_jiahao + "");
		adapter = new CameraAdapter(getActivity(), fileList);
		gridView.setAdapter(adapter);
		doSomething();
		shang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				nextOrLast.nextOrlast(1);
			}
		});
		xia.setOnClickListener(new OnClickListener() {

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
				new Thread() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						Log.v("TAG", fileList.toString());
						Log.v("TAG", "fileList=" + fileList.size());
						if (fileList.size() > 1) {
							StringBuffer s = new StringBuffer();
							s.append("[");
							for (int i = fileList.size() - 1; i > 0; i--) {
								if (!fileList.get(i).contains(content_id + "")
										&& !fileList.get(i).contains(
												studentId + "")
										&& (fileList.get(i).endsWith(".jpg")
												|| fileList.get(i).endsWith(
														".JPG")
												|| fileList.get(i).endsWith(
														".JPEG")
												|| fileList.get(i).endsWith(
														".jpeg") || fileList
												.get(i).endsWith(".png"))) {
									File file = new File(fileList.get(i));
									Log.v("TAG", fileList.toString());
									handler.sendEmptyMessage(10);
									UploadUtil u = new UploadUtil();
									String a = u.uploadFile(file, Config1
											.getInstance().FILEUPLOAD(),
											content_id, studentId);
									Log.v("TAG", "a=" + a);
									handler.sendEmptyMessage(11);
									s.append(a);
									if (i > 1 && fileList.get(i - 1) != null) {
										s.append(",");
									}
								}
							}
							s.append("]");
							Message message = Message.obtain();
							message.obj = s.toString();
							message.what = 0;
							if (!"[]".equals(s.toString())) {
								handler.sendMessage(message);
							}
						} else {
							((BaseActivity) getActivity()).toast("no photos");
						}

					}
				}.start();
			}
		});
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
			}else if (msg.what==3){
				count=-1;
				((HomeWorkDetailActivity)getActivity()).result=223;
				((HomeWorkDetailActivity) getActivity()).setBackSetResult(223);
				((HomeWorkDetailActivity) getActivity())
						.toast("上传成功！");
			}else if (msg.what==4){

			}
		}
	};
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				Map<String,Object> map=new HashMap<>();
				map.put("studentId", studentId);
				map.put("questionId", "0");
				map.put("coursewareContentId",
						coursewareContentId);
				map.put("hourId", hourId);
				Log.v("TAG", studentId + "," + coursewareContentId + ","
						+ hourId + "," + (String) msg.obj + "," + date);
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
				((BaseActivity) getActivity())
						.showRoundProcessDialog(getActivity());
			} else if (msg.what == 11) {
				((HomeWorkDetailActivity) getActivity())
						.showRoundProcessDialogCancel();
			}
		}
	};

	@SuppressLint("InflateParams")
	private void showPop() {
		// TODO Auto-generated method stub
		if (pop == null) {
			View v = LayoutInflater.from(getActivity()).inflate(
					R.layout.select_picture, null);

			pop = new PopupWindow(getActivity());
			pop.setContentView(v);
			pop.setWidth(LayoutParams.FILL_PARENT);
			pop.setHeight(LayoutParams.WRAP_CONTENT);
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
		pop.showAtLocation(shangchuan, Gravity.BOTTOM, 0, 0);

	}

	@SuppressLint("InflateParams")
	private void showDialog(final String path1, final int location) {
		// TODO Auto-generated method stub
		d = null;
		if (null == d) {
			View v = LayoutInflater.from(getActivity()).inflate(
					R.layout.see_photo, null);
			d = new Dialog(getActivity(), R.style.dialogss);
			d.addContentView(v, new LayoutParams(getActivity()
					.getWindowManager().getDefaultDisplay().getWidth(),
					getActivity().getWindowManager().getDefaultDisplay()
							.getHeight()));
			iv = (ImageView) v.findViewById(R.id.see_photo);
			btn_delete_photo = (Button) v.findViewById(R.id.btn_delete_photo);
			if (i == 0) {
				btn_delete_photo.setVisibility(View.INVISIBLE);
			} else if (i == 1) {
				btn_delete_photo.setVisibility(View.VISIBLE);
			}
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					d.dismiss();
				}
			});
		}
		btn_delete_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				d.dismiss();
				fileList.remove(location);
				adapter.notifyDataSetChanged();
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
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
					Images.Media.EXTERNAL_CONTENT_URI);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if (resultCode == Activity.RESULT_OK
					&& !TextUtils.isEmpty(photoUri.getPath())) {
				Log.v("TAG", photoUri.getPath());
				count++;
				fileList.add(photoUri.getPath());
				adapter.notifyDataSetChanged();
			}

			break;
		case 1:
			if (data != null) {
				Uri uri = geturi(data);
				Log.v("TAG", "uri:" + uri.getPath());
				if (uri != null) {
					String[] proj = { Images.Media.DATA };
					Cursor actualimagecursor = getActivity()
							.getContentResolver().query(uri, proj, null, null,
									null);
					int actual_image_column_index = actualimagecursor
							.getColumnIndexOrThrow(Images.Media.DATA);
					actualimagecursor.moveToFirst();
					String img_path = actualimagecursor
							.getString(actual_image_column_index);
					Log.v("TAG1", img_path);
					fileList.add(img_path);
					adapter.notifyDataSetChanged();
				}
			}
			break;
		}
	}

	public Uri geturi(Intent intent) {
		Uri uri = intent.getData();
		String type = intent.getType();
		if (uri.getScheme().equals("file") && (type.contains("image/"))) {
			String path = uri.getEncodedPath();
			if (path != null) {
				path = Uri.decode(path);
				ContentResolver cr = getActivity().getContentResolver();
				StringBuffer buff = new StringBuffer();
				buff.append("(").append(Images.ImageColumns.DATA).append("=")
						.append("'" + path + "'").append(")");
				Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI,
						new String[] { Images.ImageColumns._ID },
						buff.toString(), null, null);
				int index = 0;
				for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
					index = cur.getColumnIndex(Images.ImageColumns._ID);
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

}
