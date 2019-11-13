package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductCategoryDaoTest extends BaseTest {
    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    public void getProductCategoryList() {
        long shopId = 2;
        List<ProductCategory> productCategoryList = productCategoryDao.getProductCategoryList(shopId);
        System.out.println("该店铺自定义类别数目为：" + productCategoryList.size() + "个");
    }
}
