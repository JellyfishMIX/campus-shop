package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.LocalAuthDao;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.exceptions.LocalAuthOperationException;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {
    @Autowired
    private LocalAuthDao localAuthDao;

    /**
     * 通过账号和密码查询对应信息，登录用
     * @param username
     * @param password
     * @return
     */
    @Override
    public LocalAuth getLocalAuthByUserNameAndPassword(String username, String password) {
        return localAuthDao.queryLocalAuthByUsernameAndPassword(username, MD5.getMd5(password));
    }

    /**
     * 通过用户Id查询对应localAuth
     * @param userId
     * @return
     */
    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthDao.queryLocalAuthByUserId(userId);
    }

    /**
     * 新增一个本地账户
     * @param localAuth
     * @return
     * @throws LocalAuthOperationException
     */
    @Override
    @Transactional
    public LocalAuthExecution AddLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
        // 空值判断，传入传入localAuth账号密码，均不能为空，否则直接返回报错
        if (localAuth == null || localAuth.getUsername() == null || localAuth.getPassword() == null) {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        // 查询此用户是否已经注册过平台账号
        LocalAuth tempLocalAuth = localAuthDao.queryLocalAuthByUsernameAndPassword(localAuth.getUsername(), MD5.getMd5(localAuth.getPassword()));
        // 如果注册过则直接退出，以保证平台账号的唯一性
        if (tempLocalAuth != null) {
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        }
        try {
            // 如果之前没有注册过账号，则创建一个平台账号
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            // 对密码进行MD5加密
            localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
            int effectedNum = localAuthDao.insertLocalAuth(localAuth);
            // 判断是否创建成功
            if (effectedNum < 0) {
                throw new LocalAuthOperationException("账号绑定失败");
            } else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new LocalAuthOperationException("insertLocalAuth error: " + e.getMessage());
        }
    }

    /**
     * 绑定微信生成平台专属的账号
     * @param localAuth
     * @return
     * @throws LocalAuthOperationException
     */
    @Override
    @Transactional
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
        // 空值判断，传入localAuth账号密码，用户信息特别是userId不能为空，否则直接返回报错
        if (localAuth == null || localAuth.getUsername() == null || localAuth.getPassword() == null || localAuth.getPersonInfo() == null || localAuth.getPersonInfo().getUserId() == null) {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        // 查询此用户是否已绑定过平台账号
        LocalAuth tempLocalAuth = localAuthDao.queryLocalAuthByUserId(localAuth.getPersonInfo().getUserId());
        // 如果绑定过则直接退出，以保证平台账号的唯一性
        if (tempLocalAuth != null) {
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        }
        try {
            // 如果之前没有绑定过账号，则创建一个平台账号与该用户绑定
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            // 对密码进行MD5加密
            localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
            int effectedNum = localAuthDao.insertLocalAuth(localAuth);
            // 判断创建是否成功
            if (effectedNum < 0) {
                throw new LocalAuthOperationException("账号绑定失败");
            } else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new LocalAuthOperationException("insertLocalAuth error: " + e.getMessage());
        }
    }

    /**
     * 修改平台账号的登录密码
     * @param userId
     * @param username
     * @param password
     * @param newPassword
     * @return
     * @throws LocalAuthOperationException
     */
    @Override
    @Transactional
    public LocalAuthExecution modifyLocalAuth(long userId, String username, String password, String newPassword) throws LocalAuthOperationException {
        // 非空判断，判断传入的用户Id，账号，新旧密码是否相同，若不满足条件则返回错误信息
        if (userId != -1L && username != null && password != null && newPassword != null && !password.equals(newPassword)) {
            try {
                // 更改密码，对新密码进行MD5加密
                int effectedNum = localAuthDao.updateLocalAuth(userId, username, MD5.getMd5(password), MD5.getMd5(newPassword), new Date());
                // 判断更新是否成功
                if (effectedNum <= 0) {
                    throw new LocalAuthOperationException("更新密码失败");
                }
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            } catch (Exception e) {
                throw new LocalAuthOperationException("更新密码失败, errMsg: " + e.getMessage());
            }
        } else {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
    }

    /**
     * 检查用户名是否已被注册
     * @param username
     * @return
     */
    @Override
    public LocalAuthExecution checkUsername(String username) {
        LocalAuth localAuth = localAuthDao.queryLocalAuthByUsername(username);
        if (localAuth != null) {
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        } else {
            return new LocalAuthExecution(LocalAuthStateEnum.NOT_FOUND);
        }
    }
}
