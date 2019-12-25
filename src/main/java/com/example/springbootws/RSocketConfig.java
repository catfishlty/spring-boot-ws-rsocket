package com.example.springbootws;

import java.net.URI;

import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;

import com.example.springbootws.session.ConnectManager;
import com.example.springbootws.session.SessionManager;

import io.rsocket.SocketAcceptor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author Catfish
 * @version V1.0 2019/12/20 11:06
 * @email catfish_lty@qq.com
 */
@Configuration
@Slf4j
public class RSocketConfig {
    @Bean
    public SocketAcceptor socketAcceptor(SessionManager sessionManager) {
        return new ConnectManager(sessionManager);
    }
}
