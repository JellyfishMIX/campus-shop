package com.imooc.o2o.web.local;

import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.dto.PersonInfoExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.enums.PersonInfoStateEnum;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/localauth")
public class LocalAuthController {
    @Autowired
    private LocalAuthService localAuthService;
    @Autowired
    private PersonInfoService personInfoService;

    @RequestMapping(value = "/registerLocalAuth", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerLocalAuth(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        // 获取输入的用户名
        String username = HttpServletRequestUtil.getString(request, "username");
        // 获取输入的密码
        String password = HttpServletRequestUtil.getString(request, "password");
        // 非空判断，要求账号和密码非空
        if (username != null && password != null) {
            // 先创建PersonInfo
            PersonInfo personInfo = new PersonInfo();
            personInfo.setName("personInfoName");
            personInfo.setEnableStatus(1);
            personInfo.setUserType(1);
            personInfo.setCreateTime(new Date());
            personInfo.setLastEditTime(new Date());
            PersonInfoExecution personInfoExecution = personInfoService.addPersonInfo(personInfo);

            if (personInfoExecution.getState() == PersonInfoStateEnum.SUCCESS.getState()) {
                // 创建本地账号
                LocalAuth localAuth = new LocalAuth();
                localAuth.setUsername(username);
                localAuth.setPassword(password);
                localAuth.setPersonInfo(personInfoExecution.getPersonInfo());
                LocalAuthExecution localAuthExecution = localAuthService.AddLocalAuth(localAuth);

                if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    return modelMap;
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errState", localAuthExecution.getState());
                    modelMap.put("errStateInfo", localAuthExecution.getStateInfo());
                    return modelMap;
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("errState", personInfoExecution.getState());
                modelMap.put("errStateInfo", personInfoExecution.getStateInfo());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "账号和密码存在空值");
            return modelMap;
        }
    }

    /**
     * 将用户信息与平台账号绑定
     * @param request
     * @return
     */
    @RequestMapping(value = "/bindLocalAuth", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    private Map<String, Object> bindLocalAuth(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        // 获取输入的账号
        String username = HttpServletRequestUtil.getString(request, "username");
        // 获取输入的密码
        String password = HttpServletRequestUtil.getString(request, "password");
        // 从session中获取当前用户信息（用户一旦通过微信登录后，便能获取用户信息）
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        // 非空判断，要求账号密码及当前用户的session非空
        if (username != null && password != null && user != null && user.getUserId() != null) {
            // 创建localAuth对象并赋值
            LocalAuth localAuth = new LocalAuth();
            localAuth.setUsername(username);
            localAuth.setPassword(password);
            localAuth.setPersonInfo(user);
            // 绑定账号
            LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
            if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
                return modelMap;
            } else {
                modelMap.put("success", false);
                modelMap.put("errState", localAuthExecution.getState());
                modelMap.put("errStateInfo", localAuthExecution.getStateInfo());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名和密码不能为空");
            return modelMap;
        }
    }

    /**
     * 修改平台用户账号的密码
     * @param request
     * @return
     */
    @RequestMapping(value = "/changelocalpwd", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取账号
        String username = HttpServletRequestUtil.getString(request, "username");
        // 获取原密码
        String password = HttpServletRequestUtil.getString(request, "password");
        // 获取新密码
        String newPassword = HttpServletRequestUtil.getString(request, "newPassword");

        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        request.getSession().setAttribute("user", user);
        // 从session中获取当前用户信息（用户一旦通过微信登录后，便能获取用户信息）
        // PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        // 非空判断，要求账号新旧密码即当前的用户session非空，且新密码与旧密码不相同
        if (username != null && password != null && newPassword != null && user != null && user.getUserId() != null && !password.equals(newPassword)) {
            try {
                // 查看原账号，与输入的账号是否一致，不一致则认为是非法操作
                LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
                if (localAuth == null || !localAuth.getUsername().equals(username)) {
                    // 不一致则直接退出
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "输入的账号非本次登录的账号");
                    return modelMap;
                }
                // 修改平台账号的用户密码
                LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(user.getUserId(), username, password, newPassword);
                if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    return modelMap;
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errState", localAuthExecution.getState());
                    modelMap.put("errStateInfo", localAuthExecution.getStateInfo());
                    return modelMap;
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMg", "请输入密码");
            return modelMap;
        }
    }

    /**
     * 检查登录
     * @param request
     * @return
     */
    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> loginCheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取账号
        String username = HttpServletRequestUtil.getString(request, "username");
        // 获取原密码
        String password = HttpServletRequestUtil.getString(request, "password");
        // 非空校验
        if (username != null && password != null) {
            // 传入账号和密码去获取平台账号信息
            LocalAuth localAuth = localAuthService.getLocalAuthByUserNameAndPassword(username, password);
            if (localAuth != null) {
                // 若能获取到账号信息则为登录成功
                modelMap.put("success", true);
                // 同时在Session中设置用户信息
                request.getSession().setAttribute("user", localAuth.getPersonInfo());
                return modelMap;
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "用户名和密码均不能为空");
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名和密码均不能为空");
            return modelMap;
        }
    }

    /**
     * 检查用户名是否已被注册
     * @param request
     * @return
     */
    @RequestMapping(value = "/checkusername", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> checkUsername(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        String username = HttpServletRequestUtil.getString(request, "username");
        LocalAuthExecution localAuthExecution = localAuthService.checkUsername(username);
        if (localAuthExecution.getState() == LocalAuthStateEnum.ONLY_ONE_ACCOUNT.getState()) {
            modelMap.put("success", false);
            modelMap.put("errState", localAuthExecution.getState());
            modelMap.put("errStateInfo", localAuthExecution.getStateInfo());
            return modelMap;
        } else {
            modelMap.put("success", true);
            return modelMap;
        }
    }

    /**
     * 当用户点击登出按钮时，注销session
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> logout(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 将用户的session置空
        request.getSession().setAttribute("user", null);
        modelMap.put("success", true);
        return modelMap;
    }
}
