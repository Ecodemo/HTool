package com.smile.box;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.smile.box.app.MxActivity;
import com.smile.box.app.webview.MxWebView;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;
import java.net.MalformedURLException;
import java.net.URL;
import com.smile.box.app.utils.Util;
import com.smile.box.app.utils.DownloadTool;
import android.webkit.CookieManager;
import android.os.Build;
import java.io.File;
import android.util.Log;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FileUtils;
import java.io.IOException;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import java.util.List;
import java.util.ArrayList;
import android.widget.PopupWindow;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import com.smile.box.app.utils.DisplayUtil;
import com.smile.box.app.adapter.WebAdapter;
import com.smile.box.app.adapter.Web;
import android.widget.PopupMenu.OnDismissListener;
import android.widget.PopupMenu;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Adapter;

public class BrowserActivity extends MxActivity
{
	/**
	 * 作为一个浏览器的示例展示出来，采用android+web的模式
	 */
	private MxWebView mWebView;
	private ImageButton mBack;
	private ImageButton mForward;
	private ImageButton mHome;
	private ImageButton mMore;
	private View toolbar;
	private static final String mHomeUrl = "http://pos.adxwu.com/home/";
	private final int disable = 120;
	private final int enable = 255;
	private ProgressBar mPageLoadingProgressBar;
	private URL mIntentUrl;
	private FrameLayout mFast;
	private List<View> mFasts = new ArrayList<>();
	private int currentWeb = 0;
	private View web;
	private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		Intent intent = getIntent();
		try
		{
			mIntentUrl = new URL(intent.getData().toString());
		}
		catch (MalformedURLException e)
		{}
		try
		{
			if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11)
			{
				getWindow()
					.setFlags(
					android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
					android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
			}
		}
		catch (Exception e)
		{
		}

		/*
		 * getWindow().addFlags(
		 * android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
		setContentView(R.layout.layout_browser);
		mFast = findViewById(R.id.fastView);
		mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 0);

	}

	private void changGoForwardButton(WebView view)
	{
		if (view.canGoBack())
			mBack.setAlpha(enable);
		else
			mBack.setAlpha(disable);
		if (view.canGoForward())
			mForward.setAlpha(enable);
		else
			mForward.setAlpha(disable);
		if (view.getUrl() != null && view.getUrl().equals(mHomeUrl))
		{
			mHome.setAlpha(disable);
			mHome.setEnabled(false);
		}
		else
		{
			mHome.setAlpha(enable);
			mHome.setEnabled(true);
		}
	}

	private void initProgressBar()
	{
		mPageLoadingProgressBar = web.findViewById(R.id.progressBar);// new
		// ProgressBar(getApplicationContext(),
		// null,
		// android.R.attr.progressBarStyleHorizontal);
		mPageLoadingProgressBar.setMax(100);
		mPageLoadingProgressBar.setProgressDrawable(this.getResources()
													.getDrawable(R.drawable.color_progressbar));
	}

	private void init()
	{
		web = View.inflate(this, R.layout.web_view, null);
		mWebView = web.findViewById(R.id.webView);
		initProgressBar();
		initBtnListenser();
		mFasts.add(web);
		currentWeb = mFasts.size() -1;
		mFast.removeAllViews();
		mFast.addView(web, new FrameLayout.LayoutParams(
						  FrameLayout.LayoutParams.FILL_PARENT,
						  FrameLayout.LayoutParams.FILL_PARENT));
		mWebView.setWebViewClient(new WebViewClient() {

				/*@Override
				 public boolean shouldOverrideUrlLoading(WebView view, String url) {
				 return false;
				 }*/

