package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * AES对称密码算法
 */
@Slf4j
public class AESUtil {

    public static final String KEY = "Andon";
    public static final ThreadLocal<SimpleDateFormat> FORMAT_DAY = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    /**
     * 加密
     */
    public static byte[] encrypt(String content) {
        byte[] encryptResult = null; //密文
        try {
            // 创建AES的Key生成器
            KeyGenerator aes = KeyGenerator.getInstance("AES");
            // 由自定义的key+时间作为随机数初始化256位的密钥生成器
            aes.init(256, new SecureRandom((KEY + FORMAT_DAY.get().format(System.currentTimeMillis())).getBytes(StandardCharsets.UTF_8)));
            // 生成秘钥
            SecretKey secretKey = aes.generateKey();
            // 转换为AES专用秘钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            // 加密
            encryptResult = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("encrypt failure!! error={}", e.getMessage());
            e.printStackTrace();
        }
        return encryptResult;
    }

    /**
     * 解密
     */
    public static byte[] decrypt(byte[] content) {
        byte[] decryptResult = null; //明文
        try {
            // 创建AES的Key生成器
            KeyGenerator aes = KeyGenerator.getInstance("AES");
            // 由自定义的key+时间作为随机数初始化256位的秘钥生成器
            aes.init(256, new SecureRandom((KEY + FORMAT_DAY.get().format(System.currentTimeMillis())).getBytes(StandardCharsets.UTF_8)));
            // 生成秘钥
            SecretKey secretKey = aes.generateKey();
            // 转换为AES专用秘钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化加密模式的密码器
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            // 解密
            decryptResult = cipher.doFinal(content);
        } catch (Exception e) {
            log.error("decrypt failure!! error={}", e.getMessage());
            e.printStackTrace();
        }
        return decryptResult;
    }

    /**
     * 字节数组转换成16进制
     */
    public static String parseByteToHexStr(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (byte aByte : bytes) {
            String hexString = Integer.toHexString(aByte & 0xFF);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            stringBuffer.append(hexString.toUpperCase());
        }
        return stringBuffer.toString();
    }

    /**
     * 16进制转换成字节数组
     */
    public static byte[] parseHexStrToByte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] bytes = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            bytes[i] = (byte) (high * 16 + low);
        }
        return bytes;
    }

    /**
     * Base64处理字节数组转换为字符串
     */
    public static String base64EncodeToString(byte[] bytes) {
        try {
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            log.error("base64EncodeToString failure!! error={}", e.getMessage());
            return null;
        }
    }

    /**
     * Base64处理字符串转换为字节数组
     */
    public static byte[] base64DecodeToByte(String String) {
        try {
            return Base64.getDecoder().decode(String);
        } catch (Exception e) {
            log.error("base64DecodeToByte failure!! error={}", e.getMessage());
            return null;
        }
    }
}
