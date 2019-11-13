package com.imooc.o2o.enums;

public enum ProductCategoryStateEnum {
    SUCCESS(1, "操作成功"),
    INNER_ERROR(-1001, "内部错误"),
    NULL_SHOPID(-1002, "shopId为空"),
    EMPTY_LIST(-1002, "添加数少于1");

    private int state;
    private String stateInfo;

    private ProductCategoryStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 依据传入的state返回相应的return值。如根据传入的1返回"操作成功"，根据传入的-1001返回"内部错误"
     * @param state
     * @return
     */
    public static ProductCategoryStateEnum stateOf(int state) {
        for (ProductCategoryStateEnum stateEnum : values()) {
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
