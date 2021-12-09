package com.andon.springbootutil.aop;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author Andon
 * 2021/12/9
 * <p>
 * 切面类
 */
@Slf4j
@Aspect //定义切面类
@Component //交给Spring来管理
public class DemoAspect {

    /**
     * 声明切入点，包名表达式或者注解路径
     */
    @Pointcut("@annotation(com.andon.springbootutil.annotation.DemoAnnotation)")
    public void pointCut() {
    }

    /**
     * 前置通知，关注点执行前运行的方法
     */
    @Before("pointCut()")
    public void before() {
        log.info("before [{}-{}] 前置通知!!", Thread.currentThread().getName(), Thread.currentThread().getId());
    }

    /**
     * 后置通知，不论一个方法是如何结束的，最终通知都会运行
     */
    @After("pointCut()")
    public void after() {
        log.info("after [{}-{}] 后置通知!!", Thread.currentThread().getName(), Thread.currentThread().getId());
    }

    /**
     * 后置通知，获取方法的返回值
     * returning的赋值的名字,必须跟通知方法中参数的名字保持一致
     */
    @AfterReturning(value = "pointCut()", returning = "value")
    public Object afterReturning(Object value) {
        log.info("afterReturning [{}-{}] 返回后通知!! value:{}", Thread.currentThread().getName(), Thread.currentThread().getId(), value);
        return value;
    }

    /**
     * 异常通知
     * 如果想要限制通知只在某种特定的异常被抛出的时候匹配，同时还想知道异常的一些信息。
     * 那我们就需要使用throwing属性声明响应
     */
    @AfterThrowing(value = "pointCut()", throwing = "exception")
    public void afterThrowing(Exception exception) {
        log.info("afterThrowing [{}-{}] 异常通知!! error:{}", Thread.currentThread().getName(), Thread.currentThread().getId(), exception.getMessage());
    }

    /**
     * 环绕通知
     */
    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String remoteHost = request.getRemoteHost(); //访问者ip
        String method = request.getMethod(); //请求方式
        String uri = request.getRequestURI(); //请求路径
        String methodName = pjp.getSignature().getName(); //获取方法名
        Enumeration<String> headerNames = request.getHeaderNames();
        JSONObject headers = new JSONObject();
        while (headerNames.hasMoreElements()) {
            String s = headerNames.nextElement();
            headers.put(s, request.getHeader(s)); //请求头
        }
        Object[] args = pjp.getArgs(); //获取入参
        log.info("around [{}-{}] 环绕通知start!! remoteHost:{} method:{} uri:{} methodName:{} headers:{} args:{}", Thread.currentThread().getName(), Thread.currentThread().getId(), remoteHost, method, uri, methodName, headers.toJSONString(), JSONObject.toJSONString(args));
        Object obj = pjp.proceed();
        log.info("around [{}-{}] 环绕通知end!!", Thread.currentThread().getName(), Thread.currentThread().getId());
        return obj;
    }
}
