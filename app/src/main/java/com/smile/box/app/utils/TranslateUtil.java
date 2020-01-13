package com.smile.box.app.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Executors;
import org.json.JSONArray;
public class TranslateUtil
{
    public static final String GOOGLE_TRANSLATE_BASE_URL = "https://translate.google.cn/"; // 不需要翻墙即可使用
//    public static final String TRANSLATE_SINGLE_URL = "https://translate.google.cn/translate_a/single?client=gtx&sl=en&tl=zh&dt=t&q=Do%20not%20work%20overtime%20tonight";
    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
    private static String LAN_AUTO = "auto";
    public void translate(Context context, String sourceLan, String targetLan, String content, TranslateCallback callback)
	{
        TranslateTask task = new TranslateTask(context, sourceLan, targetLan, content, callback);
        task.executeOnExecutor(Executors.newCachedThreadPool());
    }

    /**
     * 使用异步任务来翻译，翻译完成后回调callback
     */
    class TranslateTask extends AsyncTask<Void, Void, String>
	{
        String sourceLan;
        String targetLan;
        String content;
        Context context;
        TranslateCallback callback;
		private ProgressDialog dialog;
        TranslateTask(Context context, String sourceLan, String targetLan, String content, TranslateCallback callback)
		{
            this.context = context;
            this.content = content;
            this.callback = callback;
            this.sourceLan = sourceLan;
            this.targetLan = targetLan;
        }

        @Override
        protected String doInBackground(Void... params)
		{
			String result = "";
            if (content == null || content.equals(""))
			{
                return result;
            }
            try
			{
                String googleResult = "";
                // 新建一个URL对象
                URL url = new URL(getTranslateUrl(sourceLan, targetLan, content));
                // 打开一个HttpURLConnection连接
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                // 设置连接主机超时时间
                urlConn.setConnectTimeout(5 * 1000);
                //设置从主机读取数据超时
                urlConn.setReadTimeout(5 * 1000);
                // 设置是否使用缓存  默认是true
                urlConn.setUseCaches(false);
                // 设置为Post请求
                urlConn.setRequestMethod("GET");
                //urlConn设置请求头信息
                urlConn.setRequestProperty("User-Agent", USER_AGENT);
                //设置请求中的媒体类型信息。
//            urlConn.setRequestProperty("Content-Type", "application/json");
                //设置客户端与服务连接类型
//            urlConn.addRequestProperty("Connection", "Keep-Alive");
                // 开始连接
                urlConn.connect();
                // 判断请求是否成功
                int statusCode = urlConn.getResponseCode();
                if (statusCode == 200)
				{
                    // 获取返回的数据
                    googleResult = streamToString(urlConn.getInputStream());
                }
                // 关闭连接
                urlConn.disconnect();

                // 处理返回结果，拼接
                JSONArray jsonArray = new JSONArray(googleResult).getJSONArray(0);
                for (int i = 0; i < jsonArray.length(); i++)
				{
                    result += jsonArray.getJSONArray(i).getString(0);
                }
            }
			catch (Exception e)
			{
                e.printStackTrace();
                result = "";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result)
		{
            if (dialog != null && dialog.isShowing())
			{
                dialog.dismiss();
            }
            if (callback != null)
			{
                callback.onTranslateDone(result);
            }
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute()
		{
			dialog = ProgressDialog.show(context, "正在翻译", null, true, false);
		}
    }

    public void translate(Context context, String targetLan, String content, TranslateCallback callback)
	{
        translate(context, LAN_AUTO, targetLan, content, callback);
    }


    private static String getTranslateUrl(String sourceLan, String targetLan, String content)
	{
        try
		{
            return GOOGLE_TRANSLATE_BASE_URL + "translate_a/single?client=gtx&sl=" + sourceLan + "&tl=" + targetLan + "&dt=t&q=" + URLEncoder.encode(content, "UTF-8");
        }
		catch (Exception e)
		{
            return GOOGLE_TRANSLATE_BASE_URL + "translate_a/single?client=gtx&sl=" + sourceLan + "&tl=" + targetLan + "&dt=t&q=" + content;
        }
	}

	/**
	 * 翻译回调
	 */
	public interface TranslateCallback
	{
		public void onTranslateDone(String result);
	}

	public static String streamToString(InputStream is)
	{
        try
		{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1)
			{
                out.write(buffer, 0, len);
            }
            out.close();
            is.close();
            byte[] byteArray = out.toByteArray();
            return new String(byteArray);
        }
		catch (Exception e)
		{
            return null;
        }
    }
}

