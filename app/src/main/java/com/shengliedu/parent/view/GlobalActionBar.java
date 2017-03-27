package com.shengliedu.parent.view;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengliedu.parent.R;

public class GlobalActionBar {
	TextView title, right;
	ImageView back, rightIv;
	Activity activity;

	public GlobalActionBar(Activity activity) {
		this.activity = activity;
		initView();
	}

	private void initView() {
		title = (TextView) activity.findViewById(R.id.bar_title);
		right = (TextView) activity.findViewById(R.id.bar_right);
		back = (ImageView) activity.findViewById(R.id.barArrow);
		rightIv = (ImageView) activity.findViewById(R.id.bar_right_iv);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
	}

	public TextView getTitle() {
		return title;
	}

	public TextView getRight() {
		return right;
	}

	public ImageView getBarArrow() {
		return back;
	}

	public ImageView getRightIv() {
		return rightIv;
	}

}
