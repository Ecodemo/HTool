package com.smile.box.app;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.smile.box.R;
import android.view.animation.LinearInterpolator;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.zxing.common.StringUtils;
import android.text.TextUtils;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MxActivity extends Activity
{
	private LinearLayout linear;
	private TextView titleView;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setCurrentTheme(this);
		ViewGroup view = findViewById(android.R.id.content);
		view.addView(View.inflate(this, R.layout.view, null));
		linear = findViewById(R.id.view);
		titleView = findViewById(R.id.title);
		findViewById(R.id.more).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view)
				{
					
				}
		});
		findViewById(R.id.exit).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view)
				{
					finish();
				}
			});
		String title = getIntent().getStringExtra("title");
		if(!TextUtils.isEmpty(title))
		{
			titleView.setText(title);
		}else{
			titleView.setText(getTitle().toString());
		}
	}

	@Override
	public void setTitle(CharSequence title)
	{
		super.setTitle(title);
		titleView.setText(title.toString());
	}
	
	@Override
	public void setContentView(int layoutResID)
	{
		LayoutInflater.from(this).inflate(layoutResID, linear);
	}

	@Override
	public void setContentView(View view)
	{
		linear.removeAllViews();
		linear.addView(view);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		//适配全面屏
		NotchManager.initNotchListener(this);
	}
	
	public LinearLayout getTitleView()
	{
		return findViewById(R.id.view_parent);
	}
	
	public static void setCurrentTheme(Context context)
	{
		boolean current = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("dark_theme", false);
		int style = current ? R.style.BlackTheme : R.style.AppTheme;
		context.setTheme(style);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			//获取窗口区域
			Window window = ((Activity)context).getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.getDecorView().setSystemUiVisibility(current ? View.SYSTEM_UI_FLAG_LAYOUT_STABLE : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			if(context instanceof PreferenceActivity)
			{
				window.setStatusBarColor(current ? Color.BLACK: Color.WHITE);
			}
        }
	}
}
