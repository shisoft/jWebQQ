/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.comm;

import iqq.model.Member;

/**
 *
 * @author chenzhihui
 */
public class Auth {

    private int clientid = 73937875;
    private String psessionid;
    private String ptwebqq;
    private String vfwebqq;
    private String skey;
    private Member member = new Member();

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getClientid() {
        return clientid;
    }

    public void setClientid(int clientid) {
        this.clientid = clientid;
    }

    public String getPsessionid() {
        return psessionid;
    }

    public void setPsessionid(String psessionid) {
        this.psessionid = psessionid;
    }

    public String getPtwebqq() {
        return ptwebqq;
    }

    public void setPtwebqq(String ptwebqq) {
        this.ptwebqq = ptwebqq;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getVfwebqq() {
        return vfwebqq;
    }

    public void setVfwebqq(String vfwebqq) {
        this.vfwebqq = vfwebqq;
    }
}
