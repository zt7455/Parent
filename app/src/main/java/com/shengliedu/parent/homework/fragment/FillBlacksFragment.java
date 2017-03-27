package com.shengliedu.parent.homework.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.AfterClassAdapter;
import com.shengliedu.parent.adapter.AswerItemAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.HomeWorkDetailQ;
import com.shengliedu.parent.bean.SynQuestion;
import com.shengliedu.parent.homework.HomeWorkDetailActivity;
import com.shengliedu.parent.pdf.ShowPdfFromUrlActivity;
import com.shengliedu.parent.showdesign.SynFillBlanksActivity;
import com.shengliedu.parent.synclass.SynSeletQuestionAnalyticalActivity;
import com.shengliedu.parent.synclass.interf.NextOrLast;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.HtmlImage;
import com.shengliedu.parent.util.NumberToCode;
import com.shengliedu.parent.util.ResultCallback;
import com.shengliedu.parent.view.NoScrollGridView;
import com.shengliedu.parent.view.NoScrollListview;
import com.shengliedu.parent.widght.CustomDialog;

import okhttp3.Call;
import okhttp3.Response;

import static com.shengliedu.parent.R.id.synsingleLin;

@SuppressLint("ValidFragment")
public class FillBlacksFragment extends Fragment{
	private View view;

	private TextView select_title, question_select_item, pandun, huida,
			select_tva, select_tvb, select_centerjx, panduan_hint;
	private ImageView select_la, select_rb;
	private Button select_btn;
	private NoScrollListview listView;
	private NoScrollGridView item_aswer;
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();

	private String ques = "";
	private String rques = "";
	private HomeWorkDetailQ homeWorkDetailQ;

	private String coursewareContentId;
	private String hourId;
	private String content_id;
	private String studentId;
	private String date;
	private String questionContentType;
	private NextOrLast nextOrLast;
	private LinearLayout fsinglecLin,fsinglecLin2;
	private TextView select_pdf;

	public FillBlacksFragment(HomeWorkDetailQ homeWorkDetailQ) {
		// TODO Auto-generated constructor stub
		this.homeWorkDetailQ = homeWorkDetailQ;
	}

