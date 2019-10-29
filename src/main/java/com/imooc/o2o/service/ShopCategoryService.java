package com.imooc.o2o.service;

import com.imooc.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {
    /**
     * 传入一个
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
