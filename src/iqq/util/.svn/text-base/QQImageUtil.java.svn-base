/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.util;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import iqq.comm.Auth;
import iqq.service.HttpService;
import javax.swing.JOptionPane;

/**
 *
 * @author chenzhihui
 */
public class QQImageUtil {

    public static String faceFlag = ",14:0,1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,11:11,12:12,13:13,0:14,50:15,51:16,96:17,53:18,54:19,73:20,74:21,75:22,76:23,77:24,78:25,55:26,56:27,57:28,58:29,79:30,80:31,81:32,82:33,83:34,84:35,85:36,86:37,87:38,88:39,97:40,98:41,99:42,100:43,101:44,102:45,103:46,104:47,105:48,106:49,107:50,108:51,109:52,110:53,111:54,112:55,32:56,113:57,114:58,115:59,63:60,64:61,59:62,33:63,34:64,116:65,36:66,37:67,38:68,91:69,92:70,93:71,29:72,117:73,72:74,45:75,42:76,39:77,62:78,46:79,47:80,71:81,95:82,118:83,119:84,120:85,121:86,122:87,123:88,124:89,27:90,21:91,23:92,25:93,26:94,125:95,126:96,127:97,128:98,129:99,130:100,131:101,132:102,133:103,134:104,52:105,24:106,22:107,20:108,60:109,61:110,89:111,90:112,31:113,94:114,65:115,35:116,66:117,67:118,68:119,69:120,70:121,15:122,16:123,17:124,18:125,19:126,28:127,30:128,40:129,41:130,43:131,44:132,48:133,49:134,";
    //public static String faceUrl = "<img name=\"@name\" src=\"http://0.web.qstatic.com/webqqpic/style/face/@img.gif\"></img>";
    public static String faceUrl = "<img name=\"@name\" src=\"@img.gif\"></img>";

    public static String parseHTML(String text) {
        // Then I take substring to remove tags (html, head, body)
        if (text.contains("<html>")) {
            text = text.substring(text.indexOf("<body>") + 10, text.indexOf("</body>") - 2);
            String start = "<p style=\"margin-top: 0\">\\n      ";
            String end = "\\n    </p>";
            text = text.replaceAll(start, "").replaceAll(end, "");
        }
        // Sometimes program sets <p style="margin-top: 0"> and </p> -tags so I remove them
        // This isn't necessary to use.
        text = text.replaceAll("\\n", "<br>");
        return text;
    }

    public static String flagToHTML(String valStr) {
        //Log.println(valStr);
        String path = null;
        if (QQEnvironment.isJar()) {
            path = QQEnvironment.getProjectPath() + "/resources/images/face/" + valStr;
        } else {
            path = new QQImageUtil().getClass().getClassLoader().getResource("iqq/res/images/face/").getPath() + valStr;
        }

        //Log.println(path);
        File f = new File(path);
        String faceTag = faceUrl.replace("@name", valStr).replace("@img", f.toURI().toString());
        //Log.println(faceTag);
        return faceTag;
    }

    public static String convertFlagToHTML(String valStr) {

        String regex = "," + valStr + ":([0-9]+),";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(faceFlag);
        String result = null;
        if (m.find()) {
            result = m.group(1);
        } else {
            result = valStr;
        }
        String path = null;
        if (QQEnvironment.isJar()) {
            path = QQEnvironment.getProjectPath() + "/resources/images/face/" + result;
        } else {
            path = new QQImageUtil().getClass().getClassLoader().getResource("iqq/res/images/face/").getPath() + result;
        }

        //Log.println(path);
        File f = new File(path);
        return faceUrl.replace("@name", valStr).replace("@img", f.toURI().toString());
    }

