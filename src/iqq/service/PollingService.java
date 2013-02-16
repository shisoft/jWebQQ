/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.service;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import iqq.comm.Auth;
import iqq.model.Group;
import iqq.model.Member;
import iqq.model.Message;
import iqq.util.ErrorMessage;
import iqq.util.Log;
import iqq.util.Method;
import iqq.util.QQImageUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

/**
 *
 * @author chenzhihui
 */
public abstract class PollingService extends Thread {

    public boolean isRunning = false;
    private Member member = null;
    private int errorCount = 0;
    private HttpService httpService;
    Auth Auth;
    QQServices qs;
    QQImageUtil QQImageUtil;

    public PollingService(Auth Auth, QQServices qs) {
        this.Auth = Auth;
        this.qs = qs;
        this.QQImageUtil = new QQImageUtil(Auth);
    }

    @Override
    public void run() {

        String pollUrl = "http://d.web2.qq.com/channel/poll2?clientid=" + Auth.getClientid()
                + "&psessionid=" + Auth.getPsessionid();
        System.out.println("pollUrl------>" + pollUrl);
        while (isRunning) {
            if (this.errorCount > 10) {
                isRunning = false;
                ErrorMessage.show("网络连接异常，请检查网络后重新登录！");
            }

            try {
                httpService = qs.usf.get(pollUrl, Method.GET);
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
                                System.out.println("gotit!!");
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
                            isRunning = false;
                            ErrorMessage.show("QQ已经在别处登录！");
                            Log.println("QQ已经在别处登录！");
                            // system_message 是系统消息 else if (retcode == 121)
                        }
                    }
                }
                //查询新信息，5秒后继续查询
                System.out.println("search....!!");
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
            Log.println(value.toString());
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
            member = qs.GetMember(from_uin);

            HTMLDocument htmlDoc = new HTMLDocument();
            content = QQImageUtil.parseHTML(content);
            htmlDoc.insertString(0, content, null);
            Message msg = new Message();
            msg.setMember(member);
            msg.setMessage(htmlDoc);
            msg.setCreateDate(new Date(time * 1000L));
            //添加新来信息到队列中
            gotMessage(msg);

            //下载头像和个人信息
//            Runnable r = new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        member.setFace(qs.downloadMemberFace(member));
//                        if (member.getStat() <= 0 && member.getClient_type() <= 0) {
//                            try {
//                                //下载个人信息
//                                member = qs.getMemberInfo(member);
//                            } catch (Exception ex) {
//                                //  Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                        }
//                    } catch (Exception ex) {
//                        Logger.getLogger(MyDefaultTreeCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            };
//
//            if (member != null) {
//                ThreadUtil.submit(r);
//            }
        } catch (JSONException ex) {
            Logger.getLogger(PollingService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PollingService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void changeStatus(JSONObject value) throws Exception {
        long from_uin = value.getLong("uin");
        String status = value.getString("status");
        Member m = qs.GetMember(from_uin);
        if (m == null) {
            return;
        }
        m.setStatus(status);
        ContactStatusChanged(m, status);
        Log.println("用户：" + m.getNickname() + "\t" + status);
    }

    public abstract void ContactStatusChanged(Member m, String status);

    public boolean sendMsg(long toUin, String msg) {
        try {
            HTMLDocument htmlDoc = new HTMLDocument();
            htmlDoc.insertString(0, msg, null);
            JSONArray mmsg = QQImageUtil.convertHTMLToFlag(htmlDoc);
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
                mmsg.add(font);

                json.put("content", mmsg.toString());
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

                httpService = qs.usf.get(sendMsgUrl, Method.POST, content);
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
        } catch (BadLocationException ex) {
            Logger.getLogger(PollingService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
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

//        if (GroupUtil.getInstance().isDisabled(GroupService.getInstance().getNumberForCode(group_code).getNumber())) {
//            return;
//        }

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

        List<Member> memberList = qs.downloadMemberList(group_code);
        for (Member m : memberList) {
            if (m.getUin() == send_uin) {
                member = m;
            }
        }
        HTMLDocument htmlDoc = new HTMLDocument();
        content = QQImageUtil.parseHTML(content);
        htmlDoc.insertString(0, content, null);

        Group group = qs.getGroup(from_uin);
        group.setMember(member);
        Message msg = new Message();
        msg.setGroup(group);
        msg.setMessage(htmlDoc);
        msg.setCreateDate(new Date(time * 1000L));
        //添加新来信息到队列中
        gotGroupMesage(msg);

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

            httpService = qs.usf.get(sendMsgUrl, Method.POST, content);
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

    public boolean isIsRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRun) {
        this.isRunning = isRun;
    }

    public abstract void gotMessage(Message m);

    public abstract void gotGroupMesage(Message m);
}
