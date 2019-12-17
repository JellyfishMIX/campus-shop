package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.PersonInfo;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class PersonInfoDaoTest extends BaseTest {
    @Autowired
    private PersonInfoDao personInfoDao;

    @Test
    @Ignore
    public void testInsertPersonInfo() {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName("李明明");
        personInfo.setGender("男");
        personInfo.setCreateTime(new Date());
        personInfo.setLastEditTime(new Date());
        personInfo.setEnableStatus(1);
        personInfo.setUserType(1);
        int effectedNum = personInfoDao.insertPersonInfo(personInfo);
        Assert.assertEquals(1, effectedNum);
    }

    @Test
    @Ignore
    public void testUpdatePersonInfo() {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(2L);
        personInfo.setName("张丽丽");
        personInfo.setGender("女");
        int effectedNum = personInfoDao.updatePersonInfo(personInfo);
        Assert.assertEquals(1, effectedNum);
    }

    @Test
    @Ignore
    public void testQueryPersonInfo() {
        PersonInfo personInfo = personInfoDao.queryPersonInfo(2L);
        Assert.assertEquals("张丽丽", personInfo.getName());
    }
}
