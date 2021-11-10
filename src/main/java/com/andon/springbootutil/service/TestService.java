package com.andon.springbootutil.service;

import com.andon.springbootutil.domain.ResponseStandard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Andon
 * 2021/11/10
 */
@Slf4j
@Service
public class TestService {

    public void testGlobalExceptionHandler() {
        int a = 1 / 0;
    }

    public ResponseStandard<String> testGlobalExceptionHandler2() {
        ResponseStandard<String> response = ResponseStandard.failureResponse("");
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("testGlobalExceptionHandler2 failure!! error:{}", e.getMessage());
            response.setMessage("testGlobalExceptionHandler2 failure!! error:" + e.getMessage());
            return response;
        }
        response = ResponseStandard.successResponse("");
        return response;
    }
}
