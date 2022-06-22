package com.andon.springbootutil;

import com.andon.springbootutil.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author Andon
 * 2022/6/22
 */
@Slf4j
public class RSATest {

    @Test
    public void test01() {
        RSAUtil.RsaKeyPair rsaKeyPair = RSAUtil.generateKeyPair(null);
        log.info("publicKey:{}", rsaKeyPair.publicKey);
        log.info("privateKey:{}", rsaKeyPair.privateKey);

        String data = "hello world";
//        String data = rsaKeyPair.publicKey;
        String encrypt = RSAUtil.publicEncrypt(data, rsaKeyPair.publicKey);
        String decrypt = RSAUtil.privateDecrypt(encrypt, rsaKeyPair.privateKey);
        log.info("data:{}", data);
        log.info("encrypt:{}", encrypt);
        log.info("decrypt:{}", decrypt);
    }
}
