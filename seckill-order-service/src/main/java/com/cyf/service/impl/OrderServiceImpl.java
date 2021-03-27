package com.cyf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyf.mapper.OrderMapper;
import com.cyf.mapper.ProductMapper;
import com.cyf.domain.Order;
import com.cyf.service.OrderService;
import com.cyf.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 陈一锋
 * @date 2021/3/19 22:49
 **/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    @Autowired
    private ProductService productService;

    @Override
    public boolean createOrder(Order order) {
        int proId = order.getProId();
        //扣减库存
        boolean result = productService.deStock((long) proId);

        //扣减成功则执行下单

        //失败可通知用户扣减失败
        return false;
    }
}
