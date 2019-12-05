package com.imooc.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
// import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public class EncryptPropertySourcesPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    // 需要加密的字段
    private String[] encryptPropNames = {"jdbc.username", "jdbc.password"};

    /**
     * 对关键的属性进行转换
     * @param propertyName
     * @param propertyValue
     * @return
     */
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)) {
            // 对已加密的字段进行解密工作
            String decryptValue = DESUtil.getDecryptString(propertyValue);
            return decryptValue;
        } else {
            return propertyValue;
        }
    }

    /**
     * 该属性是否已被加密
     * @param propertyName
     * @return
     */
    private boolean isEncryptProp(String propertyName) {
        // 若等于需要加密的field，则已被加密
        for (String encryptPropertyName : encryptPropNames) {
            if (encryptPropertyName.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }
}
