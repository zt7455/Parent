package com.shengliedu.parent.more.admininfo;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class ChangeSignActivity extends BaseActivity {
	private EditText nikename_edit;
	private String name;
	Handler handlerReq=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				App.userInfo.signature=name;
				setResult(433);
				finish();
			} else if (msg.what == 2) {
			}
		}
	};
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("更改个人签名");
		setRightText("保存", new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				name = nikename_edit.getText().toString().trim();
				if (TextUtils.isEmpty(name)) {
					toast("输入内容为空");
					return;
				}
				if (name.equals(App.userInfo.signature)) {
					finish();
				}
				Map<String,Object> map=new HashMap<>();
				map.put("userId", App.userInfo.id+"");
				map.put("infoKey", "signature");
				map.put("keyValue", name);
				doPost(Config1.getInstance().UPDATEUSERINFO(), map, new ResultCallback() {
					@Override
					public void onResponse(Call call, Response response, String json) {
						Message message=Message.obtain();
						message.what=1;
						message.obj=json;
						handlerReq.sendMessage(message);
					}

					@Override
					public void onFailure(Call call, IOException exception) {
						handlerReq.sendEmptyMessage(2);
					}
				});
			}
		});
		nikename_edit = (EditText) findViewById(R.id.nikename_edit);
		nikename_edit.setText(App.userInfo.signature);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_changenikename;
	}
}
