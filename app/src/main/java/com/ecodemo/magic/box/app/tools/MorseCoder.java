package com.ecodemo.magic.box.app.tools;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.MxActivity;
import com.ecodemo.magic.box.app.utils.IToast;
import com.ecodemo.magic.box.app.utils.MorseCoderUtil;
import android.os.Looper;

public class MorseCoder extends MxActivity
{
	private EditText one_editor;
	private EditText tow_editor;
	private Button encode;
	private Button decode;
	private MorseCoderUtil morseCoder;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case 0:
					tow_editor.setText((String)msg.obj);
					break;
				case 1:
					IToast.makeText(MorseCoder.this, "ớ ₃ờ你这可能不是摩斯电码", IToast.LENGTH_SHORT).show();
					break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.morse);
		one_editor = findViewById(R.id.one_editor);
		tow_editor = findViewById(R.id.tow_editor);
		encode = findViewById(R.id.encode);
		decode = findViewById(R.id.decode);
		morseCoder = new MorseCoderUtil();
		encode.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view)
				{
					new Thread(new Runnable(){
							@Override
							public void run()
							{
								String mos = morseCoder.encode(one_editor.getText().toString());
								Message msg = new Message();
								msg.what = 0;
								msg.obj = mos;
								handler.sendMessage(msg);
							}
						}).start();
				}
			});
		decode.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view)
				{
					new Thread(new Runnable(){
							@Override
							public void run()
							{
								try
								{
									String mos = morseCoder.decode(one_editor.getText().toString());
									Message msg = new Message();
									msg.what = 0;
									msg.obj = mos;
									handler.sendMessage(msg);
								}
								catch (Exception ex)
								{
									handler.sendEmptyMessage(1);
								}
							}
						}).start();
				}
			});
	}
}

