package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/shopadmin")
public class ProductManagementController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    // 支持上传商品详情图的最大数量
    private static final int IMAGE_MAX_COUNT = 6;

    /**
     * 新增商品
     * @param request
     * @return
     */
    @RequestMapping(value = "/addproduct", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();

        // 接收前端参数变量的初始化，包括商品，缩略图，详情图实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product;
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        MultipartHttpServletRequest multipartHttpServletRequest;
        ImageHolder thumbnail;
        List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        // 图片处理
        try {
            // 若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
            if ( commonsMultipartResolver.isMultipart(request)) {
                multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                // 取出缩略图并构建ImageHolder对象
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumnail");
                thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
                // 取出详情图列表，并构建List<ImageHolder>列表对象，最多支持六张图片上传
                for (int i=0; i<IMAGE_MAX_COUNT; i++) {
                    CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg" + i);
                    if (productImgFile != null) {
                        // 若取出的第i个详情图片文件流不为空，则将其加入详情图列表
                        ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(), productImgFile.getInputStream());
                        productImgList.add(productImg);
                    } else {
                        // 若取出的第i个详情图片文件流为空，则终止循环
                        break;
                    }
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传的主图不能为空");
                return modelMap;
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return  modelMap;
        }

        // 取出product参数
        try {
            // 尝试获取前端传过来的表单String流并将其转换成Product实体类
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        // 若product信息，缩略图以及详情图列表非空，则开始进行商品添加操作
        if (product != null && thumbnail != null && productImgList.size() > 0) {
            try {
                // 从session中获取当前店铺的Id并赋值给product，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                Shop shop = new Shop();
                shop.setShopId(currentShop.getShopId());
                product.setShop(shop);
                // 执行添加操作
                ProductExecution productExecution = productService.addProduct(product, thumbnail, productImgList);
                if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productExecution.getStateInfo());
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errState", productExecution.getState());
                    modelMap.put("errStateInfo", productExecution.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "存在商品信息为空，例如product，thumbnail，productImgList");
        }
        return modelMap;
    }

    /**
     * 通过productId获取product
     * @param productId
     * @return
     */
    @RequestMapping(value = "/getproductbyproductid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getProductByProductId(@RequestParam Long productId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        // 非空判断
        if (productId > -1) {
            // 获取商品信息
            Product product = productService.getProductByProductId(productId);
            // 获取商品列表信息。这一步本来应该原子化，前端写得不好，一个接口想请求两个参数。因此加上了。
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(currentShop.getShopId());
            modelMap.put("success", true);
            modelMap.put("product", product);
            modelMap.put("productCategoryList", productCategoryList);
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty productId");
            return modelMap;
        }
    }

    /**
     * 编辑商品
     * @param request
     * @return
     */
    @RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        // // 是商品编辑的时候调用还是上下架操作的时候调用
        // // 若为前者则进行验证码判断，后者则跳过验证码判断
        // boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        // if (!statusChange && !CodeUtil.checkVerifyCodeFromHttpServletRequest(request)) {
        //     modelMap.put("success", false);
        //     modelMap.put("errMsg", "输入了错误的验证码");
        //     return modelMap;
        // }

        // 接收前端参数变量的初始化，包括商品，缩略图，详情图实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        // 图片处理
        try {
            // 若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
            if (commonsMultipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                // 取出缩略图并构建ImageHolder对象
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
                if (thumbnailFile != null) {
                    thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
                }
                // 取出详情图列表并构建List<ImageHolder>对象
                for (int i=0; i<IMAGE_MAX_COUNT; i++) {
                    CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg" + i);
                    if (productImgFile != null) {
                        // 若取出的第i个文件流不为空，则将其加入详情图列表
                        ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(), productImgFile.getInputStream());
                        productImgList.add(productImg);
                    } else {
                        // 若取出的第i个文件流为空，则终止循环
                        break;
                    }
                }
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        // 处理product实体类
        try {
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            // 尝试获取前端传过来的表单String流并将其转换成Product实体类
            product = mapper.readValue(productStr, Product.class);
            product.setProductName(new String(product.getProductName().getBytes("8859_1"), "utf8"));
            product.setProductDesc(new String(product.getProductDesc().getBytes("8859_1"), "utf8"));
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        // 非空判断
        if (product != null) {
            try {
                // 从session中获取当前店铺的Id并赋值给product，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                // Shop shop = new Shop();
                // shop.setShopId(currentShop.getShopId());
                product.setShop(currentShop);
                // 开始进行商品信息变更操作
                ProductExecution productExecution = productService.updateProduct(product, thumbnail, productImgList);
                if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    return modelMap;
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errState", productExecution.getState());
                    modelMap.put("errStateInfo", productExecution.getStateInfo());
                    return modelMap;
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "提交的product对象不能为空");
            return modelMap;
        }
    }

    @RequestMapping(value = "/getproductlistbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getProductListByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取前端传入的页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        // 获取前端传入的每页尺寸规格
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        // 从当前session中获取店铺信息，主要是获取shopId
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            // 获取前端传入的需要检索的条件，包括是否需要从某个商品类别及模糊查找商品名去筛选某个店铺下的商品列表
            // 筛选的条件可以进行排列组合
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            Product productCondition = compactProductCondition(currentShop.getShopId(), productCategoryId, productName);
            // 传入查询条件以及分页信息进行查询，返回相应商品列表以及符合条件的商品总数
            ProductExecution productExecution = productService.getProductList(productCondition, pageIndex, pageSize);
            modelMap.put("success", true);
            modelMap.put("productList", productExecution.getProductList());
            modelMap.put("productCount", productExecution.getCount());
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId or pageIndex or pageSize");
            return modelMap;
        }
    }

    /**
     * 整合商品查询条件
     * @param shopId
     * @param productCategoryId
     * @param productName
     * @return
     */
    private Product compactProductCondition(long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        // 若有某项条件也要整合进去
        if (productCategoryId != -1L) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if (productName != null) {
            productCondition.setProductName(productName);
        }
        return productCondition;
    }
}