    public static JSONArray convertHTMLToFlag(HTMLDocument htmlDoc) {
        boolean isExistFace = false;
        ElementIterator it = new ElementIterator(htmlDoc);
        Element element;
        while ((element = it.next()) != null) {
            if (element.getName().equals(HTML.Tag.IMG.toString())) {
                isExistFace = true;
                try {
                    String name = element.getAttributes().getAttribute(HTML.Attribute.NAME).toString();
                    //String src = element.getAttributes().getAttribute(HTML.Attribute.SRC).toString();

                    int offset = element.getStartOffset();
                    htmlDoc.replace(offset, element.getEndOffset() - offset, "~face:" + name + "~", null);

                } catch (BadLocationException ex) {
                    Logger.getLogger(QQImageUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        String text = null;
        try {
            text = htmlDoc.getText(0, htmlDoc.getLength());
            htmlDoc.remove(0, htmlDoc.getLength());
        } catch (BadLocationException ex) {
            Logger.getLogger(QQImageUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (isExistFace) {
            text = text.replaceFirst("\\n", "");
        }
        //Log.println(text);
        JSONArray msg = new JSONArray();
        String[] arr = text.split("~");
        for (int i = 0; i < arr.length; i++) {
            String temp = arr[i];
            //Log.println(temp);
            if (temp.startsWith("face:")) {
                String[] tempArray = temp.split(":");
                JSONArray face = new JSONArray();
                face.add(tempArray[0]);
                String regex = ",([0-9]*):" + tempArray[1] + ",";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(faceFlag);
                String result = null;
                if (m.find()) {
                    result = m.group(1);
                } else {
                    result = tempArray[1];
                }
                int faceNumber = Integer.parseInt(result);
                face.add(faceNumber);
                msg.add(face);
            } else {
                msg.add(temp);
            }
        }

        //Log.println(msg);
        return msg;
    }

    public static String getCFaceImage(String filename, int msg_id, long uin) {
        try {
            filename = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        String url = "http://d.web2.qq.com/channel/get_cface2?lcid=" + msg_id + "&guid=" + filename + "&to=" + uin + "&count=5&time=1&clientid=" + Auth.getClientid() + "&psessionid=" + Auth.getPsessionid();

        String path = QQEnvironment.getMemberDir() + "temp" + File.separator + filename;
        QQEnvironment.mkdir(path);
        try {
            download(url, path);
        } catch (Exception ex) {
            Logger.getLogger(QQImageUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        File f = new File(path);
        String imgUrl = "<img src=\"" + f.toURI() + "\" title=\"图片或自定义表情\">";

        return imgUrl;
    }

    public static String getOffImage(String filepath, long uin) {
        try {
            filepath = URLEncoder.encode(filepath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        //String url = "<img rdata=\"offpic\" src=\"http://d.web2.qq.com/channel/get_offpic2?file_path=" + filepath + "&f_uin=" + uin + "&clientid=" + Auth.getClientid() + "&psessionid=" + Auth.getPsessionid() + "\" id=\"_cface_2\" title=\"图片或自定义表情\">";
        String url = "http://d.web2.qq.com/channel/get_offpic2?file_path=" + filepath + "&f_uin=" + uin + "&clientid=" + Auth.getClientid() + "&psessionid=" + Auth.getPsessionid();

        String path = QQEnvironment.getMemberDir() + "temp" + File.separator + System.currentTimeMillis();
        QQEnvironment.mkdir(path);
        try {
            download(url, path);
        } catch (Exception ex) {
            Logger.getLogger(QQImageUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        File f = new File(path);
        String imgUrl = "<img src=\"" + f.toURI() + "\" title=\"图片或自定义表情\">";

        return imgUrl;
    }

    public static String getGroupPic(JSONObject obj, long gid, long uin) {
        try {
            String ip = obj.getString("server");
            String port = ip.substring(ip.indexOf(':'));
            String fid = obj.getString("file_id");
            String filename = obj.getString("name");
            String url = "http://web3.qq.com/cgi-bin/get_group_pic?type=0&gid=" + gid + "&uin=" + uin + "&rip=" + ip + "&rport=" + port + "&fid=" + fid + "&pic=" + filename + "&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();

            String path = QQEnvironment.getMemberDir() + "temp" + File.separator + filename;
            QQEnvironment.mkdir(path);
            try {
                download(url, path);
            } catch (Exception ex) {
                Logger.getLogger(QQImageUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            File f = new File(path);

            return "<img src=\"" + f.toURI() + "\" title=\"图片或自定义表情\">";

        } catch (JSONException ex) {
            Logger.getLogger(QQImageUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * 下载文件到本地
     *
     * @param urlString 被下载的文件地址
     * @param filename 本地文件名
     * @throws Exception 各种异常
     */
    public static synchronized void download2(String urlString, String filename) {
        File file = new File(filename);
        if (file.exists()) {
            return;
        }
        OutputStream os = null;
        InputStream is = null;
        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("Cookie", HttpService.getCookie());
            conn.addRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Accept-Language", "zh-cn");
            conn.addRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.addRequestProperty("Cache-Control", "no-cache");
            conn.addRequestProperty("Accept-Charset", "UTF-8;");
            conn.addRequestProperty("Referer", "http://web.qq.com");
            conn.addRequestProperty("Host", "d.web2.qq.com");
            conn.addRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727)");

            // 输入流
            is = conn.getInputStream();
            if (is == null) {
                url = null;
                conn = null;
                Log.println("下载失败...");
                return;
            }
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            os = new FileOutputStream(filename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (IOException ex) {
            Logger.getLogger(QQImageUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(QQImageUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(QQImageUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * 下载文件到本地
     *
     * @param urlString 被下载的文件地址
     * @param filename 本地文件名
     * @throws Exception 各种异常
     */
    public static synchronized void download(String urlString, String filename) {
        File file = new File(filename);
        if (file.exists()) {
            return;
        }
        OutputStream os = null;
        InputStream is = null;
        try {
            // 构造HTTP连接
            HttpService hs = new HttpService(urlString, Method.GET);
            is = hs.getInputStream();

            if (is == null) {
                hs = null;
                Log.println("下载失败...");
                return;
            }
            // 8K的数据缓冲
            byte[] bs = new byte[1024 * 8];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            os = new FileOutputStream(filename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }

        } catch (IOException ex) {
            Logger.getLogger(QQImageUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // 完毕，关闭所有链接
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(QQImageUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(QQImageUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

//    public static void main(String[] args) {
////        String url = "http://0.web.qstatic.com/webqqpic/style/face/";
////        String path = QQEnvironment.getConfigPath() + "QQFace" + File.separator;
////        QQEnvironment.mkdir(path);
////        for (int i = 0; i < 105; i++) {
////            String filename = i + ".gif";
////            QQImageUtil.download(url + filename, path + filename);
////        }
////        String path = new QQImageUtil().getClass().getClassLoader().getResource("iqq/res/images/face/").getPath();
////        Log.println(path);
//
////        String text = "aa~@face:91~ff~face:94~e~face:22~";
////        String regex = "(~@face:[0-9]*~)";
////        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
////        Matcher m = p.matcher(text);
////        while (m.find()) {
////            int i = m.groupCount();
////            System.out.println(i);
////        }
////
////        String[] arr = text.split("~");
////        for (int i = 0; i < arr.length; i++) {
////            Log.println(arr[i]);
////        }
//
////        System.out.println(System.currentTimeMillis());
////        System.out.println(new Date(System.currentTimeMillis()));
////        System.out.println(new Date(1342714204000L));
////        System.out.println(new Date(1342714204L * 1000L));
////        System.out.println(1342714204 * 1000);
//
//        //new QQImageUtil().getClass().getProtectionDomain().getCodeSource().getLocation();
//        //String appPath = new QQImageUtil().getClass().getProtectionDomain().getCodeSource().getLocation().toString();
//        //System.out.println(QQEnvironment.getProjectPath());
//
//        System.out.println(JOptionPane.showInputDialog(null, "验证码：", "请输入验证码：", JOptionPane.QUESTION_MESSAGE, null, null, null));
//    }
}
