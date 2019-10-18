package com.imooc.o2o.entity;

import java.util.Date;

public class ProductImg {
    private Long productImgId;
    private String ImgAddr;
    private String ImgDesc;
    private Integer priority;
    private Date createTime;
    private Long ProductId;

//    Getter and Setter

    public Long getProductImgId() {
        return productImgId;
    }

    public void setProductImgId(Long productImgId) {
        this.productImgId = productImgId;
    }

    public String getImgAddr() {
        return ImgAddr;
    }

    public void setImgAddr(String imgAddr) {
        ImgAddr = imgAddr;
    }

    public String getImgDesc() {
        return ImgDesc;
    }

    public void setImgDesc(String imgDesc) {
        ImgDesc = imgDesc;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getProductId() {
        return ProductId;
    }

    public void setProductId(Long productId) {
        ProductId = productId;
    }
}
