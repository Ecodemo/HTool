package com.smile.box.app.service;
import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;

public class AccessibilityService extends AccessibilityService
{
	public static final int TYPE_CURRENTVIEW = 0;
	private int current;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams params;
	private TextView view;
	
	@Override
	public void onInterrupt()
	{
		
	}

	@Override
	protected void onServiceConnected()
	{
		super.onServiceConnected();
		mWindowManager = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
		if(current==0)
		{
			params = new WindowManager.LayoutParams();
			params.type = WindowManager.LayoutParams.TYPE_TOAST;
			params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
			params.gravity = Gravity.LEFT | Gravity.TOP;
			params.format = PixelFormat.TRANSLUCENT;
			params.width = WindowManager.LayoutParams.WRAP_CONTENT;
			params.height = WindowManager.LayoutParams.WRAP_CONTENT;
			params.x = 0;
			params.y = 0;
			view = new TextView(this);
			view.setTextSize(14);
			view.setText("当前界面");
			view.setTextColor(Color.BLACK);
			view.setOnTouchListener(new Touch());
			mWindowManager.addView(view, params);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		current = intent.getIntExtra("command", 0);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event)
	{
		if (current == 0)
		{
			switch (event.getEventType())
			{
				case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
					StringBuilder sb = new StringBuilder();
					sb.append("软件名：").append(getProgramNameByPackageName(this,event.getPackageName().toString())).append("\n");
					sb.append("包名：").append(event.getPackageName().toString()).append("\n");
					sb.append("类名：").append(event.getClassName().toString());
					view.setText(sb);
					break;
			}
		}
	}
	
	
	class Touch implements OnTouchListener
	{
		int startY;
		int startX;
		@Override
		public boolean onTouch(View v,  MotionEvent e)
		{
			switch (e.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					startX = (int) e.getRawX();
					startY = (int) e.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int newX = (int) e.getRawX();
					int newY = (int) e.getRawY();
					//计算偏移量
					int dx = newX - startX;
					int dy = newY - startY;
					//更新View在窗体的位置坐标:当前坐标+偏移量
					params.x += dx;
					params.y += dy;
					// 考虑边界问题
					if (params.x < 0)
					{
						params.x = 0;
					}
					if (params.y < 0)
					{
						params.y = 0;
					}
					if (params.x > mWindowManager.getDefaultDisplay().getWidth() - view.getWidth())
					{
						params.x = mWindowManager.getDefaultDisplay().getWidth() - view.getWidth();
					}
					if (params.y > mWindowManager.getDefaultDisplay().getHeight() - view.getHeight())
					{
						params.y = mWindowManager.getDefaultDisplay().getHeight() - view.getHeight();
					}
					//刷新位置
					mWindowManager.updateViewLayout(view, params);
					//更新初始位置
					startX = (int) e.getRawX();
					startY = (int) e.getRawY();
					break;
			}
			return false;
		}
	}
	
	
	public static String getProgramNameByPackageName(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		String name = null;
		try {
			name = pm.getApplicationLabel(
				pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}
}
