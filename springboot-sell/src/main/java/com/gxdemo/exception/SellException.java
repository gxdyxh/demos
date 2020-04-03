package com.gxdemo.exception;

import com.gxdemo.enums.ResultEnum;
import lombok.Getter;

@Getter
public class SellException extends RuntimeException {
    private Integer code;
    private String  msg;

    public SellException(ResultEnum resultEnum) {
       super(resultEnum.getMessage());
       this.code = resultEnum.getCode();
    }

    public SellException(Integer code, String defaultMessage) {
        super(defaultMessage);
        this.code = code;
    }
}
