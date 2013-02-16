/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chenzhihui
 */
public class Category {
    private List<Member> memberList = new ArrayList<Member>();
    private int index;
    private int sort;
    private String name;
    private int onlineCount = 0;

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || this != obj) {
            return false;
        }
        if(obj instanceof Category) {
            Category c = (Category)obj;
            if(c.index == this.index) {
                return true;
            }
        }
        return super.equals(obj);
    }

}
