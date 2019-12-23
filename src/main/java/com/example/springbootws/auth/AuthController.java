package com.example.springbootws.auth;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootws.auth.req.LoginRequestVO;
import com.example.springbootws.auth.resp.LoginResponseVO;
import com.example.springbootws.session.SessionManager;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author Catfish
 * @version V1.0 2019/12/23 16:18
 * @email catfish_lty@qq.com
 */
@RestController
@MessageMapping("auth")
@Slf4j
public class AuthController {
    @Autowired
    private SessionManager sessionManager;

    @MessageMapping("login")
    public Mono<LoginResponseVO> login(Mono<LoginRequestVO> requestVO) {
        requestVO.subscribe(loginRequestVO -> log.info("{}", loginRequestVO));
        return Mono.empty();
    }
}
