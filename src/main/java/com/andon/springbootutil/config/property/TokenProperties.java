package com.andon.springbootutil.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Andon
 * 2023/3/17
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
}
