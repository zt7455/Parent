package com.shengliedu.parent.more;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.activity.MainActivity;
import com.shengliedu.parent.jia.JiaChartActivity;
import com.shengliedu.parent.jia.JiaChartActivity2;
import com.shengliedu.parent.jia.JiaChartActivity3;
import com.shengliedu.parent.jia.JiaChartActivity4;
import com.shengliedu.parent.more.score.ClassExpressWeekActivity;
import com.shengliedu.parent.more.score.DisciplineActivity;
import com.shengliedu.parent.more.score.DutyActivity;
import com.shengliedu.parent.more.score.HomeworkExpressWeekActivity;

public class ResultsActivity extends BaseActivity implements OnClickListener {
	private ImageView menu1, menu2, menu3, menu4, menu5, menu6, menu7, menu8,
	menu9;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("成绩表现");
		if (MainActivity.page==3) {
			MainActivity.page=0;
		}
		menu1 = (ImageView) findViewById(R.id.menu_r1);
		menu2 = (ImageView) findViewById(R.id.menu_r2);
		menu3 = (ImageView) findViewById(R.id.menu_r3);
		menu4 = (ImageView) findViewById(R.id.menu_r4);
		menu5 = (ImageView) findViewById(R.id.menu_r5);
		menu6 = (ImageView) findViewById(R.id.menu_r6);
		menu7 = (ImageView) findViewById(R.id.menu_r7);
		menu8 = (ImageView) findViewById(R.id.menu_r8);
		menu9 = (ImageView) findViewById(R.id.menu_r9);
		menu9.setVisibility(View.GONE);
		menu1.setOnClickListener(this);
		menu2.setOnClickListener(this);
		menu3.setOnClickListener(this);
		menu4.setOnClickListener(this);
		menu5.setOnClickListener(this);
		menu6.setOnClickListener(this);
		menu7.setOnClickListener(this);
		menu8.setOnClickListener(this);
		menu9.setOnClickListener(this);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_results;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.menu_r1:
			//坚总的假数据
			intent = new Intent(this, JiaChartActivity.class);
			startActivity(intent);
//			intent = new Intent(this, ChartActivity.class);
//			startActivity(intent);
			break;
		case R.id.menu_r2:
			//坚总的假数据
			intent = new Intent(this,JiaChartActivity3.class);
			startActivity(intent);
//			intent = new Intent(this, EveryScoreActivity.class);
//			startActivity(intent);
			break;

		case R.id.menu_r3:
			intent = new Intent(this, ClassExpressWeekActivity.class);
			startActivity(intent);
			break;

		case R.id.menu_r4:
			intent = new Intent(this, DisciplineActivity.class);
			startActivity(intent);
			break;

		case R.id.menu_r5:
			intent = new Intent(this, HomeworkExpressWeekActivity.class);
			startActivity(intent);
			break;

		case R.id.menu_r6:
			//坚总的假数据
			intent = new Intent(this,JiaChartActivity4.class);
			startActivity(intent);
//			intent = new Intent(this,ExceptionReportActivity.class);
//			startActivity(intent);
			break;

		case R.id.menu_r7:
			intent = new Intent(this, DutyActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_r8:
			//坚总的假数据
			intent = new Intent(this, JiaChartActivity2.class);
			startActivity(intent);
//			intent = new Intent(this, KnowledgeShortActivity.class);
//			startActivity(intent);
			break;
		case R.id.menu_r9:
			Toast.makeText(this, "监控电子书包", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

}
