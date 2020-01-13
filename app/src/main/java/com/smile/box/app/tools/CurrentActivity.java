package com.smile.box.app.tools;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.smile.box.R;
import com.smile.box.app.MxActivity;
import com.smile.box.app.service.AccessibilityService;
import com.smile.box.app.utils.FloatWindowManager;
import com.smile.box.app.utils.Util;

public class CurrentActivity extends MxActivity
{

	private Button start;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.current_activity);
		start = findViewById(R.id.start);
		if(Util.isServiceRunning(CurrentActivity.this,AccessibilityService.class.getName()))
		{
			start.setText("关闭服务");
		}
		start.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view)
				{
					if(Util.isServiceRunning(CurrentActivity.this,AccessibilityService.class.getName()))
					{
						stopService(new Intent(CurrentActivity.this, CurrentActivity.class));
						return;
					}
					
					if (!FloatWindowManager.checkPermission(CurrentActivity.this))
					{
						FloatWindowManager.applyPermission(CurrentActivity.this);
					}
					else
					{
						if (isAccessibilitySettings(CurrentActivity.this))
						{
							Intent intent = new Intent(CurrentActivity.this, AccessibilityService.class);
							intent.putExtra("command", 0);
							startService(intent);
						}else{
							Intent intent = new Intent(CurrentActivity.this, AccessibilityService.class);
							intent.putExtra("command", 0);
							startService(intent);
							intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
							startActivity(intent);
						}
					}
					
					if(Util.isServiceRunning(CurrentActivity.this,AccessibilityService.class.getName()))
					{
						start.setText("关闭服务");
					}
				}
			});
	}

	private boolean isAccessibilitySettings(Context mContext)
	{
		int accessibilityEnabled = 0;
		// TestService为对应的服务
		final String service = getPackageName() + "/" + AccessibilityService.class.getCanonicalName();
		try
		{
			accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
		}
		catch (Settings.SettingNotFoundException e)
		{}
		TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

		if (accessibilityEnabled == 1)
		{
			String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
			if (settingValue != null)
			{
				mStringColonSplitter.setString(settingValue);
				while (mStringColonSplitter.hasNext())
				{
					String accessibilityService = mStringColonSplitter.next();
					if (accessibilityService.equalsIgnoreCase(service)) return true;
				}
			}
		}
		return false;
	}
}
