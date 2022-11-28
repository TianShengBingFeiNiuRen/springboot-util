package com.andon.springbootutil.controller;

import com.andon.springbootutil.domain.ResponseStandard;
import com.andon.springbootutil.service.TestSwaggerService;
import com.andon.springbootutil.dto.TestSwaggerTest2Req;
import com.andon.springbootutil.dto.TestSwaggerTest2Resp;
import com.andon.springbootutil.dto.TestSwaggerTestResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Andon
 * 2021/11/10
 */
@Api(tags = "测试swagger接口文档")
@RestController
@RequestMapping(value = "/swagger")
public class SwaggerTestController {

    @Resource
    private TestSwaggerService testSwaggerService;

    @ApiOperation("测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param1", value = "参数1", required = true, example = "hello"),
            @ApiImplicitParam(name = "param2", value = "参数2", required = true, example = "world"),
    })
    @GetMapping(value = "/test")
    public ResponseStandard<TestSwaggerTestResp> test(@RequestParam String param1, @RequestParam String param2) {
        return testSwaggerService.test(param1, param2);
    }

    @ApiOperation("测试2")
    @PostMapping(value = "/test2")
    public ResponseStandard<TestSwaggerTest2Resp> test(@RequestBody TestSwaggerTest2Req testSwaggerTest2Req) {
        return testSwaggerService.test2(testSwaggerTest2Req);
    }
}
