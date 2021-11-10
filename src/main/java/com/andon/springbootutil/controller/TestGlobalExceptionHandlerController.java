package com.andon.springbootutil.controller;

import com.andon.springbootutil.domain.ResponseStandard;
import com.andon.springbootutil.service.TestGlobalExceptionHandlerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * 测试全局异常处理器
 */
@RestController
@RequestMapping(value = "/globalExceptionHandler")
public class TestGlobalExceptionHandlerController {

    @Resource
    private TestGlobalExceptionHandlerService testGlobalExceptionHandlerService;

    @GetMapping(value = "/test")
    public void test() {
        testGlobalExceptionHandlerService.test();
    }

    @GetMapping(value = "/test2")
    public ResponseStandard<String> test2() {
        return testGlobalExceptionHandlerService.test2();
    }
}
