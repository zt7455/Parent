package com.shengliedu.parent.more;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

public class FeedBackActivity extends BaseActivity {
	private EditText feedbackEditText;
	private Button opinion_feedback_ok;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("意见反馈");
		setRightText("我的建议", new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(FeedBackActivity.this,MyFeedBackActivity.class);
				startActivity(intent);
			}
		});
		feedbackEditText = (EditText) findViewById(R.id.opinion_feedback_edit);
		opinion_feedback_ok = (Button) findViewById(R.id.opinion_feedback_ok);
		opinion_feedback_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String contentString = feedbackEditText.getText().toString();
				if (TextUtils.isEmpty(contentString)) {
					toast("请输入内容");
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", App.userInfo.id+"");
					map.put("suggestion", contentString);
					doPost(Config1.getInstance().FEEDSUBMIT(), map,new ResultCallback() {
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
			}
		});
	}
	Handler handlerReq=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				toast("您的建议我们已经收到，我们会尽快处理");
				feedbackEditText.setText("");
			} else if (msg.what == 2) {
			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_feedback;
	}

}
