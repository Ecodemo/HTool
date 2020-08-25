package com.ecodemo.magic.box.app.fragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import com.ecodemo.magic.box.AboutActivity;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.utils.IToast;
import android.graphics.Color;
import android.preference.PreferenceActivity;
import com.ecodemo.magic.box.BrowserActivity;

public class FragmentAbout extends PreferenceFragment
{
	public static String TAG = "ABOUT_FRAGMENT";
	public FragmentAbout() {}
    public static FragmentAbout newInstance(){
        FragmentAbout fragment=new FragmentAbout();
        return fragment;
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_about, null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting_about);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		((ListView)getActivity().findViewById(android.R.id.list)).setDividerHeight(0);
		((SwitchPreference)findPreference("dark_theme")).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference p1, Object p2)
				{
					getActivity().getWindow().setWindowAnimations(android.R.style.Animation_Toast);
					getActivity().recreate();
					return true;
				}
			});
		findPreference("q_group").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				@Override
				public boolean onPreferenceClick(Preference p1)
				{
					try
					{
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqapi://card/show_pslcard?src_type=internal&version=1&uin=724862228&card_type=group&source=qrcode")));
					}
					catch (Exception e)
					{
						IToast.makeText(getActivity(), "未安装QQ或版本过旧", IToast.LENGTH_SHORT).show();
					}
					return true;
				}
			});
		findPreference("bug").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				@Override
				public boolean onPreferenceClick(Preference p1)
				{
					Intent intent = new Intent(getActivity(), BrowserActivity.class);
					intent.setData(Uri.parse("https://support.qq.com/product/194267"));
					startActivity(intent);
					return true;
				}
			});
		findPreference("about").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				@Override
				public boolean onPreferenceClick(Preference p1)
				{
					startActivity(new Intent(getActivity(), AboutActivity.class));
					return true;
				}
			});
	}

	public static void setCurrentTheme(Context context)
	{
		boolean current = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("dark_theme", false);
		int style = current ? R.style.BlackTheme : R.style.AppTheme;
		context.setTheme(style);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			//获取窗口区域
			Window window = ((Activity)context).getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.getDecorView().setSystemUiVisibility(current ? View.SYSTEM_UI_FLAG_LAYOUT_STABLE : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			if(context instanceof PreferenceActivity)
			{
				window.setStatusBarColor(current ? Color.BLACK: Color.WHITE);
			}
        }
	}
}
