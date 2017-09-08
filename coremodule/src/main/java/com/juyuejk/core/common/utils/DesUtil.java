package com.juyuejk.core.common.utils;

import android.text.TextUtils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DesUtil {  
    /** 加密、解密key. */  
    private static final String PASSWORD_CRYPT_KEY = "yuyou120";  
    /** 加密算法,可用 DES,DESede,Blowfish. */  
    private final static String ALGORITHM = "DES";  

    /** 
     * 对进行过DES加密的数据解密. 
     * @param data 待进行DES加密的数�?
     * @return 返回经过DES加密后的数据 
     * @throws Exception 
     */  
    public final static String decrypt(String data) throws Exception {
        try {
            return new String(decrypt(hex2byte(data.getBytes("utf-8")),
                    PASSWORD_CRYPT_KEY.getBytes()),"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }  
    /** 
     * 对用data进行DES加密 
     * @param data DES加密数据 
     * @return 返回解密后的数据 
     * @throws Exception 
     */  
    public final static String encrypt(String data) throws Exception  {
        if (TextUtils.isEmpty(data)) return "";
        return byte2hex(encrypt(data.getBytes("utf-8"), PASSWORD_CRYPT_KEY  
                .getBytes()));  
    }  
      
    /** 
     * 用指定的key对数据进行DES加密. 
     * @param data 待加密的数据 
     * @param key DES加密的key 
     * @return 返回DES加密后的数据 
     * @throws Exception 
     */  
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {  
        // DES算法要求有一个可信任的随机数�? 
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建DESKeySpec对象  
        DESKeySpec dks = new DESKeySpec(key);  
        // 创建�?��密匙工厂，然后用它把DESKeySpec转换�? 
        // �?��SecretKey对象  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);  
        SecretKey securekey = keyFactory.generateSecret(dks);  
        // Cipher对象实际完成加密操作  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        // 用密匙初始化Cipher对象  
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);  
        // 现在，获取数据并加密  
        // 正式执行加密操作  
        return cipher.doFinal(data);  
    }  
    /** 
     * 用指定的key对数据进行DES解密. 
     * @param data 待解密的数据 
     * @param key DES解密的key 
     * @return 返回DES解密后的数据 
     * @throws Exception 
     */  
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {  
        // DES算法要求有一个可信任的随机数�? 
        SecureRandom sr = new SecureRandom();  
        // 从原始密匙数据创建一个DESKeySpec对象  
        DESKeySpec dks = new DESKeySpec(key);  
        // 创建�?��密匙工厂，然后用它把DESKeySpec对象转换�? 
        // �?��SecretKey对象  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);  
        SecretKey securekey = keyFactory.generateSecret(dks);  
        // Cipher对象实际完成解密操作  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        // 用密匙初始化Cipher对象  
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);  
        // 现在，获取数据并解密  
        // 正式执行解密操作  
        return cipher.doFinal(data);  
    }
    
    
    public static String byte2hex(byte[] b) {
    	StringBuilder hs = new StringBuilder ();  
        String stmp = "";  
        for (int n = 0; n < b.length; n++) {  
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)  
            	 hs.append("0").append(stmp);
            else  
            	hs.append(stmp);
        }  
        return hs.toString().toUpperCase();  
    }
    
//    public static byte[] hex2byte2(byte[] b) {  
//        if ((b.length % 2) != 0)  
//            throw new IllegalArgumentException("长度不是偶数");  
//        byte[] b2 = new byte[b.length / 2];  
//        for (int n = 0; n < b.length; n += 2) {  
//            String item = new String(b, n, 2);
//            b2[n / 2] = (byte)Integer.parseInt(item, 16);  
//        }  
//        return b2;  
//    } 

    public static int getASCIICode(byte b){
    	if(b>=0x30 && b <=0x39)
    		return b-0x30;
    	else{
    		return b-55;
    	}
    }
    
    public static byte[] hex2byte(byte[] b) {  
        if ((b.length % 2) != 0)  
            throw new IllegalArgumentException("长度不是偶数");  
        byte[] b2 = new byte[b.length / 2];  
        for (int n = 0; n < b.length; n += 2) {  
            b2[n / 2] = (byte) ((getASCIICode(b[n]) << 4) + getASCIICode(b[n+1]));  
        }  
        return b2;
    }
}  
