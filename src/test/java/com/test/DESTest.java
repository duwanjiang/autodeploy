package com.test;


import com.csnt.deploy.util.EncryptUtil;

/**
 * @ClassName DESTest
 * @Description TODO
 * @Author duwanjiang
 * @Date 2019/12/20 10:40
 * Version 1.0
 **/
public class DESTest {

    public static void main(String[] args) {
        String password = "11f30b61fa30a0c9f0e60f8178086383d1";
        try {
            String encryptword = EncryptUtil.encrypt(password);
            System.out.println(encryptword);
            System.out.println(EncryptUtil.decrypt(encryptword));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
