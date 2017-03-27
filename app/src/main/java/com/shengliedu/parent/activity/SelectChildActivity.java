package com.shengliedu.parent.activity;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.adapter.ChildListAdapter;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.bean.ChildInfo;

public class SelectChildActivity extends BaseActivity {
	private ListView child_list;
	private ChildListAdapter adapter;
	private List<ChildInfo> list;
	private int type;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		showTitle("选择孩子");
		type = getIntent().getIntExtra("time", -1);
		list = App.loginInfo.children;
		child_list = getView(R.id.child_list);
		adapter = new ChildListAdapter(this, list);
		child_list.setAdapter(adapter);
		child_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (App.childInfo.id != 0
						&& App.childInfo.id == list.get(arg2).id) {
				} else {
					App.childInfo = list.get(arg2);
				}
				Intent intent = new Intent(SelectChildActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityByAniamtion(intent);
				finishActivityByAniamtion();
			}
		});
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_select_child;
	}

}
