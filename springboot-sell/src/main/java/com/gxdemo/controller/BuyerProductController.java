package com.gxdemo.controller;

import com.gxdemo.dto.ProductCategory;
import com.gxdemo.dto.ProductInfo;
import com.gxdemo.exception.SellException;
import com.gxdemo.service.CategoryService;
import com.gxdemo.service.ProductService;
import com.gxdemo.utils.ResultVoUtil;
import com.gxdemo.vo.ProductInfoVo;
import com.gxdemo.vo.ProductVo;
import com.gxdemo.vo.ResultVo;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResultVo list(){
        //1.产品列表
        List<ProductInfo> productList = productService.findUpAll();

        //2.查询类目（一次性）

        //传统方法
        //List<Integer> categoryTypeList = new ArrayList<>();
        //for (ProductInfo productInfo:productList){
        //    categoryTypeList.add(productInfo.getCategoryType());
       // }

        //精简方式  lamdba表达式
        List<Integer> categoryTypeList = productList.stream().map(e -> e.getCategoryType()).collect(Collectors.toList());

        //3.数据拼装

        List<ProductCategory> productCategoryList= categoryService.findByCategoryTypeIn(categoryTypeList);

        List<ProductVo>productVoList = new ArrayList<>();
        for (ProductCategory productCategory:productCategoryList){
            ProductVo productVo = new ProductVo();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVo> productInfoVoList = new ArrayList<>();
            for (ProductInfo productInfo:productList){
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVo productInfoVo = new ProductInfoVo();
                    BeanUtils.copyProperties(productInfo,productInfoVo);
                    productInfoVoList.add(productInfoVo);
                }
             }
            productVo.setProductInfoVoList(productInfoVoList);
            productVoList.add(productVo);
        }

        ResultVo resultVo = new ResultVo();


//        resultVo.setData(productVoList);
//        resultVo.setCode(0);
//        resultVo.setMsg("成功");
//        return resultVo;

        return ResultVoUtil.success(productVoList);

    }

    @GetMapping("/off_sale")
    public ResultVo offSale(@RequestParam("productId") Integer productId, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try {
            productService.offSale(productId);
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url",request.getContextPath()+"/seller/product/list");
            //return ResultVoUtil.error(map);
        }

        map.put("url",request.getContextPath()+"/seller/product/list");
        return  ResultVoUtil.success(map);
    }


}
