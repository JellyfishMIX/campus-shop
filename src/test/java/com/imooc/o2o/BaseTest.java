package com.imooc.o2o;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 */
// 告诉Spring使用SpringJUnit4ClassRunner这个类来跑单元测试
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件的位置，让Spring在启动单元测试时加载classpath:spring/spring-dao.xml和classpath:spring/spring-service.xml
// classpath:spring/spring-dao.xml为dao层提供配置，spring-service为service层提供配置
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public abstract class BaseTest {

}
