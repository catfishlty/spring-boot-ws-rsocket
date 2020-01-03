package com.example.springbootws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springbootws.utils.JwtUtil;

/**
 * @author Catfish
 * @version V1.0 2019/7/25 15:57
 * @email catfish_lty@qq.com
 */
@Configuration
public class JwtConfig {
    /**
     * 秘钥
     */
    @Value("${jwt.secret}")
    private String secret;
    /**
     * 过期时间
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(secret, expiration);
    }
}
