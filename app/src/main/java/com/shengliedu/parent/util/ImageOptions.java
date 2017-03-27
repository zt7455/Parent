package com.shengliedu.parent.util;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.shengliedu.parent.R;

public class ImageOptions {
    public static String IMAGE_CACHE_DIR = "images";
    public static String IMAGE_CAPTRUE_DIR = "capimg";

    public static DisplayImageOptions groupImagesDisplayOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(getDefaulrRes())
                .showImageForEmptyUri(getDefaulrRes())
                .showImageOnFail(getDefaulrRes())
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .build();
    }

    public static DisplayImageOptions groupImagesDisplayOptionsForExpert() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(getDefaulrRes())
                .showImageForEmptyUri(getDefaulrRes())
                .showImageOnFail(getDefaulrRes())
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new FadeInBitmapDisplayer(800))
                .build();
    }

    public static DisplayImageOptions picImagesDisplayOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(getDefaulrRes())
                .showImageForEmptyUri(getDefaulrRes())
                .showImageOnFail(getDefaulrRes())
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }


    private static int getDefaulrRes() {
        return R.mipmap.ic_launcher;
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public static File getExternalCacheDir(final Context context) {
        return HttpCacheUtils.getDiskCacheDir(context, IMAGE_CACHE_DIR);
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

}
