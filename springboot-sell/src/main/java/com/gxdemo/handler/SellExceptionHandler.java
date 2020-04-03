package com.gxdemo.handler;

import com.gxdemo.exception.SellException;
import com.gxdemo.exception.SellerAuthException;
import com.gxdemo.utils.ResultVoUtil;
import com.gxdemo.vo.ResultVo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SellExceptionHandler {
    //拦截登录异常
    @ExceptionHandler(value = SellerAuthException.class)
    //@ResponseStatus(HttpStatus.FORBIDDEN)
    public String handlerAuthException(){
        return "redirect:/seller/login";
    }


    @ExceptionHandler(value = SellException.class)
    @ResponseBody
    public ResultVo handlerSellException(SellException e){
        return ResultVoUtil.error(e.getCode(),e.getMessage());
    }

}
