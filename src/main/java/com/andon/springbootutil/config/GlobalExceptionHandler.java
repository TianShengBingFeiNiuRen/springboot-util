package com.andon.springbootutil.config;

import com.andon.springbootutil.exception.AuthorityException;
import com.andon.springbootutil.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * @author Andon
 * 2023/5/17
 * <p>
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * token认证异常
     */
    @ExceptionHandler(AuthorityException.class)
    public CommonResponse<Object> exception(AuthorityException e, HttpServletRequest request) {
        log.warn("RemoteAddr:{} request warn!! Method:{} RequestURI:{} error:{}", request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), e.getMessage());
        return CommonResponse.builder().code(e.getHttpStatus().value()).message(e.getMessage()).build();
    }

    /**
     * 捕获参数校验异常
     */
    @ExceptionHandler(BindException.class)
    public CommonResponse<Object> exception(BindException e, HttpServletRequest request) {
        String error = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        log.warn("RemoteAddr:{} request warn!! Method:{} RequestURI:{} error:{}", request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), error);
        return CommonResponse.builder().code(-1).message(error).build();
    }

    /**
     * 捕获参数校验异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public CommonResponse<Object> exception(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("RemoteAddr:{} request warn!! Method:{} RequestURI:{} error:{}", request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), e.getMessage());
        return CommonResponse.builder().code(-1).message(e.getMessage()).build();
    }

    /**
     * 捕获Exception异常,并自定义返回数据
     */
    @ExceptionHandler(Exception.class)
    public CommonResponse<Object> exception(Exception e, HttpServletRequest request) {
        log.error("RemoteAddr:{} request warn!! Method:{} RequestURI:{} error:{}", request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), e.getMessage());
        log.error(getExceptionDetail(e));
        return CommonResponse.builder().code(-1).message("未知异常").build();
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
