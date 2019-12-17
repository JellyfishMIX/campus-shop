package com.imooc.o2o.dto;

import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.PersonInfoStateEnum;

public class PersonInfoExecution {
    private int state;
    private String stateInfo;
    private PersonInfo personInfo;

    // 默认构造器，为空
    public PersonInfoExecution() {

    }

    public PersonInfoExecution(PersonInfoStateEnum personInfoStateEnum) {
        this.state = personInfoStateEnum.getState();
        this.stateInfo = personInfoStateEnum.getStateInfo();
    }

    public PersonInfoExecution(PersonInfoStateEnum personInfoStateEnum, PersonInfo personInfo) {
        this.state = personInfoStateEnum.getState();
        this.stateInfo = personInfoStateEnum.getStateInfo();
        this.personInfo = personInfo;
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

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}
