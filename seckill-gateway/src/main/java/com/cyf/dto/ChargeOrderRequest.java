package com.cyf.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author 陈一锋
 * @date 2021/3/12 15:33
 **/
@Data
public class ChargeOrderRequest implements Serializable {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;
    /**
     * 价格
     */
    @NotBlank(message = "价格不能为空")
    private String price;
    /**
     * 秒杀商品id
     */
    @NotBlank(message = "商品id不能为空")
    private String productId;
}
