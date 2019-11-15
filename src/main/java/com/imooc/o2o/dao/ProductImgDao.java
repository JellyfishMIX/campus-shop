package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductImg;

import java.util.List;

public interface ProductImgDao {
    /**
     * 批量插入商品图片
     * @param productImgList
     * @return
     */
    int batchInsertProductImg(List<ProductImg> productImgList);
}