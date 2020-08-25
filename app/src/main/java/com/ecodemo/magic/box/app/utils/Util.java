package com.ecodemo.magic.box.app.utils;

import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.util.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

import android.content.pm.Signature;
import android.app.ActivityManager;

public class Util {

	/** 
	 * 字节转十六进制 
	 * @param b 需要进行转换的byte字节 
	 * @return  转换后的Hex字符串 
	 */  
	public static String byteToHex(byte b){
		String hex = Integer.toHexString(b & 0xFF);
		if(hex.length() < 2){
			hex = "0" + hex;
		}  
		return hex;
	}
	
	/** 
	 * 字节数组转16进制 
	 * @param bytes 需要转换的byte数组 
	 * @return  转换后的Hex字符串 
	 */  
	public static String bytesToHex(byte[] bytes) {  
		StringBuffer sb = new StringBuffer();  
		for(int i = 0; i < bytes.length; i++) {  
			String hex = Integer.toHexString(bytes[i] & 0xFF);  
			if(hex.length() < 2){  
				sb.append(0);  
			}  
			sb.append(hex);  
		}  
		return sb.toString();  
	}  
	
	/** 
	 * Hex字符串转byte 
	 * @param inHex 待转换的Hex字符串 
	 * @return  转换后的byte 
	 */  
	public static byte hexToByte(String inHex){  
		return (byte)Integer.parseInt(inHex,16);  
	}  
	
	/** 
	 * hex字符串转byte数组 
	 * @param inHex 待转换的Hex字符串 
	 * @return  转换后的byte数组结果 
	 */  
	public static byte[] hexToByteArray(String inHex){  
		int hexlen = inHex.length();  
		byte[] result;  
		if (hexlen % 2 == 1){  
			//奇数  
			hexlen++;  
			result = new byte[(hexlen/2)];  
			inHex="0"+inHex;  
		}else {  
			//偶数  
			result = new byte[(hexlen/2)];  
		}  
		int j=0;
		for (int i = 0; i < hexlen; i+=2){
			result[j]=hexToByte(inHex.substring(i,i+2));
			j++;
		}
		return result;
	}
	
