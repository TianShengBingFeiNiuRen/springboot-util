package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author Andon
 * 2023/11/2
 */
@Slf4j
public class DigestUtil {

    // 摘要算法
    public static final String SM3 = "SM3";
    public static final String MD5 = "MD5";

    /**
     * 文件Hash
     */
    public static String getHash(String algorithm, File file) {
        FileInputStream fileInputStream = null;
        String hash = null;
        try {
            BouncyCastleProvider provider = new BouncyCastleProvider();
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm, provider);
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, length);
            }
            hash = byte2Hex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("[{}] >>> {}:{}", file.getAbsolutePath(), algorithm, hash);
        return hash;
    }

    /**
     * 文本Hash
     */
    public static String getHash(String algorithm, String text) {
        String hash = null;
        try {
            BouncyCastleProvider provider = new BouncyCastleProvider();
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm, provider);
            messageDigest.update(text.getBytes(StandardCharsets.UTF_8));
            hash = byte2Hex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("[{}] >>> {}:{}", text, algorithm, hash);
        return hash;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        String algorithm = MD5;

        File file = new File("C:\\DingDing Files\\123.zip");
        String fileHash = getHash(algorithm, file);

        String text = "hello world";
        String hash = getHash(algorithm, text);
    }
}
