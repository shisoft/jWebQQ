/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.model;

import java.awt.Image;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author chenzhihui
 */
public class Member implements Serializable {

    private long uin = -2;
    private String status = "offline";
    private String account;     //账号
    private String password;    //密码
    private long loginDate;     //登录时间
    private String nickname;     //昵称
    private String markname;    //备注
    private String lnick;       //个性签名
    private Image face;        //头像
    private String level;          //等级
    private String gender;      //性别
    private Date birthday;      //出生日期
    private String phone;        //电话
    private String mobile;      //手机
    private String email;       //邮箱
    private String college;     //毕业院校
    private int regTime;    //註冊時間
    private int constel;     //星座
    private int blood;       //血型
    private String homepage;    //个人主页
    private int stat;        //统计
    private int vipInfo;    //VIP信息
    private String country;     //国家
    private String province;    //省
    private String city;        //城市
    private String personal;    //个人说明
    private String occupation;  //职业
    private int chineseZodiac;   //生肖
    private int allow;       //允許
    private int client_type; //客户类型
    private int flag;
    private Category category;
    private int cip;
    private boolean isDownloadInfo;

    public Member(String nickname, Image face) {
        this.nickname = nickname;
        this.face = face;
    }

    public Member(String nickname) {
        this.nickname = nickname;
    }

    public Member() {
    }

    public boolean isIsDownloadInfo() {
        return isDownloadInfo;
    }

    public void setIsDownloadInfo(boolean isDownloadInfo) {
        this.isDownloadInfo = isDownloadInfo;
    }

    public long getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(long loginDate) {
        this.loginDate = loginDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMarkname() {
        return markname;
    }

    public void setMarkname(String markname) {
        this.markname = markname;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getLnick() {
        return lnick;
    }

    public void setLnick(String lnick) {
        this.lnick = lnick;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getAllow() {
        return allow;
    }

    public void setAllow(int allow) {
        this.allow = allow;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public int getChineseZodiac() {
        return chineseZodiac;
    }

    public void setChineseZodiac(int chineseZodiac) {
        this.chineseZodiac = chineseZodiac;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getClient_type() {
        return client_type;
    }

    public void setClient_type(int client_type) {
        this.client_type = client_type;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public int getConstel() {
        return constel;
    }

    public void setConstel(int constel) {
        this.constel = constel;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Image getFace() {
        return this.face;
    }

    public void setFace(Image face) {
        this.face = face;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getRegTime() {
        return regTime;
    }

    public void setRegTime(int regTime) {
        this.regTime = regTime;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public long getUin() {
        return uin;
    }

    public void setUin(long uin) {
        this.uin = uin;
    }

    public int getVipInfo() {
        return vipInfo;
    }

    public void setVipInfo(int vipInfo) {
        this.vipInfo = vipInfo;
    }

    public int getCip() {
        return cip;
    }

    public void setCip(int cip) {
        this.cip = cip;
    }

    @Override
    public String toString() {
        String name = this.getNickname();
        if (this.getMarkname() != null && !this.getMarkname().equals("")) {
            name = this.getMarkname() + "(" + name + ")";
        }
        String vip = "<html><strong><font color='red'>:name</font></strong></html>";
        //重写自己的toString方法
        //由于TreeCellRenderer extends JLabel所以可以使用html标签用来修饰，以至于在JTree中显示自己预设文字的效果
        return name;
    }

    @Override
    public int hashCode() {
        return (int) this.getUin();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }

        if (obj instanceof Member) {
            Member m = (Member) obj;
            if (m.getUin() == this.getUin()) {
                return true;
            }
        }
        return false;
    }
}
