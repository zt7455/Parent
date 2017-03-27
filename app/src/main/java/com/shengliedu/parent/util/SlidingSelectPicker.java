package com.shengliedu.parent.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shengliedu.parent.R;
import com.shengliedu.parent.util.SelectPicker.OnSelectListener;

public class SlidingSelectPicker extends LinearLayout {
	/** 滑动控件 */
	private SelectPicker cityPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 选择监听 */
	private OnsSelectedListener onSelectedListener;
//	/** 搜索城市监听 */
//	private OnSearchListener onSearchListener;
//	/** 上移监听 */
//	private OnMoveUpListener onMoveUpListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
//	/** 获取省 */
//	private static final int GETPROVINCES = 0x002;
//	/** 获取市 */
//	private static final int GETCITYS = 0x003;
	/** 临时城市 */
//	private int tempProvinceIndex = 0;
	private int temCityIndex = 0;
	private Context context;
//	private static ArrayList<City> provinces;
	private static ArrayList<String> dataList=new ArrayList<String>();
	private TextView button_comfirg;
	private ComparatorRecordByCapital comparator;

	
	public SlidingSelectPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public SlidingSelectPicker(Context context) {
		super(context);
		this.context = context;
	}

	// 获取城市信息
	public void setDatas(List<String> data) {
		dataList.clear();
		dataList.addAll(data);
//		try {
//			c=JSON.parseArray(new org.json.JSONObject(SharedPreferencesUtils.getCityJson(context)).getString("data"), BeanC.class).get(0);
//			LogUtils.e("ccc", c.toString());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		comparator = new ComparatorRecordByCapital();
		Collections.sort(dataList, comparator);
			cityPicker.setCity(dataList);
			cityPicker.setDefault(0);
	}

	private TextView sd_title;
	
	public void setTitle(String title){
		if (sd_title!=null) {
			sd_title.setText(title);
		}
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.select_dialog, this);
		// 获取控件引用
		cityPicker = (SelectPicker) findViewById(R.id.select_picker);
		button_comfirg = (TextView) findViewById(R.id.select_ok);
		sd_title = (TextView) findViewById(R.id.sd_title);
		button_comfirg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onSelectedListener != null) {
					if ( dataList != null && dataList.size()>0) {
						onSelectedListener.selected(
								dataList.get(temCityIndex));
					} else {
						onSelectedListener.selected( null);
					}
				}
			}
		});
		cityPicker.setCity(dataList);
		cityPicker.setDefault(0);
		cityPicker.setOnSelectListener(new OnSelectListener() {
			@Override
			public void endSelect(int id, String text) {
				if (text == null || text.equals(""))
					return;
				if (temCityIndex != id) {
					String selectDay = cityPicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					int lastDay = Integer.valueOf(cityPicker.getListSize());
					if (id > lastDay) {
						cityPicker.setDefault(lastDay - 1);
					}
				}
				temCityIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
			}
		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selecting(true);
				break;
			}
		}

	};

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public interface OnSelectingListener {
		public void selecting(boolean selected);
	}


	public interface OnMoveUpListener {
		public void moveUp(boolean isMove);
	}

	public interface OnSearchListener {
		public void search(String cityName);
	}

	public void setOnSelectedListener(OnsSelectedListener onSelectedListener) {
		this.onSelectedListener = onSelectedListener;
	}

	public interface OnsSelectedListener {
		public void selected(String city);
	}


	class ComparatorRecordByCapital implements Comparator<String> {
		@Override
		public int compare(String city1, String city2) {
			if (city1.compareTo(city2) > 0) {
				return 1;
			} else if (city1.compareTo(city2) == 0) {
				return 0;
			} else {
				return -1;
			}
		}
	}

}
