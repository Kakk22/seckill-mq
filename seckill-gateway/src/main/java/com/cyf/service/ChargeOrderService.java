package com.cyf.service;

import com.cyf.dto.ChargeOrderRequest;

/**
 * @author 陈一锋
 * @date 2021/3/12 15:30
 **/
public interface ChargeOrderService {


    /**
     * 下单接口
     *
     * @param chargeOrderRequest 下单参数
     * @return 是否下单成功
     */
    boolean chargeOrder(ChargeOrderRequest chargeOrderRequest);

    /**
     * 秒杀订单入队
     * @param chargeOrderRequest 下单参数
     */
    void  seckillOrderEnqueue(ChargeOrderRequest chargeOrderRequest);
}