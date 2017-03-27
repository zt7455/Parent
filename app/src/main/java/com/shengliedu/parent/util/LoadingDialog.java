package com.shengliedu.parent.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.shengliedu.parent.R;


/**
 * @author shangtai.yin
 * 加载中
 */
public class LoadingDialog extends ProgressDialog {
	private TextView tv_msg;
	static String mes;

	public LoadingDialog(Context context, String mes) {
		super(context);
		this.mes = mes;
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loding_dialog);
		tv_msg = (TextView) this.findViewById(R.id.tv_msg);
		tv_msg.setText(mes);
	}

	public static LoadingDialog show(final Context ctx) {
		final LoadingDialog dialog = new LoadingDialog(ctx, mes);
		dialog.show();
		return dialog;
	}

}