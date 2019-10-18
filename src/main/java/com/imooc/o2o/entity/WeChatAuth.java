//微信账号实体类

package com.imooc.o2o.entity;

import java.util.Date;

public class WeChatAuth {
    private long wechatId;
    private String openId;
    private Date createTime;
    private PersonInfo personInfo;

//    Getter and Setter
    public long getWechatId() {
        return wechatId;
    }

    public void setWechatId(long wechatId) {
        this.wechatId = wechatId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}
