package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.*;
import com.andon.springbootutil.vo.TestSwaggerTestResp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Andon
 * 2021/11/10
 */
@Slf4j
public class Demo {

    @Test
    public void test05() {
        TestSwaggerTestResp param1 = TestSwaggerTestResp.builder().param1("param1").build();
        TestSwaggerTestResp param2 = DeepCopyUtil.deepCopy(param1);
        System.out.println(param1==param2);
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
