package com.cyf.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cyf.model.Product;
import com.cyf.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 陈一锋
 * @date 2021/4/4 10:22
 **/
@Service(version = "1.0.0")
@Component
public class ProductServiceImpl implements ProductService {

    @Override
    public void initProduct() {

    }

    @Override
    public List<Product> list(Integer pageSize, Integer pageNum) {
        return null;
    }
}
