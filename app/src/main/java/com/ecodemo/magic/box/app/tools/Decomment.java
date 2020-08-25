package com.ecodemo.magic.box.app.tools;
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
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.MxActivity;
import com.ecodemo.magic.box.app.tools.HtmlFormat;
import com.ecodemo.magic.box.app.utils.IToast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Decomment extends MxActivity
{
	private EditText code;
	private EditText demo;
	private ImageView de;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch(msg.what)
			{
				case 0:
					code.setText((String)msg.obj);
					break;
				case 1:
					IToast.makeText(Decomment.this,"未发现代码或不支持的语言",IToast.LENGTH_SHORT).show();
					break;
			}
		}
	};
	private static final String raguler = "((?<!:)\\/\\/.*|\\/\\*(\\s|.)*?\\*\\/|<!--(.|\r\n)*-->)";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.decomment);
		demo = findViewById(R.id.demo);
		code = findViewById(R.id.code);
		de = findViewById(R.id.de);
		de.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view)
				{
					new Thread(){
						@Override
						public void run(){
							Matcher matcher = Pattern.compile(raguler).matcher(demo.getText().toString());
							if(matcher.find())
							{
								Message msg = new Message();
								msg.what = 0;
								msg.obj = matcher.replaceAll("");
								handler.sendMessage(msg);
							}else{
								handler.sendEmptyMessage(1);
							}
						}
					}.start();
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
							a.setBackgroundColor(Decomment.this.getResources().getColor(typedValue2.resourceId));
							b.setBackgroundColor(Decomment.this.getResources().getColor(typedValue2.resourceId));
							up.setTint(Decomment.this.getResources().getColor(typedValue2.resourceId));
							ib.setImageDrawable(up);
							break;
						case MotionEvent.ACTION_UP:
							ImageView Ib = (ImageView)view;
							a.setBackgroundColor(Decomment.this.getResources().getColor(typedValue.resourceId));
							b.setBackgroundColor(Decomment.this.getResources().getColor(typedValue.resourceId));
							up.setTint(Decomment.this.getResources().getColor(typedValue.resourceId));
							Ib.setImageDrawable(up);
					}
					return false;
				}
			});
	}
}
