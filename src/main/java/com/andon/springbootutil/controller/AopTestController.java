package com.andon.springbootutil.controller;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.annotation.DemoAnnotation;
import com.andon.springbootutil.dto.TestSwaggerTest2Req;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Andon
 * 2021/12/9
 */
@Slf4j
@Api(tags = "aop")
@RestController
@RequestMapping(value = "/aop")
public class AopTestController {

    @DemoAnnotation(testValue = "test!!")
    @ApiOperation("测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "值", required = true)
    })
    @GetMapping(value = "/test")
    public String test(String value) {
        log.info("test!! value:{}", value);
        return value;
    }

    @DemoAnnotation
    @ApiOperation("测试2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "值", required = true)
    })
    @GetMapping(value = "/test2")
    public String test2(String value) throws Exception {
        log.info("test!! value:{}", value);
        throw new Exception("throw new Exception!!");
    }

    @DemoAnnotation
    @ApiOperation("测试3")
    @PostMapping(value = "/test3")
    public TestSwaggerTest2Req test3(@RequestBody TestSwaggerTest2Req test) {
        log.info("test3!! test:{}", JSONObject.toJSONString(test));
        return test;
    }
}
