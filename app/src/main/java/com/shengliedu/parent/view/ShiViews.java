package com.shengliedu.parent.view;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings.TextSize;
public class ShiViews extends View {
	private ArrayList<String> strs=new ArrayList<String>();
	public ShiViews(Context context) {
		super(context);
	}
	
	public ShiViews(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint paint = new Paint(); 
		/* 去锯齿 */ 
		paint.setAntiAlias(true); 
		/* 设置paint的颜色 */ 
		paint.setColor(Color.DKGRAY);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);  
		//划线
		canvas.drawLine(60.0f, 20.0f, 60.0f, 560.0f, paint);
		PathEffect effects = new DashPathEffect(new float[] { 1, 2, 4, 8}, 1);  
		paint.setPathEffect(effects);
		paint.setAntiAlias(true);
		canvas.drawLine(60.0f, 40.0f, 1400.0f*72, 40.f, paint);
		canvas.drawLine(60.0f, 80.0f, 1400.0f*72, 80.0f, paint);
		canvas.drawLine(60.0f, 120.0f, 1400.0f*72, 120.0f, paint);
		canvas.drawLine(60.0f, 160.0f, 1400.0f*72, 160.f, paint);
		canvas.drawLine(60.0f, 200.0f, 1400.0f*72, 200.0f, paint);
		canvas.drawLine(60.0f, 240.0f, 1400.0f*72, 240.0f, paint);
		canvas.drawLine(60.0f, 280.0f, 1400.0f*72, 280.0f, paint);
		canvas.drawLine(60.0f, 320.0f, 1400.0f*72, 320.0f, paint);
		canvas.drawLine(60.0f, 360.0f, 1400.0f*72,360.0f, paint);
		canvas.drawLine(60.0f, 400.0f, 1400.0f*72,400.0f, paint);
		canvas.drawLine(60.0f, 440.0f, 1400.0f*72,440.0f, paint);
		canvas.drawLine(60.0f, 480.0f, 1400.0f*72,480.0f, paint);
		canvas.drawLine(60.0f, 520.0f, 1400.0f*72,520.0f, paint);
		paint.reset();
		paint.setColor(Color.DKGRAY);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		canvas.drawLine(60.0f, 560.0f, 1400.0f*72,560.0f, paint);
		//纵坐标
		paint.reset();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		paint.setTextSize(24);
		if (strs.size()>0) {
			canvas.drawText(formatNumber(strs.get(6)), 10, 80, paint); 
			canvas.drawText(formatNumber(strs.get(5)), 10, 160, paint); 
			canvas.drawText(formatNumber(strs.get(4)), 10, 240, paint); 
			canvas.drawText(formatNumber(strs.get(3)), 10, 320, paint); 
			canvas.drawText(formatNumber(strs.get(2)), 10, 400, paint); 
			canvas.drawText(formatNumber(strs.get(1)), 10, 480, paint); 
			canvas.drawText(formatNumber(strs.get(0)), 10, 560, paint); 
		}
	}
	public void setLeftData(ArrayList<String> strs) {
		// TODO Auto-generated method stub
		this.strs=strs;
		invalidate();
	}
	private String formatNumber(String numString) {
		// TODO Auto-generated method stub
		if (numString.length()==1) {
			return "    "+numString;
		}else if (numString.length()==2) {
			return "  "+numString;
		}
		return numString;
	}
}
