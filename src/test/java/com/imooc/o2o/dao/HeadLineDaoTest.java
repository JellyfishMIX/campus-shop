package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.HeadLine;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HeadLineDaoTest extends BaseTest {
    @Autowired
    private HeadLineDao headLineDao;

    @Test
    @Ignore
    public void testQueryHeadLineList() {
        List<HeadLine> headLineList = headLineDao.queryHeadLineList(new HeadLine());
        Assert.assertEquals(4, headLineList.size());
    }
}
