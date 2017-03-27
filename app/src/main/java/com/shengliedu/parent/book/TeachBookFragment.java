package com.shengliedu.parent.book;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.BookMenuAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.Teachbook;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class TeachBookFragment extends Fragment implements
		OnRefreshListener2<GridView> {
	private static String TAG = "TeachBookFragment";
	private PullToRefreshGridView book_list_grid;
	private View fView;
	private BaseActivity activity;
	private int grade;
	private int subject;
	private int editionVersion;
	private BookMenuAdapter adapter;
	private List<Teachbook> books = new ArrayList<Teachbook>();
	private int page = 1;
	private boolean up;

	@SuppressLint("ValidFragment")
	public TeachBookFragment(BaseActivity activity, int grade, int subject,
							 int editionVersion) {
		super();
		this.activity = activity;
		this.grade = grade;
		this.subject = subject;
		this.editionVersion = editionVersion;
	}	
	public TeachBookFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		fView = inflater
				.inflate(R.layout.fragment_teach_book, container, false);
		initView();
		return fView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		// editionVersionId=82&gradeId=1&limit=30&offset=0&subjectId=3&typeId=1
		book_list_grid = (PullToRefreshGridView) fView
				.findViewById(R.id.book_list_grid);
		book_list_grid.setMode(Mode.BOTH);
		adapter = new BookMenuAdapter(activity, books);
		book_list_grid.setAdapter(adapter);
		book_list_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (books.get(arg2).type==1) {
					Intent intent=new Intent(activity,TeachBookPageActivity.class);
					intent.putExtra("outlineId", books.get(arg2).outline);
					intent.putExtra("bookId", books.get(arg2).book_id);
					activity.startActivity(intent);
				}else if (books.get(arg2).type==2 || books.get(arg2).type==6){
					Intent intent=new Intent(activity,TeachBookPageHtmActivity.class);
					intent.putExtra("outlineId", books.get(arg2).outline);
					intent.putExtra("bookId", books.get(arg2).book_id);
					activity.startActivity(intent);
				}
			}
		});
		book_list_grid.setOnRefreshListener(this);
		getData();
	}
	Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject result=JSON.parseObject((String) msg.obj);
				book_list_grid.onRefreshComplete();
				List<Teachbook> booksTemp = JSON.parseArray(
						result.getString("books"), Teachbook.class);
				if(!up){
					books.clear();
				}
				if (!App.listIsEmpty(booksTemp)) {
					for (int i = 0; i < booksTemp.size(); i++) {
						if (booksTemp.get(i).quality==1) {
							books.add(booksTemp.get(i));
						}
					}
				}
				adapter.notifyDataSetChanged();
			}else if (msg.what==2){

			}else if (msg.what==3){
				JSONArray jsonArray=JSON.parseArray((String) msg.obj);
				String editionVersion=null;
				if (jsonArray!=null && jsonArray.size()>0){
					for (int i=0;i<jsonArray.size();i++){
						if (i!=jsonArray.size()-1){
							editionVersion=jsonArray.getJSONObject(i).getInteger("editionVersion")+",";
						}else {
							editionVersion=jsonArray.getJSONObject(i).getInteger("editionVersion")+"";
						}
					}
				}
				if (grade != 0 && subject != 0 ) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("limit", 20);
					map.put("offset", (page - 1) * 20);
					map.put("gradeId", grade);
					map.put("subjectId", subject);
//			map.put("editionVersionId", editionVersion);
					map.put("typeId", "1");
					if (!TextUtils.isEmpty(editionVersion)){
						map.put("editionVersionId", editionVersion);
					}
					activity.doGet(Config1.getInstance().BOOK_LIST(), map,
							new ResultCallback() {
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
				}else {
//					if (editionVersion==0) {
//						activity.toast("暂无该版本图书");
//					}
					books.clear();
					adapter.notifyDataSetChanged();
				}
			}else if (msg.what==4){

			}
		}
	};
	private void getData() {
//		获取校端年级学科相关版本信息
//		textbook?gradeSubjectEditionVersion=1&semester=40000&dept=11&grade=3&subject=3&classroom=143
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gradeSubjectEditionVersion", 1);
		map.put("semester", App.loginInfo.currentSemesterId);
		map.put("dept", App.beanSchoolInfo.parent);
		map.put("subject", subject);
		map.put("grade", grade);
		map.put("classroom", App.childInfo.classroomId);
		activity.doGet(Config1.getInstance().IP+"textbook?", map,
				new ResultCallback() {
					@Override
					public void onResponse(Call call, Response response, String json) {
						Message message=Message.obtain();
						message.what=3;
						message.obj=json;
						handlerReq.sendMessage(message);
					}

					@Override
					public void onFailure(Call call, IOException exception) {
						handlerReq.sendEmptyMessage(4);
					}
				});

	}


	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		// TODO Auto-generated method stub
		up = false;
		page = 1;
		getData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		// TODO Auto-generated method stub
		page++;
		up = true;
		getData();
	}
}
