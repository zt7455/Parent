package com.shengliedu.parent.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.shengliedu.parent.R;

import java.io.File;
import java.text.DecimalFormat;

public class MImageLoader {

	private static ImageLoader imageLoader;
	private static DisplayImageOptions options;
	private static final String path = "zzkt/picture";

	public static void displayImage(Context context, String url,
			ImageView imageView) {
		MImageLoader.getInstance(context).displayImage(url, imageView,
				MImageLoader.getOption());
	}

	public static void displayImage1(Context context, String url,
			ImageView imageView) {
		MImageLoader.getInstance(context).displayImage(url, imageView,
				MImageLoader.getOption1());
	}

	public static ImageLoader getInstance(Context context) {
		if (imageLoader == null) {
			imageLoader = new ImageLoader() {
				@Override
				public void displayImage(String uri, ImageView imageView) {
					// TODO Auto-generated method stub
					super.displayImage(uri, imageView);
				}
			};
			File cacheDir = StorageUtils.getOwnCacheDirectory(context, path);
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context)
					.memoryCacheExtraOptions(480, 800)
					// maxwidth, max
					// height，即保存的每个缓存文件的最大长宽
					.threadPoolSize(3)
					// 线程池内加载的数量
					.threadPriority(Thread.NORM_PRIORITY - 2)
					.denyCacheImageMultipleSizesInMemory()
					.memoryCache(new LruMemoryCache(4 * 1024 * 1024))
					// You can pass your own memory cache
					// implementation/你可以通过自己的内存缓存实现
					.memoryCacheSize(6 * 1024 * 1024)
					.diskCacheSize(500 * 1024 * 1024)
					.diskCacheFileNameGenerator(new Md5FileNameGenerator())

					.tasksProcessingOrder(QueueProcessingType.LIFO)
					.diskCacheFileCount(100)
					// .diskCacheExtraOptions(480, 800, null)
					.diskCache(new UnlimitedDiskCache(cacheDir))
//					.imageDownloader(
//							new BaseImageDownloader(context, 5 * 1000,
//									30 * 1000))
					.imageDownloader(
							new BaseImageDownloader(context, 5 * 1000,
									30 * 1000)) // connectTimeout (5 s),
												// readTimeout (30 s)超时时间
//					.writeDebugLogs() // Remove for releaseapp
					.build();// 开始构建
			imageLoader.init(config);
		}
		return imageLoader;
	}
	
	public static void clear(Context context){
		getInstance(context).clearDiskCache();
		getInstance(context).clearMemoryCache();
	}
	
	public static String getSize(Context context){
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, path);
		File[] ff=cacheDir.listFiles();
		double size=0.0;
		for (int i = 0; i < ff.length; i++) {
			size+=ff[i].length();
			LogUtils.e("size",size+"");
		}
		String dw="B";
		int a=0;
		while(size>1000){
			size/=1024;
			a++;
		}
		switch (a) {
		case 1:
			dw="KB";
			break;
		case 2:
			dw="MB";
			break;
		case 3:
			dw="GB";
			break;

		default:
			break;
		}
		DecimalFormat df = new DecimalFormat("0.0");
		return df.format(size)+dw;
	}

	public static DisplayImageOptions getOption() {
		if (options == null) {
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.mipmap.index_item_back) // resource
																	// or
					// drawable
					.showImageForEmptyUri(R.mipmap.ic_launcher) // resource or
					// drawable
					.showImageOnFail(R.mipmap.ic_launcher) // resource or
					// drawable
					.resetViewBeforeLoading(false) // default
					.delayBeforeLoading(0).cacheOnDisk(true) // default
					.cacheInMemory(true).considerExifParams(false) // default
					.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
					.bitmapConfig(Bitmap.Config.ARGB_8888) // default
					.displayer(new SimpleBitmapDisplayer()) // default
					.handler(new Handler()) // default
					.build();
		}
		return options;
	}

	public static DisplayImageOptions getOption1() {
		if (options == null) {
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.mipmap.index_item_back) // resource
																	// or
					// drawable
					.showImageForEmptyUri(R.mipmap.ic_launcher) // resource or
					// drawable
					.showImageOnFail(R.mipmap.ic_launcher) // resource or
					// drawable
					.resetViewBeforeLoading(false) // default
					.delayBeforeLoading(0).cacheOnDisk(true) // default
					.cacheInMemory(true).considerExifParams(false) // default
					.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
					.bitmapConfig(Bitmap.Config.ARGB_8888) // default
					.displayer(new SimpleBitmapDisplayer()) // default
					.handler(new Handler()) // default
					.build();
		}
		return options;
	}
}
