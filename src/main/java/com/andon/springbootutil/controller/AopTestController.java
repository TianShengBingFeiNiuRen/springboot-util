package com.andon.springbootutil.controller;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.annotation.DemoAnnotation;
import com.andon.springbootutil.request.TestSwaggerTest2Req;
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
    @GetMapping(value = "/test")
    public void test() {
        log.info("test!!");
    }

    @DemoAnnotation(testValue = "test!!", message = "传递了参数 value:{value}")
    @ApiOperation("测试1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "值", required = true)
    })
    @GetMapping(value = "/test1")
    public String test1(String value) {
        log.info("test1!! value:{}", value);
        return value;
    }

    @DemoAnnotation(message = "传递了参数 value:{value}")
    @ApiOperation("测试2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "值", required = true)
    })
    @GetMapping(value = "/test2")
    public String test2(@RequestParam String value) throws Exception {
        log.info("test2!! value:{}", value);
        throw new Exception("throw new Exception!!");
    }

    @DemoAnnotation(message = "传递了参数 param1:{param1} param2:{param2}")
    @ApiOperation("测试3")
    @PostMapping(value = "/test3")
    public TestSwaggerTest2Req test3(@RequestBody TestSwaggerTest2Req test) {
        log.info("test3!! test:{}", JSONObject.toJSONString(test));
        return test;
    }

    @DemoAnnotation(message = "传递了参数 param1:{param1} param2:{param2}")
    @ApiOperation("测试4")
    @PostMapping(value = "/test4")
    public TestSwaggerTest2Req test4(TestSwaggerTest2Req test) {
        log.info("test4!! test:{}", JSONObject.toJSONString(test));
        return test;
    }
}
