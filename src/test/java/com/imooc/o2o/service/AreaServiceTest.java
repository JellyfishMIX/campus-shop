package com.imooc.o2o.service;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AreaServiceTest extends BaseTest {
    @Autowired
    private AreaService areaService;
    @Autowired
    private CacheService cacheService;

    // @Test，告诉Junit这是个测试方法，凡是带有@Test标签的，JUnit都会跑
    @Test
    @Ignore
    public void testGetAreaList() {
        List<Area> areaList = areaService.getAreaList();
        Assert.assertEquals("西区", areaList.get(0).getAreaName());
    }

    @Test
    @Ignore
    public void testRemoveFromCache() {
        cacheService.removeFromCache(areaService.AREALIST_KEY);
    }
}
