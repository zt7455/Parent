package com.shengliedu.parent.synclass.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.SynLianXianAdapter;
import com.shengliedu.parent.adapter.SynQuestionAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.SynQuestion;
import com.shengliedu.parent.synclass.SynSeletQuestionAnalyticalActivity;
import com.shengliedu.parent.synclass.interf.NextOrLast;
import com.shengliedu.parent.util.HtmlImage;
import com.shengliedu.parent.util.MyImageGetter;
import com.shengliedu.parent.util.NumberToCode;
import com.shengliedu.parent.view.NoScrollListview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneToMoreQuestionFragment extends Fragment {
	private View view;
	private boolean isPrevious;
	private boolean isNext;

	public TextView select_title, pandun, zhengquelv, syn_question_head;
	public ImageView nextmore, lastmore;
	public Button select_btn;
	private String ques = "";
	private String rques = "";
	private Map<Integer, Boolean> map2 = new HashMap<Integer, Boolean>();
	private Map<Integer, Boolean> map1 = new HashMap<Integer, Boolean>();
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private ListView listView;
	private NoScrollListview listView1;
	private NoScrollListview listView2;
	private SynQuestion data;
	private String type;
	private String lineCount;
	private NextOrLast nextOrLast;
	private LinearLayout onetomoreLin, xuanxianglinear;

	@SuppressLint("ValidFragment")
	public OneToMoreQuestionFragment(SynQuestion data, boolean isPrevious,
									 boolean isNext) {
		// TODO Auto-generated constructor stub
		this.data = data;
		this.isPrevious = isPrevious;
		this.isNext = isNext;
	}
	public OneToMoreQuestionFragment(){
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_syn_onformoreq, container,
				false);
		type = data.type;
		lineCount = data.answer.lineCount;
		nextOrLast = (NextOrLast) getActivity();
		initViews();
		dealSomething();
		return view;
	}

	private void initViews() {
		// TODO Auto-generated method stub
		select_title = (TextView) view.findViewById(R.id.syn_question_title);
		onetomoreLin = (LinearLayout) view.findViewById(R.id.onetomoreLin);
		xuanxianglinear = (LinearLayout) view
				.findViewById(R.id.xuanxianglinear);
		pandun = (TextView) view.findViewById(R.id.syn_question_pandun);
		listView = (NoScrollListview) view.findViewById(R.id.syn_question_lv);
		listView1 = (NoScrollListview) view.findViewById(R.id.syn_question_lv1);
		listView2 = (NoScrollListview) view.findViewById(R.id.syn_question_lv2);
		select_btn = (Button) view.findViewById(R.id.jiexi);
		zhengquelv = (TextView) view.findViewById(R.id.zhengquelv);
		nextmore = (ImageView) view.findViewById(R.id.next_more);
		lastmore = (ImageView) view.findViewById(R.id.last_more);
		if (isPrevious) {
			lastmore.setVisibility(View.VISIBLE);
		} else {
			lastmore.setVisibility(View.GONE);
		}
		if (isNext) {
			nextmore.setVisibility(View.VISIBLE);
		} else {
			nextmore.setVisibility(View.GONE);
		}
		lastmore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v("TAG", "1");
				nextOrLast.nextOrlast(1);
			}
		});
		nextmore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v("TAG", "2");
				nextOrLast.nextOrlast(2);
			}
		});
		select_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent iteIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("explanation", data.explanation);
				iteIntent.putExtras(bundle);
				iteIntent.setClass(getActivity(),
						SynSeletQuestionAnalyticalActivity.class);
				startActivity(iteIntent);
			}
		});
	}

	private void dealSomething() {
		// TODO Auto-generated method stub
		// select_title.setText(Html.fromHtml(data.question.main)+"\n"+Html.fromHtml(data.question.question));
		Spanned spanned1 = Html.fromHtml(data.question.main, new MyImageGetter(
				getActivity(), select_title), null);
		Spanned spanned2 = Html.fromHtml(HtmlImage
				.deleteSrc(data.question.question));
		select_title.setText(spanned1 + "" + spanned2);
		String name = data.question.question;
		List<String> imgList = HtmlImage.getImgSrc(name);
		if (imgList.size() > 0) {
			for (int j = 0; j < imgList.size(); j++) {
				final String url = imgList.get(j);
				ImageView imageView = new ImageView(getActivity());
				imageView.setScaleType(ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				ImageLoader imageLoader=ImageLoader.getInstance();
				imageLoader.displayImage(url, imageView);
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated
						// method stub
						HtmlImage htmlImage = new HtmlImage();
						htmlImage.showDialog(getActivity(), url);
					}
				});
				onetomoreLin.addView(imageView);
			}
		}
		if (data.studentAnswer != null && data.studentAnswer.size() > 0) {
			ques = data.studentAnswer.get(0).title;
		}
		rques = data.correct.get(0).title;
		Log.e("ques", "haha" + ques);
		Log.e("rques", rques);
		String per = data.percentage;
		boolean a = true;
		if ("1".equals(type) || "3".equals(type)) {
			listView.setVisibility(View.VISIBLE);
			listView1.setVisibility(View.GONE);
			listView2.setVisibility(View.GONE);
			if (ques != null) {
				map.put(NumberToCode.codeToNumber(ques), true);
			}
			List<SynQuestion> dataList = new ArrayList<SynQuestion>();
			dataList.add(data);
			if (!App.listIsEmpty(data.answer.answers)
					&& !App.listIsEmpty(data.answer.answers.get(0))) {
				xuanxianglinear.setVisibility(View.VISIBLE);
				SynQuestionAdapter adapter = new SynQuestionAdapter(
						getActivity(), dataList, map);
				listView.setAdapter(adapter);
			} else {
				xuanxianglinear.setVisibility(View.GONE);
			}
		} else if ("2".equals(type) || "4".equals(type)) {
			listView.setVisibility(View.VISIBLE);
			listView1.setVisibility(View.GONE);
			listView2.setVisibility(View.GONE);
			if (ques != null) {
				for (int i = 0; i < ques.length(); i++) {
					String code = String.valueOf(ques.charAt(i));
					map.put(NumberToCode.codeToNumber(code), true);
				}
			}
			List<SynQuestion> dataList = new ArrayList<SynQuestion>();
			dataList.add(data);
			if (!App.listIsEmpty(data.answer.answers)
					&& !App.listIsEmpty(data.answer.answers.get(0))) {
				xuanxianglinear.setVisibility(View.VISIBLE);
				SynQuestionAdapter adapter = new SynQuestionAdapter(
						getActivity(), dataList, map);
				listView.setAdapter(adapter);
			} else {
				xuanxianglinear.setVisibility(View.GONE);
			}
		} else if ("5".equals(type) && "2".equals(lineCount)) {
			listView.setVisibility(View.VISIBLE);
			listView1.setVisibility(View.VISIBLE);
			listView2.setVisibility(View.GONE);
			if (ques != null && ques.length() == 2) {
				map.put(NumberToCode.codeToNumber(ques.substring(0, 1)), true);
				map1.put(NumberToCode.codeToNumber(ques.substring(1, 2)), true);
			}
			SynLianXianAdapter adapter = new SynLianXianAdapter(getActivity(),
					data.answer.answersF.get(0).items, map);
			listView.setAdapter(adapter);
			SynLianXianAdapter adapter1 = new SynLianXianAdapter(getActivity(),
					data.answer.answersF.get(1).items, map1);
			listView1.setAdapter(adapter1);
		} else if ("5".equals(type) && "3".equals(lineCount)) {
			listView.setVisibility(View.VISIBLE);
			listView1.setVisibility(View.VISIBLE);
			listView2.setVisibility(View.VISIBLE);
			if (ques != null && ques.length() == 3) {
				map.put(NumberToCode.codeToNumber(ques.substring(0, 1)), true);
				map1.put(NumberToCode.codeToNumber(ques.substring(1, 2)), true);
				map2.put(NumberToCode.codeToNumber(ques.substring(2, 3)), true);
			}
			SynLianXianAdapter adapter = new SynLianXianAdapter(getActivity(),
					data.answer.answersF.get(0).items, map);
			listView.setAdapter(adapter);
			SynLianXianAdapter adapter1 = new SynLianXianAdapter(getActivity(),
					data.answer.answersF.get(1).items, map1);
			listView1.setAdapter(adapter1);
			SynLianXianAdapter adapter2 = new SynLianXianAdapter(getActivity(),
					data.answer.answersF.get(2).items, map2);
			listView2.setAdapter(adapter2);
		}

		String rqueStr = "";
		if (data.correct != null & data.correct.size() != 0) {
			rques = data.correct.get(0).title;
			for (int i = 0; i < data.correct.size(); i++) {
				rqueStr = rqueStr + data.correct.get(i).title + " ";
			}
		}
		int i = 0;
		String text = "";
		if (data.studentAnswer != null && data.studentAnswer.size() > 0) {
			for (int j = 0; j < data.studentAnswer.size(); j++) {
				if (rqueStr.contains(data.studentAnswer.get(i).title)) {
					i++;
				}
				text = text + data.studentAnswer.get(j).title + " ";
			}

		}
		if (!"".equals(rqueStr)) {
			if (data.correct.size() == i) {
				zhengquelv.setText("您孩子的答案是" + text + ",正确答案是" + rqueStr
						+ ",回答正确！" + "全班回答正确率 " + per + "%");

			} else {
				zhengquelv.setText("您孩子的答案是" + text + ",正确答案是" + rqueStr
						+ ",回答错误！" + "全班回答正确率 " + per + "%");
			}

		} else {
			zhengquelv.setText("您孩子未回答 正确答案是" + rqueStr + " 全班回答正确率 " + per
					+ "%");
		}

	}

}
