package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author Andon
 * 2022/6/22
 * <p>
 * RSA非对称加密算法
 */
@Slf4j
public class RSAUtil {

    public static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048; //密钥长度
    private static final int MAX_ENCRYPT_BLOCK = 245; //RSA加密明文大小
    private static final int MAX_DECRYPT_BLOCK = 256; //RSA解密密文大小

    /**
     * 生成密钥对
     */
    public static RsaKeyPair generateKeyPair(String key) {
        RsaKeyPair rsaKeyPair = null;
        try {
            // 创建RSA密钥对的生成器实例
            KeyPairGenerator rsa = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            // 由自定义随机数或者时间戳初始化2048位的密钥对生成器
            SecureRandom secureRandom = new SecureRandom((key == null ? String.valueOf(System.currentTimeMillis()) : key).getBytes(StandardCharsets.UTF_8));
            rsa.initialize(KEY_SIZE, secureRandom);
            // 生成密钥对
            KeyPair keyPair = rsa.generateKeyPair();
            // 获取公钥
            String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            // 获取私钥
            String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            rsaKeyPair = new RsaKeyPair(publicKey, privateKey);
        } catch (Exception e) {
            log.error("generateKeyPair failure!! error:{}", e.getMessage());
            e.printStackTrace();
        }
        return rsaKeyPair;
    }

    /**
     * 公钥加密
     */
    public static String publicEncrypt(String data, String publicKeyStr) {
        String encryptResult = null;
        try {
            // 公钥转换为PublicKey对象
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            // 初始化加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 公钥加密
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            int inputLen = dataBytes.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(dataBytes, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataBytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptByte = out.toByteArray();
            out.close();
            encryptByte = Base64.getEncoder().encode(encryptByte);
            encryptResult = new String(encryptByte, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("publicEncrypt failure!! error:{}", e.getMessage());
            e.printStackTrace();
        }
        return encryptResult;
    }

    /**
     * 私钥解密
     */
    public static String privateDecrypt(String encryptContent, String privateKeyStr) {
        String decryptResult = null;
        try {
            // 私钥转换为PrivateKey对象
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            // 初始化加密模式的密码器
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 私钥解密
            byte[] encryptByte = Base64.getDecoder().decode(encryptContent);
            int inputLen = encryptByte.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptByte, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptByte, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptByte = out.toByteArray();
            out.close();
            decryptResult = new String(decryptByte, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("privateDecrypt failure!! error:{}", e.getMessage());
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

    public static class RsaKeyPair {
        public String publicKey;
        public String privateKey;

        public RsaKeyPair(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }
}
