package com.example.springbootws.session;

import java.util.Objects;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.example.springbootws.session.dto.ConnectionDataDTO;
import com.example.springbootws.utils.JacksonUtil;

import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.core.scheduler.Schedulers;

/**
 * @author Catfish
 * @version V1.0 2019/12/23 10:25
 * @email catfish_lty@qq.com
 */
@Slf4j
public class ConnectManager implements SocketAcceptor {
    private final SessionManager sessionManager;

    public ConnectManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {
        ConnectionDataDTO dataDTO = JacksonUtil.jsonToObject(setup.getDataUtf8(), ConnectionDataDTO.class);
        log.info("accept {}", dataDTO);
        if (Objects.isNull(dataDTO) || StringUtils.isBlank(dataDTO.getId())) {
            log.error("Connection Id is null");
            return Mono.empty();
        }
        sendingSocket.onClose()
            .onErrorResume(e -> Mono.empty())
            .then(sessionManager.disconnect(dataDTO.getId()))
            .then(Mono.create(monoSink -> {
                log.info("socket close");
            }))
            .subscribeOn(Schedulers.elastic())
            .subscribe();
        sessionManager.connect(dataDTO.getId(), sendingSocket);
        return Mono.just(sendingSocket);
        //TODO SetTimeout to disconnect client;
    }
}
