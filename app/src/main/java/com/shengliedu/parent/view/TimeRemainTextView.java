package com.shengliedu.parent.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.util.TimeUtils;

import java.util.HashMap;

/**
 * Created by zhaoyu on 2014/12/15.
 */
public class TimeRemainTextView extends TextView {

	public HashMap<Long, CountDownTimer> timeDowners = new HashMap<Long, CountDownTimer>();
	private boolean isAttachedWindow = false;
	private long endTime;
	private long currentId;
	private static final int COUNT_DOWN_INVERTAL = 1000;
	private static final String TAG = TimeRemainTextView.class.getSimpleName();

	public void cancelTimeRemaining(long id) {
		if (timeDowners.get(id) != null) {
			timeDowners.get(id).cancel();
			if (iTimeRemainListener != null) {
				iTimeRemainListener.timeFinish();
			}
		}
	}

	public interface ITimeRemainListener {
		public void timeFinish();
	}

	private ITimeRemainListener iTimeRemainListener;

	public TimeRemainTextView(Context context) {
		super(context);
	}

	public TimeRemainTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		// Log.w(TAG, "onAttachedToWindow");
		isAttachedWindow = true;
		beginCountDown();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		isAttachedWindow = false;
		// Log.w(TAG, "onDetachedFromWindow");
		if (timeDowners.get(currentId) != null) {
			timeDowners.get(currentId).cancel();
			timeDowners.remove(currentId);
		}
	}

	private boolean isStart=false;
	public void start(long id, long endTime,BaseActivity activity2,String utel) {
		if (!isStart) {
			isStart=true;
			this.currentId = id;
			this.endTime = endTime;
			beginCountDown();
//			RequestParams params=new RequestParams();
//			params.addBodyParameter("u_tel", utel);
//			activity2.PostByCookie(Config1.SENDMESS, params,new Handler());
		}
	}

	private void beginCountDown() {
		if (currentId == 0 || endTime == 0) {
			return;
		}
		if (!isAttachedWindow) {
			return;
		}
		long timeRemain = 60000; 
		CountDownTimer countDownTimer = new CountDownTimer(timeRemain, COUNT_DOWN_INVERTAL) {
			@Override
			public void onTick(long millisUntilFinished) {
				setText("(" + TimeUtils.parseTime(millisUntilFinished) + ")重新获取");
			}

			@Override
			public void onFinish() {
				setText("重新获取");
				isStart=false;
				if (iTimeRemainListener != null) {
					iTimeRemainListener.timeFinish();
				}
			}
		};
		countDownTimer.start();
		timeDowners.put(currentId, countDownTimer);
	}

	public void setiTimeRemainListener(ITimeRemainListener iTimeRemainListener) {
		this.iTimeRemainListener = iTimeRemainListener;
	}

}
