package com.gxdemo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxdemo.dao.ProductCategoryMapper;
import com.gxdemo.dto.ProductCategory;
import com.gxdemo.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements CategoryService {
    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> ids) {
        return lambdaQuery().in(ProductCategory::getCategoryType,ids).list();
    }

    @Override
    public IPage<ProductCategory> findList(Integer page, Integer pagesize) {
        Page pageCategoryList = new Page<>(page, pagesize);
        Page pageCategory = lambdaQuery().page(pageCategoryList);
        return pageCategory;
    }
}
