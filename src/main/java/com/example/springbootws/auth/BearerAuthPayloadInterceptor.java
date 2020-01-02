package com.example.springbootws.auth;

import org.springframework.core.Ordered;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.rsocket.api.PayloadExchange;
import org.springframework.security.rsocket.api.PayloadInterceptor;
import org.springframework.security.rsocket.api.PayloadInterceptorChain;
import org.springframework.security.rsocket.authentication.BasicAuthenticationPayloadExchangeConverter;
import org.springframework.security.rsocket.authentication.PayloadExchangeAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import reactor.core.publisher.Mono;

/**
 * @author Catfish
 * @version V1.0 2020/1/2 11:49
 * @email catfish_lty@qq.com
 */
public class BearerAuthPayloadInterceptor implements PayloadInterceptor, Ordered {
    private final ReactiveAuthenticationManager authenticationManager;

    private int order;

    private PayloadExchangeAuthenticationConverter authenticationConverter =
        new BasicAuthenticationPayloadExchangeConverter();

    public BearerAuthPayloadInterceptor(ReactiveAuthenticationManager manager) {
        this.authenticationManager = manager;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * Sets the convert to be used
     * @param authenticationConverter
     */
    public void setAuthenticationConverter(
        PayloadExchangeAuthenticationConverter authenticationConverter) {
        Assert.notNull(authenticationConverter, "authenticationConverter cannot be null");
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public Mono<Void> intercept(PayloadExchange exchange, PayloadInterceptorChain chain) {
        return this.authenticationConverter.convert(exchange)
            .switchIfEmpty(chain.next(exchange).then(Mono.empty()))
            .flatMap(this.authenticationManager::authenticate)
            .flatMap(a -> onAuthenticationSuccess(chain.next(exchange), a));
    }

    private Mono<Void> onAuthenticationSuccess(Mono<Void> payload, Authentication authentication) {
        return payload
            .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }
}