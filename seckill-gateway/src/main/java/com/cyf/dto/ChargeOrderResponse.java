package com.cyf.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author 陈一锋
 * @date 2021/3/15 22:30
 **/
@Data
public class ChargeOrderResponse implements Serializable {

    /**
     * 手机号
     */
    private String phone;
    /**
     * 价格
     */
    private String price;
    /**
     * 秒杀商品id
     */
    private String productId;
    /**
     * 订单编号
     */
    private String orderSn;

}
