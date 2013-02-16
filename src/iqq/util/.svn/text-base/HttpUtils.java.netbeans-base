/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.zip.GZIPInputStream;

/**
 *已经使用HttpService代替，不使用单例访问网络
 * @author chenzhihui
 */
public class HttpUtils {

    private static final String refer = "http://d.web2.qq.com/proxy.html?v=20110331002&callback=1&id=3";
    private static String cookie = "";
    private static String contents = "";
    private static int timeoutCount = 0;
    private static int errorCount = 0;
    private static boolean isTimeout = true;

    public static String getContents() {
        return contents;
    }

    public static synchronized void setContents(String contents) {
        HttpUtils.contents = contents;
    }

    public static String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        HttpUtils.cookie = cookie;
    }

    // HTTP 消息发送
    public static synchronized String sendHttpMessage(String url, String method) {
        timeoutCount = 0;
        isTimeout = false;
        //Log.println("Cookie:" + cookie);
        //Log.println("Contents:" + contents);
        HttpURLConnection conn = null;
        try {
            conn = connect(url, method);
            checkResponseCode(conn);
            while (isTimeout) {
                conn = connect(url, method);
                checkResponseCode(conn);
            }

            if (conn.getHeaderFields().get("Set-Cookie") != null) {
                for (String s : conn.getHeaderFields().get("Set-Cookie")) {
                    cookie += s;
                }
            }

            InputStream ins = conn.getInputStream();

            // 处理GZIP压缩的
            if (null != conn.getHeaderField("Content-Encoding")
                    && conn.getHeaderField("Content-Encoding").equals("gzip")) {
                byte[] b = null;
                GZIPInputStream gzip = new GZIPInputStream(ins);
                byte[] buf = new byte[1024 * 8];
                int num = -1;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                    baos.write(buf, 0, num);
                }
                b = baos.toByteArray();
                baos.flush();
                baos.close();
                gzip.close();
                ins.close();
                return new String(b).trim();
            }

            String charset = "UTF-8";
            InputStreamReader inr = new InputStreamReader(ins, charset);
            BufferedReader br = new BufferedReader(inr);

            String line = "";
            StringBuffer sb = new StringBuffer();
            do {
                sb.append(line);
                line = br.readLine();
            } while (line != null);
            Log.println("response=" + sb);
            return sb.toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            ErrorMessage.show("无法连接互联网!");
            System.exit(0);
        } catch (MalformedURLException e) {
            if (errorCount > 10) {
                e.printStackTrace();
                ErrorMessage.show("无法连接互联网!");
                System.exit(0);
            }
            sendHttpMessage(url, method);
            errorCount++;

            Log.println("errorCount:" + errorCount);
            Log.println(e.getMessage());
        } catch (IOException e) {
            if (errorCount > 10) {
                e.printStackTrace();
                ErrorMessage.show("无法连接互联网!");
                System.exit(0);
            }
            sendHttpMessage(url, method);
            errorCount++;

            Log.println("errorCount:" + errorCount);
            Log.println(e.getMessage());
        }

        return null;

    }

    public static HttpURLConnection connect(String url, String method) {
        HttpURLConnection conn = null;
        try {
            Log.println("request=" + url);
            URL serverUrl = new URL(url);
            conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(60000);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            System.setProperty("sun.net.client.defaultReadTimeout", "60000");
            conn.setRequestMethod(method);// "POST" ,"GET"
            if (null != refer) {
                conn.addRequestProperty("Referer", refer);
            }

            conn.addRequestProperty("Cookie", cookie);
            conn.addRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Accept-Language", "zh-cn");
            conn.addRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.addRequestProperty("Cache-Control", "no-cache");
            conn.addRequestProperty("Accept-Charset", "UTF-8;");
            conn.addRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727)");
            if (method.equalsIgnoreCase(Method.GET)) {
                conn.setDoInput(true);
                conn.connect();
            } else if (method.equalsIgnoreCase(Method.POST)) {
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();
                conn.getOutputStream().write(contents.getBytes());
            } else {
                throw new RuntimeException("your method is not implement");
            }
        } catch (Exception e) {
        }
        return conn;
    }

    public static void checkResponseCode(final HttpURLConnection conn) {
        if (conn == null) {
            return;
        }
        if (timeoutCount > 10) {
            Log.println("-----------------");
            ErrorMessage.show("网络中断，请重登录！");
            ThreadUtil.shutdown();
            System.exit(0);
        }
        timeoutCount++;
        try {
            InputStream is = conn.getInputStream();
            if (is == null) {
                return;
            }
            InputStream isrs = conn.getErrorStream();
            if (isrs != null) {
                Log.println("conn.getErrorStream():" + isrs);
                return;
            }

            int status = conn.getResponseCode();
            System.out.println(status);
            switch (status) {
                case java.net.HttpURLConnection.HTTP_GATEWAY_TIMEOUT://504
                    Log.println("timeoutCount:" + timeoutCount);
                    Log.println("网络超时! status：" + status);
                case java.net.HttpURLConnection.HTTP_FORBIDDEN://403
                    Log.println("网络禁止访问! status：" + status);
                case java.net.HttpURLConnection.HTTP_INTERNAL_ERROR://500
                    Log.println("连接错误，无法连接WebQQ或不存在! status：" + status);
                case java.net.HttpURLConnection.HTTP_NOT_FOUND://404
                    Log.println("连接地址不存在! status：" + status);
                case java.net.HttpURLConnection.HTTP_OK:
                    Log.println("Connect OK!");
                    errorCount = 0;
                    isTimeout = false;
            }

            if (java.net.HttpURLConnection.HTTP_OK != status) {
                Log.println("连接有误，正在重试!status:" + status);
            }
        } catch (Exception e) { //SocketTimeoutException
            Log.println("timeoutCount:" + timeoutCount);
            Log.println("网络超时，正在重试! HttpUtils cdoe line 170 SocketTimeoutException：" + e.getMessage());
        }

    }
}
