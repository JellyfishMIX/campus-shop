package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class ProductDaoTest extends BaseTest {
    @Autowired
    private ProductDao productDao;

    @Test
    @Ignore
    public void testInsertProduct() {
        Product product = new Product();
        product.setProductName("可乐");
        product.setProductDesc("测试可乐");
        product.setImgAddr("测试图片地址");
        product.setNormalPrice(8);
        product.setPromotionPrice(6);
        product.setPriority(0);
        product.setCreateTime(new Date());
        product.setEnableStatus(1);

        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        Shop shop = new Shop();
        shop.setShopId(2L);

        product.setProductCategory(productCategory);
        product.setShop(shop);

        int effectedNum = productDao.insertProduct(product);
        Assert.assertEquals(1, effectedNum);
    }
}
