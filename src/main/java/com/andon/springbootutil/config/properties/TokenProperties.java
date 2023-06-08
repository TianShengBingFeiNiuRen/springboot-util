package com.andon.springbootutil.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Andon
 * 2023/5/17
 */
@Data
@Component
@ConfigurationProperties(prefix = "token")
public class TokenProperties {

    /**
     * token是否开启
     */
    private boolean open;

    /**
     * token headerKey
     */
    private String headerKey;

    /**
     * 签名密钥
     */
    private String secretKey;

    /**
     * 过期时间（小时）
     */
    private Long tokenExpirationHour;
}
