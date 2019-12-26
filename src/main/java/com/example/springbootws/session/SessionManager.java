package com.example.springbootws.session;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.springbootws.session.dto.SessionData;

import io.rsocket.RSocket;

/**
 * @author Catfish
 * @version V1.0 2019/12/23 10:25
 * @email catfish_lty@qq.com
 */
@Component
public class SessionManager {
    private final ConcurrentHashMap<String, String> userMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> userRMap = new ConcurrentHashMap<>();
    private final ConnectManager connectManager;

    public SessionManager(ConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    public void login(final String userId, final String connId) {
        userMap.put(userId, connId);
        userRMap.put(connId, userId);
    }

    public void logout(final String userId) {
        String connId = userMap.get(userId);
        userMap.remove(userId);
        userRMap.remove(connId);
    }

    public SessionData get(String userId) {
        String connId = userMap.get(userId);
        if (StringUtils.isBlank(connId)) {
            return null;
        }
        return connectManager.get(connId);
    }
}
