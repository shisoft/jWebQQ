/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.model;

import java.awt.Image;

/**
 *
 * @author chenzhihui
 */
public class Group {

    private long id;
    private long number = -1;
    private String name;
    private long flag;
    private int mask;
    private long code;
    private Image face;
    private Member member;

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    
    public Image getFace() {
        return face;
    }

    public void setFace(Image face) {
        this.face = face;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public long getFlag() {
        return flag;
    }

    public void setFlag(long flag) {
        this.flag = flag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
