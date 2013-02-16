/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.service;

import iqq.util.Log;
import iqq.util.Method;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author chenzhihui
 */
public class HttpService {

    private static final String refer = "http://d.web2.qq.com/proxy.html?v=20110331002&callback=1&id=3";
    public StringBuilder cookie;
    private String url = "";
    private String method = Method.GET;
    private String contents = "";
    private int timeoutCount = 0;
    private int errorCount = 0;
    private boolean isTimeout = false;
    private HttpURLConnection conn = null;
    private String filename = null;

    public HttpService(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public HttpService(String url, String method, String contents) {
        this.url = url;
        this.method = method;
        this.contents = contents;
    }

    public HttpService(String url, String method, String contents, String filename) {
        this.url = url;
        this.method = method;
        this.contents = contents;
        this.filename = filename;
    }

    public String sendHttpMessage() {
        if (errorCount > 10) {
            Log.println("无法连接互联网!");
        }
        try {
            conn = connect();
            checkResponseCode(conn);
            while (isTimeout) {
                conn = connect();
                checkResponseCode(conn);
            }

            if (conn.getHeaderFields().get("Set-Cookie") != null) {
                for (String s : conn.getHeaderFields().get("Set-Cookie")) {
                    cookie.append(s);
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
            sendHttpMessage();
            errorCount++;
        } catch (MalformedURLException e) {
            sendHttpMessage();
            errorCount++;

            Log.println("errorCount:" + errorCount);
            Log.println(e.getMessage());
        } catch (IOException e) {
            sendHttpMessage();
            errorCount++;

            Log.println("errorCount:" + errorCount);
            Log.println(e.getMessage());
        }

        return null;
    }

    public InputStream getInputStream() {
        if (errorCount > 10) {
            Log.println("无法连接互联网!");
        }
        InputStream is = null;
        conn = connect();
        checkResponseCode(conn);
        while (isTimeout) {
            conn = connect();
            checkResponseCode(conn);
        }

        if (conn.getHeaderFields().get("Set-Cookie") != null) {
            for (String s : conn.getHeaderFields().get("Set-Cookie")) {
                cookie.append(s);
            }
        }
        try {
            is = conn.getInputStream();
        } catch (IOException e) {
            Logger.getLogger(HttpService.class.getName()).log(Level.SEVERE, null, e);
            errorCount++;
            getInputStream();
            Log.println("errorCount:" + errorCount);
            Log.println(e.getMessage());
        }
        return is;
    }

    public HttpURLConnection connect() {
        HttpURLConnection conn = null;
        try {
            Log.println("request=" + url);
            URL serverUrl = new URL(url);
            conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(100000);
            System.setProperty("sun.net.client.defaultConnectTimeout", "60000");
            System.setProperty("sun.net.client.defaultReadTimeout", "100000");
            conn.setRequestMethod(method);// "POST" ,"GET"
            if (null != refer) {
                conn.addRequestProperty("Referer", refer);
            }

            conn.addRequestProperty("Cookie", cookie.toString());
            conn.addRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Accept-Language", "zh-cn");
            conn.addRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.addRequestProperty("Cache-Control", "no-cache");
            conn.addRequestProperty("Accept-Charset", "UTF-8;");
            conn.addRequestProperty("Host", "d.web2.qq.com");
            conn.addRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.19 (KHTML, like Gecko) Ubuntu/12.04 Chromium/18.0.1025.168 Chrome/18.0.1025.168 Safari/535.19");
            if (method.equalsIgnoreCase(Method.GET)) {
                conn.setDoInput(true);
                conn.connect();
            } else if (method.equalsIgnoreCase(Method.POST)) {
                conn.setDoInput(true);
                conn.setDoOutput(true);
                if (filename != null) {
                    conn.setRequestProperty("Content-Type", "multipart/form-data");
                }
                conn.connect();
                conn.getOutputStream().write(contents.getBytes());
                //上传文件
                if (filename != null) {
                    File file = new File(filename);
                    FileInputStream is = new FileInputStream(file);
                    OutputStream os = conn.getOutputStream();

                    // POST
                    // Read bytes until EOF to write
                    byte[] buffer = new byte[4096];
                    int bytes_read;    // How many bytes in buffer
                    while ((bytes_read = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytes_read);
                    }
                    os.close();
                    is.close();
                    System.out.println("Upload Done...");
                }
            } else {
                throw new RuntimeException("your method is not implement");
            }
        } catch (Exception e) {
        }
        return conn;
    }

    public void checkResponseCode(final HttpURLConnection conn) {
        if (timeoutCount > 10) {
            Log.println("-----------------timeoutCount:" + timeoutCount);
            Log.println("网络中断，请重登录！");
        }
        timeoutCount++;
        if (conn == null) {
            isTimeout = true;
            return;
        }
        try {
            InputStream is = conn.getInputStream();
            if (is == null) {
                isTimeout = true;
                return;
            }
            InputStream isrs = conn.getErrorStream();
            if (isrs != null) {
                Log.println("conn.getErrorStream():" + isrs);
                isTimeout = true;
                return;
            }

            int status = conn.getResponseCode();
            switch (status) {
                case java.net.HttpURLConnection.HTTP_GATEWAY_TIMEOUT://504
                    Log.println("timeoutCount:" + timeoutCount);
                    Log.println("网络超时! status：" + status);
                    break;
                case java.net.HttpURLConnection.HTTP_FORBIDDEN://403
                    Log.println("网络禁止访问! status：" + status);
                    break;
                case java.net.HttpURLConnection.HTTP_INTERNAL_ERROR://500
                    Log.println("连接错误，无法连接WebQQ或不存在! status：" + status);
                    break;
                case java.net.HttpURLConnection.HTTP_NOT_FOUND://404
                    Log.println("连接地址不存在! status：" + status);
                    break;
                case java.net.HttpURLConnection.HTTP_OK:
                    Log.println("Connect OK!");
                    errorCount = 0;
                    isTimeout = false;
                    return;
            }


        } catch (Exception e) { //SocketTimeoutException
            Log.println("timeoutCount:" + timeoutCount);
            Log.println("网络超时，正在重试! SocketTimeoutException：" + e.getMessage());
        }
        isTimeout = true;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getCookie() {
        return cookie.toString();
    }
}
