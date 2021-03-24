package com.cyf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyf.dao.OrderMapper;
import com.cyf.dao.ProductMapper;
import com.cyf.domain.Order;
import com.cyf.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 陈一锋
 * @date 2021/3/19 22:49
 **/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public boolean createOrder(Order order) {
        int proId = order.getProId();


        return false;
    }
}
