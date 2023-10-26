package com.andon.springbootutil.controller;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.response.CommonResponse;
import com.andon.springbootutil.util.JWTUtil;
import com.andon.springbootutil.util.RandomStringUtil;
import com.andon.springbootutil.util.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Andon
 * 2023/3/16
 */
@Slf4j
@Api(tags = "ApiData")
@RestController
public class ApiDataController {

    private static final Map<String, String> appKeySecret = new ConcurrentHashMap<>();
    private static final Map<String, String> appKeyRequestSecret = new ConcurrentHashMap<>();
    private static final Map<String, Long> requestSecretInvalidTimestamp = new ConcurrentHashMap<>();

    private static final Map<String, String> tokenUsernamePassword = new ConcurrentHashMap<>();
    private static final Map<String, Long> tokenInvalidTimestamp = new ConcurrentHashMap<>();

    static {
        appKeySecret.put("1qazCDE)", "2wsxVFR_");
        tokenUsernamePassword.put("1qazCDE)", "2wsxVFR_");
    }

    @PostMapping(value = "/apiData/requestToken")
    public JSONObject requestToken(@RequestBody TokenParams tokenParams) {
        log.info("requestToken >> tokenParams:{}", JSONObject.toJSONString(tokenParams));
        String username = tokenParams.getUsername();
        String password = tokenParams.getPassword();
        String result;
        String code;
        String error = null;
        String token = null;
        if (ObjectUtils.isEmpty(tokenUsernamePassword.get(username)) || !tokenUsernamePassword.get(username).equals(password)) {
            code = "1";
            result = "failure";
            error = "账号密码不正确";
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            code = "0";
            result = "ok";
            token = JWTUtil.generateToken(username, "SecretKeySecretKeySecretKeySecretKeySecretKeySecretKeySecretKeySecretKey", 1L);
            Long tokenInvalidTimestamp = currentTimeMillis + 30 * 60 * 1000;
            ApiDataController.tokenInvalidTimestamp.put(token, tokenInvalidTimestamp);
        }
        JSONObject response = new JSONObject();
        response.put("result", result);
        response.put("code", code);
        response.put("error", error);
        response.put("token", token);
        return response;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class TokenParams {
        String username;
        String password;
    }

    @Scheduled(initialDelay = 0, fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    public void clearToken() {
        long currentTimeMillis = System.currentTimeMillis();
        List<String> tokensInvalid = tokenInvalidTimestamp.entrySet().stream().filter(entry -> entry.getValue() < currentTimeMillis).map(Map.Entry::getKey).collect(Collectors.toList());
        for (String token : tokensInvalid) {
            Long invalidTimestamp = tokenInvalidTimestamp.remove(token);
            log.info("clearToken >> token:{} invalidTimestamp:{} invalidTIme:{}", token, tokensInvalid, TimeUtil.FORMAT.get().format(invalidTimestamp));
        }
    }

    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header")
    @PostMapping(value = "/apiData/queryDataByToken")
    public JSONObject queryDataByToken(@RequestBody TokenBodyParams tokenBodyParams, HttpServletRequest httpServletRequest) {
        log.info("queryDataByToken >> tokenBodyParams:{}", JSONObject.toJSONString(tokenBodyParams));
        String token = httpServletRequest.getHeader("Authorization").replaceFirst("jwt ", "");
        String code;
        String error;
        long currentTimeMillis = System.currentTimeMillis();
        if (ObjectUtils.isEmpty(ApiDataController.tokenInvalidTimestamp.get(token))) {
            code = "01";
            error = "未授权的token";
        } else if (ApiDataController.tokenInvalidTimestamp.get(token) < currentTimeMillis) {
            code = "02";
            error = "token已过期";
        } else {
            code = "00";
            error = null;
        }
        JSONObject response = new JSONObject();
        response.put("code", code);
        response.put("error", error);
        response.put("datas", tokenBodyParams);
        response.put("task_id", UUID.randomUUID().toString().replaceAll("-", ""));
        response.put("task_time", TimeUtil.FORMAT.get().format(currentTimeMillis));
        return response;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class TokenBodyParams {
        String dataParam1;
        String dataParam2;
        String dataParam3;
    }

    @PostMapping(value = "/apiData/requestSecret")
    public JSONObject requestSecret(AppSignParams appSignParams) throws NoSuchAlgorithmException {
        log.info("requestSecret >> appSignParams:{}", JSONObject.toJSONString(appSignParams));
        String appKey = appSignParams.getAppKey();
        String sign = appSignParams.getSign();
        Long requestTime = appSignParams.getRequestTime();
        String code;
        String msg;
        JSONObject datas = new JSONObject();
        JSONObject signLocal = getSign(GetSignParams.builder().appKey(appKey).secret(appKeySecret.get(appKey)).requestTime(requestTime).build());
        if (ObjectUtils.isEmpty(appKeySecret.get(appKey)) || !signLocal.getString("sign").equals(sign)) {
            code = "01";
            msg = "appKey或appSecret不正确";
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            code = "00";
            msg = "成功";
            String refreshSecret = UUID.randomUUID().toString().replaceAll("-", "");
            long refreshSecretEndTime = currentTimeMillis + 48 * 60 * 60 * 1000;
            String requestSecret = UUID.randomUUID().toString().replaceAll("-", "");
            long requestSecretEndTime = currentTimeMillis + 15 * 60 * 1000;
            datas.put("refreshSecret", refreshSecret);
            datas.put("refreshSecretEndTime", refreshSecretEndTime);
            datas.put("requestSecret", requestSecret);
            datas.put("requestSecretEndTime", requestSecretEndTime);
            appKeyRequestSecret.put(appKey, requestSecret);
            requestSecretInvalidTimestamp.put(appKey, requestSecretEndTime);
        }
        JSONObject response = new JSONObject();
        response.put("code", code);
        response.put("msg", msg);
        response.put("datas", datas);
        return response;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class AppSignParams {
        String appKey;
        String sign;
        Long requestTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class GetSignParams {
        String appKey;
        String secret;
        Long requestTime;
    }

    @GetMapping(value = "/apiData/getSign")
    public JSONObject getSign(GetSignParams getSignParams) throws NoSuchAlgorithmException {
        log.info("getSign >> getSignParams:{}", JSONObject.toJSONString(getSignParams));
        String appKey = getSignParams.getAppKey();
        String secret = getSignParams.getSecret();
        Long requestTime = getSignParams.getRequestTime();
        String sign = getMd5Hash(appKey + secret + requestTime);
        JSONObject response = new JSONObject();
        response.put("appKey", appKey);
        response.put("secret", secret);
        response.put("sign", sign);
        response.put("requestTime", requestTime);
        return response;
    }

    private String getMd5Hash(String content) throws NoSuchAlgorithmException {
        String algorithm = "MD5";
        MessageDigest md5 = MessageDigest.getInstance(algorithm);
        byte[] digest = md5.digest(content.getBytes());
        return parseByteToHexStr(digest);
    }

    private String parseByteToHexStr(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte aByte : bytes) {
            String hexString = Integer.toHexString(aByte & 0xFF);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            stringBuilder.append(hexString.toLowerCase());
        }
        return stringBuilder.toString();
    }

    @PostMapping(value = "/apiData/queryDataByRequestSecret")
    public JSONObject queryDataByRequestSecret(AppKeyBodyParams appKeyBodyParams) throws NoSuchAlgorithmException {
        log.info("queryDataByRequestSecret >> appKeyBodyParams:{}", JSONObject.toJSONString(appKeyBodyParams));
        String appKey = appKeyBodyParams.getAppKey();
        String code;
        String msg;
        if (ObjectUtils.isEmpty(appKeySecret.get(appKey))) {
            code = "01";
            msg = "appKey不存在";
        } else if (ObjectUtils.isEmpty(appKeyRequestSecret.get(appKey)) || !appKeyBodyParams.getSign().equals(getMd5Hash(appKey + appKeyRequestSecret.get(appKey) + appKeyBodyParams.getRequestTime()))) {
            code = "02";
            msg = "请求密钥不正确";
        } else if (ObjectUtils.isEmpty(requestSecretInvalidTimestamp.get(appKey)) || requestSecretInvalidTimestamp.get(appKey) < Long.parseLong(appKeyBodyParams.getRequestTime())) {
            code = "03";
            msg = "请求密钥已过期";
        } else {
            code = "00";
            msg = "成功";
        }
        JSONObject response = new JSONObject();
        response.put("code", code);
        response.put("msg", msg);
        response.put("datas", appKeyBodyParams);
        return response;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class AppKeyBodyParams {
        String appKey;
        String sign;
        String requestTime;
        String dataParam1;
        String dataParam2;
    }

    @GetMapping(value = "/apiData/batchGet")
    public CommonResponse<ApiDataResponse> batchGet(String key, String value) {
        log.info("batchGet >> key:{} value:{}", key, value);
        String time = TimeUtil.FORMAT.get().format(System.currentTimeMillis());
        Integer sequence = Integer.valueOf(RandomStringUtil.stringGenerate(6, true, false, false, false));
        ApiDataResponse apiDataResponse = ApiDataResponse.builder().id("batchGet").key(key).value(value).sequence(sequence).date(time).build();
        return CommonResponse.successResponse(apiDataResponse);
    }

    @GetMapping(value = "/apiData/get")
    public CommonResponse<List<ApiDataResponse>> get(String key, String value) {
        log.info("get >> key:{} value:{}", key, value);
        List<ApiDataResponse> data = new ArrayList<>();
        String time = TimeUtil.FORMAT.get().format(System.currentTimeMillis());
        if (!ObjectUtils.isEmpty(key) || !ObjectUtils.isEmpty(value)) {
            data.add(ApiDataResponse.builder().id("get").key(key).value(value).sequence(1).date(time).build());
        }
        logHeaders();
        addData(data, time);
        return CommonResponse.successResponse(data, data.size());
    }

    @PostMapping(value = "/apiData/batchPost")
    public CommonResponse<ApiDataResponse> batchPost(BodyParam bodyParam) {
        log.info("batchPost >> bodyKey:{} bodyValue:{}", bodyParam.bodyKey, bodyParam.bodyValue);
        String time = TimeUtil.FORMAT.get().format(System.currentTimeMillis());
        Integer sequence = Integer.valueOf(RandomStringUtil.stringGenerate(6, true, false, false, false));
        ApiDataResponse apiDataResponse = ApiDataResponse.builder().id("batchPost").key(bodyParam.bodyKey).value(bodyParam.bodyValue).sequence(sequence).date(time).build();
        return CommonResponse.successResponse(apiDataResponse);
    }

    @PostMapping(value = "/apiData/post")
    public CommonResponse<List<ApiDataResponse>> post(BodyParam bodyParam) {
        log.info("post >> bodyKey:{} bodyValue:{}", bodyParam.bodyKey, bodyParam.bodyValue);
        List<ApiDataResponse> data = new ArrayList<>();
        String time = TimeUtil.FORMAT.get().format(System.currentTimeMillis());
        if (!ObjectUtils.isEmpty(bodyParam.bodyKey) || !ObjectUtils.isEmpty(bodyParam.bodyValue)) {
            data.add(ApiDataResponse.builder().id("post").key(bodyParam.bodyKey).value(bodyParam.bodyValue).sequence(1).date(time).build());
        }
        logHeaders();
        addData(data, time);
        return CommonResponse.successResponse(data, data.size());
    }

    @PostMapping(value = "/apiData/batchJson")
    public CommonResponse<ApiDataResponse> batchJson(@RequestBody BodyParam bodyParam) {
        log.info("batchJson >> bodyKey:{} bodyValue:{}", bodyParam.bodyKey, bodyParam.bodyValue);
        String time = TimeUtil.FORMAT.get().format(System.currentTimeMillis());
        Integer sequence = Integer.valueOf(RandomStringUtil.stringGenerate(6, true, false, false, false));
        ApiDataResponse apiDataResponse = ApiDataResponse.builder().id("batchJson").key(bodyParam.bodyKey).value(bodyParam.bodyValue).sequence(sequence).date(time).build();
        return CommonResponse.successResponse(apiDataResponse);
    }

    @PostMapping(value = "/apiData/json")
    public CommonResponse<List<ApiDataResponse>> json(@RequestBody BodyParam bodyParam) {
        log.info("json >> bodyKey:{} bodyValue:{}", bodyParam.bodyKey, bodyParam.bodyValue);
        List<ApiDataResponse> data = new ArrayList<>();
        String time = TimeUtil.FORMAT.get().format(System.currentTimeMillis());
        if (!ObjectUtils.isEmpty(bodyParam.bodyKey) || !ObjectUtils.isEmpty(bodyParam.bodyValue)) {
            data.add(ApiDataResponse.builder().id("json").key(bodyParam.bodyKey).value(bodyParam.bodyValue).sequence(1).date(time).build());
        }
        logHeaders();
        addData(data, time);
        return CommonResponse.successResponse(data, data.size());
    }

    @PostMapping(value = "/apiData/header")
    public CommonResponse<List<ApiDataResponse>> header(@RequestHeader(required = false) String headerKey, @RequestHeader(required = false) String headerValue) {
        log.info("header >> headerKey:{} headerValue:{}", headerKey, headerValue);
        List<ApiDataResponse> data = new ArrayList<>();
        String time = TimeUtil.FORMAT.get().format(System.currentTimeMillis());
        if (!ObjectUtils.isEmpty(headerKey) || !ObjectUtils.isEmpty(headerValue)) {
            data.add(ApiDataResponse.builder().id("header").key(headerKey).value(headerValue).sequence(1).date(time).build());
        }
        logHeaders();
        addData(data, time);
        return CommonResponse.successResponse(data, data.size());
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
            String key = RandomStringUtil.stringGenerate(5, false, true, false, false);
            String value = RandomStringUtil.stringGenerate(5, false, false, true, true);
            Integer sequence = Integer.valueOf(RandomStringUtil.stringGenerate(6, true, false, false, false));
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
