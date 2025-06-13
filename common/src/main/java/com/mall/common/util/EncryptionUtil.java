package com.mall.common.util;

import cn.hutool.crypto.SecureUtil;
public class EncryptionUtil {

    public static String generateSalt(){
       return SecureUtil.generateKey("AES").toString();
    }

    public static String encryption(String salt,String originPass){
        return SecureUtil.sha256(salt+originPass);
    }

    public static boolean isValid(String salt,String originPass,String inputPass){
        String encryptedInput = SecureUtil.sha256(salt + inputPass);
        return encryptedInput.equals(originPass);
    }
}
