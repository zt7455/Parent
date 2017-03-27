package com.shengliedu.parent.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class CacheImageAsyncTask extends AsyncTask<String, Integer, Bitmap> {
	private ImageView ima;
	private ImageFileCache fileCache;
	private ImageMemoryCache memoryCache;

	public CacheImageAsyncTask(ImageView ima, Context context) {
		this.ima = ima;
		fileCache = new ImageFileCache();
		memoryCache = new ImageMemoryCache(context);
	}

	public Bitmap getBitmap(String url) {
		Bitmap result = memoryCache.getBitmapFromCache(url);
		if (result == null) {
			result = fileCache.getImage(url);
			if (result == null) {
				result = ImageGetFromHttp.downloadBitmap(url);
				if (result != null) {
					fileCache.saveBitmap(result, url);
					memoryCache.addBitmapToCache(url, result);
				}
			} else {
				memoryCache.addBitmapToCache(url, result);
			}
		}
		return result;
	}

	protected Bitmap doInBackground(String... params) {

		return getBitmap(params[0]);
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		ima.setImageBitmap(result);
	}
}