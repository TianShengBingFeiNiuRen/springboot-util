package com.andon.springbootutil.filter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Andon
 * 2021/11/10
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "securityFilter")
public class SecurityFilter implements Filter {

    @Value("${external-interface.url_remote_address_info}")
    private static String url_remote_address_info;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String xRealIP = httpServletRequest.getHeader("X-Real-IP");
        String location = queryLocation(xRealIP);
        log.info("X-Real-IP:{} location:{} remoteHost:{} method:{} uri:{}", xRealIP, location, httpServletRequest.getRemoteHost(), httpServletRequest.getMethod(), httpServletRequest.getRequestURI());
        if (httpServletRequest.getRequestURI().equals("/filter")) {
            httpServletRequest.setAttribute("code", 403);
            httpServletRequest.setAttribute("message", "没有权限!!");
            httpServletRequest.setAttribute("uri", httpServletRequest.getRequestURI());
            // 请求转发到自定义403接口处理响应
            httpServletRequest.getRequestDispatcher("/403").forward(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }

    public static String queryLocation(String ip) {
        String location = ip;
        try {
            Map<String, String> param = new HashMap<>();
            param.put("query", ip);
            param.put("resource_id", "6006");
            String response = HttpClientUtil.doGet(url_remote_address_info, param);
            if (!ObjectUtils.isEmpty(response)) {
                JSONObject jsonObject = JSONObject.parseObject(response);
                JSONArray data = jsonObject.getJSONArray("data");
                if (!ObjectUtils.isEmpty(data)) {
                    JSONObject dataJSONObject = data.getJSONObject(0);
                    location = dataJSONObject.getString("location");
                }
            }
        } catch (Exception e) {
            log.error("queryLocation failure!! error={}", e.getMessage());
        }
        return location;
    }
}
