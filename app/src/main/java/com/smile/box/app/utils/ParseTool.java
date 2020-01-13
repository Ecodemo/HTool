package com.smile.box.app.utils;
import org.apache.commons.io.FileUtils;
import java.io.File;
import android.content.Context;
import android.app.usage.ExternalStorageStats;
import android.media.audiofx.EnvironmentalReverb;
import android.os.Environment;

public class ParseTool
{
	public static String parse(byte[] b) throws Exception
	{
		byte[] t = new byte[b.length - 16];
		for (int i = 0;i < t.length;i++)
		{
			t[i] = b[16 + i];
		}
		byte[] decode = AESUtil.decryptAES(t, Base64Util.decode("bUhXZFEwTU9PT2tnTnRjdQ=="));
		return new String(decode);
	}

	public static void encode(Context context, String file)
	{
		try
		{
			File f = new File(file);
			byte[] bytes = FileUtils.readFileToByteArray(f);
			byte[] out = AESUtil.encryptAES(bytes, Base64Util.decode("bUhXZFEwTU9PT2tnTnRjdQ=="));
			byte[] start = "MxScript        ".getBytes();
			byte[] tmp = byteMerger(start, out);
			FileUtils.writeByteArrayToFile(new File(Environment.getExternalStorageDirectory()+"/MToolBox/run/", f.getName()),  tmp);
		}
		catch (Exception ex)
		{}
	}

	public static byte[] byteMerger(byte[] b_1, byte[] b_2)
	{  
        byte[] b_3 = new byte[b_1.length + b_2.length];  
        System.arraycopy(b_1, 0, b_3, 0, b_1.length);  
        System.arraycopy(b_2, 0, b_3, b_1.length, b_2.length);  
        return b_3;  
    }
}
