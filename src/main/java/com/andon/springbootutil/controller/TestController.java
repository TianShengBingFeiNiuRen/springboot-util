package com.andon.springbootutil.controller;

import com.andon.springbootutil.domain.ResponseStandard;
import com.andon.springbootutil.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Andon
 * 2021/11/10
 */
@RestController
public class TestController {

    @Resource
    private TestService testService;

    @GetMapping(value = "/testGlobalExceptionHandler")
    public void testGlobalExceptionHandler() {
        testService.testGlobalExceptionHandler();
    }

    @GetMapping(value = "/testGlobalExceptionHandler2")
    public ResponseStandard<String> testGlobalExceptionHandler2() {
        return testService.testGlobalExceptionHandler2();
    }
}
