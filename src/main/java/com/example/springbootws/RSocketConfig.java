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
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.rsocket.api.PayloadExchange;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.security.rsocket.util.matcher.PayloadExchangeMatcher;
import org.springframework.util.ClassUtils;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

import com.example.springbootws.auth.BearerAuthAuthenticationManager;
import com.example.springbootws.auth.BearerAuthPayloadExchangeConverter;
import com.example.springbootws.auth.BearerAuthPayloadInterceptor;
import com.example.springbootws.common.JsonMetadataExtractor;
import com.example.springbootws.utils.JwtUtil;
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
    public ReactiveAuthenticationManager reactiveAuthenticationManager(JwtUtil jwtUtil) {
        return new BearerAuthAuthenticationManager(jwtUtil);
    }

    @Bean
    public BearerAuthPayloadInterceptor bearerAuthPayloadInterceptor(ReactiveAuthenticationManager authenticationManager) {
        BearerAuthPayloadInterceptor interceptor = new BearerAuthPayloadInterceptor(authenticationManager);
        interceptor.setOrder(0);
        interceptor.setAuthenticationConverter(new BearerAuthPayloadExchangeConverter());
        return interceptor;
    }

//    @Bean
//    public MapReactiveUserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//            .username("user")
//            .password("password")
//            .roles("USER")
//            .build();
//        return new MapReactiveUserDetailsService(user);
//    }


    @Bean
    public PayloadSocketAcceptorInterceptor rSocketInterceptor(RSocketSecurity rSocket, BearerAuthPayloadInterceptor interceptor) {
        rSocket
            .addPayloadInterceptor(interceptor)
            .authorizePayload(authorize -> {
                authorize
                    .matcher(new PayloadExchangeMatcher() {
                        @Override
                        public Mono<MatchResult> matches(PayloadExchange exchange) {
                            return !exchange.getType().isRequest() ? MatchResult.match() : MatchResult.notMatch();
                        }
                    })
                    .permitAll()
                    .route("auth.**")
                    .permitAll()
                    .anyExchange().authenticated()
                ;
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
