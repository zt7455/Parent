package com.shengliedu.parent.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.BeanBinnal;
import com.shengliedu.parent.util.Config1;
import com.shengliedu.parent.util.LogUtils;
import com.shengliedu.parent.util.MImageLoader;
import com.shengliedu.parent.view.IndexViewPager.OnSingleTouchListener;

/**
 * 广告轮播图 AdvertisementView 传递过来 list
 * 
 * @author yang.lei
 */
@SuppressLint("HandlerLeak")
public class AdvertisementViews_medium extends FrameLayout {

	private static final String TAG = "AdvertisementViews_medium";
	// 自动轮播的时间间隔
	private static int TIME_INTERVAL = 3;
	// 自动轮播启用开关
	private static boolean isAutoPlay = true;
	// 网络获取的轮播图的網址
	private List<BeanBinnal> urlList;
	// 放轮播图片的ImageView 的list
	private List<ImageView> imageViewsList;
	// 放圆点的View的list
	private List<ImageView> dotViewsList;
	private MyPagerAdapter adapter;
	private IndexViewPager viewPager;
	private int currentItem = 0;
	private ScheduledExecutorService scheduledExecutorService;
	private Context mContext;
	private LinearLayout linearLayout_dot;
	private SlideShowTask showTask;
	/**
	 * 1 商圈首页 2 门店详情 3 商户详情 4 商城首页 5 商品详情
	 */
	private int IMAGE_COUNT;
//	private DisplayImageOptions options;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			viewPager.setCurrentItem(currentItem);
		}
	};

	public AdvertisementViews_medium(Context context) {
		this(context, null);
		mContext = context;
	}

	public AdvertisementViews_medium(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mContext = context;
	}

	public AdvertisementViews_medium(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	private void startPlay() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(showTask, TIME_INTERVAL,
				4, TimeUnit.SECONDS);
	}

	private void stopPlay() {
		if (scheduledExecutorService != null)
			scheduledExecutorService.shutdownNow();
	}

	private void startAdvertisement() {
		if (showTask == null) {
			LogUtils.i(TAG, "showTask");
			showTask = new SlideShowTask();
		}
		if (isAutoPlay) {
			startPlay();
		}
	}

	/**
	 * 
	 * @param context
	 * @param list
	 */
	public void initUIData(Context context, List<BeanBinnal> list) {
		if (scheduledExecutorService == null) {
			scheduledExecutorService = Executors
					.newSingleThreadScheduledExecutor();
		}
		urlList = list;
		IMAGE_COUNT = urlList.size();
		LayoutInflater.from(context).inflate(
				R.layout.layout_advertisement_list_medium, this, true);
		linearLayout_dot = (LinearLayout) findViewById(R.id.linearLayout_dot);
		imageViewsList = new ArrayList<ImageView>();
		dotViewsList = new ArrayList<ImageView>();
		linearLayout_dot.removeAllViews();

//		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.mipmap.advertisement_placeholder_medium)
//				// 设置图片在下载期间显示的图片
//				.showImageForEmptyUri(
//						R.mipmap.advertisement_placeholder_medium)
//				// 设置图片Uri为空或是错误的时候显示的图片
//				.showImageOnFail(R.mipmap.advertisement_placeholder_medium)
//				// 设置图片加载/解码过程中错误时候显示的图片
//				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
//				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
//				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
//				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
//				.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
//				.build();// 构建完成

		ImageView view;
		Drawable drawable;
		imageViewsList.clear();
		if (IMAGE_COUNT >= 2) {
			view = new ImageView(mContext);
			view.setScaleType(ScaleType.FIT_XY);
			drawable = view.getDrawable();
			if (drawable != null) {
				drawable.setCallback(null);
			}
//			ImageLoader.getInstance().displayImage(
//					urlList.get(IMAGE_COUNT - 2).imgUrl, view, options);
			MImageLoader.displayImage(mContext, Config1.getInstance().IP+urlList.get(IMAGE_COUNT - 2).pic, view);
			imageViewsList.add(view);
			view = new ImageView(mContext);
			view.setScaleType(ScaleType.FIT_XY);
			drawable = view.getDrawable();
			if (drawable != null) {
				drawable.setCallback(null);
			}
//			ImageLoader.getInstance().displayImage(
//					urlList.get(IMAGE_COUNT - 1).imgUrl, view, options);
			MImageLoader.displayImage(mContext, Config1.getInstance().IP+urlList.get(IMAGE_COUNT - 1).pic, view);
			imageViewsList.add(view);
		}

		for (int i = 0; i < urlList.size(); i++) {
			if (urlList.size() > 1) {
				ImageView dot = new ImageView(getContext());
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(4, 0, 0, 0);
				dot.setLayoutParams(params);
				if (i == 0)
					dot.setImageResource(R.mipmap.dot_red);
				else
					dot.setImageResource(R.mipmap.dot_white);
				dotViewsList.add(dot);
				linearLayout_dot.addView(dot);
			}
			view = new ImageView(mContext);
			view.setScaleType(ScaleType.FIT_XY);
			drawable = view.getDrawable();
			if (drawable != null) {
				drawable.setCallback(null);
			}
//			ImageLoader.getInstance().displayImage(urlList.get(i).imgUrl, view,
//					options);
			MImageLoader.displayImage(mContext, Config1.getInstance().IP+urlList.get(i).pic, view);
			imageViewsList.add(view);
		}

		if (IMAGE_COUNT >= 2) {
			view = new ImageView(mContext);
			view.setScaleType(ScaleType.FIT_XY);
			drawable = view.getDrawable();
			if (drawable != null) {
				drawable.setCallback(null);
			}
//			ImageLoader.getInstance().displayImage(urlList.get(0).imgUrl, view,
//					options);
			MImageLoader.displayImage(mContext, Config1.getInstance().IP+urlList.get(0).pic, view);
			imageViewsList.add(view);

			view = new ImageView(mContext);
			view.setScaleType(ScaleType.FIT_XY);
			drawable = view.getDrawable();
			if (drawable != null) {
				drawable.setCallback(null);
			}
//			ImageLoader.getInstance().displayImage(urlList.get(1).imgUrl, view,
//					options);
			MImageLoader.displayImage(mContext, Config1.getInstance().IP+urlList.get(1).pic, view);
			imageViewsList.add(view);
		}
		viewPager = (IndexViewPager) findViewById(R.id.viewPager);
		viewPager.setFocusable(true);
		viewPager.setOnSingleTouchListener(new OnSingleTouchListener() {

			@Override
			public void onSingleTouch() {
//					ToastUtil.toast(mContext, currentItem-2+"   "+viewPager.getCurrentItem()); 
					//Intent intent = new Intent(mContext, ImageDetailActivity.class);
//					intent.putExtra("userid", urlList.get(viewPager.getCurrentItem()).id);
					//mContext.startActivity(intent);
			}
		});
		viewPager.setOffscreenPageLimit(IMAGE_COUNT);
		adapter = new MyPagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		viewPager.setCurrentItem(2);
		this.requestLayout();
		stopPlay();
		startAdvertisement();
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(imageViewsList.get(position));
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			((ViewPager) container).addView(imageViewsList.get(position));
			return imageViewsList.get(position);
		}

		@Override
		public int getCount() {
			return imageViewsList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}

	}

	/**
	 * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author yang.lei
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case 1:// 手势滑动中
					// LogUtils.i(TAG, "scrolling");
				isAutoPlay = false;
				stopPlay();
				break;
			case 2:// 滑动结束
					// LogUtils.i(TAG, "scrolled");
				isAutoPlay = true;
				stopPlay();
				startPlay();
				break;
			case 0:// 切换完毕或者加载完毕
					// LogUtils.i(TAG, "?????");
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// LogUtils.i(TAG, "start:  "+arg0);
			// LogUtils.i(TAG, "percent:  "+arg1);
			// LogUtils.i(TAG, "end:  "+arg2);
		}

		@Override
		public void onPageSelected(int pos) {
			// LogUtils.i(TAG, "scrolled>>>"+pos);
			if (pos == 0) {
				viewPager.setCurrentItem(IMAGE_COUNT, false);
				currentItem = IMAGE_COUNT;
			} else if (pos == IMAGE_COUNT + 2) {
				viewPager.setCurrentItem(1, false);
				currentItem = 2;
				handler.obtainMessage().sendToTarget();
			} else {
				currentItem = pos;
			}
			for (int i = 0; i < dotViewsList.size(); i++) {
				dotViewsList.get(i).setImageResource(R.mipmap.dot_white);
			}

			if (IMAGE_COUNT >= 4) {
				if (pos == 2 || pos == IMAGE_COUNT + 2) {
					dotViewsList.get(0).setImageResource(R.mipmap.dot_red);
				} else if (pos == 3 || pos == IMAGE_COUNT + 3) {
					dotViewsList.get(1).setImageResource(R.mipmap.dot_red);
				} else if (pos == 0 || pos == IMAGE_COUNT + 0) {
					dotViewsList.get(IMAGE_COUNT - 2).setImageResource(
							R.mipmap.dot_red);
				} else if (pos == 1 || pos == IMAGE_COUNT + 1) {
					dotViewsList.get(IMAGE_COUNT - 1).setImageResource(
							R.mipmap.dot_red);
				} else {
					dotViewsList.get(pos - 2).setImageResource(
							R.mipmap.dot_red);
				}
			} else if (IMAGE_COUNT == 3) {
				if (pos == 2 || pos == IMAGE_COUNT + 2) {
					dotViewsList.get(0).setImageResource(R.mipmap.dot_red);
				} else if (pos == 3 || pos == IMAGE_COUNT + 3
						|| pos == IMAGE_COUNT - 3) {
					dotViewsList.get(1).setImageResource(R.mipmap.dot_red);
				} else if (pos == 1 || pos == IMAGE_COUNT + 1) {
					dotViewsList.get(2).setImageResource(R.mipmap.dot_red);
				}
			} else if (IMAGE_COUNT == 2) {
				if (pos == 0 || pos == 2 || pos == 4) {
					dotViewsList.get(0).setImageResource(R.mipmap.dot_red);
				} else {
					dotViewsList.get(1).setImageResource(R.mipmap.dot_red);
				}
			}
		}
	}

	/**
	 * 执行轮播图切换任务
	 * 
	 * @author yang.lei
	 */
	private class SlideShowTask implements Runnable {
		@Override
		public void run() {
			synchronized (viewPager) {
				if (isAutoPlay)
					currentItem++;
				handler.obtainMessage().sendToTarget();
			}
		}
	}

}
