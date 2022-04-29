package com.andon.springbootutil.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Andon
 * 2022/4/25
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
public class PolynomialUtil {

    /**
     * 生成单项式
     *
     * @param variableNum 变量个数
     * @return monomial 单项式
     */
    public static PolynomialUtil.Monomial generateMonomial(int variableNum) {
        List<Variable> variables = new ArrayList<>(variableNum);
        for (int i = 0; i < variableNum; i++) {
            variables.add(new Variable("x" + (i + 1), new Random().nextInt(9) + 1.0));
        }
        return new Monomial(new Random().nextInt(9) + 1, variables);
    }

    /**
     * 生成多项式
     *
     * @param item        项数
     * @param variableNum 变量个数
     * @return monomials 单项式集合
     */
    public static List<Monomial> generatePolynomial(int item, int variableNum) {
        List<Monomial> monomials = new ArrayList<>(item);
        for (int i = 0; i < item; i++) {
            monomials.add(generateMonomial(variableNum));
        }
        return monomials;
    }

    /**
     * 单项式加法
     *
     * @param monomials 任意个单项式
     * @return 相加后的多项式
     */
    public static List<Monomial> add(Monomial... monomials) {
        return expand(Arrays.stream(monomials).collect(Collectors.toList()));
    }

    /**
     * 单项式加法
     *
     * @param monomial  单项式
     * @param monomials 任意个多项式
     * @return 相加后的多项式
     */
    @SafeVarargs
    public static List<Monomial> add(Monomial monomial, List<Monomial>... monomials) {
        List<Monomial> monomialList = new ArrayList<>(monomials.length + 1);
        monomialList.add(monomial);
        monomialList.addAll(Arrays.stream(monomials).flatMap(Collection::stream).collect(Collectors.toList()));
        return expand(monomialList);
    }

    /**
     * 多项式加法
     *
     * @param monomials 任意个多项式
     * @return 相加后的多项式
     */
    @SafeVarargs
    public static List<Monomial> add(List<Monomial>... monomials) {
        List<Monomial> monomialList = Arrays.stream(monomials).flatMap(Collection::stream).collect(Collectors.toList());
        return expand(monomialList);
    }

    /**
     * 多项式加法
     *
     * @param monomials1 多项式
     * @param monomials2 任意个单项式
     * @return 相加后的多项式
     */
    public static List<Monomial> add(List<Monomial> monomials1, Monomial... monomials2) {
        List<Monomial> monomialList = new ArrayList<>(monomials1.size() + monomials2.length);
        monomialList.addAll(monomials1);
        monomialList.addAll(Arrays.stream(monomials2).collect(Collectors.toList()));
        return expand(monomialList);
    }

    /**
     * 单项式减法
     *
     * @param monomial  被减数（单项式）
     * @param monomials 任意个减数（任意个单项式）
     * @return 相加后的多项式
     */
    public static List<Monomial> subtract(Monomial monomial, Monomial... monomials) {
        List<Monomial> monomialList = new ArrayList<>(monomials.length + 1);
        monomialList.add(monomial);
        monomialList.addAll(Arrays.stream(monomials).map(PolynomialUtil::negateMonomial).collect(Collectors.toList()));
        return expand(monomialList);
    }

    /**
     * 单项式减法
     *
     * @param monomial  被减数（单项式）
     * @param monomials 任意个减数（任意个多项式）
     * @return 相减后的多项式
     */
    @SafeVarargs
    public static List<Monomial> subtract(Monomial monomial, List<Monomial>... monomials) {
        List<Monomial> monomialList = new ArrayList<>(monomials.length + 1);
        monomialList.add(monomial);
        monomialList.addAll(Arrays.stream(monomials).flatMap(Collection::stream).map(PolynomialUtil::negateMonomial).collect(Collectors.toList()));
        return expand(monomialList);
    }

    /**
     * 多项式减法
     *
     * @param monomial1 被减数（多项式）
     * @param monomials 任意个减数（任意个单项式）
     * @return 相减后的多项式
     */
    public static List<Monomial> subtract(List<Monomial> monomial1, Monomial... monomials) {
        List<Monomial> monomialList = new ArrayList<>(monomial1.size() + monomials.length);
        monomialList.addAll(monomial1);
        monomialList.addAll(Arrays.stream(monomials).map(PolynomialUtil::negateMonomial).collect(Collectors.toList()));
        return expand(monomialList);
    }

