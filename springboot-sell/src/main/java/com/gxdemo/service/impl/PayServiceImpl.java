package com.gxdemo.service.impl;

import com.gxdemo.config.WechatAccountConfig;
import com.gxdemo.dto.OrderDTO;
import com.gxdemo.enums.ResultEnum;
import com.gxdemo.exception.SellException;
import com.gxdemo.service.OrderService;
import com.gxdemo.service.PayService;
import com.gxdemo.utils.JsonUtil;
import com.gxdemo.utils.MathUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    private static final String ORDER_NAME="测试订单";

    @Autowired
    private  WechatAccountConfig wechatAccountConfig;

    @Autowired
    private  BestPayServiceImpl bestPayService;

    @Autowired
    private OrderService orderService;

    @Override
    public PayResponse create(OrderDTO orderDTO) {

        PayRequest payRequest = new PayRequest();
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderNo());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setOpenid(orderDTO.getBuyerOpenid());

        log.info("【微信支付】发起支付，request={}", JsonUtil.toJson(payRequest));
        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("【微信支付】发起支付，response={}", JsonUtil.toJson(payResponse));

        return payResponse;
    }

    @Override
    public PayResponse notify(String notifyData) {

        //1.验证签名
        //2.订单的支付状态
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("【微信支付】异步通知，payResponse={}",payResponse);

        //3.支付金额

        //修改订单
        OrderDTO orderDTO = orderService.findOneByNo(payResponse.getOrderId());
        if(orderDTO==null){
            log.error("【微信支付】异步通知，订单不存在，orderId={}",payResponse.getOrderId());
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //判断金额是否一致
        //if(orderDTO.getOrderAmount().compareTo(new BigDecimal(payResponse.getOrderAmount()))!=0){
        if(!MathUtil.equals(orderDTO.getOrderAmount().doubleValue(),payResponse.getOrderAmount())){
            log.error("【微信支付】异步通知，订单金额不一致，orderId={}，订单金额：{}，通知金额：{}",payResponse.getOrderId(),orderDTO.getOrderAmount(),payResponse.getOrderAmount());
            throw new SellException(ResultEnum.WX_NOTIFY_MONEY_ERROR);
        }

        //修改状态
        OrderDTO payOrderDTO = orderService.paid(orderDTO);
        return payResponse;
    }


}
