package com.shengliedu.parent.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.BeanPoint;

public class LDView extends View {
	String[] data;
	double[] ss;
	double[] s2;
	double[] s3;
	List<BeanPoint> points = new ArrayList<BeanPoint>();
	// 定义自己指定的宽和高
	int myWidth = 0;
	int myHeight = 0;

	private Paint p;
	private Context context;

	// 该View仅仅是用于代码创建时，可只添加该构造方法
	public LDView(Context context) {
		super(context);
		initPaint();
	}

	// 添加该构造方法的目的是：让View可通过布局文件的方式显示
	public LDView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initPaint();
	}

	// 初始化画笔
	public void initPaint() {
		p = new Paint();
		// 调用相关的方法设定画笔的样式
		p.setColor(Color.GREEN);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(4);
		p.setAntiAlias(true);
	}

	// 定义圆心
	private int centerX = 160;

	private float centerY = 240;

	// 定义半径
	private float radius = 100;
	// 定义角度
	private float angle = 0;

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (data != null) {
			angle = 360 / data.length;
			p.setStrokeWidth(4);
			// 把画布的坐标轴的中心一直屏幕中间
			canvas.translate(getWidth() / 2, getHeight() / 2);
			radius = getWidth() / 2 * 3 / 4;
			for (int i = 0; i < data.length; i++) {
				BeanPoint point = new BeanPoint();
				point.x = (float) (radius * Math.cos(Math.toRadians(angle * i)));
				point.y = (float) (radius * Math.sin(Math.toRadians(angle * i)));
				point.text = data[i];
				points.add(point);
			}
			for (int i = 0; i < points.size(); i++) {
				p.setStrokeWidth(2);
				p.setTextSize(20);
				p.setColor(Color.DKGRAY);
				canvas.drawLine(0, 0, points.get(i).x, points.get(i).y, p);
				if (i < (points.size() - 1)) {
					p.setColor(Color.DKGRAY);
					p.setStrokeWidth(4);
					canvas.drawLine(points.get(i).x, points.get(i).y,
							points.get(i + 1).x, points.get(i + 1).y, p);
				} else {
					p.setColor(Color.DKGRAY);
					p.setStrokeWidth(4);
					canvas.drawLine(points.get(i).x, points.get(i).y,
							points.get(0).x, points.get(0).y, p);
				}
				float x = points.get(i).x;
				float y = points.get(i).y;
				if (points.get(i).x > 0) {
					x = x + 10;
				}
				if (points.get(i).y > 0) {
					y = y + p.getTextSize();
				}
				if (points.get(i).x < 0) {
					x = x - 20 - p.getTextSize();
				}
				if (points.get(i).y < 0) {
					y = y - p.getTextSize();
				}
				p.setColor(Color.BLACK);
				p.setStrokeWidth(2);
				canvas.drawText(points.get(i).text, x, y, p);
			}
			bx = -radius * 7 / 5;
			by = radius + 70;
			List<BeanPoint> points1 = new ArrayList<BeanPoint>();
			for (int i = 0; i < ss.length; i++) {
				BeanPoint point = new BeanPoint();
				point.x = (float) (ss[i] / 100.0 * radius * Math.cos(Math
						.toRadians(angle * i)));
				point.y = (float) (ss[i] / 100.0 * radius * Math.sin(Math
						.toRadians(angle * i)));
				point.text = ss[i] + "";
				points1.add(point);
			}
			paintView(points1, canvas,
					context.getResources().getColor(R.color.score3));
			List<BeanPoint> points2 = new ArrayList<BeanPoint>();
			for (int i = 0; i < s2.length; i++) {
				BeanPoint point = new BeanPoint();
				point.x = (float) (s2[i] / 100.0 * radius * Math.cos(Math
						.toRadians(angle * i)));
				point.y = (float) (s2[i] / 100.0 * radius * Math.sin(Math
						.toRadians(angle * i)));
				point.text = s2[i] + "";
				points2.add(point);
			}
			paintView(points2, canvas,
					context.getResources().getColor(R.color.score2));
			List<BeanPoint> points3 = new ArrayList<BeanPoint>();
			for (int i = 0; i < s3.length; i++) {
				BeanPoint point = new BeanPoint();
				point.x = (float) (s3[i] / 100.0 * radius * Math.cos(Math
						.toRadians(angle * i)));
				point.y = (float) (s3[i] / 100.0 * radius * Math.sin(Math
						.toRadians(angle * i)));
				point.text = s3[i] + "";
				points3.add(point);
			}
			paintView(points3, canvas,
					context.getResources().getColor(R.color.score1));
		}
	}

	float bx = 0;
	float by = 0;

	boolean shuiping1=false;
	boolean shuiping2=false;
	boolean shuzhi1=false;
	boolean shuzhi2=false;
	int location1=1;
	int location2=1;
	int location3=1;
	int location4=1;
	private void paintView(List<BeanPoint> points, Canvas canvas, int color) {
		// TODO Auto-generated method stub
		for (int i = 0; i < points.size(); i++) {
			p.setStrokeWidth(1);
			p.setTextSize(14);
			// p.setColor(Color.DKGRAY);
			// canvas.drawLine(0, 0, points.get(i).x, points.get(i).y, p);
			Log.v("TAG", "points.size=" + points.size());
			if (i < (points.size() - 1)) {
				p.setColor(color);
				p.setStrokeWidth(1);
				Log.v("TAG", "x1=" + points.get(i).x + "y1=" + points.get(i).y);
				canvas.drawLine(points.get(i).x, points.get(i).y,
						points.get(i + 1).x, points.get(i + 1).y, p);
			} else {
				p.setColor(color);
				p.setStrokeWidth(1);
				Log.v("TAG", "x2=" + points.get(i).x + "y2=" + points.get(i).y);
				canvas.drawLine(points.get(i).x, points.get(i).y,
						points.get(0).x, points.get(0).y, p);
			}
			float x = points.get(i).x;
			float y = points.get(i).y;
			if (angle*i%360>45 && angle*i%360<=135) {
//				if (shuiping1) {
//					shuiping1=false;
//				}else {
//					shuiping1=true;
//					x=x-(20*i);
//				}
				if (location1 % 4==1) {
					x=x+(10*location1);
				}else if (location1 % 4==2) {
					y=y+(10*location1);
				}else if (location1 % 4==3) {
					x=x-(10*location1);
				}else if (location1 % 4==0) {
					y=y-(10*location1);
				}
				location1 ++;
			}else if (angle*i%360>135 && angle*i%360<=225) {
//				if (shuzhi1) {
//					shuzhi1=false;
//					y=y+(38*i);
//				}else {
//					shuzhi1=true;
//					y=y-(38*i);
//				}
				if (location2 % 4==2) {
					x=x+(10*location2);
				}else if (location2 % 4==3) {
					y=y+(10*location2);
				}else if (location2 % 4==0) {
					x=x-(10*location2);
				}else if (location2 % 4==1) {
					y=y-(10*location2);
				}
				location2 ++;
			}else if (angle*i%360>225 && angle*i%360<=315) {
//				if (shuiping2) {
//					shuiping2=false;
//					x=x+(40*i);
//				}else {
//					shuiping2=true;
//					x=x-(40*i);
//				}
				if (location3 % 4==3) {
					x=x+(10*location3);
				}else if (location3 % 4==0) {
					y=y+(10*location3);
				}else if (location3 % 4==1) {
					x=x-(10*location3);
				}else if (location3 % 4==2) {
					y=y-(10*location3);
				}
				location3 ++;
			}else {
				if (location4 % 4==0) {
					x=x+(10*location4);
				}else if (location4 % 4==1) {
					y=y+(10*location4);
				}else if (location4 % 4==2) {
					x=x-(10*location4);
				}else if (location4 % 4==3) {
					y=y-(10*location4);
				}
				location4 ++;
			}
//			if (points.get(i).x > 0) {
//				x = x + 5;
//			}
//			if (points.get(i).y > 0) {
//				y = y + 5;
//			}
//			if (points.get(i).x < 0) {
//				x = x - 5 - p.getTextSize();
//			}
//			if (points.get(i).y < 0) {
//				y = y - 5;
//			}
			p.setColor(color);
			p.setStrokeWidth(1);
			canvas.drawText(points.get(i).text, x, y, p);
			p.setStrokeWidth(5);
		}
		// canvas.drawLine(bx, by, bx+radius*2/5, by, p);
		// p.setStrokeWidth(1);
		// canvas.drawText("平均分"+1, bx+radius*2/5+10, by, p);
		// bx=bx+radius*2/5+10+("平均分"+1).length()*p.getTextSize()+10;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 通过系统传入关于View的宽和高的说明，获取一些信息

		// 获取系统得到的View的宽高说明中的模式（View是wrap_content还是match_parent）
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		// 获取系统得到的View的宽高说明中的尺寸
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		// Log.v("TAG", "系统获取的View的宽和高 ：" + "  width = " + width + "  height = "
		// + height);

		// 判定模式Mode，获取当前View的宽和高的类型（View是wrap_content还是match_parent）

		// 判定高的模式
		switch (heightMode) {
		case MeasureSpec.AT_MOST:// 对应控件是wrap_content的时候
			Log.v("TAG", "AT_MOST");
			myHeight = height / 2;
			break;
		case MeasureSpec.EXACTLY:// 对应控件是match_parent或者具体数字的时候
			Log.v("TAG", "EXACTLY");
			myHeight = height;
			break;
		case MeasureSpec.UNSPECIFIED:
			Log.v("TAG", "UNSPECIFIED");
			break;

		default:
			break;
		}

		// 判定宽的模式
		switch (widthMode) {
		case MeasureSpec.AT_MOST:// 对应控件是wrap_content的时候
			Log.v("TAG", "AT_MOST");
			myWidth = width / 2;
			break;
		case MeasureSpec.EXACTLY:// 对应控件是match_parent或者具体数字的时候
			Log.v("TAG", "EXACTLY");
			myWidth = width;
			break;
		case MeasureSpec.UNSPECIFIED:
			Log.v("TAG", "UNSPECIFIED");
			break;

		default:
			break;
		}

		// 让自己指定的宽和高生效(把自己计算的宽和高设定给系统)
		setMeasuredDimension(myWidth, myHeight);
	}

	public void setAllDate(String[] data, double[] ss, double[] s2, double[] s3) {
		// TODO Auto-generated method stub
		this.data = data;
		this.ss = ss;
		this.s2 = s2;
		this.s3 = s3;
		invalidate();
	}
}
