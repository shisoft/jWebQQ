/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.service;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import iqq.comm.Auth;
import iqq.model.Category;
import iqq.model.Group;
import iqq.model.Member;
import iqq.model.Message;
import iqq.util.*;
import java.awt.Image;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 * @author chenzhihui
 */
public abstract class QQServices {

    HttpService httpService = null;
    public Auth Auth = new Auth();
    String account;
    String password;
    public PollingService PollingService;
    public HttpServiceFactory usf = new HttpServiceFactory();

    public QQServices() {
        PollingService = new PollingService(Auth, this) {
            @Override
            public void gotMessage(Message m) {
                msgReceived(m);
            }

            @Override
            public void ContactStatusChanged(Member m, String status) {
                contactStatusChanged(m, status);
            }

            @Override
            public void gotGroupMesage(Message m) {
                gotGroupMsg(m);
            }
        };
    }

    public abstract void msgReceived(Message m);

    public abstract void gotGroupMsg(Message m);

    public abstract void contactStatusChanged(Member m, String status);
    // 检查账号是否要验证码
    private String checkQQUrl = "http://check.ptlogin2.qq.com/check?uin=:account&appid=1003903&r=" + Math.random();
    // 登录url
    private String loginUrl = "http://ptlogin2.qq.com/login?u=:account&p=:password&verifycode=:VCode&webqq_type=10&remember_uin=1&login2qq=1&aid=1003903&u1=:loginurl&h=1&ptredirect=0&ptlang=2052&from_ui=1&pttype=1&dumy=&fp=loginerroralert&action=7-24-1937704&mibao_css=m_webqq&t=1&g=1";
    // 进入登录渠道
    private String channelLoginUrl = "http://d.web2.qq.com/channel/login2";

    public Boolean login(String account, String password) {
        this.account = account;
        String loginStatus = "0";
        Log.println("Account:" + account + " pwd:" + password);
        checkQQUrl = checkQQUrl.replace(":account", account);
        usf.setCookie("");
        httpService = usf.get(checkQQUrl, Method.GET);
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
            vCode = getVerifyCode(getCaptcha(enVerifyCode));
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
        httpService = usf.get(loginUrl, Method.GET);
        result = httpService.sendHttpMessage();

        p = Pattern.compile("登录成功");
        m = p.matcher(result);
        if (m.find()) {
            Log.println("Welcome QQ : " + account + " Login Success！");
        } else {
            Log.println(result);
            Log.println(result);
            return false;
        }

        // 从cookie中提取ptwebqq,skey
        p = Pattern.compile("ptwebqq=(\\w+);");
        m = p.matcher(usf.getCookie());
        if (m.find()) {
            Auth.setPtwebqq(m.group(1));
        }
        p = Pattern.compile("skey=(@\\w+);");
        m = p.matcher(usf.getCookie());
        if (m.find()) {
            Auth.setSkey(m.group(1));
        }

        String contents = "{\"status\":\"" + loginStatus + "\",\"ptwebqq\":\"" + Auth.getPtwebqq()
                + "\",\"passwd_sig\":\"\",\"clientid\":\"" + Auth.getClientid() + "\"}";

        try {
            contents = URLEncoder.encode(contents, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        httpService = usf.get(channelLoginUrl, Method.POST);
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
                System.out.println("Psessionid--------->" + Auth.getPsessionid());
                Auth.setVfwebqq(obj.getString("vfwebqq"));
            }
        } catch (JSONException ex) {
            Logger.getLogger(QQServices.class.getName()).log(Level.SEVERE, null, ex);
        }


        return true;
    }

