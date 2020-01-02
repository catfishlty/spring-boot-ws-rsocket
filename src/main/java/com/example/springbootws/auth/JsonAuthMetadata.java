package com.example.springbootws.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Catfish
 * @version V1.0 2020/1/2 17:30
 * @email catfish_lty@qq.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonAuthMetadata {
    public final static String TOKEN = "token";
    private String token;
}
