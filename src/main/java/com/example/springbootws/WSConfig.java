package com.example.springbootws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author Catfish
 * @version V1.0 2019/12/20 3:16
 * @email catfish_lty@qq.com
 **/
//@Configuration
public class WSConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
