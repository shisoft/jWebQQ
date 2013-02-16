/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.model;

import iqq.util.Log;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.AbstractDocument.Content;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author chenzhihui
 */
public class Message {

    private long id;
    private Member member;
    private Group group;
    private HTMLDocument message;
    private Date createDate;
    private boolean isRead;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public HTMLDocument getMessage() {
        return message;
    }

    public String getMessageHTML() {
        try {
            return getMessage().getText(0, getMessage().getLength());
        } catch (BadLocationException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void setMessage(HTMLDocument message) {
        this.message = message;
    }

    @Override
    public String toString() {
        Member m = null;
        Group g = null;
        long id = -2;
        String name = null;
        if (this.getMember() != null) {
            m = this.getMember();
            id = member.getUin();
            name = member.getNickname();
        } else if (this.getGroup() != null) {
            g = this.getGroup();
            id = group.getId();
            if (group.getMember() != null) {
                name = group.getMember().getNickname();
            }
        } else {
            return null;
        }

        if (this.getCreateDate() == null || this.getMessage() == null) {
            return "";
        }

        StringBuilder messages = new StringBuilder();

        StringBuilder info = new StringBuilder();
        info.append("&nbsp;&nbsp;<font color='green'>");
        info.append(name);
        info.append("</font> ");
        info.append(getCreateDate().toLocaleString());

        StringBuilder msg = new StringBuilder();
        msg.append("<div style='margin:5px;margin-bottom:8px'>");
        String text = null;
        try {
            text = getMessage().getText(0, getMessage().getLength());
        } catch (BadLocationException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        Log.println(text);

        msg.append(text);
        msg.append("</div>");

        messages.append(info);

        messages.append(msg);

        return messages.toString();
    }
}
