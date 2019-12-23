package com.example.springbootws;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.web.bind.annotation.RestController;

import io.rsocket.RSocket;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

/**
 * @author Catfish
 * @version V1.0 2019/12/20 10:56
 * @email catfish_lty@qq.com
 */
@RestController
@MessageMapping("biz")
@Slf4j
public class MsgController {
    @MessageMapping("send")
    public Mono<MsgResponseVO> requestAndResponse(MsgRequestVO requestVO) {
        log.info("send: msg={},sendAt={}", requestVO.getMsg(), requestVO.getSendAt());
        MsgResponseVO responseVO = new MsgResponseVO();
        responseVO.setMsg(requestVO.getMsg().toUpperCase());
        return Mono.just(responseVO);
    }

    @MessageMapping("close")
    public Mono<MsgResponseVO> requestAndResponseClose(MsgRequestVO requestVO) {
        log.info("close: msg={},sendAt={}", requestVO.getMsg(), requestVO.getSendAt());
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
        return Flux.empty();
    }

    @MessageMapping("chan")
    public Flux<MsgResponseVO> requestChannel(Flux<MsgRequestVO> payloads) {
        payloads.subscribe(requestVO -> log.info("chan rec: {}", requestVO));
        return Flux.create(sink -> {
            for (int i = 0; i < 5; i++) {
                MsgResponseVO responseVO = new MsgResponseVO();
                responseVO.setMsg(String.valueOf(System.currentTimeMillis()));
                sink.next(responseVO);
            }
            sink.complete();
            for (int i = 0; i < 5; i++) {
                MsgResponseVO responseVO = new MsgResponseVO();
                responseVO.setMsg(String.valueOf(System.currentTimeMillis()));
                sink.next(responseVO);
            }
            sink.complete();
        });
    }


    @MessageExceptionHandler
    public Mono<String> handleException(Exception e) {
        return Mono.just(e.getMessage());
    }
}
