package com.ecodemo.magic.box.app.fragment;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.utils.Util;
import com.ecodemo.magic.box.app.widget.LinkTextView;
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
import android.util.Log;

public class FragmentHome extends Fragment {
	public static String TAG = "HOME_FRAGMENT";
	public FragmentHome() {}
    public static FragmentHome newInstance() {
        FragmentHome fragment=new FragmentHome();
        return fragment;
    }
	private TextView Daily;
	private LinkTextView announcement;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Daily = getActivity().findViewById(R.id.Daily);
		announcement = getActivity().findViewById(R.id.announcement);
		if (!Util.isNetworkConnected(getActivity())) {
			announcement.setText(Html.fromHtml((String)Util.readData(getActivity(), "message")));
			Daily.setText((String)Util.readData(getActivity(), "daily"));
			getActivity().findViewById(R.id.a).setVisibility(View.GONE);
			getActivity().findViewById(R.id.b).setVisibility(View.GONE);
			return;
		}
		new Daily().start();
		//new Messaged().start();
	}
	private class Daily extends Thread {
		@Override
		public void run() {
			OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
			Request request = new Request.Builder()
				.url("http://sentence.iciba.com/index.php?m=newGetdetail&c=dailysentence&date=" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
				.header("User-Agent", System.getProperty("http.agent"))
				.build();
			Call call = okHttpClient.newCall(request);
			try {
				Response response = call.execute();
				String responseData=response.body().string();
				JSONObject obj = new JSONObject(responseData);
				Log.e("Error", responseData);
				String note = obj.getString("note") + "\n";
				Message msg = new Message();
				msg.what = 0;
				msg.obj = note;
				handler.sendMessage(msg);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
			}
		}
	}

	private class Messaged extends Thread {
		@Override
		public void run() {
			OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
			Request request = new Request.Builder()
				.url("https://ecodemo.gitee.io/resources/mt/message.json")
				.header("User-Agent", System.getProperty("http.agent"))
				.build();
			Call call = okHttpClient.newCall(request);
			try {
				Response response = call.execute();
				String responseData=response.body().string();
				JSONObject obj = new JSONObject(responseData);
				String note = obj.getString("message");
				Message msg = new Message();
				msg.what = 1;
				msg.obj = note;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

