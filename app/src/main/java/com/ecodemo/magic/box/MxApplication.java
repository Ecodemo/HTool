package com.ecodemo.magic.box;
import android.app.Application;
import com.ecodemo.magic.box.app.CrashHandler;

public class MxApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		CrashHandler.getInstance().init(this);
	}
}
