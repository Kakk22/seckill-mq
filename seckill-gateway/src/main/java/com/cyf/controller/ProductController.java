package com.cyf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cyf.model.Product;
import com.cyf.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 陈一锋
 * @date 2021/4/4 16:37
 **/
@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {


    @Reference(version = "1.0.0")
    private ProductService productService;

    @PostMapping(value = "/update")
    public void updateStock(){
        productService.initProduct();
    }

    @PostMapping(value = "/list")
    public IPage<Product>  list(Integer pageSize,Integer PageNum){
        return productService.list(pageSize, PageNum);
    }
}
