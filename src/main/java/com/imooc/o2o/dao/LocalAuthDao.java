package com.imooc.o2o.dao;

import com.imooc.o2o.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface LocalAuthDao {
    /**
     * 通过账号和密码查询对应信息，登录用
     * @param username
     * @param password
     * @return
     */
    LocalAuth queryLocalAuthByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    /**
     * 通过用户Id查询对应localAuth
     * @param userId
     * @return
     */
    LocalAuth queryLocalAuthByUserId(@Param("userId") long userId);

    /**
     * 添加平台账号
     * @param localAuth
     * @return
     */
    int insertLocalAuth(LocalAuth localAuth);

    /**
     * 通过userId, userName, password更改密码
     * @param userId
     * @param username
     * @param password
     * @param newPassword
     * @param lastEditTime
     * @return
     */
    int updateLocalAuth(@Param("userId") long userId, @Param("username") String username, @Param("password") String password, @Param("newPassword") String newPassword, @Param("lastEditTime")Date lastEditTime);

    /**
     * 通过username查询对应信息
     * @param username
     * @return
     */
    LocalAuth queryLocalAuthByUsername(@Param("username") String username);
}
