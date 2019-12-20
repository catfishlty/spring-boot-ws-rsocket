package com.example.springbootws;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Catfish
 * @version V1.0 2019/12/20 10:56
 * @email catfish_lty@qq.com
 */
@Controller
@Slf4j
public class MsgController {
    @MessageMapping("rar")
    public Mono<MsgResponseVO> requestAndResponse(MsgRequestVO requestVO) {
        log.info("request and response: msg={},sendAt={}", requestVO.getMsg(), requestVO.getSendAt());
        MsgResponseVO responseVO = new MsgResponseVO();
        responseVO.setMsg(requestVO.getMsg().toUpperCase());
        return Mono.just(responseVO);
    }

    @MessageMapping("faf")
    public Mono<Void> fireAndForget(MsgRequestVO requestVO) {
        log.info("fire and forget: msg={},sendAt={}", requestVO.getMsg(), requestVO.getSendAt());
        return Mono.empty();
    }

    @MessageMapping("stream")
    public Flux<MsgResponseVO> requestStream(MsgRequestVO requestVO) {
        AtomicInteger counter = new AtomicInteger(0);
        return Flux.fromStream(Stream.generate(
            () -> {
                MsgResponseVO vo = new MsgResponseVO();
                vo.setMsg(requestVO.getMsg() + " - " + counter.getAndIncrement());
                return vo;
            }
        ).limit(10)).delayElements(Duration.ofSeconds(1));
    }

    @MessageMapping("chan")
    public Flux<MsgResponseVO> requestChannel(Flux<MsgRequestVO> payloads) {
        payloads.subscribe(requestVO -> log.info("chan rec: {}", requestVO));
        return Flux.fromStream(() -> {
            MsgResponseVO vo = new MsgResponseVO();
            vo.setMsg("chan response " + System.currentTimeMillis());
            return Stream.of(vo);
        });
    }


    @MessageExceptionHandler
    public Mono<String> handleException(Exception e) {
        return Mono.just(e.getMessage());
    }
}
