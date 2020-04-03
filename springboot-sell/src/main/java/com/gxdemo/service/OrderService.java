package com.gxdemo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gxdemo.dto.OrderDTO;
import com.gxdemo.dto.OrderMaster;
import org.springframework.stereotype.Service;


public interface OrderService extends IService<OrderMaster> {

    /** 创建订单 **/
    OrderDTO create(OrderDTO orderDTO);

    /** 查询单个订单 **/
    OrderDTO findOne(Integer orderId);

    /** 查询单个订单 **/
    OrderDTO findOneByNo(String orderNo);

    /** 查询订单列表 **/
    IPage<OrderDTO> findList(String openid,Integer page,Integer pagesize);

    /** 查询订单列表 **/
    IPage<OrderDTO> findList(Integer page,Integer pagesize);

    /** 取消订单 **/
    OrderDTO cancel(OrderDTO orderDTO);

    /** 完结订单 **/
    OrderDTO finish(OrderDTO orderDTO);

    /** 支付订单 **/
    OrderDTO paid(OrderDTO orderDTO);

}
