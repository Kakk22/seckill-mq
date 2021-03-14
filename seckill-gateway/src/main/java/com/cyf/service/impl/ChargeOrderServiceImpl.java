package com.cyf.service.impl;

import com.cyf.dto.ChargeOrderRequest;
import com.cyf.service.ChargeOrderService;
import com.cyf.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author 陈一锋
 * @date 2021/3/12 15:30
 **/
@Service
@Slf4j
public class ChargeOrderServiceImpl implements ChargeOrderService {

    @Resource
    private ProductService productService;

    @Override
    public boolean chargeOrder(ChargeOrderRequest chargeOrderRequest) {

        //验证价格
        verifyPrice(chargeOrderRequest.getPrice());
        //商品校验 预减库存
        boolean result = productService.decreaseStock(chargeOrderRequest.getProductId());

        if (!result) {
            log.warn("秒杀订单扣减库存失败,请求参数:{}", chargeOrderRequest.toString());
            return false;
        }
        //todo 秒杀订单入队

        return true;
    }

    private void verifyPrice(String price) {
        BigDecimal bigDecimal = new BigDecimal(price);
        if (bigDecimal.longValue() < 0) {
            throw new RuntimeException("订单价格不能小于0");
        }
    }
}
