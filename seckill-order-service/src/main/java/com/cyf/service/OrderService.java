package com.cyf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyf.domain.Order;

/**
 * @author 陈一锋
 * @date 2021/3/19 22:44
 **/
public interface OrderService extends IService<Order> {

    /**
     * 下单接口
     *
     * @param order 订单实体
     * @return 下单是否成功
     */
    boolean createOrder(Order order);
}
