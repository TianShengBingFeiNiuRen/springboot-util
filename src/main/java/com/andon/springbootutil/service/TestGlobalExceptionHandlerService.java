package com.andon.springbootutil.service;

import com.andon.springbootutil.domain.ResponseStandard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * 测试全局异常处理器
 */
@Slf4j
@Service
public class TestGlobalExceptionHandlerService {

    public void test() {
        int a = 1 / 0;
    }

    public ResponseStandard<String> test2() {
        ResponseStandard<String> response = ResponseStandard.failureResponse(null);
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("TestGlobalExceptionHandlerService test2 failure!! error:{}", e.getMessage());
            response.setMessage("TestGlobalExceptionHandlerService test2 test2Handler2 failure!! error:" + e.getMessage());
            return response;
        }
        response = ResponseStandard.successResponse("");
        return response;
    }
}
