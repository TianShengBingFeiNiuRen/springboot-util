package com.andon.springbootutil.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Andon
 * 2022/11/18
 */
@Data
@Component
@ConfigurationProperties(prefix = "url.white")
public class UrlWhiteListProperties {

    /**
     * URL 白名单
     */
    private List<String> urls;
}
