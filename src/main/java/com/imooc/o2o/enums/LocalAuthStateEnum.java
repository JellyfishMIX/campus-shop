package com.imooc.o2o.enums;

public enum LocalAuthStateEnum {
    SUCCESS(1, "操作成功"),
    INNER_ERROR(-1001, "内部错误"),
    EMPTY(-1002, "存在传参为空错误"),
    NULL_AUTH_INFO(-2001, "账号信息存在空信息"),
    ONLY_ONE_ACCOUNT(-3001, "此账户已存在，仅能存在一个账户"),
    NOT_FOUND(-1004, "未找到相应的账户");

    private int state;
    private String stateInfo;

    private LocalAuthStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static LocalAuthStateEnum stateOf(int state) {
        for (LocalAuthStateEnum stateEnum : values()) {
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
