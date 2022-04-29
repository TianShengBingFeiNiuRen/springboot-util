package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.PolynomialUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andon
 * 2022/4/25
 */
@Slf4j
public class PolynomialTest {

    @Test
    public void Test() {
        PolynomialUtil.Monomial monomial = PolynomialUtil.generateMonomial(2);
        PolynomialUtil.Monomial monomial1 = new PolynomialUtil.Monomial(2, monomial.getVariables());
        PolynomialUtil.Monomial monomial2 = new PolynomialUtil.Monomial(3, monomial.getVariables());
        PolynomialUtil.Monomial monomial3 = new PolynomialUtil.Monomial(5);
        PolynomialUtil.Monomial monomial4 = new PolynomialUtil.Monomial(7);
        PolynomialUtil.Monomial monomial5 = PolynomialUtil.generateMonomial(1);
        log.info(PolynomialUtil.toString(monomial));
        log.info(PolynomialUtil.toString(monomial1));
        log.info(PolynomialUtil.toString(monomial2));
        log.info(PolynomialUtil.toString(monomial3));
        log.info(PolynomialUtil.toString(monomial4));
        log.info(PolynomialUtil.toString(monomial5));

        System.out.println("===================add");
        List<PolynomialUtil.Monomial> add1 = PolynomialUtil.add(monomial, monomial1, monomial2, monomial3, monomial4, monomial5);
        log.info("add1:{}", PolynomialUtil.toString(add1));
        List<PolynomialUtil.Monomial> add2 = PolynomialUtil.add(monomial, PolynomialUtil.add(monomial1, monomial3, monomial5), PolynomialUtil.add(monomial2, monomial4));
        log.info("add2:{}", PolynomialUtil.toString(add2));
        List<PolynomialUtil.Monomial> add3 = PolynomialUtil.add(PolynomialUtil.add(monomial, new PolynomialUtil.Monomial(0)), PolynomialUtil.add(monomial1, monomial3, monomial5), PolynomialUtil.add(monomial2, monomial4));
        log.info("add3:{}", PolynomialUtil.toString(add3));
        List<PolynomialUtil.Monomial> add4 = PolynomialUtil.add(PolynomialUtil.add(monomial1, monomial3, monomial5), monomial, monomial2, monomial4);
        log.info("add4:{}", PolynomialUtil.toString(add4));

        System.out.println("===================subtract");
        List<PolynomialUtil.Monomial> subtract1 = PolynomialUtil.subtract(monomial, monomial1, monomial2, monomial3, monomial4, monomial5);
        log.info("subtract1:{}", PolynomialUtil.toString(subtract1));
        List<PolynomialUtil.Monomial> subtract2 = PolynomialUtil.subtract(monomial,  PolynomialUtil.add(monomial1, monomial3, monomial5), PolynomialUtil.add(monomial2, monomial4));
        log.info("subtract2:{}", PolynomialUtil.toString(subtract2));
        List<PolynomialUtil.Monomial> subtract3 = PolynomialUtil.subtract(PolynomialUtil.add(monomial, new PolynomialUtil.Monomial(0)), monomial1, monomial2, monomial3, monomial4, monomial5);
        log.info("subtract3:{}", PolynomialUtil.toString(subtract3));
        List<PolynomialUtil.Monomial> subtract4 = PolynomialUtil.subtract(PolynomialUtil.add(monomial, new PolynomialUtil.Monomial(0)), PolynomialUtil.add(monomial1, monomial3, monomial5), PolynomialUtil.add(monomial2, monomial4));
        log.info("subtract4:{}", PolynomialUtil.toString(subtract4));

        System.out.println("===================multiply");
        PolynomialUtil.Monomial multiply1 = PolynomialUtil.multiply(monomial, monomial1, monomial2, monomial3, monomial4, monomial5);
        log.info("multiply1:{}", PolynomialUtil.toString(multiply1));
        List<PolynomialUtil.Monomial> multiply2 = PolynomialUtil.multiply(monomial, PolynomialUtil.add(monomial1, monomial2, monomial3, monomial4, monomial5));
        log.info("multiply2:{}", PolynomialUtil.toString(multiply2));
        List<PolynomialUtil.Monomial> multiply3 = PolynomialUtil.multiply(PolynomialUtil.add(monomial1, monomial2, monomial3, monomial4, monomial5), monomial);
        log.info("multiply3:{}", PolynomialUtil.toString(multiply3));
        List<PolynomialUtil.Monomial> multiply4 = PolynomialUtil.multiply(PolynomialUtil.add(monomial, new PolynomialUtil.Monomial(0)), PolynomialUtil.add(monomial1, monomial2, monomial3, monomial4, monomial5));
        log.info("multiply4:{}", PolynomialUtil.toString(multiply4));

        System.out.println("===================divide");
        PolynomialUtil.Monomial divide1 = PolynomialUtil.divide(monomial, monomial1, monomial2, monomial3, monomial4, monomial5);
        log.info("divide1:{}", PolynomialUtil.toString(divide1));
        List<PolynomialUtil.Monomial> divide2 = PolynomialUtil.divide(monomial, PolynomialUtil.add(monomial1, monomial2, monomial3, monomial4, monomial5));
        log.info("divide2:{}", PolynomialUtil.toString(divide2));
        List<PolynomialUtil.Monomial> divide3 = PolynomialUtil.divide(PolynomialUtil.add(monomial1, monomial2, monomial3, monomial4, monomial5), monomial);
        log.info("divide3:{}", PolynomialUtil.toString(divide3));
        List<PolynomialUtil.Monomial> divide4 = PolynomialUtil.divide(PolynomialUtil.add(monomial, new PolynomialUtil.Monomial(0)), PolynomialUtil.add(monomial1, monomial2, monomial3, monomial4, monomial5));
        log.info("divide4:{}", PolynomialUtil.toString(divide4));

        System.out.println("===================power");
        PolynomialUtil.Monomial power1 = PolynomialUtil.power(monomial, 2);
        log.info("power1:{}", PolynomialUtil.toString(power1));
        PolynomialUtil.Monomial power2 = PolynomialUtil.power(monomial3, 2);
        log.info("power2:{}", PolynomialUtil.toString(power2));
        List<PolynomialUtil.Monomial> power3 = PolynomialUtil.power(PolynomialUtil.add(monomial1, monomial3), 2);
        log.info("power3:{}", PolynomialUtil.toString(power3));
        List<PolynomialUtil.Monomial> power4 = PolynomialUtil.power(PolynomialUtil.add(monomial1, monomial3), -2);
        log.info("power4:{}", PolynomialUtil.toString(power4));
        List<PolynomialUtil.Monomial> power5 = PolynomialUtil.power(PolynomialUtil.add(monomial2, monomial3), 2);
        log.info("power5:{}", PolynomialUtil.toString(power5));
        List<PolynomialUtil.Monomial> power6 = PolynomialUtil.power(PolynomialUtil.add(monomial3, monomial4), -2);
        log.info("power6:{}", PolynomialUtil.toString(power6));
    }

