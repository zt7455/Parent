package com.shengliedu.parent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.ViewPagerAdapter;
import com.shengliedu.parent.app.AutoUpdate;
import com.shengliedu.parent.chat.constant.Constants;
import com.shengliedu.parent.chat.util.Tool;
import com.shengliedu.parent.chat.util.XmppLoadThread;
import com.shengliedu.parent.chat.xmpp.XmppConnection;
import com.shengliedu.parent.util.OnTabActivityResultListener;
import com.shengliedu.parent.util.OnTabActivityResultListener1;
import com.shengliedu.parent.util.OnTabActivityResultListener2;
import com.shengliedu.parent.util.OnTabActivityResultListener3;
import com.shengliedu.parent.util.SharedPreferenceTool;
import com.shengliedu.parent.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {
	private NoScrollViewPager viewPager;
	private ViewPagerAdapter adapter;
	private List<View> views = new ArrayList<View>();
	private RadioGroup radioGroup;
	public static int page;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		page=getIntent().getIntExtra("page", 0);
//		imChat = IMChat.getInstace(this);
		View view1 = manager.startActivity("tab1",
				new Intent(MainActivity.this, ConsoleActivity.class))
				.getDecorView();
//		View view2 = manager
//				.startActivity(
//						"tab2",
//						new Intent(MainActivity.this,
//								HomeworkAfterClassActivity.class))
//				.getDecorView();
		View view3 = manager.startActivity("tab3",
				new Intent(MainActivity.this, ChatMsgActivity.class))
				.getDecorView();
		View view4 = manager.startActivity("tab4",
				new Intent(MainActivity.this, MoreActivity.class))
				.getDecorView();
		views.add(view1);
//		views.add(view2);
		views.add(view3);
		views.add(view4);
		viewPager = (NoScrollViewPager) findViewById(R.id.viewPager);
		adapter = new ViewPagerAdapter(MainActivity.this, views);
		viewPager.setAdapter(adapter);
		viewPager.setNoScroll(true);
		viewPager.setCurrentItem(page, false);
		radioGroup = (RadioGroup) findViewById(R.id.main_group);
		switch (page) {
		case 0:
			((RadioButton) findViewById(R.id.main_btn1)).setChecked(true);
			break;
		case 1:
			((RadioButton) findViewById(R.id.main_btn3)).setChecked(true);
			if (!TextUtils.isEmpty(SharedPreferenceTool
					.getUserName(MainActivity.this))) {
				loginAccount(SharedPreferenceTool
						.getUserName(MainActivity.this), SharedPreferenceTool
						.getPassword(MainActivity.this));
			}
			break;
		case 2:
			((RadioButton) findViewById(R.id.main_btn4)).setChecked(true);
			break;

		default:
			break;
		}
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.main_btn1:
					viewPager.setCurrentItem(0, false);
					break;
				case R.id.main_btn3:
					viewPager.setCurrentItem(1, false);
					break;
				case R.id.main_btn4:
					viewPager.setCurrentItem(2, false);
					break;
				default:
					break;
				}
			}
		});
	}

	Handler handler = new Handler();
	private static final String TAG = "MainActivity";

	
	private void loginAccount(final String userName, final String password) {
		new XmppLoadThread(this) {

			@Override
			protected Object load() {
				boolean isSuccess = XmppConnection.getInstance().login(
						userName, password);
				if (isSuccess) {
					Constants.USER_NAME = userName;
					Constants.PWD = password;
				}
				return isSuccess;
			}

			@Override
			protected void result(Object o) {
				boolean isSuccess = (Boolean) o;
				if (isSuccess) {
					
				} else {
					Tool.initToast(MainActivity.this, "网络或聊天服务器异常");
				}
			}

		};
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		// 判断是否实现返回值接口
		if (arg0 == 100) {
			Activity subActivity = manager.getActivity("tab1");
			Log.v("TAG2", "r=" + subActivity.getLocalClassName()
					+ (subActivity instanceof OnTabActivityResultListener));
			// 获取返回值接口实例
			OnTabActivityResultListener listener = (OnTabActivityResultListener) subActivity;
			// 转发请求到子Activity
			listener.onTabActivityResult(arg0, arg1, arg2);
		} else if (arg0 == 200) {
			Activity subActivity = manager.getActivity("tab2");
			Log.v("TAG2", "r=" + subActivity.getLocalClassName()
					+ (subActivity instanceof OnTabActivityResultListener));
			// 获取返回值接口实例
			OnTabActivityResultListener1 listener = (OnTabActivityResultListener1) subActivity;
			// 转发请求到子Activity
			listener.onTabActivityResult(arg0, arg1, arg2);
		} else if (arg0 == 300) {
			Activity subActivity = manager.getActivity("tab3");
			Log.v("TAG2", "r=" + subActivity.getLocalClassName()
					+ (subActivity instanceof OnTabActivityResultListener));
			// 获取返回值接口实例
			OnTabActivityResultListener2 listener = (OnTabActivityResultListener2) subActivity;
			// 转发请求到子Activity
			listener.onTabActivityResult(arg0, arg1, arg2);
		}else if (arg0 == 400) {
			Activity subActivity = manager.getActivity("tab4");
			Log.v("TAG2", "r=" + subActivity.getLocalClassName()
					+ (subActivity instanceof OnTabActivityResultListener3));
			// 获取返回值接口实例
			OnTabActivityResultListener3 listener = (OnTabActivityResultListener3) subActivity;
			// 转发请求到子Activity
			listener.onTabActivityResult(arg0, arg1, arg2);
		}
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		AutoUpdate autoUpdate=new AutoUpdate();
		autoUpdate.getVersionData(this, "0");
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_main;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click();
			Log.v("TAG", "5");
			return false;
		}
		Log.v("TAG", "6");
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 */
	private Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true;
			Log.v("TAG", "1");
			Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
			Log.v("TAG", "11");
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
					Log.v("TAG", "2");
				}
			}, 2000);
			Log.v("TAG", "3");
		} else {
			Log.v("TAG", "4");
			XmppConnection.getInstance().closeConnection();
			finish();
//			System.exit(0);
		}
	}
}
