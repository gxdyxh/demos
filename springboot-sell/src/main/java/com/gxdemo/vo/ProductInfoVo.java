package com.gxdemo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品详情
 */
@Data
public class ProductInfoVo {

    @JsonProperty("id")
    private  Integer productId;

    @JsonProperty("name")
    private  String  productName;

    @JsonProperty("price")
    private BigDecimal productPrice;

    @JsonProperty("desc")
    private  String  productDesc;

    @JsonProperty("icon")
    private  String  productIcon;


}
