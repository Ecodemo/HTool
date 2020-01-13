package com.smile.box;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import com.smile.box.app.fragment.FragmentAbout;
import com.smile.box.app.update.UpdateUtil;
import com.smile.box.app.utils.Util;

public class AboutActivity extends PreferenceActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		FragmentAbout.setCurrentTheme(this);
		setContentView(R.layout.about);
		getListView().setDivider(null);
		addPreferencesFromResource(R.xml.about);
		findPreference("author").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				@Override
				public boolean onPreferenceClick(Preference p1)
				{
					String[] items = {"QQ","酷安 (简尘)","Github (X-JianChen)","E-Mail"};
					AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
					builder.setTitle("联系作者");
					builder.setItems(items, new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int position)
							{
								switch(position)
								{
									case 0:
										Util.OpenBrowser(AboutActivity.this, "mqqwpa://im/chat?chat_type=wpa&uin=884938249&version=1&src_type=web&web_src=badcatu.com");
										break;
									case 1:
										Util.OpenBrowser(AboutActivity.this, "coolmarket://u/1512829");
										break;
									case 2:
										Util.OpenBrowser(AboutActivity.this, "https://github.com/x-jianchen");
										break;
									case 3:
										Util.OpenBrowser(AboutActivity.this, "mailto:jianchen9@foxmail.com");
										break;
								}
								dialog.dismiss();
							}
					}).create().show();
					return true;
				}
			});
		findPreference("update").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				@Override
				public boolean onPreferenceClick(Preference p1)
				{
					new UpdateUtil(AboutActivity.this).manual();
					return true;
				}
		});
	}
}
