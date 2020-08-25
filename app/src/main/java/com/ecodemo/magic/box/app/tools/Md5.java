package com.ecodemo.magic.box.app.tools;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import com.ecodemo.magic.box.R;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.util.TypedValue;
import com.ecodemo.magic.box.app.MxActivity;

public class Md5 extends MxActivity {
	private EditText one_editor;
	private EditText tow_editor;
	private ImageView md5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.md5);
		//锁定竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		one_editor = (EditText)findViewById(R.id.one_editor);
		tow_editor = (EditText)findViewById(R.id.tow_editor);
		md5 = (ImageView)findViewById(R.id.md5);
		final View a = findViewById(R.id.a);
		final View b = findViewById(R.id.b);
		md5.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					tow_editor.setText(getMD5(one_editor.getText().toString()));
				}
			});
		md5.setOnTouchListener(new OnTouchListener(){
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					TypedValue typedValue = new TypedValue();
					getTheme().resolveAttribute(R.attr.TextColor, typedValue, true);
					TypedValue typedValue2 = new TypedValue();
					getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue2, true);
					Drawable up = getResources().getDrawable(R.drawable.conversion);
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							ImageView ib = (ImageView)view;
							a.setBackgroundColor(Md5.this.getResources().getColor(typedValue2.resourceId));
							b.setBackgroundColor(Md5.this.getResources().getColor(typedValue2.resourceId));
							up.setTint(Md5.this.getResources().getColor(typedValue2.resourceId));
							ib.setImageDrawable(up);
							break;
						case MotionEvent.ACTION_UP:
							ImageView Ib = (ImageView)view;
							a.setBackgroundColor(Md5.this.getResources().getColor(typedValue.resourceId));
							b.setBackgroundColor(Md5.this.getResources().getColor(typedValue.resourceId));
							up.setTint(Md5.this.getResources().getColor(typedValue.resourceId));
							Ib.setImageDrawable(up);
					}
					return false;
				}
			});
	}
	/**
     * 获取String的MD5值
     *
     * @param info 字符串
     * @return 该字符串的MD5值
     */
    private static String getMD5(String info) {
        try {
            //获取 MessageDigest 对象，参数为 MD5 字符串，表示这是一个 MD5 算法（其他还有 SHA1 算法等）：
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //update(byte[])方法，输入原数据
            //类似StringBuilder对象的append()方法，追加模式，属于一个累计更改的过程
            md5.update(info.getBytes("UTF-8"));
            //digest()被调用后,MessageDigest对象就被重置，即不能连续再次调用该方法计算原数据的MD5值。可以手动调用reset()方法重置输入源。
            //digest()返回值16位长度的哈希值，由byte[]承接
            byte[] md5Array = md5.digest();
            //byte[]通常我们会转化为十六进制的32位长度的字符串来使用,本文会介绍三种常用的转换方法
            return bytesToHex1(md5Array);
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
	
    private static String bytesToHex1(byte[] md5Array) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < md5Array.length; i++) {
            int temp = 0xff & md5Array[i];//TODO:此处为什么添加 0xff & ？
            String hexString = Integer.toHexString(temp);
            if (hexString.length() == 1) {//如果是十六进制的0f，默认只显示f，此时要补上0
                strBuilder.append("0").append(hexString);
            } else {
                strBuilder.append(hexString);
            }
        }
        return strBuilder.toString();
    }
}

