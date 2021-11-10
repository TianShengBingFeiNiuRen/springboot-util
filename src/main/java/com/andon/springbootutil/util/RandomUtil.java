package com.andon.springbootutil.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * 随机字符串Util
 */
public class RandomUtil {

    public static String generateID() {
        return System.currentTimeMillis() + numGenerate(6);
    }

    public static String numGenerate(int length) {
        if (length == 0) {
            return "";
        }
        return String.valueOf((int) ((Math.random() * 9 + 1) * (Math.pow(10, length - 1))));
    }

    public static String stringGenerate(int length) {
        StringBuilder sb = new StringBuilder();
        String str1 = "0123456789";
        String s1 = stringGenerate(str1, length / 3);
        String str2 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String s2 = stringGenerate(str2, length - (length / 3));
        sb.append(s1);
        sb.append(s2);
        List<String> stringList = Arrays.asList(sb.toString().split(""));
        Collections.shuffle(stringList);
        sb = new StringBuilder();
        for (String s : stringList) {
            sb.append(s);
        }
        return sb.toString();
    }

    private static String stringGenerate(String str, int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
