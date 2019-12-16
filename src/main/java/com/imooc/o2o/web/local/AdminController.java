package com.imooc.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 账号
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * 用户注册
     *
     * @return
     */
    @RequestMapping(value = "register")
    public String register() {
        return "admin/register";
    }

    /**
     * 用户登录
     *
     * @return
     */
    @RequestMapping(value = "login")
    public String login() {
        return "admin/login";
    }

    /**
     * 用户修改密码
     *
     * @return
     */
    @RequestMapping(value = "changepwd")
    public String changePwd() {
        return "admin/changepwd";
    }

}
