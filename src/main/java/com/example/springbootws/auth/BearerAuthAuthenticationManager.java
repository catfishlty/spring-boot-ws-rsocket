package com.example.springbootws.auth;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;

import com.example.springbootws.utils.JwtUtil;

import reactor.core.publisher.Mono;

/**
 * @author Catfish
 * @version V1.0 2020/1/2 17:52
 * @email catfish_lty@qq.com
 */
public class BearerAuthAuthenticationManager implements ReactiveAuthenticationManager {
    private JwtAuthConverter converter;

    public BearerAuthAuthenticationManager(JwtUtil jwtUtil) {
        this.converter = new JwtAuthConverter(jwtUtil);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
            .filter(a -> {
                return a instanceof BearerAuthToken;
            })
            .cast(BearerAuthToken.class)
            .map(bearerAuthToken -> {
                return bearerAuthToken.getToken();
            })
            .map(token -> {
                return converter.convert(token);
            })
            .cast(Authentication.class);
    }
}
