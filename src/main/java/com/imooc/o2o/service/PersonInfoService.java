package com.imooc.o2o.service;

import com.imooc.o2o.dto.PersonInfoExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.exceptions.PersonInfoOperationException;

public interface PersonInfoService {
    /**
     * 添加用户信息
     * @param personInfo
     * @return
     * @throws PersonInfoOperationException
     */
    PersonInfoExecution addPersonInfo(PersonInfo personInfo) throws PersonInfoOperationException;

    /**
     * 修改用户信息
     * @param personInfo
     * @return
     * @throws PersonInfoOperationException
     */
    PersonInfoExecution modifyPersonInfo(PersonInfo personInfo) throws PersonInfoOperationException;

    /**
     * 根据userId查找用户信息
     * @param userId
     * @return
     */
    PersonInfo getPersonInfo(long userId);
}