    @Test
    public void expandTest() {
        List<PolynomialUtil.Monomial> monomials = PolynomialUtil.generatePolynomial(5, 1);
        PolynomialUtil.Monomial monomial = PolynomialUtil.generateMonomial(2);
        monomials.add(new PolynomialUtil.Monomial(2, monomial.getVariables()));
        monomials.add(new PolynomialUtil.Monomial(3, monomial.getVariables()));
        monomials.add(new PolynomialUtil.Monomial(5));
        monomials.add(new PolynomialUtil.Monomial(7));
        log.info("monomials:{}", PolynomialUtil.toString(monomials));
        List<PolynomialUtil.Monomial> expandMonomial = PolynomialUtil.expand(monomials);
        log.info("monomials expand:{}", PolynomialUtil.toString(expandMonomial));
    }

    @Test
    public void mergeTest() {
        PolynomialUtil.Monomial monomial = PolynomialUtil.generateMonomial(2);
        List<PolynomialUtil.Variable> variables = monomial.getVariables();
        variables.add(new PolynomialUtil.Variable("x", 2.0));
        variables.add(new PolynomialUtil.Variable("x", 3.0));
        log.info("monomial:{}", PolynomialUtil.toString(monomial));
        PolynomialUtil.Monomial monomialMerge = PolynomialUtil.merge(monomial);
        log.info("monomial merge:{}", PolynomialUtil.toString(monomialMerge));
        System.out.println("=====================");

        PolynomialUtil.Monomial monomial2 = new PolynomialUtil.Monomial(5);
        log.info("monomial2:{}", PolynomialUtil.toString(monomial2));
        PolynomialUtil.Monomial monomialMerge2 = PolynomialUtil.merge(monomial2);
        log.info("monomial2 merge:{}", PolynomialUtil.toString(monomialMerge2));
    }

    @Test
    public void toStringTest() {
        List<PolynomialUtil.Monomial> monomials = PolynomialUtil.generatePolynomial(2, 2);
        PolynomialUtil.Monomial monomial1 = PolynomialUtil.generateMonomial(2);
        List<PolynomialUtil.Variable> variables = new ArrayList<>();
        variables.add(new PolynomialUtil.Variable("x", -1.0));
        variables.add(new PolynomialUtil.Variable("y", -2.0));
        PolynomialUtil.Monomial monomial2 = new PolynomialUtil.Monomial(-3, variables);
        PolynomialUtil.Monomial monomial3 = new PolynomialUtil.Monomial(-5);
        monomials.add(monomial1);
        monomials.add(monomial2);
        monomials.add(monomial3);
        log.info("monomials:{}", JSONObject.toJSONString(monomials));
        log.info("PolynomialUtil.toString(monomial1):{}", PolynomialUtil.toString(monomial1));
        log.info("PolynomialUtil.toString(monomial2):{}", PolynomialUtil.toString(monomial2));
        log.info("PolynomialUtil.toString(monomials):{}", PolynomialUtil.toString(monomials));

    }

    @Test
    public void generatePolynomialTest() {
        List<PolynomialUtil.Monomial> monomials1 = PolynomialUtil.generatePolynomial(2, 2);
        List<PolynomialUtil.Monomial> monomials2 = PolynomialUtil.generatePolynomial(2, 1);
        log.info("PolynomialUtil.toString(monomials1):{}", PolynomialUtil.toString(monomials1));
        log.info("PolynomialUtil.toString(monomials2):{}", PolynomialUtil.toString(monomials2));
    }
}