				@Override
				public void onPageFinished(WebView view, String url)
				{
					super.onPageFinished(view, url);
					if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
						changGoForwardButton(view);
					/* mWebView.showLog("test Log"); */
				}
			});

		mWebView.setWebChromeClient(new WebChromeClient() {

				// For Android 3.0+
				public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType)
				{
					BrowserActivity.this.uploadFile = uploadFile;
					openFileChooseProcess();
				}

				// For Android < 3.0
				public void openFileChooser(ValueCallback<Uri> uploadMsgs)
				{
					BrowserActivity.this.uploadFile = uploadFile;
					openFileChooseProcess();
				}

				// For Android  > 4.1.1
				public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
				{
					BrowserActivity.this.uploadFile = uploadFile;
					openFileChooseProcess();
				}

				// For Android  >= 5.0
				public boolean onShowFileChooser(com.tencent.smtt.sdk.WebView webView,
												 ValueCallback<Uri[]> filePathCallback,
												 WebChromeClient.FileChooserParams fileChooserParams)
				{
					BrowserActivity.this.uploadFiles = filePathCallback;
					openFileChooseProcess();
					return true;
				}

				@Override
				public void onReceivedTitle(WebView view, String title)
				{
					super.onReceivedTitle(view, title);
					changGoForwardButton(view);
					if (title != null)
					{
						setTitle(title);
					}
				}

				@Override  
				public void onProgressChanged(WebView view, int newProgress)
				{
					if (newProgress == 100)
					{
						mPageLoadingProgressBar.setVisibility(View.GONE);
					}
					else
					{
						mPageLoadingProgressBar.setVisibility(View.VISIBLE);
						mPageLoadingProgressBar.setProgress(newProgress);
					}              
				}

				@Override
				public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
										   JsResult arg3)
				{
					return super.onJsConfirm(arg0, arg1, arg2, arg3);
				}

				View myVideoView;
				View myNormalView;
				CustomViewCallback callback;
				/**
				 * 全屏播放配置
				 */
				@Override
				public void onShowCustomView(View view, CustomViewCallback customViewCallback)
				{
					/*FrameLayout normalView = (FrameLayout) findViewById(R.id.web_filechooser);
					ViewGroup viewGroup = (ViewGroup) normalView.getParent();
					viewGroup.removeView(normalView);
					viewGroup.addView(view);
					myVideoView = view;
					myNormalView = normalView;
					callback = customViewCallback;*/
				}

				@Override
				public void onHideCustomView()
				{
					if (callback != null)
					{
						callback.onCustomViewHidden();
						callback = null;
					}
					if (myVideoView != null)
					{
						ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
						viewGroup.removeView(myVideoView);
						viewGroup.addView(myNormalView);
					}
				}

				@Override
				public boolean onJsAlert(WebView arg0, String arg1, String arg2,
										 JsResult arg3)
				{
					/**
					 * 这里写入你自定义的window alert
					 */
					return super.onJsAlert(null, arg1, arg2, arg3);
				}
			});

		mWebView.setDownloadListener(new DownloadListener() {
				@Override
				public void onDownloadStart(String url, String arg1, String arg2, String arg3, long arg4)
				{
					if (Util.isAvilible(BrowserActivity.this, "com.dv.adm.pay"))
					{
						Intent intent = new Intent();        
						intent.setAction("android.intent.action.VIEW");    
						Uri content_url = Uri.parse(url);   
						intent.setData(content_url);           
						intent.setClassName("com.dv.adm.pay", "com.dv.adm.pay.AEditor");   
						startActivity(intent);
					}
					else if (Util.isAvilible(BrowserActivity.this, "idm.internet.download.manager.plus"))
					{
						Intent intent = new Intent();        
						intent.setAction("android.intent.action.VIEW");    
						Uri content_url = Uri.parse(url);   
						intent.setData(content_url);           
						intent.setClassName("idm.internet.download.manager.plus", "idm.internet.download.manager.Downloader");
						startActivity(intent);
					}
					else
					{
						DownloadTool.Download(BrowserActivity.this, url);
					}
				}
			});

		WebSettings webSetting = mWebView.getSettings();
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(false);
		// webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		// webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		webSetting.setAppCachePath(getDir("appcache", 0).getPath());
		webSetting.setDatabasePath(getDir("databases", 0).getPath());
		webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		// webSetting.setPreFectch(true);
		long time = System.currentTimeMillis();
		if (mIntentUrl == null)
		{
			mWebView.loadUrl(mHomeUrl);
		}
		else
		{
			mWebView.loadUrl(mIntentUrl.toString());
		}
		TbsLog.d("time-cost", "cost time: "
				 + (System.currentTimeMillis() - time));
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().sync();
	}

	private void initBtnListenser()
	{
		mBack = (ImageButton) findViewById(R.id.btnBack);
		mForward = (ImageButton) findViewById(R.id.btnForward);
		mHome = (ImageButton) findViewById(R.id.btnHome);
		mMore = (ImageButton) findViewById(R.id.btnMore);
		toolbar = findViewById(R.id.toolbar);
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
		{
			mBack.setAlpha(disable);
			mForward.setAlpha(disable);
			mHome.setAlpha(disable);
		}
		mHome.setEnabled(false);
		mBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{
					if (mWebView != null && mWebView.canGoBack())
						mWebView.goBack();
				}
			});

		mForward.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					if (mWebView != null && mWebView.canGoForward())
						mWebView.goForward();
				}
			});
		mMore.setOnClickListener(new OnClickListener() {
			private boolean one = false;
				@Override
				public void onClick(View v)
				{
					if(one)
					{
						one = false;
						return;
					}else{
						one = true;
					}
					PopupWindow window = new PopupWindow(BrowserActivity.this);
					View view = getLayoutInflater().inflate(R.layout.web_dialog, null);
					window.setFocusable(false);
					window.setOutsideTouchable(true);
					window.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
					window.setHeight(DisplayUtil.dip2px(BrowserActivity.this, 150));
					window.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
					window.setAnimationStyle(R.style.window_anim_style);
					window.setContentView(view);
					ListView list = view.findViewById(R.id.list);
					List<Web> w = new ArrayList<>();
					for(View i : mFasts)
					{
						Web p = new Web();
						MxWebView web = i.findViewById(R.id.webView);
						p.setTitle(web.getTitle());
						p.setIcon(web.getFavicon());
						w.add(p);
					}
					WebAdapter adapter = new WebAdapter(BrowserActivity.this, w);
					list.setAdapter(adapter);
					list.setOnItemClickListener(new OnItemClickListener(){
							@Override
							public void onItemClick(AdapterView<?> adapter, View view, int position, long p4)
							{
								currentWeb = position;
								mWebView = mFasts.get(position).findViewById(R.id.webView);
								setTitle(mWebView.getTitle());
								mFast.removeAllViews();
								mFast.addView(mFasts.get(position), new FrameLayout.LayoutParams(
												  FrameLayout.LayoutParams.FILL_PARENT,
												  FrameLayout.LayoutParams.FILL_PARENT));
								changGoForwardButton(mWebView);
							}
					});
			        view.findViewById(R.id.add).setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View view)
							{
								mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 0);
							}
					});
					window.showAtLocation(toolbar, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, toolbar.getMeasuredHeight());
				}
			});

		mHome.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					if (mWebView != null)
						mWebView.loadUrl(mHomeUrl);
				}
			});
	}

	boolean[] m_selected = new boolean[] { true, true, true, true, false, false, true };

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (mWebView != null && mWebView.canGoBack())
			{
				mWebView.goBack();
				if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
					changGoForwardButton(mWebView);
				return true;
			}
			else
				return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
            switch (requestCode)
			{
                case 0:
                    if (null != uploadFile)
					{
                        Uri result = data == null || resultCode != RESULT_OK ? null
							: data.getData();
                        uploadFile.onReceiveValue(result);
                        uploadFile = null;
                    }
                    if (null != uploadFiles)
					{
                        Uri result = data == null || resultCode != RESULT_OK ? null
							: data.getData();
                        uploadFiles.onReceiveValue(new Uri[]{result});
                        uploadFiles = null;
                    }
                    break;
                default:
                    break;
            }
        }
		else if (resultCode == RESULT_CANCELED)
		{
            if (null != uploadFile)
			{
                uploadFile.onReceiveValue(null);
                uploadFile = null;
            }

        }
	}

	private void openFileChooseProcess()
	{
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "选择文件"), 0);
    }

	@Override
	protected void onNewIntent(Intent intent)
	{
		if (intent == null || mWebView == null || intent.getData() == null)
			return;
		mWebView.loadUrl(intent.getData().toString());
	}

	@Override
	protected void onDestroy()
	{
		if (mTestHandler != null)
			mTestHandler.removeCallbacksAndMessages(null);
		if (mWebView != null)
			mWebView.destroy();
		//X5浏览器内核清理
		CookieSyncManager.createInstance(this);
		CookieManager x5cookieManager = CookieManager.getInstance();
		x5cookieManager.removeAllCookie();
		mWebView.clearCache(true);
		new MxWebView(this).clearCache(true);
		super.onDestroy();
	}

	public static final int MSG_INIT_UI = 1;
	private Handler mTestHandler = new Handler() {
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case MSG_INIT_UI:
					init();
					break;
			}
			super.handleMessage(msg);
		}
	};

	private static void deleteDirectory(File file)
	{
		if (!file.exists() && !file.isDirectory()) return;
		for (File f : file.listFiles())
		{
			if (f.isFile())
			{
				f.delete();
			}
			else if (f.isDirectory())
			{
				deleteDirectory(f);
			}
		}
		file.delete();
	}
}

