package com.imooc.o2o.web.shopadmin;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/shopadmin")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 查询指定店铺下的所有商品类别
     * @param request
     * @return
     */
    @RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getProductCategoryList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // Shop shop = new Shop();
        // shop.setShopId(2L);
        // request.getSession().setAttribute("currentShop", shop);

        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        List<ProductCategory> productCategoryList;
        if (currentShop != null && currentShop.getShopId() > 0) {
            productCategoryList = productCategoryService.getProductCategoryList(currentShop.getShopId());
            modelMap.put("success", true);
            modelMap.put("productCategoryList", productCategoryList);
            return modelMap;
        } else {
            ProductCategoryStateEnum productCategoryStateEnum = ProductCategoryStateEnum.INNER_ERROR;
            modelMap.put("success", false);
            modelMap.put("errState", productCategoryStateEnum.getState());
            modelMap.put("errStateInfo", productCategoryStateEnum.getStateInfo());
            return modelMap;
        }
    }

    /**
     * 批量新增商品类别
     * @param productCategoryList, request
     * @return rows 返回影响的行数
     */
    @RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> batchAddProductCategory(@RequestBody List<ProductCategory> productCategoryList, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // 给productCategoryList中的productCategory设置上当前店铺的Id
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        for (ProductCategory productCategory:productCategoryList) {
            productCategory.setShopId(currentShop.getShopId());
        }

        if (productCategoryList != null && productCategoryList.size() > 0) {
            try {
                ProductCategoryExecution productCategoryExecution = productCategoryService.batchAddProductCategory(productCategoryList);
                if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errState", productCategoryExecution.getState());
                    modelMap.put("errStateInfo", productCategoryExecution.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入至少一个商品类别");
        }
        return modelMap;
    }

    /**
     * 将此类别下的商品中的类别id置空，再删除掉该商品类别
     * @param productCategoryId
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteproductcategory", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> deleteProductCategory(Long productCategoryId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (productCategoryId != null && productCategoryId > 0) {
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            ProductCategoryExecution productCategoryExecution = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
            if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errState", productCategoryExecution.getState());
                modelMap.put("errStateInfo", productCategoryExecution.getStateInfo());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "productCategoryId为空");
        }
        return modelMap;
    }
}
