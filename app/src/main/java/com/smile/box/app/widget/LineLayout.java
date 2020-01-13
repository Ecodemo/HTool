package com.smile.box.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.TextPaint;
import android.graphics.Paint.FontMetrics;
import android.content.res.TypedArray;
import com.smile.box.R;

public class LineLayout extends LinearLayout
{
	private Paint paint;
	private int leftLine = 30;//文字左边的线长度
	private int top = 38;//顶部距离
	private int left = 10;
	private int right = 10;
	private int bottom = 10;
	private float p = 1.8f;//线条画笔宽度
	private CharSequence cs;
	private TextPaint tp;
	public LineLayout(Context context)
	{
		this(context, null);
	}

	public LineLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineLayout);
        String text = typedArray.getString(R.styleable.LineLayout_android_text);
		cs = text == null ? new String(): text;
		int color = typedArray.getColor(R.styleable.LineLayout_android_color, Color.BLACK);
        typedArray.recycle();
		setWillNotDraw(false);//允许绘制
		setPadding(left, top, right,bottom);//设置边距
		paint = new Paint();//创建画笔
		paint.setStrokeWidth(p);//画笔宽度
		paint.setAntiAlias(true);//抗锯齿
		paint.setColor(color);//画笔颜色
		tp = new TextPaint();
		tp.setColor(color);
		tp.setTextSize(30.0f);
		tp.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawLine(p, top / 2 , leftLine, top / 2, paint);//绘制文字左边线
		canvas.drawText(cs, 0, cs.length(), leftLine + 8, top - top / 3, tp);//绘制文字
		float textpx = tp.measureText(cs, 0, cs.length());//获取文字宽度
		canvas.drawLine(((leftLine  + textpx) + 2*8), top / 2, getWidth() - p, top / 2, paint);//绘制文字右边线
		canvas.drawLine(p, top / 2, p, getHeight() - p, paint);//绘制左边线
		canvas.drawLine(getWidth() - p, top / 2, getWidth() - p, getHeight() - p, paint);//绘制右边线
		canvas.drawLine(p, getHeight() - p, getWidth() - p, getHeight() - p, paint);//绘制连接底线
	}


	public void setTitle(CharSequence cs)
	{
		this.cs = cs;
	}

	public CharSequence getTitle()
	{
		return cs;
	}
}
