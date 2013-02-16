/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.service;

import java.util.*;
import iqq.model.Group;
import iqq.model.Member;
import iqq.model.Message;
import iqq.ui.BaseChatPanel;
import iqq.ui.ChatDialog;
import iqq.ui.MainPanel;
import iqq.ui.MainFrame;
import iqq.util.AePlayWave;
import iqq.util.Log;

/**
 *
 * @author chenzhihui
 */
public class StackMessageService {

    private static StackMessageService stackMessageService = null;
    private MainFrame mainFrame = MainFrame.getMainFrame();
    private Map map = mainFrame.getMap();
    private Map<String, List<Message>> messageMaps = null;
    private Member member = null;
    private Group group = null;

    public Map<String, List<Message>> getMessageMaps() {
        return messageMaps;
    }

    public void setMessageMaps(Map<String, List<Message>> messageMaps) {
        this.messageMaps = messageMaps;
    }

    private StackMessageService() {
    }

    public static StackMessageService getIntance() {
        if (stackMessageService == null) {
            stackMessageService = new StackMessageService();
        }
        return stackMessageService;
    }

    public synchronized List<Message> pop(long uin) {
        if (messageMaps == null) {
            messageMaps = new HashMap<String, List<Message>>();
        }
        String key = "msg_" + uin;
        List<Message> msgList = messageMaps.get(key);
        messageMaps.remove(key);
        return msgList;
    }

    public synchronized void push(Message message) throws Exception {
        if (messageMaps == null) {
            messageMaps = new HashMap<String, List<Message>>();
        }
        //保存该信息到未读列表中
        long id = -2;
        if(message.getMember() != null) {
            member = message.getMember();
            id = member.getUin();
        } else if(message.getGroup() != null){
            group = message.getGroup();
            id = group.getId();
        }else{
            return;
        }
        
        String key = "msg_" + id;
        List<Message> msgList = messageMaps.get(key);
        if (msgList == null) {
            msgList = new ArrayList<Message>();
            msgList.add(message);
        } else {
            msgList.add(message);
        }
        messageMaps.put(key, msgList);

        //声音提示
        AePlayWave.play(AePlayWave.AUDIO_MSG);

        //是否已经打开了该聊天对话框
        if (isOpenedTab(message)) {
            return;
        }

        MainPanel mainPanel = (MainPanel) map.get("mainPanel");
        mainPanel.addMessagePrompt(msgList);
    }

    public synchronized boolean isOpenedTab(Message message) throws Exception {
        long id = -2;
        if(message.getMember() != null) {
            member = message.getMember();
            id = member.getUin();
        } else if(message.getGroup() != null){
            group = message.getGroup();
            id = group.getId();
        }else{
            return true;
        }
        
        ChatDialog chatDialog = (ChatDialog) map.get("chatDialog");
        if (chatDialog != null) {
            String key = "tab_" + id;
            BaseChatPanel c = (BaseChatPanel) chatDialog.getTabMaps().get(key);
            if (c != null) {
                chatDialog.addChat(message);
                return true;
            }
        }

        return false;
    }

    public synchronized int getCount(long uin) {
        String key = "msg_" + uin;
        List<Message> msgList = messageMaps.get(key);
        if (msgList == null) {
            return 0;
        }
        return msgList.size();
    }
}
