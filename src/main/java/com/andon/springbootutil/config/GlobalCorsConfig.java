package com.andon.springbootutil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Andon
 * 2022/8/5
 * <p>
 * 全局跨域
 */
@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 添加CORS配置信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 放行哪些原始域
        corsConfiguration.addAllowedOriginPattern("*");
        // 是否发送Cookie
        corsConfiguration.setAllowCredentials(true);
        // 放行哪些请求方式
        corsConfiguration.addAllowedMethod("*");
        // 放行哪些原始请求头部信息
        corsConfiguration.addAllowedHeader("*");
        // 暴露哪些头部信息
        corsConfiguration.addExposedHeader("*");
        // 添加映射路径
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        // 返回新的CorsFilter
        return new CorsFilter(corsConfigurationSource);
    }
}
