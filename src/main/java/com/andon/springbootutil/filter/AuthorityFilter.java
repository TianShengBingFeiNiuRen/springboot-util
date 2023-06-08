package com.andon.springbootutil.filter;

import com.andon.springbootutil.config.properties.TokenProperties;
import com.andon.springbootutil.config.properties.UrlWhiteListProperties;
import com.andon.springbootutil.response.CommonResponse;
import com.andon.springbootutil.util.JWTUtil;
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
 * 2023/5/25
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorityFilter extends OncePerRequestFilter {

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final UrlWhiteListProperties urlWhiteListProperties;
    private final TokenProperties tokenProperties;
    //    private final UserManagementMapper userManagementMapper;
    private final MappingJackson2HttpMessageConverter messageConverter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(tokenProperties.getHeaderKey());
        String currentUserId = token == null ? null : JWTUtil.getUserId(token, tokenProperties.getSecretKey());
        log.info("currentUserId:{} RemoteAddr:{} Method:{} RequestURI:{}", currentUserId, request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        filterChain.doFilter(request, response);

//        // 访问路径是否白名单
//        boolean isWhiteUrl = checkWhiteUrl(request);
//        // 解析token
//        String token = request.getHeader(tokenProperties.getHeaderKey());
//        if (isWhiteUrl) {
//            filterChain.doFilter(request, response);
//        } else {
//            String currentUserId = token == null ? null : JWTUtil.getUserId(token, tokenProperties.getSecretKey());
//            // 是否当前登录token
//            String currentToken = currentUserId == null ? null : userManagementMapper.selectUserCurrentTokenWhereUserId(currentUserId);
//            // 访问路径是否有权限
//            boolean allowAccess = currentUserId != null && currentToken != null && checkAllowAccess(currentUserId, request);
//            if (token == null || !token.equals(currentToken)) {
//                invalidToken(response);
//            } else if (!allowAccess) {
//                unAccess(currentUserId, request, response);
//            } else {
//                filterChain.doFilter(request, response);
//            }
//        }
    }

    /**
     * 访问路径是否白名单
     */
    private boolean checkWhiteUrl(HttpServletRequest request) {
        for (String whiteUrl : urlWhiteListProperties.getUrls()) {
            if (antPathMatcher.match(whiteUrl, request.getRequestURI())) {
                log.info("RemoteAddr:{} Method:{} RequestURI:{}", request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
                return true;
            }
        }
        return false;
    }

    /**
     * 访问路径是否有权限
     */
    private boolean checkAllowAccess(String currentUserId, HttpServletRequest request) {
//        List<UserPermissionDTO> userPermissionDTOS = userManagementMapper.selectUserPermissionDTOWhereUserId(currentUserId);
//        List<String> allowableUrls = userPermissionDTOS.stream().map(UserPermissionDTO::getUrl).filter(url -> !ObjectUtils.isEmpty(url)).collect(Collectors.toList());
//        for (String allowableUrl : allowableUrls) {
//            if (antPathMatcher.match(allowableUrl, request.getRequestURI())) {
//                log.info("currentUserId:{} RemoteAddr:{} Method:{} RequestURI:{}", currentUserId, request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
//                return true;
//            }
//        }
        return false;
    }

    /**
     * 无效token
     */
    private void invalidToken(HttpServletResponse response) throws IOException {
        ServletServerHttpResponse serverHttpResponse = new ServletServerHttpResponse(response);
        serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        CommonResponse<Object> build = CommonResponse.builder().code(HttpStatus.UNAUTHORIZED.value()).message("无效token").build();
        messageConverter.write(build, MediaType.APPLICATION_JSON, serverHttpResponse);
    }

    /**
     * 无权访问
     */
    private void unAccess(String currentUserId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.warn("currentUserId:{} - unAccess - RemoteAddr:{} Method:{} RequestURI:{}", currentUserId, request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        ServletServerHttpResponse serverHttpResponse = new ServletServerHttpResponse(response);
        serverHttpResponse.setStatusCode(HttpStatus.FORBIDDEN);
        CommonResponse<Object> build = CommonResponse.builder().code(HttpStatus.FORBIDDEN.value()).message(request.getRequestURI() + "，无权访问").build();
        messageConverter.write(build, MediaType.APPLICATION_JSON, serverHttpResponse);
    }
}
