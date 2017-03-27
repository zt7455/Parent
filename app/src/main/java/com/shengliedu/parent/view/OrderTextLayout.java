package com.shengliedu.parent.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shengliedu.parent.R;

public class OrderTextLayout extends LinearLayout {

	private Context context;
	private LayoutInflater inflater;
	public TextView labelText,contentText;

	public OrderTextLayout(Context context) {
		this(context, null);
	}

	public OrderTextLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		inflater = LayoutInflater.from(context);
		setUpViews();
	}

	private void setUpViews() {
		inflater.inflate(R.layout.item_order_text_layout, this);
		labelText = (TextView) findViewById(R.id.text_label);
		contentText = (TextView) findViewById(R.id.text_content);
	}

	public TextView getLabelText() {
		return labelText;
	}
	public TextView getRightText() {
		return contentText;
	}

}
