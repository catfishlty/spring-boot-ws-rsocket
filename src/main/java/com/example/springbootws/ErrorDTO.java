package com.example.springbootws;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Catfish
 * @version V1.0 2020/1/3 18:23
 * @email catfish_lty@qq.com
 */
@Data
public class ErrorDTO implements Serializable {
    private Integer code;
    private String desc;
}
