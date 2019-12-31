package com.example.springbootws;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.security.rsocket.core.SecuritySocketAcceptorInterceptor;
import org.springframework.util.ClassUtils;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

import com.example.springbootws.common.JsonMetadataExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author Catfish
 * @version V1.0 2019/12/20 11:06
 * @email catfish_lty@qq.com
 */
@Configuration
@Slf4j
@EnableRSocketSecurity
public class RSocketConfig {
    private static final String PATHPATTERN_ROUTEMATCHER_CLASS = "org.springframework.web.util.pattern.PathPatternRouteMatcher";

    @Bean
    public RSocketMessageHandler messageHandler(RSocketStrategies rSocketStrategies) {
        RSocketMessageHandler handler = new RSocketMessageHandler();
        handler.setRSocketStrategies(rSocketStrategies);
        return handler;
    }

    @Bean
    @ConditionalOnMissingBean
    public RSocketStrategies rSocketStrategies(ObjectProvider<RSocketStrategiesCustomizer> customizers) {
        RSocketStrategies.Builder builder = RSocketStrategies.builder();
        if (ClassUtils.isPresent(PATHPATTERN_ROUTEMATCHER_CLASS, null)) {
            builder.routeMatcher(new PathPatternRouteMatcher());
        }
        builder.metadataExtractor(new JsonMetadataExtractor());
        customizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder.build();
    }

    @Bean
    public PayloadSocketAcceptorInterceptor rSocketInterceptor(RSocketSecurity rSocket) {
        rSocket.authorizePayload(authorize -> {
            authorize
                .anyRequest().permitAll()
                .anyExchange().permitAll();
        })
            .jwt(new Customizer<RSocketSecurity.JwtSpec>() {
                @Override
                public void customize(RSocketSecurity.JwtSpec jwtSpec) {
                    jwtSpec.authenticationManager(new ReactiveAuthenticationManager() {
                        @Override
                        public Mono<Authentication> authenticate(Authentication authentication) {
                            log.info("auth {}", authentication);
                            return Mono.just(authentication);
                        }
                    });
                }
            });

        return rSocket.build();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(ObjectMapper.class)
    protected static class JacksonJsonStrategyConfiguration {

        private static final MediaType[] SUPPORTED_TYPES = {MediaType.APPLICATION_JSON,
            new MediaType("application", "*+json")};

        @Bean
        @Order(1)
        @ConditionalOnBean(ObjectMapper.class)
        public RSocketStrategiesCustomizer jacksonRSocketStrategiesCustomizer(ObjectMapper objectMapper) {
            return (strategy) -> {
                strategy.decoder(new Jackson2JsonDecoder(objectMapper, SUPPORTED_TYPES));
                strategy.encoder(new Jackson2JsonEncoder(objectMapper, SUPPORTED_TYPES));
            };
        }
    }
}
