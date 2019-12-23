package com.example.springbootws.auth.req;

import lombok.Data;

/**
 * @author Catfish
 * @version V1.0 2019/12/23 16:19
 * @email catfish_lty@qq.com
 */
@Data
public class LoginRequestVO {
    private String username;
    private String password;
}
