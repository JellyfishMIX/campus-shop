package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList;
        List<Area> areaList;
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    /**
     * 当前端通过http协议访问服务器时，request中包含前端传来的http请求头中的所有参数信息
     * 返回值为Map型，返回一些必要的键值对结果
     * 1.接收并转化相应的逻辑，包括店铺信息及图片信息
     * 2.注册店铺
     * 3.返回结果，此步在1. 2. 中
     */
    @RequestMapping(value = "registershop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();

        // 1.接收并转化相应的逻辑，包括店铺信息及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);   // 把shopStr转换成shop实体类
        } catch (Exception e) {
            modelMap.put("success:", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        // 开始进行图片相关处理
        CommonsMultipartFile shopImg = null;    // Spring自带的CommonsMultipartFile
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext()); // 文件上传解析器，解析request里的文件信息。参数：从request中提取本次会话上传的文件内容
        if (commonsMultipartResolver.isMultipart(request)) {    // 判断request中是否有上传的文件内容
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;    // 强制类型转换
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            modelMap.put("success:", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }

        // 2.注册店铺
        if (shop != null && shopImg != null) {
            PersonInfo owner = new PersonInfo();
            owner.setUserId(1L);
            shop.setOwner(owner);

            // // transfer CommonsMultipartFile To File，可替换为ImageUtil.transferCommonsMultipartFileToFile()
            // File shopImgFile = new File(PathUtil.getImgBasePath() + ImageUtil.getRandomFileName());
            // try {
            //     shopImgFile.createNewFile();
            // } catch (IOException e) {
            //     modelMap.put("success", false);
            //     modelMap.put("errMsg", e.getMessage());
            //     return modelMap;
            // }
            // try {
            //     inputStreamToFile(shopImg.getInputStream(), shopImgFile);
            // } catch (IOException e) {
            //     modelMap.put("success", false);
            //     modelMap.put("errMsg", e.getMessage());
            //     return modelMap;
            // }

            File shopImgFile = ImageUtil.transferCommonsMultipartFileToFile(shopImg);   // transfer CommonsMultipartFile To File
            ShopExecution se = shopService.addShop(shop, shopImgFile);
            if (se.getState() == ShopStateEnum.CHECK.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", se.getStateInfo());
            }
            return modelMap;
        } else {
            modelMap.put("success:", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }

    /**
     * 把inputStream塞进outputStream中
     * @param inputStream
     * @param file
     */
    public static void inputStreamToFile(InputStream inputStream, File file) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException("调用inputStreamToFile产生异常：" + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("inputStreamToFile关闭IO产生异常：" + e.getMessage());
            }
        }
    }
}
