package com.cyf.msg;

import lombok.Data;

/**
 * 取消订单消息
 *
 * @author 陈一锋
 * @date 2021/4/11 17:55
 **/
@Data
public class CancelOrderMessage {

    public static final String TOPIC = "CANCEL_ORDER_TOPIC";

    private String orderSn;
}
