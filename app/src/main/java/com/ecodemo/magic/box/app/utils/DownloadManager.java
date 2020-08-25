package com.ecodemo.magic.box.app.utils;

import android.os.*;
import android.text.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * 下载管理器，断点续传
 *
 * @author JianChen
 */
public class DownloadManager {

    private String DEFAULT_FILE_DIR;//默认下载目录
    private Map<String, DownloadTask> mDownloadTasks;//文件下载任务索引，String为url,用来唯一区别并操作下载的文件
    private static DownloadManager mInstance;
    private static final String TAG = "DownloadManager";
	public static int thread_count = 32;
	private String fileName;

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
    /**
     * 下载文件
     */
    public void download(String... urls) {
        //单任务开启下载或多任务开启下载
        for (int i = 0, length = urls.length; i < length; i++) {
            String url = urls[i];
            if (mDownloadTasks.containsKey(url)) {
				mDownloadTasks.get(url).setThreadCount(thread_count);
                mDownloadTasks.get(url).start();
            }
        }
    }


    // 获取下载文件的名称
    public String getFileName(String str) {
		String str3 = "";
		Object obj = null;
		try {
			URL url = new URL(str);
			URLConnection openConnection = url.openConnection();
			if (openConnection == null) {
				return (String) null;
			}
			Map headerFields = openConnection.getHeaderFields();
			if (headerFields == null) {
				return (String) null;
			}
			Set keySet = headerFields.keySet();
			if (keySet == null) {
				return (String) null;
			}
			Iterator it = keySet.iterator();
			do {
				if (!it.hasNext()) {
					break;
				}
				for (String bytes : (List)headerFields.get(it.next())) {
					try {
						String s = new String(bytes.getBytes("ISO-8859-1"), "GBK");
						int indexOf = s.indexOf("filename");
						if (indexOf >= 0) {
							s = s.substring(indexOf + "filename".length());
							str3 = s.substring(s.indexOf("=") + 1);
							obj = 1;
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			} while (obj == null);
			if (str3 == null || "".equals(str3)) {
				str3 = str.substring(str.lastIndexOf("/") + 1);
			}

			if (str3.indexOf(".") == -1 || str3.equals("null")) {
				HttpURLConnection hc =(HttpURLConnection)url.openConnection();
				hc.setInstanceFollowRedirects(false);
				str3 = hc.getHeaderField("location");
			}
			str3 = str3.replaceAll("\"", "");
			return str3;
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}

		return null;
	}

    /**
	 * 暂停
     */
	public void pause(String... urls) {
		//单任务暂停或多任务暂停下载
        for (int i = 0, length = urls.length; i < length; i++) {
			String url = urls[i];
            if (mDownloadTasks.containsKey(url)) {
				mDownloadTasks.get(url).pause();
			}
		}
	}

    /**
	 * 取消下载
     */
	public void cancel(String... urls) {
		//单任务取消或多任务取消下载
        for (int i = 0, length = urls.length; i < length; i++) {
			String url = urls[i];
            if (mDownloadTasks.containsKey(url)) {
				mDownloadTasks.get(url).cancel();
			}
		}
	}

    /**
	 * 添加下载任务
     */
	public void add(String url, DownloadListener l) {
		add(url, null, null, l);
	}

	/**
	 * 添加下载任务
     */
	public void add(String url, String filePath, int thread_count,String fileName, DownloadListener l) {
		this.thread_count = thread_count;
        add(url, filePath, fileName, l);
	}


    /**
	 * 添加下载任务
     */
	public void add(String url, String filePath, DownloadListener l) {
		add(url, filePath, null, l);
	}

    /**
	 * 添加下载任务
     */
	public void add(String url, String filePath, String fileName, DownloadListener l) {
		if (TextUtils.isEmpty(filePath)) {//没有指定下载目录,使用默认目录
			filePath = getDefaultDirectory();
		}
        if (TextUtils.isEmpty(fileName)) {
			fileName = getFileName(url);
		}
		this.fileName = fileName;
        mDownloadTasks.put(url, new DownloadTask(new FilePoint(url, filePath, fileName), l));
	}

    /**
	 * 默认下载目录
     * @return
     */
	private String getDefaultDirectory() {
		if (TextUtils.isEmpty(DEFAULT_FILE_DIR)) {
			DEFAULT_FILE_DIR = Environment.getExternalStoragePublicDirectory("小木匣")
				+ File.separator + "/Download" + File.separator;
		}
        return DEFAULT_FILE_DIR;
	}

    public static DownloadManager getInstance() {//管理器初始化
		if (mInstance == null) {
			synchronized (DownloadManager.class) {
				if (mInstance == null) {
					mInstance = new DownloadManager();
				}
			}
		}
        return mInstance;
	}

    public DownloadManager() {
		mDownloadTasks = new HashMap<>();
	}

    /**
	 * 取消下载
     */
	public boolean isDownloading(String... urls) {
		//这里传一个url就是判断一个下载任务
        //多个url数组适合下载管理器判断是否作操作全部下载或全部取消下载
        boolean result = false;
        for (int i = 0, length = urls.length; i < length; i++) {
			String url = urls[i];
            if (mDownloadTasks.containsKey(url)) {
				result = mDownloadTasks.get(url).isDownloading();
			}
		}
        return result;
	}
}

