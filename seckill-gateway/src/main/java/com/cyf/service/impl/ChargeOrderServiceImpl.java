package com.cyf.service.impl;

import com.cyf.dto.ChargeOrderRequest;
import com.cyf.service.ChargeOrderService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @author 陈一锋
 * @date 2021/3/12 15:30
 **/
@Service
public class ChargeOrderServiceImpl implements ChargeOrderService {

    @Override
    public void chargeOrder(ChargeOrderRequest chargeOrderRequest) {

        //验证价格
        verifyPrice(chargeOrderRequest.getPrice());
        //商品校验 是否还有库存

        //预减库存

        //秒杀订单入队
    }

    private void verifyPrice(String price) {
        BigDecimal bigDecimal = new BigDecimal(price);
        if (bigDecimal.longValue() < 0) {
            throw new RuntimeException("订单价格不能小于0");
        }
    }
}
