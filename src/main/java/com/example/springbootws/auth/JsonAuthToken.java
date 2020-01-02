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
public class JsonAuthToken extends AbstractAuthenticationToken {
    /**
     * Creates a token with the supplied array of authorities.
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     * represented by this authentication object.
     */
    private final Object principal;
    private Object credentials;

    public JsonAuthToken(String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.setAuthenticated(true);
        principal = token;
        credentials = token;
    }

    public JsonAuthToken(String token) {
        super(Collections.emptySet());
        this.setAuthenticated(true);
        principal = token;
        credentials = token;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }
}
