package com.ecodemo.magic.box.app.widget;
import android.content.Context;
import android.graphics.Paint;
import android.icu.math.BigDecimal;
import android.util.AttributeSet;
import android.widget.TextView;
import com.ecodemo.magic.box.R;

public class MxTextView extends TextView
{
	public MxTextView(Context context)
	{
		this(context, null);
	}

	public MxTextView(Context context, AttributeSet attr)
	{
		super(context, attr);
		setBackgroundResource(R.drawable.frame);
	}
}
