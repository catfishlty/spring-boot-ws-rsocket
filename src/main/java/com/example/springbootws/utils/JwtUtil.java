package com.example.springbootws.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT工具类
 * @author Catfish
 * @version 1.0 2018-09-05 17:21:10
 * @email catfish_lty@qq.com
 **/
public class JwtUtil {
    private static final String CLAIM_KEY_UID = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    private final String secret;
    private final Long expiration;

    public JwtUtil(String secret, Long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    /**
     * 根据Token获取该用户UId
     * @param token Token
     * @return 用户名
     */
    public String getUidFromToken(String token) {
        String uid;
        try {
            final Claims claims = getClaimsFromToken(token);
            uid = claims.getSubject();
        } catch (Exception e) {
            uid = null;
        }
        return uid;
    }

    /**
     * 根据Token获取生成时间
     * @param token Token
     * @return 日期
     */
    public Date getCreatedFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * 根据Token获取过期时间
     * @param token Token
     * @return 日期
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * 根据Token获取声明
     * @param token Token
     * @return 声明
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生成过期日期
     * @return 日期
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * Token是否过期
     * @param token Token
     * @return 是否过期
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 生成Token
     * @param uid 用户ID
     * @return Token
     */
    public String generateToken(String uid) {
        Map<String, Object> claims = new HashMap<>(10);
        claims.put(CLAIM_KEY_UID, uid);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 生成TOken
     * @param claims 声明
     * @return Token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(generateExpirationDate())
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    /**
     * Token能否被刷新
     * @param token Token
     * @return 结果
     */
    public boolean canTokenBeRefreshed(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token Token
     * @return 刷新后的Token
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 校验Token
     * @param token    Token
     * @param username 用户名
     * @return 结果
     */
    public boolean validateToken(String token, String username) {
        final String openId = getUidFromToken(token);
        return (
            openId.equals(username)
                && !isTokenExpired(token)
        );
    }
}
