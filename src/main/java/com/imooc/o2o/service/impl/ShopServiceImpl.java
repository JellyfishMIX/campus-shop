package com.imooc.o2o.service.impl;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.service.ShopService;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

public class ShopServiceImpl implements ShopService {

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, File shopImg) {
        if (shop == null)
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        if (shop.getShopId() == null)
            return new ShopExecution(ShopStateEnum.NULL_SHOPID);
        try {
            shop.setEnableStatus(0);

        } catch (Exception e) {
            throw new RuntimeException("addShop error: " + e.getMessage());
        }
        return null;
    }
}
