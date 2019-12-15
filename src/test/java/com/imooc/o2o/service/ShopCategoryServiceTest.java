package com.imooc.o2o.service;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class ShopCategoryServiceTest extends BaseTest {
    @Autowired
    private ShopCategoryService shopCategoryService;

    @Test
    @Ignore
    public void testGetShopCategoryList() {
        ShopCategory shopCategoryCondition = new ShopCategory();
        ShopCategory parent = new ShopCategory();
        parent.setShopCategoryId(4L);
        shopCategoryCondition.setParent(parent);
        List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
        Assert.assertEquals(2, shopCategoryList.size());
    }
}
