package com.andon.springbootutil;

import com.andon.springbootutil.system.RsaKey;
import com.andon.springbootutil.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Andon
 * 2022/6/22
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RsaKeyTest {

    @Test
    public void test01(){
        String data = "hello world";
        String encrypt = RSAUtil.publicEncrypt(data, RsaKey.rsaPublicKey);
        String decrypt = RSAUtil.privateDecrypt(encrypt, RsaKey.rsaPrivateKey);
        log.info("data:{}", data);
        log.info("encrypt:{}", encrypt);
        log.info("decrypt:{}", decrypt);
    }
}
