package com.smile.box.app.fragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.smile.box.R;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.util.Log;

public class FragmentHistory extends Fragment
{
	private ArrayList<History> beans = new ArrayList<>();
	private SimpleDateFormat format = new SimpleDateFormat("MM");
	private SimpleDateFormat format2 = new SimpleDateFormat("dd");
	private ListView listView;
	private FragmentHistory.ListAdapter adapter;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case 0:
					Gson gson = new Gson();
					JsonParser jsonParser = new JsonParser();
					JsonArray jsonElements = jsonParser.parse((String)msg.obj).getAsJsonObject().getAsJsonArray("data");
					for (JsonElement bean : jsonElements)
					{
						beans.add(gson.fromJson(bean, History.class));
						adapter.notifyDataSetChanged();
					}
					break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_history, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		listView = getActivity().findViewById(R.id.listView);
		adapter = new ListAdapter(getActivity(), beans);
		listView.setAdapter(adapter);
		new Thread(new Runnable(){
				@Override
				public void run()
				{
					OkHttpClient client = new OkHttpClient.Builder()
						.connectTimeout(5, TimeUnit.SECONDS)
						.readTimeout(5, TimeUnit.SECONDS)
						.build();
					Request request = new Request.Builder()
						.url("http://www.jiahengfei.cn:33550/port/history?dispose=detail&key=jiahengfei&month=" + format.format(new Date()) + "&day=" + format2.format(new Date()))
						.get()
						.build();
					Call call = client.newCall(request);
					call.enqueue(new Callback(){
							@Override
							public void onFailure(Call call, IOException ex)
							{

							}
							@Override
							public void onResponse(Call call, Response response) throws IOException
							{
								if (response.code() == 200)
								{
									Message msg = new Message();
									msg.what = 0;
									msg.obj = response.body().string();
									handler.sendMessage(msg);
								}
							}
						});
				}
			}).start();
	}

	public class ListAdapter extends ArrayAdapter<History>
	{
		public ListAdapter(Context context, List<History> objects)
		{
			super(context, 0, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			History history = getItem(position);
			View view = LayoutInflater.from(getContext()).inflate(R.layout.listviewitem, parent, false);
			TextView name = view.findViewById(R.id.title);
			TextView time = view.findViewById(R.id.time);
			ImageView pic = view.findViewById(R.id.pic);
			name.setText(history.getTitle());
			time.setText(String.format("——时间：%s\t\t老黄历：%s",history.getYear(), history.getLunar()));
			Glide.with(FragmentHistory.this)
					.load(TextUtils.isEmpty(history.getPic()) ? "file:///android_asset/not_pic.png": history.getPic())
					.diskCacheStrategy(DiskCacheStrategy.NONE)
					.placeholder(R.drawable.loading)
					.error(R.drawable.error)
					.into(pic);
			return view;
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
