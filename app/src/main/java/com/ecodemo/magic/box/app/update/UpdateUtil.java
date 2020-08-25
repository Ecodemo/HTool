package com.ecodemo.magic.box.app.update;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.utils.DownloadListener;
import com.ecodemo.magic.box.app.utils.DownloadManager;
import com.ecodemo.magic.box.app.utils.IToast;
import java.io.File;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.util.Log;

public class UpdateUtil
{
	private Context context;
	private static final String url = "https://ecodemo.gitee.io/resources/mt/update.json";
	private boolean showTips = false;
	private Handler handler = new Handler(){
		private StringBuilder sb = new StringBuilder();
		private String updateUrl;
		private int code;
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			Update update = analysis((String)msg.obj);
			code = getVersionCode(context);
			String name = getVerName(context);
			updateUrl = update.getDownload();
			if (update.getVersionCode() > code)
			{
				sb.append("最新版本：").append(update.getVersionCode()).append("\n");
				sb.append("当前版本：").append(name).append("\n");
				sb.append("更新内容：").append("\n");
				sb.append(update.getLog());
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("应用更新");
				builder.setMessage(sb.toString());
				AlertDialog dialog = builder.create();
				dialog.setButton("立即更新", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2)
						{
							Download(context, updateUrl, "update_"+code+".apk");
						}
					});
				
				if (!update.isForce())
				{
					dialog.setButton2("下次再说", (DialogInterface.OnClickListener)null);
				}
				dialog.show();
			}else if(showTips){
				IToast.makeText(context, "当前已是最新版本", IToast.LENGTH_SHORT).show();
			}
		}
	};

	public UpdateUtil(Context context)
	{
		this.context = context;
	}

	public void automatic()
	{
		new UpdateThread().start();
	}
	
	public void manual()
	{
		this.showTips = true;
		new UpdateThread().start();
	}

	class UpdateThread extends Thread
	{
		@Override
		public void run()
		{
			OkHttpClient client = new OkHttpClient.Builder().build();
			Request request = new Request.Builder()
			    .header("User-Agent", System.getProperty("http.agent"))
				.url(url)
				.build();
			Call call = client.newCall(request);
			try {
				Response response = call.execute();
				Message msg = new Message();
				msg.obj = response.body().string();
				msg.what = 0;
				handler.sendMessage(msg);
			} catch (IOException e) {}
		}
	}

	private Update analysis(String json)
	{
		return new Gson().fromJson(new JsonParser().parse(json), Update.class);
	}

	public static int getVersionCode(Context mContext)
	{
        int versionCode = 0;
        try
		{
            versionCode = mContext.getPackageManager().
				getPackageInfo(mContext.getPackageName(), 0).versionCode;
        }
		catch (PackageManager.NameNotFoundException e)
		{
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getVerName(Context context)
	{
        String verName = "";
        try
		{
            verName = context.getPackageManager().
				getPackageInfo(context.getPackageName(), 0).versionName;
        }
		catch (PackageManager.NameNotFoundException e)
		{
            e.printStackTrace();
        }
        return verName;
    }

	public static void Download(final Context context , final String Url,final String fileName)
	{
		final DownloadManager mDownloadManager = DownloadManager.getInstance();
		int thread = PreferenceManager.getDefaultSharedPreferences(context).getInt("thread", 32);
		final ProgressDialog pd = new ProgressDialog(context);
		pd.setTitle("文件下载");
		pd.setMessage("线程数量："+thread+"\n正在下载：" + fileName);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMax(100);
		pd.setCancelable(false);
		pd.setIndeterminate(false);
		pd.setButton("取消下载", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface di, int position)
				{
					mDownloadManager.cancel(Url);
					di.dismiss();
					IToast.makeText(context , "已取消下载", IToast.LENGTH_SHORT).show();
				}
			});
		pd.show();
		final String path = PreferenceManager.getDefaultSharedPreferences(context).getString("download_path", Environment.getExternalStoragePublicDirectory("小木匣") + File.separator + "Download");
		mDownloadManager.add(Url, path, thread, fileName, new DownloadListener(){
				@Override
				public void onFinished() {
					IToast.makeText(context, "下载完成", IToast.LENGTH_SHORT).show();
					pd.dismiss();
					if (mDownloadManager.getFileName().substring(mDownloadManager.getFileName().lastIndexOf(".") + 1).equals("apk")) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setDataAndType(Uri.fromFile(new File(path, mDownloadManager.getFileName())), "application/vnd.android.package-archive");
						context.startActivity(intent);
					}
				}
				@Override
				public void onProgress(final float progress) {
					pd.setProgress((int)(progress * 100));
				}
				@Override
				public void onPause() {
				}
				@Override
				public void onCancel() {
				}
			});
		mDownloadManager.download(Url);
	}
}
