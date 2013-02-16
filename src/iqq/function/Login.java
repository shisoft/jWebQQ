/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.function;

import iqq.model.Category;
import iqq.model.Group;
import iqq.model.Member;
import iqq.model.Message;
import iqq.service.QQServices;
//import iqq.service.CategoryService;
//import iqq.service.GroupService;
//import iqq.service.QQServices;
//import iqq.service.MemberService;
import iqq.util.Log;
import java.awt.Image;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Jay
 */
public class Login extends Thread {

    private static Member member = null;
    private static List<Category> categoryList = null;
    private static List<Member> onlineFriends = null;
    private static List<Group> groupList = null;
    private static HashMap<String, Member> account_uin;
    private static long uin = -2;

    public static void main(String[] args) throws Exception {

        QQServices qs = new QQServices() {

            @Override
            public void msgReceived(Message m) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void gotGroupMsg(Message m) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void contactStatusChanged(Member m, String status) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getVerifyCode(Image image) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        };
        qs.login("test", "*****");
        qs.PollingService.start();
        qs.PollingService.setIsRunning(true);
        Log.println("正在下载用户信息...");
        member = qs.getMemberInfo(qs.Auth.getMember());
        Log.println("正在下载用户头像...");
        member.setFace(qs.getUserFace());
        Log.println("正在加载好友列表...");
        categoryList = qs.getFriends();
        Log.println("正在加载在线好友...");
        onlineFriends = qs.getOnlineFriends();
        Member m = qs.getAccount_uin().get("376701894");
        qs.PollingService.sendMsg(m.getUin(), "test~~~...");
    }
}
