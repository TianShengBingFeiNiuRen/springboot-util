package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.JWTUtil;
import com.andon.springbootutil.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Map;

/**
 * @author Andon
 * 2021/11/10
 */
@Slf4j
public class Demo {

    @Test
    public void test02() {
        String username = "java";
        log.info("username:{}", username);
        Map<String, Object> map = JWTUtil.tokenGenerate(username);
        String token = String.valueOf(map.get("token"));
        String timestamp_expiration = String.valueOf(map.get("timestamp_expiration"));
        long timestamp = Long.parseLong(timestamp_expiration);
        log.info("map:{}", JSONObject.toJSONString(map));
        log.info("token:{}", token);
        log.info("timestamp_expiration:{}", timestamp_expiration);
        log.info("timestamp:{}", timestamp);
        String resolveUsername = JWTUtil.tokenResolveUsername(token);
        log.info("resolveUsername:{}", resolveUsername);
    }

    @Test
    public void test01() {
        String id = RandomUtil.generateID();
        log.info("id:{}", id);
    }
}
