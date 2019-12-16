package com.imooc.o2o.web.frontdesk;

import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/frontdesk")
public class ShopDetailController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductService productService;

    /**
     * 获取店铺详情及该店铺下面的商品类别列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshopdetailpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取前台传入的shopId
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        Shop shop;
        List<ProductCategory> productCategoryList;
        if (shopId != -1) {
            shop = shopService.getShopByShopId(shopId);
            // 获取店铺下面的商品类别列表
            productCategoryList = productCategoryService.getProductCategoryList(shopId);
            modelMap.put("success", true);
            modelMap.put("shop", shop);
            modelMap.put("productCategoryList", productCategoryList);
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
            return modelMap;
        }
    }

    /**
     * 展示当前店铺的商品
     * @param request
     * @return
     */
    @RequestMapping(value = "/listproductsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取店铺Id
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        // 获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        // 获取每页条数规格
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        // 空值判断
        if (shopId > -1 && pageIndex > -1 && pageSize > -1) {
            // 尝试获取商品类别Id
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            // 尝试获取模糊查找的商品名
            String productName = HttpServletRequestUtil.getString(request, "productName");
            // 组合查询条件
            Product productCondition = compactProductCondition3Search(shopId, productCategoryId, productName);
            ProductExecution productExecution = productService.getProductList(productCondition, pageIndex, pageSize);
            modelMap.put("success", true);
            modelMap.put("productList", productExecution.getProductList());
            modelMap.put("count", productExecution.getCount());
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId, empty pageIndex or empty pageSize");
            return modelMap;
        }
    }

    private Product compactProductCondition3Search(long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if (productCategoryId != -1L) {
            // 查询某个商品类别下面的商品列表
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if (productName != null) {
            // 查询名字里包含productName的店铺列表
            productCondition.setProductName(productName);
        }
        // 只允许选出状态为上架的商品
        productCondition.setEnableStatus(1);
        return productCondition;
    }
}
