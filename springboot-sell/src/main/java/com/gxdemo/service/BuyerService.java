package com.gxdemo.service;

import com.gxdemo.dto.OrderDTO;

public interface BuyerService {

    //查询一个订单
    OrderDTO findOrderOne(String openid, Integer orderId);

    //取消订单
    OrderDTO cancelOrder(String openid, Integer orderId);
}
