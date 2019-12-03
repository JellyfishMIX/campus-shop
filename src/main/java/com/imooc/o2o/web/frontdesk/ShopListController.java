package com.imooc.o2o.web.frontdesk;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
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
public class ShopListController {
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;

    @RequestMapping(value = "/listshopspageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopsPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        // 试着从前端请求中获取parentId
        long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        List<ShopCategory> shopCategoryList;

        if (parentId != -1) {
            // 如果parentId存在，则取出该一级shopCategory下的二级shopCategory列表
            try {
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            // 如果parentId不存在，则取出所有一级shopCategory（用户在首页选择的是全部商店列表）
            try {
                shopCategoryList = shopCategoryService.getShopCategoryList(null);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        }
        modelMap.put("shopCategoryList", shopCategoryList);

        List<Area> areaList;
        try {
            // 获取区域列表信息
            areaList = areaService.getAreaList();
            modelMap.put("areaList", areaList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        return modelMap;
    }

    @RequestMapping(value = "/listshops", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShops(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        // 获取某一页需要显示的数据条数
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        // 非空判断
        if (pageIndex > -1 && pageSize > -1) {
            // 试着获取一级类别Id
            long parentId = HttpServletRequestUtil.getLong(request, "parentId");
            // 试着获取特定二级类别Id
            long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
            // 试着获取区域Id
            int areaId = HttpServletRequestUtil.getInt(request, "areaId");
            // 试着获取模糊查询的名字
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            // 获取组合之后的查询条件
            Shop shopCondition = compactShopCondition4Search(parentId, shopCategoryId, areaId, shopName);

            // 根据查询条件和分页信息获取店铺列表，返回总数
            ShopExecution shopExecution = shopService.getShopListAndCount(shopCondition, pageIndex, pageSize);

            modelMap.put("success", true);
            modelMap.put("shopList", shopExecution.getShopList());
            modelMap.put("count", shopExecution.getCount());
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageIndex or pageSize");
            return modelMap;
        }
    }

    private Shop compactShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        // 查询某个一级shopCategory下面，所有二级shopCategory里面的店铺列表
        if (parentId != -1L) {
            ShopCategory parentCategory = new ShopCategory();
            ShopCategory childCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        // 查询某个二级shopCategory下面的店铺列表
        if (shopCategoryId != 1L) {
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        // 查询位于某个区域Id下的店铺列表
        if (areaId != -1) {
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        // 查询名字里包含shopName的店铺列表
        if (shopName != null) {
            shopCondition.setShopName(shopName);
        }

        // 前端展示的店铺都是审核成功后的合法店铺
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}
