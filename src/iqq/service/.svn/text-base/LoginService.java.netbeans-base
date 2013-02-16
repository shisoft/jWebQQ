/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.service;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import iqq.comm.Auth;
import iqq.model.Member;
import iqq.util.*;
import java.awt.Image;
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author chenzhihui
 */
public class LoginService {

    private HttpService httpService = null;
    private static LoginService loginService = null;
    private static String account;
    private static String password;

    public static LoginService getLoginService() {
        return loginService;
    }

    public static void setLoginService(LoginService loginService) {
        LoginService.loginService = loginService;
    }

    private LoginService() {
    }

    public static LoginService getInstance() {
        if (loginService == null) {
            loginService = new LoginService();
        }
        return loginService;
    }

    public static LoginService newInstance() {
        loginService = new LoginService();
        return loginService;
    }
    // 检查账号是否要验证码
    private String checkQQUrl = "http://check.ptlogin2.qq.com/check?uin=:account&appid=1003903&r=" + Math.random();
    // 登录url
    private String loginUrl = "http://ptlogin2.qq.com/login?u=:account&p=:password&verifycode=:VCode&webqq_type=10&remember_uin=1&login2qq=1&aid=1003903&u1=:loginurl&h=1&ptredirect=0&ptlang=2052&from_ui=1&pttype=1&dumy=&fp=loginerroralert&action=7-24-1937704&mibao_css=m_webqq&t=1&g=1";
    // 进入登录渠道
    private String channelLoginUrl = "http://d.web2.qq.com/channel/login2";

