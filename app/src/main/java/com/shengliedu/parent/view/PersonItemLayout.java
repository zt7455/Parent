package com.shengliedu.parent.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shengliedu.parent.R;

public class PersonItemLayout extends RelativeLayout {
	private ImageView iv;
	private TextView tv;
	private String text;
	private int src;

	public PersonItemLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.item_person, this);
		iv = (ImageView) findViewById(R.id.imageitem);
		tv = (TextView) findViewById(R.id.infoitem);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PersonItemLayout);
		text = a.getString(R.styleable.PersonItemLayout_text);
		tv.setText(text);	
		src = a.getResourceId(R.styleable.PersonItemLayout_src, 1);
		iv.setImageResource(src);
		a.recycle();

	}

	public void setImageResource(int resId) {
		iv.setImageResource(resId);
	}

	public void setTextViewText(String text) {
		tv.setText(text);
	}

}
