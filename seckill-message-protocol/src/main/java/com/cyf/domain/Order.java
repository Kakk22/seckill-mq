package com.cyf.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体
 * @author 陈一锋
 * @date 2021/3/19 22:45
 **/
@Data
public class Order {

    private int id;
    private int proId;
    private String proName;
    private String orderSn;
    private int orderState;
    private String userPhone;
    private int state;
    private BigDecimal money;
    private Date chargeTime;
    private Date finishTime;
}
