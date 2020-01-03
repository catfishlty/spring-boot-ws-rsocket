package com.example.springbootws.auth;

import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import lombok.Getter;

/**
 * @author Catfish
 * @version V1.0 2020/1/2 17:35
 * @email catfish_lty@qq.com
 */
public class JwtAuthToken extends AbstractAuthenticationToken {
    @Getter
    private String uid;
    @Getter
    private Long expire;

    public JwtAuthToken(String uid, Long expire) {
        super(Collections.emptySet());
        this.setAuthenticated(true);
        this.uid = uid;
        this.expire = expire;
    }

    @Override
    public Object getCredentials() {
        return getUid();
    }

    @Override
    public Object getPrincipal() {
        return getUid();
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        uid = null;
    }
}
