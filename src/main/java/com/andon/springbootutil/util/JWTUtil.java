package com.andon.springbootutil.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * JWTUtil
 */
public class JWTUtil {

    private static final long tokenExpiration = 2 * 60 * 60 * 1000; //token过期时间(2小时)
    private static final String tokenSecret = "andon"; //token秘钥
    private static final String headerKey = "token"; //header的key

    public static Map<String, Object> tokenGenerate(String username) {
        Map<String, Object> tokenAndExpirationMap = new HashMap<>();
        String token;
        Claims claims = Jwts.claims();
        claims.put("role", new ArrayList<>()); //存放自定义数据
        long timestamp_expiration = System.currentTimeMillis() + tokenExpiration;
        token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username) //设置用户名
                .setExpiration(new Date(timestamp_expiration)) //设置token过期时间
                .signWith(SignatureAlgorithm.HS512, tokenSecret).compact(); //设置token签名算法及秘钥
        tokenAndExpirationMap.put("token", token);
        tokenAndExpirationMap.put("timestamp_expiration", String.valueOf(timestamp_expiration));
        return tokenAndExpirationMap;
    }

    public static String tokenResolveUsername(String token) {
        Claims claims;
        try {
            // 解析token
            claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token.replace(tokenSecret + " ", "")).getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(headerKey);
        if (!ObjectUtils.isEmpty(token)) {
            token = token.replace(tokenSecret + " ", "");
        }
        return token;
    }

    public static boolean tokenResolve(String token) {
        try {
            // 解析token
            Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token.replace(tokenSecret + " ", "")).getBody();
            claims.get("role", List.class); //解析存放的自定义数据
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
