package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// @FixMethodOrder注解，控制junit执行的先后顺序。此处作用是使新增和删除形成闭环，删除前先新增，以免多删
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest {
    @Autowired
    private ProductCategoryDao productCategoryDao;

    /**
     * 查询指定店铺下的所有商品类别
     */
    @Test
    @Ignore
    public void testGetProductCategoryList() {
        long shopId = 2;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
        System.out.println("该店铺自定义类别数目为：" + productCategoryList.size() + "个");
    }

    /**
     * 批量新增商品类别
     */
    @Test
    @Ignore
    public void testABatchInsertProductCategoryList() {
        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setProductCategoryName("测试商品类别3");
        productCategory1.setPriority(7);
        productCategory1.setCreateTime(new Date());
        productCategory1.setShopId(1L);
        ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setProductCategoryName("测试商品类别4");
        productCategory2.setPriority(8);
        productCategory2.setCreateTime(new Date());
        productCategory2.setShopId(1L);

        List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
        productCategoryList.add(productCategory1);
        productCategoryList.add(productCategory2);
        int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
        Assert.assertEquals(2, effectedNum);
    }

    /**
     * 删除指定的商品类别
     */
    @Test
    @Ignore
    public void testBDeleteProductCategory() {
        long shopId = 1L;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
        for (ProductCategory productCategory:productCategoryList) {
            if ("测试商品类别3".equals(productCategory.getProductCategoryName()) || "测试商品类别4".equals(productCategory.getProductCategoryName())) {
                int effectedNum = productCategoryDao.deleteProductCategory(productCategory.getProductCategoryId(), shopId);
                Assert.assertEquals(1, effectedNum);
            }
        }
    }
}
