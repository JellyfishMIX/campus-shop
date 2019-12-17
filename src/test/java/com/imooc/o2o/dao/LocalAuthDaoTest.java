package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class LocalAuthDaoTest extends BaseTest {
    @Autowired
    private LocalAuthDao localAuthDao;
    private static final String username = "testusername";
    private static final String password = "testpassword";

    @Test
    @Ignore
    public void testInsertLocalAuth() {
        // 新增一条平台账号信息
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1L);
        // 给平台绑定上用户信息
        localAuth.setPersonInfo(personInfo);
        // 给设置上用户名和密码
        localAuth.setUsername(username);
        localAuth.setPassword(password);
        localAuth.setCreateTime(new Date());
        localAuth.setLastEditTime(new Date());
        int effectedNum = localAuthDao.insertLocalAuth(localAuth);
        Assert.assertEquals(1, effectedNum);
    }

    @Test
    @Ignore
    public void testQueryLocalByUserNameAndPwd() throws Exception {
        // 按照账号和密码查询用户信息
        LocalAuth localAuth = localAuthDao.queryLocalAuthByUsernameAndPassword(username, password);
        Assert.assertEquals("张全蛋", localAuth.getPersonInfo().getName());
    }

    @Test
    @Ignore
    public void testCQueryLocalByUserId() {
        // 按照用户Id查询平台账号，进而获取用户信息
        LocalAuth localAuth = localAuthDao.queryLocalAuthByUserId(1L);
        Assert.assertEquals("张全蛋", localAuth.getPersonInfo().getName());
    }

    @Test
    @Ignore
    public void testUpdateLocalAuth() {
        // 按照平台用户Id，平台账号，以及旧密码修改平台账号密码
        Date currentTime = new Date();
        int effectedNum = localAuthDao.updateLocalAuth(1L, username, password, password + "newnew", currentTime);
        Assert.assertEquals(1, effectedNum);
        // 查询该平台账号的最新消息
        LocalAuth localAuth = localAuthDao.queryLocalAuthByUserId(1L);
        // 输出新密码
        System.out.println(localAuth.getPassword());
    }
}