    /**
     * 多项式减法
     *
     * @param monomial1 被减数（多项式）
     * @param monomials 任意个减数（任意个多项式）
     * @return 相减后的多项式
     */
    @SafeVarargs
    public static List<Monomial> subtract(List<Monomial> monomial1, List<Monomial>... monomials) {
        List<Monomial> monomialList = new ArrayList<>(monomial1.size() + monomials.length);
        monomialList.addAll(monomial1);
        monomialList.addAll(Arrays.stream(monomials).flatMap(Collection::stream).map(PolynomialUtil::negateMonomial).collect(Collectors.toList()));
        return expand(monomialList);
    }

    /**
     * 单项式取反
     *
     * @param monomial 单项式
     * @return 取反后的单项式
     */
    public static Monomial negateMonomial(Monomial monomial) {
        if (monomial.getConstant() != 0 || ObjectUtils.isEmpty(monomial.getVariables()) || monomial.getVariables().stream().anyMatch(variable -> ObjectUtils.isEmpty(variable.getName()))) {
            return new Monomial(monomial.getConstant() * -1.0);
        } else {
            return new Monomial(monomial.getCoefficient() * -1.0, monomial.getVariables());
        }
    }

    /**
     * 单项式乘法
     *
     * @param monomials 任意个单项式
     * @return 相乘后的单项式
     */
    public static Monomial multiply(Monomial... monomials) {
        List<Monomial> monomialList = Arrays.stream(monomials).collect(Collectors.toList());
        double constant = 1;
        double coefficient = 1;
        List<Variable> variables = new ArrayList<>();
        for (Monomial monomial : monomialList) {
            if (monomial.getConstant() != 0 || ObjectUtils.isEmpty(monomial.getVariables()) || monomial.getVariables().stream().anyMatch(variable -> ObjectUtils.isEmpty(variable.getName()))) {
                constant *= monomial.getConstant();
            } else {
                coefficient *= monomial.getCoefficient();
                variables.addAll(monomial.getVariables());
            }
        }
        if (!ObjectUtils.isEmpty(variables)) {
            Monomial monomial = new Monomial(constant * coefficient, variables);
            return merge(monomial);
        } else {
            return new Monomial(constant);
        }
    }

    /**
     * 单项式乘法
     *
     * @param monomial  单项式
     * @param monomials 多项式
     * @return 相乘后的多项式
     */
    public static List<Monomial> multiply(Monomial monomial, List<Monomial> monomials) {
        return expand(monomials.stream().map(m -> multiply(monomial, m)).collect(Collectors.toList()));
    }

    /**
     * 多项式乘法
     *
     * @param monomials 多项式
     * @param monomial  单项式
     * @return 相乘后的多项式
     */
    public static List<Monomial> multiply(List<Monomial> monomials, Monomial monomial) {
        return expand(monomials.stream().map(m -> multiply(m, monomial)).collect(Collectors.toList()));
    }

    /**
     * 多项式乘法
     *
     * @param monomials1 多项式
     * @param monomials2 多项式
     * @return 相乘后的多项式
     */
    public static List<Monomial> multiply(List<Monomial> monomials1, List<Monomial> monomials2) {
        List<Monomial> monomialList = new ArrayList<>(monomials1.size() * monomials2.size());
        for (Monomial monomial : monomials1) {
            monomialList.addAll(monomials2.stream().map(m -> multiply(monomial, m)).collect(Collectors.toList()));
        }
        return expand(monomialList);
    }

    /**
     * 单项式除法
     *
     * @param monomial  被除数（单项式）
     * @param monomials 任意个除数（任意个单项式）
     * @return 相除后的单项式
     */
    public static Monomial divide(Monomial monomial, Monomial... monomials) {
        Monomial result = new Monomial(monomial.getCoefficient(), monomial.getVariables(), monomial.getConstant());
        for (Monomial m : monomials) {
            result = multiply(result, reciprocalMonomial(m));
        }
        return result;
    }

    /**
     * 单项式除法
     *
     * @param monomial  被除数（单项式）
     * @param monomials 除数（多项式）
     * @return 相除后的多项式
     */
    public static List<Monomial> divide(Monomial monomial, List<Monomial> monomials) {
        return expand(monomials.stream().map(m -> multiply(monomial, reciprocalMonomial(m))).collect(Collectors.toList()));
    }

    /**
     * 多项式除法
     *
     * @param monomials 被除数（多项式）
     * @param monomial  除数（单项式）
     * @return 相除后的多项式
     */
    public static List<Monomial> divide(List<Monomial> monomials, Monomial monomial) {
        Monomial reciprocal = reciprocalMonomial(monomial);
        return expand(monomials.stream().map(m -> multiply(m, reciprocal)).collect(Collectors.toList()));
    }

