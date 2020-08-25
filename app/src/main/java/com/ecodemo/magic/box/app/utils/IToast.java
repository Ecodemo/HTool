package com.ecodemo.magic.box.app.utils;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.ecodemo.magic.box.*;
import android.util.Log;

public class IToast {
	
	private static Toast toast;
	public static final int LENGTH_LONG = Toast.LENGTH_LONG;
	public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
	
    public static IToast makeText(Context context, CharSequence content,int durtion) {
        toast = Toast.makeText(context,content,durtion);
        toast.setGravity(Gravity.CENTER, 0, 0);
		return new IToast();
    }
	
    public void show() {
		toast.show();
    }
}  


