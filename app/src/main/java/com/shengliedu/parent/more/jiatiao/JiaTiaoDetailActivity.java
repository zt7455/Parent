package com.shengliedu.parent.more.jiatiao;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.JiaTiaoDetail;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class JiaTiaoDetailActivity extends BaseActivity {
	private TextView jiatiao_content, jiatiao_tt, jiatiao_sign, jiatiao_time;
	private String id;
	private String content;
	private List<JiaTiaoDetail> ac;
	Handler handlerReq=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				JSONObject result=JSON.parseObject((String) msg.obj);
				SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
				ac = JSON.parseArray(result.getString("data"), JiaTiaoDetail.class);
				JiaTiaoDetail detail = ac.get(0);
				String qingjiatype = "";
				String qingjiatime = "";
				String childname = detail.child.realname;
				String parentname = detail.parent.realname;
				if (detail.type == 1) {
					qingjiatype = "事假";
				} else {
					qingjiatype = "病假";
				}
				if (detail.part == 0) {
					qingjiatime = "全天";
				} else if (detail.part == 1) {
					qingjiatime = "上午";
				} else if (detail.part == 2) {
					qingjiatime = "下午";
				} else if (detail.part == 3) {
					for (int i = 0; i < detail.scopeList.size(); i++) {
						qingjiatime = qingjiatime
								+ detail.scopeList.get(i).getName() + ",";
					}
				}
				if (qingjiatime.contains(",")) {
					qingjiatime.replace(
							qingjiatime.charAt(qingjiatime.lastIndexOf(',')),
							' ');
				}
				jiatiao_tt.setText("尊敬的" + detail.teacher.realname
						+ "老师:");
				content = "    我是本班 " + childname + " 同学的家长 " + parentname
						+ " ," + childname + " 同学因 " + detail.content
						+ " 请" + qingjiatype + ",请假时间从 "
						+ format.format(new Date(detail.stime * 1000))
						+ " 到 "
						+ format.format(new Date(detail.etime * 1000))
						+ ", 请假时段为 " + qingjiatime + "。";
				jiatiao_content.setText(content);
				jiatiao_sign.setText("家长:" + parentname);
				jiatiao_time.setText("时间:"
						+ format.format(new Date(detail.addTime * 1000)));
			} else if (msg.what == 2) {
			}
		}
	};
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		id = getIntent().getStringExtra("id");
		setBack();
		showTitle("请假条详情");
		jiatiao_content = (TextView) findViewById(R.id.jiatiao_content);
		jiatiao_tt = (TextView) findViewById(R.id.jiatiao_tt);
		jiatiao_sign = (TextView) findViewById(R.id.jiatiao_sign);
		jiatiao_time = (TextView) findViewById(R.id.jiatiao_time);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		doGet(Config1.getInstance().NOTEDETAIL(), map, new ResultCallback() {
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

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_jiatiaodetail;
	}

}
