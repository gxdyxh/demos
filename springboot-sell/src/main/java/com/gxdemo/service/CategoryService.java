package com.gxdemo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gxdemo.dto.ProductCategory;

import java.util.List;

public interface CategoryService extends IService<ProductCategory> {
       List<ProductCategory>  findByCategoryTypeIn(List<Integer> ids);

       /** 查询订单列表 **/
       IPage<ProductCategory> findList(Integer page, Integer pagesize);

}
