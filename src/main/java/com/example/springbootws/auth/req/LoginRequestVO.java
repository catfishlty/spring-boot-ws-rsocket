package com.example.springbootws.auth.req;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Catfish
 * @version V1.0 2020/1/3 11:53
 * @email catfish_lty@qq.com
 */
@Data
public class LoginRequestVO implements Serializable {
    private String code;
}
