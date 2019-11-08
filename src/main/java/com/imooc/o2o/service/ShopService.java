package com.imooc.o2o.service;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;

import java.io.File;
import java.io.InputStream;

public interface ShopService {
    /**
     * 新建店铺
     * @param shop
     * @param shopImgInputStream
     * @return
     */
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName);

    /**
     * 通过店铺id获取店铺信息
     * @param shopId
     * @return shop
     */
    Shop getShopByShopId(long shopId);

    // /**
    //  * 更改店铺信息
    //  * @param shop
    //  * @param shopImgInputStream
    //  * @param fileName
    //  * @return
    //  * @throws ShopOperationException
    //  */
    // ShopExecution updateShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;
}
