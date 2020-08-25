package com.ecodemo.magic.box.app.tools;
import com.ecodemo.magic.box.app.MxActivity;
import android.os.Bundle;
import com.ecodemo.magic.box.R;
import android.widget.EditText;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import com.ecodemo.magic.box.app.utils.WhoisUtil;
import com.ecodemo.magic.box.app.utils.PropertiesLoader;
import com.ecodemo.magic.box.app.utils.IToast;
import android.os.Looper;

public class Whois extends MxActivity
{
	private EditText domain;
	private Button run;
	private EditText content;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whois);
		domain = findViewById(R.id.domain);
		run = findViewById(R.id.run);
		content = findViewById(R.id.content);
		run.setOnClickListener(new OnClickListener(){
				private String server;
				@Override
				public void onClick(View p1)
				{
					/*判断域名是否合法*/
					String tld = WhoisUtil.getTld(domain.getText().toString());
					if (tld == null || tld.equals(""))
					{
						IToast.makeText(Whois.this, "域名输入不合法", IToast.LENGTH_SHORT).show();
						return;
					}
					else
					{
						server = PropertiesLoader.getInstance(Whois.this).getProperty(tld);
						if (server == null || server.equals(""))
						{
							IToast.makeText(Whois.this, "域名输入不合法", IToast.LENGTH_SHORT).show();
							return;
						}
					}
					new Thread(new Runnable(){
							@Override
							public void run()
							{
								WhoisUtil whoisUtil = WhoisUtil.getInstance();
								try
								{
									final String domainDetail = whoisUtil.queryDoamin(domain.getText().toString(), server);
									if (domainDetail == null || "".equals(domainDetail))
									{
										IToast.makeText(Whois.this, "域名查询有误，请重新再试", IToast.LENGTH_SHORT).show();
									}
									else
									{
										Whois.this.runOnUiThread(new Runnable(){
												@Override
												public void run()
												{
													content.setText(domainDetail);
												}
											});
									}
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						}).start();
				}
			});
	}
}
