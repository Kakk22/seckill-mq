package com.cyf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cyf.model.Order;

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
    Order createOrder(Order order);

    /**
     * 更新订单状态
     *
     * @param status 状态值
     * @param id     id
     */
    void updateStatus(Integer status, Integer id);

    /**
     * @param userPhone 用户手机
     * @param type      秒杀订单还是正常订单
     * @param pageSize  每页大小
     * @param PageNum   当前页
     * @return 用户订单列表
     */
    IPage<Order> list(String userPhone, Integer type, Integer pageSize, Integer PageNum);


    /**
     * 超时未支付取消订单接口
     *
     * @param orderSn 订单编号
     */
    void cancelOrder(String orderSn);
}
