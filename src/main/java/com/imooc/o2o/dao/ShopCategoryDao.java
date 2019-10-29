package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopCategoryDao {
    /**
     * 列出商品类别
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}
