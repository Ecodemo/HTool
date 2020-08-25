package com.ecodemo.magic.box.app.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class AESUtil {
	/**
	 * 生成密钥
	 * @throws Exception 
	 */
	public static byte[] initKey(int length) throws Exception {
		//密钥生成器
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		//初始化密钥生成器
		keyGen.init(length);  //默认128，获得无政策权限后可用192或256
		//生成密钥
		SecretKey secretKey = keyGen.generateKey();
		return secretKey.getEncoded();
	}

	//加密
	public static byte[] encryptAES(byte[] content, byte[] key) throws Exception { 
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");  
		Cipher cipher = Cipher.getInstance("AES");
		//使用CBC模式，需要一个向量iv，可增加加密算法的强度
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);  
		byte[] encrypted = cipher.doFinal(content);  
		return encrypted;
	}  

	//解密  
	public static byte[] decryptAES(byte[] content, byte[] key) throws Exception {  
		try {   
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");  
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);  
			try {  
				byte[] original = cipher.doFinal(content);    
				return original;  
			} catch (Exception e) {  
				System.out.println(e.toString());  
				return null;  
			}  
		} catch (Exception ex) {  
			System.out.println(ex.toString());  
			return null;  
		}  
	} 
}

