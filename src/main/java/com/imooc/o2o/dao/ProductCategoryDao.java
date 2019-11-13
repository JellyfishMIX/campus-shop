package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryDao {
    /**
     * 查询指定店铺下的所有商品类别
     * @param shopId
     * @return
     */
    List<ProductCategory> getProductCategoryList(long shopId);

    /**
     * 批量新增商品类别
     * @param productCategoryList
     * @return rows 返回影响的行数
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);
}
