package com.imooc.o2o.web.frontdesk;

import com.imooc.o2o.entity.Product;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/frontdesk")
public class ProductDetailController {
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/listproductdetail", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductDetail(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        Product product = null;
        if (productId != -1L) {
            product = productService.getProductByProductId(productId);
            modelMap.put("success", true);
            modelMap.put("product", product);
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errState", ProductStateEnum.PRODUCT_ID_EMPTY.getState());
            modelMap.put("errStateInfo", ProductStateEnum.PRODUCT_ID_EMPTY.getStateInfo());
            return modelMap;
        }
    }
}
