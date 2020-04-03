package com.gxdemo.form;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductForm {

    private  Integer productId;

    private  String productName;

    private BigDecimal productPrice;

    private  Integer productStock;

    private  String productIcon;

    private  Integer categoryType;


    private  String productDesc;
}
