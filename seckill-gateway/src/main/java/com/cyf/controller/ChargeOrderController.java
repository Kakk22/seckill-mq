package com.cyf.controller;

import com.cyf.dto.ChargeOrderRequest;
import com.cyf.service.ChargeOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 秒杀订单 平台下单接口
 *
 * @author 陈一锋
 * @date 2021/3/12 15:23
 **/
@Slf4j
@RestController
@RequestMapping("/api/order")
public class ChargeOrderController {

    @Resource
    private ChargeOrderService chargeOrderService;


    @PostMapping(value = "/doCharge")
    public Object doCharge(@Validated ChargeOrderRequest chargeOrderRequest) {
        chargeOrderService.chargeOrder(chargeOrderRequest);
        return null;
    }

}
