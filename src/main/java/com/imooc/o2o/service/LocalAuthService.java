package com.imooc.o2o.service;

import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.exceptions.LocalAuthOperationException;
import org.springframework.cglib.core.Local;

public interface LocalAuthService {
    /**
     * 通过账号和密码查询对应信息，登录用
     * @param username
     * @param password
     * @return
     */
    LocalAuth getLocalAuthByUserNameAndPassword(String username, String password);

    /**
     * 通过用户Id查询对应localAuth
     * @param userId
     * @return
     */
    LocalAuth getLocalAuthByUserId(long userId);

    /**
     * 新增一个本地账户
     * @param localAuth
     * @return
     * @throws LocalAuthOperationException
     */
    LocalAuthExecution AddLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

    /**
     * 绑定微信生成平台专属的账号
     * @param localAuth
     * @return
     * @throws LocalAuthOperationException
     */
    LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

    /**
     * 修改平台账号的登录密码
     * @param userId
     * @param username
     * @param password
     * @param newPassword
     * @return
     * @throws LocalAuthOperationException
     */
    LocalAuthExecution modifyLocalAuth(long userId, String username, String password, String newPassword) throws LocalAuthOperationException;

    /**
     * 检查用户名是否已被注册
     * @param username
     * @return
     */
    LocalAuthExecution checkUsername(String username);
}