    public Boolean login(Map loginParam) {
        account = loginParam.get("account").toString();
        password = loginParam.get("password").toString();
        String verifyCode = loginParam.get("verifyCode").toString();
        String loginStatus = loginParam.get("loginStatus").toString();
        Log.println("Account:" + account + " pwd:" + password);
        checkQQUrl = checkQQUrl.replace(":account", account);
        HttpService.setCookie("");
        httpService = new HttpService(checkQQUrl, Method.GET);
        String result = httpService.sendHttpMessage();
        // 检查账号验证码返回结果
        String checkAccountVCRegex = "\\'(.+)\\'\\,\\'(.+)\\'\\,\\'(.+)\\'";
        Pattern p = Pattern.compile(checkAccountVCRegex);
        Matcher m = p.matcher(result);
        String checkType = "";
        String vCode = "";
        String enVerifyCode = "";
        String verifycodeHex = "";
        if (m.find()) {
            checkType = m.group(1);
            enVerifyCode = m.group(2);
            verifycodeHex = m.group(3);

            Log.println("CheckType:" + checkType + ",enVerifyCode:" + enVerifyCode + ",VerifycodeHex:" + verifycodeHex + ",groupCount:" + m.groupCount());
        }
        if (!enVerifyCode.startsWith("!")) {
            // 生成图片验证码
            Log.println("enVerifyCode:" + enVerifyCode);
            //enVerifyCode = JOptionPane.showInputDialog(null, "验证码：", "请输入验证码：");
            Icon icon = getCaptcha(enVerifyCode);
            if (icon != null) {
                vCode = (String) JOptionPane.showInputDialog(null, "验证码：", "请输入验证码：", JOptionPane.QUESTION_MESSAGE, icon, null, null);
                if (vCode == null || vCode.trim().equals("")) {
                    ErrorMessage.show("验证码输入有误!");
                    return false;
                }
            } else {
                ErrorMessage.show("验证码获取失败！请重试。");
                return false;
            }

        } else {
            vCode = enVerifyCode;
        }

        String encodePass = encodePass(password, vCode.toUpperCase(), verifycodeHex);
        loginUrl = loginUrl.replace(":account", account);
        loginUrl = loginUrl.replace(":password", encodePass);
        loginUrl = loginUrl.replace(":VCode", vCode.toUpperCase());
        String loginStr = "http%3A%2F%2Fweb3.qq.com%2Floginproxy.html%3Flogin2qq%3D1%26webqq_type%3D10";
        //String loginStr = "http://web3.qq.com/loginproxy.html?login2qq=1&webqq_type=10";
        loginUrl = loginUrl.replace(":loginurl", loginStr);
        //BrowserUtil.openURL(loginStr);
        httpService = new HttpService(loginUrl, Method.GET);
        result = httpService.sendHttpMessage();

        p = Pattern.compile("登录成功");
        m = p.matcher(result);
        if (m.find()) {
            Log.println("Welcome QQ : " + loginParam.get("account") + " Login Success！");
        } else {
            Log.println(result);
            ErrorMessage.show(result);
            return false;
        }

        // 从cookie中提取ptwebqq,skey
        p = Pattern.compile("ptwebqq=(\\w+);");
        m = p.matcher(HttpService.getCookie());
        if (m.find()) {
            Auth.setPtwebqq(m.group(1));
        }
        p = Pattern.compile("skey=(@\\w+);");
        m = p.matcher(HttpService.getCookie());
        if (m.find()) {
            Auth.setSkey(m.group(1));
        }

        String contents = "{\"status\":\"" + loginStatus + "\",\"ptwebqq\":\"" + Auth.getPtwebqq()
                + "\",\"passwd_sig\":\"\",\"clientid\":\"" + Auth.getClientid() + "\"}";

        try {
            contents = URLEncoder.encode(contents, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        httpService = new HttpService(channelLoginUrl, Method.POST);
        httpService.setContents("r=" + contents);// post的数据
        result = httpService.sendHttpMessage();

        Member member = Auth.getMember();
        member.setAccount(account);
        try {
            JSONObject retJson = new JSONObject(result);
            if (retJson.getInt("retcode") == 0) {
                JSONObject obj = retJson.getJSONObject("result");
                member.setCip(obj.getInt("cip"));
                member.setStatus(obj.getString("status"));
                member.setUin(obj.getLong("uin"));

                Auth.setPsessionid(obj.getString("psessionid"));
                Auth.setVfwebqq(obj.getString("vfwebqq"));
            }
        } catch (JSONException ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        }


        return true;
    }

    // 加密密码
    public String encodePass(String password, String verifyCode, String verifyCodeHex) {
        try {
            InputStream in = this.getClass().getResourceAsStream("/iqq/res/encodePass.js");
            Reader inreader = new InputStreamReader(in);
            ScriptEngineManager m = new ScriptEngineManager();
            ScriptEngine se = m.getEngineByName("javascript");
            se.eval(inreader);
            Object t = se.eval("passwordEncoding(\"" + password + "\",\"" + verifyCodeHex
                    + "\",\"" + verifyCode.toUpperCase() + "\");");

            return t.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void logout() {
        String urlStr = "http://d.web2.qq.com/channel/logout2?ids=&clientid=" + Auth.getClientid() + "&psessionid=" + Auth.getPsessionid() + "&t=" + System.currentTimeMillis();
        httpService = new HttpService(urlStr, Method.GET);
        httpService.sendHttpMessage();
    }

    public Icon getCaptcha2(String vcType) {
        URL url = null;
        InputStream is = null;
        try {
            url = new URL("http://captcha.qq.com/getimage?aid=1003903&r=" + Math.random() + "&uin=" + account);
            Log.println(url);
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Referer", "http://ui.ptlogin2.qq.com/");
            conn.addRequestProperty("Host", "captcha.qq.com");
            conn.addRequestProperty("Cookie", HttpService.getCookie());
            conn.addRequestProperty("Accept-Language", "zh-cn");
            conn.addRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.addRequestProperty("Cache-Control", "no-cache");
            conn.addRequestProperty("Accept-Charset", "UTF-8;");
            conn.addRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.19 (KHTML, like Gecko) Ubuntu/12.04 Chromium/18.0.1025.168 Chrome/18.0.1025.168 Safari/535.19");
            String cookie = HttpService.getCookie();
            if (conn.getHeaderFields().get("Set-Cookie") != null) {
                for (String s : conn.getHeaderFields().get("Set-Cookie")) {
                    cookie += s;
                }
                HttpService.setCookie(cookie);
            }
            is = conn.getInputStream();
            Image image = ImageIO.read(is);
            return new ImageIcon(image);
        } catch (IOException ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public Icon getCaptcha(String vcType) {
        String url = "http://captcha.qq.com/getimage?aid=1003903&r=" + Math.random() + "&uin=" + account;
        HttpService hs = new HttpService(url, Method.GET);
        Image image = null;
        try {
            image = ImageIO.read(hs.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ImageIcon(image);
    }

    public String login2(String url) {
        // 登录url

        //String loginUrl2 = "http://ptlogin2.qq.com/login?u=:account&p=:password&verifycode=:verifyCode&webqq_type=10&remember_uin=1&login2qq=1&aid=1003903&u1=:loginurl&h=1&ptredirect=0&ptlang=2052&from_ui=1&pttype=1&dumy=&fp=loginerroralert&action=7-24-1937704&mibao_css=m_webqq&t=1&g=1";
        String loginUrl2 = "http://ptlogin2.qq.com/login?ptlang=2052&u=:account&p=:password&verifycode=:verifyCode&css=http://imgcache.qq.com/ptcss/b2/qzone/15000101/style.css&mibao_css=m_qzone&aid=15000101&u1=:loginurl&ptredirect=1&h=1&from_ui=1&dumy=&fp=loginerroralert&action=3-10-39844&g=1&t=1&dummy=";
        checkQQUrl = checkQQUrl.replace(":account", account);

        HttpService.setCookie("");
        httpService = new HttpService(checkQQUrl, Method.GET);
        String result = httpService.sendHttpMessage();
        // 检查账号验证码返回结果
        String checkAccountVCRegex = "\\'(.+)\\'\\,\\'(.+)\\'\\,\\'(.+)\\'";
        Pattern p = Pattern.compile(checkAccountVCRegex);
        Matcher m = p.matcher(result);
        String checkType = "";
        String enVerifyCode = "";
        String verifycodeHex = "";

        if (m.find()) {
            checkType = m.group(1);
            enVerifyCode = m.group(2);
            verifycodeHex = m.group(3);

            Log.println("CheckType:" + checkType + ",enVerifyCode:" + enVerifyCode + ",VerifycodeHex:" + verifycodeHex + ",groupCount:" + m.groupCount());
        }

        if (!enVerifyCode.startsWith(
                "!")) {
            // 生成图片验证码
            Log.println("enVerifyCode:" + enVerifyCode);
        }
        String encodePass = encodePass(password, enVerifyCode, verifycodeHex);
        loginUrl2 = loginUrl2.replace(":account", account);
        loginUrl2 = loginUrl2.replace(":password", encodePass);
        loginUrl2 = loginUrl2.replace(":verifyCode", enVerifyCode);
        loginUrl2 = loginUrl2.replace(":loginurl", url);
        return loginUrl2;
    }
}
