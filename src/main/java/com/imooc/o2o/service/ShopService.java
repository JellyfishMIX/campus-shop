package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;

import java.io.File;
import java.io.InputStream;

public interface ShopService {
    /**
     * 新建店铺
     * @param shop
     * @param imageHolder
     * @return
     */
    ShopExecution addShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException;

    /**
     * 通过店铺id获取店铺信息
     * @param shopId
     * @return shop
     */
    Shop getShopByShopId(long shopId);

    /**
     * 更改店铺信息
     * @param shop
     * @param imageHolder
     * @return
     * @throws ShopOperationException
     */
    ShopExecution updateShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException;

    /**
     * 根据shopCondition分页返回列表数据和店铺总数
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ShopExecution getShopListAndCount(Shop shopCondition, int pageIndex, int pageSize);
}
