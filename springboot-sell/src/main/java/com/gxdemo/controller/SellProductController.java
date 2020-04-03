package com.gxdemo.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gxdemo.dto.OrderDTO;
import com.gxdemo.dto.ProductCategory;
import com.gxdemo.dto.ProductInfo;
import com.gxdemo.enums.ResultEnum;
import com.gxdemo.exception.SellException;
import com.gxdemo.form.ProductForm;
import com.gxdemo.service.CategoryService;
import com.gxdemo.service.OrderService;
import com.gxdemo.service.ProductService;
import com.gxdemo.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/seller/product")
@Slf4j
public class SellProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;



    @GetMapping("/list")
    public String list(@RequestParam(value = "page",defaultValue = "1") Integer page,
                       @RequestParam(value = "size",defaultValue = "10")Integer size,
                       Model model){
        IPage<ProductInfo> list = productService.findAll(page, size);
        model.addAttribute("list",list.getRecords());

        PageUtil pageUtil = new PageUtil();
        pageUtil.init((int)list.getTotal(),(int)list.getSize(),(int)list.getCurrent(),"");
        model.addAttribute("page",pageUtil);

        return "product/list";

    }

    @GetMapping("/off_sale")
    public String offSale(@RequestParam("productId") Integer productId,Model model,
                          HttpServletRequest request){

        try {
            productService.offSale(productId);
        }catch (SellException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url",request.getContextPath()+"/seller/product/list");
            return "common/error";
        }
        model.addAttribute("url",request.getContextPath()+"/seller/product/list");

        return "common/success";
    }

    @GetMapping("/on_sale")
    public String onSale(@RequestParam("productId") Integer productId,Model model,HttpServletRequest request){

        try {
            productService.onSale(productId);
        }catch (SellException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url",request.getContextPath()+"/seller/product/list");
            return "common/error";
        }
        model.addAttribute("url",request.getContextPath()+"/seller/product/list");

        return "common/success";
    }

    @GetMapping("/index")
    public String index(@RequestParam(value = "productId",required = false) Integer productId,Model model){

        if(!StringUtils.isEmpty(productId)){
            ProductInfo productInfo = productService.findOne(productId);
            model.addAttribute("product",productInfo);
        }
        //查询类目

        List<ProductCategory> productCategoryList = categoryService.list();
        model.addAttribute("categoryList",productCategoryList);
        return "product/index";
    }

    /**
     * 保存更新产品
     * @param form
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid ProductForm form, BindingResult bindingResult, Model model, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            model.addAttribute("msg",bindingResult.getFieldError().getDefaultMessage());
            model.addAttribute("url",request.getContextPath()+"/seller/product/index");
            return "common/error";
        }

        ProductInfo productInfo = new ProductInfo();
        try {
            //新增
            if(!StringUtils.isEmpty(form.getProductId())){
                 productInfo = productService.findOne(form.getProductId());
            }

            BeanUtils.copyProperties(form,productInfo);
            productService.saveOrUpdate(productInfo);
        }catch (SellException e){
            model.addAttribute("msg",e.getMessage());
            model.addAttribute("url",request.getContextPath()+"/seller/product/index");
            return "common/error";
        }

        model.addAttribute("url",request.getContextPath()+"/seller/product/list");
        return "common/success";

    }

}
