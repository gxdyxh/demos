package com.gxdemo.dto;

import lombok.Data;

@Data
public class CartDTO {

    private Integer productId;

    private Integer productQuantity;

    public CartDTO(Integer productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
