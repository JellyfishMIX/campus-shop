package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/shopadmin")
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    /**
     * 返回店铺种类列表和区域列表
     * @return
     */
    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList;
        List<Area> areaList;
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
    }

    /**
     * 当前端通过http协议访问服务器时，request中包含前端传来的http请求头中的所有参数信息
     * 返回值为Map型，返回一些必要的键值对结果
     * 1.接收并转化相应的逻辑，包括店铺信息及图片信息
     * 2.注册店铺
     * 3.返回结果，此步在1. 2. 中
     */
    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        // 1.接收并转化相应的逻辑，包括店铺信息及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop;
        try {
            shop = mapper.readValue(shopStr, Shop.class);   // 把shopStr转换成shop实体类
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        // 开始进行图片相关处理
        CommonsMultipartFile shopImg;    // Spring自带的CommonsMultipartFile
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext()); // 文件上传解析器，解析request里的文件信息。参数：从request中提取本次会话上传的文件内容
        if (commonsMultipartResolver.isMultipart(request)) {    // 判断request中是否有上传的文件内容(是否有流文件)
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;    // 强制类型转换
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }

        // 2.注册店铺
        if (shop != null && shopImg != null) {
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("usr");
            shop.setOwner(owner);
            ShopExecution shopExecution;

            try {
                ImageHolder imageHolder = new ImageHolder();
                imageHolder.setImage(shopImg.getInputStream());
                imageHolder.setImageName(shopImg.getOriginalFilename());
                shopExecution = shopService.addShop(shop, imageHolder);
                if (shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    // 该用户可以操作的店铺列表，把新创建的店铺加入店铺列表中
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if (shopList == null || shopList.size() == 0) {
                        shopList = new ArrayList<Shop>();
                    }
                    shopList.add(shopExecution.getShop());
                    request.getSession().setAttribute("shopList", shopList);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }
            } catch (ShopOperationException | IOException e) {
                e.printStackTrace();
                modelMap.put("success", false);
                modelMap.put("errMsg: ", e.toString());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "店铺信息为空，或店铺无图片");
            return modelMap;
        }
    }

    /**
     * 通过店铺id获取店铺信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/getshopbyshopid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopByShopId(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            try {
                Shop shop = shopService.getShopByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("success", true);
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                return modelMap;
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
            return modelMap;
        }
    }

    /**
     * 更改店铺信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        // 1.接收并转化相应的逻辑，包括店铺信息及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop;
        try {
            shop = mapper.readValue(shopStr, Shop.class);   // 把shopStr转换成shop实体类
            shop.setShopName(new String(shop.getShopName().getBytes("8859_1"), "utf8"));
            shop.setShopDesc(new String(shop.getShopDesc().getBytes("8859_1"), "utf8"));
            shop.setShopAddr(new String(shop.getShopAddr().getBytes("8859_1"), "utf8"));
            shop.setPhone(new String(shop.getPhone().getBytes("8859_1"), "utf8"));
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        // 开始进行图片相关处理
        CommonsMultipartFile shopImg = null;    // Spring自带的CommonsMultipartFile
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext()); // 文件上传解析器，解析request里的文件信息。参数：从request中提取本次会话上传的文件内容
        if (commonsMultipartResolver.isMultipart(request)) {    // 判断request中是否有上传的文件内容(是否有流文件)
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;    // 强制类型转换
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }

        // 2.注册店铺
        if (shop != null && shop.getShopId() != null) {
            PersonInfo owner = new PersonInfo();
            owner.setUserId(1L);
            shop.setOwner(owner);
            ShopExecution shopExecution;

            try {
                ImageHolder imageHolder = new ImageHolder();
                if (shopImg == null) {
                    imageHolder.setImageName(null);
                    imageHolder.setImage(null);
                    shopExecution = shopService.modifyShop(shop, imageHolder);
                } else {
                    imageHolder.setImageName(shopImg.getOriginalFilename());
                    imageHolder.setImage(shopImg.getInputStream());
                    shopExecution = shopService.addShop(shop, imageHolder);
                }
                if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }
            } catch (ShopOperationException | IOException e) {
                e.printStackTrace();
                modelMap.put("success", false);
                modelMap.put("errMsg: ", e.toString());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "缺少店铺id");
            return modelMap;
        }
    }

    /**
     * 根据shopCondition分页返回列表数据和店铺总数
     * @param request
     * @return
     */
    @RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopListAndCount(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        PersonInfo user = new PersonInfo();
        // user.setUserId(1L);
        // user.setName("张全蛋");
        // request.getSession().setAttribute("user", user);
        user = (PersonInfo) request.getSession().getAttribute("user");
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution shopExecution = shopService.getShopListAndCount(shopCondition, 0, 8);

            // 列出店铺列表成功后，将店铺放入session中作为权限验证依据，即该账号只能操作它自己的店铺
            request.getSession().setAttribute("shopList", shopExecution.getShopList());
            modelMap.put("success", true);
            modelMap.put("shopList", shopExecution.getShopList());
            modelMap.put("user", user);
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
    }

    /**
     * 传入shopId，设置currentShop的shopId
     * @param request
     * @return
     */
    @RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId < 0) {
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if (currentShopObj == null) {
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shop/shoplist");
                return modelMap;
            } else {
                Shop currentShop = (Shop) currentShopObj;
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
                return modelMap;
            }
        } else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("redirect", false);
            return modelMap;
        }
    }
}
