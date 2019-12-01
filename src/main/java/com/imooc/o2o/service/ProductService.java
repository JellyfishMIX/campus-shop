package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;

import java.util.List;

public interface ProductService {
    /**
     * 添加商品信息及图片处理
     * @param product
     * @param imageHolder
     * @param imageHolderList
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList) throws ProductCategoryOperationException;

    /**
     * 通过productId获取对应的product
     * @param productId
     * @return
     */
    Product getProductByProductId(long productId);

    /**
     * 更新商品信息
     * @param product
     * @param thumbnail
     * @param productImgHolderList
     * @return
     */
    ProductExecution updateProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList);

    /**
     * 查询商品列表并分页，可输入的条件有：商品名（模糊），商品状态，店铺Id，商品类别
     * @param product
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductList(Product product, int pageIndex, int pageSize);
}
