package com.andon.springbootutil.controller;

import com.andon.springbootutil.constant.EnumSex;
import com.andon.springbootutil.service.impl.TestServiceImpl1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Andon
 * 2022/6/8
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestServiceImpl1 testService;

    @GetMapping(value = "/test/log")
    public String log() {
        String s = testService.log();
        log.info("{}", s);
        return s;
    }

    @GetMapping(value = "/test/getID")
    public String getID() {
        String id = UUID.randomUUID().toString();
        log.info("{}", id);
        return id;
    }

    @GetMapping(value = "/test/sex")
    public EnumSex getSex(@RequestParam("sex") EnumSex enumSex) {
        log.info("enumSex:{}", enumSex);
        return enumSex;
    }
}
