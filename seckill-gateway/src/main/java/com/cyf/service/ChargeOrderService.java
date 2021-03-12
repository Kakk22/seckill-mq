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
     */
    void chargeOrder(ChargeOrderRequest chargeOrderRequest);
}
