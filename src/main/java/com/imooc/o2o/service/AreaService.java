package com.imooc.o2o.service;

import com.imooc.o2o.entity.Area;

import java.util.List;

public interface AreaService {
    public static final String AREALIST_KEY = "area_list";

    /**
     * 列出区域列表
     * @return
     */
    List<Area> getAreaList();
}
