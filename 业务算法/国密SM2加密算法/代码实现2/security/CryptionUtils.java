package com.sky.skyserver.util.security;

import com.sky.skyserver.test.crypto.SM2;
import com.sky.skyserver.test.crypto.SM2KeyPair;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class CryptionUtils {
    /**
     * 加密
     * @param passwordOriginal
     * @return
     */
    public static PasswordKeys encryption(String passwordOriginal){
        PasswordKeys passwordKeys = new PasswordKeys();
        SM2 sm2 = new SM2();
        SM2KeyPair keys = sm2.generateKeyPair();
        ECPoint pubKey = keys.getPublicKey();
        BigInteger priKey = keys.getPrivateKey();
        byte[] data = sm2.encrypt(passwordOriginal, pubKey);
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b :data) {
            stringBuffer.append(Byte.toString(b)+"O");//使用大写字母O进行分隔
        }
        String passwordEncryption = stringBuffer.substring(0,stringBuffer.length()-1);
        passwordKeys.setPrikey(String.valueOf(priKey));
        passwordKeys.setPasswordEncryption(passwordEncryption);
        return passwordKeys;
    }

    /**
     * 解密
     * @param passwordEncryption
     * @param prikey
     * @return
     */
    public static String decryption(String passwordEncryption,String prikey){
        SM2 sm2 = new SM2();
        String[] strs = passwordEncryption.split("O");//使用大写字母O进行分隔
        byte[] password  = new byte[strs.length];
        for (int i =0;i<strs.length;i++) {
            password[i] = Byte.parseByte(strs[i]);
        }
        String passwordOriginal = sm2.decrypt(password,new BigInteger(prikey));
        return passwordOriginal;
    }
}
