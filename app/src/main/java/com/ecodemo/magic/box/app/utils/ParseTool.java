package com.ecodemo.magic.box.app.utils;
import android.content.Context;
import android.os.Environment;
import java.io.File;
import org.apache.commons.io.FileUtils;

public class ParseTool {
	private static String prefix = "MxBox    ";
	public static String parse(byte[] b) throws Exception {
		int prefixLength = prefix.getBytes().length;
		byte[] t = new byte[b.length - prefixLength];
		for (int i = 0;i < t.length;i++) {
			t[i] = b[16 + i];
		}
		byte[] decode = AESUtil.decryptAES(t, Base64Util.decode("qQzp9xlAK3IsQOlmWHT2aXC8QxHNA4LMCd9SAP/Z4cs="));
		return new String(decode);
	}

	public static void encode(Context context, String file) {
		try {
			File f = new File(file);
			byte[] bytes = FileUtils.readFileToByteArray(f);
			byte[] out = AESUtil.encryptAES(bytes, Base64Util.decode("qQzp9xlAK3IsQOlmWHT2aXC8QxHNA4LMCd9SAP/Z4cs="));
			byte[] start = prefix.getBytes();
			byte[] tmp = byteMerger(start, out);
			FileUtils.writeByteArrayToFile(new File(Environment.getExternalStoragePublicDirectory("小木匣") + "/run/", f.getName()),  tmp);
		} catch (Exception ex) {}
	}

	public static byte[] byteMerger(byte[] b_1, byte[] b_2) {  
        byte[] b_3 = new byte[b_1.length + b_2.length];  
        System.arraycopy(b_1, 0, b_3, 0, b_1.length);  
        System.arraycopy(b_2, 0, b_3, b_1.length, b_2.length);  
        return b_3;  
    }
}
