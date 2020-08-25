package com.ecodemo.magic.box.app;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.ecodemo.magic.box.R;
import android.view.MenuInflater;

public class MxActivity extends Activity {
	private LinearLayout linear;
	private TextView titleView;
	private PopupMenu popupMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCurrentTheme(this);
		ViewGroup view = findViewById(android.R.id.content);
		view.addView(View.inflate(this, R.layout.view, null));
		linear = findViewById(R.id.view);
		titleView = findViewById(R.id.title);
		findViewById(R.id.more).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					openOptionsMenu();
				}
			});
		findViewById(R.id.exit).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					finish();
				}
			});
		String title = getIntent().getStringExtra("title");
		if (!TextUtils.isEmpty(title)) {
			titleView.setText(title);
		} else {
			titleView.setText(getTitle().toString());
		}
		
		popupMenu = new PopupMenu(this, findViewById(R.id.more));
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					return onOptionsItemSelected(item);
				}
			});
		popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
				@Override
				public void onDismiss(PopupMenu menu) {
					
				}
			});
		onCreateOptionsMenu(popupMenu.getMenu());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean run = super.onCreateOptionsMenu(menu);
		showMore(menu);
		return run;
	}
	
	private void showMore(Menu menu)
	{
		if(menu.size() == 0)
		{
			findViewById(R.id.more).setVisibility(View.GONE);
			findViewById(R.id.line).setVisibility(View.GONE);
		}else{
			findViewById(R.id.more).setVisibility(View.VISIBLE);
			findViewById(R.id.line).setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean run = super.onPrepareOptionsMenu(menu);
		showMore(menu);
		return run;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public MenuInflater getMenuInflater() {
		return popupMenu.getMenuInflater();
	}

	@Override
	public void openOptionsMenu() {
		onPrepareOptionsMenu(popupMenu.getMenu());
		if(popupMenu.getMenu().size() != 0)
		{
		    popupMenu.show();
	    }
	}
	
	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		titleView.setText(title.toString());
	}

	@Override
	public void setContentView(int layoutResID) {
		LayoutInflater.from(this).inflate(layoutResID, linear);
	}

	@Override
	public void setContentView(View view) {
		linear.removeAllViews();
		linear.addView(view);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//适配全面屏
		NotchManager.initNotchListener(this);
	}

	public LinearLayout getTitleView() {
		return findViewById(R.id.view_parent);
	}

	public static void setCurrentTheme(Context context) {
		boolean current = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("dark_theme", false);
		int style = current ? R.style.BlackTheme : R.style.AppTheme;
		context.setTheme(style);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			//获取窗口区域
			Window window = ((Activity)context).getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.getDecorView().setSystemUiVisibility(current ? View.SYSTEM_UI_FLAG_LAYOUT_STABLE : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			if (context instanceof PreferenceActivity) {
				window.setStatusBarColor(current ? Color.BLACK: Color.WHITE);
			}
        }
	}
}
