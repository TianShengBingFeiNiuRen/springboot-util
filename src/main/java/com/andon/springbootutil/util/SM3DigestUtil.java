package com.andon.springbootutil.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Andon
 * 2023/11/2
 */
public class SM3DigestUtil {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        File file = new File("C:\\Apps\\file\\springboot-util\\常驻人口.csv");
        String fileHash = getHash(file);
        System.out.println("fileHash:" + fileHash);
    }

    public static String getHash(File file) {
        FileInputStream fileInputStream = null;
        String hash = null;
        try {
            BouncyCastleProvider provider = new BouncyCastleProvider();
            MessageDigest messageDigest = MessageDigest.getInstance("SM3", provider);
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
}
