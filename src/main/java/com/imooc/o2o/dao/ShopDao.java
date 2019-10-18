package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Shop;

public interface ShopDao {
    /**
     * 新增店铺。返回值1为成功，-1为失败
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     * @param shop
     * @return
     */
    int updateShop(Shop shop);
}
