package com.smile.box.app.fragment;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.smile.box.R;
import com.smile.box.app.utils.Util;
import com.smile.box.app.widget.LinkTextView;
import java.io.IOException;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentHome extends Fragment
{
	private TextView Daily;
	private LinkTextView announcement;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case 0:
					getActivity().findViewById(R.id.a).setVisibility(View.GONE);
					Daily.setText((String)msg.obj);
					Util.saveData(getActivity(), (String)msg.obj, "daily");
					break;
				case 1:
					Util.saveData(getActivity(), (String)msg.obj, "message");
					announcement.setText(Html.fromHtml((String)msg.obj));
					getActivity().findViewById(R.id.b).setVisibility(View.GONE);
					announcement.interceptHyperLink();
					break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Daily = getActivity().findViewById(R.id.Daily);
		announcement = getActivity().findViewById(R.id.announcement);
		if (!Util.isNetworkConnected(getActivity()))
		{
			announcement.setText(Html.fromHtml((String)Util.readData(getActivity(), "message")));
			Daily.setText((String)Util.readData(getActivity(), "daily"));
			getActivity().findViewById(R.id.a).setVisibility(View.GONE);
			getActivity().findViewById(R.id.b).setVisibility(View.GONE);
			return;
		}
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Messaged());
		executor.execute(new Daily());
		executor.shutdown();
	}
	private class Daily implements Runnable
	{
		@Override
		public void run()
		{
			//创建okHttpClient对象
			OkHttpClient okHttpClient=new OkHttpClient().newBuilder().proxy(Proxy.NO_PROXY).build();
			//创建request,首先要有一个url
			Request request=new Request.Builder().url("http://sentence.iciba.com/index.php?m=newGetdetail&c=dailysentence&date="+new SimpleDateFormat("yyyy-MM-dd").format(new Date())).build();
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
						try
						{
							final String responseData=response.body().string();
							JSONObject obj = new JSONObject(responseData);
							String note = obj.getString("note") + "\n" + obj.getString("content");
							Message msg = new Message();
							msg.what = 0;
							msg.obj = note;
							handler.sendMessage(msg);
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
					}
				});
		}
	}

	private class Messaged implements Runnable
	{
		@Override
		public void run()
		{
			//创建okHttpClient对象
			OkHttpClient okHttpClient=new OkHttpClient().newBuilder().proxy(Proxy.NO_PROXY).build();
			//创建request,首先要有一个url
			Request request=new Request.Builder().url("https://gitms.gitee.io/x-jianchen/json/message.json").build();
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
						try
						{
							final String responseData=response.body().string();
							JSONObject obj = new JSONObject(responseData);
							String note = obj.getString("message");
							Message msg = new Message();
							msg.what = 1;
							msg.obj = note;
							handler.sendMessage(msg);
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
					}
				});
		}
	}
}

