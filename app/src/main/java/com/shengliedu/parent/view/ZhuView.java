package com.shengliedu.parent.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.shengliedu.parent.R;
import com.shengliedu.parent.util.Config1;

public class ZhuView extends View {
	ArrayList<Integer> temp;
	ArrayList<Integer> temps;
	ArrayList<Integer> temp3;
	ArrayList<String> vedioUrl;
	ArrayList<String> dayTime;
	private Context context;
	private Canvas canvass;
	private Paint paint;
	private Paint paints;

	public ZhuView(Context context, ArrayList<Integer> temp,
			ArrayList<Integer> temps, ArrayList<Integer> temp3,
			ArrayList<String> dayTime, ArrayList<String> vedioUrl) {
		super(context);
		this.temp = temp;
		this.temps = temps;
		this.temp3 = temp3;
		this.dayTime = dayTime;
		this.vedioUrl = vedioUrl;
		this.context = context;
	}

	public ZhuView(Context context) {
		super(context);
	}

	public ZhuView(Context context, AttributeSet attrs) {
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
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(2);
			paint.setTextSize(20);
			// 横坐标
			for (int i = 0; i < dayTime.size(); i++) {
				TextPaint textPaint = new TextPaint();
				textPaint.setARGB(0xFF, 0xFF, 0, 0);
				textPaint.setTextSize(30);
				textPaint.setAntiAlias(true);
				StaticLayout layout = new StaticLayout(dayTime.get(i),
						textPaint, 200, Alignment.ALIGN_NORMAL, 1.0F, 0.0F,
						true);
				canvas.translate(i * 200 + 20, 480);
				layout.draw(canvas);
				canvas.translate(-i * 200 - 20, -480);
			}
			// 折线
			paint.reset();
			paint.setColor(Color.BLUE);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(2);
			paints.reset();
			paints.setStyle(Paint.Style.STROKE);
			paints.setTextSize(20);
			paints.setStrokeWidth(30);
			Log.v("TAG", "s" + dayTime.size());
			for (int i = 0; i < dayTime.size(); i++) {
				paints.setStrokeWidth(30);
				paints.setColor(context.getResources().getColor(R.color.zhu1));
				canvas.drawLine(i * 200 + 35, 480, i * 200 + 35,
						480 - temp.get(i) * 4, paints);
				paints.setStrokeWidth(2);
				canvas.drawText(temp.get(i) + "%", i * 200 + 20,
						460 - temp.get(i) * 4, paints);
				paints.setStrokeWidth(30);
				paints.setColor(context.getResources().getColor(R.color.zhu2));
				canvas.drawLine(i * 200 + 75, 480, i * 200 + 75,
						480 - temps.get(i) * 4, paints);
				paints.setStrokeWidth(2);
				canvas.drawText(temps.get(i) + "%", i * 200 + 60,
						460 - temps.get(i) * 4, paints);
				paints.setStrokeWidth(30);
				paints.setColor(context.getResources().getColor(R.color.zhu3));
				canvas.drawLine(i * 200 + 115, 480, i * 200 + 115,
						480 - temp3.get(i) * 4, paints);
				paints.setStrokeWidth(2);
				canvas.drawText(temp3.get(i) + "%", i * 200 + 100,
						460 - temp3.get(i) * 4, paints);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		if (dayTime != null && dayTime.size() > 0) {

			for (int i = 0; i < dayTime.size(); i++) {
				float tempx = i * 200 + 20;
				float tempy = 480;
				if (x >= (tempx - 10) && x <= (tempx + 200) && y <= tempy + 80
						&& y >= tempy - 10) {
					if (!TextUtils.isEmpty(vedioUrl.get(i))) {
						try {
							String extension = MimeTypeMap
									.getFileExtensionFromUrl(Config1
											.getInstance().IP + vedioUrl.get(i));
							String mimeType = MimeTypeMap.getSingleton()
									.getMimeTypeFromExtension(extension);
							Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
							mediaIntent.setDataAndType(
									Uri.parse(Config1.getInstance().IP
											+ vedioUrl.get(i)), mimeType);
							context.startActivity(mediaIntent);
						} catch (ActivityNotFoundException e) {
							// TODO: handle exception
							Toast.makeText(context, "sorry附件不能打开，请下载相关软件！", 500)
									.show();
						}
					} else {
						Toast.makeText(context, dayTime.get(i) + "",
								Toast.LENGTH_SHORT).show();
					}
					break;
				}
			}
		}
		return super.onTouchEvent(event);
	}
}
