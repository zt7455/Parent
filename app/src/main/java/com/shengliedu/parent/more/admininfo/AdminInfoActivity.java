package com.shengliedu.parent.more.admininfo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.UserInfo;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.FileUtils;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.util.UploadUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class AdminInfoActivity extends BaseActivity {
	private RelativeLayout head_rel, sex_rel, nikename_rel, qianming_rel,
			dizhi_rel;
	private ImageView head_pic;
	private TextView nikename_text, sex_text, qianming_text, dizhi_text;
	private String userId;
	private int sex;
	
	private AlertDialog dialog;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		userId= App.userInfo.id+"";
		setBackSetResult(401);
		showTitle("个人信息");
		head_rel = (RelativeLayout) findViewById(R.id.head_rel1);
		sex_rel = (RelativeLayout) findViewById(R.id.sex_rel);
		nikename_rel = (RelativeLayout) findViewById(R.id.nikename_rel);
		qianming_rel = (RelativeLayout) findViewById(R.id.qianming_rel);
		dizhi_rel = (RelativeLayout) findViewById(R.id.dizhi_rel);
		head_pic = (ImageView) findViewById(R.id.head_pic);
		nikename_text = (TextView)findViewById(R.id.nikename_text);
		sex_text = (TextView) findViewById(R.id.sex_text);
		qianming_text = (TextView) findViewById(R.id.qianming_text);
		dizhi_text = (TextView)findViewById(R.id.dizhi_text);
		setting();
		head_rel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				comfireImgSelection();
			}
		});
		nikename_rel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AdminInfoActivity.this,
						ChangeNikeNameActivity.class);
				startActivityForResultByAniamtion(intent, 400);
			}
		});
		qianming_rel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AdminInfoActivity.this,
						ChangeSignActivity.class);
				startActivityForResultByAniamtion(intent, 400);
			}
		});
		dizhi_rel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AdminInfoActivity.this,
						ChangeAddressActivity.class);
				startActivityForResultByAniamtion(intent, 400);
			}
		});
		sex_rel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String[] sexl = new String[] { "男", "女" };
				Builder builder = new Builder(AdminInfoActivity.this);
				builder.setTitle("性别");
				// 给对话框设定单选列表项
				// 参数一： 选项的值 参数二： 默认选中项的索引 参数三：监测选中的项的监听器
				builder.setSingleChoiceItems(sexl, sex,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								switch (arg1) {
								case 0:
									sex = arg1;
									break;
								case 1:
									sex = arg1;
									break;
								default:
									break;
								}
								Log.v("TAG", "sex="+sex);
								if (sex == App.userInfo.sex) {
									dialog.dismiss();
								} else {
									Map<String,Object> map=new HashMap<>();
									map.put("userId", userId);
									map.put("infoKey", "sex");
									map.put("keyValue", sex+"");
									doPost(Config1.getInstance().UPDATEUSERINFO(), map, new ResultCallback() {
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
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								arg0.dismiss();
							}
						});
				dialog = builder.show();
				dialog.show();
			}
		});
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				App.userInfo.sex=sex;
				if (App.userInfo.sex==0) {
					sex_text.setText("男");
				} else if(App.userInfo.sex==1){
					sex_text.setText("女");
				}
				dialog.dismiss();
			}else if (msg.what==2){

			}else if (msg.what==3){
				JSONObject result=JSON.parseObject((String) msg.obj);
				UserInfo userInfo=JSON.parseObject(result.getString("data"),UserInfo.class);
				if (userInfo!=null) {
					App.userInfo=userInfo;
					setting();
				}
			}else if (msg.what==4){

			}else if (msg.what==5){
				getDatas();
			}else if (msg.what==6){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("userId",userId);
		doGet(Config1.getInstance().PERSONINFO(), map, new ResultCallback() {
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

	private void setting() {
		// TODO Auto-generated method stub
		sex = App.userInfo.sex;
		if (!TextUtils.isEmpty(App.userInfo.nickname)) {
			nikename_text.setText(App.userInfo.nickname);
		} else {
			nikename_text.setText(App.userInfo.realname);
		}
		if (!TextUtils.isEmpty(App.userInfo.image)) {
			Log.v("TAG", "oo=" + App.userInfo.image);
			String haha = App.userInfo.image;
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Config1.getInstance().IP+haha.replace("\\/","/"), head_pic);
		}
		if (sex==0) {
			sex_text.setText("男");
		} else if (sex==1){
			sex_text.setText("女");
		}
		if (App.userInfo.signature != null) {
			qianming_text.setText(App.userInfo.signature);
		} else {
			qianming_text.setText("");
		}
		if (App.userInfo.hometown != null) {
			dizhi_text.setText(App.userInfo.hometown);
		} else {
			dizhi_text.setText("");
		}
	}
	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_personinfo;
	}

	private void comfireImgSelection() {
		new Builder(this)
				.setTitle("选择图片")
				.setCancelable(false)
				.setItems(new CharSequence[] { "从相册选择", "拍照" },
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int which) {
								if (which == 0) {
									// 从相册中选择
									Intent i = new Intent(
											// 相册
											Intent.ACTION_PICK,
											MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
									startActivityForResult(i, RESULT_LOAD_IMAGE);
								} else if (which == 1) {
									// 拍照
									photo();
								}
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create().show();
	}

	private static final int TAKE_PICTURE = 0;
	private static final int RESULT_LOAD_IMAGE = 1;
	private static final int CUT_PHOTO_REQUEST_CODE = 2;
	private static final int SELECTIMG_SEARCH = 3;
	private String path = "";
	private Uri photoUri;

	public void photo() {
		try {
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);

			String sdcardState = Environment.getExternalStorageState();
			String sdcardPathDir = Environment
					.getExternalStorageDirectory().getPath() + "/tempImage/";
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

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 400:
			if (resultCode==431) {
				dizhi_text.setText(App.userInfo.hometown);
			}else if (resultCode==432) {
				nikename_text.setText(App.userInfo.nickname);
			}else if (resultCode==433) {
				qianming_text.setText(App.userInfo.signature);
			}
			break;
		case TAKE_PICTURE:
			startPhotoZoom(photoUri);
			break;
		case RESULT_LOAD_IMAGE:
			if (data != null) {
				Uri uri = data.getData();
				if (uri != null) {
					startPhotoZoom(uri);
				}
			}
			break;
		case CUT_PHOTO_REQUEST_CODE:
			if (resultCode == RESULT_OK && null != data) {// 裁剪返回
				final String url = data.getStringExtra("fileurl");
				new Thread() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						File file = new File(path);
						handler.sendEmptyMessage(2);
						UploadUtil u = new UploadUtil();
						String a = u.uploadHead(file,
								Config1.getInstance().FILEUPLOAD());
						handler.sendEmptyMessage(3);
						if (!TextUtils.isEmpty(a)) {
							Message message=Message.obtain();
							message.obj=a;
							message.what=1;
							handler.sendMessage(message);
						}
					}
				}.start();
			}
			break;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String a=(String) msg.obj;
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("userId", userId);
				map.put("infoKey", "image");
				map.put("keyValue", a.substring(
						a.indexOf("link") + 7,
						a.indexOf(",") - 1));
				doPost(Config1.getInstance().UPDATEUSERINFO(), map, new ResultCallback() {
					@Override
					public void onResponse(Call call, Response response, String json) {

						Message message=Message.obtain();
						message.what=5;
						message.obj=json;
						handlerReq.sendMessage(message);
					}

					@Override
					public void onFailure(Call call, IOException exception) {
						handlerReq.sendEmptyMessage(6);
					}
				});
			}else if (msg.what == 2) {
				showRoundProcessDialog(AdminInfoActivity.this);
			}else if (msg.what == 3) {
				showRoundProcessDialogCancel();
			}
		}
	};

	private void startPhotoZoom(Uri uri) {
		try {
			// 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyyMMddhhmmss");
			String address = sDateFormat.format(new java.util.Date());
			if (!FileUtils.isFileExist("")) {
				FileUtils.createSDDir("");
			}
			Uri imageUri = Uri.parse("file:///sdcard/formats/" + address
					+ ".JPEG");
			final Intent intent = new Intent("com.android.camera.action.CROP");

			// 照片URL地址
			intent.setDataAndType(uri, "image/*");

			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 480);
			intent.putExtra("outputY", 480);
			// 输出路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			// 输出格式
			intent.putExtra("outputFormat",
					Bitmap.CompressFormat.JPEG.toString());
			// 不启用人脸识别
			intent.putExtra("noFaceDetection", false);
			intent.putExtra("return-data", false);
			intent.putExtra("fileurl", FileUtils.SDPATH + address + ".JPEG");
			path = FileUtils.SDPATH + address + ".JPEG";
			Log.v("TAG", "url0=" + path);
			startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(401);
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
