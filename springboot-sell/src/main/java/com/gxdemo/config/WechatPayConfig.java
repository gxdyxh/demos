package com.gxdemo.config;

import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WechatPayConfig {

    @Autowired
    private  WechatAccountConfig accountConfig;

   @Bean
    public BestPayServiceImpl bestPayService(){
       BestPayServiceImpl bestPayService = new BestPayServiceImpl();
       bestPayService.setWxPayH5Config(wxPayConfig());
        return bestPayService;
   }

   @Bean
    public WxPayH5Config wxPayConfig(){
       WxPayH5Config wxPayConfig = new WxPayH5Config();

       wxPayConfig.setAppId(accountConfig.getMpAppid());
       //wxPayConfig.setAppSecret(accountConfig.getMpAppSecret());
       wxPayConfig.setMchId(accountConfig.getMchId());
       wxPayConfig.setMchKey(accountConfig.getMchKey());
       wxPayConfig.setKeyPath(accountConfig.getKeyPath());
       wxPayConfig.setNotifyUrl(accountConfig.getNotifyUrl());
       return wxPayConfig;
   }



}
