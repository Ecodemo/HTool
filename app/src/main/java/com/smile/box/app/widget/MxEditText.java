package com.smile.box.app.widget;
import android.widget.EditText;
import android.content.Context;
import android.util.AttributeSet;
import com.smile.box.R;

public class MxEditText extends EditText
{
	public MxEditText(Context context)
	{
		this(context, null);
	}

	public MxEditText(Context context, AttributeSet attr)
	{
		super(context, attr);
		setBackgroundResource(R.drawable.frame);
	}
}
