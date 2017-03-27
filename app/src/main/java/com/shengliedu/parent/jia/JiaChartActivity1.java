package com.shengliedu.parent.jia;

import android.content.Context;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;

public class JiaChartActivity1 extends BaseActivity {
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
		LayoutParams params = new LayoutParams(width, 772*width/720);
		imageView0.setLayoutParams(params);
		imageView0.setImageResource(R.mipmap.jia_chart3);
		lin.addView(imageView0);
		LayoutParams params1 = new LayoutParams(width, 615*width/720);
		imageView.setLayoutParams(params1);
		imageView.setImageResource(R.mipmap.jia_chart4);
		lin.addView(imageView);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_chart_jia2;
	}

}
