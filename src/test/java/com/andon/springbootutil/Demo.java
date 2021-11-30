package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.*;
import com.andon.springbootutil.vo.TestSwaggerTestResp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Andon
 * 2021/11/10
 */
@Slf4j
public class Demo {

    @Test
    public void test12() {
        UUID uuid = UUID.randomUUID();
        log.info("uuid:{}", uuid.toString());
        log.info("length:{}", uuid.toString().length());
    }

    @Test
    public void test11() {
        String str = "08bf2ad8b22ec263e9d6c9055c1591157fc6b064";
        log.info("length:{}", str.length());
    }

    @Test
    public void test10() {
        LocalDate now = LocalDate.now();
        log.info("now:{}", now.toString());
        log.info("year:{}", now.getYear());
        String dateStartStr = LocalDate.now().getYear() + "-01-01";
        java.sql.Date date = java.sql.Date.valueOf(dateStartStr);
        log.info("dateStartStr:{}", dateStartStr);
        log.info("date:{}", date);
    }

    @Test
    public void test09() {
        String str = "数据库";
        String pingYin = PinYin4JUtil.getPingYin(str);
        String pinYinHeadChar = PinYin4JUtil.getPinYinHeadChar(str);
        log.info("pingYin:{}", pingYin);
        log.info("pinYinHeadChar:{}", pinYinHeadChar);
    }

    @Test
    public void test08() {
        long timeMillis = System.currentTimeMillis();
        String format = TimeUtil.FORMAT.get().format(System.currentTimeMillis());
        log.info("timeMillis:{}", timeMillis);
        log.info("format:{}", format);
        String week = TimeUtil.getWeek(new Date());
        log.info("week:{}", week);
    }

