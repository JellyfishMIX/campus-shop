package com.imooc.o2o.service;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductServiceTest extends BaseTest {
    @Autowired
    private ProductService productService;

    @Test
    @Ignore
    public void testAddProduct() throws ShopOperationException, FileNotFoundException {
        // 创建shopId为1且productCategoryId也为1的商品实例并给其成员变量赋值
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        product.setShop(shop);
        product.setProductCategory(productCategory);
        product.setProductName("测试商品1");
        product.setProductDesc("测试商品1的描述");
        product.setPriority(8);
        product.setCreateTime(new Date());
        product.setEnableStatus(1);

        // 创建缩略图文件
        File thumbnailFile = new File("/Users/qianshijie/Programming/Backend/Java/Images/o2o/xiaohuangren.jpg");
        InputStream inputStream = new FileInputStream(thumbnailFile);
        ImageHolder imageHolder = new ImageHolder(thumbnailFile.getName(), inputStream);
        // 创建两个商品详情图文件流，并将他们添加到详情图列表中
        File productImg1 = new File("/Users/qianshijie/Programming/Backend/Java/Images/o2o/dragonfruit.jpg");
        InputStream inputStream1 = new FileInputStream(productImg1);
        ImageHolder imageHolder1 = new ImageHolder(productImg1.getName(), inputStream1);
        File productImg2 = new File("/Users/qianshijie/Programming/Backend/Java/Images/o2o/smallsun.jpg");
        InputStream inputStream2 = new FileInputStream(productImg2);
        ImageHolder imageHolder2 = new ImageHolder(productImg2.getName(), inputStream2);
        List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
        productImgList.add(imageHolder1);
        productImgList.add(imageHolder2);

        // 添加商品验证
        ProductExecution productExecution = productService.addProduct(product, imageHolder, productImgList);
        Assert.assertEquals(ProductStateEnum.SUCCESS.getState(), productExecution.getState());
    }
}
