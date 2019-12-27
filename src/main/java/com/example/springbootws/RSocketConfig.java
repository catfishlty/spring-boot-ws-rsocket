package com.example.springbootws;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.DefaultMetadataExtractor;
import org.springframework.messaging.rsocket.MetadataExtractor;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Catfish
 * @version V1.0 2019/12/20 11:06
 * @email catfish_lty@qq.com
 */
@Configuration
@Slf4j
public class RSocketConfig {

    //    @Bean
//    ServerRSocketFactoryProcessor springSecurityRSocketSecurity(SessionManager sessionManager) {
//        return new ServerRSocketFactoryProcessor() {
//            @Override
//            public RSocketFactory.ServerRSocketFactory process(RSocketFactory.ServerRSocketFactory factory) {
//                factory.acceptor(new ConnectManager(sessionManager));
//                return
//            }
//        };
//    }
//    @Bean
//    public RSocketStrategies rSocketStrategies(ObjectProvider<RSocketStrategiesCustomizer> customizers) {
//        RSocketStrategies.Builder builder = RSocketStrategies.builder();
//        builder.routeMatcher(new PathPatternRouteMatcher());
//        MetadataExtractor extractor = new DefaultMetadataExtractor(builder.));
//        builder.metadataExtractor(MetadataExtractor)
//        customizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
//        return builder.build();
//    }
}
