package com.shengliedu.parent.view;


import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.shengliedu.parent.util.LogUtils;


public class IndexViewPager extends ViewPager {

	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();
	OnSingleTouchListener onSingleTouchListener;

	public IndexViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public IndexViewPager(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context){
		ViewPagerScroller scroller = new ViewPagerScroller(context);
		scroller.setScrollDuration(1000);
		scroller.initViewPagerScroll(this);  //这个是设置切换过渡时间为0<a href="https://www.baidu.com/s?wd=%E6%AF%AB%E7%A7%92&tn=44039180_cpr&fenlei=mv6quAkxTZn0IZRqIHckPjm4nH00T1YYrjNbrjnzn10vnHFhPWnd0ZwV5Hcvrjm3rH6sPfKWUMw85HfYnjn4nH6sgvPsT6K1TL0qnfK1TL0z5HD0IgF_5y9YIZ0lQzqlpA-bmyt8mh7GuZR8mvqVQL7dugPYpyq8Q1nzP1mLPWb3PWDvP1msrHfYPf" target="_blank" class="baidu-highlight">毫秒</a>
		 
//		ViewPagerScroller scroller = new ViewPagerScroller(context);
//		scroller.setScrollDuration(2000);
//		scroller.initViewPagerScroll(this);//这个是设置切换过渡时间为2秒
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// 当拦截触摸事件到达此位置的时候，返回true，
		// 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// 每次进行onTouch事件都记录当前的按下的坐标
		curP.x = arg0.getX();
		curP.y = arg0.getY();
		if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
			// 记录按下时候的坐标
			// 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
			downP.x = arg0.getX();
			downP.y = arg0.getY();
		}

		if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
			if (Math.abs(downP.x - curP.x)*2 < Math.abs(downP.y - curP.y)) {
				getParent().requestDisallowInterceptTouchEvent(false);
			} else {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
		}

		if (arg0.getAction() == MotionEvent.ACTION_UP) {
			// 在up时判断是否按下和松手的坐标为一个点
			// 如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
			if (Math.abs(downP.x-curP.x)<=1 && Math.abs(downP.y-curP.y)<=0) {
				LogUtils.i("0>>>>", (downP.x-curP.x)+"");
				onSingleTouch();
			}else{
				LogUtils.i(">>>>", (downP.x-curP.x)+"");
			}
		}
		return super.onTouchEvent(arg0);
	}

	/**
	 * 单击
	 * @author yang.lei
	 */
	public void onSingleTouch() {
		if (onSingleTouchListener != null) {
			onSingleTouchListener.onSingleTouch();
		}
	}

	/**
	 * 创建点击事件接口
	 * @author yang.lei
	 */
	public interface OnSingleTouchListener {
		public void onSingleTouch();
	}

	public void setOnSingleTouchListener(
			OnSingleTouchListener onSingleTouchListener) {
		this.onSingleTouchListener = onSingleTouchListener;
	}

	/**
	 * ViewPager 滚动速度设置
	 * 
	 */
	@SuppressLint("NewApi")
	public class ViewPagerScroller extends Scroller {
	    private int mScrollDuration = 2000;             // 滑动速度
	 
	    /**
	     * 设置速度速度
	     * @param duration
	     */
	    public void setScrollDuration(int duration){
	        this.mScrollDuration = duration;
	    }
	     
	    public ViewPagerScroller(Context context) {
	        super(context);
	    }
	 
	    public ViewPagerScroller(Context context, Interpolator interpolator) {
	        super(context, interpolator);
	    }
	 
	    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
	        super(context, interpolator, flywheel);
	    }
	 
	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
	        super.startScroll(startX, startY, dx, dy, mScrollDuration);
	    }
	 
	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy) {
	        super.startScroll(startX, startY, dx, dy, mScrollDuration);
	    }
	 
	     
	     
	    public void initViewPagerScroll(ViewPager viewPager) {
	        try {
	            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
	            mScroller.setAccessible(true);
	            mScroller.set(viewPager, this);
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	 
}
