package com.smile.box.app.tools;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.smile.box.R;
import com.smile.box.app.MxActivity;
import com.smile.box.app.utils.Base64Util;
import com.smile.box.app.utils.DESUtil;

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
}

