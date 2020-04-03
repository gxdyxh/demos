package com.gxdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxdemo.dao.SellerInfoMapper;
import com.gxdemo.dto.SellerInfo;
import com.gxdemo.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SellerServiceImpl  extends ServiceImpl<SellerInfoMapper, SellerInfo> implements SellerService {


    @Override
    public SellerInfo findByOpenid(String openid) {
        SellerInfo sellerInfo = lambdaQuery().eq(SellerInfo::getOpenid, openid).one();
        return sellerInfo;
    }
}
