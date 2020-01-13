package com.smile.box.app.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class VSeekBar extends SeekBar
{
	public VSeekBar(Context context)
	{
		super(context);
	}

	public VSeekBar(Context context, AttributeSet attributeSet, int i)
	{
		super(context, attributeSet, i);
	}

	public VSeekBar(Context context, AttributeSet attributeSet)
	{
		super(context, attributeSet);
	}

	protected void onSizeChanged(int i, int i2, int i3, int i4)
	{
		super.onSizeChanged(i2, i, i4, i3);
	}

	@Override
	protected synchronized void onMeasure(int i, int i2)
	{
		super.onMeasure(i2, i);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	}

	protected void onDraw(Canvas canvas)
	{
		canvas.rotate((float) -90);
		canvas.translate((float) (-getHeight()), (float) 0);
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent motionEvent)
	{
		if (!isEnabled())
		{
			return false;
		}
		switch (motionEvent.getAction())
		{
			case 2:
				setProgress(getMax() - ((int) ((((float) getMax()) * motionEvent.getY()) / ((float) getHeight()))));
				onSizeChanged(getWidth(), getHeight(), 0, 0);
				break;
		}
		return true;
	}
}
