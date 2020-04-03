package com.gxdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {
    private String mpAppid;
    private String mpAppSecret;

    private String mcAppAppid;
    private String mchId;
    private String mchKey;
    private String keyPath;
    private String notifyUrl;
    private String authUrl;

}
