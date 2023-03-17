package com.andon.springbootutil.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Andon
 * 2022/11/11
 */
@Data
@Component
@ConfigurationProperties(prefix = "ip.white")
public class IpWhiteListProperties {

    /**
     * IP 白名单是否开启
     */
    private boolean open;

    /**
     * IP 白名单
     */
    private List<String> ips;
}
