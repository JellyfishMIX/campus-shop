package com.imooc.o2o.interceptor.shopadmin;

import com.imooc.o2o.entity.Shop;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {
    /**
     * 主要做事前拦截，即用户操作发生前，改写preHandle里面的逻辑，进行用户操作权限的拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从session中获取当前选择的店铺
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 从session中获取当前用户可操作的店铺列表
        List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
        // 非空判断
        if (currentShop != null && shopList != null) {
            // 遍历可操作的店铺列表
            for (Shop shop:shopList) {
                // 如果当前店铺在可操作的列表里返回true，则进行接下来的用户操作
                if (shop.getShopId().equals(currentShop.getShopId())) {
                    return true;
                }
            }
        }
        // 若不满足拦截器的验证则返回false，终止用户操作的执行，跳转后台用户登录页面
        String loginUrl = request.getContextPath() + "/admin/login";
        response.sendRedirect(loginUrl);
        return false;
    }
}
