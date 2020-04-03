package com.gxdemo.constant;

import lombok.Data;

/**
 * redis常量
 */
public interface RedisConstant {

    String TOKEN_PREFIX = "user_token:%s";

    Integer EXPIRE = 7200; //2小时
}
