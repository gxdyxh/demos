package com.gxdemo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gxdemo.dto.ProductCategory;
import com.gxdemo.exception.SellException;
import com.gxdemo.form.CategoryForm;
import com.gxdemo.service.CategoryService;
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
@RequestMapping("/seller/category")
@Slf4j
public class SellCategoryController {

    @Autowired
    CategoryService categoryService;


    @GetMapping("/list")
    public String list(@RequestParam(value = "page",defaultValue = "1") Integer page,
                       @RequestParam(value = "size",defaultValue = "10")Integer size,
                       Model model){
//        IPage<ProductCategory> list = categoryService.findList(page, size);
        List<ProductCategory> list = categoryService.list();
        model.addAttribute("list",list);
        return "category/list";

    }

    @GetMapping("/index")
    public String index(@RequestParam(value = "categoryId",required = false) Integer categoryId,Model model){

        //增加
        if(categoryId != null){
            ProductCategory categoryInfo = categoryService.lambdaQuery().eq(ProductCategory::getCategoryId, categoryId).one();
            model.addAttribute("cate",categoryInfo);
        }
        return "category/index";
    }

    /**
     * 保存更新类目
     * @param form
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid CategoryForm form, BindingResult bindingResult, Model model, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            model.addAttribute("msg",bindingResult.getFieldError().getDefaultMessage());
            model.addAttribute("url",request.getContextPath()+"/seller/category/index");
            return "common/error";
        }

        ProductCategory productCategory = new ProductCategory();
        try {
            //新增
            if(!StringUtils.isEmpty(form.getCategoryId())){
                productCategory = categoryService.lambdaQuery().eq(ProductCategory::getCategoryId, form.getCategoryId()).one();
            }

            BeanUtils.copyProperties(form,productCategory);
            categoryService.saveOrUpdate(productCategory);
        }catch (SellException e){
            model.addAttribute("msg",e.getMessage());
            model.addAttribute("url",request.getContextPath()+"/seller/category/index");
            return "common/error";
        }

        model.addAttribute("url",request.getContextPath()+"/seller/category/list");
        return "common/success";

    }

}
