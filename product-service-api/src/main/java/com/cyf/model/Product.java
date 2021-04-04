package com.cyf.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品实体
 *
 * @author 陈一锋
 * @date 2021/4/4 10:13
 **/
@Data
public class Product {
    private Integer id;
    private String name;
    private Integer status;
    private BigDecimal price;
    private Integer stock;
    private Integer version;


}
