package com.ecodemo.magic.box.app.tools;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.MxActivity;
import java.security.MessageDigest;

public class SHA1 extends MxActivity
{
	private EditText one_editor;
	private EditText tow_editor;
	private ImageView sha1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sha1);
		one_editor = findViewById(R.id.one_editor);
		tow_editor = findViewById(R.id.tow_editor);
		sha1 = findViewById(R.id.sha1);
		final View a = findViewById(R.id.a);
		final View b = findViewById(R.id.b);
		sha1.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					try
					{
						tow_editor.setText(shaEncode(one_editor.getText().toString()));
					}
					catch (Exception e)
					{}
				}
			});
		sha1.setOnTouchListener(new OnTouchListener(){
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
							a.setBackgroundColor(SHA1.this.getResources().getColor(typedValue2.resourceId));
							b.setBackgroundColor(SHA1.this.getResources().getColor(typedValue2.resourceId));
							up.setTint(SHA1.this.getResources().getColor(typedValue2.resourceId));
							ib.setImageDrawable(up);
							break;
						case MotionEvent.ACTION_UP:
							ImageView Ib = (ImageView)view;
							a.setBackgroundColor(SHA1.this.getResources().getColor(typedValue.resourceId));
							b.setBackgroundColor(SHA1.this.getResources().getColor(typedValue.resourceId));
							up.setTint(SHA1.this.getResources().getColor(typedValue.resourceId));
							Ib.setImageDrawable(up);
					}
					return false;
				}
			});
	}
	
    public static String shaEncode(String inStr) throws Exception {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