    public abstract String getVerifyCode(Image image);

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
        httpService = usf.get(urlStr, Method.GET);
        httpService.sendHttpMessage();
        this.PollingService.isRunning = false;
        this.PollingService.interrupt();
    }

    public Image getCaptcha2(String vcType) {
        URL url = null;
        InputStream is = null;
        try {
            url = new URL("http://captcha.qq.com/getimage?aid=1003903&r=" + Math.random() + "&uin=" + account);
            Log.println(url);
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Referer", "http://ui.ptlogin2.qq.com/");
            conn.addRequestProperty("Host", "captcha.qq.com");
            conn.addRequestProperty("Cookie", usf.getCookie());
            conn.addRequestProperty("Accept-Language", "zh-cn");
            conn.addRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.addRequestProperty("Cache-Control", "no-cache");
            conn.addRequestProperty("Accept-Charset", "UTF-8;");
            conn.addRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.19 (KHTML, like Gecko) Ubuntu/12.04 Chromium/18.0.1025.168 Chrome/18.0.1025.168 Safari/535.19");
            String cookie = usf.getCookie();
            if (conn.getHeaderFields().get("Set-Cookie") != null) {
                for (String s : conn.getHeaderFields().get("Set-Cookie")) {
                    cookie += s;
                }
                usf.setCookie(cookie);
            }
            is = conn.getInputStream();
            Image image = ImageIO.read(is);
            return image;
        } catch (IOException ex) {
            Logger.getLogger(QQServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(QQServices.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public Image getCaptcha(String vcType) {
        String url = "http://captcha.qq.com/getimage?aid=1003903&r=" + Math.random() + "&uin=" + account;
        HttpService hs = usf.get(url, Method.GET);
        Image image = null;
        try {
            image = ImageIO.read(hs.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(QQServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }

    public String login2(String url) {
        // 登录url

        //String loginUrl2 = "http://ptlogin2.qq.com/login?u=:account&p=:password&verifycode=:verifyCode&webqq_type=10&remember_uin=1&login2qq=1&aid=1003903&u1=:loginurl&h=1&ptredirect=0&ptlang=2052&from_ui=1&pttype=1&dumy=&fp=loginerroralert&action=7-24-1937704&mibao_css=m_webqq&t=1&g=1";
        String loginUrl2 = "http://ptlogin2.qq.com/login?ptlang=2052&u=:account&p=:password&verifycode=:verifyCode&css=http://imgcache.qq.com/ptcss/b2/qzone/15000101/style.css&mibao_css=m_qzone&aid=15000101&u1=:loginurl&ptredirect=1&h=1&from_ui=1&dumy=&fp=loginerroralert&action=3-10-39844&g=1&t=1&dummy=";
        checkQQUrl = checkQQUrl.replace(":account", account);

        usf.setCookie("");
        httpService = usf.get(checkQQUrl, Method.GET);
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
    private List<Category> categoryList = null;
    private List<Member> onlineList = null;
    private List<Member> recentList = null;
    private HashMap<String, Member> account_uin = new HashMap<String, Member>();

    public synchronized List<Category> getFriends() throws Exception {

        //所有好友
        String urlStr = "http://s.web2.qq.com/api/get_user_friends2";
        String contents = "{\"h\":\"hello\",\"vfwebqq\":\"" + Auth.getVfwebqq() + "\"}";
        contents = URLEncoder.encode(contents, "UTF-8");
        contents = "r=" + contents;

        httpService = usf.get(urlStr, Method.POST);
        httpService.setContents(contents);
        String result = httpService.sendHttpMessage();

        JSONObject retJson = new JSONObject(result);
//        JSONObject statusRetJson = JSONObject.fromObject(statusResult);

        if (retJson.getInt("retcode") == 0) {
            JSONObject obj = retJson.getJSONObject("result");
            JSONArray categories = obj.getJSONArray("categories");
            JSONArray friends = obj.getJSONArray("friends");
            JSONArray info = obj.getJSONArray("info");
            JSONArray marknames = obj.getJSONArray("marknames");
            JSONArray vipinfo = obj.getJSONArray("vipinfo");
            Category c = null;
            Member m = null;
            Category cDef = null;
            categoryList = new ArrayList<Category>();
            for (int i = 0; i < categories.size(); i++) {
                c = new Category();
                c.setIndex(categories.getJSONObject(i).getInt("index"));
                c.setSort(categories.getJSONObject(i).getInt("sort"));
                c.setName(categories.getJSONObject(i).getString("name"));
                for (int j = 0; j < friends.size(); j++) {
                    int index = friends.getJSONObject(j).getInt("categories");
                    if (index == c.getIndex()) {
                        m = new Member();
                        m.setUin(friends.getJSONObject(j).getLong("uin"));
                        m.setFlag(friends.getJSONObject(j).getInt("flag"));
                        m.setCategory(c);
                        m.setNickname(info.getJSONObject(j).getString("nick"));
                        m = getMemberAccount(m);
                        for (int k = 0; k < marknames.size(); k++) {
                            if (m.getUin() == marknames.getJSONObject(k).getLong("uin")) {
                                m.setMarkname(marknames.getJSONObject(k).getString("markname"));
                            }
                        }
                        if (m == null) {
                            System.out.println("null");
                        } else {
                            System.out.println("account--->" + m.getAccount());
                            System.out.println("uin--->" + m.getUin());
                        }
                        account_uin.put(m.getAccount(), m);
                        c.getMemberList().add(m);
                    }
                }
                if (categoryList.isEmpty()) {
                    categoryList.add(c);
                } else {
                    boolean isExist = false;
                    for (Category cate : categoryList) {
                        if (c.getIndex() == cate.getIndex()) {
                            isExist = true;
                        }
                    }
                    if (!isExist) {
                        categoryList.add(c);
                    }
                }

            }
            if (cDef != null) {
                categoryList.add(0, cDef);
            }
            Log.println("CategoryList: " + categoryList.size());
        }
        //getOnlineFriends();
        return categoryList;
    }

    public synchronized List<Member> getOnlineFriends() throws Exception {
        if (onlineList != null) {
            return onlineList;
        } else {
            onlineList = new ArrayList<Member>();
        }
        String urlStr = "http://d.web2.qq.com/channel/get_online_buddies2?clientid=" + Auth.getClientid() + "&psessionid=" + Auth.getPsessionid() + "&t=" + System.currentTimeMillis();

        httpService = usf.get(urlStr, Method.GET);
        String result = httpService.sendHttpMessage();

        JSONObject retJson = new JSONObject(result);
        JSONArray statusArray = null;
        if (retJson.getInt("retcode") == 0) {
            statusArray = retJson.getJSONArray("result");
            for (int i = 0; i < categoryList.size(); i++) {
                for (int k = 0; k < categoryList.get(i).getMemberList().size(); k++) {
                    Member m = categoryList.get(i).getMemberList().get(k);
                    for (int s = 0; s < statusArray.size(); s++) {
                        if (m.getUin() == statusArray.getJSONObject(s).getLong("uin")) {
                            m.setStatus(statusArray.getJSONObject(s).getString("status"));
                            if (!categoryList.get(i).getMemberList().isEmpty() && categoryList.get(i).getMemberList().get(0).getUin() != m.getUin()) {
                                categoryList.get(i).getMemberList().remove(k);
                                categoryList.get(i).getMemberList().add(0, m);
                                categoryList.get(i).setOnlineCount(categoryList.get(i).getOnlineCount() + 1);
                            }
                            onlineList.add(m);
                        }
                    }
                }
            }
        }
        Log.println("online" + onlineList.size());
        return onlineList;
    }

    public synchronized List<Member> getRecentList() {
        try {
            String urlStr = "http://d.web2.qq.com/channel/get_recent_list2";
            String contents = "{\"vfwebqq\":\"" + Auth.getVfwebqq() + "\",\"clientid\":\"" + Auth.getClientid() + "\",\"psessionid\":\"" + Auth.getPsessionid() + "\"}";
            contents = URLEncoder.encode(contents, "UTF-8");
            contents = "r=" + contents;

            httpService = usf.get(urlStr, Method.POST, contents);
            String result = httpService.sendHttpMessage();
            JSONObject retJson = new JSONObject(result);
            JSONArray array = null;
            if (retJson.getInt("retcode") == 0) {
                if (recentList == null) {
                    recentList = new ArrayList<Member>();
                }

                array = retJson.getJSONArray("result");
                for (int i = 0; i < array.length(); i++) {
                    long uin = array.getJSONObject(i).getLong("uin");
                    for (Category c : categoryList) {
                        List<Member> memberList = c.getMemberList();
                        for (Member m : memberList) {
                            if (uin == m.getUin()) {
                                if (!recentList.contains(m)) {
                                    recentList.add(m);
                                }
                            }
                        }
                    }
                }
            }

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(QQServices.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(QQServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        Log.println("recentList Size:" + recentList);
        return recentList;
    }

    public synchronized void changeStatus(Member member) throws Exception {
        for (Category c : categoryList) {
            List<Member> memberList = c.getMemberList();
            for (int i = 0; i < memberList.size(); i++) {
                Member m = memberList.get(i);
                if (m.getUin() == member.getUin()) {
                    m.setStatus(member.getStatus());

                    int count = m.getCategory().getOnlineCount();
                    if (m.getStatus() == null || m.getStatus().equals("") || m.getStatus().equals("hidden")
                            || m.getStatus().equals("offline")) {
                        if (count > 0) {
                            --count;
                        } else {
                            count = 0;
                        }
                        m.getCategory().setOnlineCount(count);
                        memberList.remove(i);
                        memberList.add(memberList.size(), m);
                    } else {
                        m.getCategory().setOnlineCount(++count);
                        memberList.remove(i);
                        memberList.add(0, m);
                    }
                }
            }
        }

        boolean newOnline = true;
        for (int i = 0; i < onlineList.size(); i++) {
            if (onlineList.get(i).getUin() == member.getUin()) {
                onlineList.get(i).setStatus(member.getStatus());
                if (member.getStatus() == null || member.getStatus().equals("") || member.getStatus().equals("hidden")
                        || member.getStatus().equals("offline")) {
                    onlineList.remove(i);
                }
                newOnline = false;
            }
        }
        if (newOnline) {
            onlineList.add(member);
            //声音提示
        }
        member.setFace(this.downloadMemberFace(member));
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Member> getOnlineList() {
        return onlineList;
    }

    public void setOnlineList(List<Member> onlineList) {
        this.onlineList = onlineList;
    }

    /**
     * @param aRecentList the recentList to set
     */
    public void setRecentList(List<Member> aRecentList) {
        this.recentList = aRecentList;
    }

    public HashMap<String, Member> getAccount_uin() {
        return account_uin;
    }
    private List<Group> groupList = null;
    private Map cacheGroupMap = new HashMap();

    public Map getGroupCacheMap() {
        return cacheGroupMap;
    }

    public void setGroupCacheMap(Map cacheMap) {
        this.cacheGroupMap = cacheMap;
    }

    public Group getGroup(long id) {
        if (groupList != null) {
            for (Group g : groupList) {
                if (g.getId() == id) {
                    return g;
                }
            }
        }
        return null;
    }

    public boolean setStatus(String text) {
        try {
            String urlStr = "http://s.web2.qq.com/api/set_long_nick2";
            JSONObject jobj = new JSONObject();
            jobj.put("vfwebqq", Auth.getVfwebqq());
            jobj.put("nlk", text);
            String content = jobj.toString();
            try {
                content = URLEncoder.encode(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }// 他要需要编码
            content = "r=" + content;
            httpService = usf.get(urlStr, Method.POST, content);
            String result = httpService.sendHttpMessage();
            JSONObject retJson = new JSONObject(result);
            return (retJson.getInt("retcode") == 0);

        } catch (JSONException ex) {
            Logger.getLogger(QQServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Group> downloadGroupList() {
        if (groupList != null) {
            return groupList;
        }
        try {
            String urlStr = "http://s.web2.qq.com/api/get_group_name_list_mask2";
            String content = "{\"vfwebqq\":\"" + Auth.getVfwebqq() + "\"}";
            try {
                content = URLEncoder.encode(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }// 他要需要编码
            content = "r=" + content;
            httpService = usf.get(urlStr, Method.POST, content);
            String result = httpService.sendHttpMessage();
            JSONObject retJson = new JSONObject(result);
            if (retJson.getInt("retcode") == 0) {
                JSONObject obj = retJson.getJSONObject("result");
                if (groupList == null) {
                    groupList = new ArrayList<Group>();
                }
                Group group = null;
                // if (!retJson.isNull("gnamelist")) {
                JSONArray array = obj.getJSONArray("gnamelist");
                for (int i = 0; i < array.length(); i++) {
                    group = new Group();
                    group.setFlag(array.getJSONObject(i).getLong("flag"));
                    group.setName(array.getJSONObject(i).getString("name"));
                    group.setId(array.getJSONObject(i).getLong("gid"));
                    group.setCode(array.getJSONObject(i).getLong("code"));
                    group.setFace(downloadGroupImage(group));
                    groupList.add(group);
                }
                Log.println("groupList size:" + groupList.size());
            }

        } catch (JSONException ex) {
            Logger.getLogger(QQServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        //下载群号
        fillNumber();
        return groupList;
    }

    public Image downloadGroupImage(Group group) {
        //如果源图片存在，相隔一个小时，则生成新的图片
        try {
            String urlStr = "http://face1.qun.qq.com/cgi/svr/face/getface?cache=0&type=4&fid=0&uin=" + group.getCode() + "&vfwebqq=" + Auth.getVfwebqq();
            return ImageIO.read(new URL(urlStr));
        } catch (MalformedURLException ex) {
            Logger.getLogger(QQServices.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(QQServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Member> downloadMemberList(long code) {
        Log.println("-----------下载群成员-------------");
        String key = "groupMembers" + code;
        List<Member> memberList = (List<Member>) cacheGroupMap.get(key);
        if (memberList != null) {
            return memberList;
        }

        try {
            String urlStr = "http://s.web2.qq.com/api/get_group_info_ext2?gcode=" + code + "&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();

            httpService = usf.get(urlStr, Method.GET);
            String result = httpService.sendHttpMessage();
            JSONObject retJson = new JSONObject(result);
            Log.println(retJson.toString());
            if (retJson.getInt("retcode") == 0) {
                memberList = new ArrayList<Member>();
                Member member = null;
                JSONObject obj = retJson.getJSONObject("result");
                JSONArray array = obj.getJSONArray("minfo");
                for (int i = 0; i < array.length(); i++) {
                    member = new Member();
                    member.setNickname(array.getJSONObject(i).getString("nick"));
                    member.setUin(array.getJSONObject(i).getLong("uin"));
                    memberList.add(member);
                }
                cacheGroupMap.put(key, memberList);
                Log.println("memberList:" + memberList.size());
            }
        } catch (JSONException ex) {
            Logger.getLogger(QQServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return memberList;
    }

    public Group getNumberForCode(long code) {
        if (groupList != null) {
            for (Group g : groupList) {
                if (g.getCode() == code) {
                    if (g.getNumber() == -1) {
                        g.setNumber(downloadNumberForCode(code));
                    }
                    return g;
                }
            }
        }

        return null;
    }

    public void fillNumber() {
        for (Group group : groupList) {
            group.setNumber(downloadNumberForCode(group.getCode()));
        }
    }

    public long downloadNumberForCode(long code) {
        long groupNumber = 0;
        try {
            String urlStr = "http://s.web2.qq.com/api/get_friend_uin2?tuin=" + code + "&verifysession=&type=4&code=&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();
            httpService = usf.get(urlStr, Method.GET);
            String result = httpService.sendHttpMessage();
            JSONObject retJson = new JSONObject(result);
            if (retJson.getInt("retcode") == 0) {
                JSONObject obj = retJson.getJSONObject("result");
                groupNumber = obj.getLong("account");
            }
        } catch (JSONException e) {
            Log.println(e.getMessage());
        }
        Log.println("Group Mumber:" + groupNumber);
        return groupNumber;
    }

    public Member GetMember(long uin) {
        List<Category> categoryList = getCategoryList();
        for (Category c : categoryList) {
            for (Member m : c.getMemberList()) {
                if (m.getUin() == uin) {
                    if (m.getNickname() == null || m.getNickname().equals("")) {
                        return getForDownload(uin);
                    } else {
                        return m;
                    }
                }
            }
        }

        return getForDownload(uin);
    }

    public Member getForDownload(long uin) {
        Member m = new Member();
        m.setUin(uin);
        try {
            return getMemberInfo(m);
        } catch (Exception ex) {
            Logger.getLogger(QQServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return m;
    }

    public Image getUserFace() throws Exception {
        String urlStr = "http://face10.qun.qq.com/cgi/svr/face/getface?cache=1&type=1&fid=0&uin=" + Auth.getMember().getAccount() + "&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();
        return ImageIO.read(new URL(urlStr));
    }

    public Image downloadMemberFace(long uin) throws Exception {
        return downloadMemberFace(this.GetMember(uin));
    }

    public Image downloadMemberFace(Member member) throws Exception {
        member = getMemberAccount(member);
        if (member == null || member.getAccount() == null || member.getUin() <= 0) {
            Log.println("下载会员信息不能为空！！");
            return null;
        }
        final String urlStr = "http://face10.qun.qq.com/cgi/svr/face/getface?cache=1&type=1&fid=0&uin=" + member.getUin() + "&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();
        return ImageIO.read(new URL(urlStr));
    }

    public Member getQQLevel(Member member) throws Exception {
        if (member.getLevel() != null && !member.getLevel().equals("")) {
            return member;
        }
        String urlStr = "http://s.web2.qq.com/api/get_qq_level2?tuin=" + member.getUin() + "&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();
        httpService = usf.get(urlStr, Method.GET);
        String result = httpService.sendHttpMessage();
        JSONObject retJson = new JSONObject(result);
        if (retJson.getInt("retcode") == 0) {
            JSONObject obj = retJson.getJSONObject("result");
            StringBuffer sb = new StringBuffer();
            sb.append("距升级到");
            sb.append(obj.getInt("level") + 1);
            sb.append("级还有");
            sb.append(obj.getInt("remainDays"));
            sb.append("天");
            member.setLevel(sb.toString());
            member.setIsDownloadInfo(true);
        }
        return member;
    }

    public Member getSignature(Member member) throws Exception {
        //Member member = Auth.getMember();
        if (member.getLnick() != null && !member.getLnick().equals("")) {
            return member;
        }
        String urlStr = "http://s.web2.qq.com/api/get_single_long_nick2?tuin=" + member.getUin() + "&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();
        httpService = usf.get(urlStr, Method.GET);
        String result = httpService.sendHttpMessage();
        JSONObject retJson = new JSONObject(result);
        if (retJson.getInt("retcode") == 0) {
            JSONArray obj = retJson.getJSONArray("result");
            String lnick = new JSONObject(obj.get(0).toString()).getString("lnick");
            member.setLnick(lnick);
        }
        return getQQLevel(member);
    }

    public boolean deleteFriend(String uid) throws JSONException {
        String urlStr = "http://s.web2.qq.com/api/delete_friend";
        httpService = usf.get(urlStr, Method.POST, "tuin=" + uid + "&delType=1&vfwebqq=" + Auth.getVfwebqq());
        String result = httpService.sendHttpMessage();
        JSONObject retJson = new JSONObject(result);
        return (retJson.getInt("retcode") == 0);
    }

    public Member getMemberInfo(Member member) throws Exception {
        if (member.getLnick() == null || member.getLnick().equals("")) {
            member = getSignature(member);
        }
        //String urlStr = "http://s.web2.qq.com/api/get_self_info2?t=1341188224874";
        String urlStr = "http://s.web2.qq.com/api/get_friend_info2?tuin=" + member.getUin() + "&verifysession=&code=&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();
        httpService = usf.get(urlStr, Method.GET);
        String result = httpService.sendHttpMessage();
        JSONObject retJson = new JSONObject(result);
        if (retJson.getInt("retcode") == 0) {
            JSONObject obj = retJson.getJSONObject("result");
            //member = Auth.getMember();
            //member.setAccount(Auth.getAccount());
            //member.setLnick(obj.getString("lnick"));
            member.setBirthday(DateUtils.parse(obj.getJSONObject("birthday")));
            member.setOccupation(obj.getString("occupation"));
            member.setPhone(obj.getString("phone"));
            member.setAllow(obj.getInt("allow"));
            member.setCollege(obj.getString("college"));
            if (!obj.isNull("reg_time")) {
                member.setRegTime(obj.getInt("reg_time"));
            }
            member.setUin(obj.getLong("uin"));
            member.setConstel(obj.getInt("constel"));
            member.setBlood(obj.getInt("blood"));
            member.setHomepage(obj.getString("homepage"));
            member.setStat(obj.getInt("stat"));
            member.setVipInfo(obj.getInt("vip_info"));
            member.setCountry(obj.getString("country"));
            member.setCity(obj.getString("city"));
            member.setPersonal(obj.getString("personal"));
            member.setNickname(obj.getString("nick"));
            member.setChineseZodiac(obj.getInt("shengxiao"));
            member.setEmail("email");
            member.setProvince(obj.getString("province"));
            member.setGender(obj.getString("gender"));
            member.setMobile(obj.getString("mobile"));
            if (!obj.isNull("client_type")) {
                member.setClient_type(obj.getInt("client_type"));
            }
        }
        return member;
    }

    public Member getMemberAccount(Member member) throws Exception {
        if (member.getAccount() != null && !member.getAccount().equals("")) {
            return member;
        }
        try {
            String urlStr = "http://s.web2.qq.com/api/get_friend_uin2?tuin=" + member.getUin() + "&verifysession=&type=1&code=&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();
            httpService = usf.get(urlStr, Method.GET);
            String result = httpService.sendHttpMessage();
            JSONObject retJson = new JSONObject(result);
            if (retJson.getInt("retcode") == 0) {
                JSONObject obj = retJson.getJSONObject("result");
                member.setAccount(obj.getString("account"));
            }
        } catch (JSONException e) {
            Log.println(e.getMessage());
            getMemberAccount(member);
        }

        return member;
    }

    public boolean changeStatus(String status) {
        if (status == null || status.trim().equals("")) {
            return false;
        }
        Log.println("改变状态:" + status);
        try {
            String urlStr = "http://d.web2.qq.com/channel/change_status2?newstatus=" + status + "&clientid=" + Auth.getClientid() + "&psessionid=" + Auth.getPsessionid() + "&t=" + System.currentTimeMillis();
            httpService = usf.get(urlStr, Method.GET);
            String result = httpService.sendHttpMessage();
            JSONObject retJson = new JSONObject(result);
            if (retJson.getInt("retcode") == 0) {
                String ok = retJson.getString("result");
                if (ok != null && ok.equals("ok")) {
                    return true;
                }
            }
        } catch (JSONException e) {
            Log.println(e.getMessage());
        }

        return false;
    }
}
