package com.gxdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxdemo.dto.SellerInfo;

public interface SellerService extends IService<SellerInfo> {

    SellerInfo findByOpenid(String openid);

}
