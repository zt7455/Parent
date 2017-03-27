package com.shengliedu.parent.synclass;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.widget.MediaController;
import android.widget.VideoView;
import com.shengliedu.parent.BaseActivity;
import com.shengliedu.parent.R;
import com.shengliedu.parent.util.Config1;

public class MediaPlayerActivity extends BaseActivity implements OnCompletionListener, OnErrorListener{
	private String plan_info;
	private String link;
	private VideoView mVideoView;
	private MediaController mMediaCtrl;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		link = (String) getIntent().getExtras().get("html");
		plan_info = (String) getIntent().getExtras().get("plan_info");
		setBack();
		showTitle(plan_info);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			hideTitle();
		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			showTitle(plan_info);
		}
		mVideoView = (VideoView) findViewById(R.id.videoView);
		mVideoView.setOnErrorListener(this);
		mVideoView.setOnCompletionListener(this);
		mMediaCtrl = new MediaController(this);
		// mMediaCtrl.setAnchorView(mVideoView);
		mVideoView.setMediaController(mMediaCtrl);
		// mVideoView.getHolder().setKeepScreenOn(true);
		mVideoView.getHolder().addCallback(new SurfaceCallback());
	}

	private final class SurfaceCallback implements Callback {
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			getDatas();
		}

		public void surfaceCreated(SurfaceHolder holder) {
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			onStop();
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		getWindow().getDecorView().setKeepScreenOn(true);
		if (null != mVideoView) {
			mVideoView.resume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		getWindow().getDecorView().setKeepScreenOn(false);
		if (null != mVideoView) {
			mVideoView.pause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != mVideoView) {
			mVideoView.stopPlayback();
			mVideoView = null;
		}
	}

	private void play() {
		// TODO Auto-generated method stub
		mVideoView.setVideoURI(Uri.parse(Config1.getInstance().IP+link));
		mVideoView.start();
	}
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Log.v("TAG", "link="+link);
		if (!TextUtils.isEmpty(link)) {
			play();
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_mediaplayer;
	}

	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

}
