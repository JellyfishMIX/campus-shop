package com.imooc.o2o.dao;

import com.imooc.o2o.entity.PersonInfo;
import org.apache.ibatis.annotations.Param;

public interface PersonInfoDao {
    /**
     * 添加用户信息
     * @param personInfo
     * @return
     */
    int insertPersonInfo(PersonInfo personInfo);

    /**
     * 修改用户信息
     * @param personInfo
     * @return
     */
    int updatePersonInfo(PersonInfo personInfo);

    /**
     * 根据userId查找用户信息
     * @param userId
     * @return
     */
    PersonInfo queryPersonInfo(@Param("userId") long userId);
}
