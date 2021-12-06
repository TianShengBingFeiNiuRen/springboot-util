package com.andon.springbootutil.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Andon
 * 2021/11/10
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "securityFilter")
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        log.info("remoteHost:{} method:{} uri:{}", httpServletRequest.getRemoteHost(), httpServletRequest.getMethod(), httpServletRequest.getRequestURI());
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
}
