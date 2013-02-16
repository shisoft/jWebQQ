/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.service;

import iqq.util.Method;
import iqq.util.AbstractNetImage;
import iqq.util.JpegNetImage;
import iqq.util.Log;
import iqq.util.QQEnvironment;
import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import iqq.comm.Auth;
import iqq.model.Group;
import iqq.model.Member;
import iqq.util.ThreadUtil;

/**
 *
 * @author chenzhihui
 */
public class GroupService {

    private static GroupService groupService = null;
    private static List<Group> groupList = null;
    private static Map cacheMap = new HashMap();
    private HttpService httpService = null;

    private GroupService() {
    }

    public static GroupService getInstance() {
        if (groupService == null) {
            groupService = new GroupService();
        }
        return groupService;
    }

    public static Map getCacheMap() {
        return cacheMap;
    }

    public static void setCacheMap(Map cacheMap) {
        GroupService.cacheMap = cacheMap;
    }

    public Group get(long id) {
        if (groupList != null) {
            for (Group g : groupList) {
                if (g.getId() == id) {
                    return g;
                }
            }
        }
        return null;
    }

    public List<Group> downloadGroupList() {
        if (groupList != null) {
            return groupList;
        }
        String path = QQEnvironment.getMemberDir() + "face" + File.separator + "group" + File.separator;
        QQEnvironment.mkdir(path);
        File file = null;
        try {
            String urlStr = "http://s.web2.qq.com/api/get_group_name_list_mask2";
            String content = "{\"vfwebqq\":\"" + Auth.getVfwebqq() + "\"}";
            try {
                content = URLEncoder.encode(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }// 他要需要编码
            content = "r=" + content;
            httpService = new HttpService(urlStr, Method.POST, content);
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
                    this.downloadImage(group, path + group.getFlag() + ".jpg");
                    file = new File(path + group.getFlag() + ".jpg");
                    if (file.exists()) {
                        group.setFace(new ImageIcon(path + group.getFlag() + ".jpg"));
                    }
                    groupList.add(group);
                }
                Log.println("groupList size:" + groupList.size());
            }

        } catch (JSONException ex) {
            Logger.getLogger(GroupService.class.getName()).log(Level.SEVERE, null, ex);
        }
        //下载群号
        fillNumber();
        return groupList;
    }

    public void downloadImage(Group group, String path) {
        //如果源图片存在，相隔一个小时，则生成新的图片
        File file = new File(path);
        long lastModified = file.lastModified();
        long nowTime = System.currentTimeMillis();
        long intervals = nowTime - lastModified;
        if (intervals > 3600000) {
            try {
                String urlStr = "http://face1.qun.qq.com/cgi/svr/face/getface?cache=0&type=4&fid=0&uin=" + group.getCode() + "&vfwebqq=" + Auth.getVfwebqq();
                AbstractNetImage ani = new JpegNetImage();
                ani.getImageFromUrl(urlStr, path);
            } catch (MalformedURLException ex) {
                Logger.getLogger(MemberService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(MemberService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<Member> downloadMemberList(long code) {
        Log.println("-----------下载群成员-------------");
        String key = "groupMembers" + code;
        List<Member> memberList = (List<Member>) cacheMap.get(key);
        if (memberList != null) {
            return memberList;
        }

        try {
            String urlStr = "http://s.web2.qq.com/api/get_group_info_ext2?gcode=" + code + "&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();

            httpService = new HttpService(urlStr, Method.GET);
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
                cacheMap.put(key, memberList);
                Log.println("memberList:" + memberList.size());
            }
        } catch (JSONException ex) {
            Logger.getLogger(GroupService.class.getName()).log(Level.SEVERE, null, ex);
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
        Runnable r = new Runnable() {

            @Override
            public void run() {
                for (Group group : groupList) {
                    group.setNumber(downloadNumberForCode(group.getCode()));
                }
            }
        };

        if (r != null) {
            ThreadUtil.submit(r);
        }
    }

    public long downloadNumberForCode(long code) {
        long groupNumber = 0;
        try {
            String urlStr = "http://s.web2.qq.com/api/get_friend_uin2?tuin=" + code + "&verifysession=&type=4&code=&vfwebqq=" + Auth.getVfwebqq() + "&t=" + System.currentTimeMillis();
            httpService = new HttpService(urlStr, Method.GET);
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
}
