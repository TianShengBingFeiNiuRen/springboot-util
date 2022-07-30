package com.andon.springbootutil.system;

import com.andon.springbootutil.util.FileUtil;
import com.andon.springbootutil.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.Collections;

/**
 * @author Andon
 * 2022/6/22
 */
@Slf4j
@Service
public class RsaKey {

    private final String[] rsaPublicKeyFileName = new String[]{"conf", "rsaPublicKey.txt"};
    private final String[] rsaPrivateKeyFileName = new String[]{"conf", "rsaPrivateKey.txt"};

    public static String rsaPublicKey;
    public static String rsaPrivateKey;

    @PostConstruct
    public void init() throws Exception {
        Path rsaPublicKeyPath = FileUtil.getRootFilePath(rsaPublicKeyFileName);
        Path rsaPrivateKeyPath = FileUtil.getRootFilePath(rsaPrivateKeyFileName);
        rsaPublicKey = FileUtil.readFirstLine(rsaPublicKeyPath.toString());
        rsaPrivateKey = FileUtil.readFirstLine(rsaPrivateKeyPath.toString());
        if (ObjectUtils.isEmpty(rsaPublicKey) || ObjectUtils.isEmpty(rsaPublicKey)) {
            RSAUtil.RsaKeyPair rsaKeyPair = RSAUtil.generateKeyPair(null);
            rsaPublicKey = rsaKeyPair.publicKey;
            rsaPrivateKey = rsaKeyPair.privateKey;
            FileUtil.createFileWithContent(Collections.singletonList(rsaPublicKey), rsaPublicKeyFileName);
            FileUtil.createFileWithContent(Collections.singletonList(rsaPrivateKey), rsaPrivateKeyFileName);
        }
        log.info("rsaPublicKey:{}", rsaPublicKey);
        log.info("rsaPrivateKey:{}", rsaPrivateKey);
    }
}
