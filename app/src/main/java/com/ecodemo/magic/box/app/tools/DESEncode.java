package com.ecodemo.magic.box.app.tools;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.MxActivity;
import com.ecodemo.magic.box.app.utils.Base64Util;
import com.ecodemo.magic.box.app.utils.DESUtil;

public class DESEncode extends MxActivity {
	private EditText one_editor;
	private EditText tow_editor;
	private Button encode;
	private Button decode;
	private EditText key;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.des);
		one_editor = findViewById(R.id.one_editor);
		key = findViewById(R.id.key);
		tow_editor = findViewById(R.id.tow_editor);
		encode = findViewById(R.id.encode);
		decode = findViewById(R.id.decode);
		encode.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					try
					{
						tow_editor.setText(Base64Util.encode(new DESUtil().encryptDES(one_editor.getText().toString().getBytes(), key.getText().toString().getBytes())));
					}
					catch (Exception e)
					{}
				}
			});
		decode.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					try
					{
						tow_editor.setText(new String(new DESUtil().decryptDES(Base64Util.decode(one_editor.getText().toString()), key.getText().toString().getBytes())));
					}
					catch (Exception e)
					{}
				}
			});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(Menu.NONE, 0, Menu.NONE, "随机密钥");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case 0:
				try
				{
					key.setText(Base64Util.encode(DESUtil.initKey()));
				}
				catch (Exception e)
				{}
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}

