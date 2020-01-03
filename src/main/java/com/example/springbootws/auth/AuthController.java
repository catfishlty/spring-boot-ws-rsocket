package com.example.springbootws.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootws.auth.req.LoginRequestVO;
import com.example.springbootws.auth.resp.LoginResponseVO;
import com.example.springbootws.session.SessionManager;
import com.example.springbootws.utils.JwtUtil;

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
    @Autowired
    private JwtUtil jwtUtil;

    @MessageMapping("login")
    public Mono<LoginResponseVO> login(Mono<LoginRequestVO> requestVO) {
        requestVO.subscribe(loginRequestVO -> log.info("{}", loginRequestVO));
        LoginResponseVO responseVO = new LoginResponseVO();
        responseVO.setToken(jwtUtil.generateToken("test_user_01"));
        return Mono.just(responseVO);
    }
}
