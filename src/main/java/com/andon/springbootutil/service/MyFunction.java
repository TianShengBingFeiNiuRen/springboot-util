package com.andon.springbootutil.service;

/**
 * @author Andon
 * 2022/2/10
 * <p>
 * 函数式接口
 * <p>
 * 接口中只有一个抽象方法
 * 写匿名内部类的时候，可以转换成lambda表达式
 */
@FunctionalInterface
public interface MyFunction<T, R> {

    /**
     * 唯一一个抽象方法
     */
    R myApply(T t);

    /**
     * 默认方法
     */
    default void test() {
    }
}
