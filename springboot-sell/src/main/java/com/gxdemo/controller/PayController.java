package com.gxdemo.controller;

import com.gxdemo.dto.OrderDTO;
import com.gxdemo.enums.ResultEnum;
import com.gxdemo.exception.SellException;
import com.gxdemo.service.OrderService;
import com.gxdemo.service.PayService;
import com.lly835.bestpay.model.PayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public String create(@RequestParam("orderId") Integer orderId ,
                         @RequestParam("returnUrl") String returnUrl,
                         Model map){
        //1.查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if(orderDTO==null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //发起支付
        //PayResponse payResponse = payService.create(orderDTO);
        map.addAttribute("orderId","1211123123");
        map.addAttribute("returnUrl",returnUrl);
        return "pay/create";
    }

    @PostMapping("/notify")
    public String notify(@RequestBody String  notifyData) {
        payService.notify(notifyData);
        return "pay/success";
    }
}
