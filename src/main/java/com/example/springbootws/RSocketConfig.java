package com.example.springbootws;

import java.net.URI;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;

import reactor.core.publisher.Mono;

/**
 * @author Catfish
 * @version V1.0 2019/12/20 11:06
 * @email catfish_lty@qq.com
 */
@Configuration
public class RSocketConfig {

    @Bean
    public Mono<RSocketRequester> rSocketRequester(
        RSocketStrategies rSocketStrategies,
        RSocketProperties rSocketProps) {
        return RSocketRequester.builder()
            .rsocketStrategies(rSocketStrategies)
            .connectWebSocket(getURI(rSocketProps));
    }
    private URI getURI(RSocketProperties rSocketProps) {
        return URI.create(String.format("ws://127.0.0.1:%d%s",
            rSocketProps.getServer().getPort(), rSocketProps.getServer().getMappingPath()));
    }
}
