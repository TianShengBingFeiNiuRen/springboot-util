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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andon
 * 2023/3/30
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
@Api(tags = "ApiDataCNFromToken")
@RestController
public class ApiDataCNFromTokenController {

    @ApiOperation("获取Token")
    @PostMapping(value = "/apiDataCNFromToken/getToken")
    public JSONObject getToken(@RequestBody GetTokenParam getTokenParam) {
        log.info("/apiDataCNFromToken/getToken >> getTokenParam:{}", JSONObject.toJSONString(getTokenParam));
        String url = getTokenParam.getUrl();
        String username = getTokenParam.getUsername();
        String password = getTokenParam.getPassword();

        Map<String, String> param = new HashMap<>();
        param.put("username", username);
        param.put("password", password);

        String response = RestTemplateUtil.sendHttp(url, "POST", null, JSONObject.toJSONString(param), "JSON", null);
        log.info("response:{}", response);
        return JSONObject.parseObject(response);
    }

    @ApiOperation("获取数据")
    @PostMapping(value = "/apiDataCNFromToken/getData")
    public JSONObject getData(@RequestBody JSONObject jsonParam) {
        log.info("/apiDataCNFromToken/getData >> jsonParam:{}", JSONObject.toJSONString(jsonParam));
        String url = jsonParam.getString("url");
        String token = jsonParam.getString("token");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "jwt " + token);
        jsonParam.remove("url");
        jsonParam.remove("token");
        String response = RestTemplateUtil.sendHttp(url, "POST", null, JSONObject.toJSONString(jsonParam), "JSON", headers);
        log.info("response:{}", response);
        return JSONObject.parseObject(response);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class GetTokenParam {
        @ApiModelProperty(value = "url", example = "https://api-dshare.wenzhou.gov.cn/webapi2/get_token")
        String url;
        @ApiModelProperty(value = "username", example = "yxg.ggsj")
        String username;
        @ApiModelProperty(value = "password", example = "0c13a904-ea9d-453e-a1dd-f5281a74f688")
        String password;
    }
}
