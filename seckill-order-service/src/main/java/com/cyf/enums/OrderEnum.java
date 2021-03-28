package com.cyf.enums;

import lombok.Getter;

/**
 * @author 陈一锋
 * @date 2021/3/28 16:59
 **/
@Getter
public enum OrderEnum {

    /**
     * 订单状态成功
     */
    SUCCESS(0),
    /**
     * 初始化中
     */
    INIT(1),
    /**
     * 处理中
     */
    DOING(2),
    /**
     * 处理失败
     */
    FAIL(3);

    private final int status;

    OrderEnum(int status) {
        this.status = status;
    }
}
