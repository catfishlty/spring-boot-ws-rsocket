package com.example.springbootws.auth;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Catfish
 * @version V1.0 2020/1/2 17:35
 * @email catfish_lty@qq.com
 */
public class BearerAuthToken extends AbstractAuthenticationToken {
    /**
     * Creates a token with the supplied array of authorities.
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     * represented by this authentication object.
     */
    private String token;

    public BearerAuthToken(String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
    }

    public BearerAuthToken(String token) {
        super(Collections.emptySet());
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        token = null;
    }

    public String getToken(){
        return token;
    }
}
