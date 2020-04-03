package com.gxdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxdemo.converter.OrderMaster2OrderDTOConverter;
import com.gxdemo.dao.OrderDetailMapper;
import com.gxdemo.dao.OrderMasterMapper;
import com.gxdemo.dto.*;
import com.gxdemo.enums.OrderStatusEnum;
import com.gxdemo.enums.PayStatusEnum;
import com.gxdemo.enums.ResultEnum;
import com.gxdemo.exception.SellException;
import com.gxdemo.service.OrderService;
import com.gxdemo.service.ProductService;
import com.gxdemo.service.WebSocket;
import com.gxdemo.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMasterMapper, OrderMaster> implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    @Autowired
    private WebSocket webSocket;

    @Override
    @Transactional  //添加事务
    public OrderDTO create(OrderDTO orderDTO) {

        String orderNo = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

        //List<CartDTO> cartDTOList = new ArrayList<>();

        //1.查询商品（数量，价格）
        for (OrderDetail orderDetail:orderDTO.getOrderDetailList()){
            ProductInfo productInfo = productService.getById(orderDetail.getProductId());
            if(productInfo==null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            //2.计算订单总价
            orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);

            //订单详情入库
            orderDetail.setOrderNo(orderNo);
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetailMapper.insert(orderDetail);

            //CartDTO cartDTO = new CartDTO(orderDetail.getProductId(),orderDetail.getProductQuantity());
            //cartDTOList.add(cartDTO);
        }

        //3.写入订单数据库（OrderMaster 和 orderDetail）
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderNo(orderNo);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        save(orderMaster);
        //返回订单信息
        orderDTO.setOrderId(orderMaster.getOrderId());

        //4.扣库存
        //List<CartDTO> cartDTOList = new ArrayList<>();
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e-> new CartDTO(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());
        productService.decStrock(cartDTOList);

        //发送websocket消息
        webSocket.sendMessage(String.format("您有新的订单:%s",orderMaster.getOrderId()));
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(Integer orderId) {
        OrderDTO orderDTO = new OrderDTO();

        OrderMaster orderMaster = orderMasterMapper.selectById(orderId);

        if(orderMaster==null){
            throw  new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        BeanUtils.copyProperties(orderMaster,orderDTO);

        LambdaQueryWrapper<OrderDetail> lambda = Wrappers.<OrderDetail>lambdaQuery();
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(lambda.eq(OrderDetail::getOrderNo, orderMaster.getOrderNo()));
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public OrderDTO findOneByNo(String orderNo) {
        OrderMaster orderMaster = lambdaQuery().eq(OrderMaster::getOrderNo, orderNo).one();
        return OrderMaster2OrderDTOConverter.convert(orderMaster);
    }

    @Override
    public IPage<OrderDTO> findList( String openid, Integer page, Integer pagesize) {
        Page pageOrderList = new Page<>(page, pagesize);
        //Page<OrderMaster> orderMasterPage = orderMasterMapper..selectPage(pageOrderList, null);
        Page orderMasterPage = lambdaQuery().orderByDesc(OrderMaster::getOrderId).eq(OrderMaster::getBuyerOpenid, openid).page(pageOrderList);
        orderMasterPage.setRecords(OrderMaster2OrderDTOConverter.convert(orderMasterPage.getRecords()));
        return orderMasterPage;
    }

    @Override
    public IPage<OrderDTO> findList(Integer page, Integer pagesize) {
        Page pageOrderList = new Page<>(page, pagesize);
        //Page<OrderMaster> orderMasterPage = orderMasterMapper..selectPage(pageOrderList, null);
        Page orderMasterPage = lambdaQuery().orderByDesc(OrderMaster::getOrderId).page(pageOrderList);
        orderMasterPage.setRecords(OrderMaster2OrderDTOConverter.convert(orderMasterPage.getRecords()));
        return orderMasterPage;
    }

    @Override
    @Transactional  //添加事务
    public OrderDTO cancel(OrderDTO orderDTO) {

        OrderMaster orderMaster = orderMasterMapper.selectById(orderDTO.getOrderId());
        if(orderMaster==null){
            throw  new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        if(!OrderStatusEnum.NEW.getCode().equals(orderMaster.getOrderStatus())){
            throw  new SellException(ResultEnum.ORDER_CANCEL_ERROR);
        }
        orderMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        Integer i = orderMasterMapper.updateById(orderMaster);
        if(i<1){
            throw new SellException(ResultEnum.ORDER_CANCEL_ERROR);
        }

        if(PayStatusEnum.SUCCESS.equals(orderMaster.getPayStatus())){
            //TODO 退款
        }

        BeanUtils.copyProperties(orderMaster,orderDTO);
        return orderDTO;
    }

    @Override
    public OrderDTO finish(OrderDTO orderDTO) {
        OrderMaster orderMaster = orderMasterMapper.selectById(orderDTO.getOrderId());
        if(orderMaster==null){
            throw  new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        if(!OrderStatusEnum.NEW.getCode().equals(orderMaster.getOrderStatus())){
            throw  new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        Integer i = orderMasterMapper.updateById(orderMaster);
        if(i<1){
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        BeanUtils.copyProperties(orderMaster,orderDTO);
        return orderDTO;
    }

    @Override
    public OrderDTO paid(OrderDTO orderDTO) {

        OrderMaster orderMaster = orderMasterMapper.selectById(orderDTO.getOrderId());
        if(orderMaster==null){
            throw  new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        if(!OrderStatusEnum.NEW.getCode().equals(orderMaster.getOrderStatus())){
            throw  new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        if(!PayStatusEnum.WAIT.getCode().equals(orderMaster.getPayStatus())){
            throw  new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        orderMaster.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        Integer i = orderMasterMapper.updateById(orderMaster);
        if(i<1){
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        BeanUtils.copyProperties(orderMaster,orderDTO);
        return orderDTO;
    }
}
