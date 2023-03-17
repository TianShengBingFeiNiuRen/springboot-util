package com.andon.springbootutil.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Andon
 * 2022/4/15
 */
@Data
@Component
@PropertySource({"classpath:andon.add.properties"})
@ConfigurationProperties(prefix = "andon.add")
public class AndonAddConfig {

    private Boolean enable = Boolean.FALSE;
    private String name;
}
