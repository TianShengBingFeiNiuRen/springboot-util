package com.andon.springbootutil.annotation;

import java.lang.annotation.*;

/**
 * @author Andon
 * 2021/12/9
 * <p>
 * 自定义注解
 */
@Target(ElementType.METHOD) //定义注解使用位置
@Retention(RetentionPolicy.RUNTIME) //注解声明周期
@Documented //可以被工具文档化
public @interface DemoAnnotation {

    String testValue() default "testValue";
}
