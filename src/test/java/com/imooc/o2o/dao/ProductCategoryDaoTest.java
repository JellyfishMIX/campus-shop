package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductCategoryDaoTest extends BaseTest {
    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    @Ignore
    public void getProductCategoryList() {
        long shopId = 2;
        List<ProductCategory> productCategoryList = productCategoryDao.getProductCategoryList(shopId);
        System.out.println("该店铺自定义类别数目为：" + productCategoryList.size() + "个");
    }

    @Test
    public void batchInsertProductCategoryList() {
        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setProductCategoryName("测试商品类别1");
        productCategory1.setPriority(7);
        productCategory1.setCreateTime(new Date());
        productCategory1.setShopId(1L);
        ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setProductCategoryName("测试商品类别2");
        productCategory2.setPriority(8);
        productCategory2.setCreateTime(new Date());
        productCategory2.setShopId(1L);

        List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
        productCategoryList.add(productCategory1);
        productCategoryList.add(productCategory2);
        int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
        // Assert.assertEquals(2, effectedNum);
    }
}
