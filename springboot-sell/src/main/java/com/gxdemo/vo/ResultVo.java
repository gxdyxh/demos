package com.gxdemo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 请求返回的对象
 */
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVo<T> {

    /**
     * 错误码
     */
    private  Integer code;
    /**
     * 提示信息
     */
    private String msg = "";
    /**
     * 具体数据
     */
    private T data;

}
