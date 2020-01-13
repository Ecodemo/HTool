package com.smile.box.app.utils;
import java.io.File;
import java.io.FileInputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.io.InputStream;
import android.content.Context;
public class PropertiesLoader {
    private final Properties properties;
	private static Context context;
    public PropertiesLoader(Context context) {
		this.context = context;
        properties = loadProperties();
    }
    /**
     *  单例模式
     */
    private static class LazyHolder{
        private static  PropertiesLoader INSTANCE;
    }
    public static PropertiesLoader getInstance(Context context) {
		LazyHolder.INSTANCE = new PropertiesLoader(context);
        return LazyHolder.INSTANCE;
    }
    /**
     * 取出Property，但以System的Property优先,取不到返回空字符串.
     */
    private String getValue(String key) {
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        return "";
    }
    /**
     * 取出String类型的Property，但以System的Property优先,如果都为Null则抛出异常.
     */
    public String getProperty(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }
    /**
     * 载入多个文件, 文件路径使用Spring Resource格式.
     */
    private Properties loadProperties() {
        InputStream inpf = null;
        try {
            inpf = this.context.getAssets().open("domain.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 生成properties对象
        Properties p = new Properties();
        try {
            p.load(inpf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
}

