package com.smile.box.app.utils;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.smile.box.app.MxActivity;
import java.io.IOException;
import java.net.Proxy;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import java.io.InputStreamReader;
import java.io.InputStream;
import android.net.Uri;
import java.io.File;
import org.apache.commons.io.FileUtils;

public class RunScriptApp extends MxActivity
{
	private String url;
	private org.mozilla.javascript.Context ctx;
	private Scriptable scope;
	private Dialog dialog;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			dialog.dismiss();
			switch (msg.what)
			{
				case 0:
				    ctx.evaluateString(scope, (String)msg.obj, RunScriptApp.class.getSimpleName(), 1, null);
					break;
				case 1:
					break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ctx = ContextFactory.getGlobal().enterContext();
		scope = ctx.initStandardObjects(null);
		ctx.setOptimizationLevel(-1);
		ctx.setLanguageVersion(org.mozilla.javascript.Context.VERSION_1_8);
		scope.put("context", scope, this);
		try
		{
			InputStreamReader reader = new InputStreamReader(getResources().getAssets().open("api.js"));
			ctx.evaluateReader(scope, reader, RunScriptApp.class.getSimpleName(), 1, null);
		}
		catch (IOException e)
		{}
		url = getIntent().getStringExtra("url");
		if (url.endsWith("js"))
		{
			loaderJF();
		}
		else if (url.endsWith("zip"))
		{
		}
	}

	private void loaderJF()
	{
		dialog = ProgressDialog.show(this, "正在加载", null, true, false);
		Log.e("URL", url);
		if (url.startsWith("content://"))
		{
			new Thread(new Runnable(){
					@Override
					public void run()
					{
						Uri uri = Uri.parse(url);
						File file = new File(UriToPath.getPath(RunScriptApp.this,uri));
						try
						{
							String js = FileUtils.readFileToString(file);
							ParseTool.encode(RunScriptApp.this,  file.toString());
							Message msg = new Message();
							msg .what = 0;
							msg.obj = js;
							handler.sendMessage(msg);
						}
						catch (IOException e)
						{}
					}
				}).start();
		}
		else
		{
			new LoaderJF().start();
		}
	}

	private class LoaderJF extends Thread
	{
		@Override
		public void run()
		{
			//创建okHttpClient对象
			OkHttpClient okHttpClient=new OkHttpClient().newBuilder().proxy(Proxy.NO_PROXY).build();
			//创建request,首先要有一个url
			Request request=new Request.Builder().url(url).build();
			//通过request的对象去构造得到一个Call对象，
			// 类似于将你的请求封装成了任务，既然是任务，就会有execute()和cancel()等方法。
			Call call=okHttpClient.newCall(request);
			//以异步的方式去执行请求,调用的是call.enqueue，将call加入调度队列，
			// 然后等待任务执行完成，我们在Callback中即可得到结果。
			call.enqueue(new Callback() {
					@Override
					public void onFailure(Call call, IOException e)
					{
					}
					@Override
					public void onResponse(Call call, Response response) throws IOException
					{
						//请求成功返回结果
						byte[] bytes = response.body().bytes();
						try
						{
							String js = ParseTool.parse(bytes);
							Log.e("JS", js);
							Message msg = new Message();
							msg .what = 0;
							msg.obj = js;
							handler.sendMessage(msg);
						}
						catch (Exception e)
						{}
					}
				});
		}
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Object object = scope.get("onStart", scope);
		if (object != null && object instanceof Function)
		{
			((Function)object).call(ctx, scope, scope, new Object[]{});
		}
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		Object object = scope.get("onRestart", scope);
		if (object != null && object instanceof Function)
		{
			((Function)object).call(ctx, scope, scope, new Object[]{});
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		Object object = scope.get("onPrepareOptionsMenu", scope);
		if (object != null && object instanceof Function)
		{
			return ((Function)object).call(ctx, scope, scope, new Object[]{menu});
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Object object = scope.get("onOptionsItemSelected", scope);
		if (object != null && object instanceof Function)
		{
			return ((Function)object).call(ctx, scope, scope, new Object[]{item});
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Object object = scope.get("onDestroy", scope);
		if (object != null && object instanceof Function)
		{
			((Function)object).call(ctx, scope, scope, new Object[]{});
		}
	}
}
