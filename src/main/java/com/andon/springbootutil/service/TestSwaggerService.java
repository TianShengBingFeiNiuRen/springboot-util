package com.andon.springbootutil.service;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.domain.ResponseStandard;
import com.andon.springbootutil.dto.TestSwaggerTest2Req;
import com.andon.springbootutil.dto.TestSwaggerTest2Resp;
import com.andon.springbootutil.dto.TestSwaggerTestResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * 测试swagger接口文档
 */
@Slf4j
@Service
public class TestSwaggerService {

    public ResponseStandard<TestSwaggerTestResp> test(String param1, String param2) {
        TestSwaggerTestResp data = TestSwaggerTestResp.builder().param1(param1).param2(param2).build();
        log.info("data:{}", JSONObject.toJSONString(data));
        return ResponseStandard.successResponse(data);
    }

    public ResponseStandard<TestSwaggerTest2Resp> test2(TestSwaggerTest2Req testSwaggerTest2Req) {
        String param1 = testSwaggerTest2Req.getParam1();
        String param2 = testSwaggerTest2Req.getParam2();
        TestSwaggerTest2Resp data = TestSwaggerTest2Resp.builder().param1(param1).param2(param2).build();
        log.info("data:{}", JSONObject.toJSONString(data));
        return ResponseStandard.successResponse(data);
    }
}
