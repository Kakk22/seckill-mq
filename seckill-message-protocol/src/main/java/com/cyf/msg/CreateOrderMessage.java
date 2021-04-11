package com.cyf.msg;

import lombok.Data;

import java.util.Date;

/**
 * @author 陈一锋
 * @date 2021/4/11 18:46
 **/
@Data
public class CreateOrderMessage {

    public static final String TOPIC = "SECKILL_ORDER_TOPIC";

    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 用户手机
     */
    private String userPhone;
    /**
     * 商品id
     */
    private String proId;
    /**
     * 订单价格
     */
    private String chargeMoney;
    /**
     * 下单时间
     */
    private Date chargeTime;
}
