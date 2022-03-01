package com.andon.springbootutil.controller;

import com.andon.springbootutil.event.CustomEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Andon
 * 2022/3/1
 */
@Api(tags = "事件监听")
@Slf4j
@RestController
@RequestMapping(value = "/event")
public class TestEventController {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @ApiOperation(value = "事件监听测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id"),
            @ApiImplicitParam(name = "name", value = "name"),
            @ApiImplicitParam(name = "type", value = "type"),
    })
    @GetMapping(value = "/test")
    public void test(String id, String name, String type) {
        log.info("TestEventController test start!! [{}] id:{} name:{} type:{}", Thread.currentThread().getId(), id, name, type);
        applicationEventPublisher.publishEvent(new CustomEvent(name, id, type));
        log.info("TestEventController test end!!");
    }
}
