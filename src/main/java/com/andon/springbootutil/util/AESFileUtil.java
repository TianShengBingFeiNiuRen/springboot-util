package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * @author Andon
 * 2024/10/22
 */
@Slf4j
public class AESFileUtil {

    public static final String KEY_ALGORITHM = "AES";
    public static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     */
    public static void encrypt(String key, String inputFilePath, String outputFilePath) throws Exception {
        // 初始化Cipher对象
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(Objects.requireNonNull(parseHexStrToByte(key)), KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        // 读取文件并加密
        FileInputStream fis = new FileInputStream(inputFilePath);
        FileOutputStream fos = new FileOutputStream(outputFilePath);
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                fos.write(output);
            }
        }

        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            fos.write(outputBytes);
        }

        // 关闭流
        fis.close();
        fos.close();
    }

    /**
     * 解密
     */
    public static void decrypt(String key, String inputFilePath, String outputFilePath) throws Exception {
        // 初始化Cipher对象
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(Objects.requireNonNull(parseHexStrToByte(key)), KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        // 读取文件并解密
        FileInputStream fis = new FileInputStream(inputFilePath);
        FileOutputStream fos = new FileOutputStream(outputFilePath);
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                fos.write(output);
            }
        }

        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            fos.write(outputBytes);
        }

        // 关闭流
        fis.close();
        fos.close();
    }

    public static void main(String[] args) throws Exception {
        // 生成密钥
//        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
//        keyGenerator.init(256, SecureRandom.getInstanceStrong());
//        SecretKey secretKey = keyGenerator.generateKey();
//        String key = parseByteToHexStr(secretKey.getEncoded());
        String key = "A5DF13C1FBDC45DD88154B3C189D254BEACBC78947F150601D07827145B32EF6";
        log.info("key:{}", key);

        String filePath = "C:\\Apps\\file\\springboot-util\\data_ver_host.csv";
        String encryptFilePath = "C:\\Apps\\file\\springboot-util\\data_ver_host.encrypt";
        String decryptFilePath = "C:\\Apps\\file\\springboot-util\\data_ver_host.decrypt";

        // 加密
        encrypt(key, filePath, encryptFilePath);
        log.info("encryptFilePath:{}", encryptFilePath);

        // 解密
        decrypt(key, encryptFilePath, decryptFilePath);
        log.info("decryptFilePath:{}", decryptFilePath);
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
