package com.ecodemo.magic.box.app.tools;
import com.ecodemo.magic.box.app.MxActivity;
import android.os.PersistableBundle;
import android.os.Bundle;
import com.ecodemo.magic.box.R;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import java.util.Random;
import java.util.SplittableRandom;

public class RandomActivity extends MxActivity
{
	private TextView nmb;
	private EditText start;
	private EditText end;
	private Button generate;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.random);
		nmb = findViewById(R.id.nmb);
		start = findViewById(R.id.start);
		end = findViewById(R.id.end);
		generate = findViewById(R.id.generate);
		nmb.setText(String.valueOf(new Random().nextInt(100 - 0 + 1) + 0));
		generate.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view)
				{
					int startNmb = Integer.parseInt(start.getText().toString());
					int endNmb = Integer.parseInt(end.getText().toString());
					nmb.setText(String.valueOf(new Random().nextInt(endNmb - startNmb + 1) + startNmb));
				}
			});
	}
}
