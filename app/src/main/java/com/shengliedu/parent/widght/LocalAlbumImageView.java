package com.shengliedu.parent.widght;


import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shengliedu.parent.util.ImageUtils;

import java.util.HashMap;

/**
 * Created by zhaoyu on 2014/7/5.
 * the only reason do this is just let it delay loading. only only when attached to the window!!
 * and replace the fucking code before.
 */
public class LocalAlbumImageView extends ImageView {

    private String localImageUri;
    private ImageLoader imageLoader;
    private DisplayImageOptions bigPicOptions;
    private boolean isAttachedWindow;
    public static HashMap<String, ImageAware> tasks = new HashMap<String, ImageAware>();

    private ImageAware imageAware;

    public LocalAlbumImageView(Context context) {
        super(context);
        imageLoader = ImageLoader.getInstance();
        bigPicOptions = getBigPicOptions_forPhotoViewer();
        imageAware = new ImageViewAware(this, false);
    }

    public LocalAlbumImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        imageLoader = ImageLoader.getInstance();
        bigPicOptions = getBigPicOptions_forPhotoViewer();
        imageAware = new ImageViewAware(this, false);
    }

    public LocalAlbumImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        imageLoader = ImageLoader.getInstance();
        bigPicOptions = getBigPicOptions_forPhotoViewer();
        imageAware = new ImageViewAware(this, false);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedWindow = true;
        beginLoadImage();
    }

    private void beginLoadImage() {
        if (TextUtils.isEmpty(localImageUri)) return;
        tasks.put(localImageUri, imageAware);
        // TODO should put it in the thread
        if (ImageUtils.isThisBitmapTooLargeToRead(localImageUri.replace("file:///", ""))) {
            setScaleType(ScaleType.MATRIX);
        } else {
            setScaleType(ScaleType.CENTER_CROP);
        }
        if (isAttachedWindow) {
            imageLoader.displayImage(localImageUri, imageAware, bigPicOptions, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    // 如果图片宽度较小  把图片置于中间
                    if (bitmap.getWidth() < getWidth()) {
                        int padding = (getWidth() - bitmap.getWidth()) / 2;
                        setPadding(padding, 0, padding, 0);
                    } else {
                        setPadding(0, 0, 0, 0);
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ImageAware aware = tasks.get(localImageUri);
        if (aware != null) {
            imageLoader.cancelDisplayTask(aware);
        }
    }

    public void setLocalImageUri(String localImageUri) {
        this.localImageUri = localImageUri;
        beginLoadImage();
    }

    public static DisplayImageOptions getBigPicOptions_forPhotoViewer() {
        return new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }
}
