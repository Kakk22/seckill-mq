package com.cyf.enums;

import lombok.Getter;

/**
 * @author 陈一锋
 * @date 2021/4/5 16:11
 **/
@Getter
public enum OrderType {

    /**
     * 正常订单
     */
    NORMAL(0),
    /**
     * 秒杀订单
     */
    SECKILL(1);

    private final int type;

    OrderType(int type) {
        this.type = type;
    }
}
