package com.ecodemo.magic.box.app;
import android.os.Bundle;
import com.ecodemo.magic.box.R;
import android.widget.TextView;

public class ErrorActivity extends MxActivity
{
	private TextView error;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.error);
	    error = findViewById(R.id.error);
		error.setHorizontallyScrolling(true);
		error.setText(getIntent().getStringExtra("error"));
	}

	@Override
	public void finish()
	{
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}
}
