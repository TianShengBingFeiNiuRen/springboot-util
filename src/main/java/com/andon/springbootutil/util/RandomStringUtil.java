package com.andon.springbootutil.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * 生成字符串 随机数字字母符号Util
 */
public class RandomStringUtil {

    public static String generateID() {
        return System.currentTimeMillis() + numGenerate(6);
    }

    public static String numGenerate(int length) {
        length = Math.max(length, 4);
        return String.valueOf((int) ((Math.random() * 9 + 1) * (Math.pow(10, length - 1))));
    }

    public static String stringGenerate(int length, boolean number, boolean uppercaseLetter, boolean lowercaseLetter, boolean symbol) {
        length = Math.max(length, 4);
        int divisor = ((number ? 1 : 0) + (uppercaseLetter ? 1 : 0) + (lowercaseLetter ? 1 : 0) + (symbol ? 1 : 0));
        divisor = divisor == 0 ? 1 : divisor;
        int average = length / divisor;
        int remainder = length % divisor;
        StringBuilder sb = new StringBuilder();
        if (number) {
            sb.append(stringGenerate("0123456789", average));
        }
        if (uppercaseLetter) {
            sb.append(stringGenerate("ABCDEFGHIJKLMNOPQRSTUVWXYZ", average));
        }
        if (lowercaseLetter) {
            sb.append(stringGenerate("abcdefghijklmnopqrstuvwxyz", average));
        }
        if (symbol) {
            sb.append(stringGenerate("./*~!@#$%^&?-_=+[]{}", average));
        }
        if (remainder > 0) {
            if (number) {
                sb.append(stringGenerate("0123456789", remainder));
            } else if (uppercaseLetter) {
                sb.append(stringGenerate("ABCDEFGHIJKLMNOPQRSTUVWXYZ", remainder));
            } else {
                sb.append(stringGenerate("abcdefghijklmnopqrstuvwxyz", remainder));
            }
        }
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
