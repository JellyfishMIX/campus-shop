package com.imooc.o2o.dto;

import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.enums.LocalAuthStateEnum;

import java.util.List;

public class LocalAuthExecution {
    // 结果状态
    private int state;
    // 状态标识
    private String stateInfo;

    private int count;
    private LocalAuth localAuth;
    private List<LocalAuth> localAuthList;

    // 默认构造器，为空
    public LocalAuthExecution() {

    }

    public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum) {
        this.state = localAuthStateEnum.getState();
        this.stateInfo = localAuthStateEnum.getStateInfo();
    }

    public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum, LocalAuth localAuth) {
        this.state = localAuthStateEnum.getState();
        this.stateInfo = localAuthStateEnum.getStateInfo();
        this.localAuth = localAuth;
    }

    public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum, List<LocalAuth> localAuthList) {
        this.state = localAuthStateEnum.getState();
        this.stateInfo = localAuthStateEnum.getStateInfo();
        this.localAuthList = localAuthList;
    }

    // Getter & Setter

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalAuth getLocalAuth() {
        return localAuth;
    }

    public void setLocalAuth(LocalAuth localAuth) {
        this.localAuth = localAuth;
    }

    public List<LocalAuth> getLocalAuthList() {
        return localAuthList;
    }

    public void setLocalAuthList(List<LocalAuth> localAuthList) {
        this.localAuthList = localAuthList;
    }
}
