package com.andon.springbootutil.util;

import java.util.regex.Pattern;

/**
 * @author Andon
 * 2023/6/6
 */
public class CheckStringUtil {

    /**
     * 数字判断
     */
    public static boolean containNumber(String str) {
        Pattern pattern = Pattern.compile(".*\\d.*");
        return pattern.matcher(str).matches();
    }

    /**
     * 大写判断
     */
    public static boolean containUpper(String str) {
        Pattern pattern = Pattern.compile(".*[A-Z].*");
        return pattern.matcher(str).matches();
    }

    /**
     * 小写判断
     */
    public static boolean containSmall(String str) {
        Pattern pattern = Pattern.compile(".*[a-z].*");
        return pattern.matcher(str).matches();
    }

    /**
     * 特殊字符判断
     */
    public static boolean containSpcStr(String str) {
        Pattern pattern = Pattern.compile(".*[./*~!@#$%^&?\\-_=+\\[\\]{}].*");
        return pattern.matcher(str).matches();
    }
}
