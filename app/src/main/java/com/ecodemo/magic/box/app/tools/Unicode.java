package com.ecodemo.magic.box.app.tools;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.MxActivity;
import java.util.regex.Pattern;
import android.util.Patterns;
import java.util.regex.Matcher;
import android.os.Looper;
import android.util.Log;
import java.util.List;

public class Unicode extends MxActivity
{
	private EditText one_editor;
	private EditText tow_editor;
	private Button encode;
	private Button decode;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unicode);
		//锁定竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		one_editor = findViewById(R.id.one_editor);
		tow_editor = findViewById(R.id.tow_editor);
		encode = findViewById(R.id.encode);
		decode = findViewById(R.id.decode);
		encode.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view)
				{
					tow_editor.setText(unicodeEncode(one_editor.getText().toString()));
				}
			});
		decode.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view)
				{
					tow_editor.setText(unicodeDecode(one_editor.getText().toString()));
				}
			});
	}

    public static String unicodeEncode(String string)
	{
		char[] utfBytes = string.toCharArray();
		String unicodeBytes = "";
		for (int i = 0; i < utfBytes.length; i++)
		{
			String hexB = Integer.toHexString(utfBytes[i]);
			if (hexB.length() <= 2)
			{
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + "\\u" + hexB;
		}
		return unicodeBytes;
	}

    public static String unicodeDecode(String string)
	{
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(string);
		char ch;
		while (matcher.find())
		{
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			string = string.replace(matcher.group(1), ch + "");
		}
		return string;
	}
}

