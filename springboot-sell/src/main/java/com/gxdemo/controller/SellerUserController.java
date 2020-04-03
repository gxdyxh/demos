package com.gxdemo.controller;

import com.gxdemo.constant.CookieConstant;
import com.gxdemo.constant.RedisConstant;
import com.gxdemo.dto.SellerInfo;
import com.gxdemo.enums.ResultEnum;
import com.gxdemo.service.SellerService;
import com.gxdemo.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 卖家用户相关操作
 */
@Controller
@RequestMapping("/seller")
public class SellerUserController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/login")
    public String login(){
        return "seller/login";
    }

    @PostMapping("/login")
    public String dologin(@RequestParam Map<String,Object> params, HttpServletResponse response,HttpServletRequest request, Model model){

        if(params.get("userName").toString()=="" || params.get("passWord").toString()==""){
            model.addAttribute("msg", ResultEnum.LOGIN_FILED_EMPTY.getMessage());
            model.addAttribute("url",request.getContextPath()+"/seller/login");
            return  "common/error";
        }

        SellerInfo sellerInfo = sellerService.lambdaQuery().eq(SellerInfo::getUsername, params.get("userName").toString()).one();
        if(sellerInfo==null){
            model.addAttribute("msg", ResultEnum.LOGIN_USER_NOT_FIND.getMessage());
            model.addAttribute("url",request.getContextPath()+"/seller/login");
            return  "common/error";
        }
        if(!sellerInfo.getPassword().equals(params.get("passWord").toString())){
            model.addAttribute("msg", ResultEnum.LOGIN_PASSWORD_ERROR.getMessage());
            model.addAttribute("url",request.getContextPath()+"/seller/login");
            return  "common/error";
        }

        String token  = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,token),sellerInfo.getOpenid(),RedisConstant.EXPIRE, TimeUnit.SECONDS);
        CookieUtil.set(response, CookieConstant.TOKEN,token,CookieConstant.EXPIRE);

        return "redirect:/seller/order/list" ;
    }

    @GetMapping("/logout")
    public String  logout(HttpServletRequest request,HttpServletResponse response,Model model){
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie!=null){
            redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
            CookieUtil.set(response,CookieConstant.TOKEN,null,0);

            model.addAttribute("msg", ResultEnum.LOGOUT_SUCCESS.getMessage());
            model.addAttribute("url",request.getContextPath()+"/seller/login");
            return  "common/success";
        }
        return "redirect:/seller/login";
    }

}
