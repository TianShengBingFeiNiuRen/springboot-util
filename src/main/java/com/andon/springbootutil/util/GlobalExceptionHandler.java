package com.andon.springbootutil.util;

import com.andon.springbootutil.domain.ResponseStandard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice //对Controller增强,并返回json格式字符串
public class GlobalExceptionHandler {

    /**
     * 捕获Exception异常,并自定义返回数据
     */
    @ExceptionHandler(Exception.class)
    public ResponseStandard<Object> exception(Exception e, HttpServletRequest request) {
        log.error("request error!! method:{} uri:{}", request.getMethod(), request.getRequestURI());
        log.error(getExceptionDetail(e));
        return ResponseStandard.builder().code(-1).message(request.getMethod() + " " + request.getRequestURI() + " " + e.getMessage()).build();
    }

    /**
     * 获取代码报错详细位置信息
     */
    public String getExceptionDetail(Exception e) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(e.getClass()).append(System.getProperty("line.separator"));
        stringBuilder.append(e.getLocalizedMessage()).append(System.getProperty("line.separator"));
        StackTraceElement[] arr = e.getStackTrace();
        for (StackTraceElement stackTraceElement : arr) {
            stringBuilder.append(stackTraceElement.toString()).append(System.getProperty("line.separator"));
        }
        return stringBuilder.toString();
    }
}
