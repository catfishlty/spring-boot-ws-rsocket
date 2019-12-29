package com.example.springbootws;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;

import com.example.springbootws.session.ConnectManager;
import com.example.springbootws.utils.JacksonUtil;

import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;
import reactor.core.scheduler.Schedulers;

/**
 * @author Catfish
 * @version V1.0 2019/12/25 3:06
 * @email catfish_lty@qq.com
 **/
@Controller
@Slf4j
public class MainController {
    private final ConnectManager connectManager;

    public MainController(ConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    @ConnectMapping()
    public Mono<Void> connect(@Headers Map<String, Object> m, RSocketRequester requester) {
        String connId = String.valueOf(requester.rsocket().hashCode());
        log.info("connect: {}", requester.rsocket().hashCode());
        connectManager.connect(connId, requester.rsocket());
        requester.rsocket().onClose()
            .onErrorResume(e -> Mono.empty()
                .doOnNext(o -> log.error("onClose Error", e))
                .subscribeOn(Schedulers.elastic())
                .then()
            )
            .then(
                Mono.just(connId)
                    .doOnNext(s -> log.info("disconnect: {}", s))
                    .doOnNext(connectManager::disconnect)
                    .subscribeOn(Schedulers.elastic())
                    .then()
            )
            .subscribeOn(Schedulers.single())
            .subscribe();
        return Mono.empty();
    }
    @MessageMapping("")
    public Mono<MsgResponseVO> requestAndResponse(MsgRequestVO requestVO, @Headers Map<String, Object> m, RSocketRequester requester) {
        MonoProcessor processor = (MonoProcessor) m.get("rsocketResponse");
        log.info("{}",processor.currentContext().stream().collect(Collectors.toList()));
        log.info("send: id={}, msg={}, sendAt={}, header={}", requester.rsocket().hashCode(), requestVO.getMsg(), requestVO.getSendAt(), m.entrySet());
        MsgResponseVO responseVO = new MsgResponseVO();
        responseVO.setMsg(requestVO.getMsg().toUpperCase());
        log.info("online: {}", connectManager.online());
        requester.rsocket()
            .requestChannel(s -> {
                MsgResponseVO vo = new MsgResponseVO();
                vo.setMsg("hello");
                s.onNext(DefaultPayload.create(JacksonUtil.objectToJson(vo)));
            })
            .subscribeOn(Schedulers.elastic())
            .subscribe();
        return Mono.just(responseVO);
    }
}
