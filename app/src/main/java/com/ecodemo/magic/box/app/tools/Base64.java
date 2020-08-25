package com.ecodemo.magic.box.app.tools;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.MxActivity;
import com.ecodemo.magic.box.app.utils.Base64Util;
import java.util.regex.Pattern;

public class Base64 extends MxActivity {
	private EditText one_editor;
	private EditText tow_editor;
	private Button encode;
	private Button decode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base64);
		//锁定竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		one_editor = findViewById(R.id.one_editor);
		tow_editor = findViewById(R.id.tow_editor);
		encode = findViewById(R.id.encode);
		decode = findViewById(R.id.decode);
		encode.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					tow_editor.setText(stringToBase64(one_editor.getText().toString()));
				}
			});
		decode.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					tow_editor.setText(base64ToString(one_editor.getText().toString()));
				}
			});
	}
	/**
     * 把base64的String码转换成正常显示的字符串
     */
    private String base64ToString(String base64) {
		if(!isBase64(base64))return "";
        byte[] decode = Base64Util.decode(base64);
        String s = new String(decode);
        return s;
    }
    /**
     * 把String的转换成base64码
     */
    private String stringToBase64(String ss) {
        byte[] bytes = ss.getBytes();
        String encode = Base64Util.encode(bytes);
        return encode;
    }

	private static boolean isBase64(String str) {
		String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
		return Pattern.matches(base64Pattern, str);
	}
}

