package com.andon.springbootutil.controller;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.RestTemplateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andon
 * 2023/3/28
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
@Api(tags = "ApiDataCNFromSign")
@RestController
public class ApiDataCNFromSignController {

    @ApiOperation("获取秘钥")
    @PostMapping(value = "/apiDataCNFromSign/getToken")
    public JSONObject getToken(@RequestBody GetAppKeyTokenParam getAppKeyTokenParam) throws NoSuchAlgorithmException {
        log.info("/apiDataCNFromSign/getToken >> getTokenParam:{}", JSONObject.toJSONString(getAppKeyTokenParam));
        String url = getAppKeyTokenParam.getUrl();
        String appKey = getAppKeyTokenParam.getAppKey();
        String appSecret = getAppKeyTokenParam.getAppSecret();
        long time = System.currentTimeMillis();

        String sign = getMd5Hash(appKey + appSecret + time);

        Map<String, String> param = new HashMap<>();
        param.put("appKey", appKey);
        param.put("sign", sign);
        param.put("requestTime", String.valueOf(time));

        String response = RestTemplateUtil.sendHttp(url, "POST", null, JSONObject.toJSONString(param), "FORM_DATA", null);
        log.info("response:{}", response);
        return JSONObject.parseObject(response);
    }

    @ApiOperation("获取数据")
    @PostMapping(value = "/apiDataCNFromSign/getData")
    public JSONObject getData(@RequestBody JSONObject jsonParam) throws NoSuchAlgorithmException {
        log.info("/apiDataCNFromSign/getData >> jsonParam:{}", JSONObject.toJSONString(jsonParam));
        String url = jsonParam.getString("url");
        String appKey = jsonParam.getString("appKey");
        String requestSecret = jsonParam.getString("requestSecret");
        long time = System.currentTimeMillis();

        String sign = getMd5Hash(appKey + requestSecret + time);

        jsonParam.remove("url");
        jsonParam.remove("appKey");
        jsonParam.remove("requestSecret");
        jsonParam.put("appKey", appKey);
        jsonParam.put("sign", sign);
        jsonParam.put("requestTime", String.valueOf(time));

        String response = RestTemplateUtil.sendHttp(url, "POST", null, JSONObject.toJSONString(jsonParam), "FORM_DATA", null);
        log.info("response:{}", response);
        return JSONObject.parseObject(response);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class GetAppKeyTokenParam {
        @ApiModelProperty(value = "url", example = "https://interface.zjzwfw.gov.cn/gateway/app/refreshTokenByKey.htm")
        String url;
        @ApiModelProperty(value = "appKey", example = "A330327325021202207014375")
        String appKey;
        @ApiModelProperty(value = "appSecret", example = "57a0d8f2812c428797ae1de2886cb98a")
        String appSecret;
    }

    private String getMd5Hash(String content) throws NoSuchAlgorithmException {
        String algorithm = "MD5";
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] digest = md.digest((content).getBytes());
        return parseByteToHexStr(digest);
    }

    private String parseByteToHexStr(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (byte aByte : bytes) {
            String hexString = Integer.toHexString(aByte & 0xFF);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            stringBuffer.append(hexString.toLowerCase());
        }
        return stringBuffer.toString();
    }
}
