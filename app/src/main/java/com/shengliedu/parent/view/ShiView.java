package com.shengliedu.parent.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.shengliedu.parent.R;

public class ShiView extends View {
	ArrayList<Integer> temp;
	ArrayList<Integer> temps;
	ArrayList<String> dayTime;
	ArrayList<String> strs;
	private Context context;
	private Canvas canvass;
	private Paint paint;
	private Paint paints;

	public ShiView(Context context, ArrayList<Integer> temp,
			ArrayList<Integer> temps, ArrayList<String> dayTime,
			ArrayList<String> strs) {
		super(context);
		this.temp = temp;
		this.strs = strs;
		this.temps = temps;
		this.dayTime = dayTime;
		this.context = context;
	}

	public ShiView(Context context) {
		super(context);
	}

	public ShiView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (dayTime != null && dayTime.size() > 0) {

			canvass = canvas;
			canvas.translate(60, 0);
			paint = new Paint();
			paints = new Paint();
			/* 去锯齿 */
			paint.setAntiAlias(true);
			paints.setAntiAlias(true);

			// 纵坐标
			paint.reset();
			paint.setColor(Color.BLACK);
			paint.setStyle(Style.FILL);
			paint.setStrokeWidth(2);
			paint.setTextSize(20);
			// 横坐标
			for (int i = 0; i < dayTime.size(); i++) {
				TextPaint textPaint = new TextPaint();
				textPaint.setARGB(0xFF, 0xFF, 0, 0);
				textPaint.setTextSize(20);
				textPaint.setAntiAlias(true);
				StaticLayout layout = new StaticLayout(dayTime.get(i),
						textPaint, 180, Alignment.ALIGN_NORMAL, 1.0F, 0.0F,
						true);
				canvas.translate(i * 200 + 45, 560);
				layout.draw(canvas);
				canvas.translate(-i * 200 - 45, -560);
				// canvas.restore();
				// canvas.drawText(dayTime.get(i), i * 200 + 50, 590, paint);
			}
			// 折线
			paint.reset();
			paint.setColor(context.getResources().getColor(R.color.score2));
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(2);
			paints.reset();
			paints.setColor(Color.BLACK);
			paints.setTextSize(25);
			paints.setStyle(Style.STROKE);
			paints.setStrokeWidth(2);
			Path path = new Path();
			Path paths = new Path();
			path.moveTo(
					115,
					560 - temp.get(0)
							* (80 / (float) Integer.parseInt(strs.get(1))));
			paths.moveTo(
					115,
					560 - temps.get(0)
							* (80 / (float) Integer.parseInt(strs.get(1))));
			for (int i = 1; i < dayTime.size(); i++) {
				path.lineTo(i * 200 + 115, 560 - temp.get(i)
						* (80 / (float) Integer.parseInt(strs.get(1))));
				paths.lineTo(i * 200 + 115, 560 - temps.get(i)
						* (80 / (float) Integer.parseInt(strs.get(1))));
			}
			canvas.drawPath(paths, paint);
			paint.setColor(context.getResources().getColor(R.color.score1));
			canvas.drawPath(path, paint);
			paint.reset();
			paint.setColor(Color.WHITE);
			// 设置样式-填充
			paint.setStyle(Style.FILL);
			// 绘图 // 从资源文件中生成位图
			for (int i = 0; i < dayTime.size(); i++) {
				int tempy = (int) temp.get(i);
				int tempsy = (int) temps.get(i);
				if ((tempy - tempsy) < 4 && (tempy - tempsy) > 0) {
					canvas.drawText(
							temp.get(i) + "",
							i * 200 + 105,
							540
									- temp.get(i)
									* (80 / (float) Integer.parseInt(strs
											.get(1))), paints);
					canvas.drawText(
							temps.get(i) + "",
							i * 200 + 105,
							595
									- temps.get(i)
									* (80 / (float) Integer.parseInt(strs
											.get(1))), paints);
				} else if ((tempy - tempsy) > -4 && (tempy - tempsy) < 4
						&& (tempy - tempsy) < 0) {
					canvas.drawText(
							temp.get(i) + "",
							i * 200 + 105,
							595
									- temp.get(i)
									* (80 / (float) Integer.parseInt(strs
											.get(1))), paints);
					canvas.drawText(
							temps.get(i) + "",
							i * 200 + 105,
							540
									- temps.get(i)
									* (80 / (float) Integer.parseInt(strs
											.get(1))), paints);
				} else {
					canvas.drawText(
							temp.get(i) + "",
							i * 200 + 105,
							540
									- temp.get(i)
									* (80 / (float) Integer.parseInt(strs
											.get(1))), paints);
					canvas.drawText(
							temps.get(i) + "",
							i * 200 + 105,
							540
									- temps.get(i)
									* (80 / (float) Integer.parseInt(strs
											.get(1))), paints);
				}
				Bitmap bmp = BitmapFactory.decodeResource(
						context.getResources(), R.mipmap.class_pm);
				Matrix matrix = new Matrix();
				matrix.postScale(0.5f, 0.5f);
				Bitmap dstbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
						bmp.getHeight(), matrix, true);
				canvas.drawBitmap(
						dstbmp,
						i * 200 + 115 - dstbmp.getWidth() / 2,
						560 - temp.get(i)
								* (80 / (float) Integer.parseInt(strs.get(1)))
								- dstbmp.getHeight() / 2, null);
				Bitmap bmp1 = BitmapFactory.decodeResource(
						context.getResources(), R.mipmap.grade_pm);
				Matrix matrix1 = new Matrix();
				matrix1.postScale(0.5f, 0.5f);
				Bitmap dstbmp1 = Bitmap.createBitmap(bmp1, 0, 0,
						bmp1.getWidth(), bmp1.getHeight(), matrix1, true);
				canvas.drawBitmap(
						dstbmp1,
						i * 200 + 115 - dstbmp1.getWidth() / 2,
						560 - temps.get(i)
								* (80 / (float) Integer.parseInt(strs.get(1)))
								- dstbmp1.getHeight() / 2, null);
			}
		}else {
			Toast.makeText(context, "暂无数据",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
}
