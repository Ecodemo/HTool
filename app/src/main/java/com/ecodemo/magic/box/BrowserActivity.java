package com.ecodemo.magic.box;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ecodemo.magic.box.BrowserActivity;
import com.ecodemo.magic.box.app.MxActivity;
import com.ecodemo.magic.box.app.MxWebView;
import com.ecodemo.magic.box.app.utils.IToast;
import com.ecodemo.magic.box.app.widget.VideoImpl;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
public class BrowserActivity extends MxActivity {
	private MxWebView mWebView;
	private VideoImpl mIVideo = new VideoImpl(this, mWebView);
	private ProgressBar progressBar;
	private static final String mHome = "https://magi.com/";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_browser);
		mWebView = findViewById(R.id.webView);
		progressBar = findViewById(R.id.progressBar);
		mWebView.setWebChromeClient(new WebChromeClient() {
				@Override
				public void onShowCustomView(View view, CustomViewCallback callback) {
					if (mIVideo != null) {
						mIVideo.onShowCustomView(view, callback);
					}
				}

				@Override
				public void onHideCustomView() {
					if (mIVideo != null) {
						mIVideo.onHideCustomView();
					}
				}

				@Override
				public void onReceivedTitle(WebView view, String title) {
					super.onReceivedTitle(view, title);
					if (title != null) {
						setTitle(title);
					}
				}
				@Override  
				public void onProgressChanged(WebView view, int newProgress) {
					if (newProgress == 100) {
						progressBar.setVisibility(View.GONE);
					} else {
						progressBar.setVisibility(View.VISIBLE);
						progressBar.setProgress(newProgress);
					}              
				}  
			});

		Uri data = getIntent().getData();
		if (data != null) {
			mWebView.loadUrl(data.toString());
		} else {
			mWebView.loadUrl(mHome);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.browser_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
			case R.id.home:
				if (!mWebView.getUrl().equals(mHome)) {
					mWebView.loadUrl(mHome);
				}
				break;
			case R.id.visit:
				final AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
				mDialog.setTitle("请输入URL");
				final View view = View.inflate(this, R.layout.edittext, null);
				((EditText)view.findViewById(R.id.edittext)).setText(mWebView.getUrl());
				mDialog.setView(view);
				mDialog.setPositiveButton("访问", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface p1, int p2) {
							mDialog.create().dismiss();
							mWebView.loadUrl(((EditText)view.findViewById(R.id.edittext)).getText().toString());
						}		
					});

				mDialog.setNegativeButton("取消", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface p1, int p2) {
							mDialog.create().dismiss();
						}		
					});
				mDialog.create().show();
				break;
			case R.id.up:
				mWebView.goBack();
				break;
			case R.id.down:
				mWebView.goForward();
				break;
			case R.id.refresh:
				mWebView.reload();
				break;
			case R.id.share:
				Intent share_intent = new Intent();
				share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
				share_intent.setType("text/plain");//设置分享内容的类型
				share_intent.putExtra(Intent.EXTRA_TEXT, mWebView.getTitle() + "\n" + mWebView.getUrl());//添加分享内容
				startActivity(share_intent);
				break;
			case R.id.copy:
				IToast.makeText(BrowserActivity.this, "复制成功", IToast.LENGTH_SHORT).show();
				ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE); 
				cmb.setText(mWebView.getUrl());
				break;
			case R.id.code:
				mWebView.loadUrl("view-source:" + mWebView.getUrl());
				break;
			case R.id.yard:
				try {
					Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
					hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
					hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
					BitMatrix matrix = new MultiFormatWriter().encode(mWebView.getUrl(),
																	  BarcodeFormat.QR_CODE, 600, 600);
					int width = matrix.getWidth();
					int height = matrix.getHeight();
					int[] pixels = new int[width * height];

					for (int y = 0; y < height; y++) {
						for (int x = 0; x < width; x++) {
							if (matrix.get(x, y)) {
								pixels[y * width + x] = Color.BLACK;
							} else {
								pixels[y * width + x] = Color.WHITE;
							}
						}
					}
					final Bitmap bitmap = Bitmap.createBitmap(width, height,
															  Bitmap.Config.RGB_565);
					bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
					final AlertDialog.Builder _dialog = new AlertDialog.Builder(this);
					ImageView image = new ImageView(this);
					image.setImageBitmap(bitmap);
					_dialog.setView(image);
					_dialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface p1, int p2) {
								_dialog.create().dismiss();
							}		
						});
					_dialog.setNegativeButton("保存", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface p1, int p2) {
								_dialog.create().dismiss();
								saveFile(bitmap, System.currentTimeMillis() + ".png", Bitmap.CompressFormat.PNG);

						    }
						});
					_dialog.create().show();
				} catch (WriterException e) {}
				break;
			case R.id.message:
				try {
					final StringBuilder builder = new StringBuilder();
					URL url = new URL(mWebView.getUrl());
					builder.append("URL：" + url.toString());
					builder.append("\n\n协议：" + url.getProtocol());
					builder.append("\n\n验证信息：" + url.getAuthority());
					builder.append("\n\n文件名及请求参数：" + url.getFile());
					builder.append("\n\n主机名：" + url.getHost());
					builder.append("\n\n路径：" + url.getPath());
					builder.append("\n\n端口：" + url.getPort());
					builder.append("\n\n默认端口：" + url.getDefaultPort());
					builder.append("\n\n请求参数：" + url.getQuery());
					builder.append("\n\n定位位置：" + url.getRef());

					final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
					dialog.setTitle("URL信息");
					dialog.setMessage(builder.toString());
					dialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface p1, int p2) {
								dialog.create().dismiss();
							}		
						});

					dialog.setNegativeButton("复制", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface p1, int p2) {
								Toast.makeText(BrowserActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
								ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE); 
								cmb.setText(builder.toString());
								dialog.create().dismiss();
							}		
						});
					dialog.create().show();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public static void loadUrl(Context context, String url) {
		Intent intent = new Intent(context, BrowserActivity.class);
		intent.setData(Uri.parse(url));
		context.startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mWebView != null)
			mWebView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mWebView != null)
			mWebView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWebView != null)
			mWebView.destroy();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (mWebView.canGoBack()) {

					mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
					mWebView.goBack();
				} else {
					finish();
				}
				break;
		}
		return super.onKeyUp(keyCode, event);
	}


	public static void setMargins(View v, int l, int t, int r, int b) {
		if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			p.setMargins(l, t, r, b);
			v.requestLayout();
		}
	}

	private String path = Environment.getExternalStoragePublicDirectory("小木匣") + "/Picture/";
	private void saveFile(Bitmap bm, String fileName, Bitmap.CompressFormat format) {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File myCaptureFile = new File(path + fileName);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(format, 100, bos);
            bos.flush();
            bos.close();
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path + fileName)));

        } catch (Exception e) {
			e.printStackTrace();
        }
    }
}


