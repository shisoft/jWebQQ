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
//
//    public void toLogin(String account, String password) {
//        //      String account, password;
//        // System.out.println("请输入账号密码：");
//        //   Scanner scan = new Scanner(System.in);
//        //      account = "1835984714";
//        //      password = "4329256zj";
//        Log.println("to login");
//        loginMap = new HashMap();
//        loginMap.put("account", account);
//        loginMap.put("password", password);
//        loginMap.put("verifyCode", password);
//        loginMap.put("rememberPassword", false);
//        int loginStatusIndex = 0;
//        String loginStatus = "online";
//        switch (loginStatusIndex) {
//            case 1: {
//                loginStatus = "callme";
//                break;
//            }
//            case 2: {
//                loginStatus = "busy";
//                break;
//            }
//            case 3: {
//                loginStatus = "hidden";
//                break;
//            }
//            case 4: {
//                loginStatus = "silent";
//                break;
//            }
//        }
//        loginMap.put("loginStatus", loginStatus);
//        LoginProcess();
//
//    }
//
//    private void LoginProcess() {
//        Runnable toLoginRunnable = new Runnable() {
//            @Override
//            public void run() {
//
//                boolean isLogin = loginService.login(loginMap);
//                loginStatus(isLogin);
//
//            }
//        };
//        ThreadUtil.submit(toLoginRunnable);
//    }
//
//    private void loginStatus(boolean isLogin) {
//
//        Runnable loginRunnable = new Runnable() {
//            @Override
//            public void run() {
//
//
//                try {
//                    Log.println("正在下载用户信息...");
//                    member = memberService.getMemberInfo(Auth.getMember());
//                    Log.println("正在下载用户头像...");
//                    member.setFace(memberService.getFace());
//                    Log.println("正在加载好友列表...");
//                    categoryList = categoryService.getFriends();
//                    Log.println("正在加载在线好友...");
//                    onlineFriends = categoryService.getOnlineFriends();
//                } catch (Exception ex) {
//                    System.out.println(ex);
//                }
//                //下载群列表到缓存，等待加载...
//                Log.println("正在加载群组列表...");
//                groupList = GroupService.getInstance().downloadGroupList();
//                Log.println("** " + categoryList.size());
//                //   Log.println("正初始化主界面...");
//                map.put("member", member);
//                map.put("categoryList", categoryList);
//                map.put("onlineFriends", onlineFriends);
//
//            }
//        };
//
//        if (isLogin) {
//            msg = new MessagesImpl();
//            ThreadUtil.submit(loginRunnable);
//            Map lMap = (Map) map.get("loginMap");
//            boolean remember = (Boolean) lMap.get("rememberPassword");
//            if (remember) {
//                //PersistenceUtils.getInstance().update(lMap);
//            } else {
//                lMap.put("password", "");
//                //PersistenceUtils.getInstance().update(lMap);
//            }
//        } else {
//            Log.println("登录失败，请检查你的账号和密码；或者你所在的网络状况。");
//            System.exit(1);
//        }
//    }
//
//    public Member getMember() {
//        return this.member;
//    }
//
//    public List<Category> getCategoryList() {
//        while (this.categoryList == null) {
//        }
//        return this.categoryList;
//    }
//
//    public List<Member> getOnlineFriends() {
//        while (this.onlineFriends == null) {
//        }
//        return this.onlineFriends;
//    }
//
//    public List<Group> getGroupList() {
//        while (this.groupList == null) {
//        }
//        return this.groupList;
//    }
//
//    public void sendMessage(String account, final String message) throws BadLocationException {
//        Member m = new Member();
//        m = categoryService.getAccount_uin().get(account);
//        while (m == null) {
//            m = categoryService.getAccount_uin().get(account);
//        }
//        final long uin = m.getUin();
//        System.out.println("uin------->" + uin);
//        msg.sendMessage(uin, message);
//    }
//
//    public void recieveMessage(final String account) {
//        Runnable r = new Runnable() {
//            public void run() {
//                while (true) {
//                    List<Message> MessageList = StackMessageService.getIntance().pop(categoryService.getAccount_uin().get(account).getUin());
//                    if (!MessageList.isEmpty()) {
//                        System.out.println(MessageList.get(0).getMember().getAccount());
//                    }
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//        };
//
//    }
//
//    public void setUin(long uin) {
//        this.uin = uin;
//    }
//
//    public long getUin() {
//        return this.uin;
//    }
//

    public static void main(String[] args) throws Exception {
//        Login lg = new Login();
//        lg.toLogin("1835984714", "4329256zj");
//
//        System.out.println("Please input the messages:");
//
//        lg.sendMessage("364530827", "shit~~----");
//        System.out.println("account---->" + "364530827");
//        lg.recieveMessage("364530827");
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
        qs.login("1835984714", "4329256zj");
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
