package com.example.springbootws.session;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.springbootws.session.dto.SessionData;

import io.rsocket.RSocket;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Catfish
 * @version V1.0 2019/12/23 10:25
 * @email catfish_lty@qq.com
 */
@Component
@Slf4j
public class ConnectManager {
    private final ConcurrentHashMap<String, SessionData> connectMap = new ConcurrentHashMap<>();

    public void connect(String connId, RSocket rSocket) {
        this.connectMap.put(connId, SessionData.create(connId, rSocket));
    }

    public void disconnect(String connId) {
        this.connectMap.remove(connId);
    }

    public SessionData get(String connId) {
        return connectMap.get(connId);
    }

    public int online() {
        return connectMap.size();
    }
}
