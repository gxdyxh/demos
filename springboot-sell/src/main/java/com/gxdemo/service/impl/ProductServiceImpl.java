package com.gxdemo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxdemo.dao.ProductInfoMapper;
import com.gxdemo.dto.CartDTO;
import com.gxdemo.dto.ProductInfo;
import com.gxdemo.enums.ProductStatusEnum;
import com.gxdemo.enums.ResultEnum;
import com.gxdemo.exception.SellException;
import com.gxdemo.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ProductService {

    @Override
    public List<ProductInfo> findProductStatus(Integer productStatus) {
        return  this.lambdaQuery().eq(ProductInfo::getProductStatus, productStatus).list();
    }

    @Override
    public ProductInfo findOne(Integer productId) {
        return this.lambdaQuery().eq(ProductInfo::getProductId,productId).one();
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return this.lambdaQuery().eq(ProductInfo::getProductStatus, ProductStatusEnum.UP.getCode()).list();
    }

    @Override
    public IPage<ProductInfo> findAll(Integer page, Integer pagesize) {
        Page<ProductInfo> pageProductInfo = new Page<>(page, pagesize);
        IPage<ProductInfo> ipage = this.page(pageProductInfo, null);
        return ipage;
    }

    @Override
    public void incStrock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO:cartDTOList){
            ProductInfo productInfo = findOne(cartDTO.getProductId());
            if(productInfo==null){
                throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
            lambdaUpdate().eq(ProductInfo::getProductId,productInfo.getProductId()).set(ProductInfo::getProductStock,result).update();
        }
    }

    @Override
    public void decStrock(List<CartDTO> cartDTOList) {

        for (CartDTO cartDTO:cartDTOList){
            ProductInfo productInfo = findOne(cartDTO.getProductId());
            if(productInfo==null){
                throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if(result<0){
                throw  new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            lambdaUpdate().eq(ProductInfo::getProductId,productInfo.getProductId()).set(ProductInfo::getProductStock,result).update();
        }

    }

    @Override
    public ProductInfo onSale(Integer porductId) {
        ProductInfo productInfo = findOne(porductId);
        if(productInfo==null){
            throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if(productInfo.getProductStatusEnum()==ProductStatusEnum.UP){
            throw  new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        updateById(productInfo);
        return productInfo;
    }

    @Override
    public ProductInfo offSale(Integer productId) {
        ProductInfo productInfo = findOne(productId);
        if(productInfo==null){
            throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if(productInfo.getProductStatusEnum()==ProductStatusEnum.DOWN){
            throw  new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        updateById(productInfo);
        return productInfo;
    }


}
