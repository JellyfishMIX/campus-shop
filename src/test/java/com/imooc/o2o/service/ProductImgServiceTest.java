package com.imooc.o2o.service;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductImgServiceTest extends BaseTest {
    @Autowired
    private ProductService productService;

    @Test
    public void testModifyProduct() throws ShopOperationException, FileNotFoundException {
        // 创建shopId为1且productId为1的商品实例并给其成员变量赋值
        Product product = new Product();
        product.setProductId(1L);
        Shop shop = new Shop();
        shop.setShopId(2L);
        product.setShop(shop);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        product.setProductCategory(productCategory);
        product.setProductName("测试的商品");
        product.setProductDesc("测试的商品描述");
        // 创建缩略图文件流
        File thumbnailFile = new File("/Users/qianshijie/Programming/Backend/Java/Images/o2o/xiaohuangren.jpg");
        InputStream inputStream = new FileInputStream(thumbnailFile);
        ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), inputStream);
        // 创建两个商品详情图文件流并将他们添加进详情图列表中
        File productImg1 = new File("/Users/qianshijie/Programming/Backend/Java/Images/o2o/dabaihello.jpg");
        InputStream inputStream1 = new FileInputStream(productImg1);
        File productImg2 = new File("/Users/qianshijie/Programming/Backend/Java/Images/o2o/smallsun.jpg");
        InputStream inputStream2 = new FileInputStream(productImg2);
        List<ImageHolder> productImgList = new ArrayList<>();
        productImgList.add(new ImageHolder(productImg1.getName(), inputStream1));
        productImgList.add(new ImageHolder(productImg2.getName(), inputStream2));
        // 添加商品验证
        ProductExecution productExecution = productService.updateProduct(product, thumbnail, productImgList);
        Assert.assertEquals(ProductStateEnum.SUCCESS.getState(), productExecution.getState());
    }
}
