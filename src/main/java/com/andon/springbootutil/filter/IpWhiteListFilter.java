package com.andon.springbootutil.filter;

import com.andon.springbootutil.config.IpWhiteListProperties;
import com.andon.springbootutil.config.UrlWhiteListProperties;
import com.andon.springbootutil.domain.ResponseStandard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Andon
 * 2022/11/16
 */
@SuppressWarnings("NullableProblems")
@Slf4j
@Component
@RequiredArgsConstructor
public class IpWhiteListFilter extends OncePerRequestFilter {

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final IpWhiteListProperties ipWhiteListProperties;
    private final UrlWhiteListProperties urlWhiteListProperties;
    private final MappingJackson2HttpMessageConverter messageConverter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // url白名单
        boolean isWhiteUrl = false;
        for (String whiteUrl : urlWhiteListProperties.getUrls()) {
            if (antPathMatcher.match(whiteUrl, request.getRequestURI())) {
                log.info("RemoteAddr:{} Method:{} RequestURI:{}", request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
                isWhiteUrl = true;
                break;
            }
        }
        // ip白名单
        if (ipWhiteListProperties.isOpen() && !isWhiteUrl && !ipWhiteListProperties.getIps().contains(request.getRemoteAddr())) {
            log.warn("RemoteAddr:{} Method:{} RequestURI:{} ipWhiteListProperties getIps:{}", request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), ipWhiteListProperties.getIps());
            // 非IP白名单，无权访问
            unWhiteIpAccess(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 非IP白名单，无权访问
     */
    private void unWhiteIpAccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletServerHttpResponse serverHttpResponse = new ServletServerHttpResponse(response);
        serverHttpResponse.setStatusCode(HttpStatus.FORBIDDEN);
        ResponseStandard<Object> build = ResponseStandard.builder().code(HttpStatus.FORBIDDEN.value()).message(request.getRemoteAddr() + "非IP白名单，无权访问").build();
        messageConverter.write(build, MediaType.APPLICATION_JSON, serverHttpResponse);
    }
}
