package com.shengliedu.parent.jia;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;

public class JiaChartActivity extends BaseActivity {
	private ImageView imageView0, imageView;
	private LinearLayout lin;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("三图一表");
		lin = getView(R.id.lin);
		imageView0 = new ImageView(this);
		imageView = new ImageView(this);
		WindowManager wm = (WindowManager)getSystemService(
				Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		LayoutParams params = new LayoutParams(width, 735*width/720);
		imageView0.setLayoutParams(params);
		imageView0.setImageResource(R.mipmap.jia_chart1);
		lin.addView(imageView0);
		LayoutParams params1 = new LayoutParams(width, 399*width/720);
		imageView.setLayoutParams(params1);
		imageView.setImageResource(R.mipmap.jia_chart2);
		lin.addView(imageView);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(JiaChartActivity.this,
						JiaChartActivity1.class));
			}
		});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_chart_jia;
	}

}
