package com.shengliedu.parent.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.app.AutoUpdate;
import com.shengliedu.parent.bean.ChildInfo;
import com.shengliedu.parent.bean.LoginInfo;
import com.shengliedu.parent.bean.UserInfo;
import com.shengliedu.parent.chat.xmpp.XmppConnection;
import com.shengliedu.parent.more.AboutUsActivity;
import com.shengliedu.parent.more.FeedBackActivity;
import com.shengliedu.parent.more.ResultsActivity;
import com.shengliedu.parent.more.admininfo.AdminInfoActivity;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.OnTabActivityResultListener3;
import com.shengliedu.parent.util.SharedPreferenceTool;
import com.shengliedu.parent.view.RoundImageView;

public class MoreActivity extends BaseActivity implements OnClickListener,OnTabActivityResultListener3 {
	private Button btn_esc;
	private RoundImageView roundImageView;
	private TextView name;
	private LinearLayout  relative3,relative4,
			relative5, relative6, relative7, relative8, relative9, relative10;
	private TextView versionname;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		relative3 = (LinearLayout) findViewById(R.id.relative3);
		relative3.setOnClickListener(this);
		relative4 = (LinearLayout) findViewById(R.id.relative4);
		relative4.setOnClickListener(this);
		relative5 = (LinearLayout)findViewById(R.id.relative5);
		relative5.setOnClickListener(this);
		relative6 = (LinearLayout) findViewById(R.id.relative6);
		relative6.setOnClickListener(this);
		relative7 = (LinearLayout) findViewById(R.id.relative7);
		relative7.setOnClickListener(this);
		relative8 = (LinearLayout) findViewById(R.id.relative8);
		relative8.setOnClickListener(this);
//		relative9 = (RelativeLayout) findViewById(R.id.relative9);
//		relative9.setOnClickListener(this);
		relative10 = (LinearLayout) findViewById(R.id.relative10);
		relative10.setOnClickListener(this);
		roundImageView = (RoundImageView) findViewById(R.id.touxiang);
		name = (TextView) findViewById(R.id.name);
		btn_esc = (Button)findViewById(R.id.btn_esc);
		btn_esc.setOnClickListener(this);
		versionname = (TextView) findViewById(R.id.versionname);
		try {
			versionname.setText(AutoUpdate.getVersionName(this));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(App.userInfo.image)) {
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Config1.getInstance().IP+App.userInfo.image.replace("\\/","/"),roundImageView);
		}
		if (!TextUtils.isEmpty(App.userInfo.nickname)) {
			name.setText(App.userInfo.nickname);
		}else if (!TextUtils.isEmpty(App.userInfo.realname)) {
			name.setText(App.userInfo.realname);
		}
		if (MainActivity.page==3) {
			Intent intent = new Intent(this,ResultsActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_more;
	}
	private AlertDialog dialog;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub]
		Intent intent = null;
		switch (v.getId()) {
		case R.id.relative3:
			final String[] bells = new String[] {"滴答声提示", "人声提示","静音" };
			Builder builder = new Builder(MoreActivity.this);
			builder.setTitle("更改声音");
			// 给对话框设定单选列表项
			// 参数一： 选项的值 参数二： 默认选中项的索引 参数三：监测选中的项的监听器
			builder.setSingleChoiceItems(bells, SharedPreferenceTool.getAppBell(this),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							SharedPreferenceTool.setAppBell(MoreActivity.this, arg1);
							arg0.dismiss();
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
			break;
		case R.id.relative4:
			toast("已清除");
			break;
		case R.id.relative5:
			AutoUpdate autoUpdate=new AutoUpdate();
			autoUpdate.getVersionData(this, "1");
			break;
		case R.id.relative6:
			intent = new Intent(this,AboutUsActivity.class);
			startActivity(intent);
			break;
		case R.id.relative7:
			intent = new Intent(this,SelectChildActivity.class);
			intent.putExtra("time", 1);
			startActivity(intent);
			break;
		case R.id.relative8:
			intent = new Intent(this,AdminInfoActivity.class);
			getParent().startActivityForResult(intent, 400);
			break;
//		case R.id.relative9:
//			intent = new Intent(this,NoteActivity.class);
//			startActivity(intent);
//			break;
		case R.id.relative10:
			intent = new Intent(this,FeedBackActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_esc:
//			imChat.logout();
			XmppConnection.getInstance().closeConnection();
			intent = new Intent(this,LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			App.childInfo=new ChildInfo();
			App.loginInfo=new LoginInfo();
			App.userInfo =new UserInfo();
			startActivity(intent);
			getParent().finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode ==400 && resultCode == 401) {
			getDatas();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
