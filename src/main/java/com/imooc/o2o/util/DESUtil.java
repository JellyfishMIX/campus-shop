package com.imooc.o2o.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同的密钥的算法
 */
public class DESUtil {
    private static Key key;
    private static String KEY_STR = "myDESKeyStr";
    private static String CHAR_SET_NAME = "UTF-8";
    private static String ALGORITHM = "DES";

    static {
        try {
            // 生成DES算法对象
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            // 运用SHA1安全策略
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            // 设置密钥种子
            secureRandom.setSeed(KEY_STR.getBytes());
            // 初始化基于SHA1的算法对象
            generator.init(secureRandom);
            // 生成密钥对象
            key = generator.generateKey();
            generator = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 获取加密后的信息
    public static String getEncryptString(String str) {
        // 基于BASE64编码，接收byte[]并转换成String
        Base64.Encoder encoder = Base64.getEncoder();
        try {
            // 按UTF-8编码
            byte[] bytes = str.getBytes(CHAR_SET_NAME);
            // 获取加密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化密码信息
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            byte[] doFinal = cipher.doFinal(bytes);
            // byte[] to encode好的String并返回
            return encoder.encodeToString(doFinal);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    // 获取解密之后的信息
    public static String getDecryptString(String str) {
        // 基于BASE64编码，接收byte[]并转换为String
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            // 将字符串decode成byte[]
            byte[] bytes = decoder.decode(str);
            // 获取解密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化解密信息
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(bytes);
            // 返回解密后的信息
            return new String(doFinal, CHAR_SET_NAME);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 获取需要加密的字符串
     * @param args
     */
    public static void main(String[] args) {
        String encryptStr1 = getEncryptString("");
        String encryptStr2 = getEncryptString("");
        System.out.println(encryptStr1);
        System.out.println(encryptStr2);
        System.out.println(getDecryptString(encryptStr1));
        System.out.println(getDecryptString(encryptStr2));
    }
}
