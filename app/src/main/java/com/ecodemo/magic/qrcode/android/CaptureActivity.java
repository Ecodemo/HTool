package com.ecodemo.magic.qrcode.android;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.Result;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.MxActivity;
import com.ecodemo.magic.qrcode.bean.ZxingConfig;
import com.ecodemo.magic.qrcode.camera.CameraManager;
import com.ecodemo.magic.qrcode.common.Constant;
import com.ecodemo.magic.qrcode.decode.DecodeImgCallback;
import com.ecodemo.magic.qrcode.decode.DecodeImgThread;
import com.ecodemo.magic.qrcode.decode.ImageUtil;
import com.ecodemo.magic.qrcode.view.ViewfinderView;
import java.io.IOException;

public class CaptureActivity extends MxActivity implements SurfaceHolder.Callback, View.OnClickListener
{
    private static final String TAG = CaptureActivity.class.getSimpleName();
    public ZxingConfig config;
    private SurfaceView previewView;
    private ViewfinderView viewfinderView;
    private ImageView flashLightIv;
    private TextView flashLightTv;
    private LinearLayout flashLightLayout;
    private LinearLayout albumLayout;
    private LinearLayout bottomLayout;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private SurfaceHolder surfaceHolder;

    public ViewfinderView getViewfinderView()
	{
        return viewfinderView;
    }

    public Handler getHandler()
	{
        return handler;
    }

    public CameraManager getCameraManager()
	{
        return cameraManager;
    }

    public void drawViewfinder()
	{
        viewfinderView.drawViewfinder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);

        // 保持Activity处于唤醒状态
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        /*先获取配置信息*/
        config = new ZxingConfig();
		config.setPlayBeep(true);//是否播放扫描声音 默认为true
		config.setShake(true);//是否震动  默认为true
		config.setDecodeBarCode(true);//是否扫描条形码 默认为true
		TypedValue typedValue = new TypedValue();
		this.getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true);
		config.setReactColor(typedValue.resourceId)    ;//设置扫描框四个角的颜色 默认为白色
		this.getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
		config.setFrameLineColor(typedValue.resourceId);//设置扫描框边框颜色 默认无色
		config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
		config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描

        setContentView(R.layout.activity_capture);

        initView();

        hasSurface = false;

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        beepManager.setPlayBeep(config.isPlayBeep());
        beepManager.setVibrate(config.isShake());

    }

    private void initView()
	{
        previewView = (SurfaceView) findViewById(R.id.preview_view);
        previewView.setOnClickListener(this);

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setZxingConfig(config);

        flashLightIv = (ImageView) findViewById(R.id.flashLightIv);
        flashLightTv = (TextView) findViewById(R.id.flashLightTv);

        flashLightLayout = (LinearLayout) findViewById(R.id.flashLightLayout);
        flashLightLayout.setOnClickListener(this);
        albumLayout = (LinearLayout) findViewById(R.id.albumLayout);
        albumLayout.setOnClickListener(this);
        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);

        switchVisibility(bottomLayout, config.isShowbottomLayout());
        switchVisibility(flashLightLayout, config.isShowFlashLight());
        switchVisibility(albumLayout, config.isShowAlbum());

        /*有闪光灯就显示手电筒按钮  否则不显示*/
        if (isSupportCameraLedFlash(getPackageManager()))
		{
            flashLightLayout.setVisibility(View.VISIBLE);
        }
		else
		{
            flashLightLayout.setVisibility(View.GONE);
        }
    }


    /**
     * @param pm
     * @return 是否有闪光灯
     */
    public static boolean isSupportCameraLedFlash(PackageManager pm)
	{
        if (pm != null)
		{
            FeatureInfo[] features = pm.getSystemAvailableFeatures();
            if (features != null)
			{
                for (FeatureInfo f : features)
				{
                    if (f != null && PackageManager.FEATURE_CAMERA_FLASH.equals(f.name))
					{
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param flashState 切换闪光灯图片
     */
    public void switchFlashImg(int flashState)
	{

        if (flashState == Constant.FLASH_OPEN)
		{
            flashLightIv.setImageResource(R.drawable.ic_open);
            flashLightTv.setText("关闭闪光灯");
        }
		else
		{
            flashLightIv.setImageResource(R.drawable.ic_close);
            flashLightTv.setText("打开闪光灯");
        }

    }

    /**
     * @param rawResult 返回的扫描结果
     */
    public void handleDecode(Result rawResult)
	{
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
		final String content = rawResult.getText();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("扫描结果");
        builder.setMessage(content);
        builder.setPositiveButton(android.R.string.copy,
		new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE); 
					cmb.setText(content);
					runOnUiThread(new Runnable(){
							@Override
							public void run() {
								onResume();
							}
						});
				}
		});
		builder.setNegativeButton(android.R.string.ok,
			new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					runOnUiThread(new Runnable(){
							@Override
							public void run() {
								onResume();
							}
					});
				}
			});
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }


    private void switchVisibility(View view, boolean b)
	{
        if (b)
		{
            view.setVisibility(View.VISIBLE);
        }
		else
		{
            view.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume()
	{
        super.onResume();
		init();
    }
	
	private void init()
	{
		cameraManager = new CameraManager(getApplication(), config);
		viewfinderView.setCameraManager(cameraManager);
        handler = null;
        surfaceHolder = previewView.getHolder();
        if (hasSurface)
		{

            initCamera(surfaceHolder);
        }
		else
		{
            // 重置callback，等待surfaceCreated()来初始化camera
            surfaceHolder.addCallback(this);
        }

        beepManager.updatePrefs();
        inactivityTimer.onResume();
	}

    private void initCamera(SurfaceHolder surfaceHolder)
	{
        if (surfaceHolder == null)
		{
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen())
		{
            return;
        }
        try
		{
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            if (handler == null)
			{
                handler = new CaptureActivityHandler(this, cameraManager);
            }
        }
		catch (IOException ioe)
		{
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        }
		catch (RuntimeException e)
		{
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }
	
	private void displayFrameworkBugMessageAndExit()
	{
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("扫一扫");
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    @Override
    protected void onPause()
	{

        Log.i("CaptureActivity", "onPause");
        if (handler != null)
		{
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();

        if (!hasSurface)
		{

            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy()
	{
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
	{
        if (!hasSurface)
		{
            hasSurface = true;
            initCamera(holder);
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
	{
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height)
	{

    }

    @Override
    public void onClick(View view)
	{
        int id = view.getId();
        if (id == R.id.flashLightLayout)
		{
            /*切换闪光灯*/
            cameraManager.switchFlashLight(handler);
        }
		else if (id == R.id.albumLayout)
		{
            /*打开相册*/
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, Constant.REQUEST_IMAGE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_IMAGE && resultCode == RESULT_OK)
		{
            String path = ImageUtil.getImageAbsolutePath(this, data.getData());

            new DecodeImgThread(path, new DecodeImgCallback() {
					@Override
					public void onImageDecodeSuccess(Result result)
					{
						handleDecode(result);
					}

					@Override
					public void onImageDecodeFailed()
					{
						Toast.makeText(CaptureActivity.this, "抱歉，解析失败,换个图片试试.", Toast.LENGTH_SHORT).show();
					}
				}).run();
        }
    }
}
