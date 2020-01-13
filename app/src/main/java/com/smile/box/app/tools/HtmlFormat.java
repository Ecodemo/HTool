package com.smile.box.app.tools;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import com.smile.box.R;
import com.smile.box.app.MxActivity;
import com.smile.box.app.tools.Md5;
import org.jsoup.Jsoup;

public class HtmlFormat extends MxActivity
{
	private EditText code;
	private EditText demo;
	private ImageView de;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			code.setText(Jsoup.parseBodyFragment(demo.getText().toString()).html());
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.html_format);
		demo = findViewById(R.id.demo);
		code = findViewById(R.id.code);
		de = findViewById(R.id.de);
		de.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view)
				{
					handler.sendEmptyMessage(0);
				}
			});
		final View a = findViewById(R.id.a);
		final View b = findViewById(R.id.b);
		de.setOnTouchListener(new OnTouchListener(){
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					TypedValue typedValue = new TypedValue();
					getTheme().resolveAttribute(R.attr.TextColor, typedValue, true);
					TypedValue typedValue2 = new TypedValue();
					getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue2, true);
					Drawable up = getResources().getDrawable(R.drawable.conversion);
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							ImageView ib = (ImageView)view;
							a.setBackgroundColor(HtmlFormat.this.getResources().getColor(typedValue2.resourceId));
							b.setBackgroundColor(HtmlFormat.this.getResources().getColor(typedValue2.resourceId));
							up.setTint(HtmlFormat.this.getResources().getColor(typedValue2.resourceId));
							ib.setImageDrawable(up);
							break;
						case MotionEvent.ACTION_UP:
							ImageView Ib = (ImageView)view;
							a.setBackgroundColor(HtmlFormat.this.getResources().getColor(typedValue.resourceId));
							b.setBackgroundColor(HtmlFormat.this.getResources().getColor(typedValue.resourceId));
							up.setTint(HtmlFormat.this.getResources().getColor(typedValue.resourceId));
							Ib.setImageDrawable(up);
					}
					return false;
				}
			});
	}
}
