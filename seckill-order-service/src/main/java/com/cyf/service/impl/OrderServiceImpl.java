package com.cyf.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyf.domain.Order;
import com.cyf.mapper.OrderMapper;
import com.cyf.service.OrderService;
import com.cyf.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 陈一锋
 * @date 2021/3/19 22:49
 **/
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    @Reference(version = "1.0.0")
    private ProductService productService;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order createOrder(Order order) {
        int proId = order.getProId();
        //扣减库存
        boolean result = productService.deStockByLock((long) proId);

        if (!result) {
            //失败可通知用户扣减失败
            log.info("用户扣减库存失败,proId:{},用户手机号:{}", proId, order.getUserPhone());
            return null;
        }
        //扣减成功则执行下单
        boolean saveResult = save(order);

        if (saveResult) {
            log.info("用户下单成功,下单实体:{}", order.toString());
            return order;
        } else {
            log.error("用户下单失败,下单实体:{}", order.toString());
            return null;
        }
    }

    @Override
    public void updateStatus(Integer status, Integer id) {
        orderMapper.updateStatus(status, id);
    }
}
