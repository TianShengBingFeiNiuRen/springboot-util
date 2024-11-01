package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * @author Andon
 * 2024/10/25
 */
@Slf4j
public class AESCTRUtil {

    public static final String KEY_ALGORITHM = "AES";
    public static final String ALGORITHM = "AES/CTR/NoPadding";

    /**
     * 加密
     */
    public static String encrypt(String key, String iv, String content) throws Exception {
        // 初始化Cipher对象
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(Objects.requireNonNull(parseHexStrToByte(key)), KEY_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Objects.requireNonNull(parseHexStrToByte(iv)));
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
        // 加密
        byte[] encryptBytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return parseByteToHexStr(encryptBytes);
    }

    /**
     * 解密
     */
    public static String decrypt(String key, String iv, String content) throws Exception {
        // 初始化Cipher对象
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(Objects.requireNonNull(parseHexStrToByte(key)), KEY_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Objects.requireNonNull(parseHexStrToByte(iv)));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        // 解密
        byte[] encryptBytes = cipher.doFinal(Objects.requireNonNull(parseHexStrToByte(content)));
        return new String(encryptBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        // 生成密钥
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(256, SecureRandom.getInstanceStrong());
        SecretKey secretKey = keyGenerator.generateKey();
        String key = parseByteToHexStr(secretKey.getEncoded());
        log.info("key:{}", key);

        // 初始化向量（IV），CTR模式需要一个IV，但不需要保密
        byte[] ivBytes = new byte[16]; // AES的块大小是128位，所以IV长度是16字节
        new SecureRandom().nextBytes(ivBytes);
        String iv = parseByteToHexStr(ivBytes);
        log.info("iv:{}", iv);

        String content = "hello world";

        // 加密
        String encrypt = encrypt(key, iv, content);
        log.info("encrypt:{}", encrypt);

        // 解密
        String decrypt = decrypt(key, iv, encrypt);
        log.info("decrypt:{}", decrypt);
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
}