	public static boolean isNetworkConnected(Context context)
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null)
			{
				//mNetworkInfo.isAvailable();
				return true;//有网
			}
		}
		return false;//没有网
	}
	/**
	 * 检查手机上是否安装了指定的软件
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAvilible(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		List<String> packageNames = new ArrayList<String>();

		if (packageInfos != null) {
			for (int i = 0; i < packageInfos.size(); i++) {
				String packName = packageInfos.get(i).packageName;
				packageNames.add(packName);
			}
		}
		// 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
		return packageNames.contains(packageName);
	}
    public static void LAPP(Context context)
    {
        byte[] bytes = {51,52,58,70,48,58,66,66,58,53,56,58,55,55,58,66,51,58,55,70,58,52,56,58,49,68,58,48,49,58,53,54,58,69,53,58,50,52,58,53,69,58,48,50,58,50,67};
        try
        {  
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);  
            Signature[] signs = packageInfo.signatures;  
            Signature sign = signs[0];  
            int code = sign.hashCode();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sign.toByteArray());
            byte[] block = md.digest();
            StringBuffer buf = new StringBuffer();
            int len = block.length;
            for (int i = 0; i < len; i++)
            {
                char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
                int high = ((block[i] & 0xf0) >> 4);  
                int low = (block[i] & 0x0f);  
                buf.append(hexChars[high]);  
                buf.append(hexChars[low]);
                if (i < len - 1)
                {  
                    buf.append(":");
                }
            }
            if (!buf.toString().equals(new String(bytes)) && code/8 != 114990206)
            {
                System.exit(0);
            }
        }
        catch (Exception e)
        {  
            e.printStackTrace();  
        }
    }
	public static String getParam(String url, String name) {
		Map<String,String> map = new HashMap<String,String>();
		int a = 0;
		int b = 0;
		url = url.substring(url.indexOf("?") + 1);
		String[] urls = url.split("\\&");
		for (String u : urls) {
			String[] urls_2 = u.split("=");
			for(int i = 0;i<urls_2.length;i++)
			{
				if(i%2==0)
				{
					a = i;
				}else{
					b = i;
				}
				map.put(urls_2[a],urls_2[b]);
			}
		}

		for (Map.Entry<String,String> str : map.entrySet()) {
			if (str.getKey().equals(name)) {
				return str.getValue();
			}
		}
		return null;
	}

	public static <T>void saveListData(Context context, List<T> value, String tag) {
        File file = context.getFilesDir();
        File Cache = new File(file, tag);
        try {
            ObjectOutputStream outputStream =
				new ObjectOutputStream(new FileOutputStream(Cache));
            outputStream.writeObject(value);
        } catch (IOException e) {
            e.printStackTrace();
			Log.e("Error",e.toString());
        }
    }
	public static <T>List<T> readListData(Context context, String tag) {
        File file = context.getFilesDir();
       	Object value = null;
		File cache = new File(file, tag);
		if (!cache.exists()) {
			return null;
		}
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(cache));
			value = inputStream.readObject();
			return (List<T>)value;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error",e.toString());
		}
        return null;
    }

	public static void saveData(Context context, Object value, String tag) {
        File file = context.getFilesDir();
        File Cache = null;
        Cache = new File(file, tag);
        try {
            ObjectOutputStream outputStream =
				new ObjectOutputStream(new FileOutputStream(Cache));
            outputStream.writeObject(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public static Object readData(Context context, String tag) {
        File file = context.getFilesDir();
       	Object value = null;
		File cache = new File(file, tag);
		if (!cache.exists()) {
			return null;
		}
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(cache));
			value = inputStream.readObject();
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }

	public static String subString(String str, String strStart, String strEnd) {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }


	public static String ReadTxt(File FilePath, String Encode) {
		String t = FilePath.toString();
		StringBuffer buffer = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(t);
			InputStreamReader isr = new InputStreamReader(fis, Encode);
			Reader in = new BufferedReader(isr);
			int ch;
			while ((ch = in.read()) > -1) {
				buffer.append((char)ch);
			}
			in.close();
			String a = buffer.toString();
			return a;
		} catch (IOException e) {
			return "Read failure";
		}
	}
	public static void WriterTxt(File FilePath, String Content) {
        try {
			String a = FilePath.toString();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(a), true));
            writer.write(Content);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getHtmlSource(String url) {  
        StringBuffer stb=new StringBuffer();  
        try {  
            URLConnection uc=new URL(url).openConnection();  
            BufferedReader br=new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String temp=null;  
            while ((temp = br.readLine()) != null) {  
                stb.append(temp).append("\n");  
            }  
            br.close();  

        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return stb.toString();  

    }  
	public static boolean isWifi(Context mContext) {  
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext  
            .getSystemService(Context.CONNECTIVITY_SERVICE);  
		NetworkInfo info= connectivityManager.getActiveNetworkInfo();  
		if (info != null  
            && info.getType() == ConnectivityManager.TYPE_WIFI) {  
			return true;  
		}  
		return false;  
	}  

	public static String turn(boolean o, String str, String Coding) throws UnsupportedEncodingException {
		if (o) {
			return URLDecoder.decode(str, Coding);
		} else {
			return URLEncoder.encode(str, Coding);
		}
	}
	
	
	public static String readAssetsTxt(Context context, String fileName) {
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
			return text;
        } catch (IOException e) {
            // Should never happen!
			// throw new RuntimeException(e);
            e.printStackTrace();
        }
        return "读取错误，请检查文件名";
    }


	private static final boolean isChinese(char c) {  
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
			|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
			|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
			|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
			|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
			|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
            return true;  
        }  
        return false;  
    }  

    public static final boolean isChinese(String strName) {  
        char[] ch = strName.toCharArray();  
        for (int i = 0; i < ch.length; i++) {  
            char c = ch[i];  
            if (isChinese(c)) {  
                return true;  
            }  
        }  
        return false;  
    }  
	
	/*
     * 判断服务是否启动,context上下文对象 ，className服务的name
     */
    public static boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
			.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
			.getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }
        Log.e("OnlineService：",className);
        for (int i = 0; i < serviceList.size(); i++) {
            Log.e("serviceName：",serviceList.get(i).service.getClassName());
            if (serviceList.get(i).service.getClassName().contains(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
	}
	
	public static void OpenBrowser(Context context, String url)
	{
		try{
		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		}catch(Exception e){
			IToast.makeText(context, "未安装相关应用", IToast.LENGTH_SHORT).show();
		}
	}
}