    /**
     * 多项式除法
     *
     * @param monomials1 被除数（多项式）
     * @param monomials2 除数（多项式）
     * @return 相除后的多项式
     */
    public static List<Monomial> divide(List<Monomial> monomials1, List<Monomial> monomials2) {
        List<Monomial> monomialList = new ArrayList<>(monomials1.size() * monomials2.size());
        List<Monomial> reciprocals = monomials2.stream().map(PolynomialUtil::reciprocalMonomial).collect(Collectors.toList());
        for (Monomial monomial : monomials1) {
            monomialList.addAll(reciprocals.stream().map(m -> multiply(monomial, m)).collect(Collectors.toList()));
        }
        return expand(monomialList);
    }

    /**
     * 单项式取倒数
     *
     * @param monomial 单项式
     * @return 取倒数后的单项式
     */
    public static Monomial reciprocalMonomial(Monomial monomial) {
        if (monomial.getConstant() != 0 || ObjectUtils.isEmpty(monomial.getVariables()) || monomial.getVariables().stream().anyMatch(variable -> ObjectUtils.isEmpty(variable.getName()))) {
            return new Monomial(1.0 / monomial.getConstant());
        } else {
            List<Variable> variables = monomial.getVariables().stream()
                    .map(variable -> new Variable(variable.getName(), variable.getExponential() * -1.0))
                    .collect(Collectors.toList());
            return new Monomial(1.0 / monomial.getCoefficient(), variables);
        }
    }

    /**
     * 单项式幂运算
     *
     * @param monomial    单项式
     * @param exponential 指数
     * @return 幂运算之后的单项式
     */
    public static Monomial power(Monomial monomial, double exponential) {
        Monomial merge = merge(monomial);
        if (merge.getConstant() == 0 && merge.getCoefficient() == 0) {
            return new Monomial(0);
        }
        if (merge.getConstant() != 0 || ObjectUtils.isEmpty(merge.getVariables()) || merge.getVariables().stream().anyMatch(variable -> ObjectUtils.isEmpty(variable.getName()))) {
            return new Monomial(Math.pow(merge.getConstant(), exponential));
        }
        List<Variable> variables = merge.getVariables().stream().map(variable -> new Variable(variable.getName(), variable.getExponential() * exponential)).collect(Collectors.toList());
        return new Monomial(Math.pow(merge.getCoefficient(), exponential), variables);
    }

    /**
     * 多项式幂运算
     *
     * @param monomials   多项式
     * @param exponential 指数
     * @return 幂运算之后的多项式
     */
    public static List<Monomial> power(List<Monomial> monomials, int exponential) {
        List<Monomial> expand = expand(monomials);
        List<Monomial> result = new ArrayList<>();
        result.add(new Monomial(1));
        int abs = Math.abs(exponential);
        for (int i = 0; i < abs; i++) {
            result = multiply(result, expand);
        }
        if (exponential < 0) {
            result = divide(new Monomial(1), result);
        }
        return result;
    }

    /**
     * 单项式合并
     *
     * @param monomial 单项式
     * @return monomial 合并后的单项式
     */
    public static Monomial merge(Monomial monomial) {
        if (monomial.getConstant() == 0 && monomial.getCoefficient() == 0) {
            return new Monomial(0);
        }
        if (monomial.getConstant() != 0 || ObjectUtils.isEmpty(monomial.getVariables()) || monomial.getVariables().stream().anyMatch(variable -> ObjectUtils.isEmpty(variable.getName()))) {
            return new Monomial(monomial.getConstant());
        }
        Map<String, List<Variable>> listMap = monomial.getVariables().stream()
                .filter(variable -> !ObjectUtils.isEmpty(variable.getExponential()))
                .collect(Collectors.groupingBy(Variable::getName));
        List<Variable> variables = listMap.entrySet().stream()
                .map(entry -> {
                    Optional<Double> exponentialOptional = entry.getValue().stream().map(Variable::getExponential).reduce(Double::sum);
                    Double exponential = exponentialOptional.orElse(1.0);
                    return Variable.builder().name(entry.getKey()).exponential(exponential).build();
                })
                .collect(Collectors.toList());
        return new Monomial(monomial.getCoefficient(), variables);
    }

