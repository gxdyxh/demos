package com.gxdemo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.gxdemo.converter.OrderForm2OrderDTOConverter;
import com.gxdemo.dto.OrderDTO;
import com.gxdemo.dto.OrderDetail;
import com.gxdemo.dto.OrderMaster;
import com.gxdemo.enums.ResultEnum;
import com.gxdemo.exception.SellException;
import com.gxdemo.form.OrderForm;
import com.gxdemo.service.BuyerService;
import com.gxdemo.service.OrderService;
import com.gxdemo.utils.ResultVoUtil;
import com.gxdemo.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    //创建订单
    @PostMapping("/create")
    public ResultVo<Map<String,Integer>> addOrder(@Valid OrderForm orderForm, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            log.error("【创建订单】参数不正确 orderForm={}",orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO =  OrderForm2OrderDTOConverter.conver(orderForm);

        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }

       // OrderDTO createResult =new OrderDTO();

        OrderDTO  createResult = orderService.create(orderDTO);

        Map<String,Integer> map = new HashMap<>();
        map.put("orderId",createResult.getOrderId());
        return ResultVoUtil.success(map);

    }
    //订单列表
    @GetMapping("/list")
    public  ResultVo<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                                        @RequestParam(value = "size",defaultValue = "10")Integer size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("【查询订单列表】openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        IPage<OrderDTO> list = orderService.findList(openid,page, size);
        return ResultVoUtil.success(list.getRecords());
    }

    // 订单详情
    @GetMapping("/detail")
    public  ResultVo<OrderDTO> list(@RequestParam("openid") String openid, @RequestParam("orderId") Integer orderId) {
        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        return ResultVoUtil.success(orderDTO);
    }

    // 取消订单
    @PostMapping("/cancel")
    public ResultVo cancel(@RequestParam("openid") String openid,
                           @RequestParam("orderId") Integer orderId) {
        buyerService.cancelOrder(openid, orderId);
        return ResultVoUtil.success();
    }

}
