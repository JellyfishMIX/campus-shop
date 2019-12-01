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
import com.imooc.o2o.util.PageCalculator;
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
     * 通过productId获取对应的product
     * @param productId
     * @return
     */
    @Override
    public Product getProductByProductId(long productId) {
        return productDao.queryProductByProductId(productId);
    }

    /**
     * 1. 若缩略图参数有值，则处理缩略图。若原先存在缩略图，先删除再添加新图。之后获取缩略图
     * @param product
     * @param thumbnail
     * @param productImgHolderList
     * @return
     */
    @Override
    @Transactional
    public ProductExecution updateProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) {
        // 控制判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            // 给商品设置默认属性
            product.setLastEditTime(new Date());
            // 若参数传入的商品略缩图不为空，且原有缩略图不为空，则删除原有缩略图并添加新缩略图
            if (thumbnail != null) {
                // 先获取一遍原有信息，因为原有信息中有原图片地址
                Product temProduct = productDao.queryProductByProductId(product.getProductId());
                if (temProduct.getImgAddr() != null) {
                    ImageUtil.deleteFileOrPath(temProduct.getImgAddr());
                }
                addThumbnail(product, thumbnail);
            }
            // 如果有新存入的商品详情图，则将原先的删除，并添加新的图片
            if (productImgHolderList != null && productImgHolderList.size() > 0) {
                deleteProductImgList(product.getProductId());
                addProductImgList(product, productImgHolderList);
            }
            try {
                // 更新商品信息
                int effectedNum = productDao.updateProduct(product);
                if (effectedNum < 0) {
                    throw new ProductOperationException("更新商品信息失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            } catch (Exception e) {
                throw new ProductOperationException("更新商品信息失败" + e.toString());
            }
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 查询商品列表并分页，可输入的条件有：商品名（模糊），商品状态，店铺Id，商品类别
     * @param product
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ProductExecution getProductList(Product product, int pageIndex, int pageSize) {
        // 页码转化成数据库的行码，并调用dao层取回指定页码的商品列表
        int rowIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
        List<Product> productList = productDao.queryProductList(product, rowIndex, pageSize);
        // 基于同样的查询条件返回该查询条件下的商品总数
        int count = productDao.queryProductCount(product);
        ProductExecution productExecution = new ProductExecution();
        productExecution.setProductList(productList);
        productExecution.setCount(count);
        return productExecution;
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

    /**
     * 批量添加商品的详情图
     * @param product
     * @param imageHolderList
     */
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

    /**
     * 批量删除商品的详情图
     * @param productId
     */
    private void deleteProductImgList(long productId) {
        // 根据productId获取原来的图片
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        // 干掉原来的照片
        for (ProductImg productImg : productImgList) {
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        // 删除数据库中原有的productImg信息
        productImgDao.deleteProductImgByProductId(productId);
    }
}
