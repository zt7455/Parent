package com.shengliedu.parent.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.ConsoleMenuAdapter;
import com.shengliedu.parent.bean.ConsoleMenu;
import com.shengliedu.parent.book.TeachResourceActivity;
import com.shengliedu.parent.chat.activitys.SchoolNotifyActivity;
import com.shengliedu.parent.jia.JiaChartActivity;
import com.shengliedu.parent.jia.JiaChartActivity2;
import com.shengliedu.parent.jia.JiaChartActivity3;
import com.shengliedu.parent.jia.JiaChartActivity4;
import com.shengliedu.parent.more.jiatiao.NoteActivity;
import com.shengliedu.parent.more.score.ClassExpressWeekActivity;
import com.shengliedu.parent.more.score.DisciplineActivity;
import com.shengliedu.parent.more.score.DutyActivity;
import com.shengliedu.parent.more.score.HomeworkExpressWeekActivity;
import com.shengliedu.parent.util.ConsoleMenus;
import com.shengliedu.parent.util.OnTabActivityResultListener;

import java.util.ArrayList;
import java.util.List;

//import com.shengliedu.parent.new_homework.HomeworkListActivity;

public class ConsoleActivity extends BaseActivity implements
		OnItemClickListener, OnTabActivityResultListener {
	private GridView grid_select;
	private ConsoleMenuAdapter adapter;
	private List<ConsoleMenu> list = new ArrayList<ConsoleMenu>();

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		grid_select = getView(R.id.grid_select);
		grid_select.setFocusable(false);
		adapter = new ConsoleMenuAdapter(this, list);
		grid_select.setAdapter(adapter);
		grid_select.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = null;
		int function = list.get(arg2).number;
		switch (function) {
		case 100:// 作业
//			intent = new Intent(this, HomeworkAfterClassActivity.class);
//			startActivity(intent);
			intent = new Intent(this, HomeworkAfterClass2Activity.class);
			startActivity(intent);
			break;
		case 200:// 同步课堂
//			intent = new Intent(this, SysClassActivity.class);
//			startActivity(intent);
			intent = new Intent(this, SysClass2Activity.class);
			startActivity(intent);
			break;
		case 300:// 请假条
			intent = new Intent(this, NoteActivity.class);
			startActivity(intent);
			break;
		case 400:// 教学资源
			intent = new Intent(ConsoleActivity.this,
					TeachResourceActivity.class);
			getParent().startActivityForResult(intent, 0);
			break;
		case 500:// 通知
			intent = new Intent(this, SchoolNotifyActivity.class);
			startActivity(intent);
			break;
		case 600:// 三图一表
			// 坚总的假数据
			intent = new Intent(this, JiaChartActivity.class);
			startActivity(intent);
			// intent = new Intent(this, ChartActivity.class);
			// startActivity(intent);
			break;
		case 700:// 各科成绩
			// 坚总的假数据
			intent = new Intent(this, JiaChartActivity3.class);
			startActivity(intent);
			// intent = new Intent(this, EveryScoreActivity.class);
			// startActivity(intent);
			break;
		case 800:// 课堂回答
			intent = new Intent(this, ClassExpressWeekActivity.class);
			startActivity(intent);
			break;
		case 900:// 在校纪律
			intent = new Intent(this, DisciplineActivity.class);
			startActivity(intent);
			break;
		case 1000:// 作业表现
			intent = new Intent(this, HomeworkExpressWeekActivity.class);
			startActivity(intent);
			break;
		case 1100:// 异动报告
			// 坚总的假数据
			intent = new Intent(this, JiaChartActivity4.class);
			startActivity(intent);
			// intent = new Intent(this,ExceptionReportActivity.class);
			// startActivity(intent);
			break;
		case 1200:// 出勤情况
			intent = new Intent(this, DutyActivity.class);
			startActivity(intent);
			break;
		case 1300:// 知识短板
			// 坚总的假数据
			intent = new Intent(this, JiaChartActivity2.class);
			startActivity(intent);
			// intent = new Intent(this, KnowledgeShortActivity.class);
			// startActivity(intent);
			break;
		case 1400:// 错题集

			break;
		default:
			break;
		}
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		ConsoleMenus.removeListDuplicateObject(list, ConsoleMenus.init());
		adapter.notifyDataSetChanged();
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_console;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
		}
	}

}
