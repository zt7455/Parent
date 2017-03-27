package com.shengliedu.parent.more.score;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.util.Config1;

public class WebFragment extends Fragment {
	private View view;
	private WebView webView;
	private BaseActivity activity;
	private String subjectId;
	private String studentId;
	private String semesterId;
	private String swDate;
	private String ewDate;
	private int type;
	private String url;
	public WebFragment(){
		super();
	}
	@SuppressLint("ValidFragment")
	public WebFragment(BaseActivity activity, String subjectId,
					   String studentId, String semesterId, int type) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.semesterId = semesterId;
		this.studentId = studentId;
		this.subjectId = subjectId;
		this.type = type;
		switch (type) {
		case 1:
			url = Config1.getInstance().SUBJECTEXAM() + semesterId + "/" + subjectId + "/"
					+ studentId;
			break;
		case 2:
			url = Config1.getInstance().LESSONSHOWMONTH() + semesterId + "/" + subjectId + "/"
					+ studentId;
			break;
		case 3:
			url = Config1.getInstance().HOMEWORKEXPRESSMONTH() + semesterId + "/" + subjectId + "/"
					+ studentId;
			break;
		default:
			break;
		}
		Log.v("TAG","url:"+url);
	}

	@SuppressLint("ValidFragment")
	public WebFragment(BaseActivity activity, String subjectId,
					   String studentId, String swDate, String ewDate, int type) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.subjectId = subjectId;
		this.studentId = studentId;
		this.swDate = swDate;
		this.ewDate = ewDate;
		this.type = type;
		switch (type) {
		case 2:
			url = Config1.getInstance().LESSONSHOWWEEK() + subjectId + "/" + swDate + "/"
					+ ewDate + "/" + studentId;
			break;
		case 3:
			url = Config1.getInstance().HOMEWORKEXPRESS() + subjectId + "/" + swDate + "/"
					+ ewDate + "/" + studentId;
			break;

		default:
			break;
		}
		Log.v("TAG","url:"+url);
	}

	@SuppressLint("ValidFragment")
	public WebFragment(BaseActivity activity, int type, String studentId,
					   String swDate, String ewDate) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.studentId = studentId;
		this.swDate = swDate;
		this.ewDate = ewDate;
		this.type = type;
		switch (type) {
		case 1:
			url = Config1.getInstance().LESSONBEHAVIOR() + swDate + "/" + ewDate + "/"
					+ studentId;
			break;
		case 2:
			url = Config1.getInstance().ATTENDANCE() + swDate + "/" + ewDate + "/" + studentId;
			break;
		default:
			break;
		}
		Log.v("TAG","url:"+url);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_web, container, false);
		initView();
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		webView = (WebView) view.findViewById(R.id.fragment_web);
		WebSettings settings = webView.getSettings();
		int screenDensity = getResources().getDisplayMetrics().densityDpi;
		WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
		switch (screenDensity) {
		case DisplayMetrics.DENSITY_LOW:
			zoomDensity = WebSettings.ZoomDensity.CLOSE;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			zoomDensity = WebSettings.ZoomDensity.MEDIUM;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			zoomDensity = WebSettings.ZoomDensity.FAR;
			break;
		}
		settings.setDefaultZoom(zoomDensity);
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		activity.showRoundProcessDialog(activity);
		webView.loadUrl(url);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				activity.showRoundProcessDialogCancel();
			}
		});

	}
}
