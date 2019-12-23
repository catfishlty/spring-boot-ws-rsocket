package com.example.springbootws.session;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.example.springbootws.session.dto.SessionData;

import io.rsocket.RSocket;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author Catfish
 * @version V1.0 2019/12/23 10:25
 * @email catfish_lty@qq.com
 */
@Component
public class SessionManager {
    private final ConcurrentHashMap<String, SessionData> sessionMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> userMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> userRMap = new ConcurrentHashMap<>();

    public void connect(String connId, RSocket rSocket) {
        this.sessionMap.put(connId, SessionData.create(connId, rSocket));
    }

    public Mono<Void> disconnect(final String connId) {
        return Mono.just(connId)
            .doOnNext(connId1 -> {
                SessionData data = sessionMap.get(connId1);
                data.getRSocket().dispose();
                String userId = userRMap.get(connId1);
                if (Objects.nonNull(userId)) {
                    userMap.remove(userId);
                }
                userRMap.remove(connId1);
            })
            .subscribeOn(Schedulers.elastic())
            .then();
    }

    public void login(final String userId, final String connId) {
        userMap.put(userId, connId);
        userRMap.put(connId, userId);
    }

    public Mono<Void> logout(final String userId) {
        return Mono.just(userId)
            .flatMap((Function<String, Mono<String>>) s -> {
                String connId = userMap.get(userId);
                userMap.remove(userId);
                userRMap.remove(connId);
                return Mono.just(connId);
            })
            .flatMap((Function<String, Mono<Void>>) this::disconnect)
            .then();
    }
}
