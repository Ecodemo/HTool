package com.ecodemo.magic.box.app.tools;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.MxActivity;
import com.ecodemo.magic.box.app.utils.AESUtil;
import com.ecodemo.magic.box.app.utils.Base64Util;
import com.ecodemo.magic.box.app.utils.Util;

public class AESEncode extends MxActivity {
	private EditText one_editor;
	private EditText tow_editor;
	private Button encode;
	private Button decode;
	private EditText key;
	private Spinner code;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aes);
		one_editor = findViewById(R.id.one_editor);
		key = findViewById(R.id.key);
		tow_editor = findViewById(R.id.tow_editor);
		encode = findViewById(R.id.encode);
		decode = findViewById(R.id.decode);
		code = findViewById(R.id.code);
		encode.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					if (code.getSelectedItemPosition() == 0) {
						try {
							tow_editor.setText(Base64Util.encode(new AESUtil().encryptAES(one_editor.getText().toString().getBytes(), Base64Util.decode(key.getText().toString()))));
						} catch (Exception e) {}
					} else {
						try {
							tow_editor.setText(Util.bytesToHex(new AESUtil().encryptAES(one_editor.getText().toString().getBytes(), Util.hexToByteArray(key.getText().toString()))));
						} catch (Exception e) {}
					}
				}
			});
		decode.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					if (code.getSelectedItemPosition() == 0) {
						try {
							tow_editor.setText(new String(new AESUtil().decryptAES(Base64Util.decode(one_editor.getText().toString()), Base64Util.decode(key.getText().toString()))));
						} catch (Exception e) {}
				    } else {
						try {
							tow_editor.setText(new String(new AESUtil().decryptAES(Util.hexToByteArray(one_editor.getText().toString()), Util.hexToByteArray(key.getText().toString()))));
						} catch (Exception e) {}
					}
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 0, Menu.NONE, "随机密钥");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case 0:
				if (code.getSelectedItemPosition() == 0) {
					try {
						key.setText(Base64Util.encode(AESUtil.initKey(256)));
					} catch (Exception e) {}
				} else {
					try {
						key.setText(Util.bytesToHex(AESUtil.initKey(256)));
					} catch (Exception e) {}
				}
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}

