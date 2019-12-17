package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.PersonInfoDao;
import com.imooc.o2o.dto.PersonInfoExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.PersonInfoStateEnum;
import com.imooc.o2o.exceptions.PersonInfoOperationException;
import com.imooc.o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {
    @Autowired
    private PersonInfoDao personInfoDao;

    /**
     * 添加用户信息
     * @param personInfo
     * @return
     * @throws PersonInfoOperationException
     */
    @Override
    @Transactional
    public PersonInfoExecution addPersonInfo(PersonInfo personInfo) throws PersonInfoOperationException {
        if (personInfo == null || personInfo.getName() == null) {
            return new PersonInfoExecution(PersonInfoStateEnum.EMPTY);
        }
        try {
            int effectedNum = personInfoDao.insertPersonInfo(personInfo);
            if (effectedNum <= 0) {
                throw new PersonInfoOperationException("添加用户信息失败");
            } else {
                return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS, personInfo);
            }
        } catch (Exception e) {
            throw new PersonInfoOperationException("用户信息为空");
        }
    }

    /**
     * 修改用户信息
     * @param personInfo
     * @return
     * @throws PersonInfoOperationException
     */
    @Override
    @Transactional
    public PersonInfoExecution modifyPersonInfo(PersonInfo personInfo) throws PersonInfoOperationException {
        if (personInfo == null || personInfo.getName() == null) {
            return new PersonInfoExecution(PersonInfoStateEnum.EMPTY);
        }
        try {
            int effectedNum = personInfoDao.updatePersonInfo(personInfo);
            if (effectedNum <= 0) {
                throw new PersonInfoOperationException("添加用户信息失败");
            } else {
                return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS, personInfo);
            }
        } catch (Exception e) {
            throw new PersonInfoOperationException("用户信息为空");
        }
    }

    /**
     * 根据userId查找用户信息
     * @param userId
     * @return
     */
    @Override
    public PersonInfo getPersonInfo(long userId) {
        PersonInfo personInfo = personInfoDao.queryPersonInfo(userId);
        return personInfo;
    }
}
