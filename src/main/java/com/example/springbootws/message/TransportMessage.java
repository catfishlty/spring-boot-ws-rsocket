package com.example.springbootws.message;

import lombok.Data;

/**
 * @author Catfish
 * @version V1.0 2019/12/23 11:17
 * @email catfish_lty@qq.com
 */
@Data
public class TransportMessage {
    private String type;
    private Object data;
}