	public FillBlacksFragment(HomeWorkDetailQ homeWorkDetailQ,
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_homework_single, container,
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
		}
		if (homeWorkDetailQ.correct != null
				& homeWorkDetailQ.correct.size() != 0) {
			rques = homeWorkDetailQ.correct.get(0).title;
		}
		if ("A".equals(ques)) {
			map.clear();
			map.put(0, true);
		} else if ("B".equals(ques)) {
			map.clear();
			map.put(1, true);
		} else if ("C".equals(ques)) {
			map.clear();
			map.put(2, true);
		} else if ("D".equals(ques)) {
			map.clear();
			map.put(3, true);
		} else if ("E".equals(ques)) {
			map.clear();
			map.put(4, true);
		} else if ("F".equals(ques)) {
			map.clear();
			map.put(5, true);
		} else if ("G".equals(ques)) {
			map.clear();
			map.put(6, true);
		} else if ("H".equals(ques)) {
			map.clear();
			map.put(7, true);
		} else if ("I".equals(ques)) {
			map.clear();
			map.put(8, true);
		} else if ("J".equals(ques)) {
			map.clear();
			map.put(9, true);
		}
		Log.v("TAG", "url=" + homeWorkDetailQ.question.contentLink);
		if ("1".equals(questionContentType)) {
			select_pdf.setVisibility(View.VISIBLE);
			select_pdf.setText("查看PDF题目");
			select_pdf.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(getActivity(),ShowPdfFromUrlActivity.class);
					intent.putExtra("url", Config1.IP + homeWorkDetailQ.question.contentLink);
					startActivity(intent);
				}
			});
		} else {
			select_pdf.setVisibility(View.GONE);
		}
		List<String> items = new ArrayList<String>();
		if (!App.listIsEmpty(homeWorkDetailQ.answer.answers)
				&& !App.listIsEmpty(homeWorkDetailQ.answer.answers.get(0))) {
			AfterClassAdapter adapter = new AfterClassAdapter(getActivity(),
					homeWorkDetailQ);
			listView.setAdapter(adapter);
			for (int i = 0; i < homeWorkDetailQ.answer.answers.size(); i++) {
				items.add(homeWorkDetailQ.answer.answers.get(i).get(0).title);
			}
		} else {
			if (homeWorkDetailQ.type == 1) {
				for (int i = 0; i < 6; i++) {
					items.add(NumberToCode.numberToCode(i));
				}
			} else if (homeWorkDetailQ.type == 3) {
				items.add("A");
				items.add("B");
			}
		}
		if (homeWorkDetailQ.type == 3) {
			panduan_hint.setVisibility(View.VISIBLE);
		}
		final AswerItemAdapter adapter = new AswerItemAdapter(getActivity(),
				map, items);
		item_aswer.setAdapter(adapter);
		if (!"".equals(ques)) {
			if (ques != null) {
				ques = (Html.fromHtml(ques) + "").replace(" ", "");
			}
			if (rques != null) {
				rques = (Html.fromHtml(rques) + "").replace(" ", "");
			}
			if (ques.equals(rques)) {
				huida.setText("正确答案是" + rques + ",回答正确");
			} else {
				huida.setText("正确答案是" + rques + ",回答错误");
			}

		}
		select_title.setText(Html.fromHtml(HtmlImage
				.deleteSrc(homeWorkDetailQ.question.main)));
		question_select_item.setText(Html.fromHtml(HtmlImage
				.deleteSrc(homeWorkDetailQ.question.question)));
		String main = homeWorkDetailQ.question.main;
		List<String> imgList = HtmlImage.getImgSrc(main);
		ImageLoader utils = ImageLoader.getInstance();
		if (imgList.size() > 0) {
			for (int j = 0; j < imgList.size(); j++) {
				final String url = imgList.get(j);
				ImageView imageView = new ImageView(getActivity());
				imageView.setScaleType(ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				utils.displayImage(url, imageView);
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated
						// method stub
						HtmlImage htmlImage = new HtmlImage();
						htmlImage.showDialog(getActivity(), url);
					}
				});
				fsinglecLin.addView(imageView);
			}
		}
		String name = homeWorkDetailQ.question.question;
		List<String> imgList2 = HtmlImage.getImgSrc(name);
		ImageLoader utils2 = ImageLoader.getInstance();
		if (imgList.size() > 0) {
			for (int j = 0; j < imgList2.size(); j++) {
				final String url = imgList.get(j);
				ImageView imageView = new ImageView(getActivity());
				imageView.setScaleType(ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				utils2.displayImage(url, imageView);
				imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated
						// method stub
						HtmlImage htmlImage = new HtmlImage();
						htmlImage.showDialog(getActivity(), url);
					}
				});
				fsinglecLin2.addView(imageView);
			}
		}
		pandun.setText(homeWorkDetailQ.typeName);

		item_aswer.setOnItemClickListener(new OnItemClickListener() {

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
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void initViews() {
		// TODO Auto-generated method stub
		select_pdf = (TextView) view.findViewById(R.id.select_pdf);
		fsinglecLin = (LinearLayout) view.findViewById(R.id.fsinglecLin);
		fsinglecLin2 = (LinearLayout) view.findViewById(R.id.fsinglecLin2);
		panduan_hint = (TextView) view.findViewById(R.id.panduan_hint);
		select_title = (TextView) view.findViewById(R.id.select_title);
		question_select_item = (TextView) view
				.findViewById(R.id.question_select_item);
		pandun = (TextView) view.findViewById(R.id.pandun);
		select_btn = (Button) view.findViewById(R.id.select_btn);
		huida = (TextView) view.findViewById(R.id.huida);
		select_la = (ImageView) view.findViewById(R.id.select_la);
		select_rb = (ImageView) view.findViewById(R.id.select_rb);
		select_tva = (TextView) view.findViewById(R.id.select_tva);
		select_tvb = (TextView) view.findViewById(R.id.select_tvb);
		select_centerjx = (TextView) view.findViewById(R.id.select_centerjx);
		listView = (NoScrollListview) view.findViewById(R.id.lv);
		item_aswer = (NoScrollGridView) view.findViewById(R.id.item_aswer);
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
						Toast.makeText(getActivity(), "不可再次提交作业", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	private void showWarning() {
		CustomDialog.Builder customDialog = new CustomDialog.Builder(
				getActivity());
		customDialog.setMessage("请注意,提交作业不可修改,是否继续？");
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
		String t = "\"title\"";
		String awser = "[{" + t + ":" + "\"" + ques + "\"}]";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", studentId);
		map.put("questionId", content_id);
		map.put("coursewareContentId", coursewareContentId);
		map.put("hourId", hourId);
		map.put("studentAnswer", awser);
		map.put("date", date);
		((HomeWorkDetailActivity) getActivity()).doPost(Config1.getInstance()
				.HOMEWORKSUBMIT(), map, new ResultCallback() {
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
				((HomeWorkDetailActivity) getActivity()).result = 223;
				((HomeWorkDetailActivity) getActivity()).setBackSetResult(223);
				if (ques.equals(rques)) {
					huida.setText("您的孩子的答案是" + ques + ",正确答案是" + rques
							+ ",回答正确");

				} else {
					huida.setText("您的孩子的答案是" + ques + ",正确答案是" + rques
							+ ",回答错误");
				}
				submited = true;
			}else if (msg.what==2){

			}
		}
	};
}
