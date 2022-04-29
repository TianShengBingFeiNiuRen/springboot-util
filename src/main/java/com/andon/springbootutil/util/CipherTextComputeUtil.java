package com.andon.springbootutil.util;

import java.text.DecimalFormat;

/**
 * @author Andon
 * 2022/4/21
 * <p>
 * 密文计算
 */
@SuppressWarnings("DuplicatedCode")
public class CipherTextComputeUtil {

    /**
     * 生成随机数r
     *
     * @return r
     */
    public static double generateRandom() {
        return Double.parseDouble(new DecimalFormat("#").format(Math.random() * 10000000000.0));
    }

    /**
     * 数据方：param拆分成param1、param2
     *
     * @param param 数据方原值
     * @return 拆分后值1、值2
     */
    public static double[][] paramSplit(double[] param) {
        double[][] paramSplit = new double[2][param.length];
        double[] param1 = new double[param.length];
        double[] param2 = new double[param.length];
        for (int i = 0; i < param.length; i++) {
            double random = generateRandom();
            param2[i] = random;
            param1[i] = param[i] * random;
        }
        paramSplit[0] = param1;
        paramSplit[1] = param2;
        return paramSplit;
    }

    /**
     * 计算节点1：生成随机数r1，r1结合param1进行计算得出结果c1
     *
     * @param coefficient 单项式系数
     * @param degree      单项式指数
     * @param param1      原值拆分后的值1
     * @param r1          随机数r1
     * @return 计算结果c1
     */
    public static double c1(double coefficient, double[] degree, double[] param1, double r1) {
        double c1 = 1;
        for (int i = 0; i < param1.length; i++) {
            c1 *= Math.pow(param1[i], degree[i]);
        }
        c1 = coefficient * c1 + r1;
        return c1;
    }

    /**
     * 计算节点2：生成随机数r2，r2结合c1 param2进行计算得出结果c2
     *
     * @param c1          计算节点1的计算结果
     * @param coefficient 单项式系数
     * @param degree      单项式指数
     * @param param2      原值拆分后的值2
     * @param r2          随机数r2
     * @return 计算结果c2
     */
    public static double c2(double c1, double coefficient, double[] degree, double[] param2, double r2) {
        double c2 = 1;
        for (int i = 0; i < param2.length; i++) {
            c2 *= Math.pow(param2[i], degree[i]);
        }
        c2 = c1 / (coefficient * c2) + r2;
        return c2;
    }

    /**
     * 计算节点3：r1结合c2 param2进行计算得出结果c3
     *
     * @param c2          计算节点2的计算结果
     * @param coefficient 单项式系数
     * @param degree      单项式指数
     * @param param2      原值拆分后的值2
     * @param r1          计算节点1的随机数r1
     * @return 计算结果c2
     */
    public static double c3(double c2, double coefficient, double[] degree, double[] param2, double r1) {
        double c3 = 1;
        for (int i = 0; i < param2.length; i++) {
            c3 *= Math.pow(param2[i], degree[i]);
        }
        c3 = c2 - r1 / (coefficient * c3);
        return c3;
    }

    /**
     * 查询方：c3结合r2 进行计算得出单项式结果
     *
     * @param coefficient 单项式系数
     * @param c3          计算节点3的计算结果
     * @param r2          计算节点2的随机数r2
     * @return 最终结果result
     */
    public static double result(double coefficient, double c3, double r2) {
        return coefficient * (c3 - r2);
    }
}
