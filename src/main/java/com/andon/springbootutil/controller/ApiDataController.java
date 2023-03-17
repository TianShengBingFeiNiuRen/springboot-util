package com.andon.springbootutil.controller;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.domain.ResponseStandard;
import com.andon.springbootutil.util.RandomStringUtil;
import com.andon.springbootutil.util.TimeUtil;
import io.swagger.annotations.Api;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

/**
 * @author Andon
 * 2023/3/16
 */
@Slf4j
@Api(tags = "ApiData")
@RestController
public class ApiDataController {

    @GetMapping(value = "/apiData/get")
    public ResponseStandard<List<ApiDataResponse>> get(String key, String value) {
        log.info("get >> key:{} value:{}", key, value);
        List<ApiDataResponse> data = new ArrayList<>();
        String time = TimeUtil.FORMAT.get().format(System.currentTimeMillis());
        if (!ObjectUtils.isEmpty(key) || !ObjectUtils.isEmpty(value)) {
            data.add(ApiDataResponse.builder().id("get").key(key).value(value).sequence(1).date(time).build());
        }
        logHeaders();
        addData(data, time);
        return ResponseStandard.successResponse(data, data.size());
    }

    @PostMapping(value = "/apiData/post")
    public ResponseStandard<List<ApiDataResponse>> post(BodyParam bodyParam) {
        log.info("post >> bodyKey:{} bodyValue:{}", bodyParam.bodyKey, bodyParam.bodyValue);
        List<ApiDataResponse> data = new ArrayList<>();
        String time = TimeUtil.FORMAT.get().format(System.currentTimeMillis());
        if (!ObjectUtils.isEmpty(bodyParam.bodyKey) || !ObjectUtils.isEmpty(bodyParam.bodyValue)) {
            data.add(ApiDataResponse.builder().id("post").key(bodyParam.bodyKey).value(bodyParam.bodyValue).sequence(1).date(time).build());
        }
        logHeaders();
        addData(data, time);
        return ResponseStandard.successResponse(data, data.size());
    }

    @PostMapping(value = "/apiData/json")
    public ResponseStandard<List<ApiDataResponse>> json(@RequestBody BodyParam bodyParam) {
        log.info("json >> bodyKey:{} bodyValue:{}", bodyParam.bodyKey, bodyParam.bodyValue);
        List<ApiDataResponse> data = new ArrayList<>();
        String time = TimeUtil.FORMAT.get().format(System.currentTimeMillis());
        if (!ObjectUtils.isEmpty(bodyParam.bodyKey) || !ObjectUtils.isEmpty(bodyParam.bodyValue)) {
            data.add(ApiDataResponse.builder().id("json").key(bodyParam.bodyKey).value(bodyParam.bodyValue).sequence(1).date(time).build());
        }
        logHeaders();
        addData(data, time);
        return ResponseStandard.successResponse(data, data.size());
    }

    @PostMapping(value = "/apiData/header")
    public ResponseStandard<List<ApiDataResponse>> header(@RequestHeader(required = false) String headerKey, @RequestHeader(required = false) String headerValue) {
        log.info("header >> headerKey:{} headerValue:{}", headerKey, headerValue);
        List<ApiDataResponse> data = new ArrayList<>();
        String time = TimeUtil.FORMAT.get().format(System.currentTimeMillis());
        if (!ObjectUtils.isEmpty(headerKey) || !ObjectUtils.isEmpty(headerValue)) {
            data.add(ApiDataResponse.builder().id("header").key(headerKey).value(headerValue).sequence(1).date(time).build());
        }
        logHeaders();
        addData(data, time);
        return ResponseStandard.successResponse(data, data.size());
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class BodyParam {
        String bodyKey;
        String bodyValue;
    }

    private void logHeaders() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        JSONObject headers = new JSONObject();
        while (headerNames.hasMoreElements()) {
            String s = headerNames.nextElement();
            headers.put(s, request.getHeader(s));
        }
        log.info("headers:{}", JSONObject.toJSONString(headers));
    }

    private void addData(List<ApiDataResponse> data, String time) {
        for (int i = 0; i < 10; i++) {
            String id = String.valueOf(UUID.randomUUID());
            String key = RandomStringUtil.stringGenerate(5, false, true, false);
            String value = RandomStringUtil.stringGenerate(5, false, false, true);
            Integer sequence = Integer.valueOf(RandomStringUtil.stringGenerate(6, true, false, false));
            data.add(ApiDataResponse.builder().id(id).key(key).value(value).sequence(sequence).date(time).build());
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class ApiDataResponse {
        String id;
        String key;
        String value;
        Integer sequence;
        String date;
    }
}
