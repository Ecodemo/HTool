package com.smile.box.app.widget;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import com.smile.box.BrowserActivity;
import com.smile.box.app.utils.IToast;

public class LinkTextView extends TextView
{
	public LinkTextView(Context context)
	{
		this(context, null);
	}

	public LinkTextView(Context context, AttributeSet attr)
	{
		super(context, attr);
	}

	public void interceptHyperLink()
	{
		setMovementMethod(LinkMovementMethod.getInstance());
		setHighlightColor(Color.TRANSPARENT);
		CharSequence text = getText();
        if (text instanceof Spannable)
		{
            int end = text.length();
            Spannable sp = (Spannable) text;
            URLSpan urls[] = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();
            for (URLSpan span : urls)
			{
                CustomUrlSpan myURLSpan = new CustomUrlSpan(getContext(), span.getURL());
                style.setSpan(myURLSpan, sp.getSpanStart(span),
							  sp.getSpanEnd(span),
							  Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            }
            setText(style);
		}
	}

	public class CustomUrlSpan extends ClickableSpan
	{

		private Context context;
		private String url;
		public CustomUrlSpan(Context context, String url)
		{
			this.context = context;
			this.url = url;
		}

		@Override
		public void updateDrawState(TextPaint ds)
		{
			TypedValue typedValue = new TypedValue();
			context.getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true);
			ds.setColor(context.getResources().getColor(typedValue.resourceId));
			ds.setUnderlineText(false);
		}

		@Override
		public void onClick(View widget)
		{
			if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:"))
			{
				try
				{
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					context.startActivity(intent);
				}catch(ActivityNotFoundException e){
					e.fillInStackTrace();
					if(url.startsWith("mailto:")){
						IToast.makeText(context,"请安装邮箱相关APP",IToast.LENGTH_SHORT).show();
					}
				}
			}
			else if (url.startsWith("https") || url.startsWith("http") || url.startsWith("file"))
			{
				Intent intent = new Intent(context, BrowserActivity.class);
				intent.setData(Uri.parse(url));
				context.startActivity(intent);
			}
			else
			{
				try
				{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					context.startActivity(intent);
				}
				catch (ActivityNotFoundException e)
				{
					e.fillInStackTrace();
				}
			}
		}
	}
}
