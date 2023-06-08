package com.andon.springbootutil.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * JWTUtil
 */
public class JWTUtil {

    public static String generateToken(String userId, String secretKey, Long tokenExpirationHour) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId); //存放自定义数据
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(userId) //设置用户ID
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationHour * 60 * 60 * 1000)) //设置token过期时间
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256).compact(); //设置token签名算法及密钥
    }

    public static String getUserId(String token, String secretKey) {
        Claims claims;
        try {
            // 解析token
            claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean tokenResolve(String token, String secretKey) {
        try {
            // 解析token
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
            claims.get("userId", String.class); //解析存放的自定义数据
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
