package com.imooc.o2o.service;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.HeadLine;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HeadLineServiceTest extends BaseTest {
    @Autowired
    private HeadLineService headLineService;

    @Test
    @Ignore
    public void getHeadLineList() {
        List<HeadLine> headLineList = headLineService.getHeadLineList(new HeadLine());
        Assert.assertEquals(4, headLineList.size());
    }
}
