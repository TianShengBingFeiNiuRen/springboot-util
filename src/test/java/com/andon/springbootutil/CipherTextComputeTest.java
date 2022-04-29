package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.CipherTextComputeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Andon
 * 2022/4/20
 */
@Slf4j
public class CipherTextComputeTest {

    /**
     * 多项式计算测试
     */
    @Test
    public void polynomialTest() {
        int item = 3;
        int element = 3;
        List<List<double[]>> polynomialList = new ArrayList<>(item);
        for (int i = 0; i < item; i++) {
            List<double[]> polynomial = new ArrayList<>(3);
            double[] coefficient = new double[1];
            double[] variable = new double[element];
            double[] degree = new double[element];
            coefficient[0] = new Random().nextInt(9) + 1.0;
            coefficient[0] = coefficient[0] % 2 == 0 ? coefficient[0] : coefficient[0] * -1;
            polynomial.add(coefficient);
            for (int e = 0; e < element; e++) {
                variable[e] = new Random().nextInt(9) + 1.0;
                degree[e] = new Random().nextInt(9) + 1.0;
                degree[e] = degree[e] % 2 == 0 ? degree[e] : -1;
            }
            polynomial.add(variable);
            polynomial.add(degree);
            polynomialList.add(polynomial);
        }
        log.info("polynomialList:{}", JSONObject.toJSONString(polynomialList));
        StringBuilder stringBuilderP = new StringBuilder();
        stringBuilderP.append("计算多项式 >>> ff=(");
        double ff = 0;
        double result = 0;
        for (int i = 0; i < polynomialList.size(); i++) {
            // 多项式拆分成单项式计算
            double[] coefficient = polynomialList.get(i).get(0);
            double[] variable = polynomialList.get(i).get(1);
            double[] degree = polynomialList.get(i).get(2);
            double f = coefficient[0];
            StringBuilder stringBuilderM = new StringBuilder();
            stringBuilderM.append("计算单项式 >>> f=(").append(coefficient[0]);
            stringBuilderP.append("(").append(coefficient[0]);
            for (int j = 0; j < variable.length; j++) {
                stringBuilderM.append(" * ").append(variable[j]).append("^").append(degree[j]);
                stringBuilderP.append(" * ").append(variable[j]).append("^").append(degree[j]);
                f *= Math.pow(variable[j], degree[j]);
            }
            ff += f;
            stringBuilderM.append(")=").append(f);
            if (i == polynomialList.size() - 1) {
                stringBuilderP.append(")");
            } else {
                stringBuilderP.append(") + ");
            }
            log.info(stringBuilderM.toString());
            // 单项式计算
            double resultF = monomialCompute(coefficient[0], variable, degree);
            result += resultF;
        }
        stringBuilderP.append(")=").append(ff);
        log.info(stringBuilderP.toString());
        log.info("result:{}", result);
    }

    /**
     * 单项式计算测试
     */
    @Test
    public void monomialTest() {
        // 计算单项式(2 * x^3 * y^1 / z)
        double coefficient = 2;
        double x = 2.0;
        double y = 3.0;
        double z = 4.0;
        double degreeX = 3;
        double degreeY = 1;
        double degreeZ = -1;
        log.info("计算单项式f(coefficient * x^degreeX * y^degreeY / z)");
        log.info("f({} * {}^{} * {}^{} / {}) = {}", coefficient, x, degreeX, y, degreeY, z, coefficient * Math.pow(x, degreeX) * Math.pow(y, degreeY) * Math.pow(z, degreeZ));
        System.out.println("================================");
        double[] param = new double[]{x, y, z};
        double[] degree = new double[]{degreeX, degreeY, degreeZ};
        monomialCompute(coefficient, param, degree);
    }

    /**
     * 单项式计算
     *
     * @param coefficient 系数
     * @param param       变量值
     * @param degree      指数
     */
    private double monomialCompute(double coefficient, double[] param, double[] degree) {
        log.info("系数 >>> coefficient:{}", coefficient);
        log.info("变量值 >>> param:{}", JSONObject.toJSONString(param));
        log.info("指数 >>> degree:{}", JSONObject.toJSONString(degree));
        System.out.println("================================");

        // 数据方：param拆分成param1、param2
        double[][] paramSplit = CipherTextComputeUtil.paramSplit(param);
        double[] param1 = paramSplit[0];
        double[] param2 = paramSplit[1];
        log.info("数据方：param拆分成param1、param2 >>> paramSplit:{}", JSONObject.toJSONString(paramSplit));
        System.out.println("================================");

        // 计算节点1：生成随机数r1，r1结合param1进行计算得出结果c1
        double r1 = CipherTextComputeUtil.generateRandom();
        double c1 = CipherTextComputeUtil.c1(coefficient, degree, param1, r1);
        log.info("计算节点1：生成随机数r1，r1结合param1进行计算得出结果c1 >>> r1:{} c1:{}", r1, c1);
        System.out.println("================================");

        // 计算节点2：生成随机数r2，r2结合c1 param2进行计算得出结果c2
        double r2 = CipherTextComputeUtil.generateRandom();
        double c2 = CipherTextComputeUtil.c2(c1, coefficient, degree, param2, r2);
        log.info("计算节点2：生成随机数r2，r2结合c1 param2进行计算得出结果c2 >>> r2:{} c2:{}", r2, c2);
        System.out.println("================================");

        // 计算节点3：r1结合c2 param2进行计算得出结果c3
        double c3 = CipherTextComputeUtil.c3(c2, coefficient, degree, param2, r1);
        log.info("计算节点3：r1结合c2 param2进行计算得出结果c3 >>> c3:{}", c3);
        System.out.println("================================");

        // 查询方：c3结合r2 进行计算得出单项式结果
        double result = CipherTextComputeUtil.result(coefficient, c3, r2);
        log.info("查询方：c3结合r2 进行计算得出单项式结果 >>> result:{}", result);
        return result;
    }
}
