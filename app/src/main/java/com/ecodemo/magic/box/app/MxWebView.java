package com.ecodemo.magic.box.app;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.webkit.JavascriptInterface;
import android.util.Log;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebResourceResponse;
import java.io.InputStream;
import com.ecodemo.magic.box.app.utils.Util;
import com.ecodemo.magic.box.app.update.UpdateUtil;

public class MxWebView extends WebView {
	
	private WebViewClient client = new WebViewClient() {
		
		private String startUrl;
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			startUrl = url;
		}
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			if(startUrl!=null && startUrl.equals(url)){
				if (url.startsWith("https") || url.startsWith("http") || url.startsWith("file")){
					webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
					view.loadUrl(url);
				}else{
					try{
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						getContext().startActivity(intent);
						return false;
					}catch (ActivityNotFoundException e){
						return true;
					}
				}
			}else{
				return super.shouldOverrideUrlLoading(view, url);
			}
			return true;
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url)
		{
			if (url.contains("[tag]"))
			{
				String localPath = url.replaceFirst("^http.*[tag]\\]", "");
				try
				{
					InputStream is = getContext().getAssets().open(localPath);
					String mimeType = "text/javascript";
					if (localPath.endsWith("css"))
					{
						mimeType = "text/css";
					}
					return new WebResourceResponse(mimeType, "UTF-8", is);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return null;
				}
			}
			else
			{
				return null;
			}

		}
	};

	private WebSettings webSetting;
	
	public MxWebView(Context context, AttributeSet attr)
	{
		super(context, attr);
		initWebViewSettings();
		addJavascriptInterface(new JSInterface(), "mxbox");
		setWebViewClient(client);
	}
	
	private void initWebViewSettings() {
		webSetting = getSettings();
		webSetting.setUserAgentString(webSetting.getUserAgentString() + " MxBox/" + UpdateUtil.getVerName(getContext()));
		webSetting.setJavaScriptEnabled(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setDisplayZoomControls(false);
		webSetting.setBlockNetworkImage(false);//解决图片不显示
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		webSetting.setAppCacheEnabled(true);
		webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);//开启DOM缓存，关闭的话H5自身的一些操作是无效的
		webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
	}
	
	public class JSInterface
	{
		@JavascriptInterface
		public String getVersion()
		{
			return UpdateUtil.getVerName(getContext());
		}
	}
}

