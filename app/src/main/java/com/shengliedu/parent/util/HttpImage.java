package com.shengliedu.parent.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

/**
 * 图片下载 zhangtao
 * 
 * @date 2014-10-15
 */
public class HttpImage {
	public static File compressImage(String imagepath) {
		Bitmap image=BitmapFactory.decodeFile(imagepath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
		int options = 100;
		while (baos.toByteArray().length / 1024 > 500) { // 100k
			baos.reset();
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
			options -= 25;
		}
		float f = (baos.toByteArray().length / 1024);
		LogUtils.i("TAG", "current img size:" + f);
		return getFileFromBytes(baos.toByteArray(),imagepath);
		// ByteArrayInputStream isBm = new ByteArrayInputStream();
		// Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		// saveMyBitmap(srcPath.getName(), bitmap);
	}
	public static File getFileFromBytes(byte[] b, String outputFile) {
	      BufferedOutputStream stream = null;
	       File file = null;
	       try {
	      file = new File(outputFile);
	           FileOutputStream fstream = new FileOutputStream(file);
	           stream = new BufferedOutputStream(fstream);
	           stream.write(b);
	       } catch (Exception e) {
	           e.printStackTrace();
	      } finally {
	          if (stream != null) {
	               try {
	                  stream.close();
	               } catch (IOException e1) {
	                  e1.printStackTrace();
	              }
	          }
	      }
	       return file;
	   }
	public static boolean isImage2(File file)  
	{  
		 Drawable drawable = Drawable.createFromPath(file.getAbsolutePath());
		 if(drawable == null){
		   return false;
		 }
		return true;  
	}  
	
	public static boolean isImage3(File file) {
		if (file == null) {
			return false;
		}
		String drawable = file.getAbsolutePath();
		if (drawable == null) {
			return false;
		}
		if (drawable.endsWith("PNG") || drawable.endsWith("png")
				|| drawable.endsWith("JPEG")
				|| drawable.endsWith("jpeg")) {
			return true;
		}
		return false;
	}
	public static void compressPicture(String srcPath, String desPath) {
		FileOutputStream fos = null;
		BitmapFactory.Options op = new BitmapFactory.Options();

		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		op.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, op);
		op.inJustDecodeBounds = false;

		// 缩放图片的尺寸
		float w = op.outWidth;
		float h = op.outHeight;
		float hh = 1024f;//
		float ww = 1024f;//
		// 最长宽度或高度1024
		float be = 1.0f;
		if (w > h && w > ww) {
			be = (float) (w / ww);
		} else if (w < h && h > hh) {
			be = (float) (h / hh);
		}
		if (be <= 0) {
			be = 1.0f;
		}
		op.inSampleSize = (int) be;// 设置缩放比例,这个数字越大,图片大小越小.
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, op);
		int desWidth = (int) (w / be);
		int desHeight = (int) (h / be);
		bitmap = Bitmap.createScaledBitmap(bitmap, desWidth, desHeight, true);
		try {
			fos = new FileOutputStream(desPath);
			if (bitmap != null) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
