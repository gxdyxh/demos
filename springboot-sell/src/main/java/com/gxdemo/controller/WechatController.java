package com.gxdemo.controller;

import com.gxdemo.config.WechatAccountConfig;
import com.gxdemo.enums.ResultEnum;
import com.gxdemo.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.net.URLEncoder;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

        @Autowired
        private WxMpService wxMpService;

        @Autowired
        private WechatAccountConfig accountConfig;

        @GetMapping("authorize")
        public String authorize(@RequestParam("returnUrl") String returnUrl){
                String url = accountConfig.getAuthUrl();

               String redirectUrl =  wxMpService.oauth2buildAuthorizationUrl(url,WxConsts.OAuth2Scope.SNSAPI_BASE, URLEncoder.encode(returnUrl));
                log.info("【微信网页授权】 获取code，result={}",redirectUrl);
                return "redirect:" + redirectUrl;
        }

        @GetMapping("/userInfo")
        public  String userInfo(@RequestParam("code") String code,
                              @RequestParam("state") String returnUrl){
                WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
                try {
                        wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
                } catch (WxErrorException e) {
                        log.error("【微信网页授权】{}",e);
                        throw  new SellException(ResultEnum.WX_MP_ERROR.getCode(),e.getError().getErrorMsg());
                }

                String openid = wxMpOAuth2AccessToken.getOpenId();
                return "redirect:"+ returnUrl + "?openid="+openid;

        }

}
