package com.example.springbootws;

import java.util.Map;

import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author Catfish
 * @version V1.0 2019/12/25 3:06
 * @email catfish_lty@qq.com
 **/
@Controller
@Slf4j
public class MainController {
    @ConnectMapping()
    public Mono<Void> connect(@Headers Map<String, Object> m, RSocketRequester requester) {
        log.info("connect: {}, {}", requester.rsocket().hashCode(), m);
        requester.rsocket().onClose()
            .onErrorResume(e -> Mono.empty())
            .then(
                Mono.empty()
                    .doOnNext(o -> log.info("disconnect: {}", requester.rsocket().hashCode()))
                    .subscribeOn(Schedulers.single())
                    .then()
            )
            .subscribeOn(Schedulers.single())
            .subscribe();
        return Mono.empty();
    }
}
