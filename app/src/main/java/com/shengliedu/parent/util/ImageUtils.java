package com.shengliedu.parent.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.File;

/**
 * Created by zhaoyu on 2014/11/28.
 */
public class ImageUtils {

    public static boolean isThisBitmapTooLargeToRead(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;
        if (width == -1 || height == -1) {
            return false;
        }
        if (width > 2048 || height > 2048) {
            // 是否为长图或宽图
            if (width / height > 3 || height / width > 3) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 合并两张bitmap为一张
     *
     * @param background
     * @param foreground
     * @return Bitmap
     */
    public static Bitmap combineBitmap(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();
        Bitmap newmap = Bitmap.createBitmap(bgWidth +10+ fgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(foreground, bgWidth+10, 0, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newmap;
    }

}