    @Test
    public void test07() {
        // ZoneId代表的是时区，获取系统时区：
        ZoneId zoneId = ZoneId.systemDefault();
        log.info("zoneId:{}", zoneId.toString());
        // 东八区
        ZoneId zoneIdUTC8 = ZoneId.of("UTC+8");
        log.info("zoneIdUTC8:{}", zoneIdUTC8);
        // 0时区
        LocalDate now = LocalDate.now();
        log.info("now:{}", now);
        // 东八区
        LocalDate nowUTC8 = LocalDate.now(ZoneId.of("UTC+8"));
        log.info("nowUTC8:{}", nowUTC8);
        // DateTimeFormatter 时间格式化成String
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = dateTimeFormatter.format(LocalDateTime.now(ZoneId.of("UTC+0")));
        String format2 = dateTimeFormatter.format(LocalDateTime.now(ZoneId.of("UTC+8")));
        log.info("format:{}", format);
        log.info("format2:{}", format2);
        // 时间戳
        Instant now1 = Instant.now();
        log.info("now1:{}", now1);
        now1.atZone(ZoneId.of("UTC+8"));
        log.info("now1:{}", now1);
        long l = now1.toEpochMilli();
        log.info("l:{}", l);
        // 时间戳转时间
        long timestamp = System.currentTimeMillis();
        String time = dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()));
        String time2 = dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC+0")));
        log.info("timestamp:{}", timestamp);
        log.info("time:{}", time);
        log.info("time2:{}", time2);
    }

    @Test
    public void test06() {
        String url = "http://127.0.0.1:8080/swagger/test";
        Map<String, String> params = new HashMap<>();
        params.put("param1", "hello");
        params.put("param2", "world");
        String response = null;
        try {
            response = HttpClientUtil.doGet(url, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("response:{}", response);

        String url2 = "http://127.0.0.1:8080/swagger/test2";
        String json = JSONObject.toJSONString(params);
        String response2 = null;
        try {
            response2 = HttpClientUtil.doPostJson(url2, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("response2:{}", response2);
    }

    @Test
    public void test05() {
        TestSwaggerTestResp param1 = TestSwaggerTestResp.builder().param1("param1").build();
        TestSwaggerTestResp param2 = DeepCopyUtil.deepCopy(param1);
        System.out.println(param1 == param2);
        log.info("param1:{}", JSONObject.toJSONString(param1));
        log.info("param2:{}", JSONObject.toJSONString(param2));
    }

    @Test
    public void test04() {
        String content = "test"; //明文内容
        log.info("明文:{}", content);
        System.out.println("AES加密==========");
        // AES加密
        byte[] encrypt = AESUtil.encrypt(content);
        log.info("密文:{}", encrypt);

        String parseByteToHexStr = AESUtil.parseByteToHexStr(encrypt);
        log.info("密文--字节数组转换成16进制:{}", parseByteToHexStr);
        String base64EncodeToString = AESUtil.base64EncodeToString(encrypt);
        log.info("密文--Base64处理字节数组转换为字符串:{}", base64EncodeToString);

        System.out.println("AES解密==========");
        // AES解密
        byte[] parseHexStrToByte = AESUtil.parseHexStrToByte(parseByteToHexStr);
        log.info("密文--16进制转换成字节数组:{}", parseHexStrToByte);
        byte[] base64DecodeToByte = AESUtil.base64DecodeToByte(base64EncodeToString);
        log.info("密文--Base64处理字符串转换为字节数组:{}", base64DecodeToByte);

        byte[] decryptParseHexStrToByte = AESUtil.decrypt(parseHexStrToByte);
        log.info("解密后的明文--16进制转换成字节数组:{}", decryptParseHexStrToByte);
        byte[] decryptBase64DecodeToByte = AESUtil.decrypt(base64DecodeToByte);
        log.info("解密后的明文--Base64处理字符串转换为字节数组:{}", decryptBase64DecodeToByte);
        String decryptParseHexStrToByteString = new String(decryptParseHexStrToByte, StandardCharsets.UTF_8);
        log.info("明文--字节数组转换成16进制:{}", decryptParseHexStrToByteString);
        String decryptBase64DecodeToByteString = new String(decryptBase64DecodeToByte, StandardCharsets.UTF_8);
        log.info("明文--Base64处理字节数组转换为字符串:{}", decryptBase64DecodeToByteString);
    }

    @Test
    public void test03() {
        String content = "test"; //明文内容
        log.info("明文:{}", content);
        String rsaPublicKey = RSAUtil.getRSAPublicKey();
        log.info("rsaPublicKey:{}", rsaPublicKey);
        String rsaPrivateKey = RSAUtil.getRSAPrivateKey();
        log.info("rsaPrivateKey:{}", rsaPrivateKey);
        System.out.println("RSA加密==========");
        // RSA加密
        byte[] encrypt = RSAUtil.publicEncrypt(content);
        log.info("密文:{}", encrypt);

        String parseByteToHexStr = RSAUtil.parseByteToHexStr(encrypt);
        log.info("密文--字节数组转换成16进制:{}", parseByteToHexStr);
        String base64EncodeToString = RSAUtil.base64EncodeToString(encrypt);
        log.info("密文--Base64处理字节数组转为字符串:{}", base64EncodeToString);

        System.out.println("RSA解密==========");
        // RSA解密
        byte[] parseHexStrToByte = RSAUtil.parseHexStrToByte(parseByteToHexStr);
        log.info("密文--16进制转换成字节数组:{}", parseHexStrToByte);
        byte[] base64DecodeToByte = RSAUtil.base64DecodeToByte(base64EncodeToString);
        log.info("密文--Base64处理字符串转换为字节数组:{}", base64DecodeToByte);

        byte[] decryptParseHexStrToByte = RSAUtil.privateDecrypt(parseHexStrToByte);
        log.info("解密后的明文--16进制转换成字节数组:{}", decryptParseHexStrToByte);
        byte[] decryptBase64DecodeToByte = RSAUtil.privateDecrypt(base64DecodeToByte);
        log.info("解密后的明文--Base64处理字符串转换为字节数组:{}", decryptBase64DecodeToByte);
        String decryptParseHexStrToByteString = new String(decryptParseHexStrToByte, StandardCharsets.UTF_8);
        log.info("明文--字节数组转换成16进制:{}", decryptParseHexStrToByteString);
        String decryptBase64DecodeToByteString = new String(decryptBase64DecodeToByte, StandardCharsets.UTF_8);
        log.info("明文--Base64处理字节数组转换为字符串:{}", decryptBase64DecodeToByteString);
    }

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
