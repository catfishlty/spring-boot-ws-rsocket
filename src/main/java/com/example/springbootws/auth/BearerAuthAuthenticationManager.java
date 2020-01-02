package com.example.springbootws.auth;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;

import reactor.core.publisher.Mono;

/**
 * @author Catfish
 * @version V1.0 2020/1/2 17:52
 * @email catfish_lty@qq.com
 */
public class BearerAuthAuthenticationManager implements ReactiveAuthenticationManager {
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        authentication.get
        return null;
    }
}
