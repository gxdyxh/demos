package com.gxdemo.enums;
import lombok.Getter;

@Getter
public enum PayStatusEnum  implements CodeEnum {
    WAIT(0,"待支付"),
    SUCCESS(1,"已支付"),
    ;
    private  Integer code;
    private  String message;

    PayStatusEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }
}
