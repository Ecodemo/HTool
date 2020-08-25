package com.ecodemo.magic.box.app.widget;
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
import com.ecodemo.magic.box.R;
import android.widget.Button;
import android.util.TypedValue;
import com.ecodemo.magic.box.app.utils.DisplayUtil;

public class MxButton extends Button
{
	private Paint paint;
	private int p;//线条画笔宽度
	public MxButton(Context context)
	{
		this(context, null);
	}

	public MxButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setWillNotDraw(false);//允许绘制
		paint = new Paint();//创建画笔
		p = DisplayUtil.dip2px(context, 1);
		paint.setStrokeWidth(p);//画笔宽度
		paint.setAntiAlias(true);//抗锯齿
		TypedValue typedValue = new TypedValue();
		context.getTheme().resolveAttribute(R.attr.FrameColor, typedValue, true);
		paint.setColor(context.getResources().getColor(typedValue.resourceId));//画笔颜色
		context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
		setBackgroundResource(typedValue.resourceId);
		context.getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true);
		setTextColor(context.getResources().getColor(typedValue.resourceId));
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawLine(p, 0 , getWidth() - p, 0, paint);//绘制文字左边线
		canvas.drawLine(p, 0, p, getHeight() - p, paint);//绘制左边线
		canvas.drawLine(getWidth() - p, 0, getWidth() - p, getHeight() - p, paint);//绘制右边线
		canvas.drawLine(p, getHeight() - p, getWidth() - p, getHeight() - p, paint);//绘制连接底线
	}
}
