package com.example.springbootws.auth;

import org.springframework.core.convert.converter.Converter;

import com.example.springbootws.utils.JwtUtil;

/**
 * @author Catfish
 * @version V1.0 2020/1/3 11:37
 * @email catfish_lty@qq.com
 */
public class JwtAuthConverter implements Converter<String, JwtAuthToken> {
    private JwtUtil jwtUtil;

    public JwtAuthConverter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public JwtAuthToken convert(String token) {
        return new JwtAuthToken(jwtUtil.getUidFromToken(token), jwtUtil.getExpirationDateFromToken(token).getTime());
    }
}
