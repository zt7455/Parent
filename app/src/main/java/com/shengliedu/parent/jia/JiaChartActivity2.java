package com.shengliedu.parent.jia;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;

public class JiaChartActivity2 extends BaseActivity {
	private Button b1,b2,b3;
	private ImageView jia_img;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("知识短板");
		b1=getView(R.id.b1);
		b2=getView(R.id.b2);
		b3=getView(R.id.b3);
		jia_img=getView(R.id.jia_img);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				jia_img.setImageResource(R.mipmap.jia_chart5);
			}
		});
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				jia_img.setImageResource(R.mipmap.jia_chart6);
			}
		});
		b3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				jia_img.setImageResource(R.mipmap.jia_chart7);
			}
		});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_chart_jia3;
	}

}
