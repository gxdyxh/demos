package com.gxdemo.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxdemo.enums.OrderStatusEnum;
import com.gxdemo.enums.PayStatusEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class OrderMaster {

    //@TableId //主键
    @TableId(type = IdType.AUTO)
    private  Integer orderId;
    private  String orderNo;
    private  String buyerName;

    private  String buyerPhone;

    private  String buyerAddress;

    private  String buyerOpenid;

    private BigDecimal orderAmount;

    private Integer orderStatus = OrderStatusEnum.NEW.getCode();

    private  Integer payStatus = PayStatusEnum.WAIT.getCode();

    @TableField( fill = FieldFill.INSERT)// 新增执行
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 新增和更新执行
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  LocalDateTime updateTime;

    private transient List<OrderDetail> orderDetailList;

}
