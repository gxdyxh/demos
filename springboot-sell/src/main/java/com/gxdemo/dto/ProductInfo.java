package com.gxdemo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gxdemo.enums.OrderStatusEnum;
import com.gxdemo.enums.ProductStatusEnum;
import com.gxdemo.utils.EnumUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductInfo {

    @TableId(type = IdType.AUTO)
    private  Integer productId;

    private  String productName;

    private BigDecimal productPrice;

    private  Integer productStock;

    private  String productIcon;

    private  Integer categoryType;

    private  Integer productStatus = ProductStatusEnum.UP.getCode();

    private  String productDesc;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  LocalDateTime updateTime;

    @JsonIgnore
    public ProductStatusEnum getProductStatusEnum(){
        return EnumUtil.getByCode(productStatus, ProductStatusEnum.class);
    }
}
