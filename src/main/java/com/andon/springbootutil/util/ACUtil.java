package com.andon.springbootutil.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andon
 * 2022/1/10
 * <p>
 * 排列组合工具类
 */
public class ACUtil {

    /**
     * 计算阶乘数，即n! = n * (n-1) * ... * 2 * 1
     */
    private static long factorial(int n) {
        return (n > 1) ? n * factorial(n - 1) : 1;
    }

    /**
     * 计算排列数，即A(n, m) = n!/(n-m)!
     */
    public static long arrangement(int n, int m) {
        return (n >= m) ? factorial(n) / factorial(n - m) : 0;
    }

    /**
     * 计算组合数，即C(n, m) = n!/((n-m)! * m!)
     */
    public static long combination(int n, int m) {
        return (n >= m) ? factorial(n) / factorial(n - m) / factorial(m) : 0;
    }

    /**
     * 排列选择（从列表中选择n个排列）
     *
     * @param dataArr 待选列表
     * @param n       选择个数
     */
    public static List<String> arrangementSelect(String[] dataArr, int n) {
        List<String> result = new ArrayList<>();
        arrangementSelect(dataArr, new String[n], 0, result);
        return result;
    }

    /**
     * 排列选择
     *
     * @param dataArr     待选列表
     * @param resultArr   前面（resultIndex-1）个的排列结果
     * @param resultIndex 选择索引，从0开始
     */
    private static void arrangementSelect(String[] dataArr, String[] resultArr, int resultIndex, List<String> result) {
        int resultLen = resultArr.length;
        if (resultIndex >= resultLen) { // 全部选择完时，输出排列结果
            result.add(String.join(",", resultArr));
            return;
        }
        // 递归选择下一个
        for (String s : dataArr) {
            // 判断待选项是否存在于排列结果中
            boolean exists = false;
            for (int j = 0; j < resultIndex; j++) {
                if (s.equals(resultArr[j])) {
                    exists = true;
                    break;
                }
            }
            if (!exists) { // 排列结果不存在该项，才可选择
                resultArr[resultIndex] = s;
                arrangementSelect(dataArr, resultArr, resultIndex + 1, result);
            }
        }
    }

    /**
     * 组合选择（从列表中选择n个组合）
     *
     * @param dataArr 待选列表
     * @param n       选择个数
     */
    public static List<String> combinationSelect(String[] dataArr, int n) {
        List<String> result = new ArrayList<>();
        combinationSelect(dataArr, 0, new String[n], 0, result);
        return result;
    }

    /**
     * 组合选择
     *
     * @param dataArr     待选列表
     * @param dataIndex   待选开始索引
     * @param resultArr   前面（resultIndex-1）个的组合结果
     * @param resultIndex 选择索引，从0开始
     */
    private static void combinationSelect(String[] dataArr, int dataIndex, String[] resultArr, int resultIndex, List<String> result) {
        int resultLen = resultArr.length;
        int resultCount = resultIndex + 1;
        if (resultCount > resultLen) { // 全部选择完时，输出组合结果
            result.add(String.join(",", resultArr));
            return;
        }
        // 递归选择下一个
        for (int i = dataIndex; i < dataArr.length + resultCount - resultLen; i++) {
            resultArr[resultIndex] = dataArr[i];
            combinationSelect(dataArr, i + 1, resultArr, resultIndex + 1, result);
        }
    }
}
