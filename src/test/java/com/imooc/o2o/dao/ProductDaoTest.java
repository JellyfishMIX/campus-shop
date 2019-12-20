package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.entity.Shop;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest extends BaseTest {
    @Autowired
    private ProductDao productDao;
    @Autowired
    ProductImgDao productImgDao;

    @Test
    @Ignore
    public void testAInsertProduct() {
        Product product = new Product();
        product.setProductName("可乐");
        product.setProductDesc("测试可乐");
        product.setImgAddr("测试图片地址");
        product.setNormalPrice(8);
        product.setPromotionPrice(6);
        product.setPriority(0);
        product.setCreateTime(new Date());
        product.setEnableStatus(1);

        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        Shop shop = new Shop();
        shop.setShopId(2L);

        product.setProductCategory(productCategory);
        product.setShop(shop);

        int effectedNum = productDao.insertProduct(product);
        Assert.assertEquals(1, effectedNum);
    }

    @Test
    @Ignore
    public void testBQueryProductByProductId() {
        long productId = 1;
        // 初始化两个商品详情图实例作为productId为1的商品下的详情图
        // 批量插入到商品详情图表中
        ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddr("图片1");
        productImg1.setImgDesc("这是一条测试描述");
        productImg1.setPriority(0);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(productId);

        ProductImg productImg2 = new ProductImg();
        productImg2.setImgAddr("图片2");
        productImg2.setImgDesc("这是一条测试描述");
        productImg2.setPriority(1);
        productImg2.setCreateTime(new Date());
        productImg2.setProductId(productId);

        List<ProductImg> productImgList = new ArrayList<ProductImg>();
        productImgList.add(productImg1);
        productImgList.add(productImg2);
        int effectedNum = productImgDao.batchInsertProductImg(productImgList);
        Assert.assertEquals(2, effectedNum);

        // 查询productId为1的商品信息并校验返回的详情图实例列表是否为2
        Product product = productDao.queryProductByProductId(productId);
        Assert.assertEquals(2, product.getProductImgList().size());

        // 删除新增的这两个商品详情图实例
        effectedNum = productImgDao.deleteProductImgByProductId(productId);
        Assert.assertEquals(2, effectedNum);
    }

    @Test
    @Ignore
    public void testCUpdateProduct() {
        Product product = new Product();
        ProductCategory productCategory = new ProductCategory();
        Shop shop = new Shop();

        product.setProductId(5L);
        // product.setProductName("可乐");
        shop.setShopId(2L);
        // productCategory.setProductCategoryId(1L);
        product.setShop(shop);
        // product.setProductCategory(productCategory);
        product.setEnableStatus(0);

        // 修改productId为1的商品的名称
        // 以及商品类别并校验影响的行数是否为1
        int effectedNum = productDao.updateProduct(product);
        Assert.assertEquals(1, effectedNum);
    }

    @Test
    @Ignore
    public void testQueryProductList() throws Exception {
        Product productCondition = new Product();
        // 分页查询，预期返回两条结果
        List<Product> productList = productDao.queryProductList(productCondition, 0, 3);
        Assert.assertEquals(productList.size(), 3);

        // 查询商品总数
        int count = productDao.queryProductCount(productCondition);
        Assert.assertEquals(3, count);
        // 查询名称为"可乐"的总数，使用模糊查询，预期返回1
        productCondition.setProductName("可乐");
        productList = productDao.queryProductList(productCondition, 0, 3);
        Assert.assertEquals(1, productList.size());
        count = productDao.queryProductCount(productCondition);
        Assert.assertEquals(1, count);
    }

    /**
     * 删除商品之前，将商品类别Id置空
     */
    @Test
    @Ignore
    public void testUpdateProductCategoryToNull() {
        // 将productCategoryId为1的商品类别下面的商品的商品类别置空
        long productCategoryId = 1L;
        int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
        Assert.assertEquals(3, effectedNum);
    }
}
