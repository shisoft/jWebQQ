/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.service;

import iqq.util.Method;
import iqq.util.Log;
import iqq.util.AePlayWave;
import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import iqq.comm.Auth;
import iqq.model.Category;
import iqq.model.Member;
import iqq.ui.MainPanel;
import iqq.ui.MainFrame;

/**
 *
 * @author chenzhihui
 */
public class CategoryService {

    /**
     * @param aRecentList the recentList to set
     */
    private MemberService memberService = MemberService.getInstance();
    private HttpService httpService = null;
    private static List<Category> categoryList = null;
    private static List<Member> onlineList = null;
    private static CategoryService categoryService = null;
    private static List<Member> recentList = null;

    private CategoryService() {
    }

    public static CategoryService getInstance() {
        if (categoryService == null) {
            categoryService = new CategoryService();
        }
        return categoryService;
    }

    public synchronized List<Category> getFriends() throws Exception {

        //所有好友
        String urlStr = "http://s.web2.qq.com/api/get_user_friends2";
        String contents = "{\"h\":\"hello\",\"vfwebqq\":\"" + Auth.getVfwebqq() + "\"}";
        contents = URLEncoder.encode(contents, "UTF-8");
        contents = "r=" + contents;

        httpService = new HttpService(urlStr, Method.POST);
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
                        for (int k = 0; k < marknames.size(); k++) {
                            if (m.getUin() == marknames.getJSONObject(k).getLong("uin")) {
                                m.setMarkname(marknames.getJSONObject(k).getString("markname"));
                            }
                        }

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

        httpService = new HttpService(urlStr, Method.GET);
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

            httpService = new HttpService(urlStr, Method.POST, contents);
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
            Logger.getLogger(CategoryService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(CategoryService.class.getName()).log(Level.SEVERE, null, ex);
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
            AePlayWave.play(AePlayWave.AUDIO_ONLINE);
        }

        member.setFace(memberService.downloadFace(member));
        MainPanel mainPanel = (MainPanel) MainFrame.getMainFrame().getMap().get("mainPanel");

        mainPanel.changeStatus(member.getCategory());
    }

    public static List<Category> getCategoryList() {
        return categoryList;
    }

    public static void setCategoryList(List<Category> categoryList) {
        CategoryService.categoryList = categoryList;
    }

    public static List<Member> getOnlineList() {
        return onlineList;
    }

    public static void setOnlineList(List<Member> onlineList) {
        CategoryService.onlineList = onlineList;
    }

    /**
     * @param aRecentList the recentList to set
     */
    public static void setRecentList(List<Member> aRecentList) {
        recentList = aRecentList;
    }
}
