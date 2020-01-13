package com.smile.box.app.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import com.smile.box.R;
import java.io.File;
import android.preference.PreferenceManager;

public class DownloadTool {

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
		final String path = PreferenceManager.getDefaultSharedPreferences(context).getString("download_path", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MToolBox/Download");
		mDownloadManager.add(Url, path, thread, fileName, new DownloadListener(){
				@Override
				public void onFinished() {
					IToast.makeText(context, "下载完成", IToast.LENGTH_SHORT).show();
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
					IToast.makeText(context, "暂停下载", IToast.LENGTH_SHORT).show();
				}
				@Override
				public void onCancel() {
					IToast.makeText(context , "已取消下载", IToast.LENGTH_SHORT).show();
				}
			});
		mDownloadManager.download(Url);
	}
    public static void Download(final Context context , final String Url) {
		Download(context,Url,new DownloadManager().getFileName(Url));
	}
}

