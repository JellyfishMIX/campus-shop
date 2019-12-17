package com.imooc.o2o.dao;

import com.imooc.o2o.entity.PersonInfo;
import org.apache.ibatis.annotations.Param;

public interface PersonInfoDao {
    /**
     * 根据userId查找用户信息
     * @param userId
     * @return
     */
    PersonInfo queryPersonInfo(@Param("userId") long userId);

    /**
     * 添加用户信息
     * @param user
     * @return
     */
    int insertPersonInfo(PersonInfo user);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    int updatePersonInfo(PersonInfo user);
}
