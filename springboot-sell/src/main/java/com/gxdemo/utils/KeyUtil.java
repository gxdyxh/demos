package com.gxdemo.utils;

import java.util.Random;

public class KeyUtil {
    /**
     * 生成唯一主键
     * 格式：时间+随机数
     */

    public static synchronized String genUniqueKey(){

        Random random = new Random();
        System.currentTimeMillis();

        Integer a = random.nextInt(900000)+100000;
        return System.currentTimeMillis() + String.valueOf(a);

    }
}
