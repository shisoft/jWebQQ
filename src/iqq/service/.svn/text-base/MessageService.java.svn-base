/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.service;

import iqq.util.Method;
import iqq.util.ThreadUtil;
import iqq.util.ErrorMessage;
import iqq.util.Log;
import iqq.util.QQImageUtil;
import iqq.util.GroupUtil;
import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.html.HTMLDocument;
import iqq.comm.Auth;
import iqq.comm.MyDefaultTreeCellRenderer;
import iqq.model.Group;
import iqq.model.Member;
import iqq.model.Message;
import iqq.ui.MainPanel;

/**
 *
 * @author chenzhihui
 */
public class MessageService extends Thread {

    private static MessageService messageService = null;
    private HttpService httpService = null;
    private MemberService memberService = MemberService.getInstance();
    private static boolean isRun = false;
    private Member member = null;
    private int errorCount = 0;

    private MessageService() {
    }

    public static MessageService getIntance() {
        if (messageService == null) {
            messageService = new MessageService();
        }
        return messageService;
    }

    @Override
    public void run() {
        String pollUrl = "http://d.web2.qq.com/channel/poll2?clientid=" + Auth.getClientid()
                + "&psessionid=" + Auth.getPsessionid();
        while (isRun) {
            if (this.errorCount > 10) {
                isRun = false;
                ErrorMessage.show("网络连接异常，请检查网络后重新登录！");
                System.exit(0);
            }

            try {
                httpService = new HttpService(pollUrl, Method.GET);
                String ret = httpService.sendHttpMessage();
                JSONObject retJ = new JSONObject(ret);
                int retcode = retJ.getInt("retcode");
                if (retcode == 0) {
                    JSONArray result = retJ.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        String poll_type = result.getJSONObject(i).getString(
                                "poll_type");
                        JSONObject value = result.getJSONObject(i).getJSONObject("value");
                        if ("message".equals(poll_type)) {// 好友消息
                            try {
                                receiveMsg(value);
                                this.errorCount = 0;
                            } catch (Exception e) {
                            }
                        } else if ("buddies_status_change".equals(poll_type)) {// 好友上下线
                            changeStatus(value);
                            this.errorCount = 0;
                        } else if ("group_message".equals(poll_type)) {// 群消息
                            receiveGroupMsg(value);
                            this.errorCount = 0;
                        } else if ("kick_message".equals(poll_type)) {
                            isRun = false;
                            ErrorMessage.show("QQ已经在别处登录！");
                            Log.println("QQ已经在别处登录！");
                            ThreadUtil.shutdown();
                            System.exit(0);
                            // system_message 是系统消息 else if (retcode == 121)
                        }
                    }
                }
                //查询新信息，5秒后继续查询
                Thread.sleep(5000);
            } catch (Exception e) {
                // TODO: handle exception
                Log.println("errorCount:" + errorCount);
                Log.println("Response PollMessage failure = " + e.getMessage());
                e.printStackTrace();
                this.errorCount++;
            }
        }
    }

    public void receiveMsg(JSONObject value) {
        try {
            int msg_id = value.getInt("msg_id");
            long from_uin = value.getLong("from_uin");
            long reply_ip = value.getLong("reply_ip");
            long time = value.getLong("time");

            JSONArray array = value.getJSONArray("content");
            int size = array.length();
            String content = "";
            for (int i = 1; i < size; i++) {
                String valStr = array.get(i).toString();
                if (valStr.startsWith("[\"face\",")) {
                    content += QQImageUtil.convertFlagToHTML(array.getJSONArray(i).getString(1));
                } else if (valStr.startsWith("[\"offpic\"")) {
                    JSONObject obj = new JSONObject(array.getJSONArray(i).getString(1));
                    if (obj.getInt("success") == 1) {
                        content += QQImageUtil.getOffImage(obj.getString("file_path"), from_uin);
                    }
                } else if (valStr.startsWith("[\"cface\"")) {
                    content += QQImageUtil.getCFaceImage(array.getJSONArray(i).getString(1), msg_id, from_uin);
                } else {
                    content += array.getString(i);
                }
            }
            Log.println("新信息来了：" + content);
            member = memberService.get(from_uin);

            HTMLDocument htmlDoc = new HTMLDocument();
            content = QQImageUtil.parseHTML(content);
            htmlDoc.insertString(0, content, null);
            Message msg = new Message();
            msg.setMember(member);
            msg.setMessage(htmlDoc);
            msg.setCreateDate(new Date(time * 1000L));
            //添加新来信息到队列中
            StackMessageService.getIntance().push(msg);

            //下载头像和个人信息
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    try {
                        member.setFace(MemberService.getInstance().downloadFace(member));
                        if (member.getStat() <= 0 && member.getClient_type() <= 0) {
                            try {
                                //下载个人信息
                                member = MemberService.getInstance().getMemberInfo(member);
                            } catch (Exception ex) {
                                Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(MyDefaultTreeCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            if (member != null) {
                ThreadUtil.submit(r);
            }
        } catch (JSONException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static void changeStatus(JSONObject value) throws Exception {
        long from_uin = value.getLong("uin");
        String status = value.getString("status");
        Member m = MemberService.getInstance().get(from_uin);
        if (m == null) {
            return;
        }
        m.setStatus(status);
        Log.println("用户：" + m.getNickname() + "\t" + status);
        CategoryService.getInstance().changeStatus(m);
    }

    public boolean sendMsg(long toUin, HTMLDocument doc) {
        JSONArray msg = QQImageUtil.convertHTMLToFlag(doc);
        try {
            JSONObject json = new JSONObject();
            json.put("to", toUin);// 要发送的人
            json.put("face", 0);

//            JSONArray msg = new JSONArray();
//            JSONArray face = new JSONArray();
//            face.add("face");
//            face.add(0);
//            msg.add(face);
//            msg.add(text);
            JSONArray font = new JSONArray();
            font.add("font");

            JSONObject font1 = new JSONObject().put("name", "宋体").put("size",
                    "10");

            JSONArray style = new JSONArray();
            style.add(0);
            style.add(0);
            style.add(0);
            font1.put("style", style);
            font1.put("color", "000000");

            font.add(font1);
            msg.add(font);

            json.put("content", msg.toString());
            json.put("msg_id", new Random().nextInt(10000000));
            json.put("clientid", Auth.getClientid());
            json.put("psessionid", Auth.getPsessionid());// 需要这个才能发送
            String sendMsgUrl = "http://d.web2.qq.com/channel/send_msg2";
            String content = json.toString();
            Log.println("****content: " + content);
            try {
                content = URLEncoder.encode(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }// 他要需要编码
            content = "r=" + content;

            httpService = new HttpService(sendMsgUrl, Method.POST, content);
            String res = httpService.sendHttpMessage();// 发送
            // 不出意外，这是返回结果：{"retcode":0,"result":"ok"}
            if (null == res || !res.contains("result")) {
                return false;
            }
            JSONObject rh = new JSONObject(res);
            if ("ok".equals(rh.getString("result"))) {
                return true;
            }
        } catch (Exception e) {
            Log.println("send message to " + toUin + " failure......\n" + e.getMessage());
        }
        return false;
    }

    public boolean sendMsgToQQ(Long toQQ, String message) {
        return sendMsg(toQQ, null);
    }

    public void receiveGroupMsg(JSONObject value) throws Exception {
        //Log.println(value);
        //String content = value.getJSONArray("content").getString(1);
        int msg_id = value.getInt("msg_id");
        long from_uin = value.getLong("from_uin");
        long send_uin = value.getLong("send_uin");
        long reply_ip = value.getLong("reply_ip");
        long group_code = value.getLong("group_code");
        long time = value.getLong("time");

        if (GroupUtil.getInstance().isDisabled(GroupService.getInstance().getNumberForCode(group_code).getNumber())) {
            return;
        }

        JSONArray array = value.getJSONArray("content");
        int size = array.length();
        String content = "";
        for (int i = 1; i < size; i++) {
            String valStr = array.get(i).toString();
            if (valStr.startsWith("[\"face\",")) {
                content += QQImageUtil.convertFlagToHTML(array.getJSONArray(i).getString(1));
            } else if (valStr.startsWith("[\"offpic\"")) {
                JSONObject obj = new JSONObject(array.getJSONArray(i).getString(1));
                if (obj.getInt("success") == 1) {
                    content += QQImageUtil.getOffImage(obj.getString("file_path"), from_uin);
                }
            } else if (valStr.startsWith("[\"cface\"")) {
                JSONObject obj = new JSONObject(array.getString(i).replaceFirst(",", ":").replace("[", "{").replace("]", "}"));
                //Log.println(obj);
                content += QQImageUtil.getGroupPic(obj.getJSONObject("cface"), from_uin, send_uin);
            } else {
                content += array.getString(i);
            }
        }

        Log.println("新信息来了：" + content);

        List<Member> memberList = GroupService.getInstance().downloadMemberList(group_code);
        for (Member m : memberList) {
            if (m.getUin() == send_uin) {
                member = m;
            }
        }
        HTMLDocument htmlDoc = new HTMLDocument();
        content = QQImageUtil.parseHTML(content);
        htmlDoc.insertString(0, content, null);

        Group group = GroupService.getInstance().get(from_uin);
        group.setMember(member);
        Message msg = new Message();
        msg.setGroup(group);
        msg.setMessage(htmlDoc);
        msg.setCreateDate(new Date(time * 1000L));
        //添加新来信息到队列中
        StackMessageService.getIntance().push(msg);

        //Log.println(content);
    }

    public boolean sendGroupMsg(long toUin, HTMLDocument doc) {
        JSONArray msg = QQImageUtil.convertHTMLToFlag(doc);
        try {
            JSONObject json = new JSONObject();
            json.put("group_uin", toUin);// 要发送的群
            json.put("face", 330);

//            JSONArray msg = new JSONArray();
//            msg.add(message);
            JSONArray font = new JSONArray();
            font.add("font");

            JSONObject font1 = new JSONObject().put("name", "宋体").put("size",
                    "10");

            JSONArray style = new JSONArray();
            style.add(0);
            style.add(0);
            style.add(0);
            font1.put("style", style);
            font1.put("color", "000000");

            font.add(font1);
            msg.add(font);

            json.put("content", msg.toString());
            json.put("msg_id", new Random().nextInt(10000000));
            json.put("clientid", Auth.getClientid());
            json.put("psessionid", Auth.getPsessionid());// 需要这个才能发送
            String sendMsgUrl = "http://d.web2.qq.com/channel/send_qun_msg2";
            String content = json.toString();
            Log.println("****content: " + content);
            try {
                content = URLEncoder.encode(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }// 他要需要编码
            content = "r=" + content;

            httpService = new HttpService(sendMsgUrl, Method.POST, content);
            String res = httpService.sendHttpMessage();// 发送
            // 不出意外，这是返回结果：{"retcode":0,"result":"ok"}
            if (null == res || !res.contains("result")) {
                return false;
            }
            JSONObject rh = new JSONObject(res);
            if ("ok".equals(rh.getString("result"))) {
                return true;
            }
        } catch (Exception e) {
            Log.println("send message to " + toUin + " failure......\n" + e.getMessage());
        }
        return false;
    }

    public static boolean isIsRun() {
        return isRun;
    }

    public static void setIsRun(boolean isRun) {
        MessageService.isRun = isRun;
    }
}
