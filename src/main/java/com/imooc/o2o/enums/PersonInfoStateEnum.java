package com.imooc.o2o.enums;

public enum PersonInfoStateEnum {
    SUCCESS(1, "操作成功"),
    INNER_ERROR(-1001, "内部错误"),
    EMPTY(-1002, "存在传参为空错误"),
    USER_ID_EMPTY(-2001, "userId为空");

    private int state;
    private String stateInfo;

    private PersonInfoStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static PersonInfoStateEnum stateOf(int state) {
        for (PersonInfoStateEnum stateEnum : values()) {
            if (stateEnum.getState() == state) {
                return stateEnum;
            }
        }
        return null;
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
}
