/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.service;

import iqq.util.Method;
import iqq.util.ThreadUtil;
import iqq.util.AbstractNetImage;
import iqq.util.JpegNetImage;
import iqq.util.Log;
import iqq.util.DateUtils;
import iqq.util.QQEnvironment;
import iqq.util.ImageUtil;
import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import iqq.comm.Auth;
import iqq.model.Category;
import iqq.model.Member;

/**
 *
 * @author chenzhihui
 */
public class MemberService {

    private HttpService httpService = null;
    private static MemberService memberService = null;
    //private CategoryService categoryService = CategoryService.getInstance();

    private MemberService() {
    }

    public static MemberService getInstance() {
        if (memberService == null) {
            memberService = new MemberService();
        }
        return memberService;
    }

    public Member get(long uin) {
        List<Category> categoryList = CategoryService.getCategoryList();
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
            Logger.getLogger(MemberService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return m;
    }

    public ImageIcon getFace() throws Exception {
        String path = QQEnvironment.getMemberDir() + "face" + File.separator + "face.jpg";
        QQEnvironment.mkdir(path);
        String urlStr = "http://face10.qun.qq.com/cgi/svr/face/getface?cache=1&type=1&fid=0&uin=" + Auth.getMember().getAccount() + "&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();
        //Log.println("getFace:" + path);

        AbstractNetImage ani = new JpegNetImage();
        ani.getImageFromUrl(urlStr, path);

        ImageIcon face = new ImageIcon(path);
        return face;
    }

    public ImageIcon downloadFace(long uin) throws Exception {
        return downloadFace(this.get(uin));
    }

    public ImageIcon downloadFace(Member member) throws Exception {
        member = getMemberAccount(member);
        if (member == null || member.getAccount() == null || member.getUin() <= 0) {
            Log.println("下载会员信息不能为空！！");
            return null;
        }
        String path = QQEnvironment.getMemberDir() + "face" + File.separator + member.getAccount() + ".jpg";
        String destpath = QQEnvironment.getMemberDir() + "face" + File.separator + "status" + File.separator + member.getAccount();
        QQEnvironment.mkdir(path);
        QQEnvironment.mkdir(destpath);
        File srcFile = new File(path);
        //如果不图片存在，则下载。
        if (!srcFile.exists()) {
            downloadImage(member, path);
        } else {
            //如果图片存在，相隔一个小时，则生成下载新的图片
            long lastModified = srcFile.lastModified();
            long nowTime = System.currentTimeMillis();
            long intervals = nowTime - lastModified;
            if (intervals > 3600000) {
                downloadImage(member, path);
            }
        }
        String statusRes = member.getStatus();
        String statusWatermark = member.getStatus() + ".png";
        if (statusRes == null || statusRes.equals("")) {
            statusRes = "offline";
        }
        destpath += statusRes + ".jpg";
        srcFile = new File(path);
        File destFile = new File(destpath);
        ImageIcon face = null;

        //如果源图片存在，但目标加水印图片不存在，则生成。
        if (srcFile.exists() && !destFile.exists()) {
            InputStream in = this.getClass().getResourceAsStream("/iqq/res/images/status/" + statusWatermark);

            //URI watemark = this.getClass().getClassLoader().getResource("iqq/res/images/status/" + statusRes).toURI();

            ImageUtil.addWatermark(srcFile, destFile, in, ImageUtil.WatermarkPosition.bottomRight, 100);
        } else if (srcFile.exists()) {
            //如果源图片存在，相隔一个小时，则生成新的图片
            long lastModified = destFile.lastModified();
            long nowTime = System.currentTimeMillis();
            long intervals = nowTime - lastModified;
            if (intervals > 3600000) {
                //URI watemark = this.getClass().getClassLoader().getResource("iqq/res/images/status/" + statusRes).toURI();
                InputStream in = this.getClass().getResourceAsStream("/iqq/res/images/status/" + statusWatermark);
                ImageUtil.addWatermark(srcFile, destFile, in, ImageUtil.WatermarkPosition.bottomRight, 100);
            }
        } else {
            return null;
        }

        face = new ImageIcon(destpath);
        return face;
    }

    public void downloadImage(Member member, final String path) {
        try {
            final String urlStr = "http://face10.qun.qq.com/cgi/svr/face/getface?cache=1&type=1&fid=0&uin=" + member.getUin() + "&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    try {
                        AbstractNetImage ani = new JpegNetImage();
                        ani.getImageFromUrl(urlStr, path);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(MemberService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MemberService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(MemberService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            if (r != null) {
                ThreadUtil.submit(r);
            }
        } catch (Exception ex) {
            Logger.getLogger(MemberService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ImageIcon loadDefFace(Member member) throws Exception {
        String destpath = QQEnvironment.getMemberDir() + "face" + File.separator + "status" + File.separator + member.getAccount();

        String statusRes = member.getStatus();
        String statusWatermark = member.getStatus() + ".png";
        if (statusRes == null || statusRes.equals("")) {
            statusRes = "offline";
        }
        String faceFile = destpath + statusRes + ".jpg";
        File f = new File(faceFile);
        ImageIcon face = null;
        if (f.exists()) {
            face = new ImageIcon(faceFile);
        } else {
            //InputStream in = this.getClass().getResourceAsStream("/iqq/res/encodePass.js");
            try {
                URL filename = this.getClass().getClassLoader().getResource("iqq/res/images/status/" + statusWatermark.substring(0, statusWatermark.indexOf(".")) + ".jpg").toURI().toURL();
                face = new ImageIcon(filename);
            } catch (Exception e) {
                Log.println("status:" + statusWatermark.substring(0, statusWatermark.indexOf(".")) + ".jpg");
                e.printStackTrace();
                return null;
            }
            //face = new ImageIcon(Toolkit.getDefaultToolkit().createImage("iqq/res/images/status/" + statusRes));
        }

        return face;
    }

    public Member getQQLevel(Member member) throws Exception {
        if (member.getLevel() != null && !member.getLevel().equals("")) {
            return member;
        }
        String urlStr = "http://s.web2.qq.com/api/get_qq_level2?tuin=" + member.getUin() + "&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();
        httpService = new HttpService(urlStr, Method.GET);
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
        httpService = new HttpService(urlStr, Method.GET);
        String result = httpService.sendHttpMessage();
        JSONObject retJson = new JSONObject(result);
        if (retJson.getInt("retcode") == 0) {
            JSONArray obj = retJson.getJSONArray("result");
            String lnick = new JSONObject(obj.get(0).toString()).getString("lnick");
            member.setLnick(lnick);
        }
        return getQQLevel(member);
    }

    public Member getMemberInfo(Member member) throws Exception {
        if (member.getLnick() == null || member.getLnick().equals("")) {
            member = getSignature(member);
        }
        //String urlStr = "http://s.web2.qq.com/api/get_self_info2?t=1341188224874";
        String urlStr = "http://s.web2.qq.com/api/get_friend_info2?tuin=" + member.getUin() + "&verifysession=&code=&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();
        httpService = new HttpService(urlStr, Method.GET);
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
            httpService = new HttpService(urlStr, Method.GET);
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
            httpService = new HttpService(urlStr, Method.GET);
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
