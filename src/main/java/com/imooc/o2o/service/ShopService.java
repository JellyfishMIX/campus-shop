package com.imooc.o2o.service;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;

import java.io.File;

public interface ShopService {
    /**
     * 新建店铺
     * @param shop
     * @param shopImg
     * @return
     */
    ShopExecution addShop(Shop shop, File shopImg);
}
