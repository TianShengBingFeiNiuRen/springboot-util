package com.andon.springbootutil.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * RSA非对称加密算法
 */
@Slf4j
public class RSAUtil {

    public static ConcurrentHashMap<String, String> keyPairMap = new ConcurrentHashMap<>(); //密钥对

    public static final String KEY = "Andon"; //自定义Andon作为随机数
    public static final String PUBLIC_KEY = "public_key"; //公钥KEY
    public static final String PRIVATE_KEY = "private_key"; //私钥KEY
    public static final ThreadLocal<SimpleDateFormat> FORMAT_DAY = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
    private static final int KEY_SIZE = 2048; //密钥长度
    private static final int MAX_ENCRYPT_BLOCK = 245; //RSA加密明文大小
    private static final int MAX_DECRYPT_BLOCK = 256; //RSA解密密文大小

    /**
     * 生成密钥对
     */
    private static void generateKeyPair() {
        try {
            // 创建RSA密钥对的生成器实例
            KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
            // 由自定义的key+时间作为随机数初始化2048位的密钥对生成器
            SecureRandom secureRandom = new SecureRandom((KEY + FORMAT_DAY.get().format(System.currentTimeMillis())).getBytes(StandardCharsets.UTF_8));
            rsa.initialize(KEY_SIZE, secureRandom);
            // 生成密钥对
            KeyPair keyPair = rsa.generateKeyPair();
            // 获取公钥
            PublicKey aPublic = keyPair.getPublic();
            byte[] publicKeyByte = aPublic.getEncoded();
            String publicKeyStr = RSAUtil.base64EncodeToString(publicKeyByte);
            // 获取私钥
            PrivateKey aPrivate = keyPair.getPrivate();
            byte[] privateKeyByte = aPrivate.getEncoded();
            String privateKeyStr = RSAUtil.base64EncodeToString(privateKeyByte);
            if (!ObjectUtils.isEmpty(publicKeyStr) && !ObjectUtils.isEmpty(privateKeyStr)) {
                keyPairMap.put(PUBLIC_KEY, publicKeyStr);
                keyPairMap.put(PRIVATE_KEY, privateKeyStr);
                log.info("generateKeyPair!! keyPairMap:{}", JSONObject.toJSONString(keyPairMap));
            }
        } catch (Exception e) {
            log.error("getPublicKey failure!! error={}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取公钥
     */
    public static String getRSAPublicKey() {
        String publicKeyStr = RSAUtil.keyPairMap.get(PUBLIC_KEY);
        if (ObjectUtils.isEmpty(publicKeyStr)) {
            RSAUtil.generateKeyPair();
            publicKeyStr = RSAUtil.keyPairMap.get(PUBLIC_KEY);
        }
        return publicKeyStr;
    }

    /**
     * 获取私钥
     */
    public static String getRSAPrivateKey() {
        String privateStr = RSAUtil.keyPairMap.get(PRIVATE_KEY);
        if (ObjectUtils.isEmpty(privateStr)) {
            RSAUtil.generateKeyPair();
            privateStr = RSAUtil.keyPairMap.get(PRIVATE_KEY);
        }
        return privateStr;
    }

    /**
     * 公钥加密
     */
    public static byte[] publicEncrypt(String content) {
        byte[] encryptResult = null;
        try {
            // 获取公钥
            String publicKeyStr = keyPairMap.get(PUBLIC_KEY);
            if (ObjectUtils.isEmpty(publicKeyStr)) {
                // 生成公钥
                generateKeyPair();
                publicKeyStr = keyPairMap.get(PUBLIC_KEY);
            }
            byte[] publicKeyByte = RSAUtil.base64DecodeToByte(publicKeyStr);
            if (!ObjectUtils.isEmpty(publicKeyByte)) {
                // 公钥转换为PublicKey对象
                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyByte);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
                // 创建密码器
                Cipher cipher = Cipher.getInstance("RSA");
                // 初始化加密模式的密码器
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                // 公钥加密
                byte[] data = content.getBytes(StandardCharsets.UTF_8);
                int inputLen = data.length;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段加密
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(data, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_ENCRYPT_BLOCK;
                }
                encryptResult = out.toByteArray();
                out.close();
            }
        } catch (Exception e) {
            log.error("publicEncrypt failure!! error={}", e.getMessage());
            e.printStackTrace();
        }
        return encryptResult;
    }

    /**
     * 私钥解密
     */
    public static byte[] privateDecrypt(byte[] content) {
        byte[] decryptResult = null;
        try {
            // 获取私钥
            String privateKeyStr = keyPairMap.get(PRIVATE_KEY);
            if (ObjectUtils.isEmpty(privateKeyStr)) {
                // 生成私钥
                generateKeyPair();
                privateKeyStr = keyPairMap.get(PRIVATE_KEY);
            }
            byte[] privateKeyByte = RSAUtil.base64DecodeToByte(privateKeyStr);
            if (!ObjectUtils.isEmpty(privateKeyByte)) {
                // 私钥转换为PrivateKey对象
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyByte);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
                // 创建密码器
                Cipher cipher = Cipher.getInstance("RSA");
                // 初始化加密模式的密码器
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                // 私钥解密
                int inputLen = content.length;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段解密
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                        cache = cipher.doFinal(content, offSet, MAX_DECRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(content, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_DECRYPT_BLOCK;
                }
                decryptResult = out.toByteArray();
                out.close();
            }
        } catch (Exception e) {
            log.error("privateDecrypt failure!! error={}", e.getMessage());
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
