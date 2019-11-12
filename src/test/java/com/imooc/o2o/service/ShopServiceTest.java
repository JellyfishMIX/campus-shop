package com.imooc.o2o.service;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

public class ShopServiceTest extends BaseTest {
    @Autowired
    private ShopService shopService;

    @Test
    @Ignore
    public void testAddShop() throws FileNotFoundException {
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();

        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(1L);

        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试小店5");
        shop.setShopDesc("test5");
        shop.setShopAddr("test5");
        shop.setPhone("test5");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");

        File shopImg = new File("/Users/qianshijie/Programming/Backend/Java/Images/o2o/xiaohuangren.jpg");
        InputStream shopImgInputStream = new FileInputStream(shopImg);
        ShopExecution se = shopService.addShop(shop, shopImgInputStream, shopImg.getName());
        Assert.assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
    }

    @Test
    @Ignore
    public void testUpdateShop() throws ShopOperationException, FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(3L);
        shop.setShopName("修改后的3L店铺名称");
        File shopImg = new File("/Users/qianshijie/Programming/Backend/Java/Images/o2o/dabai.jpg");
        InputStream shopImgInputStream = new FileInputStream(shopImg);
        ShopExecution shopExecution = shopService.updateShop(shop, shopImgInputStream, "dabai.jpg");
    }

    @Test
    @Ignore
    public void testGetShopListAndCount() {
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        ShopExecution shopExecution = shopService.getShopListAndCount(shopCondition, 1, 2);
        System.out.println("店铺列当前页每页规格为：" + shopExecution.getShopList().size());
        System.out.println("店铺总数为：" + shopExecution.getCount());
    }
}
