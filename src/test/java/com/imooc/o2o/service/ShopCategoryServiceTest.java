package com.imooc.o2o.service;

import com.imooc.o2o.BaseTest;
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
        List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(null);
        Assert.assertEquals(2, shopCategoryList.size());
    }
}
