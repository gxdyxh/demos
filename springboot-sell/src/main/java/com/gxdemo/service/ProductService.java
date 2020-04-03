package com.gxdemo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gxdemo.dto.CartDTO;
import com.gxdemo.dto.ProductInfo;

import java.util.List;


public interface ProductService extends IService<ProductInfo> {

    //根据状态查找商品
    List<ProductInfo> findProductStatus(Integer productStatus);

    //查找单个商品
    ProductInfo findOne(Integer productId);

    //查询所有商家商品
    List<ProductInfo> findUpAll();


    //查询所有商品
    //https://blog.csdn.net/sinat_34338162/article/details/83543994  分页使用
    IPage<ProductInfo> findAll(Integer page, Integer pagesize);

    //加库存
    void incStrock(List<CartDTO> cartDTOList);

    //减库存
    void decStrock(List<CartDTO> cartDTOList);

    //上架
    ProductInfo onSale(Integer porductId);

    //下架
    ProductInfo offSale(Integer productId);

}
