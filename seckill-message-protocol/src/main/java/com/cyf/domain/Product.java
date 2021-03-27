package com.cyf.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品数据库实体类
 *
 * @author 陈一锋
 * @date 2021/3/12 16:07
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
