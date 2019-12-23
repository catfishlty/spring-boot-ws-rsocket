package com.example.springbootws.session.dto;

import java.io.Serializable;

import com.example.springbootws.message.TransportMessage;
import com.example.springbootws.utils.JacksonUtil;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.util.DefaultPayload;
import lombok.Data;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;

/**
 * @author Catfish
 * @version V1.0 2019/12/23 11:16
 * @email catfish_lty@qq.com
 */
@Data
public class SessionData implements Serializable {
    private String connId;
    private RSocket rSocket;
    private DirectProcessor<TransportMessage> processor;
    private Flux<Payload> flux;

    public static SessionData create(String connId, RSocket rSocket) {
        SessionData data = new SessionData();
        data.setConnId(connId);
        data.setRSocket(rSocket);
        data.setProcessor(DirectProcessor.create());
        data.setFlux(data.processor.map(e -> DefaultPayload.create(JacksonUtil.objectToJson(e))));
        return data;
    }
}
