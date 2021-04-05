package com.cyf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cyf.dto.ChargeOrderRequest;
import com.cyf.model.Order;
import com.cyf.service.ChargeOrderService;
import com.cyf.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Reference(version = "1.0.0")
    private OrderService orderService;


    @PostMapping(value = "/doCharge")
    public Object doCharge(@Validated @RequestBody ChargeOrderRequest chargeOrderRequest) {
        return chargeOrderService.chargeOrder(chargeOrderRequest);
    }


    @GetMapping(value = "/list")
    public IPage<Order> list(@RequestParam("userPhone") String userPhone,
                             //默认查询秒杀订单
                             @RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        //这里因为简略没做用户登录功能,如果有正常用户信息要在登录服务器获取或者上下文中获取
        //这里简略就直接从前端传入参数获取
        if (StringUtils.isBlank(userPhone)) {
            return null;
        }
        return orderService.list(userPhone, type, pageSize, pageNum);
    }
}
