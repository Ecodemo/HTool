package com.smile.box.app.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WhoisUtil {
    private static final int DEFAULT_PORT = 43;//连接默认端口43
    /**
     * 单例模式
     */
    private static class LazyHolder {
        private static final WhoisUtil INSTANCE = new WhoisUtil();
    }
    /**
     * 获取实例
     * @return
     */
    public static WhoisUtil getInstance() {
        return LazyHolder.INSTANCE;
    }
    /**
     * 查询域名
     * @param domain
     * @return
     * @throws Exception
     */
    public String queryDoamin(String domain,String server)throws Exception{
        StringBuilder sb = new StringBuilder();
        Socket socket = null;
        try {
            socket = new Socket(server, DEFAULT_PORT);
            String lineSeparator = "\n";
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(domain);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line + lineSeparator);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
			socket.close();
        }
        return sb.toString();
    }

    /**
     * 获取后缀
     * @param domain
     */
    public static String getTld(String domain) {
        final int index;
        String tld = "";
        if(domain != null && !"".equals(domain) && (index = domain.lastIndexOf('.')+1) != 0) {
			tld = domain.substring(index);
        }
        return tld;
    }
}
