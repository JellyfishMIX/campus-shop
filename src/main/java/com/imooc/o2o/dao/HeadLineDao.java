package com.imooc.o2o.dao;

import com.imooc.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HeadLineDao {
    /**
     * 根据传入的条件，头条名查询头条
     * @param headLine
     * @return
     */
    List<HeadLine> queryHeadLineList(@Param("headLineCondition") HeadLine headLine);
}