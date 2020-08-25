package com.ecodemo.magic.box.app.tools;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.MxActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.view.LayoutInflater;

public class Ping extends MxActivity
{
	private EditText ping;
	private ListView list;
	private Button run;
	private Ping.PingAdapter adapter;
	private List<PingItem> items = new ArrayList<>();
	private Pattern pattern = Pattern.compile("(?<=from\\s).*?(?=\\:)");
	private Pattern time = Pattern.compile("(?<=time=).*?(?=\\s)");
	private Pattern icmp_seq = Pattern.compile("(?<=icmp_seq=).*?(?=\\s)");
	private Pattern ttl = Pattern.compile("(?<=ttl=).*?(?=\\s)");
	private Pattern size = Pattern.compile(".*(?= bytes)");
	private Pattern end = Pattern.compile("(?<=\\=\\s).*?(?=\\sms)");
	private Process pro;
	private Thread thread;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ping);
		ping = findViewById(R.id.ping);
		list = findViewById(R.id.list);
		run = findViewById(R.id.run);
		adapter = new PingAdapter(this, items);
		list.setAdapter(adapter);
		run.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view)
				{
					if (thread != null && thread.isAlive())
					{
						pro.destroy();
						run.setText("执行");
						return;
					}
					items.clear();
					adapter.notifyDataSetChanged();
					thread = new Thread(new Runnable(){
							@Override
							public void run()
							{
								try
								{
									String line = null;
								    pro = Runtime.getRuntime().exec("ping -c 10 " + ping.getText().toString());
									BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream()));
									while ((line = buf.readLine()) != null)
									{
										Matcher matcher = pattern.matcher(line);
										if (matcher.find())
										{
											Ping.PingItem item = new PingItem();
											Matcher s = size.matcher(line);
											if (s.find())
											{
												Matcher i = icmp_seq.matcher(line);
												if (i.find())
												{
													Matcher t = ttl.matcher(line);
													if (t.find())
													{
														item.setContent(String.format("来自 %s \n序列 %s, 大小 %s 字节, TTL %s", matcher.group(), i.group(), s.group(), t.group()));
													}
												}
											}
											Matcher m = time.matcher(line);
											while (m.find())
											{
												item.setTime(String.format("%s 毫秒", m.group()));
											}
											items.add(item);
										}
										else
										{
											Matcher e = end.matcher(line);
											if (e.find())
											{
												String[] p = e.group().split("/");
												Ping.PingItem i = new PingItem();
												i.setContent(String.format("完成\n最小%s \\ 平均 %s \\ 最大 %s \\ 平均差值 %s ms", p[0], p[1], p[2], p[3]));
												i.setTime("");
												items.add(i);
												Ping.this.runOnUiThread(new Runnable(){
														@Override
														public void run()
														{
															run.setText("执行");
														}
													});
											}
										}
										Ping.this.runOnUiThread(new Runnable(){
												@Override
												public void run()
												{
													adapter.notifyDataSetChanged();
													list.setSelection(list.getBottom());  
												}
											});
									}
							    }
								catch (Exception ex)
								{
									ex.printStackTrace();
								}
							}
						});
					thread.start();
					run.setText("停止");
				}
			});
	}


	class PingAdapter extends ArrayAdapter<PingItem>
	{
		public PingAdapter(Context context, List<PingItem> list)
		{
			super(context, 0, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ping_item, parent, false);
			PingItem item = getItem(position);
			TextView content = view.findViewById(R.id.content);
			TextView time = view.findViewById(R.id.time);
			content.setText(item.getContent());
			time.setText(item.getTime());
			return view;
		}
	}

	private class PingItem
	{
		private String content;
		private String time;


		public void setContent(String content)
		{
			this.content = content;
		}

		public String getContent()
		{
			return content;
		}

		public void setTime(String time)
		{
			this.time = time;
		}

		public String getTime()
		{
			return time;
		}
	}
}
