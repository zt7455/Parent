package com.shengliedu.parent.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shengliedu.parent.R;

public class LoginTextLayout extends LinearLayout {

	private Context context;
	private LayoutInflater inflater;
	public TextView labelText;
	public EditText rightEt;

	public LoginTextLayout(Context context) {
		this(context, null);
	}

	public LoginTextLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		inflater = LayoutInflater.from(context);
		setUpViews();
	}

	private void setUpViews() {
		inflater.inflate(R.layout.item_text_layout, this);
		labelText = (TextView) findViewById(R.id.text_label);
		rightEt = (EditText) findViewById(R.id.edit_right);
	}

	public TextView getText() {
		return labelText;
	}

	public EditText getEditText() {
		return rightEt;
	}

}
