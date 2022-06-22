package com.andon.springbootutil.service.impl;

import com.andon.springbootutil.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Andon
 * 2022/6/8
 */
@Slf4j
@Service
public class TestServiceImpl2 implements TestService {

    @Override
    public String log() {
        return "TestServiceImpl2!!";
    }
}
