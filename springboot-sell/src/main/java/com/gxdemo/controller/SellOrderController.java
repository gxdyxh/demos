package com.gxdemo.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gxdemo.dto.OrderDTO;


import com.gxdemo.enums.ResultEnum;
import com.gxdemo.exception.SellException;
import com.gxdemo.service.OrderService;
import com.gxdemo.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/seller/order")
@Slf4j
public class SellOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public String list(@RequestParam(value = "page",defaultValue = "1") Integer page,
                             @RequestParam(value = "size",defaultValue = "10")Integer size,
                             Model model){
        IPage<OrderDTO> list = orderService.findList(page, size);
        model.addAttribute("list",list.getRecords());
        PageUtil pageUtil = new PageUtil();
        pageUtil.init((int)list.getTotal(),(int)list.getSize(),(int)list.getCurrent(),"");
        model.addAttribute("page",pageUtil);
        return "order/list";

    }

    @GetMapping("/cancel")
    public String cancel(@RequestParam("orderId") Integer orderId, Model model , HttpServletRequest request){

        try {
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderService.cancel(orderDTO);

        }catch (SellException e){
            log.error("【卖家端取消订单】 发生异常{}",e);
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url",request.getContextPath()+"/seller/order/list");
            return "common/error";
        }
        model.addAttribute("msg", ResultEnum.ORDER_CANCEL_SUCCESS.getMessage());
        model.addAttribute("url",request.getContextPath()+"/seller/order/list");
        return "common/success";
    }

    @GetMapping("/finish")
    public String finish(@RequestParam("orderId") Integer orderId,Model model,HttpServletRequest request){

        try {
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderService.finish(orderDTO);
        }catch (SellException e){
             log.error("【卖家端完结订单】 发生异常{}",e);
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url",request.getContextPath()+"/seller/order/list");
            return "common/error";
        }

        model.addAttribute("msg", ResultEnum.ORDER_FINISH_SUCCESS.getMessage());
        model.addAttribute("url",request.getContextPath()+"/seller/order/list");
        return "common/success";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("orderId") Integer orderId,Model model,HttpServletRequest request){

        try {
            OrderDTO orderDTO = orderService.findOne(orderId);
            model.addAttribute("orderDTO",orderDTO);
        }catch (SellException e){
            log.error("【卖家端查询订单详情】 发生异常{}",e);
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url",request.getContextPath()+"/seller/order/list");
            return "common/error";
        }
        return "order/detail";
    }

}
