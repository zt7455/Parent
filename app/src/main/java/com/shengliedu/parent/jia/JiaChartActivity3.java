package com.shengliedu.parent.jia;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;

public class JiaChartActivity3 extends BaseActivity {
	private Button b1,b2,b3;
	private ImageView jia_img;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("各科成绩");
		b1=getView(R.id.b1);
		b2=getView(R.id.b2);
		b3=getView(R.id.b3);
		jia_img=getView(R.id.jia_img);
		jia_img.setImageResource(R.mipmap.jia_chart8);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				jia_img.setImageResource(R.mipmap.jia_chart8);
			}
		});
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				jia_img.setImageResource(R.mipmap.jia_chart9);
			}
		});
		b3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				jia_img.setImageResource(R.mipmap.jia_chart10);
			}
		});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_chart_jia3;
	}

}
