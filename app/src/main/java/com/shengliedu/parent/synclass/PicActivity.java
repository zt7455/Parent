package com.shengliedu.parent.synclass;

import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;

public class PicActivity extends BaseActivity {
	private String plan_info;
	private String content_host;
	private String link;
	private ImageView imageView;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		content_host = (String) getIntent().getExtras().get("content_host");
		link = (String) getIntent().getExtras().get("html");
		plan_info = (String) getIntent().getExtras().get("plan_info");
		setBack();
		showTitle(plan_info);
		imageView=getView(R.id.syn_png);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			hideTitle();
		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			showTitle(plan_info);
		}
	}
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Log.v("TAG", "link="+link);
		if (!TextUtils.isEmpty(link)) {
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(content_host+link, imageView);
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_synpic;
	}

}