    /**
     * 多项式展开
     *
     * @param monomials 多项式
     * @return monomials 展开后的多项式
     */
    public static List<Monomial> expand(List<Monomial> monomials) {
        monomials = monomials.stream().map(PolynomialUtil::merge).collect(Collectors.toList());
        Map<String, List<Monomial>> listMap = new HashMap<>();
        String CONSTANT = "constant";
        for (Monomial monomial : monomials) {
            if (monomial.getConstant() != 0 || ObjectUtils.isEmpty(monomial.getVariables()) || monomial.getVariables().stream().anyMatch(variable -> ObjectUtils.isEmpty(variable.getName()))) {
                List<Monomial> list = listMap.get(CONSTANT);
                if (ObjectUtils.isEmpty(list)) {
                    list = new ArrayList<>();
                }
                list.add(monomial);
                listMap.put(CONSTANT, list);
                continue;
            }
            List<Variable> variables = monomial.getVariables().stream().sorted(Comparator.comparing(Variable::getName)).collect(Collectors.toList());
            Optional<String> variableKeyOptional = variables.stream().map(variable -> variable.getName() + "_" + variable.getExponential()).reduce((v1, v2) -> v1 + "_" + v2);
            variableKeyOptional.ifPresent(variableKey -> {
                List<Monomial> list = listMap.get(variableKey);
                if (ObjectUtils.isEmpty(list)) {
                    list = new ArrayList<>();
                }
                list.add(monomial);
                listMap.put(variableKey, list);
            });
        }
        List<Monomial> monomialList = new ArrayList<>();
        for (Map.Entry<String, List<Monomial>> entry : listMap.entrySet()) {
            List<Monomial> list = entry.getValue();
            if (entry.getKey().equals(CONSTANT)) {
                Optional<Double> constantOptional = list.stream().map(Monomial::getConstant).reduce(Double::sum);
                constantOptional.ifPresent(constant -> {
                    if (constant != 0) {
                        monomialList.add(new Monomial(constant));
                    }
                });
            } else {
                Optional<Double> coefficientOptional = list.stream().map(Monomial::getCoefficient).reduce(Double::sum);
                coefficientOptional.ifPresent(coefficient -> {
                    if (coefficient != 0) {
                        monomialList.add(new Monomial(coefficient, list.get(0).getVariables()));
                    }
                });
            }
        }
        return monomialList;
    }

    public static String toString(Monomial monomial) {
        StringBuilder stringBuilder = new StringBuilder("单项式 > fx=(");
        if (monomial.getConstant() != 0) {
            if (monomial.getConstant() < 0) {
                stringBuilder.append("(").append(monomial.getConstant()).append(")");
            } else {
                stringBuilder.append(monomial.getConstant());
            }
        } else if (!ObjectUtils.isEmpty(monomial.getVariables()) && monomial.getVariables().stream().noneMatch(variable -> ObjectUtils.isEmpty(variable.getName()))) {
            stringBuilder.append(monomial.getCoefficient());
            for (Variable variable : monomial.getVariables()) {
                stringBuilder.append("*").append("(").append(variable.getName()).append("^").append(variable.getExponential()).append(")");
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public static String toString(List<Monomial> monomials) {
        StringBuilder stringBuilder = new StringBuilder("多项式 >>> fx=(");
        for (int i = 0; i < monomials.size(); i++) {
            if (i != 0) {
                stringBuilder.append(" + ");
            }
            PolynomialUtil.Monomial monomial = monomials.get(i);
            if (monomial.getConstant() != 0) {
                if (monomial.getConstant() < 0) {
                    stringBuilder.append("(").append(monomial.getConstant()).append(")");
                } else {
                    stringBuilder.append(monomial.getConstant());
                }
            } else if (!ObjectUtils.isEmpty(monomial.getVariables()) && monomial.getVariables().stream().noneMatch(variable -> ObjectUtils.isEmpty(variable.getName()))) {
                stringBuilder.append("(").append(monomial.getCoefficient());
                for (Variable variable : monomial.getVariables()) {
                    stringBuilder.append("*").append("(").append(variable.getName()).append("^").append(variable.getExponential()).append(")");
                }
                stringBuilder.append(")");
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Data
    @AllArgsConstructor
    @ApiModel("单项式")
    public static class Monomial {
        @ApiModelProperty(value = "系数 a", example = "2.0")
        private double coefficient = 1.0;
        @ApiModelProperty(value = "变量", example = "x^3")
        List<Variable> variables;
        @ApiModelProperty(value = "常量 c", example = "0.0")
        private double constant = 0.0;

        public Monomial(double coefficient, List<Variable> variables) {
            this.coefficient = coefficient;
            this.variables = variables;
        }

        public Monomial(double constant) {
            this.constant = constant;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("变量")
    public static class Variable {
        @ApiModelProperty(value = "变量名 x", example = "x")
        private String name;
        @ApiModelProperty(value = "指数 b", example = "2.0")
        @Builder.Default
        private Double exponential = 1.0;
    }
}
