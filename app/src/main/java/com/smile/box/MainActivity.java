package com.smile.box;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.smile.box.app.BottomTabBar;
import com.smile.box.app.NotchManager;
import com.smile.box.app.fragment.FragmentAbout;
import com.smile.box.app.fragment.FragmentHistory;
import com.smile.box.app.fragment.FragmentHome;
import com.smile.box.app.fragment.FragmentTool;
import com.smile.box.app.update.UpdateUtil;
import android.Manifest;
import android.content.pm.PackageManager;

public class MainActivity extends Activity
{
	private BottomTabBar mBottomBar;
	//读写权限
    private static String[] PERMISSIONS_STORAGE = {
		Manifest.permission.CAMERA,
		Manifest.permission.WRITE_EXTERNAL_STORAGE,
		Manifest.permission.RECORD_AUDIO,
		Manifest.permission.ACCESS_FINE_LOCATION};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setCurrentTheme(this);
        setContentView(R.layout.activity_main);
		initBottomBar();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
			if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && 
			   checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
			   checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED &&
			   checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
			{
			    requestPermissions(PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
			}
        }
		new UpdateUtil(this).automatic();
    }
	
	private void initBottomBar()
	{
		mBottomBar = findViewById(R.id.bottom_bar);
		TypedValue typedValue = new TypedValue();
		getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
		TypedValue frame = new TypedValue();
		getTheme().resolveAttribute(R.attr.FrameColor, frame, true);
		BottomTabBar btb = mBottomBar.init(getFragmentManager(), 750, 1334)    
			.setImgSize(50, 50)
			.setFontSize(28)
			.setTabPadding(10, 6, 4)
			.setChangeColor(Color.parseColor("#2784E7"), MainActivity.this.getResources().getColor(frame.resourceId))
		    .isShowDivider(true)
		    .setDividerColor(MainActivity.this.getResources().getColor(frame.resourceId))
			.setTabBarBackgroundColor(MainActivity.this.getResources().getColor(typedValue.resourceId));
			
		btb.addTabItem("首页", R.drawable.ic_home_black, FragmentHome.class);
		btb.addTabItem("工具", R.drawable.ic_tool_black, FragmentTool.class);
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("history", false))
		{
			btb.addTabItem("历史", R.drawable.ic_history_black, FragmentHistory.class);
		}
		btb.addTabItem("关于", R.drawable.ic_info_outline_black_48dp, FragmentAbout.class);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		//适配全面屏
		NotchManager.initNotchListener(this);
	}

	public static void setCurrentTheme(Context context)
	{
		boolean current = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("dark_theme", false);
		int style = current ? R.style.BlackTheme : R.style.AppTheme;
		context.setTheme(style);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
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
