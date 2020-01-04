package com.csnt.deploy.util;

import com.alibaba.druid.filter.config.ConfigTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName EncryptUtil
 * @Description 加/解密工具类
 * @Author duwanjiang
 * @Date 2019/12/20 11:17
 * Version 1.0
 **/
public class EncryptUtil {
    private static Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

    /**
     * 加密
     *
     * @param plainText
     * @return
     */
    public static String encrypt(String plainText) {
        try {
            return ConfigTools.encrypt(plainText);
        } catch (Exception e) {
            logger.error("加密数据异常:{}", e.toString(), e);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param cipherText
     * @return
     */
    public static String decrypt(String cipherText) {
        try {
            return ConfigTools.decrypt(cipherText);
        } catch (Exception e) {
            logger.error("加密数据异常:{}", e.toString(), e);
        }
        return null;
    }
}
