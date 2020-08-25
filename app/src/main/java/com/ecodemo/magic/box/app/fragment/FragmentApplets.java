package com.ecodemo.magic.box.app.fragment;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.ScriptItem;
import com.ecodemo.magic.box.app.adapter.ScriptAdapter;
import com.ecodemo.magic.box.app.widget.CustomeGridView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentApplets extends Fragment
{
	public static String TAG = "APPLETS_FRAGMENT";

	private CustomeGridView program;
	public FragmentApplets() {}
    public static FragmentApplets newInstance(){
        FragmentApplets fragment=new FragmentApplets();
        return fragment;
    }
	private List<ScriptItem> beans = new ArrayList<>();
	private ScriptAdapter adapter;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what)
			{
				case 0:
					Gson gson = new Gson();
					JsonParser jsonParser = new JsonParser();
					JsonArray jsonElements = jsonParser.parse((String)msg.obj).getAsJsonArray();
					for (JsonElement bean : jsonElements) {
						beans.add(gson.fromJson(bean, ScriptItem.class));
					}
					adapter = new ScriptAdapter(getActivity(), beans);
					program.setAdapter(adapter);
					break;
			}
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_applets, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		program = getActivity().findViewById(R.id.program);
		new Script().start();
	}
	
	private class Script extends Thread {
		@Override
		public void run() {
			OkHttpClient okHttpClient=new OkHttpClient().newBuilder().proxy(Proxy.NO_PROXY).build();
			Request request = new Request.Builder()
				.url("https://ecodemo.gitee.io/resources/mt/script.json")
				.header("User-Agent", System.getProperty("http.agent"))
				.build();
			Call call=okHttpClient.newCall(request);
			try {
				Response response = call.execute();
				Message msg = new Message();
				msg.obj = response.body().string();
				msg.what = 1;
				handler.sendMessage(msg);
			} catch (IOException e) {}
		}
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		try
		{
			getActivity().onBackPressed();
		}catch(Exception ex){
			Log.e("Error",ex.toString());
		}
	}
}
