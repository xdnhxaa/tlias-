package com.tlias.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类：生成令牌 / 解析令牌
 */
public class JwtUtils {

    /** 签名密钥 */
    private static String signKey = "SVRIRUlNQQ==";

    /** 过期时间：12 小时 */
    private static Long expire = 43200000L;

    public static void setSignKey(String key) {
        signKey = key;
    }

    public static void setExpire(Long milliseconds) {
        expire = milliseconds;
    }

    /** 生成 JWT 令牌 */
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
    }

    /** 解析 JWT 令牌，返回负载内容；解析失败会抛异常 */
    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
