package com.shengliedu.parent.homework.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.LianXianLeftAdapter;
import com.shengliedu.parent.bean.HomeWorkDetailQ;
import com.shengliedu.parent.bean.StudentAnswer;
import com.shengliedu.parent.homework.HomeWorkDetailActivity;
import com.shengliedu.parent.synclass.SynSeletQuestionAnalyticalActivity;
import com.shengliedu.parent.synclass.interf.NextOrLast;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.HtmlImage;
import com.shengliedu.parent.util.NumberToCode;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.view.NoScrollListview;
import com.shengliedu.parent.widght.CustomDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class ShuangLianXianFragment extends Fragment {
	private View view;

	private TextView select_title, pandun, huida, select_tva, select_tvb,
			select_centerjx, lianxian_text;
	private Button select_btn, lianxian_btn;
	private ImageView select_la,select_rb;
	private NoScrollListview lvLeft;
	private NoScrollListview lvRight;

	private String ques = "";
	private String rques = "";
	private String rqueStr = "";
	private HomeWorkDetailQ homeWorkDetailQ;
	private String coursewareContentId;
	private String hourId;
	private String content_id;
	private String studentId;
	private String date;
	private String questionContentType;
	private NextOrLast nextOrLast;

	private Map<Integer, Boolean> map1 = new HashMap<Integer, Boolean>();
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private Map<Integer, String> mapAnwser = new HashMap<Integer, String>();
	private LinearLayout fshuanglianxLin;
	@SuppressLint("ValidFragment")
	public ShuangLianXianFragment(HomeWorkDetailQ homeWorkDetailQ,
								  String coursewareContentId, String studentId, String hourId,
								  String content_id, String date, String questionContentType) {
		// TODO Auto-generated constructor stub
		this.homeWorkDetailQ = homeWorkDetailQ;
		this.coursewareContentId = coursewareContentId;
		this.studentId = studentId;
		this.hourId = hourId;
		this.content_id = content_id;
		this.date = date;
		this.questionContentType = questionContentType;
	}
	public ShuangLianXianFragment(){
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_shuanglianxian, container,
				false);
		nextOrLast = (NextOrLast) getActivity();
		initViews();
		doSomething();
		return view;
	}

	private void doSomething() {
		// TODO Auto-generated method stub
		if (homeWorkDetailQ.studentAnswer != null
				& homeWorkDetailQ.studentAnswer.size() != 0) {
			ques = homeWorkDetailQ.studentAnswer.get(0).title;
			String text = "";
			for (int i = 0; i < homeWorkDetailQ.studentAnswer.size(); i++) {
				mapAnwser.put(
						NumberToCode.codeToNumber(homeWorkDetailQ.studentAnswer
								.get(i).title.substring(0, 1)),
						homeWorkDetailQ.studentAnswer.get(i).title);
				text = text + homeWorkDetailQ.studentAnswer.get(i).title + " ";
			}
			lianxian_text.setText(text);
		}
		if (homeWorkDetailQ.correct != null
				& homeWorkDetailQ.correct.size() != 0) {
			rques = homeWorkDetailQ.correct.get(0).title;
			for (int i = 0; i < homeWorkDetailQ.correct.size(); i++) {
				rqueStr = rqueStr + homeWorkDetailQ.correct.get(i).title + " ";
			}
		}
		String ques1 = "";
		String ques2 = "";
		if (ques != null && !"".equals(ques)) {
			ques1 = ques.substring(0, 1);
			ques2 = ques.substring(1, 2);
		}
		if ("A".equals(ques1)) {
			map1.clear();
			map1.put(0, true);
		} else if ("B".equals(ques1)) {
			map1.clear();
			map1.put(1, true);
		} else if ("C".equals(ques1)) {
			map1.clear();
			map1.put(2, true);
		} else if ("D".equals(ques1)) {
			map1.clear();
			map1.put(3, true);
		} else if ("E".equals(ques1)) {
			map1.clear();
			map1.put(4, true);
		} else if ("F".equals(ques1)) {
			map1.clear();
			map1.put(5, true);
		} else if ("G".equals(ques1)) {
			map1.clear();
			map1.put(6, true);
		} else if ("H".equals(ques1)) {
			map1.clear();
			map1.put(7, true);
		} else if ("I".equals(ques1)) {
			map1.clear();
			map1.put(8, true);
		} else if ("J".equals(ques1)) {
			map1.clear();
			map1.put(9, true);
		}
		if ("A".equals(ques2)) {
			map.clear();
			map.put(0, true);
		} else if ("B".equals(ques2)) {
			map.clear();
			map.put(1, true);
		} else if ("C".equals(ques2)) {
			map.clear();
			map.put(2, true);
		} else if ("D".equals(ques2)) {
			map.clear();
			map.put(3, true);
		} else if ("E".equals(ques2)) {
			map.clear();
			map.put(4, true);
		} else if ("F".equals(ques2)) {
			map.clear();
			map.put(5, true);
		} else if ("G".equals(ques2)) {
			map.clear();
			map.put(6, true);
		} else if ("H".equals(ques2)) {
			map.clear();
			map.put(7, true);
		} else if ("I".equals(ques2)) {
			map.clear();
			map.put(8, true);
		} else if ("J".equals(ques2)) {
			map.clear();
			map.put(9, true);
		}
		final LianXianLeftAdapter adapter = new LianXianLeftAdapter(
				getActivity(), homeWorkDetailQ.answer.answersF.get(0).items,
				map1);
		lvLeft.setAdapter(adapter);
		final LianXianLeftAdapter adapterRight = new LianXianLeftAdapter(
				getActivity(), homeWorkDetailQ.answer.answersF.get(1).items,
				map);
		lvRight.setAdapter(adapterRight);
		int i = 0;
		String text = "";
		if (homeWorkDetailQ.studentAnswer != null
				&& homeWorkDetailQ.studentAnswer.size() > 0) {
			for (int j = 0; j < homeWorkDetailQ.studentAnswer.size(); j++) {
				if (rqueStr
						.contains(homeWorkDetailQ.studentAnswer.get(i).title)) {
					i++;
				}
				text = text + homeWorkDetailQ.studentAnswer.get(j).title + " ";
			}
			if (homeWorkDetailQ.correct.size() == i) {
				huida.setText("您的孩子的答案是" + text + ",正确答案是" + rqueStr + ",回答正确");
			} else {
				huida.setText("您的孩子的答案是" + text + ",正确答案是" + rqueStr + ",回答错误");
			}
		}

//		select_title.setText(Html.fromHtml(homeWorkDetailQ.question.question));
		select_title.setText(Html.fromHtml(HtmlImage.deleteSrc(homeWorkDetailQ.question
				.question)));
		String name = homeWorkDetailQ.question
				.question;
		List<String> imgList = HtmlImage.getImgSrc(name);
		if (imgList.size() > 0) {
			for (int j = 0; j < imgList.size(); j++) {
				final String url = imgList.get(j);
				ImageView imageView = new ImageView(
						getActivity());
				imageView.setScaleType(ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				ImageLoader imageLoader=ImageLoader.getInstance();
				imageLoader.displayImage(url, imageView);
				imageView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated
								// method stub
								HtmlImage htmlImage=new HtmlImage();
								htmlImage.showDialog(getActivity(),url);
							}
						});
				fshuanglianxLin.addView(imageView);
			}
		}
		pandun.setText(homeWorkDetailQ.typeName);

		lvLeft.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {
					ques = "A";
				} else if (arg2 == 1) {
					ques = "B";
				} else if (arg2 == 2) {
					ques = "C";
				} else if (arg2 == 3) {
					ques = "D";
				} else if (arg2 == 4) {
					ques = "E";
				} else if (arg2 == 5) {
					ques = "F";
				} else if (arg2 == 6) {
					ques = "G";
				} else if (arg2 == 7) {
					ques = "H";
				} else if (arg2 == 8) {
					ques = "I";
				} else if (arg2 == 9) {
					ques = "J";
				}
				map1.clear();
				map1.put(arg2, true);
				adapter.notifyDataSetChanged();
			}
		});
		lvRight.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {
					ques = "A";
				} else if (arg2 == 1) {
					ques = "B";
				} else if (arg2 == 2) {
					ques = "C";
				} else if (arg2 == 3) {
					ques = "D";
				} else if (arg2 == 4) {
					ques = "E";
				} else if (arg2 == 5) {
					ques = "F";
				} else if (arg2 == 6) {
					ques = "G";
				} else if (arg2 == 7) {
					ques = "H";
				} else if (arg2 == 8) {
					ques = "I";
				} else if (arg2 == 9) {
					ques = "J";
				}
				map.clear();
				map.put(arg2, true);
				adapterRight.notifyDataSetChanged();
			}
		});
	}

	private void initViews() {
		// TODO Auto-generated method stub
		fshuanglianxLin = (LinearLayout) view.findViewById(R.id.fshuanglianxLin);
		select_title = (TextView) view.findViewById(R.id.select_title);
		pandun = (TextView) view.findViewById(R.id.pandun);
		select_btn = (Button) view.findViewById(R.id.select_btn);
		lianxian_btn = (Button) view.findViewById(R.id.lianxian_btn);
		huida = (TextView) view.findViewById(R.id.huida);
		select_la = (ImageView) view.findViewById(R.id.select_la);
		select_rb = (ImageView) view.findViewById(R.id.select_rb);
		select_tva = (TextView) view.findViewById(R.id.select_tva);
		select_tvb = (TextView) view.findViewById(R.id.select_tvb);
		select_centerjx = (TextView) view.findViewById(R.id.select_centerjx);
		lianxian_text = (TextView) view.findViewById(R.id.lianxian_text);
		lvLeft = (NoScrollListview) view.findViewById(R.id.lv);
		lvRight = (NoScrollListview) view.findViewById(R.id.lv1);
		select_tva.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				nextOrLast.nextOrlast(1);
			}
		});
		select_la.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				nextOrLast.nextOrlast(1);
			}
		});
		select_tvb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				nextOrLast.nextOrlast(2);
			}
		});
		select_rb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				nextOrLast.nextOrlast(2);
			}
		});
		select_centerjx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent iteIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("explanation", homeWorkDetailQ.explanation);
				iteIntent.putExtras(bundle);
				iteIntent.setClass(getActivity(),
						SynSeletQuestionAnalyticalActivity.class);
				startActivity(iteIntent);
			}
		});
		lianxian_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getAnswer();
			}
		});
		select_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if ("".equals(ques)) {
					Toast.makeText(getActivity(), "请答题",

					Toast.LENGTH_SHORT).show();
				} else {
					if (homeWorkDetailQ.studentAnswer == null
							|| (homeWorkDetailQ.studentAnswer != null && homeWorkDetailQ.studentAnswer
									.size() == 0)) {
						if (!submited) {
							showWarning();
						}else {
							Toast.makeText(getActivity(), "不可再次提交作业", Toast.LENGTH_SHORT).show();
						}
					}else {
						Toast.makeText(getActivity(), "不可再次提交作业",Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	private void showWarning() {
		CustomDialog.Builder customDialog = new CustomDialog.Builder(
				getActivity());
		customDialog
				.setMessage("请注意,提交作业不可修改,是否继续？");
		customDialog.setTitle("提示");
		customDialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						SubmitHomework();
					}
				});
		customDialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		customDialog.create().show();
	}
	private boolean submited;
	private void SubmitHomework() {
		int k = 0;
		String t = "\"title\"";
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		while (true) {
			if (mapAnwser.containsKey(k)) {
				builder.append("{");
				builder.append(t + ":\"");
				builder.append(mapAnwser.get(k));
				builder.append("\"}");
				builder.append(",");
			}
			if (k > 10) {
				break;
			}
			k++;
		}
		;
		builder.append("]");
		String awser = builder.toString().replace(",]", "]");
		Map<String,Object> map=new HashMap<>();
		map.put("studentId", studentId);
		map.put("questionId", content_id);
		map.put("coursewareContentId", coursewareContentId);
		map.put("hourId", hourId);
		map.put("studentAnswer", awser);
		map.put("date", date);
		((HomeWorkDetailActivity) getActivity()).doPost(
				Config1.getInstance().HOMEWORKSUBMIT(), map, new ResultCallback() {
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
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				submited = true;
				JSONObject result=JSON.parseObject((String) msg.obj);
				((HomeWorkDetailActivity)getActivity()).result=223;
				((HomeWorkDetailActivity) getActivity()).setBackSetResult(223);
				List<StudentAnswer> answers = JSON
						.parseArray(
								result.getString("data"),
								StudentAnswer.class);
				int k = 0;
				String text = "";
				while (true) {
					if (mapAnwser.containsKey(k)) {
						text = text + mapAnwser.get(k) + " ";
					}
					if (k > 10) {
						break;
					}
					k++;
				}
				;
				if (rqueStr!=null&&rqueStr.equals(text)) {
					huida.setText("您的孩子的答案是" + text
							+ ",正确答案是" + rqueStr + ",回答正确");

				} else {
					huida.setText("您的孩子的答案是" + text
							+ ",正确答案是" + rqueStr + ",回答错误");
				}
			}else if (msg.what==2){

			}
		}
	};
	private void getAnswer() {
		// TODO Auto-generated method stub
		int i = 0, j = 0;
		String lianxian1 = "", lianxian2 = "";
		while (true) {
			if (map.containsKey(i)) {
				lianxian1 = NumberToCode.numberToCode(i);
				;
				break;
			}
			if (i > 10) {
				break;
			}
			i++;
		}
		;
		while (true) {
			if (map1.containsKey(j)) {
				lianxian2 = NumberToCode.numberToCode(j);
				break;
			}
			if (j > 10) {
				break;
			}
			j++;
		}
		;
		ques = lianxian2 + lianxian1;
		Log.v("TAG", "ques=" + ques);
		if ("".equals(ques) || ques.length() != 2 || "".equals(lianxian2)
				|| "".equals(lianxian1)) {
			Toast.makeText(getActivity(), "请点击连线选项",
					Toast.LENGTH_SHORT).show();
		} else {
			mapAnwser.put(NumberToCode.codeToNumber(lianxian2), ques);
			// [{"title":"A"}]
			int k = 0;
			String t = "\"title\"";
			StringBuilder builder = new StringBuilder();
			String text = "";
			builder.append("[");
			while (true) {
				if (mapAnwser.containsKey(k)) {
					builder.append("{");
					builder.append(t + ":");
					builder.append(mapAnwser.get(k));
					text = text + mapAnwser.get(k) + " ";
					builder.append("\"}");
					builder.append(",");
				}
				if (k > 10) {
					break;
				}
				k++;
			}
			;
			builder.append("]");
			String awser = builder.toString().replace(",]", "]");
			// String awser="[{"+t+":"+"\""+ques+"\"}]";
			Log.v("TAG", "t=" + awser);
			lianxian_text.setText(text);
		}
	}
}
