package com.smile.box;
import android.app.Application;
import android.util.Log;
import com.smile.box.app.CrashHandler;
import com.tencent.smtt.sdk.QbSdk;

public class MxApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		CrashHandler.getInstance().init(this);
		/*//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
			@Override
			public void onViewInitFinished(boolean arg0) {}
			@Override
			public void onCoreInitFinished() {}
		};
		//x5内核初始化接口
		QbSdk.initX5Environment(getApplicationContext(),  cb);*/
	}
}
