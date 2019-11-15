package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    /**
     * 1.处理略缩图，获取略缩图相对路径并赋值给product
     * 2.往tb_product写入商品信息，获取productId
     * 3.结合productId批量处理商品详情图
     * 4.将商品详情图列表批量插入tb_product_img中
     * @param product
     * @param imageHolder
     * @param imageHolderList
     * @return
     * @throws ProductCategoryOperationException
     */
    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList) throws ProductCategoryOperationException {
        // 空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            // 给商品赋值默认属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            // 默认为上架的状态
            product.setEnableStatus(1);
            // 若商品缩略图不为空则添加
            if (imageHolder != null) {
                addThumbnail(product, imageHolder);
            }
            try {
                // 创建商品信息
                int effectedNum = productDao.insertProduct(product);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("添加商品失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("添加商品失败, errMsg: " + e.toString());
            }
            if (imageHolderList != null && imageHolderList.size() > 0) {
                addProductImgList(product, imageHolderList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            // 传参为空则返回空值错误信息
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 添加商品的缩略图（主图）
     * @param product
     * @param imageHolder
     */
    private void addThumbnail(Product product, ImageHolder imageHolder) {
        String targetPath = PathUtil.getShopImgPath(product.getShop().getShopId()); // 获取用于存储图片的目录路径
        String thumbnailAddr = ImageUtil.generateThumbnail(imageHolder, targetPath);
        product.setImgAddr(thumbnailAddr);
    }

    private void addProductImgList(Product product, List<ImageHolder> imageHolderList) {
        String targetPath = PathUtil.getShopImgPath(product.getShop().getShopId()); // 获取用于存储图片的目录路径
        List<ProductImg> productImgList = new ArrayList<ProductImg>();
        // 遍历列表一次，并添加进productImg实体类中
        for (ImageHolder productImgHolder:imageHolderList) {
            String productImgAddr = ImageUtil.generateNormalImg(productImgHolder, targetPath);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(productImgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }
        // 如果确实有详情图需要添加但，则调用dao层方法，批量添加
        if (productImgList.size() > 0) {
            try {
                int effectedNum = productImgDao.batchInsertProductImg(productImgList);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("详情图批量添加失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("详情图批量添加失败, errMsg: " + e.toString());
            }
        }
        product.setProductImgList(productImgList);
    }
}
